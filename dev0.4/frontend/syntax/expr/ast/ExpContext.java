package frontend.syntax.expr.ast;

import java.util.ArrayList;

public class ExpContext {
    private int value;
    private boolean isConst;
    private boolean hasValue;
    private ArrayList<Integer> dimensions;

    // TODO: 'dimensions = new Arraylist()' -> is not ARRAY
    public ExpContext(int value, boolean isConst, boolean hasValue, ArrayList<Integer> dimensions) {
        this.value = value;
        this.isConst = isConst;
        this.hasValue = hasValue;
        this.dimensions = dimensions;
    }

    public int getValue() {
        return value;
    }

    public boolean isConst() {
        return isConst;
    }

    public boolean hasValue() {
        return hasValue;
    }

    public ArrayList<Integer> getDimensions() {
        return dimensions;
    }
}
