package frontend.syntax.func.ast;

import frontend.symbol.FuncSymbol;
import frontend.symbol.SymbolTable;
import frontend.syntax.NodeBase;
import frontend.tokenize.Token;

import java.util.ArrayList;

/*
<FuncFParams>   := <FuncFParam> { ',' <FuncFParam> }
------------------> FuncFParamsNode
-----------------{| FuncFParamNode
 */
public class FuncFParamsNode extends NodeBase {
    private ArrayList<NodeBase> funcFParams;

    public FuncFParamsNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(ArrayList<NodeBase> funcFParams) {
        this.funcFParams = funcFParams;
    }

    public void checkErrors(FuncSymbol symbol, SymbolTable symbolTable) {
        updateLastSymbolTable();
        for (NodeBase nodeBase : funcFParams) {
            FuncFParamNode funcFParamNode = (FuncFParamNode) nodeBase;
            funcFParamNode.checkErrors(symbol, symbolTable);
        }
    }
}
