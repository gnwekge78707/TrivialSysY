package midend.ir.value;

import backend.MipsAssembly;
import backend.template.MipsOtherTemplate;
import driver.Config;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.instr.Instruction;
import midend.ir.value.instr.PutStringInstr;
import midend.ir.value.instr.terminator.CallInstr;
import midend.pass.ConflictGraph;
import util.IList;

import java.util.ArrayList;
import java.util.HashSet;

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

    public String getMipsTagString() { return "bb_" + getFunction().getName() + "_" + toString(); }

    @Override
    public String toString() {
        return getName();
    }

    //===================================================================optimize support
    private BasicBlock idominator; //被谁直接支配
    private ArrayList<BasicBlock> idominateds = new ArrayList<>(); //该bb直接支配的bb
    private ArrayList<BasicBlock> dominators = new ArrayList<>(); //该bb的支配者们
    private ArrayList<BasicBlock> dominanceFrontier = new ArrayList<>(); //支配边界
    private int dominanceLevel;
    private boolean dirty;
    private HashSet<Value> activeDefSet = new HashSet<>();
    private HashSet<Value> activeUseSet = new HashSet<>();
    private HashSet<Value> activeInSet = new HashSet<>();
    private HashSet<Value> activeOutSet = new HashSet<>();

    public ArrayList<BasicBlock> getPredecessors() {
        return predecessors;
    }

    public ArrayList<BasicBlock> getSuccessors() {
        return successors;
    }

    public void setSuccessors(ArrayList<BasicBlock> newSuccessors) {
        successors = newSuccessors;
    }

    public BasicBlock getIdominator() {
        return idominator;
    }

    public void setIdominator(BasicBlock idominator) {
        this.idominator = idominator;
    }

    public ArrayList<BasicBlock> getIdominateds() {
        return idominateds;
    }

    public ArrayList<BasicBlock> getDominators() {
        return dominators;
    }

    public ArrayList<BasicBlock> getDominanceFrontier() {
        return dominanceFrontier;
    }

    public int getDominanceLevel() {
        return dominanceLevel;
    }

    public void setDominanceLevel(int dominanceLevel) {
        this.dominanceLevel = dominanceLevel;
    }

    public boolean isDirty () {
        return dirty;
    }

    public void setDirty (boolean dirty) {
        this.dirty = dirty;
    }

    public Function getParent() {
        return iNode.getParent().getHolder();
    }

    public HashSet<Value> getActiveDefSet() {
        return activeDefSet;
    }

    public HashSet<Value> getActiveOutSet() {
        return activeOutSet;
    }

    public HashSet<Value> getActiveInSet() {
        return activeInSet;
    }

    public HashSet<Value> getActiveUseSet() {
        return activeUseSet;
    }

    public void setActiveInSet(HashSet<Value> activeInSet) {
        this.activeInSet = activeInSet;
    }

    //====================================================================backend support
    public void toAssembly(MipsAssembly assembly) {
        assembly.initLocalRegisters();
        MipsOtherTemplate.mipsProcessComment("basicBlock_" + this.toString(), assembly);
        if (!getFunction().getBbList().getEntry().getValue().equals(this)) {
            MipsOtherTemplate.mipsProcessTag(getMipsTagString(), assembly);
        }
        int instrIdx = 0;
        for (IList.INode<Instruction, BasicBlock> instrNode : instrList) {
            Instruction instr = instrNode.getValue();
            if (instr == instrList.getLast().getValue() && instr.getInstrType() == Instruction.InstrType.BR) {
                bbDealRegister(assembly);
            }
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
        if (!instrList.isEmpty() && instrList.getLast().getValue().getInstrType() != Instruction.InstrType.BR) {
            bbDealRegister(assembly);
        }
        if (needInitRegAtEndOfBB) {
            assembly.initLocalRegisters();
        }
        //FIXME! could not tell whats wrong
    }

    private boolean needInitRegAtEndOfBB = false;

    public void bbDealRegister(MipsAssembly assembly) {
        if (!instrList.isEmpty()) {
            Instruction end = instrList.getLast().getValue();
            if (!Config.getInstance().hasOptimize(Config.Optimize.llvmMem2Reg)) {
                if (end != null && !end.getInstrType().isTerminator()) {
                    MipsOtherTemplate.mipsProcessComment("bb end flush reg::", assembly);
                    assembly.flushLocalRegisters();
                    needInitRegAtEndOfBB = true;
                    //assembly.initLocalRegisters();
                }
            } else {
                if (!Config.getInstance().hasOptimize(Config.Optimize.ssaGlobalRegAlloc)) {
                    if (end != null && end.getInstrType() != Instruction.InstrType.RET) {
                        MipsOtherTemplate.mipsProcessComment("bb end flush reg::", assembly);

                        if (Config.getInstance().hasOptimize(Config.Optimize.activeVariable)) {
                            assembly.flushLocalRegisters(this.getActiveOutSet());
                        } else {
                            assembly.flushLocalRegisters();
                        }
                        needInitRegAtEndOfBB = true;
                        //assembly.initLocalRegisters();
                    }
                } else {

                }
            }
            /*
            if (end != null && !end.getInstrType().isTerminator()) {
                assembly.flushLocalRegisters();
                assembly.initLocalRegisters();
            }*/
            //FIXME!!! without SSA, could be very wrong
            // flush's condition too weak,
        }
    }

    public void buildConflictGraph(ConflictGraph conflictGraph) {
        activeInSet.forEach(i -> activeInSet.forEach(j -> conflictGraph.link(i, j)));
        for (Value value : activeDefSet) {
            if (activeOutSet.contains(value))
            activeInSet.forEach(k -> conflictGraph.link(value, k));
        }
    }
}
