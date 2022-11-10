package midend.ir;

import driver.Config;
import driver.Output;
import midend.ir.value.BasicBlock;
import midend.ir.value.Function;
import midend.ir.value.GlobalVariable;
import midend.ir.value.instr.Instruction;
import util.IList;

import java.util.ArrayList;
import java.util.HashMap;

public class Module {
    private ArrayList<GlobalVariable> globalVariables = new ArrayList<>();
    private IList<Function, Module> functionList = new IList<>(this);

    private HashMap<String, Function> libs = new HashMap<>();

    private HashMap<String, Integer> strCon2Idx = new HashMap<>();

    public Module() { }

    public void addGlobalVar(GlobalVariable variable) {
        globalVariables.add(variable);
    }

    public ArrayList<GlobalVariable> getGlobalVariables() {
        return globalVariables;
    }

    public IList<Function, Module> getFunctionList() {
        return functionList;
    }

    public void registerLibFunc(HashMap<String, Function> libs) {
        this.libs = libs;
    }

    public void putStrCon(String str, int idx) {
        strCon2Idx.put(str, idx);
    }

    public HashMap<String, Integer> getStrCon2Idx() {
        return strCon2Idx;
    }

    public void nameVariable() {
        for (GlobalVariable variable : globalVariables) {
            if (!variable.getName().startsWith("@")) {
                variable.setName("@" + variable.getName());
            }
        }
        for (IList.INode<Function, Module> f : functionList) {
            if (f.getValue().isBuiltIn()) {
                continue;
            }
            int counter = 0;
            Function func = f.getValue();
            for (Function.Param arg : func.getParams()) {
                arg.setName("%" + String.valueOf(counter++));
            }
            for (IList.INode<BasicBlock, Function> bbINode : func.getBbList()) {
                bbINode.getValue().setName(String.valueOf(counter++));
                for (IList.INode<Instruction, BasicBlock> instrINode : bbINode.getValue().getInstrList()) {
                    if (instrINode.getValue().hasDst()) {
                        instrINode.getValue().setName("%" + counter++);
                    }
                }
            }
        }
    }

    public void dumpLLVM() {
        nameVariable();
        for (Function libFunc : libs.values()) {
            Output.getInstance().updateBuffer(Config.OutputLevel.MIDCODE, "declare " + libFunc);
        }
        for (GlobalVariable variable : globalVariables) {
            Output.getInstance().updateBuffer(Config.OutputLevel.MIDCODE, variable.toString());
        }
        int funcCnt = 0;
        for (IList.INode<Function, Module> f : functionList) {
            Function func = f.getValue();
            if (func.isBuiltIn()) {
                continue;
            }
            Output.getInstance().updateBuffer(Config.OutputLevel.MIDCODE,
                    "\ndefine dso_local " + func  + " {");
            for (IList.INode<BasicBlock, Function> bbINode : func.getBbList()) {
                BasicBlock bb = bbINode.getValue();
                if (!func.getBbList().getEntry().equals(bbINode)) {
                    Output.getInstance().updateBuffer(Config.OutputLevel.MIDCODE, bb + ":");
                }
                boolean afterTerminatorTag = false;
                for (IList.INode<Instruction, BasicBlock> instrNode : bbINode.getValue().getInstrList()) {
                    if (!afterTerminatorTag) {
                        Output.getInstance().updateBuffer(Config.OutputLevel.MIDCODE, instrNode.getValue().toString());
                    } else {
                        instrNode.removeSelf();
                    }
                    if (instrNode.getValue().getInstrType().isTerminator()) {
                        afterTerminatorTag = true;
                    }
                }
            }
            Output.getInstance().updateBuffer(Config.OutputLevel.MIDCODE, "}");
        }
    }

}
