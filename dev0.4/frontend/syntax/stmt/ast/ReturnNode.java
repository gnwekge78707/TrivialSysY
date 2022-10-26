package frontend.syntax.stmt.ast;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.expr.ast.Calculatable;
import frontend.tokenize.Token;

public class ReturnNode extends NodeBase implements Stmt {
    private NodeBase exp;

    public ReturnNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(NodeBase exp) {
        this.exp = exp;
    }

    @Override
    public void checkErrors() {
        updateLastSymbolTable();
        if (exp == null) {
            return;
        }
        ((Calculatable) exp).checkErrors();
        if (getLastSymbolTable().getCurrentFunction() != null &&
                getLastSymbolTable().getCurrentFunction().getDeclType() == Token.TokenType.VOIDTK) {
            handleError(Error.ErrorType.ILLEGAL_RETURN);
        }
    }
}
