package frontend.syntax.expr.parse;

import frontend.syntax.ParserBase;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class MulExpParser extends ParserBase {

    // <MulExp>        := <UnaryExp> { ('*' | '/' | '%') <UnaryExp>}
    // <MulExp>        := <UnaryExp> | <MulExp> ( '*' | '/' | '%' ) <UnaryExp>
    public MulExpParser(Tokenizer tokenizer) {
        super(tokenizer);
        new UnaryExpParser(tokenizer);
        display();
        while (checkToken(Token.TokenType.MULT, Token.TokenType.DIV, Token.TokenType.MOD)) {
            new UnaryExpParser(tokenizer);
            display();
        }
    }
}
