package frontend.syntax.expr.parse;

import frontend.syntax.ParserBase;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class AddExpParser extends ParserBase {

    // <AddExp>        := <MulExp> { ('+' | '−') <MulExp>}
    public AddExpParser(Tokenizer tokenizer) {
        super(tokenizer);
        new MulExpParser(tokenizer);
        display();
        while (checkToken(Token.TokenType.PLUS, Token.TokenType.MINU)) {
            new MulExpParser(tokenizer);
            display();
        }
    }
}
