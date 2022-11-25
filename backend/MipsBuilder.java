package backend;

import backend.isa.MipsInstruction;
import backend.template.MipsOtherTemplate;
import driver.Config;
import driver.Output;
import midend.ir.Module;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.Constant;
import midend.ir.value.Function;
import midend.ir.value.GlobalVariable;
import midend.ir.value.instr.Instruction;
import util.IList;

import java.util.ArrayList;
import java.util.HashMap;

public class MipsBuilder {
    private MipsAssembly assembly;
    private Module llvmModule;
    private ArrayList<String> initString = new ArrayList<>();
    private ArrayList<Integer> initGlobalVar = new ArrayList<>();

    public MipsBuilder(Module llvmModule) {
        assembly = new MipsAssembly();
        this.llvmModule = llvmModule;
    }

    private Function mainFunction;

    private void calRuntimeStackSize() {
        int globalStackSpace = 0;
        for (GlobalVariable gv : llvmModule.getGlobalVariables()) {
            int gvSpace = gv.getMipsMemContex().getSpace();
            gv.getMipsMemContex().setOffset(globalStackSpace);
            globalStackSpace += gvSpace;
            //todo -> calculate global init space(above) & value(below)
            System.out.println(gv.getName() + ", " + gv.getMipsMemContex().getOffset());
            assert gv.getType() instanceof LLVMType.Pointer;
            LLVMType gvType = ((LLVMType.Pointer) gv.getType()).getPointedTo();
            if (gvType instanceof LLVMType.Int) {
                if (gv.getInit() instanceof Constant) {
                    initGlobalVar.add(((Constant) gv.getInit()).getConstVal());
                } else {
                    initGlobalVar.add(0);
                }
            } else if (gvType instanceof LLVMType.Array) {
                if (gv.getInits() == null) {
                    for (int i = 0; i < ((LLVMType.Array) gvType).getLength(); i++) {
                        initGlobalVar.add(0);
                    }
                } else {
                    for (Value value : gv.getInits()) {
                        initGlobalVar.add(((Constant) value).getConstVal());
                    }
                }
            }
        }
        if (globalStackSpace > 0) {
            assembly.addObjectCode(MipsInstruction.getLa(MipsAssembly.gp, "global"));
        }
        // todo -> globalStack(above), RuntimeStack(below)
        for (IList.INode<Function, Module> functionNode : llvmModule.getFunctionList()) {
            Function func = functionNode.getValue();
            if (func.isBuiltIn()) {
                continue;
            }
            if (func.getName().equals("main")) {
                mainFunction = func;
            }
            System.out.println("============================== in  function :: " + func.getName());
            int funcStackSpace = (1 << 2); // ra + params (i32, i32*)
            for (Function.Param param : func.getParams()) {
                param.getMipsMemContex().setOffset(funcStackSpace);
                System.out.println(param.getName() + ", addr = " + param.getMipsMemContex().getOffset());
                funcStackSpace += param.getMipsMemContex().getSpace();
            }
            for (IList.INode<BasicBlock, Function> bbNode : func.getBbList()) {
                BasicBlock bb = bbNode.getValue();
                for (IList.INode<Instruction, BasicBlock> instrNode : bb.getInstrList()) {
                    Instruction instr = instrNode.getValue();
                    int instrSpace = instr.getMipsMemContex().getSpace();
                    instr.getMipsMemContex().setOffset(funcStackSpace);
                    System.out.println(instr.getName() + ", space = " + instr.getMipsMemContex().getSpace() + ", addr = " + instr.getMipsMemContex().getOffset());
                    funcStackSpace += instrSpace;
                }
            }
            func.getMipsMemContex().setSpace(funcStackSpace);
        }
    }

    public void buildMipsAssembly() {
        // todo -> init global string constant
        HashMap<String, Integer> str2idx = llvmModule.getStrCon2Idx();
        str2idx.forEach((str, idx) -> initString.add("str_" + idx + ": .asciiz \"" + str + "\""));
        // todo -> globalStack (variable as well), RuntimeStack init
        calRuntimeStackSize();
        if (mainFunction == null) {
            throw new Error("missing main function");
        }
        // todo -> alloc $sp for mainFunction
        MipsOtherTemplate.processAllocMainStack(mainFunction.getMipsMemContex().getSpace(), assembly);
        // todo -> cal assembly to all functions
        mainFunction.toAssembly(assembly);
        for (IList.INode<Function, Module> functionNode : llvmModule.getFunctionList()) {
            Function func = functionNode.getValue();
            if (func.isBuiltIn()) {
                continue;
            }
            if (!func.getName().equals("main")) {
                func.toAssembly(assembly);
            }
        }
    }


    public void dumpStr(String s) {
        Output.getInstance().updateBuffer(Config.OutputLevel.MIPS, s);
    }

    public void dumpMipsAssembly() {
        if (initString.size() > 0 || initGlobalVar.size() > 0) {
            dumpStr(".data");
            if (initGlobalVar.size() > 0) {
                dumpStr("\tglobal:");
                /*
                String str = "\t";
                for (int i = 0; i < initGlobalVar.size(); i++) {
                    str = str + initGlobalVar.get(i) + " ";
                }*/
                StringBuilder builder = new StringBuilder();
                builder.append("\t");
                for (Integer i : initGlobalVar) {
                    builder.append(i).append(" ");
                }
                dumpStr(builder.toString());
            }
            initString.forEach(u -> dumpStr("\t" + u));
        }
        dumpStr("\n\n.text");
        for (MipsInstruction instruction : assembly.getObjectCode()) {
            String indent;
            if (instruction.toString().startsWith("# function")) {
                dumpStr("");
                indent = "";
            } else if (instruction.toString().startsWith("# basicBlock")) {
                indent = "\t";
            } else {
                indent = "\t\t";
            }
            dumpStr(indent + instruction.toString());
        }
    }
}
