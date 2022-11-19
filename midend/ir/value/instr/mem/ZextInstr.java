package midend.ir.value.instr.mem;

import backend.MipsAssembly;
import backend.template.MipsCalTemplate;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.Constant;
import midend.ir.value.instr.Instruction;

public class ZextInstr extends Instruction {
    private Value dstVar;

    public ZextInstr(Value src, LLVMType dstType, BasicBlock parent) {
        super(InstrType.ZEXT, dstType, 1, parent);
        this.setOperand(0, src);
    }

    public ZextInstr(Value src, LLVMType dstType, BasicBlock parent, Value dstVar) {
        super(InstrType.ZEXT, dstType, 1, parent);
        this.setOperand(0, src);
        this.dstVar = dstVar;
    }

    public Value getDstVar() { return this.dstVar; }

    public boolean hasExplicitDstVar() {
        return this.dstVar != null;
    }

    public String getName() { return hasExplicitDstVar() ? dstVar.getName() : super.getName(); }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t").append(this.getName()).append(" = zext i1 ")
                .append(getOperand(0).getName())
                .append(" to i32");
        return sb.toString();
    }

    @Override
    public void toAssembly(MipsAssembly assembly) {
        //FIXME!
        if (getOperand(0) instanceof Constant) {
            MipsCalTemplate.mipsInitNumTemplate(
                    this.getMipsMemContex().appointRegister(assembly),
                    ((Constant) getOperand(0)).getConstVal(),
                    assembly);
        } else {
            this.setMipsMemContex(getOperand(0).getMipsMemContex());
        }
    }
}
