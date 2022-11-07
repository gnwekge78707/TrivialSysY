package driver;

import java.io.*;
import java.util.*;

public class Config {
    private static final Config CONFIG = new Config();

    public static Config getInstance() { return CONFIG; }

    private boolean debugMode = true;
    private OutputLevel defaultOutputLevel = OutputLevel.SYNTAX;
    private ArrayList<OutputLevel> defaultOutputLevels = new ArrayList<>(
            Arrays.asList(OutputLevel.ERROR, OutputLevel.SYNTAX, OutputLevel.MIDCODE, OutputLevel.MIPS));

    private String sourceFileName = "testfile.txt";
    private HashMap<OutputLevel, String> outputLevel2TargetName = new HashMap<>();

    private InputStream sourceInputStream = null;
    private HashMap<OutputLevel, PrintStream> outputLevel2Target = new HashMap<>();

    private HashSet<Optimize> optimizeLevels = new HashSet<>();

    private Config() {
        outputLevel2TargetName.put(OutputLevel.TOKENIZE, "output.txt");
        outputLevel2TargetName.put(OutputLevel.SYNTAX, "output.txt");
        outputLevel2TargetName.put(OutputLevel.AST, "testfile_ast.txt");
        outputLevel2TargetName.put(OutputLevel.ERROR, "error.txt");
        outputLevel2TargetName.put(OutputLevel.MIDCODE, "llvm_ir.txt");
        outputLevel2TargetName.put(OutputLevel.MIPS, "mips.txt");
        for (OutputLevel level : defaultOutputLevels) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(outputLevel2TargetName.get(level));
                PrintStream printStream = new PrintStream(fileOutputStream);
                outputLevel2Target.put(level, printStream);
            } catch (FileNotFoundException e) {
                System.err.println("InitConfigErr : target file open failed");
            }
        }
        setOptimizeLevel(0);
    }

    public boolean isDebugMode() { return debugMode; }

    public InputStream getSourceInputStream() { return sourceInputStream; }

    public boolean hasOutputLevel(OutputLevel level) { return outputLevel2Target.containsKey(level); }

    public PrintStream getOutputStream(OutputLevel level) {
        if (hasOutputLevel(level)) {
            return outputLevel2Target.get(level);
        } else {
            return null;
        }
    }

    public boolean parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-i")) {
                // parsing source file
                if (Objects.nonNull(this.sourceInputStream)) {
                    System.err.println("ParseArgsErr : duplicated source file");
                    return false;
                } else if (i >= args.length - 1) {
                    System.err.println("ParseArgsErr : source file not found");
                    return false;
                }
                i++;
                try {
                    this.sourceInputStream = new FileInputStream(args[i]);
                    this.sourceFileName = args[i];
                } catch (FileNotFoundException e) {
                    System.err.println("ParseArgsErr : source file open failed");
                    return false;
                }
            } else if (args[i].equals("-d") || args[i].equals("--debug")) {
                this.debugMode = true;
            } else {
                // parsing output file
                boolean isLegitArg = false;
                for (OutputLevel outputLevel : OutputLevel.values()) {
                    if (args[i].equals(outputLevel.getArg())) {
                        isLegitArg = true;
                        if (!outputLevel2Target.containsKey(outputLevel)) {
                            outputLevel2Target.put(outputLevel, null);
                        }
                        if (i >= args.length - 1) {
                            System.err.println("ParseArgsErr : target output file not found");
                            return false;
                        }
                        i++;
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(args[i]);
                            PrintStream printStream = new PrintStream(fileOutputStream);
                            outputLevel2Target.put(outputLevel, printStream);
                        } catch (FileNotFoundException e) {
                            System.err.println("ParseArgsErr : output file open failed");
                            return false;
                        }
                    }
                }
            }
        }
        if (this.sourceInputStream == null) {
            try {
                this.sourceInputStream = new FileInputStream(this.sourceFileName);
            } catch (FileNotFoundException e) {
                System.err.println("ParseArgsErr : source file open failed");
                return false;
            }
        }
        return true;
    }

    public enum OutputLevel {
        TOKENIZE("-T"),
        SYNTAX("-S"),
        AST("-A"),
        ERROR("-E"),
        MIDCODE("-M"),
        MIPS("-O")
        ;

        private final String arg;

        OutputLevel(String arg) { this.arg = arg; }

        public String getArg() { return arg; }
    }

    public enum Optimize {
        syntaxTreeExpressionOptimize,
        llvmMem2Reg,
        lruLocalRegAlloc
    }
    //syntaxTreeExpressionOptimize is a must have

    private void setOptimizeLevel(int o) {
        this.optimizeLevels.add(Optimize.syntaxTreeExpressionOptimize);
        this.optimizeLevels.add(Optimize.lruLocalRegAlloc);
    }

    public boolean hasOptimize(Optimize optimize) {
        return this.optimizeLevels.contains(optimize);
    }
}
