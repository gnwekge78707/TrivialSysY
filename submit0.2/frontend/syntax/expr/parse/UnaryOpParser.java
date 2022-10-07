package frontend.syntax.expr.parse;

import frontend.error.Error;
import frontend.syntax.ParserBase;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class UnaryOpParser extends ParserBase {
    public UnaryOpParser(Tokenizer tokenizer) {
        super(tokenizer);
        if (!checkToken(Token.TokenType.PLUS, Token.TokenType.MINU, Token.TokenType.NOT)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        display();
    }
}
