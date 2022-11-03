package frontend.syntax.expr.parse;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.expr.ast.LValNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.ArrayList;

public class LValParser extends ParserBase {

    // <LVal>          := Ident { '[' <Exp> ']' }
    public LValParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        Token ident; //todo
        ArrayList<NodeBase> exps = new ArrayList<>(); //todo
        this.node = new LValNode(parent, NodeBase.SyntaxType.LVAL); //todo

        if (!this.checkToken(Token.TokenType.IDENFR)) {
            this.handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        ident = this.getCurrentToken(); //todo
        while (this.checkToken(Token.TokenType.LBRACK)) {
            exps.add((new ExpParser(tokenizer, this.node)).getNode());
            if (!this.checkToken(Token.TokenType.RBRACK)) {
                //System.out.println("now missing ]::" + getCurrentToken().toString()+ "." + getCurrentToken().getEndIdx());
                this.handleError(Error.ErrorType.MISSING_RBRACK);
            }
        }

        ((LValNode) this.node).register(ident, exps); //todo
        node.addErrorTokens(ident, Error.ErrorType.UNDEFINED_IDENT);
        this.display();
    }
}
