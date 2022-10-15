package frontend.syntax.expr.parse;

import frontend.error.Error;
import frontend.syntax.ParserBase;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class NumberParser extends ParserBase {

    // <Number>        := IntConst
    public NumberParser(Tokenizer tokenizer) {
        super(tokenizer);
        if (!checkToken(Token.TokenType.INTCON)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        display();
    }
}
