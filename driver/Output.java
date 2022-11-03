package driver;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;

public class Output {
    private static final Output OUTPUT = new Output();

    public static Output getInstance() { return OUTPUT; }

    private HashMap<Config.OutputLevel, LinkedList<String>> outputLevel2Buffer;
    private final Config.OutputLevel stdOutputLevel = Config.OutputLevel.MIDCODE;

    private Output() {
        this.outputLevel2Buffer = new HashMap<>();
        for (Config.OutputLevel outputLevel : Config.OutputLevel.values()) {
            outputLevel2Buffer.put(outputLevel, new LinkedList<>());
        }
    }

    public void updateBuffer(Config.OutputLevel outputLevel, String str) {
        if (outputLevel2Buffer.containsKey(outputLevel)) {
            if (Config.getInstance().isDebugMode()
                    && Config.getInstance().hasOutputLevel(outputLevel)
                    && outputLevel.equals(stdOutputLevel)) {
                System.out.println(str);
            }
            outputLevel2Buffer.get(outputLevel).add(str);
        }
    }

    public LinkedList<String> getBuffer(Config.OutputLevel outputLevel) {
        return new LinkedList<>(outputLevel2Buffer.get(outputLevel));
    }

    public void replaceBuffer(Config.OutputLevel outputLevel, LinkedList<String> linkedList) {
        outputLevel2Buffer.put(outputLevel, linkedList);
    }

    public void display(Config.OutputLevel outputLevel) {
        PrintStream printStream = Config.getInstance().getOutputStream(outputLevel);
        outputLevel2Buffer.get(outputLevel).forEach(u -> {
            printStream.println(u);
        });
    }

}
