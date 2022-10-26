package frontend.syntax.stmt.ast;

import frontend.syntax.NodeBase;
import frontend.syntax.expr.ast.Calculatable;

public class ConditionNode extends NodeBase implements Stmt {
    private NodeBase cond;
    private NodeBase stmt;
    private NodeBase elseStmt;

    public ConditionNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(NodeBase cond, NodeBase stmt, NodeBase elseStmt) {
        this.cond = cond;
        this.stmt = stmt;
        this.elseStmt = elseStmt;
    }

    @Override
    public void checkErrors() {
        updateLastSymbolTable();
        ((Calculatable) cond).checkErrors();
        if (stmt != null) {
            // in case of :: if(1);
            ((Stmt) stmt).checkErrors();
        }
        if (elseStmt != null) {
            ((Stmt) elseStmt).checkErrors();
        }
    }
}
