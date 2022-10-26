package frontend.syntax.expr.parse;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class PrimaryExpParser extends ParserBase {

    // <PrimaryExp>    := '(' <Exp> ')' | <LVal> | <Number>
    public PrimaryExpParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);

        updateToken();
        if (lookForward(Token.TokenType.LPARENT)) {
            step();
            this.node = (new ExpParser(tokenizer, parent)).getNode();
            if (!checkToken(Token.TokenType.RPARENT)) {
                handleError(Error.ErrorType.MISSING_RPARENT);
            }
        } else if (lookForward(Token.TokenType.IDENFR)) {
            reverse();
            this.node = (new LValParser(tokenizer, parent)).getNode();
        } else {
            reverse();
            this.node = (new NumberParser(tokenizer, parent)).getNode();
        }
        display();
    }
}
