package frontend.syntax.decl.parse;

import frontend.error.Error;
import frontend.syntax.ParserBase;
import frontend.syntax.expr.parse.ConstExpParser;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class ConstInitValParser extends ParserBase {

    // <ConstInitVal>  := <ConstExp> | '{' [ <ConstInitVal> { ',' <ConstInitVal> } ] '}'
    public ConstInitValParser(Tokenizer tokenizer) {
        super(tokenizer);
        updateToken();
        if (lookForward(Token.TokenType.LBRACE)) {
            step();
            updateToken();
            if (lookForward(Token.TokenType.RBRACE)) {
                step();
            } else {
                reverse();
                new ConstInitValParser(tokenizer);
                while (checkToken(Token.TokenType.COMMA)) {
                    new ConstInitValParser(tokenizer);
                }
                if (!checkToken(Token.TokenType.RBRACE)) {
                    handleError(Error.ErrorType.UNDEFINED_ERROR);
                }
            }
        } else {
            reverse();
            new ConstExpParser(tokenizer);
        }
        display();
    }
}
