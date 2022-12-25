package midend.pass;

import midend.ir.Module;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.Function;
import midend.ir.value.GlobalVariable;
import midend.ir.value.instr.Instruction;
import midend.ir.value.instr.mem.AllocaInstr;
import util.IList;

import java.util.HashMap;

public class InterProcedureAnalysis {
    HashMap<GlobalVariable, Boolean> gvMap = new HashMap<>();
    Module m;

    public void run(Module m) {
        this.m = m;
        gvMap.clear();
        for (GlobalVariable gv : m.getGlobalVariables()) {
            gvMap.put(gv, false);
        }
        for (IList.INode<Function, Module> funcNode : m.getFunctionList()) {
            Function func = funcNode.getValue();
            func.getCallees().clear();
            func.getCallers().clear();
            func.setHasSideEffect(func.isBuiltIn());
            func.setUseGlobalVariable(func.isBuiltIn());
            func.getLoadGVSet().clear();
            func.getStoreGVSet().clear();
            func.setDirty(false);
        }
        for (IList.INode<Function, Module> funcNode : m.getFunctionList()) {
            Function func = funcNode.getValue();
            analyze(func);
        }
        for (IList.INode<Function, Module> funcNode : m.getFunctionList()) {
            Function func = funcNode.getValue();
            if (func.getName().equals("main")) {
                dfs(func);
                break;
            }
        }
        for (IList.INode<Function, Module> funcNode : m.getFunctionList()) {
            Function func = funcNode.getValue();
            System.out.println("interProced:::::" + func.getName() + "\t\t::" + func.hasSideEffect());
        }
    }

    public void analyze(Function func) {
        for (IList.INode<BasicBlock, Function> bbNode : func.getBbList()) {
            BasicBlock bb = bbNode.getValue();
            for (IList.INode<Instruction, BasicBlock> instrNode : bb.getInstrList()) {
                Instruction instr = instrNode.getValue();
                switch (instr.getInstrType()) {
                    case PUTSTR: {
                        Function callee = m.getLibFunc("putch");
                        func.getCallees().add(callee);
                        callee.getCallers().add(func);
                        break;
                    }
                    case CALL: {
                        Function callee = (Function) instr.getOperands().get(0);
                        func.getCallees().add(callee);
                        callee.getCallers().add(func);
                        break;
                    }
                    case LOAD: {
                        Value pointer = instr.getOperands().get(0);
                        if (pointer instanceof GlobalVariable) {
                            func.getLoadGVSet().add((GlobalVariable) pointer);
                        }
                        break;
                    }
                    case STORE: {
                        Value pointer = instr.getOperands().get(1);
                        if (pointer instanceof GlobalVariable) {
                            gvMap.put((GlobalVariable) pointer, true);
                            func.getStoreGVSet().add((GlobalVariable) pointer);
                        }
                        if (pointer instanceof AllocaInstr && !(((AllocaInstr) pointer).getAllocated() instanceof LLVMType.Pointer)) {
                            continue;
                        }
                        pointer = AliasAnalysis.getArray(pointer);
                        func.setHasSideEffect(true);
                        /*
                        if (AliasAnalysis.isGlobal(pointer) || AliasAnalysis.isParam(pointer)) {
                            func.setHasSideEffect(true);
                        }

                         */
                        break;
                    }
                }
            }
        }
    }

    public void dfs(Function func) {
        if (func.isDirty()) {
            return;
        }
        func.setDirty(true);
        for (Function callee : func.getCallees()) {
            dfs(callee);
            func.getStoreGVSet().addAll(callee.getStoreGVSet());
            func.getLoadGVSet().addAll(callee.getLoadGVSet());
            if (callee.hasSideEffect()) {
                func.setHasSideEffect(true);
            }
            if (callee.useGlobalVariable()) {
                func.setUseGlobalVariable(true);
            }
        }
    }
}
