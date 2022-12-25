package midend.pass;

public class ValueCopy {
    /*
    private final HashMap<Value, Value> initMap;

    public ValueCopy(HashMap<Value, Value> initMap) {
        this.initMap = initMap;
    }

    private final HashMap<BasicBlock, BasicBlock> basicBlockMap = new HashMap<>();
    private final HashMap<Function.Param, Function.Param> paramMap = new HashMap<>();
    private final HashMap<Instruction, Value> instrMap = new HashMap<>();

    public Value findValue(Value value) {
        if (initMap.containsKey(value)) {
            return initMap.get(value);
        } else if (value instanceof GlobalVariable) {
            return value;
        } else if (value instanceof Function) {
            return value;
        } else if (value instanceof Constant) {
            return value;
        } else if (value instanceof BasicBlock && basicBlockMap.containsKey(value)) {
            return basicBlockMap.get(value);
        } else if (value instanceof Function.Param && paramMap.containsKey(value)) {
            return paramMap.get(value);
        } else if (value instanceof Instruction && instrMap.containsKey(value)) {
            return instrMap.get(value);
        } else {
            return value;
        }
    }

    public void putValue(Value source, Value copy) {
        if (source instanceof BasicBlock && !basicBlockMap.containsKey(source)) {
            basicBlockMap.put((BasicBlock) source, (BasicBlock) copy);
        } else if (source instanceof Function.Param && !paramMap.containsKey(source)) {
            paramMap.put((Function.Param) source, (Function.Param) copy);
        } else if (source instanceof Instruction && !instrMap.containsKey(source)) {
            instrMap.put((Instruction) source, copy);
        }
    }

    public Value getInstrCopy(Instruction instrSource, BasicBlock bbCopy, ArrayList<MemInst.Phi> phis) {
        Instruction instrCopy;
        if (instrSource instanceof BinaryInst) {
            Value operand0 = findValue(instrSource.getOperand(0));
            Value operand1 = findValue(instrSource.getOperand(1));
            instrCopy = new BinaryInst(instrSource.tag, operand0, operand1, bbCopy);
        } else if (instrSource.tag == InstrTag.Alloca) {
            instrCopy = new MemInst.Alloca(((MemInst.Alloca) instrSource).getAllocated(), bbCopy);
        } else if (instrSource.tag == InstrTag.Br) {
            Value operand0 = findValue(instrSource.getOperand(0));
            if (instrSource.getOperandNum() == 1) {
                instrCopy = new TerminatorInst.Br((BasicBlock) operand0, bbCopy);
            } else {
                Value operand1 = findValue(instrSource.getOperand(1));
                Value operand2 = findValue(instrSource.getOperand(2));
                instrCopy = new TerminatorInst.Br(operand0, (BasicBlock) operand1, (BasicBlock) operand2, bbCopy);
            }
        } else if (instrSource.tag == InstrTag.Call) {
            ArrayList<Value> paramsCopy = new ArrayList<>();
            TerminatorInst.Call callSource = (TerminatorInst.Call) instrSource;
            Value operand0 = findValue(instrSource.getOperand(0));
            for (int i = 1; i < callSource.getOperandNum(); ++i) {
                paramsCopy.add(findValue(callSource.getOperand(i)));
            }
            instrCopy = new TerminatorInst.Call((Function) operand0, paramsCopy, bbCopy);
        } else if (instrSource.tag == InstrTag.Ret) {
            Value operand0Source = instrSource.getOperand(0);
            Value operand0 = operand0Source == null ? null : findValue(operand0Source);
            instrCopy = new TerminatorInst.Ret(operand0, bbCopy);
        } else if (instrSource.tag == InstrTag.Load) {
            Value operand0 = findValue(instrSource.getOperand(0));
            instrCopy = new MemInst.Load(instrSource.getType(), operand0, bbCopy);
        } else if (instrSource.tag == InstrTag.Store) {
            Value operand0 = findValue(instrSource.getOperand(0));
            Value operand1 = findValue(instrSource.getOperand(1));
            instrCopy = new MemInst.Store(operand0, operand1, bbCopy);
        } else if (instrSource.tag == InstrTag.GetElementPtr) {
            Value operand0 = findValue(instrSource.getOperand(0));
            ArrayList<Value> indicesCopy = new ArrayList<>();
            for (int i = 1; i < instrSource.getOperandNum(); ++i) {
                indicesCopy.add(findValue(instrSource.getOperand(i)));
            }
            instrCopy = new MemInst.GEP(operand0, indicesCopy, bbCopy);
        } else if (instrSource.tag == InstrTag.Zext) {
            Value operand0 = findValue(instrSource.getOperand(0));
            instrCopy = new MemInst.Zext(operand0, ((MemInst.Zext) instrSource).getDstType(), bbCopy);
        } else if (instrSource.tag == InstrTag.Fptosi) {
            Value operand0 = findValue(instrSource.getOperand(0));
            instrCopy = new MemInst.FTS(operand0, ((MemInst.FTS) instrSource).getDstType(), bbCopy);
        } else if (instrSource.tag == InstrTag.Sitofp) {
            Value operand0 = findValue(instrSource.getOperand(0));
            instrCopy = new MemInst.STF(operand0, bbCopy);
        } else if (instrSource.tag == InstrTag.Phi) {
            if (initMap.containsKey(instrSource)) {
                return initMap.get(instrSource);
            }
            phis.add((MemInst.Phi) instrSource);
            instrCopy = new MemInst.Phi(instrSource.getType(), bbCopy);
        } else {
            throw new RuntimeException("error in getInstrCopy");
        }
        return instrCopy;
    }*/
}
