package frontend.syntax.stmt.ast;

import frontend.syntax.NodeBase;
import frontend.syntax.expr.ast.Calculatable;

public class LoopNode extends NodeBase implements Stmt {
    private NodeBase cond;
    private NodeBase stmt;

    public LoopNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(NodeBase cond, NodeBase stmt) {
        this.cond = cond;
        this.stmt = stmt;
    }

    @Override
    public void checkErrors() {
        updateLastSymbolTable();
        ((Calculatable) cond).checkErrors();
        if (stmt != null) {
            // incase of :: while(0);
            ((Stmt) stmt).checkErrors();
        }
    }
}
