package driver;

import backend.MipsBuilder;
import frontend.Source;
import frontend.error.ErrorBuffer;
import frontend.exceptions.FrontendBaseException;
import frontend.syntax.CompUnitParser;
import frontend.syntax.RootNode;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;
import midend.ir.Module;
import midend.ir.ModuleBuilder;
import midend.pass.*;

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
                Output.getInstance().display(Config.OutputLevel.TOKENIZE);
                //tokenizer.output(Config.getInstance().getOutputStream(Config.OutputLevel.TOKENIZE));
            }
        } catch (FrontendBaseException e) {
            e.printStackTrace();
            //
        }
        //todo-----------------------------------------------------------------------------PARSE
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
        System.out.println("frontend finish");
        //todo-----------------------------------------------------------------------------MID

        if (!ErrorBuffer.getInstance().getErrors().isEmpty()) {
            System.out.println("!testfile has error!");
            return;
        }
        ModuleBuilder moduleBuilder = new ModuleBuilder();
        rootNode.buildIR(moduleBuilder);
        //todo-----------------------------------------------------------------------------MID_OPT
        if (Config.getInstance().hasOptimize(Config.Optimize.llvmMem2Reg)) {
            optimize(moduleBuilder.getModule());
            moduleBuilder.getModule().nameVariable();
            new RemovePhi().run(moduleBuilder.getModule());
            moduleBuilder.getModule().dumpLLVM();
        } else {
            moduleBuilder.getModule().nameVariable();
            moduleBuilder.getModule().dumpLLVM();
        }

        if (Config.getInstance().hasOutputLevel(Config.OutputLevel.MIDCODE)) {
            Output.getInstance().display(Config.OutputLevel.MIDCODE);
        }

        if (Config.getInstance().hasOptimize(Config.Optimize.activeVariable)) {
            new LiveInOutAnalysis().run(moduleBuilder.getModule());
        }

        //todo-----------------------------------------------------------------------------BACK
        MipsBuilder mipsBuilder = new MipsBuilder(moduleBuilder.getModule());
        mipsBuilder.buildMipsAssembly();
        mipsBuilder.dumpMipsAssembly();
        if (Config.getInstance().hasOutputLevel(Config.OutputLevel.MIPS)) {
            Output.getInstance().display(Config.OutputLevel.MIPS);
        }

    }

    private void optimize(Module module) {
        new BBPredSuccAnalysis().run(module);
        new InterProcedureAnalysis().run(module);
        new DeadCodeElimination().run(module);
        new BranchOptimization().run(module);
        new DominanceAnalysis().run(module);
        new GVL().run(module);
        new Mem2Reg().run(module);

        new DeadCodeElimination().run(module);
        new BranchOptimization().run(module);
        new DominanceAnalysis().run(module);
        new LoopInfoAnalysis().run(module);
        new GCM().run(module);
        new DominanceAnalysis().run(module);
        new GVN().run(module);
        new LoopInfoAnalysis().run(module);
        new GCM().run(module);
        new BranchOptimization().run(module);
        new DeadCodeElimination().run(module);
        new BranchOptimization().run(module);
        //module.nameVariable();

        DeadCodeElimination dce = new DeadCodeElimination();
        BranchOptimization bo = new BranchOptimization();
        do {
            new InterProcedureAnalysis().run(module);
            dce.run(module);
            bo.run(module);
            new LoadStoreInBlock().run(module);
            new DominanceAnalysis().run(module);
            new GCM().run(module);
            new DominanceAnalysis().run(module);
            new GVN().run(module);
            new LoopInfoAnalysis().run(module);
            new GCM().run(module);
            new InstructionCombination().run(module);
            dce.run(module);
        } while(bo.run(module));

        boolean changed ;
        do {
            changed = false;
            new InterProcedureAnalysis().run( module);
            changed |= dce.run( module);
            new LoadStoreInBlock().run( module);
            new DominanceAnalysis().run( module);
            new GCM().run( module);
            new DominanceAnalysis().run( module);
            new GVN().run( module);
            new LoopInfoAnalysis().run( module);
            new GCM().run( module);
            new InstructionCombination().run( module);
            changed |= dce.run( module);
            changed |= bo.run( module);
        } while(changed);
    }

}
