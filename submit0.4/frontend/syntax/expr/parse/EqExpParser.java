package frontend.syntax.expr.parse;

import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.expr.ast.BinaryExpNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.ArrayList;

public class EqExpParser extends ParserBase {

    // <EqExp>         := <RelExp> { ('==' | '!=') <RelExp>}
    public EqExpParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);

        this.node = new BinaryExpNode(parent, NodeBase.SyntaxType.EQEXP);
        NodeBase firstExp; //todo
        ArrayList<NodeBase> exps = new ArrayList<>(); //todo
        ArrayList<Token> ops = new ArrayList<>(); //todo

        firstExp = (new RelExpParser(tokenizer, node)).getNode();
        display();
        while (checkToken(Token.TokenType.EQL, Token.TokenType.NEQ)) {
            ops.add(getCurrentToken()); //todo
            exps.add((new RelExpParser(tokenizer, node)).getNode());
            display();
        }

        ((BinaryExpNode) node).register(firstExp, ops, exps); //todo
    }
}
