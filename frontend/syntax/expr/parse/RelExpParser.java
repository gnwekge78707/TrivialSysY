package frontend.syntax.expr.parse;

import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.expr.ast.BinaryExpNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.ArrayList;

public class RelExpParser extends ParserBase {

    // <RelExp>        := <AddExp> { ('<' | '>' | '<=' | '>=') <AddExp>}
    public RelExpParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);

        this.node = new BinaryExpNode(parent, NodeBase.SyntaxType.RELEXP);
        NodeBase firstExp; //todo
        ArrayList<NodeBase> exps = new ArrayList<>(); //todo
        ArrayList<Token> ops = new ArrayList<>(); //todo

        firstExp = (new AddExpParser(tokenizer, node)).getNode();
        display();
        while (checkToken(Token.TokenType.LSS, Token.TokenType.GRE, Token.TokenType.LEQ, Token.TokenType.GEQ)) {
            ops.add(getCurrentToken()); //todo
            exps.add((new AddExpParser(tokenizer, node)).getNode());
            display();
        }

        ((BinaryExpNode) node).register(firstExp, ops, exps); //todo
    }
}
