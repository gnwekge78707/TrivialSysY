package frontend.syntax.expr.ast;

import driver.Config;
import frontend.error.Error;
import frontend.symbol.LValSymbol;
import frontend.syntax.NodeBase;
import frontend.tokenize.Token;
import midend.ir.ModuleBuilder;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.Constant;
import midend.ir.value.instr.mem.GEPInstr;
import midend.ir.value.instr.mem.LoadInstr;
import midend.ir.value.instr.mem.StoreInstr;

import java.util.ArrayList;

// <LVal>          := Ident { '[' <Exp> ']' }
public class LValNode extends NodeBase implements Calculatable {
    private Token ident;
    private ArrayList<NodeBase> exps;

    public LValNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(Token ident, ArrayList<NodeBase> exps) {
        this.ident = ident;
        this.exps = exps;
    }

    private ExpContext expContext;

    public ExpContext getExpContext() {
        return expContext;
    }

    public void checkErrors() {
        updateLastSymbolTable();
        //todo :: can only calculate value for const, incase of control flow

        LValSymbol symbol = getLastSymbolTable().getLValSymbol(ident.toString(), false);
        if (symbol == null) {
            handleError(Error.ErrorType.UNDEFINED_IDENT);
            this.expContext = new ExpContext(0, false, false, new ArrayList<>());
        }
        for (NodeBase nodeBase : exps) {
            Calculatable exp = (Calculatable) nodeBase;
            exp.checkErrors();
        }
        if (symbol != null) calculate(symbol);
        dstSymbol = symbol;
    }

    // TODO -> CAUTION !! better made it !hasValue than hasValue
    // TODO slicing:: DECL: a[2][2] -> REF: a[0][1], a[0]
    // TODO dimension dont hasValue:: still needs to proceed
    // FIXME! 作为函数里面，引用形参数组，形参数组第一维没有存？
    public void calculate(LValSymbol symbol) {
        if (exps.size() == symbol.getDimensions().size()) {
            ArrayList<Integer> dims = new ArrayList<>();
            for (NodeBase nodeBase : exps) {
                Calculatable exp = (Calculatable) nodeBase;
                if (!exp.getExpContext().hasValue()) {
                    this.expContext = new ExpContext(0, symbol.isConst(), false, new ArrayList<>());
                    return;
                }
                if (exp.getExpContext().getValue() >= symbol.getDimensions().get(dims.size())) {
                    handleError(Error.ErrorType.UNDEFINED_ERROR);
                }
                dims.add((exp.getExpContext().getValue()));
            }
            if (!symbol.isConst()) {
                this.expContext = new ExpContext(0, false, false, new ArrayList<>());
                return;
            }
            BinaryExpNode initVal = symbol.getInit(dims);
            if (initVal == null) {
                this.expContext = new ExpContext(0, true, false, new ArrayList<>());
            } else {
                this.expContext = new ExpContext(initVal.getExpContext().getValue(),
                        initVal.getExpContext().isConst(),
                        initVal.getExpContext().hasValue(),
                        new ArrayList<>());
            }
        }
        else if (exps.size() < symbol.getDimensions().size()) {
            ArrayList<Integer> newDims = new ArrayList<>(
                    symbol.getDimensions().subList(exps.size(), symbol.getDimensions().size())
            );
            this.expContext = new ExpContext(0, symbol.isConst(), false, newDims);
        }
        else { //TODO -> ERROR!
            handleError(Error.ErrorType.UNDEFINED_ERROR);
            this.expContext = new ExpContext(0, symbol.isConst(), false, new ArrayList<>());
        }
    }


    //FIXME! lVal can be both on lsh and rsh
    // / if father is AssignNode, lVal_dst should be pointer
    // / but what if AssignNode -> LVal = LVal;
    // / dst是值还是指针 ? 表达式，数组取到值，是值；在实参中去部分维度，是指针；lsh是指针
    // / 数组还要考虑是函数形参的情况，i32**
    // / LVal as RParam -> foo(a[b+c*2][3]); | a[foo(b,2)][2] = 1; need to set corresponding pointer as dst
    private Value dst; // Constant | Instr | NormalVar
    private LValSymbol dstSymbol;

    public Value getDst() {
        return dst;
    }

    @Override
    public void buildIR(ModuleBuilder builder) { // called in exp
        if (Config.getInstance().hasOptimize(Config.Optimize.syntaxTreeExpressionOptimize) && expContext.hasValue()) {
            LLVMType type = dstSymbol.getLLVMType();
            if (type instanceof LLVMType.Array) {
                type = ((LLVMType.Array) type).getType();
            }
            dst = new Constant(type, expContext.getValue());
            return;
        }
        if (!dstSymbol.isArray()) {
            //fixme: be conservative, just load store
            // / in case of very long expr: t = t1+t2+...+t33+t1+t2+... (reg exceeded)
            Value pointer = dstSymbol.getPointer();
            dst = new LoadInstr(dstSymbol.getLLVMType(), pointer, builder.getCurBasicBlock());
        } else {
            //TODO! array reference
            // / if exps.size < symbol.getDims.size, dst is pointer instead of int (this is func real params - arr type)
            // / 上面就是一个 i32*
            ArrayList<Value> dimValues = new ArrayList<>();
            for (NodeBase nodeBase : exps) {
                nodeBase.buildIR(builder);
                dimValues.add(((Calculatable) nodeBase).getDst());
            }
            Value offset = dstSymbol.dimensions2indexAtBase(dimValues, builder);
            Value pointer = new GEPInstr(dstSymbol.getPointer(), offset, builder.getCurBasicBlock());
            if (exps.size() < dstSymbol.getDimensions().size()) {
                dst = pointer;
            } else {
                dst = new LoadInstr(((LLVMType.Pointer) pointer.getType()).getPointedTo(), pointer, builder.getCurBasicBlock());
            }
        }
    }

    public void updateValue(Value src, ModuleBuilder builder) {
        if (exps.size() == 0) {
            if (dstSymbol.isArray()) {
                throw new java.lang.Error("cannot store value to array without index");
            }
            Value pointer = dstSymbol.getPointer();
            new StoreInstr(pointer, src, builder.getCurBasicBlock());
        } else {
            //TODO! update array here
            if (dstSymbol.getDimensions().size() != exps.size()) {
                throw new java.lang.Error("cannot store value to array without exceeded index");
            }
            ArrayList<Value> dimValues = new ArrayList<>();
            for (NodeBase nodeBase : exps) {
                nodeBase.buildIR(builder);
                dimValues.add(((Calculatable) nodeBase).getDst());
            }
            Value offset = dstSymbol.dimensions2indexAtBase(dimValues, builder);
            Value pointer = new GEPInstr(dstSymbol.getPointer(), offset, builder.getCurBasicBlock());
            new StoreInstr(pointer, src, builder.getCurBasicBlock());
        }
    }
}
