package midend.pass;

import midend.ir.Module;
import midend.ir.Use;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.Constant;
import midend.ir.value.Function;
import midend.ir.value.GlobalVariable;
import midend.ir.value.instr.Instruction;
import midend.ir.value.instr.binary.BinaryInstr;
import midend.ir.value.instr.mem.GEPInstr;
import midend.ir.value.instr.mem.LoadInstr;
import midend.ir.value.instr.mem.PhiInstr;
import midend.ir.value.instr.terminator.BrInstr;
import util.IList;
import util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class GVN {
    private Module m;

    public void run(Module module) {
        this.m = module;
        runGVN();
    }

    private void runGVN() {
        for (IList.INode<Function, Module> fNode : m.getFunctionList()) {
            if (!fNode.getValue().isBuiltIn()) {
                runGVNonFunction(fNode.getValue());
            }
        }
        ArrayList<GlobalVariable> removedGVs = new ArrayList<>();
        for (GlobalVariable GV : m.getGlobalVariables()) {
            boolean hasUse = false;
            for (Use use : GV.getUseList()) {
                if (use.user instanceof LoadInstr || use.user instanceof GEPInstr) {
                    hasUse = true;
                    break;
                }
            }
            if (!hasUse) {
                removedGVs.add(GV);
            }
        }
        for (GlobalVariable dstGlobalVar : removedGVs) {
            ArrayList<Use> uses = new ArrayList<>(dstGlobalVar.getUseList());
            for (Use use : uses) {
                ((Instruction) use.user).getINode().removeSelf();
                use.user.replaceSelfWith(null);
                use.user.removeSelfUses();
            }
        }
    }

    public static void removeBrFlow(BasicBlock fromBB, BasicBlock toBB) {
        int pos = toBB.getPredecessors().indexOf(fromBB);
        for (IList.INode<Instruction, BasicBlock> iNode : toBB.getInstrList()) {
            if (iNode.getValue() instanceof PhiInstr) {
                ((PhiInstr) iNode.getValue()).removeIncomingValue(pos);
            }
        }
        toBB.getPredecessors().remove(pos);
        fromBB.getSuccessors().remove(toBB);
    }

    public static void unreachableBasicBlockRemove(Function f) {
        HashSet<BasicBlock> blockVisited = new HashSet<>();
        BasicBlock start = f.getBbList().getEntry().getValue();
        Queue<BasicBlock> q = new LinkedList<>();
        q.add(start);
        blockVisited.add(start);
        while (!q.isEmpty()) {
            BasicBlock bb = q.poll();
            if (bb.getInstrList().getLast().getValue().getInstrType() == Instruction.InstrType.BR) {
                BrInstr br = (BrInstr) bb.getInstrList().getLast().getValue();
                ArrayList<BasicBlock> successors = new ArrayList<>();
                if (br.getOperandNum() == 3) {
                    successors.add((BasicBlock) br.getOperand(1));
                    successors.add((BasicBlock) br.getOperand(2));
                } else {
                    successors.add((BasicBlock) br.getOperand(0));
                }
                for (BasicBlock successor : successors) {
                    if (!blockVisited.contains(successor)) {
                        blockVisited.add(successor);
                        q.add(successor);
                    }
                }
            }
        }
        ArrayList<BasicBlock> deleteBBs = new ArrayList<>();
        for (IList.INode<BasicBlock, Function> bNode : f.getBbList()) {
            if (!blockVisited.contains(bNode.getValue())) {
                deleteBBs.add(bNode.getValue());
            }
        }
        for (BasicBlock bb : deleteBBs) {
            ArrayList<BasicBlock> successorsCopy = new ArrayList<>(bb.getSuccessors());
            for (BasicBlock succBBA : successorsCopy) {
                removeBrFlow(bb, succBBA);
            }
            ArrayList<Instruction> removedInstruction = new ArrayList<>();
            for (IList.INode<Instruction, BasicBlock> iNode : bb.getInstrList()) {
                removedInstruction.add(iNode.getValue());
            }
            for (Instruction instruction : removedInstruction) {
                instruction.removeSelfUses();
                instruction.replaceSelfWith(null);
                instruction.getINode().removeSelf();
            }
            bb.getINode().removeSelf();
        }
    }

    public static ArrayList<BasicBlock> basicBlockToposort(Function f) {
        HashSet<BasicBlock> blockVisited = new HashSet<>();
        ArrayList<BasicBlock> res = new ArrayList<>();
        BasicBlock start = f.getBbList().getEntry().getValue();
        Queue<BasicBlock> q = new LinkedList<>();
        q.add(start);
        blockVisited.add(start);
        while (!q.isEmpty()) {
            BasicBlock bb = q.poll();
            res.add(bb);
            for (BasicBlock successor : bb.getSuccessors()) {
                if (blockVisited.contains(successor)) {
                    continue;
                }
                boolean ok = true;
                for (IList.INode<Instruction, BasicBlock> iter : successor.getInstrList()) {
                    Instruction instr = iter.getValue();
                    if (instr.getInstrType() == Instruction.InstrType.PHI) {
                        continue;
                    }
                    for (int i = 0; i < instr.getOperandNum(); ++i) {
                        if (instr.getOperand(i) instanceof Instruction) {
                            if (!blockVisited.contains(((Instruction) instr.getOperand(i)).getParent()) &&
                                    !((Instruction) instr.getOperand(i)).getParent().equals(successor)) {
                                ok = false;
                                break;
                            }
                        }
                    }
                    if (!ok) {
                        break;
                    }
                }
                if (ok) {
                    blockVisited.add(successor);
                    q.add(successor);
                }
            }
        }
        return res;
    }

    private void runGVNonFunction(Function f) {
        valueTable.clear();
        ArrayList<BasicBlock> basicBlocks = basicBlockToposort(f);
        for (BasicBlock bb : basicBlocks) {
            runGVNonBasicBlock(bb);
        }
    }

    private void runGVNonBasicBlock(BasicBlock bb) {
        for (IList.INode<Instruction, BasicBlock> iNode : bb.getInstrList()) {
            runGVNonInstruction(iNode.getValue());
        }
    }

    private void runGVNonInstruction(Instruction instr) {
        Value v = simplifyInstruction(instr);
        if (!(v instanceof Instruction)) {
            if (!(v instanceof Constant || v instanceof Function.Param)) {
                throw new RuntimeException(v.toString());
            }
            replaceInstruction(instr, v);
            return;
        }
        Instruction newInstr = (Instruction) v;
        Value value = findSimplifiedInstruction(newInstr);
        replaceInstruction(instr, value);
    }

    private final ArrayList<Pair<Value, Value>> valueTable = new ArrayList<>();

    private void replaceInstruction(Instruction origin, Value value) {
        if (origin == value) {
            return;
        }
        if (origin.getInstrType() == Instruction.InstrType.BR) {
            assert origin.getOperandNum() == 3;
            assert ((BrInstr) value).getOperandNum() == 1;
            BasicBlock nxt = (BasicBlock) ((BrInstr) value).getOperand(0);
            if (nxt.equals(origin.getOperand(1))) {
                removeBrFlow(origin.getParent(), (BasicBlock) origin.getOperand(2));
            } else {
                removeBrFlow(origin.getParent(), (BasicBlock) origin.getOperand(1));
            }
        }
        valueTable.removeIf(v -> v.getFirst() == origin);
        origin.replaceSelfWith(value);
        origin.removeSelfUses();
        origin.getINode().removeSelf();
    }

    private Value findBinaryInstr(BinaryInstr binaryInst) {
        for (Pair<Value, Value> iter : valueTable) {
            Value key = iter.getFirst();
            Value value = iter.getSecond();
            if (key.equals(binaryInst)) {
                return value;
            }
            if (key instanceof BinaryInstr) {
                if (BinaryInstr.checkSame(binaryInst, (BinaryInstr) key)) {
                    return value;
                }
            }
        }
        valueTable.add(new Pair<>(binaryInst, binaryInst));
        return binaryInst;
    }

    private Value findGEPInstr(GEPInstr gep) {
        for (Pair<Value, Value> iter : valueTable) {
            Value key = iter.getFirst();
            Value value = iter.getSecond();
            if (key.equals(gep)) {
                return value;
            }
            if (key instanceof GEPInstr) {
                if (GEPInstr.checkSame(gep, (GEPInstr) key)) {
                    return value;
                }
            }
        }
        valueTable.add(new Pair<>(gep, gep));
        return gep;
    }

    private Value findSimplifiedInstruction(Instruction instruction) {
        for (Pair<Value, Value> pair : valueTable) {
            if (pair.getFirst() == instruction) {
                return pair.getSecond();
            }
        }
        Value res;
        if (instruction.getInstrType().isBinary()) {
            res = findBinaryInstr((BinaryInstr) instruction);
        } else if (instruction.getInstrType() == Instruction.InstrType.GETELEMENTPTR) {
            res = findGEPInstr((GEPInstr) instruction);
        } else {
            res = instruction;
        }
        return res;
    }

    public static Value simplifyInstruction(Instruction instruction) {
        if (instruction.getInstrType().isBinary()) {
            Value lhs = instruction.getOperand(0);
            Value rhs = instruction.getOperand(1);
            if (lhs instanceof Constant && rhs instanceof Constant) {
                if (lhs.getType() != rhs.getType()) {
                    return instruction;
                }
                return BinaryInstr.calcBinaryInt(instruction.getInstrType(), (Constant) lhs, (Constant) rhs);
            }
        }
        switch (instruction.getInstrType()) {
            case ADD: return simplifyAdd(instruction);
            case MUL: return simplifyMul(instruction);
            case SUB: return simplifySub(instruction);
            case SREM: return simplifyMod(instruction);
            case SDIV: return simplifyDiv(instruction);
            case EQ: case SLE: case SGE: return simplifyEqIsTrue(instruction);
            case NE: case SLT: case SGT: return simplifyEqIsFalse(instruction);
            case BR: return simplifyBr(instruction);
            default: return instruction;
        }
    }
    
    //todo===============================================simplify single instr
    public static Value simplifyBr(Instruction instr) {
        BrInstr br = (BrInstr) instr;
        if (br.getOperandNum() == 3 && br.getOperand(0) instanceof Constant) {
            if (((Constant) br.getOperand(0)).getConstVal() == 1) {
                BrInstr newBr = new BrInstr((BasicBlock) br.getOperand(1));
                newBr.getINode().insertPrev(br.getINode());
                return newBr;
            } else {
                BrInstr newBr = new BrInstr((BasicBlock) br.getOperand(2));
                newBr.getINode().insertPrev(br.getINode());
                return newBr;
            }
        }
        return instr;
    }

    
    public static Value simplifyAdd(Instruction instr) {
        Value lhs = instr.getOperand(0);
        Value rhs = instr.getOperand(1);
        if (lhs instanceof Constant && ((Constant) lhs).getConstVal() == 0) {
            return rhs;
        }
        if (rhs instanceof Constant && ((Constant) rhs).getConstVal() == 0) {
            return lhs;
        }
        if (rhs instanceof Constant &&
                lhs instanceof BinaryInstr &&
                ((BinaryInstr) lhs).rhs() instanceof Constant &&
                ((BinaryInstr) lhs).getInstrType() == Instruction.InstrType.ADD) {
            Value newLhs = ((BinaryInstr) lhs).lhs();
            Value newRhs = new Constant(LLVMType.Int.getI32(),
                    ((Constant) rhs).getConstVal() + ((Constant) ((BinaryInstr) lhs).rhs()).getConstVal());
            instr.setOperand(0, newLhs);
            instr.setOperand(1, newRhs);
            return instr;
        }
        if (lhs instanceof Instruction &&
                ((Instruction) lhs).getInstrType() == Instruction.InstrType.SUB &&
                ((Instruction) lhs).getOperand(1).equals(rhs)) {
            return ((Instruction) lhs).getOperand(0);
        }
        if (rhs instanceof Instruction &&
                ((Instruction) rhs).getInstrType() == Instruction.InstrType.SUB &&
                ((Instruction) rhs).getOperand(1).equals(lhs)) {
            return ((Instruction) rhs).getOperand(0);
        }
        return instr;
    }

    public static Value simplifySub(Instruction instr) {
        Value lhs = instr.getOperand(0);
        Value rhs = instr.getOperand(1);
        if (lhs.equals(rhs)) {
            return new Constant(LLVMType.Int.getI32(), 0);
        }
        if (rhs instanceof Constant && ((Constant) rhs).getConstVal() == 0) {
            return lhs;
        }
        if (lhs instanceof Instruction &&
                ((Instruction) lhs).getInstrType() == Instruction.InstrType.ADD &&
                ((Instruction) lhs).getOperand(1).equals(rhs)) {
            return ((Instruction) lhs).getOperand(0);
        }
        if (lhs instanceof Instruction &&
                ((Instruction) lhs).getInstrType() == Instruction.InstrType.ADD &&
                ((Instruction) lhs).getOperand(0).equals(rhs)) {
            return ((Instruction) lhs).getOperand(1);
        }
        if (rhs instanceof Instruction &&
                ((Instruction) rhs).getInstrType() == Instruction.InstrType.SUB &&
                ((Instruction) rhs).getOperand(0).equals(lhs)) {
            return ((Instruction) rhs).getOperand(1);
        }
        return instr;
    }

    public static Value simplifyMod(Instruction instr) {
        Value lhs = instr.getOperand(0);
        Value rhs = instr.getOperand(1);
        if (lhs.equals(rhs)) {
            return Constant.getConst0();
        }
        if (lhs instanceof Constant && ((Constant) lhs).getConstVal() == 0) {
            return Constant.getConst0();
        }
        if (rhs instanceof Constant && ((Constant) rhs).getConstVal() == 1) {
            return Constant.getConst0();
        }
        if (rhs instanceof Constant && ((Constant) rhs).getConstVal() == -1) {
            return Constant.getConst0();
        }
        return instr;
    }

    public static Value simplifyMul(Instruction instr) {
        Value lhs = instr.getOperand(0);
        Value rhs = instr.getOperand(1);
        if (lhs instanceof Constant && ((Constant) lhs).getConstVal() == 0) {
            return Constant.getConst0();
        }
        if (rhs instanceof Constant && ((Constant) rhs).getConstVal() == 0) {
            return Constant.getConst0();
        }
        if (lhs instanceof Constant && ((Constant) lhs).getConstVal() == 1) {
            return rhs;
        }
        if (rhs instanceof Constant && ((Constant) rhs).getConstVal() == 1) {
            return lhs;
        }
        return instr;
    }

    public static Value simplifyDiv(Instruction instr) {
        Value lhs = instr.getOperand(0);
        Value rhs = instr.getOperand(1);
        if (lhs.equals(rhs)) {
            return new Constant(LLVMType.Int.getI32(), 1);
        }
        if (rhs instanceof Constant && ((Constant) rhs).getConstVal() == 1) {
            return lhs;
        }
        return instr;
    }

    public static Value simplifyEqIsTrue(Instruction instr) {
        Value lhs = instr.getOperand(0);
        Value rhs = instr.getOperand(1);
        if (lhs.equals(rhs)) {
            return new Constant(LLVMType.Int.getI1(), 1);
        }
        return instr;
    }

    public static Value simplifyEqIsFalse(Instruction instr) {
        Value lhs = instr.getOperand(0);
        Value rhs = instr.getOperand(1);
        if (lhs.equals(rhs)) {
            return new Constant(LLVMType.Int.getI1(), 0);
        }
        return instr;
    }
}
