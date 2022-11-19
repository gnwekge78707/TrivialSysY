package backend.template;

import backend.MipsAssembly;
import backend.isa.MipsInstruction;
import midend.ir.Value;
import midend.ir.value.BasicBlock;
import midend.ir.value.Constant;

public class MipsBrTemplate {
    public static void mipsJTemplate(Value dstBB, MipsAssembly assembly) {
        assembly.addObjectCode(MipsInstruction.getJ(((BasicBlock) dstBB).getMipsTagString()));
    }

    public static void mipsBnezTemplate(Value src, Value thenBB, Value elseBB, MipsAssembly assembly) {
        String thenTag = ((BasicBlock) thenBB).getMipsTagString();
        String elseTag = ((BasicBlock) elseBB).getMipsTagString();
        if (src instanceof Constant) {
            assembly.addObjectCode(MipsInstruction.getJ(
                    (((Constant) src).getConstVal() != 0) ? thenTag : elseTag));
        }
        else {
            int srcReg = src.getMipsMemContex().loadToRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getBnez(srcReg, thenTag));
            assembly.addObjectCode(MipsInstruction.getJ(elseTag));
        }
    }

    public static void mipsBeqTemplate(Value src1, Value src2, Value thenBB, Value elseBB, MipsAssembly assembly) {
        String thenTag = ((BasicBlock) thenBB).getMipsTagString();
        String elseTag = ((BasicBlock) elseBB).getMipsTagString();
        if (src1 instanceof Constant && src2 instanceof Constant) {
            assembly.addObjectCode(MipsInstruction.getJ(
                    ((Constant) src1).getConstVal() == ((Constant) src2).getConstVal() ? thenTag : elseTag
            ));
        }
        else if (src1 instanceof Constant) {
            int srcReg = src2.getMipsMemContex().loadToRegister(assembly);
            MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, ((Constant) src1).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getBeq(MipsAssembly.at, srcReg, thenTag));
            assembly.addObjectCode(MipsInstruction.getJ(elseTag));
        }
        else if (src2 instanceof Constant) {
            int srcReg = src1.getMipsMemContex().loadToRegister(assembly);
            MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, ((Constant) src2).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getBeq(srcReg, MipsAssembly.at, thenTag));
            assembly.addObjectCode(MipsInstruction.getJ(elseTag));
        }
        else {
            int src1Reg = src1.getMipsMemContex().loadToRegister(assembly);
            int src2Reg = src2.getMipsMemContex().loadToRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getBeq(src1Reg, src2Reg, thenTag));
            assembly.addObjectCode(MipsInstruction.getJ(elseTag));
        }
    }

    public static void mipsBneTemplate(Value src1, Value src2, Value thenBB, Value elseBB, MipsAssembly assembly) {
        String thenTag = ((BasicBlock) thenBB).getMipsTagString();
        String elseTag = ((BasicBlock) elseBB).getMipsTagString();
        if (src1 instanceof Constant && src2 instanceof Constant) {
            assembly.addObjectCode(MipsInstruction.getJ(
                    ((Constant) src1).getConstVal() != ((Constant) src2).getConstVal() ? thenTag : elseTag
            ));
        }
        else if (src1 instanceof Constant) {
            int srcReg = src2.getMipsMemContex().loadToRegister(assembly);
            MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, ((Constant) src1).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getBne(MipsAssembly.at, srcReg, thenTag));
            assembly.addObjectCode(MipsInstruction.getJ(elseTag));
        }
        else if (src2 instanceof Constant) {
            int srcReg = src1.getMipsMemContex().loadToRegister(assembly);
            MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, ((Constant) src2).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getBne(srcReg, MipsAssembly.at, thenTag));
            assembly.addObjectCode(MipsInstruction.getJ(elseTag));
        }
        else {
            int src1Reg = src1.getMipsMemContex().loadToRegister(assembly);
            int src2Reg = src2.getMipsMemContex().loadToRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getBne(src1Reg, src2Reg, thenTag));
            assembly.addObjectCode(MipsInstruction.getJ(elseTag));
        }
    }

    public static void mipsBleTemplate(Value src1, Value src2, Value thenBB, Value elseBB, MipsAssembly assembly) {
        String thenTag = ((BasicBlock) thenBB).getMipsTagString();
        String elseTag = ((BasicBlock) elseBB).getMipsTagString();
        if (src1 instanceof Constant && src2 instanceof Constant) {
            assembly.addObjectCode(MipsInstruction.getJ(
                    ((Constant) src1).getConstVal() <= ((Constant) src2).getConstVal() ? thenTag : elseTag
            ));
        }
        else if (src1 instanceof Constant) {
            int srcReg = src2.getMipsMemContex().loadToRegister(assembly);
            MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, ((Constant) src1).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getBle(MipsAssembly.at, srcReg, thenTag));
            assembly.addObjectCode(MipsInstruction.getJ(elseTag));
        }
        else if (src2 instanceof Constant) {
            int srcReg = src1.getMipsMemContex().loadToRegister(assembly);
            MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, ((Constant) src2).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getBle(srcReg, MipsAssembly.at, thenTag));
            assembly.addObjectCode(MipsInstruction.getJ(elseTag));
        }
        else {
            int src1Reg = src1.getMipsMemContex().loadToRegister(assembly);
            int src2Reg = src2.getMipsMemContex().loadToRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getBle(src1Reg, src2Reg, thenTag));
            assembly.addObjectCode(MipsInstruction.getJ(elseTag));
        }
    }

    public static void mipsBltTemplate(Value src1, Value src2, Value thenBB, Value elseBB, MipsAssembly assembly) {
        String thenTag = ((BasicBlock) thenBB).getMipsTagString();
        String elseTag = ((BasicBlock) elseBB).getMipsTagString();
        if (src1 instanceof Constant && src2 instanceof Constant) {
            assembly.addObjectCode(MipsInstruction.getJ(
                    ((Constant) src1).getConstVal() < ((Constant) src2).getConstVal() ? thenTag : elseTag
            ));
        }
        else if (src1 instanceof Constant) {
            int srcReg = src2.getMipsMemContex().loadToRegister(assembly);
            MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, ((Constant) src1).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getBlt(MipsAssembly.at, srcReg, thenTag));
            assembly.addObjectCode(MipsInstruction.getJ(elseTag));
        }
        else if (src2 instanceof Constant) {
            int srcReg = src1.getMipsMemContex().loadToRegister(assembly);
            MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, ((Constant) src2).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getBlt(srcReg, MipsAssembly.at, thenTag));
            assembly.addObjectCode(MipsInstruction.getJ(elseTag));
        }
        else {
            int src1Reg = src1.getMipsMemContex().loadToRegister(assembly);
            int src2Reg = src2.getMipsMemContex().loadToRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getBlt(src1Reg, src2Reg, thenTag));
            assembly.addObjectCode(MipsInstruction.getJ(elseTag));
        }
    }

    public static void mipsBgeTemplate(Value src1, Value src2, Value thenBB, Value elseBB, MipsAssembly assembly) {
        String thenTag = ((BasicBlock) thenBB).getMipsTagString();
        String elseTag = ((BasicBlock) elseBB).getMipsTagString();
        if (src1 instanceof Constant && src2 instanceof Constant) {
            assembly.addObjectCode(MipsInstruction.getJ(
                    ((Constant) src1).getConstVal() >= ((Constant) src2).getConstVal() ? thenTag : elseTag
            ));
        }
        else if (src1 instanceof Constant) {
            int srcReg = src2.getMipsMemContex().loadToRegister(assembly);
            MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, ((Constant) src1).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getBge(MipsAssembly.at, srcReg, thenTag));
            assembly.addObjectCode(MipsInstruction.getJ(elseTag));
        }
        else if (src2 instanceof Constant) {
            int srcReg = src1.getMipsMemContex().loadToRegister(assembly);
            MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, ((Constant) src2).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getBge(srcReg, MipsAssembly.at, thenTag));
            assembly.addObjectCode(MipsInstruction.getJ(elseTag));
        }
        else {
            int src1Reg = src1.getMipsMemContex().loadToRegister(assembly);
            int src2Reg = src2.getMipsMemContex().loadToRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getBge(src1Reg, src2Reg, thenTag));
            assembly.addObjectCode(MipsInstruction.getJ(elseTag));
        }
    }

    public static void mipsBgtTemplate(Value src1, Value src2, Value thenBB, Value elseBB, MipsAssembly assembly) {
        String thenTag = ((BasicBlock) thenBB).getMipsTagString();
        String elseTag = ((BasicBlock) elseBB).getMipsTagString();
        if (src1 instanceof Constant && src2 instanceof Constant) {
            assembly.addObjectCode(MipsInstruction.getJ(
                    ((Constant) src1).getConstVal() > ((Constant) src2).getConstVal() ? thenTag : elseTag
            ));
        }
        else if (src1 instanceof Constant) {
            int srcReg = src2.getMipsMemContex().loadToRegister(assembly);
            MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, ((Constant) src1).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getBgt(MipsAssembly.at, srcReg, thenTag));
            assembly.addObjectCode(MipsInstruction.getJ(elseTag));
        }
        else if (src2 instanceof Constant) {
            int srcReg = src1.getMipsMemContex().loadToRegister(assembly);
            MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, ((Constant) src2).getConstVal(), assembly);
            assembly.addObjectCode(MipsInstruction.getBgt(srcReg, MipsAssembly.at, thenTag));
            assembly.addObjectCode(MipsInstruction.getJ(elseTag));
        }
        else {
            int src1Reg = src1.getMipsMemContex().loadToRegister(assembly);
            int src2Reg = src2.getMipsMemContex().loadToRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getBgt(src1Reg, src2Reg, thenTag));
            assembly.addObjectCode(MipsInstruction.getJ(elseTag));
        }
    }

}
