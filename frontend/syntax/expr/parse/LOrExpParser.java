package frontend.syntax.expr.parse;

import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.expr.ast.BinaryExpNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.ArrayList;

public class LOrExpParser extends ParserBase {

    // <LOrExp>        := <LAndExp> { '||' <LAndExp>}
    public LOrExpParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);

        this.node = new BinaryExpNode(parent, NodeBase.SyntaxType.LOREXP);
        NodeBase firstExp; //todo
        ArrayList<NodeBase> exps = new ArrayList<>(); //todo
        ArrayList<Token> ops = new ArrayList<>(); //todo

        firstExp = (new LAndExpParser(tokenizer, node)).getNode();
        display();
        while (checkToken(Token.TokenType.OR)) {
            ops.add(getCurrentToken());
            exps.add((new LAndExpParser(tokenizer, node)).getNode());
            display();
        }

        ((BinaryExpNode) node).register(firstExp, ops, exps); //todo
    }
}
