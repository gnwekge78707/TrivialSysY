package frontend.syntax.func.parse;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.decl.parse.BTypeParser;
import frontend.syntax.expr.parse.ConstExpParser;
import frontend.syntax.func.ast.FuncFParamNode;
import frontend.syntax.func.ast.FuncFParamsNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.ArrayList;

public class FuncFParamParser extends ParserBase {

    // <FuncFParam>    := <BType> Ident [ '[' ']' { '[' <ConstExp> ']' } ]
    public FuncFParamParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        this.node = new FuncFParamNode(parent, NodeBase.SyntaxType.FUNCFPARAM); //todo

        NodeBase btype = (new BTypeParser(tokenizer, this.node)).getNode();
        if (!checkToken(Token.TokenType.IDENFR)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        Token ident = this.getCurrentToken(); //todo
        boolean hasFirstDim = false; //todo
        ArrayList<NodeBase> constExps = new ArrayList<>(); //todo
        if (checkToken(Token.TokenType.LBRACK)) {
            if (!checkToken(Token.TokenType.RBRACK)) {
                handleError(Error.ErrorType.MISSING_RBRACK);
            }
            hasFirstDim = true; //todo
            while (checkToken(Token.TokenType.LBRACK)) {
                constExps.add((new ConstExpParser(tokenizer, this.node)).getNode());
                if (!checkToken(Token.TokenType.RBRACK)) {
                    handleError(Error.ErrorType.MISSING_RBRACK);
                }
            }
        }
        ((FuncFParamNode) node).register(btype, ident, hasFirstDim, constExps); //todo
        node.addErrorTokens(ident, Error.ErrorType.DUPLICATED_IDENT);
        display();
    }
}
