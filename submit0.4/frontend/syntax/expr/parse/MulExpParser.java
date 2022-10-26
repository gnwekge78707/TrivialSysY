package frontend.syntax.expr.parse;

import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.expr.ast.BinaryExpNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.ArrayList;

public class MulExpParser extends ParserBase {

    // <MulExp>        := <UnaryExp> { ('*' | '/' | '%') <UnaryExp>}
    // <MulExp>        := <UnaryExp> | <MulExp> ( '*' | '/' | '%' ) <UnaryExp>
    public MulExpParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);

        this.node = new BinaryExpNode(parent, NodeBase.SyntaxType.MULEXP); //todo
        NodeBase firstExp; //todo
        ArrayList<NodeBase> exps = new ArrayList<>(); //todo
        ArrayList<Token> ops = new ArrayList<>(); //todo

        firstExp = (new UnaryExpParser(tokenizer, node)).getNode();
        display();
        while (checkToken(Token.TokenType.MULT, Token.TokenType.DIV, Token.TokenType.MOD)) {
            ops.add(getCurrentToken()); //todo
            exps.add((new UnaryExpParser(tokenizer, node)).getNode());
            display();
        }

        ((BinaryExpNode) node).register(firstExp, ops, exps); //todo
    }
}
