package frontend.exceptions;

public class FrontendBaseException extends Exception{
    private int line;
    private int index;
    private String source;

    public FrontendBaseException(int line, int index, String source, String excType) {
        super(String.format("%s at line %d, col %d : %s", excType, line, index, source));
    }

    public FrontendBaseException(String excType) {
        super(excType);
    }
}
