package frontend.syntax.stmt.ast;

import frontend.error.Error;
import frontend.symbol.SymbolTable;
import frontend.syntax.NodeBase;
import frontend.syntax.RootNode;
import frontend.syntax.Scope;
import frontend.syntax.decl.ast.VarDeclNode;
import frontend.syntax.expr.ast.Calculatable;
import frontend.syntax.func.ast.FuncDefNode;
import frontend.tokenize.Token;

import java.util.ArrayList;

public class BlockNode extends NodeBase implements Scope, Stmt {
    private ArrayList<NodeBase> blockItems;

    public BlockNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(ArrayList<NodeBase> blockItems) {
        this.blockItems = blockItems;
    }

    private SymbolTable symbolTable;

    @Override
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    //TODO: in block, **dont** use getLastSymbolTable to query symbol in this scope
    public void checkErrors(SymbolTable symbolTable) {
        updateLastSymbolTable();
        this.symbolTable = symbolTable;
        for (NodeBase nodeBase : blockItems) {
            if (nodeBase instanceof Stmt) {
                ((Stmt) nodeBase).checkErrors();
            } else if (nodeBase instanceof VarDeclNode) {
                ((VarDeclNode) nodeBase).checkErrors();
            } else if (nodeBase instanceof Calculatable) {
                ((Calculatable) nodeBase).checkErrors();
            } else if (nodeBase != null){
                // in case of a single ';' per blockItem
                throw new java.lang.Error("in checkError : Block -> has other type of children: "+(nodeBase == null));
            }
        }
        if (this.getParent() instanceof FuncDefNode || this.getParent() instanceof RootNode) {
            if (this.symbolTable.getCurrentFunction().getDeclType() != Token.TokenType.VOIDTK) {
                if (this.blockItems.size() == 0 || !(this.blockItems.get(blockItems.size() - 1) instanceof ReturnNode)) {
                    handleError(Error.ErrorType.MISSING_RETURN);
                }
            }
        }
    }

    @Override
    public void checkErrors() {
        //TODO: this is call when block is defined inside function
        updateLastSymbolTable();
        checkErrors(new SymbolTable(getLastSymbolTable(), getLastSymbolTable().getCurrentFunction()));
    }
}
