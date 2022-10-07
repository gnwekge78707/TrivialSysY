package frontend.syntax.expr.parse;

import frontend.syntax.ParserBase;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class LOrExpParser extends ParserBase {

    // <LOrExp>        := <LAndExp> { '||' <LAndExp>}
    public LOrExpParser(Tokenizer tokenizer) {
        super(tokenizer);
        new LAndExpParser(tokenizer);
        display();
        while (checkToken(Token.TokenType.OR)) {
            new LAndExpParser(tokenizer);
            display();
        }
    }
}
