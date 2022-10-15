package frontend.tokenize;

import java.math.BigInteger;

public class IntConst extends Token {
    private final BigInteger intValue;

    public IntConst(int line, int startIdx, int endIdx, String value) {
        super(line, startIdx, endIdx, value, TokenType.INTCON);
        this.intValue = new BigInteger(value);
    }

    public BigInteger getIntValue() {
        return intValue;
    }
}
