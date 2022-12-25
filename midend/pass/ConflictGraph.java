package midend.pass;

import backend.MipsAssembly;
import driver.Config;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.Function;
import midend.ir.value.NormalVariable;
import midend.ir.value.instr.Instruction;

import java.util.*;

public class ConflictGraph {
    private final HashSet<Value> nodeSet;
    private final ArrayList<Value> memoryList;
    private final ArrayList<Value> registerList;
    private final HashMap<Value, HashSet<Value>> graph;

    public ConflictGraph() {
        graph = new HashMap<>();
        nodeSet = new HashSet<>();
        memoryList = new ArrayList<>();
        registerList = new ArrayList<>();
    }

    private void insertNode(Value abstractLeftValue) {
        nodeSet.add(abstractLeftValue);
        graph.put(abstractLeftValue, new HashSet<>());
    }

    private boolean isValidLeftValue(Value leftValue) {
        if (Config.getInstance().hasOptimize(Config.Optimize.ssaGlobalRegAlloc)) {
            return leftValue instanceof NormalVariable ||
                    leftValue instanceof Function.Param ||
                    (leftValue instanceof Instruction
                            && ((Instruction) leftValue).getDstType() != LLVMType.Label.getLabel()
                            && ((Instruction) leftValue).getDstType() != LLVMType.Void.getVoid());
        }
        else {
            return leftValue instanceof NormalVariable;
        }
    }

    public void insertNodes(HashSet<Value> nodes) {
        nodes.stream().filter(i -> !graph.containsKey(i)
                && isValidLeftValue(i)).forEach(this::insertNode);
    }

    public void link(Value src, Value dst) {
        if (isValidLeftValue(src) && isValidLeftValue(dst) && !src.equals(dst)) {
            if (!graph.containsKey(src)) {
                insertNode(src);
            }
            if (!graph.containsKey(dst)) {
                insertNode(dst);
            }
            graph.get(src).add(dst);
            graph.get(dst).add(src);
        }
    }

    private void fillList(HashMap<Value, Integer> outCount) {
        graph.forEach((k, v) -> outCount.put(k, v.size()));
        //outCount.forEach(Value::updateConflicts);
        while (registerList.size() + memoryList.size() < graph.size()) {
            Map.Entry<Value, Integer> entry = outCount.entrySet()
                    .stream().filter(i -> nodeSet.contains(i.getKey()))
                    .min(Map.Entry.comparingByValue()).orElse(null);
            if (entry == null) {
                System.err.println("unexpected null entry appeared when coloring node");
                continue;
            }
            if (entry.getValue() < MipsAssembly.globalRegisterPool.length) {
                graph.get(entry.getKey()).forEach(i -> outCount.compute(i, (k, v) -> v -= 1));
                registerList.add(entry.getKey());
                nodeSet.remove(entry.getKey());
                continue;
            }
            Value toRemove = nodeSet.iterator().next();
            graph.get(toRemove).forEach(i -> outCount.compute(i, (k, v) -> v -= 1));
            memoryList.add(toRemove);
            nodeSet.remove(toRemove);
        }
    }

    public void colorNode(Function function) {
        fillList(new HashMap<>());
        HashMap<Value, HashSet<Integer>> validColor = new HashMap<>();
        registerList.forEach(i -> {
            HashSet<Integer> colors = new HashSet<>();
            for (int j : MipsAssembly.globalRegisterPool) {
                colors.add(j);
            }
            validColor.put(i, colors);
        });
        //registerList.stream().sorted((Comparator.comparing(i -> graph.get(i).size()).reversed())).forEach(i -> {
        registerList.stream().forEach(i -> {
            int color = validColor.get(i).iterator().next();
            i.getMipsMemContex().setRegister(color);
            System.out.println(function.getName() + "| GB_reg: " + i.getName() + " -> $" + color);
            graph.get(i).stream().filter(validColor::containsKey).forEach(j ->
                    validColor.get(j).remove(color));
        });
    }

    public void colorNode() {
        ArrayList<Integer> colors = new ArrayList<>();
        for (int j : MipsAssembly.globalRegisterPool) {
            colors.add(j);
        }
        int cnt = 0;
        for (Value value : nodeSet) {
            if (cnt < colors.size()) {
                value.getMipsMemContex().setRegister(colors.get(cnt));
                cnt++;
            } else {
                break;
            }
        }
    }
}
