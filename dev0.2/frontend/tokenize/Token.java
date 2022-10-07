package frontend.tokenize;

import java.io.PrintStream;
import java.util.regex.Pattern;

public abstract class Token {
    private final int line;
    private final int startIdx;
    private final int endIdx;
    private final String value;
    private final TokenType tokenType;

    public static Token getInstance(int line, int startIdx, int endIdx, String value, TokenType tokenType) {
        switch (tokenType) {
            case IDENFR: return new Ident(line, startIdx, endIdx, value);
            case INTCON: return new IntConst(line, startIdx, endIdx, value);
            case STRCON: return new FormatStr(line, startIdx, endIdx, value);
            default: return new Reserved(line, startIdx, endIdx, value, tokenType);
        }
    }

    protected Token(int line, int startIdx, int endIdx, String value, TokenType tokenType) {
        this.line = line;
        this.startIdx = startIdx;
        this.endIdx = endIdx;
        this.value = value;
        this.tokenType = tokenType;
    }

    public int getLine() {
        return line;
    }

    public int getStartIdx() {
        return startIdx;
    }

    public int getEndIdx() {
        return endIdx;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getTokenTypeName() {
        return tokenType.name();
    }

    @Override
    public String toString() {
        return value;
    }

    public void output(PrintStream printStream) {
        printStream.println(tokenType.name() + " " + value);
    }

    public String getOutputString() { return tokenType.name() + " " + value; }

    public enum TokenType {
        // RESERVED
        MAINTK("main", true),
        CONSTTK("const", true),
        INTTK("int", true),
        BREAKTK("break", true),
        CONTINUETK("continue", true),
        IFTK("if", true),
        ELSETK("else", true),
        VOIDTK("void", true),
        WHILETK("while", true),
        GETINTTK("getint", true),
        PRINTFTK("printf", true),
        RETURNTK("return", true),
        // IDENT INTCONST STRCONTENT
        IDENFR("[_A-Za-z][_A-Za-z0-9]*"),
        INTCON("[0-9]+"),
        STRCON("\\\"[^\\\"]*\\\""),
        // DELIMITER
        AND("&&"),
        OR("\\|\\|"),
        LEQ("<="),
        GEQ(">="),
        EQL("=="),
        NEQ("!="),
        PLUS("\\+"),
        MINU("-"),
        MULT("\\*"),
        DIV("/"),
        MOD("%"),
        LSS("<"),
        GRE(">"),
        NOT("!"),
        ASSIGN("="),
        SEMICN(";"),
        COMMA(","),
        LPARENT("\\("),
        RPARENT("\\)"),
        LBRACK("\\["),
        RBRACK("]"),
        LBRACE("\\{"),
        RBRACE("}")
        ;

        private final Pattern pattern;

        TokenType(String pattern) {
            this.pattern = Pattern.compile("^" + pattern);
        }

        TokenType(String pattern, boolean postCheck) {
            String checker = postCheck ? "(?![_A-Za-z0-9])" : "";
            this.pattern = Pattern.compile("^" + pattern + checker);
        }

        public Pattern getPattern() {
            return pattern;
        }
    }
}
