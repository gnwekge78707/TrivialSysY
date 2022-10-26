package frontend.symbol;

import frontend.syntax.NodeBase;
import frontend.syntax.expr.ast.BinaryExpNode;
import frontend.tokenize.Token;

import java.util.ArrayList;
import java.util.HashMap;

public class LValSymbol implements Symbol {
    private boolean isConst;
    private Token ident;
    private Token.TokenType declType;
    private ArrayList<Integer> dimensions = new ArrayList<>();
    private HashMap<ArrayList<Integer>, BinaryExpNode> inits = new HashMap<>();

    public LValSymbol(boolean isConst, Token ident, Token.TokenType declType,
                      ArrayList<Integer> dimensions, HashMap<ArrayList<Integer>, BinaryExpNode> inits) {
        this.isConst = isConst;
        this.ident = ident;
        this.dimensions = dimensions;
        this.inits = inits;
        this.declType = declType;
    }

    public LValSymbol(boolean isConst, Token.TokenType declType) {
        this.isConst = isConst;
        this.declType = declType;
    }

    public void setIdent(Token ident) {
        this.ident = ident;
    }

    public void addDimension(int dim) {
        dimensions.add(dim);
    }

    public ArrayList<Integer> getDimensions() {
        return dimensions;
    }

    public void addInit(ArrayList<Integer> dims, BinaryExpNode init) {
        this.inits.put(dims, init);
    }

    public boolean hasInits() {
        return inits.size() != 0;
    }

    public BinaryExpNode getInit(ArrayList<Integer> dims) {
        return inits.getOrDefault(dims, null);
    }

    public boolean isConst() {
        return isConst;
    }

    public String getName() {
        return ident.toString();
    }
}
