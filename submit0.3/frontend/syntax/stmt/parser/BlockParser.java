package frontend.syntax.stmt.parser;

import frontend.error.Error;
import frontend.syntax.ParserBase;
import frontend.syntax.decl.parse.ConstDeclParser;
import frontend.syntax.decl.parse.ConstDefParser;
import frontend.syntax.decl.parse.VarDeclParser;
import frontend.syntax.decl.parse.VarDefParser;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class BlockParser extends ParserBase {

    // <Block> := '{' { ConstDecl | VarDecl | Stmt } '}'
    public BlockParser(Tokenizer tokenizer) {
        super(tokenizer);
        if (!checkToken(Token.TokenType.LBRACE)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        while (true) {
            updateToken();
            if (lookForward(Token.TokenType.RBRACE)) {
                step();
                break;
            }
            if (lookForward(Token.TokenType.CONSTTK)) {
                reverse();
                new ConstDeclParser(tokenizer);
            } else if (lookForward(Token.TokenType.INTTK)) {
                reverse();
                new VarDeclParser(tokenizer);
            } else {
                reverse();
                new StmtParser(tokenizer);
            }
        }
        display();
    }
}
