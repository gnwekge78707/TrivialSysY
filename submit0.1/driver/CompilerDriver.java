package driver;

import frontend.Source;
import frontend.exceptions.FrontendBaseException;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.io.InputStream;
import java.util.ArrayList;

public class CompilerDriver {
    private ArrayList<Token> tokens;

    public void run() {
        InputStream inputStream = Config.getInstance().getSourceInputStream();
        Source source = new Source(inputStream);
        Tokenizer tokenizer = new Tokenizer(source);
        try {
            tokens = tokenizer.tokenize();
            if (Config.getInstance().hasOutputLevel(Config.OutputLevel.TOKENIZE)) {
                tokenizer.output(Config.getInstance().getOutputStream(Config.OutputLevel.TOKENIZE));
            }
        } catch (FrontendBaseException e) {
            e.printStackTrace();
            //
        }
    }
}
