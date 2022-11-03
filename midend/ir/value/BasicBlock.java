package midend.ir.value;

import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.instr.Instruction;
import util.IList;

import java.util.ArrayList;

public class BasicBlock extends Value {
    private IList.INode<BasicBlock, Function> iNode = new IList.INode<>(this);
    private IList<Instruction, BasicBlock> instrList = new IList<>(this);
    private ArrayList<BasicBlock> predecessors = new ArrayList<>();
    private ArrayList<BasicBlock> successors = new ArrayList<>();

    public BasicBlock(String name) {
        super(LLVMType.Label.getLabel(), name);
    }

    public BasicBlock(String name, Function function) {
        super(LLVMType.Label.getLabel(), name);
        iNode.setParent(function.getBbList());
        iNode.insertAtEnd(iNode.getParent());
    }

    public IList<Instruction, BasicBlock> getInstrList() {
        return instrList;
    }

    public IList.INode<BasicBlock, Function> getINode() {
        return iNode;
    }

    @Override
    public String toString() {
        return getName();
    }
}
