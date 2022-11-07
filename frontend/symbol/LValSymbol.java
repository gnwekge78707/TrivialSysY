package frontend.symbol;

import frontend.syntax.expr.ast.BinaryExpNode;
import frontend.tokenize.Token;
import midend.ir.ModuleBuilder;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.Constant;
import midend.ir.value.instr.Instruction;
import midend.ir.value.instr.binary.BinaryInstr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class LValSymbol implements Symbol {
    private boolean isConst;
    private Token ident;
    private Token.TokenType declType;
    private ArrayList<Integer> dimensions = new ArrayList<>(); //todo : in function, first dim is 0
    private HashMap<ArrayList<Integer>, BinaryExpNode> inits = new HashMap<>();

    public LValSymbol(boolean isConst, Token ident, Token.TokenType declType,
                      ArrayList<Integer> dimensions, HashMap<ArrayList<Integer>, BinaryExpNode> inits) {
        this.isConst = isConst;
        this.ident = ident;
        this.dimensions = dimensions;
        this.inits = inits;
        this.declType = declType;
    }

    public LValSymbol(boolean isConst, Token.TokenType declType) {
        this.isConst = isConst;
        this.declType = declType;
    }

    public void setIdent(Token ident) {
        this.ident = ident;
    }

    public void addDimension(int dim) {
        dimensions.add(dim);
    }

    public void setDimension(int idx, int dim) { // called in funcRParam -> set function
        dimensions.set(idx, dim);
    }

    public ArrayList<Integer> getDimensions() {
        return dimensions;
    }

    public void addInit(ArrayList<Integer> dims, BinaryExpNode init) {
        this.inits.put(dims, init);
    }

    public boolean hasInits() {
        return inits.size() != 0;
    }

    public BinaryExpNode getInit(ArrayList<Integer> dims) {
        return inits.getOrDefault(dims, null);
    }

    public boolean isConst() {
        return isConst;
    }

    public String getName() {
        return ident.toString();
    }

    //=================================================LLVM below====================================================//

    public boolean isArray() {
        return getDimensions().size() > 0;
    }

    public boolean isInt() {
        return declType.equals(Token.TokenType.INTTK);
    }

    private Value pointer;
    private boolean isFuncFParamArray;

    public void setIsFuncFParamArray(boolean isFuncFParamArray) { this.isFuncFParamArray = isFuncFParamArray; }

    public boolean isFuncFParamArr() { return isFuncFParamArray; }

    public void setPointer(Value pointer) {
        if (!(pointer.getType() instanceof LLVMType.Pointer)) {
            throw new Error("set LValSymbol pointer err: not a pointer");
        }
        this.pointer = pointer;
    }

    public Value getPointer() {
        return pointer;
    }

    //TODO! 对于函数参数数组，作为纯指针 i32* 处理？其他按照全局局部数组按照一维数组处理
    // / 不过对于形参，计算维数的方法应该能和普通数组相同，GEP时候不一样罢了
    // / 在调用常量数组时，type 会表示为数组的情况
    public LLVMType getLLVMType() {
        LLVMType baseType = isInt() ? LLVMType.Int.getI32() : null;
        if (baseType == null) {
            throw new Error("unsupported llvm type");
        }
        if (isArray()) {
            if (isFuncFParamArray) { // funcFPram, dim[0] = 0
                assert dimensions.get(0) == 0; //
                return new LLVMType.Pointer(baseType);
            }
            return new LLVMType.Array(baseType, this.getArrSize());
        } else {
            return baseType;
        }
    }

    //FIXME: probably time consuming
    public int dimensions2indexAtBase(ArrayList<Integer> dims) {
        if (dims.size() == 0 || dims.size() > this.dimensions.size()) {
            return 0;
        }
        int res = 0;
        int[] base = new int[dimensions.size()];
        // int a[4][5][6]; base[2]=1 base[1]=6 base[0]=5*6
        // idx for a[2][3][1] -> 30*2 + 6*3 + 1
        for (int i = dimensions.size() - 1; i >= 0; i--) {
            base[i] = (i == dimensions.size() - 1) ? 1 : base[i + 1] * dimensions.get(i + 1);
        }
        for (int i = 0; i < dims.size(); i++) {
            if (dims.get(i) >= this.dimensions.get(i) && this.getDimensions().get(i) > 0) {
                throw new Error("dim for array exceeded limits when indexing");
            }
            res = res + base[i] * dims.get(i);
        }
        return res;
    }

    public Value dimensions2indexAtBase(ArrayList<Value> dims, ModuleBuilder builder) {
        if (dims.stream().allMatch(u -> (u instanceof Constant))) {
            ArrayList<Integer> intDims = new ArrayList<>();
            dims.forEach(u -> intDims.add(((Constant) u).getConstVal()));
            return new Constant(LLVMType.Int.getI32(), dimensions2indexAtBase(intDims));
        }
        int[] base = new int[dimensions.size()];
        for (int i = dimensions.size() - 1; i >= 0; i--) {
            base[i] = (i == dimensions.size() - 1) ? 1 : base[i + 1] * dimensions.get(i + 1);
        }
        Value res = null;
        for (int i = 0; i < dims.size(); i++) {
            Value tempMul = new BinaryInstr(Instruction.InstrType.MUL, dims.get(i),
                    new Constant(LLVMType.Int.getI32(), base[i]), builder.getCurBasicBlock());
            if (res == null) {
                res = tempMul;
            } else {
                res = new BinaryInstr(Instruction.InstrType.ADD, res, tempMul, builder.getCurBasicBlock());
            }
        }
        return res == null ? new Constant(LLVMType.Int.getI32(), 0) : res;
    }

    public int getArrSize() {
        return dimensions.stream().mapToInt(Integer::intValue).reduce(1, (x, y) -> x * y);
    }

    public ArrayList<Value> init2OneDimArray(boolean isGlobal) {
        int size = dimensions.stream().mapToInt(Integer::intValue).reduce(1, (x, y) -> x * y);
        ArrayList<Value> res = new ArrayList<>(Collections.nCopies(size, null));
        for (ArrayList<Integer> dim : inits.keySet()) {
            BinaryExpNode initVal = inits.get(dim);
            int idx = dimensions2indexAtBase(dim);
            if (isGlobal) {
                if (!initVal.getExpContext().hasValue()) {
                    throw new Error("global array init error: initVal is not valid constExp");
                }
                res.set(idx, new Constant(LLVMType.Int.getI32(), initVal.getExpContext().getValue()));
            } else {
                res.set(idx, initVal.getDst());
            }
        }
        return res;
    }
}
