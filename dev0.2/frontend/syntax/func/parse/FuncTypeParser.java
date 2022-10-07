package frontend.syntax.func.parse;

import frontend.error.Error;
import frontend.syntax.ParserBase;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class FuncTypeParser extends ParserBase {

    // <FuncType>      := 'void' | 'int'
    public FuncTypeParser(Tokenizer tokenizer) {
        super(tokenizer);
        if (!checkToken(Token.TokenType.VOIDTK, Token.TokenType.INTTK)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        display();
    }
}
