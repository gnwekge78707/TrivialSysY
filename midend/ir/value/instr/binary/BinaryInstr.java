package midend.ir.value.instr.binary;

import backend.MipsAssembly;
import backend.template.MipsCalTemplate;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.Constant;
import midend.ir.value.instr.Instruction;
import midend.ir.value.instr.terminator.BrInstr;

public class BinaryInstr extends Instruction {
    private Value dstVar; //default = null
    //for ssa, dst is instr itself
    //else, dst usually is a NormalVar
    //fixme: instr itself as a value can be used by other instr
    //todo: this dstVar can be used in an assignInstr -> int a = 0; a = a+1;

    private static LLVMType getDstType(InstrType instrType) { //InstrType 2 LLVMType
        if (instrType.isArithmetic()) {
            return LLVMType.Int.getI32();
        } else if (instrType.isIcmp()) {
            return LLVMType.Int.getI1();
        } else {
            throw new Error("wrong binary instr type");
        }
    }

    public BinaryInstr(InstrType instrType, Value src1, Value src2) {
        super(instrType, getDstType(instrType), 2);
        this.setOperand(0, src1);
        this.setOperand(1, src2);
    }

    public BinaryInstr(InstrType instrType, Value src1, Value src2, BasicBlock parent) {
        super(instrType, getDstType(instrType), 2, parent);
        this.setOperand(0, src1);
        this.setOperand(1, src2);
    }

    public BinaryInstr(InstrType instrType, Value dst, Value src1, Value src2, BasicBlock parent) {
        super(instrType, getDstType(instrType), 2, parent);
        this.dstVar = dst;
        this.setOperand(0, src1);
        this.setOperand(1, src2);
    }

    public Value getDstVar() { return this.dstVar; }

    public boolean hasExplicitDstVar() {
        return this.dstVar != null;
    }

    public String getName() { return hasExplicitDstVar() ? dstVar.getName() : super.getName(); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t").append(this.getName()).append(" = ");
        switch (this.getInstrType()) {
            case ADD: sb.append("add i32 "); break;
            case EQ: sb.append("icmp eq " + getOperand(0).getType() + " "); break;
            case NE: sb.append("icmp ne " + getOperand(0).getType() + " "); break;
            case OR: sb.append("or i32 "); break;
            case AND: sb.append("and i32 "); break;
            case MUL: sb.append("mul i32 "); break;
            case SGE: sb.append("icmp sge " + getOperand(0).getType() + " "); break;
            case SLE: sb.append("icmp sle " + getOperand(0).getType() + " "); break;
            case SGT: sb.append("icmp sgt " + getOperand(0).getType() + " "); break;
            case SLT: sb.append("icmp slt " + getOperand(0).getType() + " "); break;
            case SUB: sb.append("sub i32 "); break;
            case XOR: sb.append("xor i32 "); break;
            case SDIV: sb.append("sdiv i32 "); break;
            case SREM: sb.append("srem i32 "); break;
            default: break;
        }
        sb.append(getOperand(0).getName() + ", " + getOperand(1).getName());
        return sb.toString();
    }

    public void toAssembly(MipsAssembly assembly) {
        Value dst = hasExplicitDstVar() ? dstVar : this;
        Value src1 = getOperand(0);
        Value src2 = getOperand(1);
        boolean onlyHaveBrUsage = this.getUseList().stream().allMatch(u -> (u.user instanceof BrInstr));
        switch (this.getInstrType()) {
            case ADD:
                MipsCalTemplate.mipsAddTemplate(dst, src1, src2, assembly);
                break;
            case EQ:
                if (!onlyHaveBrUsage) MipsCalTemplate.mipsSeqTemplate(dst, src1, src2, assembly);
                break;
            case NE:
                if (!onlyHaveBrUsage) MipsCalTemplate.mipsSneTemplate(dst, src1, src2, assembly);
                break;
            case OR:  break;
            case AND:  break;
            case MUL:
                MipsCalTemplate.mipsMulTemplate(dst, src1, src2, assembly);
                break;
            case SGE:
                if (!onlyHaveBrUsage) MipsCalTemplate.mipsSgeTemplate(dst, src1, src2, assembly);
                break;
            case SLE:
                if (!onlyHaveBrUsage) MipsCalTemplate.mipsSleTemplate(dst, src1, src2, assembly);
                break;
            case SGT:
                if (!onlyHaveBrUsage) MipsCalTemplate.mipsSgtTemplate(dst, src1, src2, assembly);
                break;
            case SLT:
                if (!onlyHaveBrUsage) MipsCalTemplate.mipsSltTemplate(dst, src1, src2, assembly);
                break;
            case SUB:
                MipsCalTemplate.mipsSubTemplate(dst, src1, src2, assembly);
                break;
            case XOR:  break;
            case SDIV:
                MipsCalTemplate.mipsDivTemplate(dst, src1, src2, assembly);
                break;
            case SREM:
                MipsCalTemplate.mipsModTemplate(dst, src1, src2, assembly);
                break;
            default: break;
        }
        dst.getMipsMemContex().updateMem(assembly);
    }

