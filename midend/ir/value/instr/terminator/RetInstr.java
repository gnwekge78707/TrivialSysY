package midend.ir.value.instr.terminator;

import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.instr.Instruction;

public class RetInstr extends Instruction {
    public RetInstr(Value retValue) {
        super(InstrType.RET, LLVMType.Void.getVoid(), 1);
        setOperand(0, retValue);
        setHasDst(false);
    }

    public RetInstr(Value retValue, BasicBlock parent) {
        super(InstrType.RET, LLVMType.Void.getVoid(), 1, parent);
        setHasDst(false);
        setOperand(0, retValue);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t").append("ret ");
        if (this.getOperand(0) != null) {
            sb.append(this.getOperand(0).getType())
                    .append(" ")
                    .append(this.getOperand(0).getName());
        } else {
            sb.append("void");
        }
        return sb.toString();
    }
}
