package midend.pass;

import midend.ir.Module;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.Constant;
import midend.ir.value.Function;
import midend.ir.value.instr.Instruction;
import midend.ir.value.instr.mem.AllocaInstr;
import midend.ir.value.instr.mem.PhiInstr;
import midend.ir.value.instr.mem.StoreInstr;
import util.IList;

import java.util.*;

public class Mem2Reg {
    Stack<HashMap<AllocaInstr, Value>> stack = new Stack<>();
    HashMap<PhiInstr, AllocaInstr> phi2alloca = new HashMap<>();

    public void run(Module m) {
        for (IList.INode<Function, Module> funcNode : m.getFunctionList()) {
            Function func = funcNode.getValue();
            if (!func.isBuiltIn()) {
                run(func);
            }
        }
    }

    public void run(Function func) {
        HashMap<AllocaInstr, ArrayList<BasicBlock>> defs = new HashMap<>();
        ArrayList<AllocaInstr> allocas = new ArrayList<>();
        for (IList.INode<BasicBlock, Function> bbNode : func.getBbList()) {
            BasicBlock bb = bbNode.getValue();
            for (IList.INode<Instruction, BasicBlock> instNode : bb.getInstrList()) {
                Instruction inst = instNode.getValue();
                if (inst.getInstrType() == Instruction.InstrType.ALLOCA) {
                    if (((AllocaInstr) inst).getAllocated() == LLVMType.Int.getI32()) {
                        allocas.add((AllocaInstr) inst);
                        defs.put((AllocaInstr) inst, new ArrayList<>());
                    }
                }
                if (inst.getInstrType() == Instruction.InstrType.STORE) {
                    assert inst instanceof StoreInstr;
                    if (((StoreInstr) inst).getPointerValue() instanceof AllocaInstr) {
                        if(defs.containsKey((AllocaInstr) ((StoreInstr) inst).getPointerValue())) {
                            defs.get((AllocaInstr) ((StoreInstr) inst).getPointerValue()).add(bb);
                        }
                    }
                }
            }
        }
        Queue<BasicBlock> W = new LinkedList<>();
        for (AllocaInstr alloca : allocas) {
            for (IList.INode<BasicBlock, Function> bbNode : func.getBbList()) {
                BasicBlock bb = bbNode.getValue();
                bb.setDirty(false);
            }
            W.addAll(defs.get(alloca));
            while (!W.isEmpty()) {
                BasicBlock X = W.remove();
                for (BasicBlock Y : X.getDominanceFrontier()) {
                    if (!Y.isDirty()) {
                        Y.setDirty(true);
                        PhiInstr phi = new PhiInstr(alloca.getAllocated(), Y);
                        phi2alloca.put(phi, alloca);
                        if (!defs.get(alloca).contains(Y)) {
                            W.add(Y);
                        }
                    }
                }
            }
        }
        HashMap<AllocaInstr, Value> Values = new HashMap<>();
        for (AllocaInstr alloca : allocas) {
            if (alloca.getAllocated() == LLVMType.Int.getI32()) {
                Values.put(alloca, new Constant(LLVMType.Int.getI32(), 0));
            }
        }
        stack.push(Values);
        dfsBB(func.getBbList().getEntry().getValue());
    }

    public void dfsBB(BasicBlock bb) {
        HashMap<AllocaInstr, Value> Values = new HashMap<>(stack.peek());
        for (IList.INode<Instruction, BasicBlock> instNode : bb.getInstrList()) {
            Instruction inst = instNode.getValue();
            if (inst.getInstrType() == Instruction.InstrType.ALLOCA) {
                if (Values.get(inst) != null) {
                    instNode.removeSelf();
                }
            } else if (inst.getInstrType() == Instruction.InstrType.LOAD) {
                if (!(inst.getOperand(0) instanceof AllocaInstr)) {
                    continue;
                }
                AllocaInstr alloca = (AllocaInstr) inst.getOperand(0);
                if (Values.get(alloca) != null) {
                    instNode.removeSelf();
                    inst.replaceSelfWith(Values.get(alloca));
                    inst.removeAllUses();
                }
            } else if (inst.getInstrType() == Instruction.InstrType.STORE) {
                if (!(inst.getOperand(1) instanceof AllocaInstr)) {
                    continue;
                }
                AllocaInstr alloca = (AllocaInstr) inst.getOperand(1);
                if (Values.get(alloca) != null) {
                    instNode.removeSelf();
                    assert inst.getUseList().isEmpty();
                    inst.removeAllUses();
                    Values.put(alloca, inst.getOperand(0));
                }
            } else if (inst.getInstrType() == Instruction.InstrType.PHI) {
                AllocaInstr alloca = phi2alloca.get(inst);
                Values.put(alloca, inst);
            }
        }
        stack.push(Values);
        for (BasicBlock succ : bb.getSuccessors()) {
            for (IList.INode<Instruction, BasicBlock> instNode : succ.getInstrList()) {
                Instruction inst = instNode.getValue();
                if (inst.getInstrType() == Instruction.InstrType.PHI) {
                    int index = succ.getPredecessors().indexOf(bb);
                    inst.setOperand(index, Values.get(phi2alloca.get((PhiInstr) inst)));
                }
            }
        }
        for (BasicBlock dom : bb.getIdominateds()) {
            dfsBB(dom);
        }
        stack.pop();
    }
}
