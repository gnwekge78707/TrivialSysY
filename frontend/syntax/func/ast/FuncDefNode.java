package frontend.syntax.func.ast;

import frontend.error.Error;
import frontend.symbol.FuncSymbol;
import frontend.symbol.SymbolTable;
import frontend.syntax.NodeBase;
import frontend.syntax.TokenNode;
import frontend.syntax.stmt.ast.BlockNode;
import frontend.tokenize.Token;
import midend.ir.ModuleBuilder;
import midend.ir.type.LLVMType;
import midend.ir.value.Constant;
import midend.ir.value.Function;
import midend.ir.value.instr.terminator.RetInstr;

/*
<FuncDef>       := <FuncType> Ident '(' [<FuncFParams> ] ')' <Block>
------------------> FuncDefNode
-----------------{| FuncType (TokenNode), Token, [], BlockNode
 */
public class FuncDefNode extends NodeBase {
    private NodeBase funcType;
    private Token ident;
    private NodeBase funcFParams;
    private NodeBase block;

    public FuncDefNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(NodeBase funcType, Token ident, NodeBase funcFParams, NodeBase block) {
        this.funcType = funcType;
        this.ident = ident;
        this.funcFParams = funcFParams;
        this.block = block;
    }

    public void checkErrors() {
        updateLastSymbolTable();
        FuncSymbol symbol = new FuncSymbol(ident, ((TokenNode) funcType).getTokenType());
        SymbolTable nextSymbolTable = new SymbolTable(getLastSymbolTable(), symbol);
        if (funcFParams != null) {
            ((FuncFParamsNode) funcFParams).checkErrors(symbol, nextSymbolTable);
        }
        if (getLastSymbolTable().getSymbol(symbol.getName(), true) == null) {
            getLastSymbolTable().addSymbol(symbol);
        } else {
            handleError(Error.ErrorType.DUPLICATED_IDENT);
        }
        this.curSymbol = symbol;
        ((BlockNode) block).checkErrors(nextSymbolTable);
    }

    private FuncSymbol curSymbol;

    @Override
    public void buildIR(ModuleBuilder builder) {
        Function llvmFunction = builder.putFunction(ident.toString(), curSymbol.getLLVMType(), false);
        curSymbol.setLLVMFunction(llvmFunction);
        builder.putBasicBlockAsCur("func_" + ident.toString());
        block.buildIR(builder);
        //TODO! add ret stmt
        if (builder.getCurBasicBlock().getInstrList().getLast().getValue() == null ||
                !builder.getCurBasicBlock().getInstrList().getLast().getValue().getInstrType().isTerminator()) {
            if (llvmFunction.getRetType() == LLVMType.Void.getVoid()) {
                new RetInstr(null, builder.getCurBasicBlock());
            } else {
                new RetInstr(Constant.getConst0(), builder.getCurBasicBlock());
            }
        }
    }
}
