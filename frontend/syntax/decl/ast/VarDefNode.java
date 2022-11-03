package frontend.syntax.decl.ast;

import frontend.error.Error;
import frontend.symbol.FuncSymbol;
import frontend.symbol.LValSymbol;
import frontend.syntax.NodeBase;
import frontend.syntax.RootNode;
import frontend.syntax.expr.ast.BinaryExpNode;
import frontend.syntax.expr.ast.ExpContext;
import frontend.tokenize.Token;
import midend.ir.ModuleBuilder;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.Constant;
import midend.ir.value.GlobalVariable;
import midend.ir.value.instr.mem.AllocaInstr;
import midend.ir.value.instr.mem.GEPInstr;
import midend.ir.value.instr.mem.StoreInstr;

import java.util.ArrayList;

public class VarDefNode extends NodeBase {
    private Token ident;
    private ArrayList<NodeBase> constExps;
    private NodeBase initVal;

    public VarDefNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(Token ident, ArrayList<NodeBase> constExps, NodeBase initVal) {
        this.ident = ident;
        this.constExps = constExps;
        this.initVal = initVal;
    }

    private LValSymbol curSymbol;

    public void checkErrors(LValSymbol symbol) {
        updateLastSymbolTable();
        symbol.setIdent(ident);
        for (NodeBase nodeBase : constExps) {
            BinaryExpNode binaryExpNode = (BinaryExpNode) nodeBase;
            binaryExpNode.checkErrors();
            if (!binaryExpNode.getExpContext().isConst()) {
                handleError(Error.ErrorType.UNDEFINED_ERROR);
            }
            symbol.addDimension(binaryExpNode.getExpContext().getValue());
        }
        if (initVal != null) {
            if (initVal instanceof InitValNode) {
                ((InitValNode) initVal).checkErrors(symbol, new ArrayList<>());
            } else if (initVal instanceof BinaryExpNode) {
                ((BinaryExpNode) initVal).checkErrors();
                symbol.addInit(new ArrayList<>(), ((BinaryExpNode) initVal));
            }
        }
        if (getLastSymbolTable().getSymbol(ident.toString(), true) == null) {
            getLastSymbolTable().addSymbol(symbol);
            curSymbol = symbol;
        } else {
            handleError(Error.ErrorType.DUPLICATED_IDENT);
        }
    }

    //FIXME!! need to call build on initVal on non-const condition
    // /i.e. normal var decl,
    @Override
    public void buildIR(ModuleBuilder builder) {
        if (curSymbol == null) {
            throw new java.lang.Error("ident is duplicated when building ir");
        }
        if (builder.getCurFunction() == null) {
            GlobalVariable dst;
            if (curSymbol.isArray()) {
                assert curSymbol.isInt();
                LLVMType llvmType = new LLVMType.Array(LLVMType.Int.getI32(), curSymbol.getArrSize());
                ArrayList<Value> init = curSymbol.hasInits() ?
                        curSymbol.init2OneDimArray(true) : null;
                dst = new GlobalVariable(curSymbol.getName(), llvmType, curSymbol.getArrSize(), init);
                builder.putGlobalVar(dst);
            } else {
                assert curSymbol.isInt();
                LLVMType llvmType = LLVMType.Int.getI32();
                Value init = null;
                if (curSymbol.hasInits()) {
                    ExpContext initExpCon = curSymbol.getInit(new ArrayList<>()).getExpContext();
                    if (!initExpCon.hasValue()) {
                        throw new java.lang.Error("globalVar init is not valid constExp");
                    }
                    init = new Constant(llvmType, initExpCon.getValue());
                }
                dst = new GlobalVariable(curSymbol.getName(), llvmType, init);
                builder.putGlobalVar(dst);
            }
            curSymbol.setPointer(dst);
        } else {
            //fixme: for local var, init can be a reference
            // /维度是否确定
            if (initVal != null) {
                initVal.buildIR(builder);
            }
            Value dst;
            if (curSymbol.isArray()) {
                assert curSymbol.isInt();
                LLVMType llvmType = new LLVMType.Array(LLVMType.Int.getI32(), curSymbol.getArrSize());
                Value basePointer = new AllocaInstr(llvmType, builder.getCurBasicBlock());
                dst = basePointer;
                if (curSymbol.hasInits()) {
                    int arrSize = curSymbol.getArrSize();
                    ArrayList<Value> arrInits = curSymbol.init2OneDimArray(false);
                    for (int i = 0; i < arrSize; i++) {
                        Value pointer = new GEPInstr(
                                basePointer, new Constant(LLVMType.Int.getI32(), i), builder.getCurBasicBlock()
                        );
                        new StoreInstr(pointer, arrInits.get(i), builder.getCurBasicBlock());
                    }
                }
            } else {
                assert curSymbol.isInt();
                LLVMType llvmType = LLVMType.Int.getI32();
                dst = new AllocaInstr(llvmType, builder.getCurBasicBlock());
                if (curSymbol.hasInits()) {
                    Value init = curSymbol.getInit(new ArrayList<>()).getDst();
                    new StoreInstr(dst, init, builder.getCurBasicBlock());
                }
            }
            curSymbol.setPointer(dst);
        }
    }
}
