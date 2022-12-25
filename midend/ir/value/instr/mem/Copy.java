package midend.ir.value.instr.mem;

import backend.MipsAssembly;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.instr.Instruction;

import java.util.ArrayList;

public class Copy extends Instruction {
    private ArrayList<Value> LHS;
    private ArrayList<Value> RHS;

    public Copy(ArrayList<Value> LHS, ArrayList<Value> RHS, BasicBlock parent) {
        super(InstrType.COPY, LLVMType.Void.getVoid(), 0);
        this.LHS = LHS;
        this.RHS = RHS;
        this.getINode().insertAtEntry(parent.getInstrList());
    }

    public Copy(ArrayList<Value> LHS, ArrayList<Value> RHS) {
        super(InstrType.COPY, LLVMType.Void.getVoid(), 0);
        this.LHS = LHS;
        this.RHS = RHS;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("\tCopy ");
        int len = RHS.size();
        for (int i = 0; i < len; i++) {
            ret.append(LHS.get(i).getName()).append(" <-- ").append(RHS.get(i).getName());
            if (i < len - 1) {
                ret.append(", ");
            }
        }
        return ret.toString();
    }

    public void addToPC(Value tag, Value src) {
        this.LHS.add(tag);
        this.RHS.add(src);
    }

    public ArrayList<Value> getLHS() {
        return LHS;
    }

    public ArrayList<Value> getRHS() {
        return RHS;
    }

    @Override
    public void toAssembly(MipsAssembly assembly) {

    }
}
