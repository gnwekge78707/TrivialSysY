package midend.pass;

import midend.ir.Module;
import midend.ir.value.BasicBlock;
import midend.ir.value.Function;
import util.IList;

import java.util.ArrayList;
import java.util.BitSet;

public class DominanceAnalysis {
    public void run(Module m) {
        for (IList.INode<Function, Module> funcNode : m.getFunctionList()) {
            Function func = funcNode.getValue();
            if (!func.isBuiltIn()) {
                run(func);
            }
        }
    }

    public void run(Function func) {
        int blockNum = func.getBbList().getNodeNum();
        ArrayList<BitSet> dominatorSet = new ArrayList<>(blockNum);
        ArrayList<BasicBlock> blockList = new ArrayList<>();
        int i = 0;
        for (IList.INode<BasicBlock, Function> bbNode : func.getBbList()) {
            BasicBlock bb = bbNode.getValue();
            blockList.add(bb);
            bb.getDominators().clear();
            bb.getDominanceFrontier().clear();
            bb.getIdominateds().clear();
            dominatorSet.add(new BitSet(blockNum));
            if (bbNode == func.getBbList().getEntry()) {
                dominatorSet.get(i).set(i);
            } else {
                dominatorSet.get(i).set(0, blockNum);
            }
            i++;
        }
        boolean flag = true;
        while (flag) {
            flag = false;
            i = 0;
            for (IList.INode<BasicBlock, Function> bbNode : func.getBbList()) {
                if (bbNode != func.getBbList().getEntry()) {
                    BasicBlock bb = bbNode.getValue();
                    BitSet tmp = new BitSet();
                    tmp.set(0, blockNum);
                    for (BasicBlock pred : bb.getPredecessors()) {
                        tmp.and(dominatorSet.get(blockList.indexOf(pred)));
                    }
                    tmp.set(i);
                    if (!tmp.equals(dominatorSet.get(i))) {
                        flag = true;
                        dominatorSet.get(i).clear();
                        dominatorSet.get(i).or(tmp);
                    }
                }
                i++;
            }
        }
        for (i = 0; i < blockNum; ++i) {
            BasicBlock bb = blockList.get(i);
            BitSet dominators = dominatorSet.get(i);
            for (int j = 0; j < blockNum; ++j) {
                if (dominators.get(j)) {
                    bb.getDominators().add(blockList.get(j));
                }
            }
        }
        for (i = 0; i < blockNum; ++i) {
            BasicBlock bb = blockList.get(i);
            for (BasicBlock dominator1 : bb.getDominators()) {
                flag = true;
                if (dominator1.getDominators().size() != bb.getDominators().size() - 1) {
                    flag = false;
                }
                if (flag) {
                    assert dominator1.getDominators().size() == bb.getDominators().size() - 1;
                    bb.setIdominator(dominator1);
                    dominator1.getIdominateds().add(bb);
                    break;
                }
            }
        }
        dfsDominanceLevel(func.getBbList().getEntry().getValue(), 0);
        // compute the dominance frontier
        for (IList.INode<BasicBlock, Function> bbNode : func.getBbList()) {
            BasicBlock bb = bbNode.getValue();
            for (BasicBlock pred : bb.getPredecessors()) {
                BasicBlock runner = pred;
                while (runner != bb.getIdominator()) {
                    runner.getDominanceFrontier().add(bb);
                    runner = runner.getIdominator();
                }
            }
        }
    }

    public void dfsDominanceLevel(BasicBlock bb, int level) {
        bb.setDominanceLevel(level);
        for (BasicBlock dominated : bb.getIdominateds()) {
            dfsDominanceLevel(dominated, level + 1);
        }
    }
}
