package frontend.syntax.expr.ast;

import frontend.error.Error;
import frontend.symbol.FuncSymbol;
import frontend.syntax.NodeBase;
import frontend.tokenize.Token;

import java.util.ArrayList;

// <UnaryExp>      := <PrimaryExp> | <Ident> '(' [ <FuncRParams> ] ')' | <UnaryOp> <UnaryExp>
public class FuncCallNode extends NodeBase implements Calculatable {
    private Token ident;
    private NodeBase funcRParams;

    public FuncCallNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(Token ident, NodeBase funcRParams) {
        this.ident = ident;
        this.funcRParams = funcRParams;
    }

    public NodeBase getFuncRParams() { //in order to register errorToken
        return funcRParams;
    }

    private ExpContext expContext;

    public ExpContext getExpContext() {
        return this.expContext;
    }

    //fixme: def has/hasNot FPARAMS, ref has/hasNot RPARAMS
    public void checkErrors() {
        updateLastSymbolTable();
        FuncSymbol funcSymbol = getLastSymbolTable().getFuncSymbol(ident.toString(), false);
        if (funcSymbol == null) {
            handleError(Error.ErrorType.UNDEFINED_IDENT);
            if (funcRParams != null) {
                ((FuncRParamsNode) funcRParams).checkErrors(null);
            }
            this.expContext = new ExpContext(0, false, false, new ArrayList<>());
            return;
        }

        if ((funcRParams == null && funcSymbol.hasParams())) {
            handleError(Error.ErrorType.MISMATCHED_PARAM_NUM);
        } else if (funcRParams != null) {
            ((FuncRParamsNode) funcRParams).checkErrors(funcSymbol);
        }

        if (funcSymbol.getDeclType().equals(Token.TokenType.INTTK)) {
            this.expContext = new ExpContext(0, false, false, new ArrayList<>());
        } else {
            this.expContext = new ExpContext(0, false, false, null);
        }
    }

    public void calculate() {
        //fixme: new ArrayList() can represent a int function, but VOID? set to null?
        this.expContext = new ExpContext(0, false, false, new ArrayList<>());
    }
}
