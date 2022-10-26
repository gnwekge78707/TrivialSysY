package frontend.syntax;


import frontend.error.Error;
import frontend.error.ErrorBuffer;
import frontend.symbol.SymbolTable;
import frontend.tokenize.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public abstract class NodeBase {
    private SyntaxType syntaxType;
    private NodeBase parent;
    private SymbolTable lastSymbolTable;
    private HashMap<Error.ErrorType, Token> errorTokens = new HashMap<>();

    //fixme: during init method, parent's symbol table has not been initialized
    public NodeBase(NodeBase parent, SyntaxType syntaxType) {
        this.parent = parent;
        this.syntaxType = syntaxType;
        /*
        this.lastSymbolTable = parent == null ? null : parent instanceof Scope ?
                ((Scope) parent).getSymbolTable() : parent.getLastSymbolTable();
        */
    }

    public SyntaxType getSyntaxType() {
        return syntaxType;
    }

    public void setParent(NodeBase parent) {
        this.parent = parent;
    }

    public NodeBase getParent() {
        return parent;
    }

    //TODO: block -> block -> block, will miss symbol table?
    public SymbolTable getLastSymbolTable() {
        return this.lastSymbolTable;
    }

    public void updateLastSymbolTable() {
        //System.out.println("in " + this.syntaxType.name() + ";");
        this.lastSymbolTable = parent == null ? null : parent instanceof Scope ?
                ((Scope) parent).getSymbolTable() : parent.getLastSymbolTable();
    }

    public void addErrorTokens(Token token, Error.ErrorType errorType) {
        this.errorTokens.put(errorType, token);
    }

    public int getErrorLine(Error.ErrorType errorType) {
        return errorTokens.get(errorType).getLine();
    }

    public Token getErrorToken(Error.ErrorType errorType) { return this.errorTokens.get(errorType); }

    public void handleError(Error.ErrorType errorType) {
        if (!errorType.equals(Error.ErrorType.UNDEFINED_ERROR)) {
            /* TODO */
            //Token token = tokenizer.getPrevToken();
            assert errorTokens.containsKey(errorType);
            Token token = errorTokens.get(errorType);
            ErrorBuffer.getInstance().addError(new Error(errorType, token.getLine(), token.getEndIdx()));
        }
    }

    public enum SyntaxType {
        EXP,
        COND,
        LVAL,
        PRIMARYEXP,
        NUMBER,
        UNARYEXP,
        UNARYOP,
        FUNCRPARAMS,
        MULEXP,
        ADDEXP,
        RELEXP,
        EQEXP,
        LANDEXP,
        LOREXP,
        CONSTEXP,

        TOKEN,
        BTYPE,
        CONSTDECL,
        CONSTDEF,
        CONSTINITVAL,
        VARDECL,
        VARDEF,
        INITVAL,

        STMT,
        BLOCK,

        FUNCDEF,
        MAINFUNCDEF,
        FUNCTYPE,
        FUNCFPARAMS,
        FUNCFPARAM,
        COMPUNIT
    }
}
