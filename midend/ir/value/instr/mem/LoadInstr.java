package midend.ir.value.instr.mem;

import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.instr.Instruction;

public class LoadInstr extends Instruction {
    private Value dstVar;

    public LoadInstr(LLVMType dstType, Value pointer) {
        super(InstrType.LOAD, dstType, 1);
        setOperand(0, pointer);
    }

    public LoadInstr(LLVMType dstType, Value pointer, BasicBlock parent) {
        super(InstrType.LOAD, dstType, 1, parent);
        setOperand(0, pointer);
    }

    public LoadInstr(LLVMType dstType, Value pointer, BasicBlock parent, Value dstVar) {
        super(InstrType.LOAD, dstType, 1);
        setOperand(0, pointer);
        this.dstVar = dstVar;
    }

    public Value getPointer() { return getOperand(0); }

    public Value getDstVar() { return this.dstVar; }

    public boolean hasExplicitDstVar() {
        return this.dstVar != null;
    }

    public String getName() { return hasExplicitDstVar() ? dstVar.getName() : super.getName(); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Value op = getOperand(0);
        sb.append("\t").append(this.getName()).append(" = load ")
                .append(this.getType()).append(", ")
                .append(op.getType()).append(" ")
                .append(op.getName());
        return sb.toString();
    }
}
