package midend.ir.value.instr.terminator;

import backend.MipsAssembly;
import backend.template.MipsCalTemplate;
import backend.template.MipsFuncTemplate;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.Function;
import midend.ir.value.instr.Instruction;

import java.util.ArrayList;

public class CallInstr extends Instruction {
    private Value dstVar;
    private ArrayList<Value> funcRParams;

    public CallInstr(Function function, ArrayList<Value> funcRParams) {
        super(InstrType.CALL, ((LLVMType.Function) function.getType()).getRetType(), funcRParams.size() + 1);
        if (this.getType() instanceof LLVMType.Void) {
            setHasDst(false);
        }
        this.funcRParams = funcRParams;
        setOperand(0, function);
        for (int i = 0; i < funcRParams.size(); i++) {
            setOperand(i + 1, funcRParams.get(i));
        }
    }

    public CallInstr(Function function, ArrayList<Value> funcRParams, BasicBlock parent) {
        super(InstrType.CALL, ((LLVMType.Function) function.getType()).getRetType(),
                funcRParams.size() + 1, parent);
        if (this.getType() instanceof LLVMType.Void) {
            setHasDst(false);
        }
        this.funcRParams = funcRParams;
        setOperand(0, function);
        for (int i = 0; i < funcRParams.size(); i++) {
            setOperand(i + 1, funcRParams.get(i));
        }
    }

    public CallInstr(Function function, ArrayList<Value> funcRParams, BasicBlock parent, Value dstVar) {
        super(InstrType.CALL, ((LLVMType.Function) function.getType()).getRetType(),
                funcRParams.size() + 1, parent);
        if (this.getType() instanceof LLVMType.Void) {
            setHasDst(false);
        }
        this.dstVar = dstVar;
        this.funcRParams = funcRParams;
        setOperand(0, function);
        for (int i = 0; i < funcRParams.size(); i++) {
            setOperand(i + 1, funcRParams.get(i));
        }
    }

    public Function getFunction() {
        return (Function) getOperand(0);
    }

    public Value getDstVar() { return this.dstVar; }

    public boolean hasExplicitDstVar() {
        return this.dstVar != null;
    }

    public String getName() { return hasExplicitDstVar() ? dstVar.getName() : super.getName(); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t");
        if (((LLVMType.Function) this.getOperand(0).getType()).getRetType() instanceof LLVMType.Void) {
            sb.append("call ");
        }
        else {
            sb.append(this.getName()).append(" = call ");
        }
        sb.append(this.getType()).append(" @").append(this.getOperand(0).getName());
        sb.append("(");
        for (int i = 1; i < this.getOperands().size(); i++) {
            sb.append(this.getOperand(i).getType()).append(" ").append(this.getOperand(i).getName());
            if (i != this.getOperands().size() - 1)
                sb.append(",");
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public void toAssembly(MipsAssembly assembly) {
        //TODO: global register push & pop to runtime stack
        Value dst = hasExplicitDstVar() ? dstVar : this;
        if (getFunction().isBuiltIn()) {
            if (getFunction().getName().equals("getint")) {
                MipsFuncTemplate.mipsGetIntTemplate(dst, assembly);
                dst.getMipsMemContex().updateMem(assembly);
            } else if (getFunction().getName().equals("putint")) {
                if (getOperands().size() != 2) {
                    throw new Error("putint function has wrong params size");
                }
                MipsFuncTemplate.mipsPrintNumTemplate(getOperand(1), assembly);
            } else {
                throw new Error("unsupported builtin function");
            }
            return;
        }
        assembly.flushLocalRegisters();
        MipsFuncTemplate.mipsFuncCallTemplate(getFunction(), this.funcRParams, assembly);
        assembly.initLocalRegisters();
        if (this.getType() != LLVMType.Void.getVoid()) {
            int dstReg = dst.getMipsMemContex().appointRegister(assembly);
            MipsCalTemplate.mipsProcessMove(dstReg, MipsAssembly.v0, assembly);
            dst.getMipsMemContex().updateMem(assembly);
        }
    }
}
