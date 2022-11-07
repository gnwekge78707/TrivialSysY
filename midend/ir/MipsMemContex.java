package midend.ir;

import backend.MipsAssembly;
import backend.template.MipsCalTemplate;
import backend.template.MipsMemTemplate;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.Function;
import midend.ir.value.GlobalVariable;
import midend.ir.value.instr.mem.AllocaInstr;
import midend.ir.value.instr.mem.GEPInstr;
import util.Pair;

public class MipsMemContex {
    private int space;
    private int base;
    private int offset;
    private int register; // global reg
    private int tempRegister; // temp-local reg
    private LLVMType llvmType;
    private Value value;
    //todo: 对于(1)值类型  (2)GEP指针 (3)Param in Function，space offset 为值在 mips中的空间和地址 (字)
    //    / 对于(1)Alloca (2)GlobalVariable指针类型，space offset 为指向变量的空间和地址

    //todo for all offset, 只要不是在直接操作 MipsInstruction.getXXX, 都是字 不是字节

    //FIXME! only consider single layer pointer (no nesting pointer)
    // /只有 Alloca GlobalVariable存的是对应的空间和地址
    public MipsMemContex(Value parent) {
        this.register = this.tempRegister = this.offset = -1;
        this.value = parent;
        this.base = (parent instanceof GlobalVariable) ? 28 : 29;
        this.llvmType = parent.getType();
        space = 0;
        if (llvmType instanceof LLVMType.Int) {
            space = 1 << 2;
        } else if (llvmType instanceof LLVMType.Array) {
            space = ((LLVMType.Array) llvmType).getLength() << 2;
        } else if (llvmType instanceof LLVMType.Pointer) {
            if (parent instanceof GEPInstr || parent instanceof Function.Param) {
                space = 1 << 2;
            } else if (parent instanceof AllocaInstr || parent instanceof GlobalVariable) {
                if (((LLVMType.Pointer) llvmType).getPointedTo() instanceof LLVMType.Array) {
                    space = ((LLVMType.Array) ((LLVMType.Pointer) llvmType).getPointedTo()).getLength() << 2;
                } else {
                    space = 1 << 2;
                }
            } else {
                throw new Error("pointer type Value occur in unsupported case");
            }
        }
        //todo: function, label, void type of llvmValue will set to 0 for temp
    }

    /**
     * load LLVM_Value to mips assembly register
     *  -> get appointed register, and process load
     */
    public int loadToRegister(MipsAssembly assembly) {
        if (register > 0) {
            return register;
        }
        Pair<Boolean, Integer> allocated = assembly.allocLocalReg(value);
        tempRegister = allocated.getSecond();
        if (allocated.getFirst()) {
            MipsMemTemplate.mipsProcessLw(tempRegister, offset, base, assembly);
        }
        return tempRegister;
    }

    public void loadToRegister(MipsAssembly assembly, int dstReg) {
        if (register > 0) {
            MipsCalTemplate.mipsProcessMove(dstReg, register, assembly);
        } else if (tempRegister > 0 && assembly.hasBeenAllocated(value)) {
            MipsCalTemplate.mipsProcessMove(dstReg, tempRegister, assembly);
        } else {
            MipsMemTemplate.mipsProcessLw(dstReg, offset, base, assembly);
        }
    }

    /**
     * just appoint, DO NOT load
     */
    public int appointRegister(MipsAssembly assembly) {
        if (register > 0) {
            return register;
        }
        tempRegister = assembly.allocLocalReg(value).getSecond();
        return tempRegister;
    }

    /**
     * write back to mem in mips assembly
     */
    public void writeBackMem(MipsAssembly assembly) {
        if (register < 0) {
            MipsMemTemplate.mipsProcessSw(tempRegister, offset, base, assembly);
        }
    }

    /**
     * set need update tag to be true, will update if necessary (flush at last)
     */
    public void updateMem(MipsAssembly assembly) {
        if (register < 0) {
            assembly.setRegNeedWriteBack(value);
        }
    }


    public int getSpace() {
        return space;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public int getBase() {
        return base;
    }

    public void setRegister(int register) {
        this.register = register;
    }

    public int getRegister() {
        return register;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    private int lastUseIdxInBB;
    private BasicBlock lastUseBB;

    public void setPlaceInfo(BasicBlock lastUseBB, int lastUseIdxInBB) {
        this.lastUseBB = lastUseBB;
        this.lastUseIdxInBB = lastUseIdxInBB;
    }

    public BasicBlock getLastUseBB() {
        return lastUseBB;
    }

    public int getLastUseIdxInBB() {
        return lastUseIdxInBB;
    }
}
