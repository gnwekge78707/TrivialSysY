package frontend.tokenize;

public class Reserved extends Token{
    public Reserved(int line, int startIdx, int endIdx, String value, TokenType tokenType) {
        super(line, startIdx, endIdx, value, tokenType);
    }
}
