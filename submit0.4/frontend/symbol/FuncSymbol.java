package frontend.symbol;

import frontend.syntax.expr.parse.AddExpParser;
import frontend.tokenize.Token;

import java.util.ArrayList;
import java.util.Arrays;

public class FuncSymbol implements Symbol {
    private Token ident;
    private Token.TokenType declType;
    private ArrayList<LValSymbol> params;

    public FuncSymbol(Token ident, Token.TokenType declType, ArrayList<LValSymbol> params) {
        this.declType = declType;
        this.ident = ident;
        this.params = params;
    }

    public FuncSymbol(Token ident, Token.TokenType declType) {
        this.declType = declType;
        this.ident = ident;
        this.params = new ArrayList<>();
    }

    public void addParams(LValSymbol symbol) {
        params.add(symbol);
    }

    public ArrayList<LValSymbol> getParams() {
        return params;
    }

    public Token.TokenType getDeclType() {
        return declType;
    }

    public boolean hasParams() { return params.size() > 0; }

    public String getName() {
        return ident.toString();
    }
}
