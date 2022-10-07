package frontend.tokenize;

public class FormatStr extends Token {
    private final String formatStr;

    public FormatStr(int line, int startIdx, int endIdx, String value) {
        super(line, startIdx, endIdx, value, TokenType.STRCON);
        this.formatStr = value;
    }

    public String getFormatStr() {
        return formatStr;
    }

    public String getInnerFormatStr() {
        assert formatStr.length() >= 2 && formatStr.charAt(0) == '\"' && formatStr.charAt(formatStr.length() - 1) == '\"';
        return formatStr.substring(1, formatStr.length() - 1);
    }
}