    public Value lhs() {
        return this.getOperand(0);
    }

    public Value rhs() {
        return this.getOperand(1);
    }

    public static boolean checkSame(BinaryInstr b1, BinaryInstr b2) {
        return (b1.getInstrType() == b2.getInstrType() && b1.lhs().equals(b2.lhs()) && b1.rhs().equals(b2.rhs())) ||
                (b1.getInstrType() == b2.getInstrType() && b1.getInstrType().isCommutative() && b1.lhs().equals(b2.rhs()) && b1.rhs().equals(b2.lhs())) ||
                (InstrType.isInverse(b1.getInstrType(), b2.getInstrType()) && b1.lhs().equals(b2.rhs()) && b1.rhs().equals(b2.lhs()));
    }
    
    public static Constant calcBinaryInt(InstrType type, Constant lhs, Constant rhs) {
        if (lhs.getType() != rhs.getType()) {
            throw new RuntimeException("different Type in Binary!!!");
        }
        if (lhs.getType() == LLVMType.Int.getI1()) {
            throw new RuntimeException("binary calc i1Type?");
        }
        switch (type) {
            case ADD: return  new Constant(LLVMType.Int.getI32(), lhs.getConstVal() + rhs.getConstVal());
            case SUB: return  new Constant(LLVMType.Int.getI32(), lhs.getConstVal() - rhs.getConstVal());
            case MUL: return  new Constant(LLVMType.Int.getI32(), lhs.getConstVal() * rhs.getConstVal());
            case SDIV: return  new Constant(LLVMType.Int.getI32(), lhs.getConstVal() / rhs.getConstVal());
            case SREM: return  new Constant(LLVMType.Int.getI32(), lhs.getConstVal() % rhs.getConstVal());
            case EQ: return  new Constant(LLVMType.Int.getI1(), lhs.getConstVal() == rhs.getConstVal() ? 1 : 0);
            case NE: return  new Constant(LLVMType.Int.getI1(), lhs.getConstVal() != rhs.getConstVal() ? 1 : 0);
            case SLE: return  new Constant(LLVMType.Int.getI1(), lhs.getConstVal() <= rhs.getConstVal() ? 1 : 0);
            case SLT: return  new Constant(LLVMType.Int.getI1(), lhs.getConstVal() < rhs.getConstVal() ? 1 : 0);
            case SGT: return  new Constant(LLVMType.Int.getI1(), lhs.getConstVal() >= rhs.getConstVal() ? 1 : 0);
            case SGE: return  new Constant(LLVMType.Int.getI1(), lhs.getConstVal() > rhs.getConstVal() ? 1 : 0);
            case XOR: return  new Constant(LLVMType.Int.getI32(), lhs.getConstVal() ^ rhs.getConstVal());
            case AND: return  new Constant(LLVMType.Int.getI32(), lhs.getConstVal() & rhs.getConstVal());
            case OR: return  new Constant(LLVMType.Int.getI32(), lhs.getConstVal() | rhs.getConstVal());
            default: throw new RuntimeException("undefined binary operation in calcBinaryInt!!!");
        }
    }
}
