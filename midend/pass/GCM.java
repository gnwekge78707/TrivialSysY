package midend.pass;

import midend.ir.Module;
import midend.ir.Use;
import midend.ir.User;
import midend.ir.Value;
import midend.ir.value.BasicBlock;
import midend.ir.value.Function;
import midend.ir.value.instr.Instruction;
import util.IList;

import java.util.ArrayList;
import java.util.Collections;

public class GCM {
    public void run(Module m) {
        for (IList.INode<Function, Module> fNode : m.getFunctionList()) {
            Function f = fNode.getValue();
            if (f.isBuiltIn()) {
                continue;
            }
            runGCMOnFunction(f);
        }
    }

    private void runGCMOnFunction(Function f) {
        ArrayList<Instruction> instructions = new ArrayList<>();
        ArrayList<BasicBlock> bbs = GVN.basicBlockToposort(f);
        for (BasicBlock bb : bbs) {
            for (IList.INode<Instruction, BasicBlock> iNode : bb.getInstrList()) {
                instructions.add(iNode.getValue());
            }
        }
        for (Instruction instr : instructions) {
            scheduleEarly(instr);
        }
        Collections.reverse(instructions);
        for (Instruction instr : instructions) {
            scheduleLate(instr);
        }
        GVN.unreachableBasicBlockRemove(f);
    }

    private void scheduleEarly(Instruction instr) {
        int maxDomLevel = 0;
        switch (instr.getInstrType()) {
            case BR: case RET: case STORE: case LOAD: case ALLOCA: case PHI: case CALL: case PUTSTR:
                return;
            case ZEXT:
                Value value0 = instr.getOperand(0);
                if (value0 instanceof Instruction) {
                    maxDomLevel = Math.max(maxDomLevel, ((Instruction) value0).getParent().getDominanceLevel());
                }
                break;
            case GETELEMENTPTR:
                for (int i = 0; i < instr.getOperandNum(); ++i) {
                    Value value = instr.getOperand(i);
                    if (value instanceof Instruction) {
                        maxDomLevel = Math.max(maxDomLevel, ((Instruction) value).getParent().getDominanceLevel());
                    }
                }
                break;
            default:
                Value lhs = instr.getOperand(0);
                Value rhs = instr.getOperand(1);
                if (lhs instanceof Instruction) {
                    maxDomLevel = Math.max(maxDomLevel, ((Instruction) lhs).getParent().getDominanceLevel());
                }
                if (rhs instanceof Instruction) {
                    maxDomLevel = Math.max(maxDomLevel, ((Instruction) rhs).getParent().getDominanceLevel());
                }
                break;
        }
        if (maxDomLevel == instr.getParent().getDominanceLevel()) {
            return;
        }
        BasicBlock cur = instr.getParent();
        while (cur.getDominanceLevel() > maxDomLevel) {
            cur = cur.getIdominator();
        }
        instr.getINode().removeSelf();
        instr.getINode().insertNext(cur.getInstrList().getLast());
    }

    private void scheduleLate(Instruction instr) {
        switch (instr.getInstrType()) {
            case BR: case RET: case STORE: case LOAD: case ALLOCA: case PHI: case CALL: case PUTSTR:
                return;
        }
        if (instr.getUseList().isEmpty()) {
            return;
        }
        BasicBlock lca = null;
        for (Use use : instr.getUseList()) {
            User user = use.user;
            Instruction userInst = (Instruction) user;
            BasicBlock userBB = userInst.getParent();
            if (userInst.getInstrType() == Instruction.InstrType.PHI) {
                userBB = userBB.getPredecessors().get(use.idxOfValueInUser);
            }
            lca = LCA(lca, userBB);
        }
        BasicBlock finalBB = lca;
        BasicBlock cur = lca;
        LoopInfoAnalysis.LoopInfo loopInfo = instr.getParent().getParent().getLoopInfo();
        while (cur != instr.getParent()) {
            if (loopInfo.BBLoopDepth(cur) < loopInfo.BBLoopDepth(finalBB)) {
                finalBB = cur;
            }
            cur = cur.getIdominator();
        }
        if (finalBB == lca) {
            for (IList.INode<Instruction, BasicBlock> iNode : finalBB.getInstrList()) {
                if (iNode.getValue().getInstrType() != Instruction.InstrType.PHI && iNode.getValue().getOperands().contains(instr)) {
                    instr.getINode().removeSelf();
                    instr.getINode().insertNext(iNode);
                    return;
                }
            }
        }
        instr.getINode().removeSelf();
        instr.getINode().insertNext(finalBB.getInstrList().getLast());
    }

    private BasicBlock LCA(BasicBlock a, BasicBlock b) {
        if (a == null) return b;
        if (b == null) return a;
        while (a.getDominanceLevel() > b.getDominanceLevel()) {
            a = a.getIdominator();
        }
        while (b.getDominanceLevel() > a.getDominanceLevel()) {
            b = b.getIdominator();
        }
        while (a != b) {
            a = a.getIdominator();
            b = b.getIdominator();
        }
        return a;
    }
}
