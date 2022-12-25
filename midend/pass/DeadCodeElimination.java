package midend.pass;

import midend.ir.Module;
import midend.ir.Value;
import midend.ir.value.BasicBlock;
import midend.ir.value.Function;
import midend.ir.value.GlobalVariable;
import midend.ir.value.instr.Instruction;
import midend.ir.value.instr.mem.LoadInstr;
import midend.ir.value.instr.mem.StoreInstr;
import midend.ir.value.instr.terminator.CallInstr;
import util.IList;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class DeadCodeElimination {
    private HashSet<Instruction> usefulInstr = new HashSet<>();
    private Module m;
    private boolean changed;

    public boolean run(Module m) {
        this.m = m;
        changed = false;
        removeDeadFunction();
        for (IList.INode<Function, Module> funcNode : m.getFunctionList()) {
            Function func = funcNode.getValue();
            if (!func.isBuiltIn()) {
                run(func);
            }
        }
        return changed;
    }

    public void run(Function func) {
        removeUselessStore(func);
        removeDeadInstruction(func);
    }

    public void removeUselessStore(Function func) {
        for (IList.INode<BasicBlock, Function> bbNode : func.getBbList()) {
            BasicBlock bb = bbNode.getValue();
            for (IList.INode<Instruction, BasicBlock> instNode = bb.getInstrList().getEntry(); instNode.getValue() != null;) {
                IList.INode<Instruction, BasicBlock> nxt = instNode.getNext();
                Instruction inst = instNode.getValue();
                if (inst.getInstrType() == Instruction.InstrType.STORE) {
                    Value arr = AliasAnalysis.getArray(inst.getOperand(1));
                    for (IList.INode<Instruction, BasicBlock> ninstNode = nxt; ninstNode.getValue() != null; ninstNode = ninstNode.getNext()) {
                        Instruction ninst = ninstNode.getValue();
                        if (ninst instanceof StoreInstr) {
                            if (inst.getOperand(1) == ninst.getOperand(1)) {
                                inst.removeAllUses();
                                instNode.removeSelf();
                                break;
                            }
                        } else if (ninst instanceof LoadInstr) {
                            break;
                        } else if (ninst instanceof CallInstr) {
                            break;
                        }
                    }
                }
                instNode = nxt;
            }
        }
    }

    public void removeDeadInstruction(Function func) {
        usefulInstr.clear();
        for (IList.INode<BasicBlock, Function> bbNode : func.getBbList()) {
            for (IList.INode<Instruction, BasicBlock> instNode : bbNode.getValue().getInstrList()) {
                Instruction inst = instNode.getValue();
                if (isUseful(inst)) {
                    markInst(inst);
                }
            }
        }
        for (IList.INode<BasicBlock, Function> bbNode : func.getBbList()) {
            BasicBlock bb = bbNode.getValue();
            for (IList.INode<Instruction, BasicBlock> instNode = bb.getInstrList().getEntry(); instNode.getValue() != null;) {
                Instruction inst = instNode.getValue();
                IList.INode<Instruction, BasicBlock> nxt = instNode.getNext();
                if (!usefulInstr.contains(inst)) {
                    inst.removeAllUses();
                    instNode.removeSelf();
                    changed = true;
                }
                instNode = nxt;
            }
        }
    }

    public boolean isUseful(Instruction inst) {
        switch (inst.getInstrType()) {
            case BR: return true;
            case RET: return true;
            case STORE: return true;
            case PUTSTR: return true;
            case CALL:
                if (((Function) (inst.getOperands().get(0))).hasSideEffect()) System.out.println("remaining :: " + ((Function) (inst.getOperands().get(0))).getName());
                else System.out.println("removed :: " + ((Function) (inst.getOperands().get(0))).getName());
                return ((Function) (inst.getOperands().get(0))).hasSideEffect();
            default: return false;
        }
    }

    public void markInst(Instruction inst) {
        Queue<Instruction> q = new LinkedList<>();
        q.offer(inst);
        while (!q.isEmpty()) {
            inst = q.poll();
            if (usefulInstr.contains(inst)) {
                continue;
            }
            usefulInstr.add(inst);
            for (Value op : inst.getOperands()) {
                if (op instanceof Instruction) {
                    q.offer((Instruction) op);
                }
            }
        }
    }

    public void removeDeadFunction() {
        Queue<Function> q = new LinkedList<>();
        for (IList.INode<Function, Module> funcNode : m.getFunctionList()) {
            Function func = funcNode.getValue();
            if (!func.isBuiltIn() && func.getCallers().isEmpty() && !func.getName().equals("main")) {
                q.offer(func);
            }
        }
        while (!q.isEmpty()) {
            Function func = q.poll();
            for (GlobalVariable gv : m.getGlobalVariables()) {
                gv.getUseList().removeIf(
                        use -> ((Instruction) use.user).getParent().getParent().equals(func)
                );
            }
            func.getINode().removeSelf();
            changed = true;
            for (Function callee : func.getCallees()) {
                callee.getCallers().remove(func);
                if (callee.getCallers().isEmpty()) {
                    q.offer(callee);
                }
            }
        }
    }
}
