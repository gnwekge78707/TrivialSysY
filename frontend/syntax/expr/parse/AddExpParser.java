package frontend.syntax.expr.parse;

import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.expr.ast.BinaryExpNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.ArrayList;

public class AddExpParser extends ParserBase {

    // <AddExp>        := <MulExp> { ('+' | '−') <MulExp>}
    public AddExpParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);

        this.node = new BinaryExpNode(parent, NodeBase.SyntaxType.ADDEXP); //todo
        NodeBase firstExp; //todo
        ArrayList<NodeBase> exps = new ArrayList<>(); //todo
        ArrayList<Token> ops = new ArrayList<>(); //todo

        firstExp = (new MulExpParser(tokenizer, node)).getNode();
        display();
        while (checkToken(Token.TokenType.PLUS, Token.TokenType.MINU)) {
            ops.add(getCurrentToken()); //todo
            exps.add((new MulExpParser(tokenizer, node)).getNode());
            display();
        }

        ((BinaryExpNode) node).register(firstExp, ops, exps); //todo
    }
}
