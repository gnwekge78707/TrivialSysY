package driver;

import backend.MipsBuilder;
import frontend.Source;
import frontend.error.ErrorBuffer;
import frontend.exceptions.FrontendBaseException;
import frontend.syntax.CompUnitParser;
import frontend.syntax.RootNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;
import midend.ir.ModuleBuilder;

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


        if (!ErrorBuffer.getInstance().getErrors().isEmpty()) {
            System.out.println("!testfile has error!");
            return;
        }
        ModuleBuilder moduleBuilder = new ModuleBuilder();
        rootNode.buildIR(moduleBuilder);
        moduleBuilder.getModule().dumpLLVM();
        if (Config.getInstance().hasOutputLevel(Config.OutputLevel.MIDCODE)) {
            Output.getInstance().display(Config.OutputLevel.MIDCODE);
        }


        MipsBuilder mipsBuilder = new MipsBuilder(moduleBuilder.getModule());
        mipsBuilder.buildMipsAssembly();
        mipsBuilder.dumpMipsAssembly();
        if (Config.getInstance().hasOutputLevel(Config.OutputLevel.MIPS)) {
            Output.getInstance().display(Config.OutputLevel.MIPS);
        }
    }


    public void run() {
        runFrontEnd();
    }
}
