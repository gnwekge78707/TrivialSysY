package frontend.syntax.expr.parse;

import frontend.error.Error;
import frontend.syntax.ParserBase;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class LValParser extends ParserBase {

    // <LVal>          := Ident { '[' <Exp> ']' }
    public LValParser(Tokenizer tokenizer) {
        super(tokenizer);
        if (!this.checkToken(Token.TokenType.IDENFR)) {
            this.handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        while (this.checkToken(Token.TokenType.LBRACK)) {
            new ExpParser(tokenizer);
            if (!this.checkToken(Token.TokenType.RBRACK)) {
                this.handleError(Error.ErrorType.MISSING_RBRACK);
            }
        }
        this.display();
    }
}
