package midend.ir.value.instr.mem;

import backend.MipsAssembly;
import backend.template.MipsMemTemplate;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.instr.Instruction;

public class Move extends Instruction {
    public Move(Value dst, Value src) {
        super(InstrType.MOVE, LLVMType.Void.getVoid(), 2);
        setOperand(0, dst);
        setOperand(1, src);
    }

    public Move(Value dst, Value src, BasicBlock parent) {
        super(InstrType.MOVE, LLVMType.Void.getVoid(), 2);
        setOperand(0, dst);
        setOperand(1, src);
        this.getINode().insertAtEntry(parent.getInstrList());
    }

    public Value getDst() {
        return getOperand(0);
    }

    public Value getSrc() {
        return getOperand(1);
    }

    @Override
    public String toString() {
        String ret = "\tmove ";
        ret += getDst().getName() + " <-- " + getSrc().getName();
        return ret;
    }

    @Override
    public void toAssembly(MipsAssembly assembly) {
        Value dst = getDst();
        Value src = getSrc();
        MipsMemTemplate.mipsMoveTemplate(dst, src, assembly);
        dst.getMipsMemContex().updateMem(assembly);
    }
}
