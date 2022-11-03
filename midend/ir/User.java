package midend.ir;

import midend.ir.type.LLVMType;

import java.util.ArrayList;
import java.util.Collections;

public class User extends Value {
    private ArrayList<Value> operands;
    private int operandNum;

    public User(LLVMType type, String name, int operandNum) {
        super(type, name);
        this.operandNum = operandNum;
        this.operands = new ArrayList<>(Collections.nCopies(operandNum, null));
    }

    public void setOperand(int idx, Value operand) {
        Value oldOperand = this.getOperand(idx);
        this.operands.set(idx, operand);
        if (oldOperand != null) {
            oldOperand.removeUse(this, idx);
        }
        if (operand != null) {
            operand.addUse(new Use(this, operand, idx));
        }
    }

    public Value getOperand(int idx) {
        assert 0 <= idx && idx < this.operandNum;
        return this.operands.get(idx);
    }

    public ArrayList<Value> getOperands() {
        return operands;
    }

    public int getOperandNum() {
        return operandNum;
    }



}
