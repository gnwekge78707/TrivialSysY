package midend.ir.value.instr.mem;

import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.instr.Instruction;

public class GEPInstr extends Instruction {
    private Value basePointer;
    private Value dstVar;

    private static LLVMType getPointerValueType(Value value) {
        assert value.getType() instanceof LLVMType.Pointer;
        return ((LLVMType.Pointer) value.getType()).getPointedTo();
    }

    public GEPInstr(Value basePointer, Value idx) { // one dimension array, for
        super(InstrType.GETELEMENTPTR, new LLVMType.Pointer(getPointerValueType(basePointer)), 2);
        setOperand(0, basePointer);
        setOperand(1, idx);
        this.basePointer = (basePointer instanceof GEPInstr)?
                ((GEPInstr) basePointer).basePointer : basePointer;
    }

    public GEPInstr(Value basePointer, Value idx, BasicBlock parent) { // one dimension array
        super(InstrType.GETELEMENTPTR, new LLVMType.Pointer(getPointerValueType(basePointer)), 2, parent);
        setOperand(0, basePointer);
        setOperand(1, idx);
        this.basePointer = (basePointer instanceof GEPInstr)?
                ((GEPInstr) basePointer).basePointer : basePointer;
    }

    public GEPInstr(Value basePointer, Value idx, BasicBlock parent, Value dstVar) { // one dimension array
        super(InstrType.GETELEMENTPTR, new LLVMType.Pointer(getPointerValueType(basePointer)), 2, parent);
        setOperand(0, basePointer);
        setOperand(1, idx);
        this.dstVar = dstVar;
        this.basePointer = (basePointer instanceof GEPInstr)?
                ((GEPInstr) basePointer).basePointer : basePointer;
    }

    public Value getBasePointer() {
        return basePointer;
    }

    public Value getDstVar() { return this.dstVar; }

    public boolean hasExplicitDstVar() {
        return this.dstVar != null;
    }

    public String getName() { return hasExplicitDstVar() ? dstVar.getName() : super.getName(); }

    @Override
    public String
    toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t").append(this.getName())
                .append(" = getelementptr ")
                .append(((LLVMType.Pointer) getOperand(0).getType()).getPointedTo())
                .append(", ")
                .append(getOperand(0).getType())
                .append(" ")
                .append(getOperand(0).getName());
        for (int i = 1; i < getOperandNum(); ++i) {
            sb.append(", ").append(getOperand(i).getType())
                    .append(" ").append(getOperand(i).getName());
        }
        return sb.toString();
    }
}
