package frontend.exceptions;

public class TokenTypeNotFoundException extends FrontendBaseException{

    public TokenTypeNotFoundException(int line, int index, String source) {
        super(line, index, source, "Not found token type exception");
    }
}
