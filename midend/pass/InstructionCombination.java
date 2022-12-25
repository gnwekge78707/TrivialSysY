package midend.pass;

import midend.ir.Module;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.Constant;
import midend.ir.value.Function;
import midend.ir.value.instr.Instruction;
import midend.ir.value.instr.binary.BinaryInstr;
import util.IList;

public class InstructionCombination {
    Module m;
    public void run(Module m) {
        this.m = m;
        for (IList.INode<Function, Module> funcNode : m.getFunctionList()) {
            Function func = funcNode.getValue();
            if (!func.isBuiltIn()) {
                run(func);
            }
        }
    }
    
    Constant cst;
    Value vlu;
    public void run(Function func) {
        for (IList.INode<BasicBlock, Function> bbNode : func.getBbList()) {
            BasicBlock bb = bbNode.getValue();
            for (IList.INode<Instruction, BasicBlock> instNode : bb.getInstrList()) {
                Instruction targetInstr = instNode.getValue();
                if (targetInstr.getInstrType() != Instruction.InstrType.ADD) {
                    continue;
                }
                if (targetInstr.getOperand(0) == targetInstr.getOperand(1)) {
                    Value lhs = targetInstr.getOperand(0);
                    if (isConstMulValue(lhs)) {
                        targetInstr.setOperand(0, vlu);
                        targetInstr.setOperand(1, new Constant(LLVMType.Int.getI32(), cst.getConstVal() * 2));
                        targetInstr.setInstrType(Instruction.InstrType.MUL);
                        continue;
                    }
                    targetInstr.setOperand(1, new Constant(LLVMType.Int.getI32(), 2));
                    targetInstr.setInstrType(Instruction.InstrType.MUL);
                    continue;
                }
                Value lhs = targetInstr.getOperand(0);
                Value rhs = targetInstr.getOperand(1);
                if (lhs instanceof BinaryInstr && ((BinaryInstr)lhs).getInstrType() == Instruction.InstrType.ADD
                        && lhs.getUseList().size() == 1 && rhs instanceof Constant) {
                    Value llhs = ((BinaryInstr)lhs).getOperand(0);
                    Value rlhs = ((BinaryInstr)lhs).getOperand(1);
                    if (rlhs instanceof Constant) {
                        targetInstr.setOperand(0, llhs);
                        Constant c = new Constant(LLVMType.Int.getI32(), ((Constant)rhs).getConstVal() + ((Constant)rlhs).getConstVal());
                        targetInstr.setOperand(1, c);
                        ((BinaryInstr) lhs).removeAllUses();
                        ((BinaryInstr) lhs).getINode().removeSelf();
                    } else if (llhs instanceof Constant) {
                        targetInstr.setOperand(0, rlhs);
                        Constant c = new Constant(LLVMType.Int.getI32(), ((Constant)rhs).getConstVal() + ((Constant) llhs).getConstVal());
                        targetInstr.setOperand(1, c);
                        ((BinaryInstr) lhs).removeAllUses();
                        ((BinaryInstr) lhs).getINode().removeSelf();
                    }
                }
                if (rhs instanceof BinaryInstr && ((BinaryInstr)rhs).getInstrType() == Instruction.InstrType.ADD
                        && rhs.getUseList().size() == 1 && lhs instanceof Constant) {
                    Value lrhs = ((BinaryInstr)rhs).getOperand(0);
                    Value rrhs = ((BinaryInstr)rhs).getOperand(1);
                    if (rrhs instanceof Constant) {
                        targetInstr.setOperand(1, lrhs);
                        Constant c = new Constant(LLVMType.Int.getI32(), ((Constant)lhs).getConstVal() + ((Constant)rrhs).getConstVal());
                        targetInstr.setOperand(0, c);
                        ((BinaryInstr) rhs).removeAllUses();
                        ((BinaryInstr) rhs).getINode().removeSelf();
                    } else if (lrhs instanceof Constant) {
                        targetInstr.setOperand(1, rrhs);
                        Constant c = new Constant(LLVMType.Int.getI32(), ((Constant)lhs).getConstVal() + ((Constant)lrhs).getConstVal());
                        targetInstr.setOperand(0, c);
                        ((BinaryInstr) rhs).removeAllUses();
                        ((BinaryInstr) rhs).getINode().removeSelf();
                    }
                }
                if (isConstMulValue(lhs) && rhs == vlu) {
                    targetInstr.setOperand(0, vlu);
                    targetInstr.setOperand(1, new Constant(LLVMType.Int.getI32(), cst.getConstVal() + 1));
                    targetInstr.setInstrType(Instruction.InstrType.MUL);
                    continue;
                } else if (isConstMulValue(rhs) && lhs == vlu) {
                    targetInstr.setOperand(0, vlu);
                    targetInstr.setOperand(1, new Constant(LLVMType.Int.getI32(), cst.getConstVal() + 1));
                    targetInstr.setInstrType(Instruction.InstrType.MUL);
                    continue;
                } else if (isConstMulValue(lhs)) {
                    Constant tcst = cst;
                    Value tvlu = vlu;
                    if (isConstMulValue(rhs)) {
                        if (tvlu == vlu) {
                            targetInstr.setOperand(0, vlu);
                            targetInstr.setOperand(1, new Constant(LLVMType.Int.getI32(), cst.getConstVal() + tcst.getConstVal()));
                            targetInstr.setInstrType(Instruction.InstrType.MUL);
                            continue;
                        }
                    }
                }
            }
        }
    }
    public boolean isConstMulValue(Value inst) {
        if (inst instanceof BinaryInstr && ((BinaryInstr) inst).getInstrType() == Instruction.InstrType.MUL) {
            Value lhs = ((BinaryInstr) inst).getOperand(0);
            Value rhs = ((BinaryInstr) inst).getOperand(1);
            if (lhs instanceof Constant) {
                cst = (Constant)lhs;
                vlu = rhs;
            } else if (rhs instanceof Constant) {
                cst = (Constant)rhs;
                vlu = lhs;
            } else {
                return false;
            }
            return true;
        }
        return false;
    }
}
