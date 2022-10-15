package frontend.syntax.expr.parse;

import frontend.error.Error;
import frontend.syntax.ParserBase;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class UnaryExpParser extends ParserBase {

    // <UnaryExp>      := <PrimaryExp> | Ident '(' [ <FuncRParams> ] ')' | <UnaryOp> <UnaryExp>
    public UnaryExpParser(Tokenizer tokenizer) {
        super(tokenizer);
        updateToken();
        if (lookForward(Token.TokenType.PLUS) || lookForward(Token.TokenType.MINU) || lookForward(Token.TokenType.NOT)) {
            reverse();
            new UnaryOpParser(tokenizer);
            new UnaryExpParser(tokenizer);
        } else if (lookForward(Token.TokenType.IDENFR)) {
            updateToken();
            if (lookForward(Token.TokenType.LPARENT)) {
                step();
                updateToken();
                if (lookForward(firsts.get("Exp"))) {
                    reverse();
                    new FuncRParamsParser(tokenizer);
                    if (!checkToken(Token.TokenType.RPARENT)) {
                        handleError(Error.ErrorType.MISSING_RPARENT);
                    }
                } else if (lookForward(Token.TokenType.RPARENT)) {
                    step();
                } else { //mind the position of handle error -> before or after step() reverse() checkToken()
                    reverse();
                    handleError(Error.ErrorType.MISSING_RPARENT);
                }
            } else {
                reverse();
                new PrimaryExpParser(tokenizer);
            }
        } else {
            reverse();
            new PrimaryExpParser(tokenizer);
        }
        display();
    }
}
