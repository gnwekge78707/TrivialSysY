package backend.template;

import backend.MipsAssembly;
import backend.isa.MipsInstruction;
import midend.ir.Value;
import midend.ir.value.Constant;
import midend.ir.value.Function;
import midend.ir.value.instr.mem.GEPInstr;

/**
 * mipsXXXTemplate -> called in mir's toAssembly
 * also called in LLVM_Value's MipsMemContex
 * any direct call to MipsInstruction must goes through template
 *
 * @methods
 *  mips process xxx  -> process means directly calls
 *  mips xxx template -> template means LLVM IR to mips
 */
public class MipsMemTemplate {

    public static void mipsProcessLw(int dstReg, int offset, int base, MipsAssembly assembly) {
        assembly.addObjectCode(MipsInstruction.getLw(dstReg, offset, base));
    }

    //FIXME! pointer can be instance of -> GlobalVar | GEP | Alloca | Param
    public static void mipsProcessSw(int dstReg, int offset, int base, MipsAssembly assembly) {
        assembly.addObjectCode(MipsInstruction.getSw(dstReg, offset, base));
    }

    public static void mipsStoreTemplate(Value src, Value pointer, MipsAssembly assembly) {
        if (src instanceof Constant) {
            MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, ((Constant) src).getConstVal(), assembly);
        }
        int srcReg = src instanceof Constant ? MipsAssembly.at : src.getMipsMemContex().loadToRegister(assembly);
        //FIXME: pointer is what ? for array, pointer may not be calculated
        if (pointer instanceof GEPInstr || pointer instanceof Function.Param) {
            int addrReg = pointer.getMipsMemContex().loadToRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getSw(srcReg, 0, addrReg));
        } else {
            int addr = pointer.getMipsMemContex().getOffset();
            int base = pointer.getMipsMemContex().getBase();
            if (Short.MIN_VALUE <= addr && addr <= Short.MAX_VALUE) {
                assembly.addObjectCode(MipsInstruction.getSw(srcReg, addr, base));
            } else {
                MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, addr + base, assembly);
                assembly.addObjectCode(MipsInstruction.getSw(srcReg, 0, MipsAssembly.at));
            }
        }
    }

    public static void mipsLoadTemplate(Value dst, Value pointer, MipsAssembly assembly) {
        int dstReg = dst.getMipsMemContex().appointRegister(assembly);
        if (pointer instanceof GEPInstr || pointer instanceof Function.Param) {
            int addrReg = pointer.getMipsMemContex().loadToRegister(assembly);
            assembly.addObjectCode(MipsInstruction.getLw(dstReg, 0, addrReg));
        } else {
            int addr = pointer.getMipsMemContex().getOffset();
            int base = pointer.getMipsMemContex().getBase();
            if (Short.MIN_VALUE <= addr && addr <= Short.MAX_VALUE) {
                assembly.addObjectCode(MipsInstruction.getLw(dstReg, addr, base));
            } else {
                MipsCalTemplate.mipsInitNumTemplate(MipsAssembly.at, addr + base, assembly);
                assembly.addObjectCode(MipsInstruction.getLw(dstReg, 0, MipsAssembly.at));
            }
        }
    }

}
