package midend.ir.value;

import backend.MipsAssembly;
import backend.template.MipsOtherTemplate;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.instr.Instruction;
import midend.ir.value.instr.PutStringInstr;
import midend.ir.value.instr.terminator.CallInstr;
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

    public Function getFunction() {
        return iNode.getParent().getHolder();
    }

    @Override
    public String toString() {
        return getName();
    }

    //====================================================================backend support
    public void toAssembly(MipsAssembly assembly) {
        assembly.initLocalRegisters();
        MipsOtherTemplate.mipsProcessComment("basicBlock_" + this.toString(), assembly);
        if (!getFunction().getBbList().getEntry().getValue().equals(this)) {
            MipsOtherTemplate.mipsProcessTag(toString() + "_" + getFunction().getName(), assembly);
        }
        int instrIdx = 0;
        for (IList.INode<Instruction, BasicBlock> instrNode : instrList) {
            Instruction instr = instrNode.getValue();
            if (!(instr instanceof PutStringInstr)) {
                MipsOtherTemplate.mipsProcessComment(instr.toString(), assembly);
            }
            instr.setPlaceInfo(this, instrIdx);
            //System.out.println(instr.toString() + "__________" + instrIdx);
            if (instr instanceof CallInstr) {
                instrIdx += instr.getOperandNum();
            } else {
                instrIdx++;
            }
            instr.toAssembly(assembly);
        }
        if (!instrList.isEmpty()) {
            Instruction end = instrList.getLast().getValue();
            if (end != null && !end.getInstrType().isTerminator()) {
                assembly.flushLocalRegisters();
                assembly.initLocalRegisters();
            }
        }
        //FIXME! could not tell whats wrong
    }
}
