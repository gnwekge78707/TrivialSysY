package frontend.syntax.expr.parse;

import frontend.syntax.ParserBase;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class LAndExpParser extends ParserBase {

    // <LAndExp>       := <EqExp> { '&&' <EqExp>}
    public LAndExpParser(Tokenizer tokenizer) {
        super(tokenizer);
        new EqExpParser(tokenizer);
        display();
        while (checkToken(Token.TokenType.AND)) {
            new EqExpParser(tokenizer);
            display();
        }
    }
}
