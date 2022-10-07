package frontend.syntax.expr.parse;

import frontend.syntax.ParserBase;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class RelExpParser extends ParserBase {

    // <RelExp>        := <AddExp> { ('<' | '>' | '<=' | '>=') <AddExp>}
    public RelExpParser(Tokenizer tokenizer) {
        super(tokenizer);
        new AddExpParser(tokenizer);
        display();
        while (checkToken(Token.TokenType.LSS, Token.TokenType.GRE, Token.TokenType.LEQ, Token.TokenType.GEQ)) {
            new AddExpParser(tokenizer);
            display();
        }
    }
}
