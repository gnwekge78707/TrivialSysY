package midend.ir.value.instr.mem;

import backend.MipsAssembly;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.instr.Instruction;

public class AllocaInstr extends Instruction {
    private final LLVMType allocated;

    public LLVMType getAllocated() {
        return allocated;
    }

    public AllocaInstr(LLVMType allocated) {
        super(InstrType.ALLOCA, new LLVMType.Pointer(allocated), 0);
        this.allocated = allocated;
    }

    public AllocaInstr(LLVMType allocated, BasicBlock parent) {
        super(InstrType.ALLOCA, new LLVMType.Pointer(allocated), 0, parent);
        this.allocated = allocated;
    }

    private Value dstVar;

    public AllocaInstr(LLVMType allocated, BasicBlock parent, Value dstVar) {
        super(InstrType.ALLOCA, new LLVMType.Pointer(allocated), 0, parent);
        this.allocated = allocated;
        this.dstVar = dstVar;
    }

    public Value getDstVar() { return this.dstVar; }

    public boolean hasExplicitDstVar() {
        return this.dstVar != null;
    }

    public String getName() { return hasExplicitDstVar() ? dstVar.getName() : super.getName(); }

    @Override
    public String toString() {
        return "\t" + this.getName() + " = alloca " + this.getAllocated();
    }

    /**
     * note: alloca Instruction's allocated address are preCalculated
     * address are definite and constant, stored in pointer (this).mipsMemContext
     * don't need to load to register
     */
    public void toAssembly(MipsAssembly assembly) {
        if (this.getMipsMemContex().getOffset() == -1) {
            throw new Error("address hasn't been pre-calculated");
        }
    }
}
