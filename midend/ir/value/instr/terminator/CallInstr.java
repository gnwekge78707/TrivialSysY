package midend.ir.value.instr.terminator;

import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.Function;
import midend.ir.value.instr.Instruction;

import java.util.ArrayList;

public class CallInstr extends Instruction {
    private Value dstVar;

    public CallInstr(Function function, ArrayList<Value> funcRParams) {
        super(InstrType.CALL, ((LLVMType.Function) function.getType()).getRetType(), funcRParams.size() + 1);
        if (this.getType() instanceof LLVMType.Void) {
            setHasDst(false);
        }
        setOperand(0, function);
        for (int i = 0; i < funcRParams.size(); i++) {
            setOperand(i + 1, funcRParams.get(i));
        }
    }

    public CallInstr(Function function, ArrayList<Value> funcRParams, BasicBlock parent) {
        super(InstrType.CALL, ((LLVMType.Function) function.getType()).getRetType(),
                funcRParams.size() + 1, parent);
        if (this.getType() instanceof LLVMType.Void) {
            setHasDst(false);
        }
        setOperand(0, function);
        for (int i = 0; i < funcRParams.size(); i++) {
            setOperand(i + 1, funcRParams.get(i));
        }
    }

    public CallInstr(Function function, ArrayList<Value> funcRParams, BasicBlock parent, Value dstVar) {
        super(InstrType.CALL, ((LLVMType.Function) function.getType()).getRetType(),
                funcRParams.size() + 1, parent);
        if (this.getType() instanceof LLVMType.Void) {
            setHasDst(false);
        }
        this.dstVar = dstVar;
        setOperand(0, function);
        for (int i = 0; i < funcRParams.size(); i++) {
            setOperand(i + 1, funcRParams.get(i));
        }
    }

    public Value getDstVar() { return this.dstVar; }

    public boolean hasExplicitDstVar() {
        return this.dstVar != null;
    }

    public String getName() { return hasExplicitDstVar() ? dstVar.getName() : super.getName(); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t");
        if (((LLVMType.Function) this.getOperand(0).getType()).getRetType() instanceof LLVMType.Void) {
            sb.append("call ");
        }
        else {
            sb.append(this.getName()).append(" = call ");
        }
        sb.append(this.getType()).append(" @").append(this.getOperand(0).getName());
        sb.append("(");
        for (int i = 1; i < this.getOperands().size(); i++) {
            sb.append(this.getOperand(i).getType()).append(" ").append(this.getOperand(i).getName());
            if (i != this.getOperands().size() - 1)
                sb.append(",");
        }
        sb.append(")");
        return sb.toString();
    }
}
