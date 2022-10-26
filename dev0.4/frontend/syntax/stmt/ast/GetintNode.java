package frontend.syntax.stmt.ast;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.expr.ast.LValNode;

public class GetintNode extends NodeBase implements Stmt {
    private NodeBase lVal;

    public GetintNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(NodeBase lVal) {
        this.lVal = lVal;
    }

    @Override
    public void checkErrors() {
        updateLastSymbolTable();
        ((LValNode) lVal).checkErrors();
        if (((LValNode) lVal).getExpContext().isConst()) {
            handleError(Error.ErrorType.MODIFIED_CONST);
        }
    }
}
