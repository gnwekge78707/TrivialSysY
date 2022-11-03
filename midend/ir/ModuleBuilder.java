package midend.ir;

import midend.ir.Module;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.Function;
import midend.ir.value.GlobalVariable;
import midend.ir.value.instr.Instruction;
import midend.ir.value.instr.terminator.BrInstr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

public class ModuleBuilder {
    private Module module;
    private Function curFunction;
    private BasicBlock curBasicBlock;
    private int basicBlockCnt;

    private HashMap<String, Function> libs = new HashMap<>();
    private HashMap<String, Integer> strCon2Idx = new HashMap<>();

    public ModuleBuilder() {
        this.module = new Module();
        this.basicBlockCnt = 0;
        libs.put("getint", new Function("getint",
                new LLVMType.Function(LLVMType.Int.getI32(), new ArrayList<>()),
                module, true)
        );
        libs.put("putint", new Function("putint",
                new LLVMType.Function(LLVMType.Void.getVoid(), new ArrayList<>(Arrays.asList(LLVMType.Int.getI32()))),
                module, true)
        );
        libs.put("putch", new Function("putch",
                new LLVMType.Function(LLVMType.Void.getVoid(), new ArrayList<>(Arrays.asList(LLVMType.Int.getI32()))),
                module, true)
        );
        module.registerLibFunc(libs);
    }

    public void putGlobalVar(GlobalVariable globalVariable) {
        module.addGlobalVar(globalVariable);
    }

    public Function putFunction(String name, LLVMType llvmType, boolean isBuiltIn) {
        curFunction = new Function(name, llvmType, module, isBuiltIn);
        return curFunction;
    }

    public BasicBlock putBasicBlock(String name) {
        basicBlockCnt++;
        return new BasicBlock(name + getBasicBlockCnt(), curFunction);
    }

    public BasicBlock putBasicBlockAsCur(String name) {
        basicBlockCnt++;
        curBasicBlock = new BasicBlock(name, curFunction);
        return curBasicBlock;
    }

    public Instruction putBrInstr(BasicBlock target, BasicBlock from) {
        if (from.getInstrList().getLast().getValue() != null &&
                from.getInstrList().getLast().getValue().getInstrType().isTerminator()) {
            return null;
        } else {
            return new BrInstr(target, from);
        }
    }

    public Instruction putBrInstr(Value cond, BasicBlock trueBB, BasicBlock falseBB, BasicBlock from) {
        if (from.getInstrList().getLast().getValue() != null &&
                from.getInstrList().getLast().getValue().getInstrType().isTerminator()) {
            return null;
        } else {
            return new BrInstr(cond, trueBB, falseBB, from);
        }
    }

    public int putStrCon(String str) {
        if (strCon2Idx.containsKey(str)) {
            return strCon2Idx.get(str);
        }
        int idx = strCon2Idx.size();
        strCon2Idx.put(str, idx);
        module.putStrCon(str, idx);
        return idx;
    }

    public Module getModule() {
        return module;
    }

    public void setCurBasicBlock(BasicBlock curBasicBlock) {
        this.curBasicBlock = curBasicBlock;
    }

    public BasicBlock getCurBasicBlock() { return curBasicBlock; }

    public Function getCurFunction() { return curFunction; }

    public int getBasicBlockCnt() { return basicBlockCnt; }

    public Function getLibFunc(String name) { return libs.get(name); }

    //-------------for break & continue-------------
    private Stack<BasicBlock> loopCondBB = new Stack<>();
    private Stack<BasicBlock> loopNextBB = new Stack<>();

    public Stack<BasicBlock> getLoopCondBB() {
        return loopCondBB;
    }

    public Stack<BasicBlock> getLoopNextBB() {
        return loopNextBB;
    }
}
