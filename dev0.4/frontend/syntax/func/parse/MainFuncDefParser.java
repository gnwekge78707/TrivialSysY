package frontend.syntax.func.parse;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.stmt.parser.BlockParser;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class MainFuncDefParser extends ParserBase {

    // <MainFuncDef>   := 'int' 'main' '(' ')' <Block>
    public MainFuncDefParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        if (!checkToken(Token.TokenType.INTTK)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        if (!checkToken(Token.TokenType.MAINTK)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        if (!checkToken(Token.TokenType.LPARENT)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        if (!checkToken(Token.TokenType.RPARENT))  {
            handleError(Error.ErrorType.MISSING_RPARENT);
        }
        this.node = (new BlockParser(tokenizer, parent)).getNode();
        display();
    }
}
