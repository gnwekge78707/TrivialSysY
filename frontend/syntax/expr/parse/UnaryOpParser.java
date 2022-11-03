package frontend.syntax.expr.parse;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.TokenNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class UnaryOpParser extends ParserBase {
    public UnaryOpParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        this.node = new TokenNode(parent, NodeBase.SyntaxType.TOKEN); //todo

        if (!checkToken(Token.TokenType.PLUS, Token.TokenType.MINU, Token.TokenType.NOT)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        ((TokenNode) node).register(this.getCurrentToken());
        display();
    }


}
