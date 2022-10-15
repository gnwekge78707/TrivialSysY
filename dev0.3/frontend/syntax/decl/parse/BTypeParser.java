package frontend.syntax.decl.parse;

import frontend.error.Error;
import frontend.syntax.ParserBase;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class BTypeParser extends ParserBase {
    public BTypeParser(Tokenizer tokenizer) {
        super(tokenizer);
        if (!checkToken(Token.TokenType.INTTK)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
    }
}
