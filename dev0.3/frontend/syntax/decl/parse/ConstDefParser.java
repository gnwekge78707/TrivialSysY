package frontend.syntax.decl.parse;

import frontend.error.Error;
import frontend.syntax.ParserBase;
import frontend.syntax.expr.parse.ConstExpParser;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class ConstDefParser extends ParserBase {

    // <ConstDef>      := Ident { '[' <ConstExp> ']' } '=' <ConstInitVal>
    public ConstDefParser(Tokenizer tokenizer) {
        super(tokenizer);
        if (!checkToken(Token.TokenType.IDENFR)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        while (checkToken(Token.TokenType.LBRACK)) {
            new ConstExpParser(tokenizer);
            if (!checkToken(Token.TokenType.RBRACK)) {
                handleError(Error.ErrorType.MISSING_RBRACK);
            }
        }
        if (!checkToken(Token.TokenType.ASSIGN)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        new ConstInitValParser(tokenizer);
        display();
    }
}
