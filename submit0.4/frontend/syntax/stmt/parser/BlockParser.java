package frontend.syntax.stmt.parser;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.decl.parse.ConstDeclParser;
import frontend.syntax.decl.parse.ConstDefParser;
import frontend.syntax.decl.parse.VarDeclParser;
import frontend.syntax.decl.parse.VarDefParser;
import frontend.syntax.stmt.ast.BlockNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.ArrayList;

public class BlockParser extends ParserBase {

    // <Block> := '{' { ConstDecl | VarDecl | Stmt } '}'
    public BlockParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        this.node = new BlockNode(parent, NodeBase.SyntaxType.BLOCK); //todo
        ArrayList<NodeBase> blockItems = new ArrayList<>(); //todo

        if (!checkToken(Token.TokenType.LBRACE)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        while (true) {
            updateToken();
            if (lookForward(Token.TokenType.RBRACE)) {
                step();
                node.addErrorTokens(getCurrentToken(), Error.ErrorType.MISSING_RETURN);
                break;
            }
            if (lookForward(Token.TokenType.CONSTTK)) {
                reverse();
                blockItems.add((new ConstDeclParser(tokenizer, this.node)).getNode());
            } else if (lookForward(Token.TokenType.INTTK)) {
                reverse();
                blockItems.add((new VarDeclParser(tokenizer, this.node)).getNode());
            } else {
                reverse();
                blockItems.add((new StmtParser(tokenizer, this.node)).getNode());
            }
        }
        ((BlockNode) node).register(blockItems); //todo
        display();
    }
}
