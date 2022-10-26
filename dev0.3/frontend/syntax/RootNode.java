package frontend.syntax;

/*
<CompUnit>      := {<ConstDecl> | <VarDecl>} {<FuncDef>} <MainFuncDef>
------------------> RootNode
-----------------{| {VarDefNode} {FuncDefNode} BlockNode
 */

import frontend.symbol.FuncSymbol;
import frontend.symbol.SymbolTable;
import frontend.syntax.decl.ast.VarDeclNode;
import frontend.syntax.func.ast.FuncDefNode;
import frontend.syntax.stmt.ast.BlockNode;
import frontend.tokenize.Token;

import java.util.ArrayList;

public class RootNode extends NodeBase implements Scope {
    private ArrayList<NodeBase> varDecls;
    private ArrayList<NodeBase> funcDefs;
    private NodeBase mainFuncDef;

    public RootNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(ArrayList<NodeBase> varDecls, ArrayList<NodeBase> funcDefs, NodeBase mainFuncDef) {
        this.varDecls = varDecls;
        this.funcDefs = funcDefs;
        this.mainFuncDef = mainFuncDef;
    }

    private SymbolTable symbolTable;

    @Override
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void checkErrors() {
        symbolTable = new SymbolTable(null);
        for (NodeBase nodeBase : varDecls) {
            ((VarDeclNode) nodeBase).checkErrors();
        }
        for (NodeBase nodeBase : funcDefs) {
            ((FuncDefNode) nodeBase).checkErrors();
        }
        ((BlockNode) mainFuncDef).checkErrors(
                new SymbolTable(symbolTable, new FuncSymbol(null, Token.TokenType.INTTK)));
    }
}
