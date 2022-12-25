package midend.ir.value.instr.mem;

import backend.MipsAssembly;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.instr.Instruction;

import java.util.ArrayList;

public class PhiInstr extends Instruction {
    public PhiInstr(LLVMType llvmType, int inCnt) {
        super(InstrType.PHI, llvmType, inCnt);
    }

    public PhiInstr(LLVMType llvmType, BasicBlock parent) {
        super(InstrType.PHI, llvmType, parent.getPredecessors().size());
        this.getINode().insertAtEntry(parent.getInstrList());
    }

    public void removeIncomingValue(int index) {
        ArrayList<Value> operands = new ArrayList<>(this.getOperands());
        this.removeAllOperands();
        for (int i = 0; i < operands.size(); ++i) {
            if (i == index) {
                continue;
            }
            this.addOperand(operands.get(i));
        }
    }

    public String getName() { return super.getName(); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t").append(this.getName()).append(" = phi ").append(this.getType());
        BasicBlock parent = this.getINode().getParent().getHolder().getINode().getValue();
        for (int i = 0; i < this.getOperandNum(); ++i) {
            sb.append(" [ ").append(this.getOperand(i).getName()).append(", %").append(parent.getPredecessors().get(i).getName()).append(" ]");
            if (i != this.getOperandNum() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    @Override
    public void toAssembly(MipsAssembly assembly) {
    }
}
