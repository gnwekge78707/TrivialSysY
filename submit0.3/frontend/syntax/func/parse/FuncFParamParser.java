package frontend.syntax.func.parse;

import frontend.error.Error;
import frontend.syntax.ParserBase;
import frontend.syntax.decl.parse.BTypeParser;
import frontend.syntax.expr.parse.ConstExpParser;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class FuncFParamParser extends ParserBase {

    // <FuncFParam>    := <BType> Ident [ '[' ']' { '[' <ConstExp> ']' } ]
    public FuncFParamParser(Tokenizer tokenizer) {
        super(tokenizer);
        new BTypeParser(tokenizer);
        if (!checkToken(Token.TokenType.IDENFR)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        if (checkToken(Token.TokenType.LBRACK)) {
            if (!checkToken(Token.TokenType.RBRACK)) {
                handleError(Error.ErrorType.MISSING_RBRACK);
            }
            while (checkToken(Token.TokenType.LBRACK)) {
                new ConstExpParser(tokenizer);
                if (!checkToken(Token.TokenType.RBRACK)) {
                    handleError(Error.ErrorType.MISSING_RBRACK);
                }
            }
        }
        display();
    }
}
