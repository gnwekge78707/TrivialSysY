package frontend.syntax.stmt.ast;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.expr.ast.LValNode;
import midend.ir.ModuleBuilder;
import midend.ir.Value;
import midend.ir.value.instr.terminator.CallInstr;

import java.util.ArrayList;

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

    @Override
    public void buildIR(ModuleBuilder builder) {
        Value src = new CallInstr(builder.getLibFunc("getint"), new ArrayList<>(), builder.getCurBasicBlock());
        ((LValNode) lVal).updateValue(src, builder);
    }
}
