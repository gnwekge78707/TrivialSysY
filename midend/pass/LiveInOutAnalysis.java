package midend.pass;

import midend.ir.Module;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.Constant;
import midend.ir.value.Function;
import midend.ir.value.instr.Instruction;
import midend.ir.value.instr.PutStringInstr;
import midend.ir.value.instr.mem.Copy;
import midend.ir.value.instr.mem.Move;
import midend.ir.value.instr.mem.StoreInstr;
import midend.ir.value.instr.terminator.BrInstr;
import midend.ir.value.instr.terminator.CallInstr;
import midend.ir.value.instr.terminator.RetInstr;
import util.IList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class LiveInOutAnalysis {
    public void run(Module m) {
        for (IList.INode<Function, Module> funcINode : m.getFunctionList()) {
            Function function = funcINode.getValue();
            if (!function.isBuiltIn()) {
                livenessAnalysis(function);
            }
        }
    }

    private ArrayList<Value> getInstrDefValue(Instruction instruction) {
        if (instruction instanceof Move) {
            return new ArrayList<>(Arrays.asList(((Move) instruction).getDst()));
        } else if (instruction instanceof StoreInstr || instruction instanceof BrInstr ||
                instruction instanceof RetInstr || instruction instanceof PutStringInstr) {
            return new ArrayList<>();
        } else if (instruction instanceof CallInstr) {
            if (instruction.getDstType() == LLVMType.Void.getVoid()) {
                return new ArrayList<>();
            } else {
                return new ArrayList<>(Arrays.asList(instruction));
            }
        } else {
            assert !(instruction instanceof Copy);
            return new ArrayList<>(Arrays.asList(instruction));
        }
    }

    private ArrayList<Value> getInstrUseValue(Instruction instruction) {
        if (instruction instanceof Move) {
            if (((Move) instruction).getSrc() instanceof Constant) {
                return new ArrayList<>();
            }
            return new ArrayList<>(Arrays.asList(((Move) instruction).getSrc()));
        } else {
            ArrayList<Value> res = new ArrayList<>();
            for (Value value : instruction.getOperands()) {
                if (value instanceof Constant || value.getType() instanceof LLVMType.Label) {
                    continue;
                }
                res.add(value);
            }
            return res;
        }
    }

    private void livenessAnalysis(Function function) {
        System.out.println("====func====" + function.getName());
        for (IList.INode<BasicBlock, Function> bbINode : function.getBbList()) {
            BasicBlock bb = bbINode.getValue();
            bb.getActiveDefSet().clear();
            bb.getActiveUseSet().clear();
            for (IList.INode<Instruction, BasicBlock> instrINode : bb.getInstrList()) {
                Instruction instr = instrINode.getValue();
                for (Value value : this.getInstrUseValue(instr)) {
                    if (!bb.getActiveDefSet().contains(value)) bb.getActiveUseSet().add(value);
                }
                for (Value value : this.getInstrDefValue(instr)) {
                    if (!bb.getActiveUseSet().contains(value)) bb.getActiveDefSet().add(value);
                }
            }
            bb.setActiveInSet(new HashSet<>(bb.getActiveUseSet()));
            bb.getActiveOutSet().clear();
            System.out.println("activeDefUse: " + bb.getName() + " |-def-> " + getSetStr(bb.getActiveDefSet()) + " |-use-> " + getSetStr(bb.getActiveUseSet()));
        }
        liveInOutAnalysis(function);
    }

    public void liveInOutAnalysis(Function function) {
        boolean changed = true;
        ArrayList<BasicBlock> basicBlocks = new ArrayList<>();
        for (IList.INode<BasicBlock, Function> bbINode : function.getBbList()) {
            basicBlocks.add(bbINode.getValue());
        }
        while (changed) {
            changed = false;
            for (int i = basicBlocks.size() - 1; i >= 0; i--) {
                BasicBlock bb = basicBlocks.get(i);
                ArrayList<Value> newLiveOut = new ArrayList<>();
                for (BasicBlock successors : bb.getSuccessors()) {
                    for (Value liveIn : successors.getActiveInSet()) {
                        if (bb.getActiveOutSet().add(liveIn)) {
                            newLiveOut.add(liveIn);
                        }
                    }
                }
                if (newLiveOut.size() > 0) {
                    changed = true;
                }
                if (changed) {
                    for (Value o : bb.getActiveOutSet()) {
                        if (!bb.getActiveDefSet().contains(o)) {
                            bb.getActiveInSet().add(o);
                        }
                    }
                }
            }
        }
        for (IList.INode<BasicBlock, Function> bbINode : function.getBbList()) {
            BasicBlock bb = bbINode.getValue();
            System.out.println("activeInOut: " + bb.getName() + " |-in-> " + getSetStr(bb.getActiveInSet()) + " |-out-> " + getSetStr(bb.getActiveOutSet()));
        }
    }

    private String getSetStr(HashSet<Value> set) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (Value value : set) {
            stringBuilder.append(value.getName()).append(", ");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
