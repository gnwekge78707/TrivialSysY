package midend.ir.value.instr.terminator;

import backend.MipsAssembly;
import backend.template.MipsFuncTemplate;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.Function;
import midend.ir.value.instr.Instruction;

public class RetInstr extends Instruction {

    public RetInstr(Value retValue, BasicBlock parent) {
        super(InstrType.RET, LLVMType.Void.getVoid(), retValue != null ? 1 : 0, parent);
        setHasDst(false);
        if (retValue != null) setOperand(0, retValue);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t").append("ret ");
        if (this.getOperandNum() > 0) {
            sb.append(this.getOperand(0).getType())
                    .append(" ")
                    .append(this.getOperand(0).getName());
        } else {
            sb.append("void");
        }
        return sb.toString();
    }

    @Override
    public void toAssembly(MipsAssembly assembly) {
        assembly.flushLocalRegisters();
        Function curFunction = this.getParent().getINode().getParent().getHolder();
        MipsFuncTemplate.mipsFuncRetTemplate(curFunction, this.getOperandNum() > 0 ? getOperand(0) : null, assembly);
        assembly.initLocalRegisters();
    }
}
