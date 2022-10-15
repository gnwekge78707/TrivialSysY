package frontend.syntax.stmt.parser;

import driver.CompilerDriver;
import frontend.error.Error;
import frontend.syntax.ParserBase;
import frontend.syntax.expr.parse.CondParser;
import frontend.syntax.expr.parse.ExpParser;
import frontend.syntax.expr.parse.LValParser;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

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
    public StmtParser(Tokenizer tokenizer) {
        super(tokenizer);
        updateToken();
        if (lookForward(Token.TokenType.SEMICN)) {
            step();
            display();
        } else if (lookForward(Token.TokenType.LBRACE)) {
            reverse();
            new BlockParser(tokenizer);
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
        if (!checkToken(Token.TokenType.IFTK)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        if (!checkToken(Token.TokenType.LPARENT)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        new CondParser(tokenizer);
        if (!checkToken(Token.TokenType.RPARENT)) {
            handleError(Error.ErrorType.MISSING_RPARENT);
        }
        new StmtParser(tokenizer);
        if (checkToken(Token.TokenType.ELSETK)) {
            new StmtParser(tokenizer);
        }
        display();
    }

    private void parseWhile(Tokenizer tokenizer) {
        if (!checkToken(Token.TokenType.WHILETK)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        if (!checkToken(Token.TokenType.LPARENT)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        new CondParser(tokenizer);
        if (!checkToken(Token.TokenType.RPARENT)) {
            handleError(Error.ErrorType.MISSING_RPARENT);
        }
        new StmtParser(tokenizer);
        display();
    }

    private void parseBreak(Tokenizer tokenizer) {
        if (!checkToken(Token.TokenType.BREAKTK)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        if (!checkToken(Token.TokenType.SEMICN)) {
            handleError(Error.ErrorType.MISSING_SEMICOLON);
        }
        display();
    }

    private void parseContinue(Tokenizer tokenizer) {
        if (!checkToken(Token.TokenType.CONTINUETK)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        if (!checkToken(Token.TokenType.SEMICN)) {
            handleError(Error.ErrorType.MISSING_SEMICOLON);
        }
        display();
    }

    private void parseReturn(Tokenizer tokenizer) {
        if (!checkToken(Token.TokenType.RETURNTK)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        if (!checkToken(Token.TokenType.SEMICN)) {
            new ExpParser(tokenizer);
            if (!checkToken(Token.TokenType.SEMICN)) {
                handleError(Error.ErrorType.MISSING_SEMICOLON);
            }
        }
        display();
    }

    private void parseLVal(Tokenizer tokenizer) {
        putCheckpoint();
        new LValParser(tokenizer);
        if (!checkToken(Token.TokenType.ASSIGN)) {
            restoreCheckpoint();
            parseExp(tokenizer);
            return;
        }
        updateToken();
        if (lookForward(Token.TokenType.GETINTTK)) {
            step();
            if (!checkToken(Token.TokenType.LPARENT)) {
                handleError(Error.ErrorType.UNDEFINED_ERROR);
            }
            if (!checkToken(Token.TokenType.RPARENT)) {
                handleError(Error.ErrorType.MISSING_RPARENT);
            }
        } else {
            reverse();
            new ExpParser(tokenizer);
        }
        if (!checkToken(Token.TokenType.SEMICN)) {
            handleError(Error.ErrorType.MISSING_SEMICOLON);
        }
        display();
    }

    private void parsePrint(Tokenizer tokenizer) {
        if (!checkToken(Token.TokenType.PRINTFTK)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        if (!checkToken(Token.TokenType.LPARENT)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        if (!checkToken(Token.TokenType.STRCON)) {
            handleError(Error.ErrorType.UNDEFINED_ERROR);
        }
        while (checkToken(Token.TokenType.COMMA)) {
            new ExpParser(tokenizer);
        }
        if (!checkToken(Token.TokenType.RPARENT)) {
            handleError(Error.ErrorType.MISSING_RPARENT);
        }
        if (!checkToken(Token.TokenType.SEMICN)) {
            handleError(Error.ErrorType.MISSING_SEMICOLON);
        }
        display();
    }

    private void parseExp(Tokenizer tokenizer) {
        new ExpParser(tokenizer);
        if (!checkToken(Token.TokenType.SEMICN)) {
            handleError(Error.ErrorType.MISSING_SEMICOLON);
        }
        display();
    }
}
