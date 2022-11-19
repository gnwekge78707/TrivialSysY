package midend.ir.value.instr.terminator;

import backend.MipsAssembly;
import backend.template.MipsBrTemplate;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.instr.Instruction;
import midend.ir.value.instr.binary.BinaryInstr;

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

    @Override
    public void toAssembly(MipsAssembly assembly) {
        if (getOperandNum() == 1) {
            MipsBrTemplate.mipsJTemplate(getOperand(0), assembly);
        } else if (getOperandNum() == 3) {
            Value cond = getOperand(0);
            Value thenBB = getOperand(1);
            Value elseBB = getOperand(2);
            if (cond instanceof BinaryInstr && ((BinaryInstr) cond).getInstrType().isIcmp()) {
                switch (((BinaryInstr) cond).getInstrType()) {
                    case EQ:
                        MipsBrTemplate.mipsBeqTemplate(
                                ((BinaryInstr) cond).getOperand(0), ((BinaryInstr) cond).getOperand(1),
                                thenBB, elseBB, assembly);
                        break;
                    case NE:
                        MipsBrTemplate.mipsBneTemplate(
                                ((BinaryInstr) cond).getOperand(0), ((BinaryInstr) cond).getOperand(1),
                                thenBB, elseBB, assembly);
                        break;
                    case SLE:
                        MipsBrTemplate.mipsBleTemplate(
                                ((BinaryInstr) cond).getOperand(0), ((BinaryInstr) cond).getOperand(1),
                                thenBB, elseBB, assembly);
                        break;
                    case SLT:
                        MipsBrTemplate.mipsBltTemplate(
                                ((BinaryInstr) cond).getOperand(0), ((BinaryInstr) cond).getOperand(1),
                                thenBB, elseBB, assembly);
                        break;
                    case SGE:
                        MipsBrTemplate.mipsBgeTemplate(
                                ((BinaryInstr) cond).getOperand(0), ((BinaryInstr) cond).getOperand(1),
                                thenBB, elseBB, assembly);
                        break;
                    case SGT:
                        MipsBrTemplate.mipsBgtTemplate(
                                ((BinaryInstr) cond).getOperand(0), ((BinaryInstr) cond).getOperand(1),
                                thenBB, elseBB, assembly);
                        break;
                    default:
                        break;
                }
            } else {
                MipsBrTemplate.mipsBnezTemplate(cond, thenBB, elseBB, assembly);
            }
        }
    }
}
