package frontend.syntax.expr.parse;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.expr.ast.FuncCallNode;
import frontend.syntax.expr.ast.UnaryExpNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class UnaryExpParser extends ParserBase {

    // <UnaryExp>      := <PrimaryExp> | Ident '(' [ <FuncRParams> ] ')' | <UnaryOp> <UnaryExp>
    public UnaryExpParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        updateToken();
        if (lookForward(Token.TokenType.PLUS) || lookForward(Token.TokenType.MINU) || lookForward(Token.TokenType.NOT)) {
            reverse();

            this.node = new UnaryExpNode(parent, NodeBase.SyntaxType.UNARYEXP); //todo
            ((UnaryExpNode) node).register( //todo
                    (new UnaryOpParser(tokenizer, node)).getNode(),
                    (new UnaryExpParser(tokenizer, node)).getNode());

        } else if (lookForward(Token.TokenType.IDENFR)) {
            Token ident = this.getCurrentToken(); //todo

            updateToken();
            if (lookForward(Token.TokenType.LPARENT)) {
                step();

                this.node = new FuncCallNode(parent, NodeBase.SyntaxType.UNARYEXP); //todo
                node.addErrorTokens(ident, Error.ErrorType.UNDEFINED_IDENT); //todo
                node.addErrorTokens(ident, Error.ErrorType.MISMATCHED_PARAM_NUM); //todo

                updateToken();
                if (lookForward(firsts.get("Exp"))) {
                    reverse();

                    ((FuncCallNode) node).register(
                            ident,
                            (new FuncRParamsParser(tokenizer, node)).getNode());
                    ((FuncCallNode) node).getFuncRParams().addErrorTokens(ident, Error.ErrorType.MISMATCHED_PARAM_NUM);
                    ((FuncCallNode) node).getFuncRParams().addErrorTokens(ident, Error.ErrorType.MISMATCHED_PARAM_TYPE);
                    //fixme: needs to clarify (error ident's Parser, error handled node) relation

                    if (!checkToken(Token.TokenType.RPARENT)) {
                        handleError(Error.ErrorType.MISSING_RPARENT);
                    }
                } else if (lookForward(Token.TokenType.RPARENT)) {
                    step();
                    ((FuncCallNode) node).register(ident, null);
                } else { //mind the position of handle error -> before or after step() reverse() checkToken()
                    reverse();
                    ((FuncCallNode) node).register(ident, null);
                    handleError(Error.ErrorType.MISSING_RPARENT);
                }
            } else {
                reverse();
                this.node = (new PrimaryExpParser(tokenizer, parent)).getNode();
            }
        } else {
            reverse();
            this.node = (new PrimaryExpParser(tokenizer, parent)).getNode();
        }
        display();
    }
}
