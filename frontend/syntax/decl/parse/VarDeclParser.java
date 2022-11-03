package frontend.syntax.decl.parse;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.decl.ast.VarDeclNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.ArrayList;

public class VarDeclParser extends ParserBase {

    // <VarDecl>       := <BType> <VarDef> { ',' <VarDef> } ';'
    public VarDeclParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        this.node = new VarDeclNode(parent, NodeBase.SyntaxType.VARDECL); //todo
        ArrayList<NodeBase> varDefs = new ArrayList<>(); //todo

        NodeBase btype = (new BTypeParser(tokenizer, this.node)).getNode();
        varDefs.add((new VarDefParser(tokenizer, this.node)).getNode());
        while (checkToken(Token.TokenType.COMMA)) {
            varDefs.add((new VarDefParser(tokenizer, this.node)).getNode());
        }
        if (!checkToken(Token.TokenType.SEMICN)) {
            handleError(Error.ErrorType.MISSING_SEMICOLON);
        }
        ((VarDeclNode) node).register(btype, varDefs); //todo
        display();
    }
}
