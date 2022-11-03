package frontend.syntax.func.ast;

import frontend.error.Error;
import frontend.symbol.FuncSymbol;
import frontend.symbol.LValSymbol;
import frontend.symbol.SymbolTable;
import frontend.syntax.NodeBase;
import frontend.syntax.TokenNode;
import frontend.syntax.expr.ast.BinaryExpNode;
import frontend.tokenize.Token;
import midend.ir.ModuleBuilder;

import java.util.ArrayList;

/*
<FuncFParam>    := <BType> Ident [ '[' ']' { '[' <ConstExp> ']' } ]
------------------> FuncFParamNode
-----------------{| TokenNode, Token, {AddExpNode}
 */

public class FuncFParamNode extends NodeBase {
    private NodeBase btype;
    private Token ident;
    private boolean hasFirstDim;
    private ArrayList<NodeBase> constExps;

    public FuncFParamNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(NodeBase btype, Token ident, boolean hasFirstDim, ArrayList<NodeBase> constExps) {
        this.btype = btype;
        this.ident = ident;
        this.constExps = constExps;
        this.hasFirstDim = hasFirstDim;
    }

    public void checkErrors(FuncSymbol symbol, SymbolTable symbolTable) {
        updateLastSymbolTable();
        LValSymbol lValSymbol = new LValSymbol(false, ((TokenNode) btype).getTokenType());
        lValSymbol.setIdent(ident);
        if (hasFirstDim) {
            lValSymbol.addDimension(0);
        }
        for (NodeBase nodeBase : constExps) {
            BinaryExpNode binaryExpNode = (BinaryExpNode) nodeBase;
            binaryExpNode.checkErrors();
            if (!binaryExpNode.getExpContext().isConst()) {
                handleError(Error.ErrorType.UNDEFINED_ERROR);
            }
            lValSymbol.addDimension(binaryExpNode.getExpContext().getValue());
        }
        //todo: 函数形参，设置 LValSymbol 的 dst，放在函数基本块一开始的地方
        if (hasFirstDim) {
            lValSymbol.setIsFuncFParamArray(true);
        }

        if (symbolTable.getSymbol(lValSymbol.getName(), true) == null) {
            symbolTable.addSymbol(lValSymbol);
        } else {
            handleError(Error.ErrorType.DUPLICATED_IDENT);
        }
        symbol.addParams(lValSymbol);
        //System.out.println(lValSymbol.getDimensions());
    }

    @Override
    public void buildIR(ModuleBuilder builder) {
        //do nothing
    }
}
