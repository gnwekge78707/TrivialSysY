package frontend.syntax.decl.parse;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.decl.ast.VarDeclNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.ArrayList;

public class ConstDeclParser extends ParserBase {

    // <ConstDecl>     := 'const' <BType> <ConstDef> { ',' <ConstDef> } ';'
    public ConstDeclParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        this.node = new VarDeclNode(parent, NodeBase.SyntaxType.CONSTDECL); //todo
        NodeBase btype; //todo
        ArrayList<NodeBase> constDefs = new ArrayList<>(); //todo

        if (!checkToken(Token.TokenType.CONSTTK)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        btype = (new BTypeParser(tokenizer, this.node)).getNode();
        constDefs.add((new ConstDefParser(tokenizer, this.node)).getNode());
        while (checkToken(Token.TokenType.COMMA)) {
            constDefs.add((new ConstDefParser(tokenizer, this.node)).getNode());
        }
        if (!checkToken(Token.TokenType.SEMICN)) {
            handleError(Error.ErrorType.MISSING_SEMICOLON);
        }
        ((VarDeclNode) node).register(btype, constDefs); //todo
        display();
    }
}
