package driver;

import frontend.Source;
import frontend.error.ErrorBuffer;
import frontend.exceptions.FrontendBaseException;
import frontend.syntax.CompUnitParser;
import frontend.syntax.RootNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.io.InputStream;
import java.util.ArrayList;

public class CompilerDriver {
    private ArrayList<Token> tokens;

    public void runFrontEnd() {
        InputStream inputStream = Config.getInstance().getSourceInputStream();
        Source source = new Source(inputStream);
        Tokenizer tokenizer = new Tokenizer(source);
        try {
            tokens = tokenizer.tokenize();
            if (Config.getInstance().hasOutputLevel(Config.OutputLevel.TOKENIZE)) {
                Output.getInstance().display(Config.OutputLevel.TOKENIZE);
                //tokenizer.output(Config.getInstance().getOutputStream(Config.OutputLevel.TOKENIZE));
            }
        } catch (FrontendBaseException e) {
            e.printStackTrace();
            //
        }
        CompUnitParser parser = new CompUnitParser(tokenizer);
        if (Config.getInstance().hasOutputLevel(Config.OutputLevel.SYNTAX)) {
            Output.getInstance().display(Config.OutputLevel.SYNTAX);
        }
        RootNode rootNode = (RootNode) parser.getNode();
        rootNode.checkErrors();
        ErrorBuffer.getInstance().display();
        if (Config.getInstance().hasOutputLevel(Config.OutputLevel.ERROR)) {
            Output.getInstance().display(Config.OutputLevel.ERROR);
        }

    }

    public void run() {
        runFrontEnd();
    }
}
