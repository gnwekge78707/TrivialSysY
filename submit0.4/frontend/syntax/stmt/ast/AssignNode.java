package frontend.syntax.stmt.ast;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.expr.ast.Calculatable;
import frontend.syntax.expr.ast.LValNode;

public class AssignNode extends NodeBase implements Stmt{
    private NodeBase lVal;
    private NodeBase exp;

    public AssignNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(NodeBase lVal, NodeBase exp) {
        this.exp = exp;
        this.lVal = lVal;
    }

    @Override
    public void checkErrors() {
        updateLastSymbolTable();
        ((LValNode) lVal).checkErrors();
        ((Calculatable) exp).checkErrors();
        if (((LValNode) lVal).getExpContext().isConst() && exp != null) {
            handleError(Error.ErrorType.MODIFIED_CONST);
        }
    }
}
