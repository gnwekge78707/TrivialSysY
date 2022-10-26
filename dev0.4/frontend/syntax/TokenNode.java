package frontend.syntax;

import frontend.syntax.expr.ast.Calculatable;
import frontend.syntax.expr.ast.ExpContext;
import frontend.tokenize.IntConst;
import frontend.tokenize.Token;

import java.util.ArrayList;

public class TokenNode extends NodeBase implements Calculatable {
    private Token token;

    public TokenNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public Token.TokenType getTokenType() { return token.getTokenType(); }

    private ExpContext expContext;

    public ExpContext getExpContext() {
        if (!(token instanceof IntConst)) {
            throw new Error("token not instance of intConst -- "+ token.getTokenType());
        }
        return expContext;
    }

    public void calculate() {
        if (token instanceof IntConst) {
            this.expContext = new ExpContext(((IntConst) token).getIntValue(), true, true, new ArrayList<>());
        }
    }

    public void checkErrors() {
        updateLastSymbolTable();
        calculate();
    }
}
