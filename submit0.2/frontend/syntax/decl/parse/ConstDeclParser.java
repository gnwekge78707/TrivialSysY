package frontend.syntax.decl.parse;

import frontend.error.Error;
import frontend.syntax.ParserBase;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class ConstDeclParser extends ParserBase {

    // <ConstDecl>     := 'const' <BType> <ConstDef> { ',' <ConstDef> } ';'
    public ConstDeclParser(Tokenizer tokenizer) {
        super(tokenizer);
        if (!checkToken(Token.TokenType.CONSTTK)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        new BTypeParser(tokenizer);
        new ConstDefParser(tokenizer);
        while (checkToken(Token.TokenType.COMMA)) {
            new ConstDefParser(tokenizer);
        }
        if (!checkToken(Token.TokenType.SEMICN)) {
            handleError(Error.ErrorType.MISSING_SEMICOLON);
        }
        display();
    }
}
