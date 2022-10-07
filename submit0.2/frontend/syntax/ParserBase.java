package frontend.syntax;

import driver.Config;
import driver.Output;
import frontend.error.Error;
import frontend.error.ErrorBuffer;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public abstract class ParserBase {
    private final Tokenizer tokenizer;
    private Token currentToken;

    public ParserBase(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
        this.currentToken = null;
    }

    public void updateToken() {
        currentToken = tokenizer.consumeToken();
    }

    public boolean checkToken(Token.TokenType... expectedTypes) {
        updateToken();
        if (currentToken != null) {
            for (Token.TokenType type : expectedTypes) {
                if (currentToken.getTokenType().equals(type)) {
                    tokenizer.step();
                    return true;
                }
            }
        }
        tokenizer.reverse();
        return false;
    }

    public boolean lookForward(Token.TokenType tokenType) {
        return currentToken != null && currentToken.getTokenType().equals(tokenType);
    }

    public boolean lookForward(HashSet<Token.TokenType> tokenTypes) {
        return currentToken != null && tokenTypes.contains(currentToken.getTokenType());
    }

    public void step() { tokenizer.step(); }

    public void reverse() { tokenizer.reverse(); }

    public void putCheckpoint() { tokenizer.putCheckpoint(); }

    public void restoreCheckpoint() { tokenizer.restoreCheckpoint(); }

    public void handleError(Error.ErrorType errorType) {
        if (!errorType.equals(Error.ErrorType.UNDEFINED_ERROR)) {
            /* TODO */
            //Token token = tokenizer.getPrevToken();
            Token token = currentToken;
            ErrorBuffer.getInstance().addError(new Error(errorType, token.getLine(), token.getEndIdx()));
        }
    }

    public String toString() {
        String className = this.getClass().toString();
        return className.substring(className.lastIndexOf('.') + 1, className.length() - 6);
    }

    public void display() {
        Output.getInstance().updateBuffer(Config.OutputLevel.SYNTAX, "<" + this + ">");
    }

    protected static final HashMap<String, HashSet<Token.TokenType>> firsts;

    static {
        firsts = new HashMap<>();
        firsts.put("Exp", new HashSet<>(Arrays.asList(
                Token.TokenType.NOT,
                Token.TokenType.PLUS,
                Token.TokenType.MINU,
                Token.TokenType.LPARENT,
                Token.TokenType.INTCON,
                Token.TokenType.IDENFR
        )));
    }

}
