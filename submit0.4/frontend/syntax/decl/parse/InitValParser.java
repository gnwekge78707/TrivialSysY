package frontend.syntax.decl.parse;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.decl.ast.InitValNode;
import frontend.syntax.expr.parse.ExpParser;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.ArrayList;

public class InitValParser extends ParserBase {

    // <InitVal>       := <Exp> | '{' [ <InitVal> { ',' <InitVal> } ] '}'
    public InitValParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        updateToken();
        if (lookForward(Token.TokenType.LBRACE)) {
            step();
            this.node = new InitValNode(parent, NodeBase.SyntaxType.INITVAL); //todo
            ArrayList<NodeBase> initVals = new ArrayList<>(); //todo

            updateToken();
            if (lookForward(Token.TokenType.RBRACE)) {
                step();
            } else {
                reverse();
                initVals.add((new InitValParser(tokenizer, this.node)).getNode());
                while (checkToken(Token.TokenType.COMMA)) {
                    initVals.add((new InitValParser(tokenizer, this.node)).getNode());
                }
                if (!checkToken(Token.TokenType.RBRACE)) {
                    handleError(Error.ErrorType.UNDEFINED_ERROR);
                }
            }
            ((InitValNode) node).register(initVals);
        } else {
            reverse();
            this.node = (new ExpParser(tokenizer, parent)).getNode();
        }
        display();
    }
}
