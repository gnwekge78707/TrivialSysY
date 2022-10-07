package frontend.syntax.expr.parse;

import frontend.syntax.ParserBase;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class FuncRParamsParser extends ParserBase {
    public FuncRParamsParser(Tokenizer tokenizer) {
        super(tokenizer);
        new ExpParser(tokenizer);
        while (checkToken(Token.TokenType.COMMA)) {
            new ExpParser(tokenizer);
        }
        display();
    }
}
