package frontend.syntax.func.parse;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.TokenNode;
import frontend.syntax.func.ast.FuncDefNode;
import frontend.syntax.stmt.parser.BlockParser;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class FuncDefParser extends ParserBase {

    // <FuncDef>       := <FuncType> Ident '(' [<FuncFParams> ] ')' <Block>
    public FuncDefParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        this.node = new FuncDefNode(parent, NodeBase.SyntaxType.FUNCDEF); //todo

        NodeBase funcType = (new FuncTypeParser(tokenizer, this.node)).getNode();
        if (!checkToken(Token.TokenType.IDENFR)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        Token ident = this.getCurrentToken(); //todo
        if (!checkToken(Token.TokenType.LPARENT)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        NodeBase funcFParams = null;
        //fixme: missing RPARENT error
        if (!checkToken(Token.TokenType.RPARENT)) {
            funcFParams = (new FuncFParamsParser(tokenizer, this.node)).getNode();
            if (!checkToken(Token.TokenType.RPARENT)) {
                handleError(Error.ErrorType.MISSING_RPARENT);
            }
        }
        NodeBase block = (new BlockParser(tokenizer, this.node)).getNode();

        ((FuncDefNode) node).register(funcType, ident, funcFParams, block); //todo
        node.addErrorTokens(ident, Error.ErrorType.DUPLICATED_IDENT);
        display();
    }
}
