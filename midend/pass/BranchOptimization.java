package midend.pass;

import midend.ir.Module;
import midend.ir.Value;
import midend.ir.value.BasicBlock;
import midend.ir.value.Constant;
import midend.ir.value.Function;
import midend.ir.value.instr.Instruction;
import midend.ir.value.instr.mem.PhiInstr;
import midend.ir.value.instr.terminator.BrInstr;
import util.IList;

import java.util.LinkedList;
import java.util.Queue;

public class BranchOptimization {
    public boolean run(Module m) {
        boolean changed = false;
        for (IList.INode<Function, Module> funcNode : m.getFunctionList()) {
            Function func = funcNode.getValue();
            if (!funcNode.getValue().isBuiltIn()) {
                changed |= run(func);
            }
        }
        return changed;
    }

    public boolean run(Function func) {
        boolean changed;
        boolean res = false;
        while (true) {
            changed = removeUselessPhi(func);
            changed |= onlySingleUnCondBr(func);
            changed |= endWithUncondBr(func);
            changed |= removeDeadBB(func);
            changed |= mergeSameBr(func);
            if (!changed) {
                break;
            }
            res = true;
        }
        return res;
    }

    public boolean removeUselessPhi(Function func) {
        boolean changed = false;
        for (IList.INode<BasicBlock, Function> bbNode : func.getBbList()) {
            BasicBlock bb = bbNode.getValue();
            for (IList.INode<Instruction, BasicBlock> instNode : bb.getInstrList()) {
                Instruction inst = instNode.getValue();
                if (!(inst instanceof PhiInstr)) {
                    break;
                }
                if (bb.getPredecessors().size() == 1) {
                    assert inst.getOperandNum() == 1;
                    inst.replaceSelfWith(inst.getOperands().get(0));
                    instNode.removeSelf();
                    inst.removeAllUses();
                    changed = true;
                } else if (bb.getPredecessors().size() == 0) {
                    assert inst.getOperandNum() == 0;
                    instNode.removeSelf();
                    changed = true;
                }
            }
        }
        return changed;
    }

    public boolean onlySingleUnCondBr(Function func) {
        boolean changed = false;
        for (IList.INode<BasicBlock, Function> bbNode : func.getBbList()) {
            BasicBlock bb = bbNode.getValue();
            if (bb.getParent() == null || bbNode == func.getBbList().getEntry()) {
                continue;
            }
            Instruction lastInst = bb.getInstrList().getLast().getValue();
            if (bb.getInstrList().getNodeNum() == 1
                    && lastInst instanceof BrInstr && (lastInst.getOperandNum() == 1)) {
                boolean canRemove = true;
                BasicBlock succ = (BasicBlock) lastInst.getOperand(0);
                if (succ.getInstrList().getEntry().getValue() instanceof PhiInstr) {
                    for (BasicBlock pred : bb.getPredecessors()) {
                        if (succ.getPredecessors().contains(pred)) {
                            canRemove = false;
                            break;
                        }
                    }
                }
                if (!canRemove) {
                    continue;
                }
                int index = succ.getPredecessors().indexOf(bb);
                succ.getPredecessors().remove(index);
                for (BasicBlock pred : bb.getPredecessors()) {
                    assert  pred.getInstrList().getLast().getValue() instanceof BrInstr;
                    BrInstr br = (BrInstr) pred.getInstrList().getLast().getValue();
                    if (br.getOperandNum() == 1) {
                        br.setOperand(0, succ);
                        pred.getSuccessors().set(0, succ);
                    } else {
                        if (br.getOperand(1) == bb) {
                            br.setOperand(1, succ);
                            pred.getSuccessors().set(0, succ);
                        } else {
                            br.setOperand(2, succ);
                            pred.getSuccessors().set(1, succ);
                        }
                    }
                    succ.getPredecessors().add(pred);
                }
                for (IList.INode<Instruction, BasicBlock> instNode : succ.getInstrList()) {
                    Instruction inst = instNode.getValue();
                    if (!(inst instanceof PhiInstr)) {
                        break;
                    }
                    Value value = inst.getOperand(index);
                    inst.removeOperand(index);
                    for (BasicBlock pred : bb.getPredecessors()) {
                        inst.addOperand(value);
                    }
                }
                lastInst.removeAllOperands();
                bbNode.removeSelf();
            }
        }
        return changed;
    }

