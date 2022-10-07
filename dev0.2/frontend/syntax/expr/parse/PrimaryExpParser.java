package frontend.syntax.expr.parse;

import frontend.error.Error;
import frontend.syntax.ParserBase;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class PrimaryExpParser extends ParserBase {

    // <PrimaryExp>    := '(' <Exp> ')' | <LVal> | <Number>
    public PrimaryExpParser(Tokenizer tokenizer) {
        super(tokenizer);
        updateToken();
        if (lookForward(Token.TokenType.LPARENT)) {
            step();
            new ExpParser(tokenizer);
            if (!checkToken(Token.TokenType.RPARENT)) {
                handleError(Error.ErrorType.MISSING_RPARENT);
            }
        } else if (lookForward(Token.TokenType.IDENFR)) {
            reverse();
            new LValParser(tokenizer);
        } else {
            reverse();
            new NumberParser(tokenizer);
        }
        display();
    }
}
