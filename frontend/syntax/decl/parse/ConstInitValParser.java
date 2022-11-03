package frontend.syntax.decl.parse;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.decl.ast.InitValNode;
import frontend.syntax.expr.parse.ConstExpParser;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.ArrayList;

public class ConstInitValParser extends ParserBase {

    // <ConstInitVal>  := <ConstExp> | '{' [ <ConstInitVal> { ',' <ConstInitVal> } ] '}'
    public ConstInitValParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        updateToken();
        if (lookForward(Token.TokenType.LBRACE)) {
            step();
            this.node = new InitValNode(parent, NodeBase.SyntaxType.CONSTINITVAL); //todo
            ArrayList<NodeBase> constInitVals = new ArrayList<>(); //todo

            updateToken();
            if (lookForward(Token.TokenType.RBRACE)) {
                step();
            } else {
                reverse();
                constInitVals.add((new ConstInitValParser(tokenizer, this.node)).getNode());
                while (checkToken(Token.TokenType.COMMA)) {
                    constInitVals.add((new ConstInitValParser(tokenizer, this.node)).getNode());
                }
                if (!checkToken(Token.TokenType.RBRACE)) {
                    handleError(Error.ErrorType.UNDEFINED_ERROR);
                }
            }
            ((InitValNode) node).register(constInitVals); //todo
        } else {
            reverse();
            this.node = (new ConstExpParser(tokenizer, parent)).getNode();
        }
        display();
    }
}
