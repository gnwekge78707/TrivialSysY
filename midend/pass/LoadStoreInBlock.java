package midend.pass;

import midend.ir.Module;
import midend.ir.Value;
import midend.ir.value.BasicBlock;
import midend.ir.value.Function;
import midend.ir.value.instr.Instruction;
import midend.ir.value.instr.mem.LoadInstr;
import midend.ir.value.instr.mem.PhiInstr;
import midend.ir.value.instr.mem.StoreInstr;
import midend.ir.value.instr.terminator.BrInstr;
import util.IList;

import java.util.HashMap;

public class LoadStoreInBlock {
    public void run(Module module) {
        for (IList.INode<Function, Module> fNode : module.getFunctionList()) {
            if (!fNode.getValue().isBuiltIn()) {
                run(fNode.getValue());
            }
        }
    }

    private void run(Function function) {
        for (IList.INode<BasicBlock, Function> bNode : function.getBbList()) {
            run(bNode.getValue());
        }
    }

    public static void removeBrFlow(BasicBlock fr, BasicBlock to) {
        int pos = to.getPredecessors().indexOf(fr);
        for (IList.INode<Instruction, BasicBlock> iNode : to.getInstrList()) {
            if (iNode.getValue() instanceof PhiInstr) {
                ((PhiInstr) iNode.getValue()).removeIncomingValue(pos);
            }
        }
        to.getPredecessors().remove(pos);
        fr.getSuccessors().remove(to);
    }

    private void replaceInstruction(Instruction origin, Value value) {
        if (origin == value) {
            return;
        }
        if (origin.getInstrType() == Instruction.InstrType.BR) {
            assert origin.getOperandNum() == 3;
            assert value instanceof BrInstr;
            assert ((BrInstr) value).getOperandNum() == 1;
            BasicBlock nxt = (BasicBlock) ((BrInstr) value).getOperand(0);
            if (nxt.equals(origin.getOperand(1))) {
                removeBrFlow(origin.getParent(), (BasicBlock) origin.getOperand(2));
            } else {
                removeBrFlow(origin.getParent(), (BasicBlock) origin.getOperand(1));
            }
        }
        origin.replaceSelfWith(value);
        origin.removeSelfUses();
        origin.getINode().removeSelf();
    }

    private void run(BasicBlock bb) {
        HashMap<Value, Value> loadedValues = new HashMap<>();
        for (IList.INode<Instruction, BasicBlock> iNode : bb.getInstrList()) {
            Instruction instruction = iNode.getValue();
            if (instruction.getInstrType() == Instruction.InstrType.LOAD) {
                if (loadedValues.containsKey(((LoadInstr) instruction).getPointer())) {
                    replaceInstruction(instruction, loadedValues.get(((LoadInstr) instruction).getPointer()));
                } else {
                    loadedValues.put(((LoadInstr) instruction).getPointer(), instruction);
                }
            } else if (instruction.getInstrType() == Instruction.InstrType.STORE) {
                loadedValues.clear();
                // System.out.println("Store : " + ((MemInst.Store) instruction).getPointer() + " - " + ((MemInst.Store) instruction).getValue());
                loadedValues.put(((StoreInstr) instruction).getOperand(1), ((StoreInstr) instruction).getOperand(0));
            } else if (instruction.getInstrType() == Instruction.InstrType.CALL) {
                loadedValues.clear();
            }
        }
    }
}
