package midend.pass;

import midend.ir.Module;
import midend.ir.value.Function;
import midend.ir.value.GlobalVariable;
import midend.ir.value.instr.Instruction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class GVL {
    private Module m;
    private HashMap<Function, HashSet<GlobalVariable>> func2gv = new HashMap<>();
    private HashMap<GlobalVariable, HashSet<Function>> gv2func = new HashMap<>();
    private HashSet<Function> recursiveFuncs = new HashSet<>();

    public void run(Module m) {
        this.m = m;
        removeDeadFunction();
    }

    public void localize() {
        for (GlobalVariable gv : m.getGlobalVariables()) {
            HashSet<Function> funcs = gv2func.get(gv);
        }
    }

    public void removeDeadFunction() {
        Queue<Function> q = new LinkedList<>();
        for (util.IList.INode<Function, Module> funcNode : m.getFunctionList()) {
            Function func = funcNode.getValue();
            if (!func.isBuiltIn() && func.getCallers().isEmpty() && !func.getName().equals("main")) {
                q.offer(func);

            }
        }
        while (!q.isEmpty()) {
            Function func = q.poll();
            for (GlobalVariable gv : m.getGlobalVariables()) {
                gv.getUseList().removeIf(
                        use -> ((Instruction)use.user).getParent().getParent().equals(func)
                );
            }

            func.getINode().removeSelf();
            for (Function callee : func.getCallees()) {
                callee.getCallers().remove(func);
                if (callee.getCallers().isEmpty()) {
                    q.offer(callee);
                }
            }
        }
    }

    public void dfsFunc(Function func) {
        if (func.isDirty()) return;
        func.setDirty(true);
        for (Function callee : func.getCallees()) {
            dfsFunc(callee);
            func2gv.get(func).addAll(func2gv.get(callee));
            if (callee == func) {
                recursiveFuncs.add(func);
            }
        }
    }
}
