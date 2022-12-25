package backend.template;

import backend.MipsAssembly;
import backend.isa.MipsInstruction;
import midend.ir.Value;
import util.Pair;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class MipsMulDivOptimize {
    private static class MulExtract {
        private final int steps;
        private final int source;
        private final ArrayList<Pair<Boolean, Integer>> items;

        private MulExtract(int... values) {
            int optimizeValue = 0;
            items = new ArrayList<>();
            for (int i : values) {
                if (i >= 0) {
                    optimizeValue += 1 << i;
                    items.add(new Pair<>(true, i & Integer.MAX_VALUE));
                }
                else {
                    optimizeValue -= 1 << (i & Integer.MAX_VALUE);
                    items.add(new Pair<>(false, i & Integer.MAX_VALUE));
                }
            }
            source = optimizeValue;
            int count = items.get(0).getFirst() || items.get(0).getSecond() == 0 ? 1 : 2;
            for (int i = 1; i < items.size(); i++) {
                count += items.get(i).getSecond() == 0 ? 1 : 2;
            }
            steps = count;
        }

        private int getSteps() {
            return steps;
        }

        private int getSource() {
            return source;
        }

        private boolean isBetter() {
            int base = 6;
            if ((source & 0xffff) == 0 || (Short.MIN_VALUE <= source
                    && source <= Short.MAX_VALUE - Short.MIN_VALUE)) {
                base--;
            }
            return steps < base;
        }

        private void solve(int dst, int src, MipsAssembly assembly) {
            if (items.size() == 1) {
                assembly.addObjectCode(MipsInstruction.getSll(dst, src, items.get(0).getSecond()));
                if (!items.get(0).getFirst()) {
                    assembly.addObjectCode(MipsInstruction.getSubu(dst, MipsAssembly.zero, dst));
                }
                return;
            }
            assembly.addObjectCode(MipsInstruction.getSll(MipsAssembly.v1, src, items.get(0).getSecond()));
            if (!items.get(0).getFirst()) {
                assembly.addObjectCode(MipsInstruction.getSubu(MipsAssembly.v1, MipsAssembly.zero, MipsAssembly.v1));
            }
            for (int i = 1; i < items.size() - 1; i++) {
                if (items.get(i).getSecond() == 0) {
                    assembly.addObjectCode(items.get(i).getFirst() ?
                            MipsInstruction.getAddu(MipsAssembly.v1, MipsAssembly.v1, src) :
                            MipsInstruction.getSubu(MipsAssembly.v1, MipsAssembly.v1, src));
                }
                else {
                    assembly.addObjectCode(MipsInstruction.getSll(MipsAssembly.at, src, items.get(i).getSecond()));
                    assembly.addObjectCode(items.get(i).getFirst() ?
                            MipsInstruction.getAddu(MipsAssembly.v1, MipsAssembly.v1, MipsAssembly.at) :
                            MipsInstruction.getSubu(MipsAssembly.v1, MipsAssembly.v1, MipsAssembly.at));
                }
            }
            if (items.get(items.size() - 1).getSecond() == 0) {
                assembly.addObjectCode(items.get(items.size() - 1).getFirst() ?
                        MipsInstruction.getAddu(dst, MipsAssembly.v1, src) :
                        MipsInstruction.getSubu(dst, MipsAssembly.v1, src));
            }
            else {
                assembly.addObjectCode(MipsInstruction.getSll(
                        MipsAssembly.at, src, items.get(items.size() - 1).getSecond()
                ));
                assembly.addObjectCode(items.get(items.size() - 1).getFirst() ?
                        MipsInstruction.getAddu(dst, MipsAssembly.v1, MipsAssembly.at) :
                        MipsInstruction.getSubu(dst, MipsAssembly.v1, MipsAssembly.at));
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder("x * " + source + " = ");
            for (int i = 0; i < items.size(); i++) {
                if (i != 0 || !items.get(i).getFirst()) {
                    builder.append(items.get(i).getFirst() ? "+ " : "- ");
                }
                if (items.get(i).getSecond() == 0) {
                    builder.append("x ");
                }
                else {
                    builder.append("(x << ").append(items.get(i).getSecond()).append(") ");
                }
            }
            return builder.append("-> ").append(steps).toString();
        }
    }

    private static int shPosition;
    private static int leftOneBit;
    private static long magicValue;
    private static final HashMap<Integer, MulExtract> mulOptimizable;

    static {
        LinkedList<MulExtract> sourceList = new LinkedList<>();
        for (int i = 0; i < 32; i++) {
            sourceList.add(new MulExtract(i));
            sourceList.add(new MulExtract(i | 0x80000000));
            for (int j = 0; j < 32; j++) {
                sourceList.add(new MulExtract(i, j));
                sourceList.add(new MulExtract(i, j | 0x80000000));
                sourceList.add(new MulExtract(i | 0x80000000, j));
                sourceList.add(new MulExtract(i | 0x80000000, j | 0x80000000));
                for (int k = 0; k < 32; k++) {
                    sourceList.add(new MulExtract(i, j, k));
                    sourceList.add(new MulExtract(i, j, k | 0x80000000));
                    sourceList.add(new MulExtract(i, j | 0x80000000, k));
                    sourceList.add(new MulExtract(i, j | 0x80000000, k | 0x80000000));
                    sourceList.add(new MulExtract(i | 0x80000000, j, k));
                    sourceList.add(new MulExtract(i | 0x80000000, j, k | 0x80000000));
                    sourceList.add(new MulExtract(i | 0x80000000, j | 0x80000000, k));
                    sourceList.add(new MulExtract(i | 0x80000000, j | 0x80000000, k | 0x80000000));
                }
            }
        }
        mulOptimizable = new HashMap<>();
        sourceList.stream().filter(MulExtract::isBetter).forEach(i -> {
            if (!mulOptimizable.containsKey(i.getSource()) || i.getSteps() <
                    mulOptimizable.get(i.getSource()).getSteps()) {
                mulOptimizable.put(i.getSource(), i);
            }
        });
    }

    protected static boolean canOptimizeMul(int value) {
        return mulOptimizable.containsKey(value);
    }

    protected static void mulCalOptimize(Value dst, Value src, int value, MipsAssembly assembly) {
        int srcRegister = src.getMipsMemContex().loadToRegister(assembly);
        int dstRegister = dst.getMipsMemContex().appointRegister(assembly);
        mulOptimizable.get(value).solve(dstRegister, srcRegister, assembly);
    }

    private static void getMagicValue(BigInteger divImm) {
        leftOneBit = 0;
        while (divImm.compareTo(BigInteger.valueOf(1).shiftLeft(leftOneBit)) > 0) {
            leftOneBit++;
        }
        shPosition = leftOneBit;
        long lowValue = BigInteger.valueOf(1).shiftLeft(32 + leftOneBit).divide(divImm).longValue();
        long highValue = BigInteger.valueOf(1).shiftLeft(32 + leftOneBit).add(BigInteger.valueOf(1)
                .shiftLeft(1 + leftOneBit)).divide(divImm).longValue();
        while (highValue >> 1 > lowValue >> 1 && shPosition > 0) {
            highValue >>= 1;
            lowValue >>= 1;
            shPosition--;
        }
        magicValue = highValue;
    }

    private static void divTemplate(int dst, int src, MipsAssembly assembly) {
        if (magicValue < Integer.MAX_VALUE) {
            MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, (int) magicValue, assembly);
            assembly.addObjectCode(MipsInstruction.getMult(MipsAssembly.at, src));
            assembly.addObjectCode(MipsInstruction.getMfhi(MipsAssembly.at));
            assembly.addObjectCode(MipsInstruction.getSra(MipsAssembly.v1, MipsAssembly.at, shPosition));
        }
        else {
            MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, (int) (magicValue - (1L << 32)), assembly);
            assembly.addObjectCode(MipsInstruction.getMult(MipsAssembly.at, src));
            assembly.addObjectCode(MipsInstruction.getMfhi(MipsAssembly.at));
            assembly.addObjectCode(MipsInstruction.getAddu(MipsAssembly.v1, src, MipsAssembly.at));
            assembly.addObjectCode(MipsInstruction.getSra(MipsAssembly.v1, MipsAssembly.v1, shPosition));
        }
        assembly.addObjectCode(MipsInstruction.getSlt(MipsAssembly.at, src, MipsAssembly.zero));
        assembly.addObjectCode(MipsInstruction.getAddu(dst, MipsAssembly.v1, MipsAssembly.at));
    }

    protected static void divCalOptimize(Value dst, Value src, int val, MipsAssembly assembly) {
        getMagicValue(BigInteger.valueOf(val).abs());
        int srcRegister = src.getMipsMemContex().loadToRegister(assembly);
        int dstRegister = dst.getMipsMemContex().appointRegister(assembly);

        if (Math.abs(val) == 1) {
            MipsCalTemplate.mipsInitNumTemplate(dstRegister, srcRegister, assembly);
        }
        else if (Integer.bitCount(Math.abs(val)) == 1) {
            assembly.addObjectCode(MipsInstruction.getSra(MipsAssembly.at, srcRegister, leftOneBit - 1));

            if (leftOneBit != 32) {
                assembly.addObjectCode(MipsInstruction.getSrl(MipsAssembly.at, MipsAssembly.at, 32 - leftOneBit));
            }
            assembly.addObjectCode(MipsInstruction.getAddu(dstRegister, srcRegister, MipsAssembly.at));
            assembly.addObjectCode(MipsInstruction.getSra(dstRegister, dstRegister, leftOneBit));
        }
        else {
            divTemplate(dstRegister, srcRegister, assembly);
        }
        if (val < 0) {
            assembly.addObjectCode(MipsInstruction.getSubu(dstRegister, MipsAssembly.zero, dstRegister));
        }
    }

    protected static void modCalOptimize(Value dst, Value src, int val, MipsAssembly assembly) {
        getMagicValue(BigInteger.valueOf(val).abs());
        int srcRegister = src.getMipsMemContex().loadToRegister(assembly);
        int dstRegister = dst.getMipsMemContex().appointRegister(assembly);
        if (Math.abs(val) == 1) {
            MipsCalTemplate.mipsInitNumTemplate(dstRegister, MipsAssembly.zero, assembly);
        }
        else if (Integer.bitCount(Math.abs(val)) == 1) {
            assembly.addObjectCode(MipsInstruction.getSra(MipsAssembly.at, srcRegister, leftOneBit - 1));
            if (leftOneBit != 32) {
                assembly.addObjectCode(MipsInstruction.getSrl(MipsAssembly.at, MipsAssembly.at, 32 - leftOneBit));
            }
            assembly.addObjectCode(MipsInstruction.getAddu(MipsAssembly.at, srcRegister, MipsAssembly.at));
            assembly.addObjectCode(MipsInstruction.getSra(MipsAssembly.at, MipsAssembly.at, leftOneBit));
            if (val < 0) {
                assembly.addObjectCode(MipsInstruction.getSubu(MipsAssembly.at, MipsAssembly.zero, MipsAssembly.at));
            }
            assembly.addObjectCode(MipsInstruction.getSll(MipsAssembly.at, MipsAssembly.at, leftOneBit));
            assembly.addObjectCode(MipsInstruction.getSubu(dstRegister, srcRegister, MipsAssembly.at));
        }
        else {
            divTemplate(MipsAssembly.v1, srcRegister, assembly);
            if (val < 0) {
                assembly.addObjectCode(MipsInstruction.getSubu(MipsAssembly.v1, MipsAssembly.zero, MipsAssembly.v1));
            }
            MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, val, assembly);
            assembly.addObjectCode(MipsInstruction.getMult(MipsAssembly.at, MipsAssembly.v1));
            assembly.addObjectCode(MipsInstruction.getMflo(MipsAssembly.v1));
            assembly.addObjectCode(MipsInstruction.getSubu(dstRegister, srcRegister, MipsAssembly.v1));
        }
    }
}
