package frontend.tokenize;

public class IntConst extends Token {
    private final int intValue;

    public IntConst(int line, int startIdx, int endIdx, String value) {
        super(line, startIdx, endIdx, value, TokenType.INTCON);
        this.intValue = Integer.parseInt(value);
    }

    public int getIntValue() {
        return intValue;
    }
}
