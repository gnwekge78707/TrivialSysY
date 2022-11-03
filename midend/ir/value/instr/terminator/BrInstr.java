package midend.ir.value.instr.terminator;

import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.instr.Instruction;

public class BrInstr extends Instruction {
    //cond
    public BrInstr(Value cond, BasicBlock trueBB, BasicBlock falseBB) {
        super(InstrType.BR, LLVMType.Label.getLabel(), 3);
        this.setOperand(0, cond);
        this.setOperand(1, trueBB);
        this.setOperand(2, falseBB);
        setHasDst(false);
    }

    public BrInstr(Value cond, BasicBlock trueBB, BasicBlock falseBB, BasicBlock parentBB) {
        super(InstrType.BR, LLVMType.Label.getLabel(), 3, parentBB);
        this.setOperand(0, cond);
        this.setOperand(1, trueBB);
        this.setOperand(2, falseBB);
        setHasDst(false);
    }

    //unCond
    public BrInstr(BasicBlock target) {
        super(InstrType.BR, LLVMType.Label.getLabel(), 1);
        this.setOperand(0, target);
        setHasDst(false);
    }

    public BrInstr(BasicBlock target, BasicBlock parentBB) {
        super(InstrType.BR, LLVMType.Label.getLabel(), 1, parentBB);
        this.setOperand(0, target);
        setHasDst(false);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t").append("br ");
        assert this.getOperands().size() == this.getOperandNum();
        if (this.getOperandNum() == 1) {
            sb.append(this.getOperand(0).getType() + " %" + this.getOperand(0).getName());
        } else {
            sb.append(this.getOperand(0).getType() + " " + getOperand(0).getName()).append(", ");
            sb.append(this.getOperand(1).getType() + " %" + getOperand(1).getName()).append(", ");
            sb.append(this.getOperand(2).getType() + " %" + getOperand(2).getName());
        }
        return sb.toString();
    }

}
