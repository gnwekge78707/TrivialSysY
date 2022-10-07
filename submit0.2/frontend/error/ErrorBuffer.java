package frontend.error;

import driver.Config;
import driver.Output;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class ErrorBuffer {
    private static final ErrorBuffer ERROR_BUFFER = new ErrorBuffer();

    public static ErrorBuffer getInstance() { return ERROR_BUFFER; }

    private LinkedList<Error> errors = new LinkedList<>();

    public void addError(Error error) {
        errors.add(error);
    }

    public void display() {
        if (errors.isEmpty()) {
            return;
        }
        Collections.sort(errors);
        errors.forEach(u -> Output.getInstance().updateBuffer(Config.OutputLevel.ERROR, u.getOutputString()));
    }

}
