package midend.ir.value.instr.mem;

import backend.MipsAssembly;
import backend.template.MipsMemTemplate;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.instr.Instruction;

public class StoreInstr extends Instruction {
    public StoreInstr(Value pointer, Value src) {
        super(InstrType.STORE, LLVMType.Void.getVoid(), 2);
        setOperand(0, src);
        setOperand(1, pointer);
        this.setHasDst(false);
    }

    public StoreInstr(Value pointer, Value src, BasicBlock parent) {
        super(InstrType.STORE, LLVMType.Void.getVoid(), 2, parent);
        setOperand(0, src);
        setOperand(1, pointer);
        this.setHasDst(false);
    }

    public Value getPointerValue() { return getOperand(1); }

    public Value getSrcValue() { return getOperand(0); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Value op0 = getOperand(0);
        Value op1 = getOperand(1);

        sb.append("\t").append("store ")
                .append(op0.getType() + " " + op0.getName()).append(", ")
                .append(op1.getType() + " " + op1.getName());
        return sb.toString();
    }

    @Override
    public void toAssembly(MipsAssembly assembly) {
        MipsMemTemplate.mipsStoreTemplate(getSrcValue(), getPointerValue(), assembly);
        //FIXME! probably set writeBackTag -> false?
        //FIXME! also, need updateVariable after binary Instr, load, etc...
    }
}
