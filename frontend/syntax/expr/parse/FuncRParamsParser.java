package frontend.syntax.expr.parse;

import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.expr.ast.FuncRParamsNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.ArrayList;

public class FuncRParamsParser extends ParserBase {
    public FuncRParamsParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);

        this.node = new FuncRParamsNode(parent, NodeBase.SyntaxType.FUNCRPARAMS); //todo
        ArrayList<NodeBase> exps = new ArrayList<>(); //todo

        exps.add((new ExpParser(tokenizer, node)).getNode());
        while (checkToken(Token.TokenType.COMMA)) {
            exps.add((new ExpParser(tokenizer, node)).getNode());
        }
        ((FuncRParamsNode) node).register(exps); //todo
        display();
    }
}