    public boolean endWithUncondBr(Function func) {
        boolean changed = false;
        for (IList.INode<BasicBlock, Function> bbNode : func.getBbList()) {
            BasicBlock bb = bbNode.getValue();
            if (bbNode.getParent() == null) {
                continue;
            }
            Instruction lastInst = bb.getInstrList().getLast().getValue();
            if (lastInst instanceof BrInstr && (lastInst.getOperandNum() == 1)) {
                BasicBlock succ = (BasicBlock) lastInst.getOperands().get(0);
                if (mergeBB(bb, succ)) {
                    changed = true;
                }
            }
        }
        return changed;
    }

    public boolean removeDeadBB(Function func) {
        Queue<BasicBlock> queue = new LinkedList<>();
        for (IList.INode<BasicBlock, Function> bbNode : func.getBbList()) {
            BasicBlock bb = bbNode.getValue();
            if (bb.getPredecessors().size() == 0 && bbNode != func.getBbList().getEntry()) {
                queue.offer(bb);
            }
        }
        boolean changed = false;
        while (!queue.isEmpty()) {
            BasicBlock bb = queue.poll();

            for (int i = bb.getSuccessors().size() - 1; i >= 0; --i) {
                BasicBlock succ = bb.getSuccessors().get(i);
                removePredBB(bb, succ);
            }
            assert bb.getSuccessors().size() == 0;
            for (IList.INode<Instruction, BasicBlock> instNode : bb.getInstrList()) {
                Instruction inst = instNode.getValue();
                inst.removeAllUses();
            }
            bb.getINode().removeSelf();
            changed = true;
            for (BasicBlock succ : bb.getSuccessors()) {
                if (succ.getPredecessors().size() == 0) {
                    queue.offer(succ);
                }
            }
        }
        return changed;
    }

    public boolean mergeSameBr(Function func) {
        boolean changed = false;
        for (IList.INode<BasicBlock, Function> bbNode : func.getBbList()) {
            BasicBlock bb = bbNode.getValue();
            Instruction lastInst = bb.getInstrList().getLast().getValue();
            if (lastInst instanceof BrInstr && (lastInst.getOperandNum() == 3)) {
                if (lastInst.getOperands().get(1) == lastInst.getOperands().get(2)) {
                    BasicBlock succ = (BasicBlock)lastInst.getOperands().get(1);
                    lastInst.removeOperand(2);
                    lastInst.removeOperand(0);
                    bb.getSuccessors().remove(1);
                    for (BasicBlock pred : succ.getPredecessors()) {
                        if (pred == bb) {
                            succ.getPredecessors().remove(pred);
                            break;
                        }
                    }
                } else if (lastInst.getOperands().get(0) instanceof Constant) {
                    int res = ((Constant) lastInst.getOperands().get(0)).getConstVal();
                    BasicBlock unreachBB = (BasicBlock) lastInst.getOperands().get(res + 1);
                    lastInst.removeOperand(res + 1);
                    lastInst.removeOperand(0);
                    removePredBB(bb, unreachBB);
                    changed = true;
                }
            }
        }
        return changed;
    }

    public boolean mergeBB(BasicBlock pred, BasicBlock succ) {
        Instruction lastInst = pred.getInstrList().getLast().getValue();
        Instruction firstInst = succ.getInstrList().getEntry().getValue();
        if (succ.getPredecessors().size() != 1
                || firstInst instanceof PhiInstr
                || !(lastInst instanceof BrInstr)
                || lastInst.getOperandNum() != 1) {
            return false;
        }
        lastInst.getINode().removeSelf();
        lastInst.removeAllOperands();
        pred.setSuccessors(succ.getSuccessors());
        succ.getINode().removeSelf();
        for (IList.INode<Instruction, BasicBlock> instNode = succ.getInstrList().getEntry(); !succ.getInstrList().isEmpty(); ) {
            IList.INode<Instruction, BasicBlock> tmp = instNode.getNext();
            instNode.removeSelf();
            instNode.insertAtEnd(pred.getInstrList());
            instNode = tmp;
        }
        for (BasicBlock bb : succ.getSuccessors()) {
            int index = bb.getPredecessors().indexOf(succ);
            bb.getPredecessors().set(index, pred);
        }
        return true;
    }

    public void removePredBB(BasicBlock pred, BasicBlock succ) {
        int index = succ.getPredecessors().indexOf(pred);
        succ.getPredecessors().remove(pred);
        pred.getSuccessors().remove(succ);
        for (IList.INode<Instruction, BasicBlock> instNode : succ.getInstrList()) {
            Instruction inst = instNode.getValue();
            if (!(inst instanceof PhiInstr)) {
                break;
            }
            inst.removeOperand(index);
        }
    }
}
