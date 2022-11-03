package frontend.syntax.func.parse;

import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.func.ast.FuncFParamsNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.ArrayList;

public class FuncFParamsParser extends ParserBase {

    // <FuncFParams>   := <FuncFParam> { ',' <FuncFParam> }
    public FuncFParamsParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        this.node = new FuncFParamsNode(parent, NodeBase.SyntaxType.FUNCFPARAMS); //todo
        ArrayList<NodeBase> funcFParams = new ArrayList<>(); //todo

        funcFParams.add((new FuncFParamParser(tokenizer, this.node)).getNode());
        while (checkToken(Token.TokenType.COMMA)) {
            funcFParams.add((new FuncFParamParser(tokenizer, this.node)).getNode());
        }
        ((FuncFParamsNode) node).register(funcFParams); //todo
        display();
    }
}
