package backend.template;

import backend.MipsAssembly;
import backend.isa.MipsInstruction;
import midend.ir.Value;
import midend.ir.value.Constant;
import midend.ir.value.Function;

import java.util.ArrayList;

public class MipsFuncTemplate {
    public static void mipsFuncCallTemplate(
            Function function, ArrayList<Value> funcRParams, MipsAssembly assembly) {
        if (function.getName().equals("main")) {
            if (function.getMipsMemContex().getSpace() != 0) {
                MipsCalTemplate.mipsAddNumTemplate(MipsAssembly.sp, MipsAssembly.sp,
                        -function.getMipsMemContex().getSpace(), assembly);
            }
            //TODO! jal to main or not?
        } else {
            int space = function.getMipsMemContex().getSpace();
            assembly.addObjectCode(MipsInstruction.getSw(MipsAssembly.ra, 0, MipsAssembly.sp));
            //TODO: must alloc space for func params
            for (int i = 0; i < funcRParams.size(); i++) {
                if (funcRParams.get(i) instanceof Constant) {
                    MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at,
                            ((Constant) funcRParams.get(i)).getConstVal(), assembly);
                }
                int reg = funcRParams.get(i) instanceof Constant ?
                        MipsAssembly.at : funcRParams.get(i).getMipsMemContex().loadToRegister(assembly);
                assembly.addObjectCode(MipsInstruction.getSw(reg, -space + ((i + 1) << 2), MipsAssembly.sp));
            }
            funcRParams.clear();
            MipsCalTemplate.mipsAddNumTemplate(MipsAssembly.sp, MipsAssembly.sp, -space, assembly);
            assembly.addObjectCode(MipsInstruction.getJaL(function.getMipsFuncName()));
            MipsCalTemplate.mipsAddNumTemplate(MipsAssembly.sp, MipsAssembly.sp, space, assembly);
            assembly.addObjectCode(MipsInstruction.getLw(MipsAssembly.ra, 0, MipsAssembly.sp));
        }
    }

    public static void mipsFuncRetTemplate(Function function, Value retValue, MipsAssembly assembly) {
        if (function.getName().equals("main")) {
            MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.v0, 10, assembly);
            assembly.addObjectCode(MipsInstruction.getSyscall());
        } else if (retValue == null) {
            assembly.addObjectCode(MipsInstruction.getJr(MipsAssembly.ra));
        } else {
            if (retValue instanceof Constant) {
                MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.v0, ((Constant) retValue).getConstVal(), assembly);
            } else {
                retValue.getMipsMemContex().loadToRegister(assembly, MipsAssembly.v0);
            }
            assembly.addObjectCode(MipsInstruction.getJr(MipsAssembly.ra));
        }
    }

    public static void mipsPrintNumTemplate(Value src, MipsAssembly assembly) {
        if (src instanceof Constant) {
            MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.a0, ((Constant) src).getConstVal(), assembly);
        } else {
            src.getMipsMemContex().loadToRegister(assembly, MipsAssembly.a0);
        }
        MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.v0, 1, assembly);
        assembly.addObjectCode(MipsInstruction.getSyscall());
    }

    public static void mipsPrintStrTemplate(int strIdx, MipsAssembly assembly) {
        assembly.addObjectCode(MipsInstruction.getLa(MipsAssembly.a0, "str_" + strIdx));
        MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.v0, 4, assembly);
        assembly.addObjectCode(MipsInstruction.getSyscall());
    }

    public static void mipsGetIntTemplate(Value dst, MipsAssembly assembly) {
        int dstReg = dst.getMipsMemContex().appointRegister(assembly);
        MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.v0, 5, assembly);
        assembly.addObjectCode(MipsInstruction.getSyscall());
        MipsCalTemplate.mipsProcessMove(dstReg, MipsAssembly.v0, assembly);
    }
}
