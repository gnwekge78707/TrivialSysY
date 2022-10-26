package frontend.syntax.stmt.ast;

import frontend.error.Error;
import frontend.syntax.NodeBase;

public class BreakNode extends NodeBase implements Stmt {
    public BreakNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    @Override
    public void checkErrors() {
        updateLastSymbolTable();
        NodeBase i = this.getParent();
        while (i != null && !(i instanceof LoopNode)) {
            i = i.getParent();
        }
        if (i == null) {
            handleError(Error.ErrorType.MISPLACED_LOOP_CONTROL);
        }
    }
}
