package frontend.syntax.func.parse;

import frontend.error.Error;
import frontend.syntax.ParserBase;
import frontend.syntax.stmt.parser.BlockParser;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class FuncDefParser extends ParserBase {

    // <FuncDef>       := <FuncType> Ident '(' [<FuncFParams> ] ')' <Block>
    public FuncDefParser(Tokenizer tokenizer) {
        super(tokenizer);
        new FuncTypeParser(tokenizer);
        if (!checkToken(Token.TokenType.IDENFR)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        if (!checkToken(Token.TokenType.LPARENT)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        if (!checkToken(Token.TokenType.RPARENT)) {
            new FuncFParamsParser(tokenizer);
            if (!checkToken(Token.TokenType.RPARENT)) {
                handleError(Error.ErrorType.MISSING_RPARENT);
            }
        }
        new BlockParser(tokenizer);
        display();
    }
}
