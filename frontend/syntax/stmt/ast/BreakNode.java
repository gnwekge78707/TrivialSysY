package frontend.syntax.stmt.ast;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import midend.ir.ModuleBuilder;

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

    @Override
    public void buildIR(ModuleBuilder builder) {
        if (builder.getLoopNextBB().empty()) {
            throw new java.lang.Error("break not in loop");
        }
        builder.putBrInstr(builder.getLoopNextBB().peek(), builder.getCurBasicBlock());
    }
}
