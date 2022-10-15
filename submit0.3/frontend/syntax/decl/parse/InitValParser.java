package frontend.syntax.decl.parse;

import frontend.error.Error;
import frontend.syntax.ParserBase;
import frontend.syntax.expr.parse.ExpParser;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class InitValParser extends ParserBase {

    // <InitVal>       := <Exp> | '{' [ <InitVal> { ',' <InitVal> } ] '}'
    public InitValParser(Tokenizer tokenizer) {
        super(tokenizer);
        updateToken();
        if (lookForward(Token.TokenType.LBRACE)) {
            step();
            updateToken();
            if (lookForward(Token.TokenType.RBRACE)) {
                step();
            } else {
                reverse();
                new InitValParser(tokenizer);
                while (checkToken(Token.TokenType.COMMA)) {
                    new InitValParser(tokenizer);
                }
                if (!checkToken(Token.TokenType.RBRACE)) {
                    handleError(Error.ErrorType.UNDEFINED_ERROR);
                }
            }
        } else {
            reverse();
            new ExpParser(tokenizer);
        }
        display();
    }
}
