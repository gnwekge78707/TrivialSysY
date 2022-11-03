package frontend.syntax.expr.parse;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.TokenNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class NumberParser extends ParserBase {

    // <Number>        := IntConst
    public NumberParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        node = new TokenNode(parent, NodeBase.SyntaxType.TOKEN); //todo

        if (!checkToken(Token.TokenType.INTCON)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        ((TokenNode) node).register(this.getCurrentToken()); //todo
        display();
    }
}
