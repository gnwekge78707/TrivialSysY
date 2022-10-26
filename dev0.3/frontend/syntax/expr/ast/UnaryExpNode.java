package frontend.syntax.expr.ast;

import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.TokenNode;
import frontend.tokenize.Token;

import java.util.ArrayList;

public class UnaryExpNode extends NodeBase implements Calculatable {
    private NodeBase unaryOp;
    private NodeBase unaryExp;
    // <UnaryExp>      := <PrimaryExp> | Ident '(' [ <FuncRParams> ] ')' | <UnaryOp> <UnaryExp>

    public UnaryExpNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(NodeBase unaryOp, NodeBase unaryExp) {
        this.unaryExp = unaryExp;
        this.unaryOp = unaryOp;
    }

    private ExpContext expContext;

    public ExpContext getExpContext() {
        return expContext;
    }

    public void checkErrors() {
        updateLastSymbolTable();
        if (!(unaryExp instanceof Calculatable)) {
            throw new Error("CHECKERROR -> UnaryExp children not calculatable");
        }
        ((Calculatable) unaryExp).checkErrors();
        calculate();
    }

    public void calculate() {
        Calculatable exp = (Calculatable) unaryExp;
        boolean isConst = exp.getExpContext().isConst();
        boolean hasVal = exp.getExpContext().hasValue();
        int val = 0;
        if (hasVal) {
            val = exp.getExpContext().getValue();
            if (((TokenNode) unaryOp).getTokenType() == Token.TokenType.MINU) {
                val = -val;
            } else if (((TokenNode) unaryOp).getTokenType() == Token.TokenType.NOT) {
                val = (val == 0)? 1 : 0;
            }
        }
        ArrayList<Integer> childDims = exp.getExpContext().getDimensions();
        expContext = new ExpContext(val, isConst, hasVal,
                childDims == null ? null : new ArrayList<>(childDims));
    }
}
