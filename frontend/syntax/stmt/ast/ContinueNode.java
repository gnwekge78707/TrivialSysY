package frontend.syntax.stmt.ast;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import midend.ir.ModuleBuilder;
import midend.ir.value.instr.terminator.BrInstr;

public class ContinueNode extends NodeBase implements Stmt {
    public ContinueNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

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
        if (builder.getLoopCondBB().empty()) {
            throw new java.lang.Error("continue not in loop");
        }
        builder.putBrInstr(builder.getLoopCondBB().peek(), builder.getCurBasicBlock());
    }
}
