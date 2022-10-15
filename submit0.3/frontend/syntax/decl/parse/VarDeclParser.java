package frontend.syntax.decl.parse;

import frontend.error.Error;
import frontend.syntax.ParserBase;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class VarDeclParser extends ParserBase {

    // <VarDecl>       := <BType> <VarDef> { ',' <VarDef> } ';'
    public VarDeclParser(Tokenizer tokenizer) {
        super(tokenizer);
        new BTypeParser(tokenizer);
        new VarDefParser(tokenizer);
        while (checkToken(Token.TokenType.COMMA)) {
            new VarDefParser(tokenizer);
        }
        if (!checkToken(Token.TokenType.SEMICN)) {
            handleError(Error.ErrorType.MISSING_SEMICOLON);
        }
        display();
    }
}
