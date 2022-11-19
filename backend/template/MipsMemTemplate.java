package backend.template;

import backend.MipsAssembly;
import backend.isa.MipsInstruction;
import midend.ir.Value;
import midend.ir.value.Constant;
import midend.ir.value.Function;
import midend.ir.value.GlobalVariable;
import midend.ir.value.instr.mem.AllocaInstr;
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
                MipsCalTemplate.mipsAddNumTemplate(MipsAssembly.at, base, addr, assembly);
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
                //fixme: has fixed bug for lw $dst, 0($base)
                MipsCalTemplate.mipsAddNumTemplate(MipsAssembly.at, base, addr, assembly);
                assembly.addObjectCode(MipsInstruction.getLw(dstReg, 0, MipsAssembly.at));
            }
        }
    }

    public static void mipsGEPTemplate(Value dst, Value basePointer, Value offset, MipsAssembly assembly) {
        //TODO
        // %dst = gep [i32 x 10], [i32 x 10]* %base, i32 0, i32 %offset  ;%base can be Alloca | GlobalVar
        // %dst = gep i32, i32* %base, i32 %offset
        // base是函数参数指针，那它本身就是计算好的地址值 (旧的栈帧里面 有效)，只要加上偏移即可
        // base是局部数组，那需要计算出 base 的绝对地址，然后加上偏移量
        int dstReg = dst.getMipsMemContex().appointRegister(assembly);
        if (basePointer instanceof Function.Param) {
            int baseReg = basePointer.getMipsMemContex().loadToRegister(assembly);
            if (offset instanceof Constant) {
                MipsCalTemplate.mipsInitNumTemplate(dstReg, ((Constant) offset).getConstVal() << 2, assembly);
            } else {
                int offsetReg = offset.getMipsMemContex().loadToRegister(assembly);
                assembly.addObjectCode(MipsInstruction.getSll(dstReg, offsetReg, 2));
            }
            assembly.addObjectCode(MipsInstruction.getAddu(dstReg, dstReg, baseReg));
        } else if (basePointer instanceof AllocaInstr || basePointer instanceof GlobalVariable) {
            if (offset instanceof Constant) {
                int addr = basePointer.getMipsMemContex().getOffset() + (((Constant) offset).getConstVal() << 2);
                MipsCalTemplate.mipsAddNumTemplate(dstReg, basePointer.getMipsMemContex().getBase(), addr, assembly);
            } else {
                int offsetReg = offset.getMipsMemContex().loadToRegister(assembly);
                int addr = basePointer.getMipsMemContex().getOffset();
                MipsCalTemplate.mipsAddNumTemplate(dstReg, basePointer.getMipsMemContex().getBase(), addr, assembly);
                assembly.addObjectCode(MipsInstruction.getSll(MipsAssembly.at, offsetReg, 2));
                assembly.addObjectCode(MipsInstruction.getAddu(dstReg, dstReg, MipsAssembly.at));
            }
        } else {
            throw new Error("gep has basePointer other than alloca, globalVar, offset");
        }
    }

}
