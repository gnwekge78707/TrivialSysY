package midend.pass;

import midend.ir.Module;
import midend.ir.Value;
import midend.ir.value.BasicBlock;
import midend.ir.value.Function;
import midend.ir.value.instr.Instruction;
import midend.ir.value.instr.mem.Copy;
import midend.ir.value.instr.mem.Move;
import midend.ir.value.instr.mem.PhiInstr;
import midend.ir.value.instr.terminator.BrInstr;
import util.IList;

import java.util.ArrayList;

public class RemovePhi {
    private Module m;

    public void run(Module module) {
        this.m = module;
        removePhiAddPCopy();
        removeCopies();
    }

    private void removePhiAddPCopy() {
        for (IList.INode<Function, Module> function: m.getFunctionList()) {
            if (!function.getValue().isBuiltIn()) {
                Function func = function.getValue();
                removeFuncPhi(func);
            }
        }
    }

    private void removeCopies() {
        for (IList.INode<Function, Module> function: m.getFunctionList()) {
            if (!function.getValue().isBuiltIn()) {
                Function func = function.getValue();
                removeFuncCopy(func);
            }
        }
    }

    private static int midCnt = 0;

    private void removeFuncPhi(Function function) {
        for (IList.INode<BasicBlock, Function> bbINode : function.getBbList()) {
            BasicBlock bb = bbINode.getValue();
            if (!(bb.getInstrList().getEntry().getValue() instanceof PhiInstr)) {
                continue;
            }
            ArrayList<BasicBlock> pres = new ArrayList<>();
            for (BasicBlock b : bb.getPredecessors()) {
                pres.add(b);
            }
            ArrayList<Copy> copies = new ArrayList<>();
            for (int i = 0; i < pres.size(); i++) {
                BasicBlock incomeBB = pres.get(i);

                if (incomeBB.getSuccessors().size() > 1) {
                    BasicBlock mid = new BasicBlock("remove_phi_mid" + (midCnt++), function);
                    Copy copy = new Copy(new ArrayList<>(), new ArrayList<>(), mid);
                    copies.add(copy);
                    addMidBB(incomeBB, mid, bb);
                } else {
                    Copy copy = new Copy(new ArrayList<>(), new ArrayList<>());
                    copy.getINode().insertNext(incomeBB.getInstrList().getLast());
                    copies.add(copy);
                }
            }
            //FIXME!! phi被 remove了，phi表示的变量也没了

            //System.out.println(function.getName() + "-" + bb.getName() + ":: " + pres.size());
            for (IList.INode<Instruction, BasicBlock> instrINode : bb.getInstrList()) {
                Instruction instr = instrINode.getValue();
                if (!(instr instanceof PhiInstr)) {
                    break;
                }
                ArrayList<Value> phiRHS = instr.getOperands();
                for (int i = 0; i < phiRHS.size(); i++) {
                    copies.get(i).addToPC(instr, phiRHS.get(i));
                    //System.out.println(instr + ":::---:::" + phiRHS.get(i));
                }
            }
            for (IList.INode<Instruction, BasicBlock> instrINode : bb.getInstrList()) {
                if (!(instrINode.getValue() instanceof PhiInstr)) {
                    break;
                }
                instrINode.getValue().getINode().removeSelf();
                //todo after remove, phi's uses still exist (and phi is numbered as before)
                // / do what? can we coalesce now?
                // / when converted to moves, probably gonna need extra effort to assign
                // / virtual variable to some ssa --> that can be global variable in mips
            }
            /*
            Instruction instr = bb.getInstrList().getEntry().getValue();
            while (instr instanceof PhiInstr) {
                LinkedList<Use> phiRHS = instr.getUseList();
                for (int i = 0; i < phiRHS.size(); i++) {
                    copies.get(i).addToPC(instr, phiRHS.get(i).value);
                }
                instr = instr.getINode().getNext().getValue();
            }

            instr = bb.getInstrList().getEntry().getValue();
            while (instr instanceof PhiInstr) {
                Instruction temp = instr;
                instr = instr.getINode().getNext().getValue();
                temp.getINode().removeSelf();
            }
             */
        }
        /*
        BasicBlock bb = function.getBbList().getEntry().getValue();
        while (bb.getINode().getNext().getValue() != null) {
            System.out.println(bb.getINode().getNext().getValue());
            if (!(bb.getInstrList().getEntry().getValue() instanceof PhiInstr)) {
                bb = bb.getINode().getNext().getValue();
                continue;
            }
            ArrayList<BasicBlock> pres = new ArrayList<>();
            for (BasicBlock b : bb.getPredecessors()) {
                pres.add(b);
            }
            ArrayList<Copy> copies = new ArrayList<>();
            for (int i = 0; i < pres.size(); i++) {
                BasicBlock incomeBB = pres.get(i);

                if (incomeBB.getSuccessors().size() > 1) {
                    BasicBlock mid = new BasicBlock("remove_phi_mid", function);
                    Copy copy = new Copy(new ArrayList<>(), new ArrayList<>(), incomeBB);
                    copies.add(copy);
                    addMidBB(incomeBB, mid, bb);
                } else {
                    Instruction endInstr = incomeBB.getInstrList().getLast().getValue();
                    Copy copy = new Copy(new ArrayList<>(), new ArrayList<>(), incomeBB);
                    endInstr.getINode().removeSelf();
                    endInstr.getINode().insertNext(copy.getINode());
                    copies.add(copy);
                }
            }

            Instruction instr = bb.getInstrList().getEntry().getValue();
            while (instr instanceof PhiInstr) {
                LinkedList<Use> phiRHS = instr.getUseList();
                for (int i = 0; i < phiRHS.size(); i++) {
                    copies.get(i).addToPC(instr, phiRHS.get(i).value);
                }
                instr = instr.getINode().getNext().getValue();
            }

            bb.getInstrList().getEntry().getValue();
            while (instr instanceof PhiInstr) {
                Instruction temp = instr;
                instr = instr.getINode().getNext().getValue();
                temp.getINode().removeSelf();
            }
        }

         */
    }

