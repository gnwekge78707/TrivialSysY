package backend;

import backend.isa.MipsInstruction;
import driver.Config;
import midend.ir.Value;
import util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * manage mips registers and object code
 */
public class MipsAssembly {
    public static final int zero = 0;
    public static final int at = 1;
    public static final int v0 = 2;
    public static final int v1 = 3;
    public static final int a0 = 4;
    public static final int gp = 28;
    public static final int sp = 29;
    public static final int ra = 31;

    public static int[] globalRegisterPool = {
            17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 30
    };
    public static int[] localRegisterPool = {
            5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16
    };
    public static final int[] registerPool = {
            5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 30
    };

    private Value[] allocatedValueBuffer;
    private boolean[] regNeedWriteBack;
    // todo: regNeedWriteBack -> if a allocated reg needs to write back to mem

    private ArrayList<MipsInstruction> objectCode;

    public MipsAssembly() {
        objectCode = new ArrayList<>();
        allocatedValueBuffer = new Value[localRegisterPool.length];
        regNeedWriteBack = new boolean[localRegisterPool.length];
    }

    public void addObjectCode(MipsInstruction mipsInstruction) {
        this.objectCode.add(mipsInstruction);
    }

    public ArrayList<MipsInstruction> getObjectCode() {
        return objectCode;
    }

    public boolean hasBeenAllocated(Value value) {
        for (Value i : allocatedValueBuffer) {
            if (i == value) return true;
        }
        return false;
    }

    /**
     * init for new function scope (mips)
     * how to distribute local and global regs
     */
    public void initForNewFunction(int numOfGlobalRegs) {
        int numOfLocalRegs = registerPool.length - numOfGlobalRegs;
        localRegisterPool = new int[numOfLocalRegs];
        globalRegisterPool = new int[numOfGlobalRegs];
        for (int i = 0; i < registerPool.length; i++) {
            if (i < numOfLocalRegs) {
                localRegisterPool[i] = registerPool[i];
            }
            else {
                globalRegisterPool[i - numOfLocalRegs] = registerPool[i];
            }
        }
        regNeedWriteBack = new boolean[localRegisterPool.length];
        allocatedValueBuffer = new Value[localRegisterPool.length];
    }

    /**
     * allocate local register for llvmValue -> (local variable, temp variable)
     * depends on whether using conflictGraph
     * @param dst -> value to allocate
     * @return -> (if success, register allocated)
     */
    public Pair<Boolean, Integer> allocLocalReg(Value dst) {
        for (int i = 0; i < localRegisterPool.length; i++) {
            if (allocatedValueBuffer[i] != null && allocatedValueBuffer[i].equals(dst)) {
                return new Pair<>(false, localRegisterPool[i]);
            }
        }
        for (int i = 0; i < localRegisterPool.length; i++) {
            if (allocatedValueBuffer[i] == null) {
                allocatedValueBuffer[i] = dst;
                return new Pair<>(true, localRegisterPool[i]);
            }
        }
        //TODO! find LRU here
        // /分配方式可以是 OPT 也可以是 LRU  临时寄存器替换 CRT是 OPT
        //FIXME! continually call 2 allocReg, they must not be the same
        // /i.e. 一个指令两个操作数，操作数要分配寄存器，寄存器不足的时候，不能一个替换掉另一个
        int replaceIdx = -1;
        /*
        int minLastUseIdx = Integer.MAX_VALUE;
        for (int i = 0; i < localRegisterPool.length; i++) {
            assert allocatedValueBuffer[i] != null;
            if (allocatedValueBuffer[i].getMipsMemContex().getLastUseBB() == dst.getMipsMemContex().getLastUseBB() &&
                    allocatedValueBuffer[i].getMipsMemContex().getLastUseIdxInBB() == dst.getMipsMemContex().getLastUseIdxInBB()) {
                //System.out.println(allocatedValueBuffer[i].getName() + "," + allocatedValueBuffer[i].getMipsMemContex().getLastUseIdxInBB()
                //+ "...........dst idx -> " + dst.getMipsMemContex().getLastUseIdxInBB());
                continue;
            }
            int lastUseIdx = allocatedValueBuffer[i].getMipsMemContex().getLastUseIdxInBB();
            if (lastUseIdx < minLastUseIdx) {
                minLastUseIdx = lastUseIdx;
                replaceIdx = i;
            }
        }*/

        for (int i = 0; i < localRegisterPool.length; i++) {
            assert allocatedValueBuffer[i] != null;
            if (allocatedValueBuffer[i].getMipsMemContex().getLastUseIdxInBB() != dst.getMipsMemContex().getLastUseIdxInBB()) {
                replaceIdx = i;
            }
        }

        if (allocatedValueBuffer[replaceIdx] != null && regNeedWriteBack[replaceIdx]) {
            allocatedValueBuffer[replaceIdx].getMipsMemContex().writeBackMem(this);
            regNeedWriteBack[replaceIdx] = false;
        }
        allocatedValueBuffer[replaceIdx] = dst;
        return new Pair<>(true, localRegisterPool[replaceIdx]);
    }

    /**
     * 为 value 设置需要写回 mem 的标志位
     * @param value
     */
    public void setRegNeedWriteBack(Value value) {
        for (int i = 0; i < localRegisterPool.length; i++) {
            if (allocatedValueBuffer[i] != null && allocatedValueBuffer[i].equals(value)) {
                regNeedWriteBack[i] = true;
            }
        }
    }

    /**
     * init (clear) allocated reg pool for a new basic block
     */
    public void initLocalRegisters() {
        Arrays.fill(allocatedValueBuffer, null);
        Arrays.fill(regNeedWriteBack, false);
    }

    /**
     * flush out (write back reg to mem) after a basic block
     */
    public void flushLocalRegisters() {
        // TODO! probably active analysis here? just write back active variable
        for (int i = 0; i < localRegisterPool.length; i++) {
            if (!regNeedWriteBack[i]) {
                continue;
            } else {
                if (!Config.getInstance().hasOptimize(Config.Optimize.llvmMem2Reg)) {
                    // if don't have mem2reg, then all local variable are stored in memory
                    // therefore do not need to write back
                    //FIXME! here if
                    /*
                     * # 	%49 = load i32, i32* %32
                     * 		sw		 $30, 184($29)
                     * 		lw		 $30, 128($29)
                     * 		# 	%50 = call i32 @foo()
                     * 		sw		 $31, 0($29)
                     * 		addiu	 $29, $29, -4
                     * 		jal foo
                     * 		addiu	 $29, $29, 4
                     * 		lw		 $31, 0($29)
                     * 		xor		 $5, $2, $0
                     * 		# 	%51 = add i32 %49, %50
                     * 		lw		 $6, 196($29)
                     * 		addu	 $7, $6, $5
                     */
                    allocatedValueBuffer[i].getMipsMemContex().writeBackMem(this);
                } else {
                    allocatedValueBuffer[i].getMipsMemContex().writeBackMem(this);
                }
            }
        }
    }

}
