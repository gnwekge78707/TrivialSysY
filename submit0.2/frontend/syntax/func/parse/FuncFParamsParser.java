package frontend.syntax.func.parse;

import frontend.syntax.ParserBase;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class FuncFParamsParser extends ParserBase {

    // <FuncFParams>   := <FuncFParam> { ',' <FuncFParam> }
    public FuncFParamsParser(Tokenizer tokenizer) {
        super(tokenizer);
        new FuncFParamParser(tokenizer);
        while (checkToken(Token.TokenType.COMMA)) {
            new FuncFParamParser(tokenizer);
        }
        display();
    }
}
