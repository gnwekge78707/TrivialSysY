package frontend.tokenize;

import java.math.BigInteger;

public class IntConst extends Token {
    private final BigInteger bigIntegerValue;
    private final int intValue;

    public IntConst(int line, int startIdx, int endIdx, String value) {
        super(line, startIdx, endIdx, value, TokenType.INTCON);
        this.bigIntegerValue = new BigInteger(value);
        this.intValue = Integer.parseInt(value);
    }

    public BigInteger getBigIntegerValue() {
        return bigIntegerValue;
    }

    public int getIntValue() { return intValue; }
}
