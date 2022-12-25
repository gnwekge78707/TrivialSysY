package midend.ir;

import midend.ir.type.LLVMType;
import midend.ir.value.Constant;

import java.util.ArrayList;
import java.util.Collections;

public class User extends Value {
    private ArrayList<Value> operands;
    private int operandNum;

    public User(LLVMType type, String name, int operandNum) {
        super(type, name);
        this.operandNum = operandNum;
        this.operands = new ArrayList<>(Collections.nCopies(operandNum, Constant.getConst0()));
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

    /**
     * 添加新的operand，并且添加use，维护operandNum
     */
    public void addOperand(Value operand) {
        int index = this.operandNum++;
        this.operands.add(operand);
        if (operand != null) {
            operand.addUse(new Use(this, operand, index));
        }
    }

    public void removeSelfUses() {
        for (int i = 0; i < this.operandNum; ++i) {
            this.setOperand(i, null);
        }
    }

    /**
     * 删除所有operands，同时维护operands 和 num
     */
    public void removeAllOperands() {
        removeAllUses();
        this.operands.clear();
        this.operandNum = 0;
    }

    /**
     * 删除所有Use，但不删除User的operands；即operands和num不变
     */
    public void removeAllUses() {
        for (int i = 0; i < operands.size(); i++) {
            Value operand = this.getOperand(i);
            if (operand != null) {
                operand.removeUse(this, i);
            }
        }
    }

    /**
     * 将某个operand删除，同时operands长度会改变
     * 连续删除时，一定要从大到小删，否则后续rank会发生改变
     */
    public void removeOperand(int index) {
        ArrayList<Value> tmp = new ArrayList<>(operands);
        removeAllOperands();
        for (int i = 0; i < tmp.size(); i++) {
            if (i != index) {
                this.addOperand(tmp.get(i));
            }
        }
    }
}
