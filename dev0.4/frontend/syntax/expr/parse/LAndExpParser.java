package frontend.syntax.expr.parse;

import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.expr.ast.BinaryExpNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.ArrayList;

public class LAndExpParser extends ParserBase {

    // <LAndExp>       := <EqExp> { '&&' <EqExp>}
    public LAndExpParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);

        this.node = new BinaryExpNode(parent, NodeBase.SyntaxType.LANDEXP);
        NodeBase firstExp; //todo
        ArrayList<NodeBase> exps = new ArrayList<>(); //todo
        ArrayList<Token> ops = new ArrayList<>(); //todo

        firstExp = (new EqExpParser(tokenizer, node)).getNode();
        display();
        while (checkToken(Token.TokenType.AND)) {
            ops.add(getCurrentToken());
            exps.add((new EqExpParser(tokenizer, node)).getNode());
            display();
        }

        ((BinaryExpNode) node).register(firstExp, ops, exps); //todo
    }
}
