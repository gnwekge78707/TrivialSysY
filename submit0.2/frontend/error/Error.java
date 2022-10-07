package frontend.error;

import frontend.tokenize.Token;

public class Error implements Comparable<Error> {

    private final ErrorType errorType;
    private final int line;
    private final int col;

    public Error(ErrorType type, int line) {
        this.errorType = type;
        this.line = line;
        this.col = 0;
    }

    public Error(ErrorType type, int line, int col) {
        this.errorType = type;
        this.line = line;
        this.col = col;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getOutputString() {
        return line + " " + errorType.getId();
    }

    @Override
    public int compareTo(Error o) {
        if (line == o.getLine()) {
            return Integer.compare(col, o.getCol());
        }
        return Integer.compare(line, o.getLine());
    }

    public enum ErrorType {
        ILLEGAL_CHAR("a"),
        DUPLICATED_IDENT("b"),
        UNDEFINED_IDENT("c"),
        MISMATCHED_PARAM_NUM("d"),
        MISMATCHED_PARAM_TYPE("e"),
        ILLEGAL_RETURN("f"),
        MISSING_RETURN("g"),
        MODIFIED_CONST("h"),
        MISSING_SEMICOLON("i"),
        MISSING_RPARENT("j"),
        MISSING_RBRACK("k"),
        MISMATCHED_STRCON("l"),
        MISPLACED_LOOP_CONTROL("m"),
        UNDEFINED_ERROR("z")
        ;

        private final String id;

        ErrorType(String id) { this.id = id; }

        public String getId() { return id; }
    }
}
