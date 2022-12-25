package backend.template;

import backend.MipsAssembly;
import backend.isa.MipsInstruction;
import driver.Config;
import midend.ir.Value;
import midend.ir.value.Constant;

public class MipsCalTemplate {
    public static void mipsAddNumTemplate(int dstReg, int srcReg, int num, MipsAssembly assembly) {
        if (Short.MIN_VALUE <= num && num <= Short.MAX_VALUE) {
            assembly.addObjectCode(MipsInstruction.getAddiu(dstReg, srcReg, num));
        }
        else {
            if (num >= 0) {
                MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, num, assembly);
                assembly.addObjectCode(MipsInstruction.getAddu(dstReg, srcReg, MipsAssembly.at));
            } else {
                MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, -num, assembly);
                assembly.addObjectCode(MipsInstruction.getSubu(dstReg, srcReg, MipsAssembly.at));
            }
        }
    }

    public static void mipsProcessMove(int dstReg, int srcReg, MipsAssembly assembly) {
        assembly.addObjectCode(MipsInstruction.getXor(dstReg, srcReg, MipsAssembly.zero));
    }

    public static void mipsInitNumTemplate(int dstReg, int num, MipsAssembly assembly) {
        if (num < 0) {
            mipsInitNumTemplate(dstReg, -num, assembly);
            assembly.addObjectCode(MipsInstruction.getSubu(dstReg, MipsAssembly.zero, dstReg));
            return;
        }

        if (Short.MIN_VALUE <= num && num <= Short.MAX_VALUE) {
            assembly.addObjectCode(MipsInstruction.getAddiu(dstReg, MipsAssembly.zero, num));
        }
        else if (0 <= num && num <= Short.MAX_VALUE - Short.MIN_VALUE) {
            assembly.addObjectCode(MipsInstruction.getOri(dstReg, MipsAssembly.zero, num));
        }
        else if ((num & 0xffff) == 0) {
            assembly.addObjectCode(MipsInstruction.getLui(dstReg, (num >> 16) & 0xffff));
        }
        else {
            assembly.addObjectCode(MipsInstruction.getLui(dstReg, (num >> 16) & 0xffff));
            assembly.addObjectCode(MipsInstruction.getOri(dstReg, dstReg, num & 0xffff));
        }
    }

    public static void mipsAddTemplate(Value dst, Value src1, Value src2, MipsAssembly assembly) {
        if (src1 instanceof Constant && src2 instanceof Constant) {
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(dstReg,
                    ((Constant) src1).getConstVal() + ((Constant) src2).getConstVal(),
                    assembly);
        }
        else if (src1 instanceof Constant) {
            int srcReg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsAddNumTemplate(dstReg, srcReg, ((Constant) src1).getConstVal(), assembly);
        }
        else if (src2 instanceof Constant) {
            int srcReg = src1.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsAddNumTemplate(dstReg, srcReg, ((Constant) src2).getConstVal(), assembly);
        }
        else {
            int src1Reg = src1.getMipsMemContex().loadToRegister(assembly);
            int src2Reg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getAddu(dstReg, src1Reg, src2Reg));
        }
    }

    public static void mipsSubTemplate(Value dst, Value src1, Value src2, MipsAssembly assembly) {
        if (src1 instanceof Constant && src2 instanceof Constant) {
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(dstReg,
                    ((Constant) src1).getConstVal() - ((Constant) src2).getConstVal(),
                    assembly);
        }
        else if (src1 instanceof Constant) {
            int srcReg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(MipsAssembly.at, ((Constant) src1).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getSubu(dstReg, MipsAssembly.at, srcReg));
        }
        else if (src2 instanceof Constant) {
            int srcReg = src1.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(MipsAssembly.at, ((Constant) src2).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getSubu(dstReg, srcReg, MipsAssembly.at));
            // FIXME!!! - addNum Of minus number may cause problem when we are initNum
            //mipsAddNumTemplate(dstReg, srcReg, -((Constant) src2).getConstVal(), assembly);
        }
        else {
            int src1Reg = src1.getMipsMemContex().loadToRegister(assembly);
            int src2Reg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getSubu(dstReg, src1Reg, src2Reg));
        }
    }

    public static void mipsMulTemplate(Value dst, Value src1, Value src2, MipsAssembly assembly) {
        if (src1 instanceof Constant && src2 instanceof Constant) {
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(dstReg,
                    ((Constant) src1).getConstVal() * ((Constant) src2).getConstVal(),
                    assembly);
        }
        else if (src1 instanceof Constant) {
            if (Config.getInstance().hasOptimize(Config.Optimize.mipsMulDivOptimize) &&
                    MipsMulDivOptimize.canOptimizeMul(((Constant) src1).getConstVal())) {
                MipsMulDivOptimize.mulCalOptimize(dst, src2, ((Constant) src1).getConstVal(), assembly);
            } else {
                int srcReg = src2.getMipsMemContex().loadToRegister(assembly);
                int dstReg = dst.getMipsMemContex().appointRegister(assembly);
                mipsInitNumTemplate(MipsAssembly.at, ((Constant) src1).getConstVal(), assembly);
                assembly.addObjectCode(MipsInstruction.getMult(srcReg, MipsAssembly.at));
                assembly.addObjectCode(MipsInstruction.getMflo(dstReg));
            }
        }
        else if (src2 instanceof Constant) {
            if (Config.getInstance().hasOptimize(Config.Optimize.mipsMulDivOptimize) &&
                    MipsMulDivOptimize.canOptimizeMul(((Constant) src2).getConstVal())) {
                MipsMulDivOptimize.mulCalOptimize(dst, src1, ((Constant) src2).getConstVal(), assembly);
            } else {
                int srcReg = src1.getMipsMemContex().loadToRegister(assembly);
                int dstReg = dst.getMipsMemContex().appointRegister(assembly);
                mipsInitNumTemplate(MipsAssembly.at, ((Constant) src2).getConstVal(), assembly);
                assembly.addObjectCode(MipsInstruction.getMult(srcReg, MipsAssembly.at));
                assembly.addObjectCode(MipsInstruction.getMflo(dstReg));
            }
        }
        else {
            int src1Reg = src1.getMipsMemContex().loadToRegister(assembly);
            int src2Reg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getMult(src1Reg, src2Reg));
            assembly.addObjectCode(MipsInstruction.getMflo(dstReg));
        }
    }

    public static void mipsDivTemplate(Value dst, Value src1, Value src2, MipsAssembly assembly) {
        if (src1 instanceof Constant && src2 instanceof Constant) {
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(dstReg,
                    ((Constant) src1).getConstVal() / ((Constant) src2).getConstVal(),
                    assembly);
        }
        else if (src1 instanceof Constant) {
            int srcReg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(MipsAssembly.at, ((Constant) src1).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getDiv(MipsAssembly.at, srcReg));
            assembly.addObjectCode(MipsInstruction.getMflo(dstReg));
        }
        else if (src2 instanceof Constant) {
            if (Config.getInstance().hasOptimize(Config.Optimize.mipsMulDivOptimize)) {
                MipsMulDivOptimize.divCalOptimize(dst, src1, ((Constant) src2).getConstVal(), assembly);
            } else {
                int srcReg = src1.getMipsMemContex().loadToRegister(assembly);
                int dstReg = dst.getMipsMemContex().appointRegister(assembly);
                mipsInitNumTemplate(MipsAssembly.at, ((Constant) src2).getConstVal(), assembly);
                assembly.addObjectCode(MipsInstruction.getDiv(srcReg, MipsAssembly.at));
                assembly.addObjectCode(MipsInstruction.getMflo(dstReg));
            }
        }
        else {
            int src1Reg = src1.getMipsMemContex().loadToRegister(assembly);
            int src2Reg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getDiv(src1Reg, src2Reg));
            assembly.addObjectCode(MipsInstruction.getMflo(dstReg));
        }
    }

    public static void mipsModTemplate(Value dst, Value src1, Value src2, MipsAssembly assembly) {
        if (src1 instanceof Constant && src2 instanceof Constant) {
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(dstReg,
                    ((Constant) src1).getConstVal() % ((Constant) src2).getConstVal(),
                    assembly);
        }
        else if (src1 instanceof Constant) {
            int srcReg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(MipsAssembly.at, ((Constant) src1).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getDiv(MipsAssembly.at, srcReg));
            assembly.addObjectCode(MipsInstruction.getMfhi(dstReg));
        }
        else if (src2 instanceof Constant) {
            if (Config.getInstance().hasOptimize(Config.Optimize.mipsMulDivOptimize)) {
                MipsMulDivOptimize.modCalOptimize(dst, src1, ((Constant) src2).getConstVal(), assembly);
            } else {
                int srcReg = src1.getMipsMemContex().loadToRegister(assembly);
                int dstReg = dst.getMipsMemContex().appointRegister(assembly);
                mipsInitNumTemplate(MipsAssembly.at, ((Constant) src2).getConstVal(), assembly);
                assembly.addObjectCode(MipsInstruction.getDiv(srcReg, MipsAssembly.at));
                assembly.addObjectCode(MipsInstruction.getMfhi(dstReg));
            }
        }
        else {
            int src1Reg = src1.getMipsMemContex().loadToRegister(assembly);
            int src2Reg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getDiv(src1Reg, src2Reg));
            assembly.addObjectCode(MipsInstruction.getMfhi(dstReg));
        }
    }

    //todo ______________________________________________________________________________________________________logical

    public static void mipsSleTemplate(Value dst, Value src1, Value src2, MipsAssembly assembly) {
        if (src1 instanceof Constant && src2 instanceof Constant) {
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(dstReg,
                    ((Constant) src1).getConstVal() <= ((Constant) src2).getConstVal() ? 1 : 0,
                    assembly);
        }
        else if (src1 instanceof Constant) {
            int srcReg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(MipsAssembly.at, ((Constant) src1).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getSle(dstReg, MipsAssembly.at, srcReg));
        }
        else if (src2 instanceof Constant) {
            int srcReg = src1.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(MipsAssembly.at, ((Constant) src2).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getSle(dstReg, srcReg, MipsAssembly.at));
        }
        else {
            int src1Reg = src1.getMipsMemContex().loadToRegister(assembly);
            int src2Reg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getSle(dstReg, src1Reg, src2Reg));
        }
    }

    public static void mipsSltTemplate(Value dst, Value src1, Value src2, MipsAssembly assembly) {
        if (src1 instanceof Constant && src2 instanceof Constant) {
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(dstReg,
                    ((Constant) src1).getConstVal() < ((Constant) src2).getConstVal() ? 1 : 0,
                    assembly);
        }
        else if (src1 instanceof Constant) {
            int srcReg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(MipsAssembly.at, ((Constant) src1).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getSlt(dstReg, MipsAssembly.at, srcReg));
        }
        else if (src2 instanceof Constant) {
            int srcReg = src1.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            int constVal = ((Constant) src2).getConstVal();
            if (Short.MIN_VALUE <= constVal && constVal <= Short.MAX_VALUE) {
                assembly.addObjectCode(MipsInstruction.getSlti(dstReg, srcReg, constVal));
            } else {
                mipsInitNumTemplate(MipsAssembly.at, ((Constant) src2).getConstVal(), assembly);
                assembly.addObjectCode(MipsInstruction.getSlt(dstReg, srcReg, MipsAssembly.at));
            }
        }
        else {
            int src1Reg = src1.getMipsMemContex().loadToRegister(assembly);
            int src2Reg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getSlt(dstReg, src1Reg, src2Reg));
        }
    }

    public static void mipsSgeTemplate(Value dst, Value src1, Value src2, MipsAssembly assembly) {
        if (src1 instanceof Constant && src2 instanceof Constant) {
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(dstReg,
                    ((Constant) src1).getConstVal() >= ((Constant) src2).getConstVal() ? 1 : 0,
                    assembly);
        }
        else if (src1 instanceof Constant) {
            int srcReg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(MipsAssembly.at, ((Constant) src1).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getSge(dstReg, MipsAssembly.at, srcReg));
        }
        else if (src2 instanceof Constant) {
            int srcReg = src1.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(MipsAssembly.at, ((Constant) src2).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getSge(dstReg, srcReg, MipsAssembly.at));
        }
        else {
            int src1Reg = src1.getMipsMemContex().loadToRegister(assembly);
            int src2Reg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getSge(dstReg, src1Reg, src2Reg));
        }
    }

    public static void mipsSgtTemplate(Value dst, Value src1, Value src2, MipsAssembly assembly) {
        if (src1 instanceof Constant && src2 instanceof Constant) {
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(dstReg,
                    ((Constant) src1).getConstVal() > ((Constant) src2).getConstVal() ? 1 : 0,
                    assembly);
        }
        else if (src1 instanceof Constant) {
            int srcReg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            int constVal = ((Constant) src1).getConstVal();
            if (Short.MIN_VALUE <= constVal && constVal <= Short.MAX_VALUE) {
                assembly.addObjectCode(MipsInstruction.getSlti(dstReg, srcReg, constVal));
            } else {
                mipsInitNumTemplate(MipsAssembly.at, ((Constant) src1).getConstVal(), assembly);
                assembly.addObjectCode(MipsInstruction.getSgt(dstReg, MipsAssembly.at, srcReg));
            }
        }
        else if (src2 instanceof Constant) {
            int srcReg = src1.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(MipsAssembly.at, ((Constant) src2).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getSgt(dstReg, srcReg, MipsAssembly.at));
        }
        else {
            int src1Reg = src1.getMipsMemContex().loadToRegister(assembly);
            int src2Reg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getSgt(dstReg, src1Reg, src2Reg));
        }
    }

    public static void mipsSneTemplate(Value dst, Value src1, Value src2, MipsAssembly assembly) {
        if (src1 instanceof Constant && src2 instanceof Constant) {
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(dstReg,
                    ((Constant) src1).getConstVal() != ((Constant) src2).getConstVal() ? 1 : 0,
                    assembly);
        }
        else if (src1 instanceof Constant) {
            int srcReg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(MipsAssembly.at, ((Constant) src1).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getSne(dstReg, MipsAssembly.at, srcReg));
        }
        else if (src2 instanceof Constant) {
            int srcReg = src1.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(MipsAssembly.at, ((Constant) src2).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getSne(dstReg, srcReg, MipsAssembly.at));
        }
        else {
            int src1Reg = src1.getMipsMemContex().loadToRegister(assembly);
            int src2Reg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getSne(dstReg, src1Reg, src2Reg));
        }
    }

    public static void mipsSeqTemplate(Value dst, Value src1, Value src2, MipsAssembly assembly) {
        if (src1 instanceof Constant && src2 instanceof Constant) {
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(dstReg,
                    ((Constant) src1).getConstVal() == ((Constant) src2).getConstVal() ? 1 : 0,
                    assembly);
        }
        else if (src1 instanceof Constant) {
            int srcReg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(MipsAssembly.at, ((Constant) src1).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getSeq(dstReg, MipsAssembly.at, srcReg));
        }
        else if (src2 instanceof Constant) {
            int srcReg = src1.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            mipsInitNumTemplate(MipsAssembly.at, ((Constant) src2).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getSeq(dstReg, srcReg, MipsAssembly.at));
        }
        else {
            int src1Reg = src1.getMipsMemContex().loadToRegister(assembly);
            int src2Reg = src2.getMipsMemContex().loadToRegister(assembly);
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getSeq(dstReg, src1Reg, src2Reg));
        }
    }

}
