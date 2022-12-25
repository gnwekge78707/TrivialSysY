package midend.pass;

public class ConstantLoopUnroll {
    /*
    public void run(Module m) {
        for (IList.INode<Function, Module> fNode : m.getFunctionList()) {
            if (!fNode.getValue().isBuiltIn()) {
                run(fNode.getValue());
            }
        }
    }

    private void run(Function f) {
        boolean tag = true;
        while (tag) {
            tag = false;
            for (LoopInfoAnalysis.Loop loop : f.getLoopInfo().loops) {
                if (checkConstantUnroll(loop)) {
                    if (run(loop)) {
                        tag = true;
                        break;
                    }
                }
            }
            if (tag) {
                new DominanceAnalysis().run(f);
                new LoopInfoAnalysis().calcLoopInfo(f);
            }
        }
    }

    boolean checkIcmp(LoopInfoAnalysis.Loop loop, int i) {
        if (loop.icmp.getOperand(0).equals(loop.phi)) {
            return BinaryInst.calcIcmp(loop.icmp.tag, i, loop.end);
        } else {
            return BinaryInst.calcIcmp(loop.icmp.tag, loop.end, i);
        }
    }

    int getNext(LoopInfoAnalysis.Loop loop, int i) {
        if (loop.iAfter.lhs().equals(loop.phi)) {
            return BinaryInst.calcAddSub(loop.iAfter.tag, i, ((Constant.ConstantInt) loop.iAfter.rhs()).getValue());
        } else {
            return BinaryInst.calcAddSub(loop.iAfter.tag, ((Constant.ConstantInt) loop.iAfter.lhs()).getValue(), i);
        }
    }

    private boolean run(LoopInfoAnalysis.Loop loop) {
        int latchPos = loop.header.getPredecessors().indexOf(loop.latchBB);
        BasicBlock exitBB = (BasicBlock) loop.header.getInstrList().getLast().getValue().getOperand(2);
        ArrayList<Loop> loops = new ArrayList<>();
        ArrayList<ValueCopy> valueMaps = new ArrayList<>();
        loops.add(loop);
        valueMaps.add(new ValueCopy(new HashMap<>()));
        LoopInfoAnalysis.Loop lastLoop = loop;
        if (!checkIcmp(loop, loop.start)) {
            BasicBlock pred = loop.header.getPredecessors().get(latchPos ^ 1);
            for (IList.INode<Instruction, BasicBlock> iNode : loop.header.getInstrList()) {
                Instruction instruction = iNode.getValue();
                if (instruction.tag == InstrTag.Phi) {
                    instruction.replaceSelfWith(instruction.getOperand(latchPos ^ 1));
                    instruction.removeSelfUses();
                    instruction.node.removeSelf();
                }
            }
            pred.getSuccessors().remove(loop.header);
            pred.getSuccessors().add(exitBB);
            Instruction lastBr = pred.getInstrList().getLast().getValue();
            int headerPos = lastBr.getOperands().indexOf(loop.header);
            lastBr.setOperand(headerPos, exitBB);
            headerPos = exitBB.getPredecessors().indexOf(loop.header);
            exitBB.getPredecessors().set(headerPos, pred);
            return true;
        }
        int cnt = 0;
        for (int i = getNext(loop, loop.start); checkIcmp(loop, i) && cnt < 30; i = getNext(loop, i)) {
            ++cnt;
        }
        //System.out.println(loop.bbs.size() + " - " + cnt);
        if (loop.bbs.size() > 20) {
            return false;
        }
        if (cnt >= 20) {
            return false;
        }
        if (cnt * loop.bbs.size() > 200) {
            return false;
        }
        for (int i = getNext(loop, loop.start); checkIcmp(loop, i); i = getNext(loop, i)) {
            HashMap<Value, Value> thisLoopVariables = new HashMap<>();
            ValueCopy lastValueMap = valueMaps.get(valueMaps.size() - 1);
            for (IList.INode<Instruction, BasicBlock> iNode : loop.header.getInstrList()) {
                if (iNode.getValue().tag == InstrTag.Phi) {
                    thisLoopVariables.put(iNode.getValue(), lastValueMap.findValue(iNode.getValue().getOperand(latchPos)));
                }
            }
            ValueCopy thisValueMap = new ValueCopy(thisLoopVariables);
            Loop thisLoop = LoopUnroll.copyLoop(loop, thisValueMap, lastLoop.latchBB);
            loops.add(thisLoop);
            lastLoop = thisLoop;
            valueMaps.add(thisValueMap);
        }
        lastLoop = loop;
        for (int i = 1; i < loops.size(); ++i) {
            Loop thisLoop = loops.get(i);
            BasicBlock endBB = lastLoop.latchBB;
            endBB.getSuccessors().remove(lastLoop.header);
            endBB.getSuccessors().add(thisLoop.bodyEntryBB);
            Instruction lastBr = endBB.getInstrList().getLast().getValue();
            int headerPos = lastBr.getOperands().indexOf(lastLoop.header);
            lastBr.setOperand(headerPos, thisLoop.bodyEntryBB);
            int thisLoopHeaderPos = thisLoop.bodyEntryBB.getPredecessors().indexOf(thisLoop.header);
            thisLoop.bodyEntryBB.getPredecessors().set(thisLoopHeaderPos, lastLoop.latchBB);
            lastLoop = thisLoop;
            for (IList.INode<Instruction, BasicBlock> iNode : thisLoop.header.getInstrList()) {
                Instruction instr = iNode.getValue();
                instr.removeSelfUses();
                instr.replaceSelfWith(null);
                instr.node.removeSelf();
            }
            thisLoop.header.node.removeSelf();
        }
        Instruction lastBr = lastLoop.latchBB.getInstrList().getLast().getValue();
        int headerPos = lastBr.getOperands().indexOf(lastLoop.header);
        lastBr.setOperand(headerPos, exitBB);
        lastLoop.latchBB.getSuccessors().add(exitBB);
        lastLoop.latchBB.getSuccessors().remove(lastLoop.header);
        int exitHeaderPos = exitBB.getPredecessors().indexOf(loop.header);
        exitBB.getPredecessors().set(exitHeaderPos, lastLoop.latchBB);
        ValueCopy lastValueMap = valueMaps.get(valueMaps.size() - 1);
        for (IList.INode<Instruction, BasicBlock> iNode : loop.header.getInstrList()) {
            Instruction instruction = iNode.getValue();
            if (instruction.tag == InstrTag.Phi) {
                Value inLoop = instruction.getOperand(latchPos ^ 1);
                Value outLoop = lastValueMap.findValue(instruction.getOperand(latchPos));
                for (int i = instruction.getUseList().size() - 1; i >= 0; i--) {// System.out.println(this + " - " + use.user + " - " + newValue);
                    var use = instruction.getUseList().get(i);
                    if (loop.bbs.contains(((Instruction) use.user).getParent())) {
                        use.user.setOperand(use.idxOfValueInUser, inLoop);
                    } else {
                        use.user.setOperand(use.idxOfValueInUser, outLoop);
                    }
                }
                instruction.getUseList().clear();
                instruction.removeSelfUses();
                instruction.node.removeSelf();
            } else if (instruction.tag == InstrTag.Br) {
                instruction.removeAllOperands();
                instruction.addOperand(loop.bodyEntryBB);
                for (BasicBlock nxt : loop.header.getSuccessors()) {
                    if (!nxt.equals(loop.bodyEntryBB)) {
                        nxt.getPredecessors().remove(loop.header);
                    }
                }
                loop.header.getSuccessors().clear();
                loop.header.getSuccessors().add(loop.bodyEntryBB);
            }
        }
        loop.header.getPredecessors().remove(loop.latchBB);
        return true;
    }

    private boolean checkConstantUnroll(LoopInfoAnalysis.Loop loop) {
        if (!LoopUnroll.checkUnroll(loop)) {
            return false;
        }
        int entryPos = loop.header.getPredecessors().indexOf(loop.latchBB) ^ 1;
        if (!(loop.phi.getOperand(entryPos) instanceof Constant.ConstantInt)) {
            return false;
        }
        loop.start = ((Constant.ConstantInt) loop.phi.getOperand(entryPos)).getValue();
        int endPos = loop.icmp.getOperands().indexOf(loop.phi) ^ 1;
        if (!(loop.icmp.getOperand(endPos) instanceof Constant.ConstantInt)) {
            return false;
        }
        loop.end = ((Constant.ConstantInt) loop.icmp.getOperand(endPos)).getValue();
        return true;
    }*/
}
