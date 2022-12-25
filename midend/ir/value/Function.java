package midend.ir.value;

import backend.MipsAssembly;
import backend.template.MipsMemTemplate;
import backend.template.MipsOtherTemplate;
import driver.Config;
import midend.ir.Module;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.instr.Instruction;
import midend.ir.value.instr.mem.PhiInstr;
import midend.pass.ConflictGraph;
import midend.pass.LoopInfoAnalysis;
import util.IList;

import java.util.ArrayList;
import java.util.HashSet;

public class Function extends Value {
    public static class Param extends Value {
        public Param(LLVMType type) { super(type, ""); }

        @Override
        public String toString() {
            return this.getType().toString();
        }
    }

    private IList.INode<Function, Module> iNode = new IList.INode<>(this);
    private IList<BasicBlock, Function> bbList = new IList<>(this);
    private ArrayList<Param> params = new ArrayList<>();
    private HashSet<PhiInstr> virtualValues = new HashSet<>();
    // todo: virtualValues 为被消掉的 phi节点，但是仍然作为变量被 move操作使用。
    private boolean isBuiltIn;

    public Function(String name, LLVMType llvmType, Module module, boolean isBuiltIn) {
        super(llvmType, name);
        this.isBuiltIn = isBuiltIn;
        this.iNode.setParent(module.getFunctionList());
        this.iNode.insertAtEnd(iNode.getParent());
        LLVMType funcType = this.getType();
        for (LLVMType paramType : ((LLVMType.Function) funcType).getParamsType()) {
            params.add(new Param(paramType));
        }
    }

    public IList<BasicBlock, Function> getBbList() {
        return bbList;
    }

    public IList.INode<Function, Module> getINode() {
        return iNode;
    }

    public ArrayList<Param> getParams() {
        return params;
    }

    public HashSet<PhiInstr> getVirtualValues() {
        return virtualValues;
    }

    public LLVMType getRetType() {
        assert this.getType() instanceof LLVMType.Function;
        return ((LLVMType.Function) this.getType()).getRetType();
    }

    public boolean isBuiltIn() {
        return isBuiltIn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(((LLVMType.Function) this.getType())
                .getRetType()).append(" @").append(this.getName()).append("(");
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).toString());
            if (i != params.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public String getMipsFuncName() {
        return "f_" + super.getName();
    }

    //===================================================================optimize support

    private final ArrayList<Function> callers = new ArrayList<>();
    private final ArrayList<Function> callees = new ArrayList<>();
    private HashSet<GlobalVariable> storeGVSet = new HashSet<>();
    private HashSet<GlobalVariable> loadGVSet = new HashSet<>();
    private boolean hasSideEffect = false;
    private boolean useGlobalVariable = false;
    private boolean dirty = false;
    private LoopInfoAnalysis.LoopInfo loopInfo = new LoopInfoAnalysis.LoopInfo();

    public ArrayList<Function> getCallers() {
        return callers;
    }

    public ArrayList<Function> getCallees() {
        return callees;
    }

    public boolean hasSideEffect() {
        return hasSideEffect;
    }

    public void setHasSideEffect(boolean hasSideEffect) {
        this.hasSideEffect = hasSideEffect;
    }

    public boolean useGlobalVariable() {
        return useGlobalVariable;
    }

    public void setUseGlobalVariable(boolean useGlobalVariable) {
        this.useGlobalVariable = useGlobalVariable;
    }

    public HashSet<GlobalVariable> getStoreGVSet() {
        return storeGVSet;
    }

    public HashSet<GlobalVariable> getLoadGVSet() {
        return loadGVSet;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }

    public LoopInfoAnalysis.LoopInfo getLoopInfo() {
        return loopInfo;
    }

    public void removeSelf() {
        for (IList.INode<BasicBlock, Function> bbNode : this.getBbList()) {
            for (IList.INode<Instruction, BasicBlock> iNode : bbNode.getValue().getInstrList()) {

            }
        }
    }

    //====================================================================backend support
    public void toAssembly(MipsAssembly assembly) {
        //TODO! distribute local global registers for graphAlloc
        if (Config.getInstance().hasOptimize(Config.Optimize.ssaGlobalRegAlloc)) {
            assembly.initForNewFunction(18);
        } else {
            assembly.initForNewFunction(0);
        }
        MipsOtherTemplate.mipsProcessComment("function_" + toString() + ":", assembly);
        MipsOtherTemplate.mipsProcessTag(getMipsFuncName(), assembly);

        if (Config.getInstance().hasOptimize(Config.Optimize.ssaGlobalRegAlloc)) {
            ConflictGraph conflictGraph = new ConflictGraph();
            for (IList.INode<BasicBlock, Function> bbINode : bbList) {
                BasicBlock bb = bbINode.getValue();
                bb.buildConflictGraph(conflictGraph);
            }
            conflictGraph.colorNode();

            for (Value param : getParams()) {
                int paramReg = param.getMipsMemContex().getRegister();
                if (paramReg > 0) {
                    MipsMemTemplate.mipsProcessLw(paramReg,
                            param.getMipsMemContex().getOffset(),
                            param.getMipsMemContex().getBase(),
                            assembly);
                }
            }
        }

        assembly.initLocalRegisters();
        for (IList.INode<BasicBlock, Function> bbNode : bbList) {
            BasicBlock bb = bbNode.getValue();
            bb.toAssembly(assembly);
        }
    }
}
