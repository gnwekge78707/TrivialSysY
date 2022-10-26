package frontend.syntax.stmt.parser;

import driver.CompilerDriver;
import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.expr.parse.CondParser;
import frontend.syntax.expr.parse.ExpParser;
import frontend.syntax.expr.parse.LValParser;
import frontend.syntax.stmt.ast.*;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.ArrayList;

/*
<Stmt> := <LVal> '=' <Exp> ';'
    | <Exp> ';'
    | ';'
    | <Block>
    | 'if' '(' <Cond> ')' <Stmt> [ 'else' <Stmt> ]
    | 'while' '(' <Cond> ')' <Stmt>
    | 'break' ';'
    | 'continue' ';'
    | 'return' [<Exp>] ';'
    | <LVal> '=' 'getint' '(' ')' ';'
    | 'printf' '(' FormatString { ',' <Exp> } ')' ';'
 */
public class StmtParser extends ParserBase {
    private NodeBase parent;

    public StmtParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        this.parent = parent;
        updateToken();
        if (lookForward(Token.TokenType.SEMICN)) {
            step();
            display();
        } else if (lookForward(Token.TokenType.LBRACE)) {
            reverse();
            this.node = (new BlockParser(tokenizer, parent)).getNode();
            display();
        } else if (lookForward(Token.TokenType.IFTK)) {
            reverse();
            parseIf(tokenizer);
        } else if (lookForward(Token.TokenType.WHILETK)) {
            reverse();
            parseWhile(tokenizer);
        } else if (lookForward(Token.TokenType.BREAKTK)) {
            reverse();
            parseBreak(tokenizer);
        } else if (lookForward(Token.TokenType.CONTINUETK)) {
            reverse();
            parseContinue(tokenizer);
        } else if (lookForward(Token.TokenType.RETURNTK)) {
            reverse();
            parseReturn(tokenizer);
        } else if (lookForward(Token.TokenType.PRINTFTK)) {
            reverse();
            parsePrint(tokenizer);
        } else if (lookForward(Token.TokenType.IDENFR)) {
            updateToken();
            if (lookForward(Token.TokenType.LPARENT)) {
                reverse();
                parseExp(tokenizer);
            } else {
                reverse();
                parseLVal(tokenizer);
            }
        } else {
            reverse();
            parseExp(tokenizer);
        }
    }

    private void parseIf(Tokenizer tokenizer) {
        this.node = new ConditionNode(parent, NodeBase.SyntaxType.STMT); //todo
        if (!checkToken(Token.TokenType.IFTK)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        if (!checkToken(Token.TokenType.LPARENT)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        NodeBase cond = (new CondParser(tokenizer, this.node)).getNode();
        if (!checkToken(Token.TokenType.RPARENT)) {
            handleError(Error.ErrorType.MISSING_RPARENT);
        }
        NodeBase stmt = (new StmtParser(tokenizer, this.node)).getNode();
        NodeBase elseStmt = null;
        if (checkToken(Token.TokenType.ELSETK)) {
            elseStmt = (new StmtParser(tokenizer, this.node)).getNode();
        }
        ((ConditionNode) node).register(cond, stmt, elseStmt); //todo
        display();
    }

    private void parseWhile(Tokenizer tokenizer) {
        this.node = new LoopNode(parent, NodeBase.SyntaxType.STMT); //todo
        if (!checkToken(Token.TokenType.WHILETK)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        if (!checkToken(Token.TokenType.LPARENT)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        NodeBase cond = (new CondParser(tokenizer, this.node)).getNode();
        if (!checkToken(Token.TokenType.RPARENT)) {
            handleError(Error.ErrorType.MISSING_RPARENT);
        }
        NodeBase stmt = (new StmtParser(tokenizer, this.node)).getNode();
        ((LoopNode) node).register(cond, stmt); //todo
        display();
    }

    private void parseBreak(Tokenizer tokenizer) {
        this.node = new BreakNode(parent, NodeBase.SyntaxType.STMT); //todo
        if (!checkToken(Token.TokenType.BREAKTK)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        node.addErrorTokens(getCurrentToken(), Error.ErrorType.MISPLACED_LOOP_CONTROL);
        if (!checkToken(Token.TokenType.SEMICN)) {
            handleError(Error.ErrorType.MISSING_SEMICOLON);
        }
        display();
    }

    private void parseContinue(Tokenizer tokenizer) {
        this.node = new ContinueNode(parent, NodeBase.SyntaxType.STMT); //todo
        if (!checkToken(Token.TokenType.CONTINUETK)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        node.addErrorTokens(getCurrentToken(), Error.ErrorType.MISPLACED_LOOP_CONTROL);
        if (!checkToken(Token.TokenType.SEMICN)) {
            handleError(Error.ErrorType.MISSING_SEMICOLON);
        }
        display();
    }

    private void parseReturn(Tokenizer tokenizer) {
        this.node = new ReturnNode(parent, NodeBase.SyntaxType.STMT); //todo
        if (!checkToken(Token.TokenType.RETURNTK)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        node.addErrorTokens(getCurrentToken(), Error.ErrorType.ILLEGAL_RETURN);
        NodeBase exp = null;
        updateToken();
        if (lookForward(firsts.get("Exp"))) {
            reverse();
            exp = (new ExpParser(tokenizer, this.node)).getNode();
        } else {
            reverse();
        }

        if (!checkToken(Token.TokenType.SEMICN)) {
            handleError(Error.ErrorType.MISSING_SEMICOLON);
        }
        ((ReturnNode) node).register(exp); //todo
        display();
    }

    private void parseLVal(Tokenizer tokenizer) {
        putCheckpoint();
        NodeBase lVal = (new LValParser(tokenizer, null)).getNode();
        if (!checkToken(Token.TokenType.ASSIGN)) {
            restoreCheckpoint();
            parseExp(tokenizer);
            return;
        }
        updateToken();
        if (lookForward(Token.TokenType.GETINTTK)) {
            step();
            this.node = new GetintNode(parent, NodeBase.SyntaxType.STMT); //todo
            lVal.setParent(this.node); //todo
            if (!checkToken(Token.TokenType.LPARENT)) {
                handleError(Error.ErrorType.UNDEFINED_ERROR);
            }
            if (!checkToken(Token.TokenType.RPARENT)) {
                handleError(Error.ErrorType.MISSING_RPARENT);
            }
            ((GetintNode) node).register(lVal); //todo
            node.addErrorTokens(
                    lVal.getErrorToken(Error.ErrorType.UNDEFINED_IDENT), Error.ErrorType.MODIFIED_CONST //TODO
            );
        } else {
            reverse();
            this.node = new AssignNode(parent, NodeBase.SyntaxType.STMT); //todo
            lVal.setParent(this.node); //todo
            NodeBase exp = (new ExpParser(tokenizer, this.node)).getNode();
            ((AssignNode) node).register(lVal, exp); //todo
            node.addErrorTokens(
                    lVal.getErrorToken(Error.ErrorType.UNDEFINED_IDENT), Error.ErrorType.MODIFIED_CONST //TODO
            );
        }
        if (!checkToken(Token.TokenType.SEMICN)) {
            handleError(Error.ErrorType.MISSING_SEMICOLON);
        }
        display();
    }

    private void parsePrint(Tokenizer tokenizer) {
        this.node = new PrintNode(parent, NodeBase.SyntaxType.STMT); //todo
        if (!checkToken(Token.TokenType.PRINTFTK)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        node.addErrorTokens(getCurrentToken(), Error.ErrorType.MISMATCHED_STRCON); //TODO
        if (!checkToken(Token.TokenType.LPARENT)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        if (!checkToken(Token.TokenType.STRCON)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        Token formatString = this.getCurrentToken(); //todo
        ArrayList<NodeBase> exps = new ArrayList<>(); //todo
        while (checkToken(Token.TokenType.COMMA)) {
            exps.add((new ExpParser(tokenizer, parent)).getNode());
        }
        if (!checkToken(Token.TokenType.RPARENT)) {
            handleError(Error.ErrorType.MISSING_RPARENT);
        }
        if (!checkToken(Token.TokenType.SEMICN)) {
            handleError(Error.ErrorType.MISSING_SEMICOLON);
        }
        ((PrintNode) node).register(formatString, exps); //todo
        node.addErrorTokens(formatString, Error.ErrorType.ILLEGAL_CHAR);
        display();
    }

    private void parseExp(Tokenizer tokenizer) {
        this.node = (new ExpParser(tokenizer, parent)).getNode();
        if (!checkToken(Token.TokenType.SEMICN)) {
            handleError(Error.ErrorType.MISSING_SEMICOLON);
        }
        display();
    }
}
