package frontend.syntax.decl.parse;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.decl.ast.VarDefNode;
import frontend.syntax.expr.parse.ConstExpParser;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.ArrayList;

public class VarDefParser extends ParserBase {

    // <VarDef>        := Ident { '[' <ConstExp> ']' }  [ '=' <InitVal> ]
    public VarDefParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        this.node = new VarDefNode(parent, NodeBase.SyntaxType.VARDEF); //todo
        Token ident; //todo
        ArrayList<NodeBase> constExps = new ArrayList<>(); //todo
        NodeBase initVal = null; //todo

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
        if (checkToken(Token.TokenType.ASSIGN)) {
            initVal = (new InitValParser(tokenizer, this.node)).getNode();
        }
        ((VarDefNode) node).register(ident, constExps, initVal);
        node.addErrorTokens(ident, Error.ErrorType.DUPLICATED_IDENT);
        display();
    }
}
