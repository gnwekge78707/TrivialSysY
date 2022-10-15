package frontend.syntax.expr.parse;

import frontend.syntax.ParserBase;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class EqExpParser extends ParserBase {

    // <EqExp>         := <RelExp> { ('==' | '!=') <RelExp>}
    public EqExpParser(Tokenizer tokenizer) {
        super(tokenizer);
        new RelExpParser(tokenizer);
        display();
        while (checkToken(Token.TokenType.EQL, Token.TokenType.NEQ)) {
            new RelExpParser(tokenizer);
            display();
        }
    }
}
