package midend.pass;

import midend.ir.Module;
import midend.ir.value.BasicBlock;
import midend.ir.value.Function;
import midend.ir.value.instr.Instruction;
import midend.ir.value.instr.terminator.BrInstr;
import util.IList;

public class BBPredSuccAnalysis {
    public void run(Module m) {
        for (IList.INode<Function, Module> funcNode : m.getFunctionList()) {
            Function func = funcNode.getValue();
            if (!func.isBuiltIn()) {
                run(func);
            }
        }
    }

    public void run(Function func) {
        initialize(func);
        buildBBPredSucc(func);
    }


    public void initialize(Function func) {
        for (IList.INode<BasicBlock, Function> bbNode: func.getBbList()) {
            BasicBlock bb = bbNode.getValue();
            bb.getPredecessors().clear();
            bb.getSuccessors().clear();
        }
    }

    public void buildBBPredSucc(Function func) {
        for (IList.INode<BasicBlock, Function> bbNode: func.getBbList()) {
            BasicBlock bb = bbNode.getValue();
            Instruction instr = bb.getInstrList().getLast().getValue();
            if (! (instr instanceof BrInstr)) {
                continue;
            }
            if (instr.getOperandNum() == 1) {
                addEdge(bb, (BasicBlock) instr.getOperands().get(0));
            } else if (instr.getOperandNum() == 3) {
                addEdge(bb, (BasicBlock) instr.getOperands().get(1));
                addEdge(bb, (BasicBlock) instr.getOperands().get(2));
            }
        }
    }

    public void addEdge(BasicBlock pred, BasicBlock succ) {
        pred.getSuccessors().add(succ);
        succ.getPredecessors().add(pred);
    }
}
