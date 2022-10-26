package frontend.syntax.func.parse;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.TokenNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class FuncTypeParser extends ParserBase {

    // <FuncType>      := 'void' | 'int'
    public FuncTypeParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        this.node = new TokenNode(parent, NodeBase.SyntaxType.FUNCTYPE); //todo
        if (!checkToken(Token.TokenType.VOIDTK, Token.TokenType.INTTK)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        ((TokenNode) node).register(this.getCurrentToken()); //todo
        display();
    }
}
