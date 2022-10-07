package frontend.tokenize;

public class Ident extends Token {
    private final String name;

    public Ident(int line, int startIdx, int endIdx, String value) {
        super(line, startIdx, endIdx, value, TokenType.IDENFR);
        this.name = value;
    }

    public String getName() { return name; }
}
