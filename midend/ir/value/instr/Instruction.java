package midend.ir.value.instr;

import backend.MipsAssembly;
import midend.ir.User;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.Constant;
import util.IList;

//TODO: 2 scenario: hasDst or singleDst(auto)
public abstract class Instruction extends User {
    private IList.INode<Instruction, BasicBlock> iNode;
    private InstrType instrType; //instr is a kind of LLVMType
    private boolean hasDst = true; //TODO: including implicit and explicit (default tobe implicit)

    public Instruction(InstrType instrType, LLVMType dstType, int operandNum) {
        super(dstType, "", operandNum);
        this.instrType = instrType;
        this.iNode = new IList.INode<>(this);
    }

    public Instruction(InstrType instrType, LLVMType dstType, int operandNum, BasicBlock parent) {
        super(dstType, "", operandNum);
        this.instrType = instrType;
        this.iNode = new IList.INode<>(this);
        if (parent != null) {
            this.iNode.insertAtEnd(parent.getInstrList());
        }
    }

    public void setParent(BasicBlock parent) {
        if (iNode.getParent() != null) {
            iNode.removeSelf();
        }
        if (parent != null) {
            this.iNode.insertAtEnd(parent.getInstrList());
        }
    }

    public void setPlaceInfo(BasicBlock basicBlock, int lastUseIdxInBB) {
        this.getMipsMemContex().setPlaceInfo(basicBlock, lastUseIdxInBB);
        int idx = lastUseIdxInBB;
        if (getInstrType() == InstrType.CALL) {
            for (Value value : getOperands()) {
                idx++;
                value.getMipsMemContex().setPlaceInfo(basicBlock, idx);
            }
        } else {
            for (Value value : getOperands()) {
                value.getMipsMemContex().setPlaceInfo(basicBlock, lastUseIdxInBB);
            }
        }
    }

    public void setInstrType(InstrType instrType) {
        this.instrType = instrType;
    }

    public InstrType getInstrType() {
        return instrType;
    }

    public LLVMType getDstType() { return this.getType(); }

    public IList.INode<Instruction, BasicBlock> getINode() {
        return iNode;
    }

    public BasicBlock getParent() {
        return iNode.getParent().getHolder();
    }

    public boolean hasDst() {
        return hasDst;
    }

    public void setHasDst(boolean hasDst) {
        this.hasDst = hasDst;
    }

    public boolean thisEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instruction that = (Instruction) o;
        if (this.instrType != that.instrType || this.getOperandNum() != that.getOperandNum()) {
            return false;
        }
        for (int i = 0; i < this.getOperandNum(); ++i) {
            if (this.getOperand(i) instanceof Constant) {
                if (!this.getOperand(i).equals(that.getOperand(i))) {
                    return false;
                }
            } else {
                if (this.getOperand(i) != that.getOperand(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    public enum InstrType {
        ADD, SUB, MUL, SDIV, SREM, XOR, AND, OR, EQ, NE, SLE, SLT, SGE, SGT,
        BR, CALL, RET,
        ALLOCA, LOAD, STORE, GETELEMENTPTR, ZEXT, PHI,
        COPY, MOVE,
        PUTSTR;

        public boolean isArithmetic() { return this.ordinal() <= SREM.ordinal(); }

        public boolean isIcmp() { return this.ordinal() >= EQ.ordinal() && this.ordinal() <= SGT.ordinal(); }

        public boolean isTerminator() { return this.ordinal() == BR.ordinal() || this.ordinal() == RET.ordinal(); }

        public boolean isMem() { return this.ordinal() >= ALLOCA.ordinal() && this.ordinal() <= PHI.ordinal(); }

        public boolean isBinary() { return isArithmetic() || isIcmp(); }

        public boolean needName() { return !(isTerminator() && this != InstrType.CALL); }

        public boolean isCommutative() {
            switch (this) {
                case ADD: case MUL: case EQ: case NE: case XOR: case AND: case OR:
                    return true;
                case SUB: case SREM: case SDIV: case SGT: case SGE: case SLT: case SLE:
                    return false;
                default: throw new RuntimeException("check commutative not Binary");
            }
        }

        public static boolean isInverse(InstrType type1, InstrType type2) {
            if (!type1.isBinary() || !type2.isBinary()) {
                return false;
            }
            //todo: when needed.
            return type1 == type2 && type1.isCommutative() ||
                    (type1 == SGT && type2 == SLT) ||
                    (type1 == SGE && type2 == SLE) ||
                    (type1 == SLT && type2 == SGT) ||
                    (type1 == SLE && type2 == SGE);
        }
    }

    //====================================================================backend support

    public abstract void toAssembly(MipsAssembly assembly);
}
