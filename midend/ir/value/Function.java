package midend.ir.value;

import backend.MipsAssembly;
import backend.template.MipsOtherTemplate;
import midend.ir.Module;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import util.IList;

import java.util.ArrayList;

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

    //====================================================================backend support
    public void toAssembly(MipsAssembly assembly) {
        //TODO! distribute local global registers for graphAlloc
        assembly.initForNewFunction(0);
        MipsOtherTemplate.mipsProcessComment("function_" + toString() + ":", assembly);
        MipsOtherTemplate.mipsProcessTag(getMipsFuncName(), assembly);
        for (IList.INode<BasicBlock, Function> bbNode : bbList) {
            BasicBlock bb = bbNode.getValue();
            bb.toAssembly(assembly);
        }
    }
}
