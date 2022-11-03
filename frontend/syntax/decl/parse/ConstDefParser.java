package frontend.syntax.decl.parse;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.decl.ast.VarDefNode;
import frontend.syntax.expr.parse.AddExpParser;
import frontend.syntax.expr.parse.ConstExpParser;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.ArrayList;

public class ConstDefParser extends ParserBase {

    // <ConstDef>      := Ident { '[' <ConstExp> ']' } '=' <ConstInitVal>
    public ConstDefParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        this.node = new VarDefNode(parent, NodeBase.SyntaxType.CONSTDEF); //todo
        Token ident; //todo
        ArrayList<NodeBase> constExps = new ArrayList<>(); //todo
        NodeBase constInitVal; //todo

        if (!checkToken(Token.TokenType.IDENFR)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        ident = this.getCurrentToken(); //todo
        while (checkToken(Token.TokenType.LBRACK)) {
            constExps.add((new ConstExpParser(tokenizer, this.node)).getNode());
            if (!checkToken(Token.TokenType.RBRACK)) {
                handleError(Error.ErrorType.MISSING_RBRACK);
            }
        }
        if (!checkToken(Token.TokenType.ASSIGN)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        constInitVal = (new ConstInitValParser(tokenizer, this.node)).getNode();
        ((VarDefNode) node).register(ident, constExps, constInitVal); //todo
        node.addErrorTokens(ident, Error.ErrorType.DUPLICATED_IDENT);
        display();
    }
}