    private void addMidBB(BasicBlock src, BasicBlock mid, BasicBlock tag) {
        src.getSuccessors().remove(tag);
        src.getSuccessors().add(mid);
        mid.getPredecessors().add(src);
        mid.getSuccessors().add(tag);
        tag.getPredecessors().remove(src);
        tag.getPredecessors().add(mid);

        Instruction instr = src.getInstrList().getLast().getValue();
        assert instr instanceof BrInstr;
        BasicBlock thenBB = (BasicBlock) instr.getOperand(1);
        BasicBlock elseBB = (BasicBlock) instr.getOperand(2);

        if (tag == thenBB) {
            instr.setOperand(1, mid);
            new BrInstr(tag, mid);
        } else if (tag == elseBB) {
            instr.setOperand(2, mid);
            new BrInstr(tag, mid);
        } else {
            System.err.println("Panic At Remove PHI addMidBB");
        }
    }

    private static int removePhiCnt = 0;

    private void removeFuncCopy(Function function) {
        //HashMap<PhiInstr, GlobalVariable> phiVariables2gv = new HashMap<>();

        for (IList.INode<BasicBlock, Function> bbINode : function.getBbList()) {
            BasicBlock bb = bbINode.getValue();
            ArrayList<Instruction> moves = new ArrayList<>();
            ArrayList<Instruction> copies = new ArrayList<>();

            for (IList.INode<Instruction, BasicBlock> instrINode : bb.getInstrList()) {
                Instruction instr = instrINode.getValue();
                if (!(instr instanceof Copy)) continue;
                copies.add(instr);
                ArrayList<Value> dsts = ((Copy) instr).getLHS();
                ArrayList<Value> srcs = ((Copy) instr).getRHS();

                for (int i = 0; i < dsts.size(); i++) {
                    if (srcs.get(i).getName().equals(dsts.get(i).getName())) continue;
                    assert dsts.get(i) instanceof PhiInstr;
                    PhiInstr phiInstr = (PhiInstr) dsts.get(i);
                    //todo: phiInstr itself can be used as a virtual value
                    if (!function.getVirtualValues().contains(phiInstr)) {
                        function.getVirtualValues().add(phiInstr);
                    }
                    /*
                    if (!phiVariables2gv.containsKey(phiInstr)) {
                        GlobalVariable phiVar = new GlobalVariable(
                                "llvm_remove_phi_" + (removePhiCnt++),
                                phiInstr.getDstType(),
                                null);
                        phiVariables2gv.put(phiInstr, phiVar);
                    }
                    GlobalVariable phiVar = phiVariables2gv.get(phiInstr);
                     */
                    Instruction move = new Move(phiInstr, srcs.get(i));
                    moves.add(move);
                }
            }
            for (Instruction copy : copies) {
                copy.getINode().removeSelf();
            }
            for (Instruction move : moves) {
                move.getINode().insertNext(bb.getInstrList().getLast());
            }
        }
    }
}
