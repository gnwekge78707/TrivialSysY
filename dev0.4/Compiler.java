import driver.CompilerDriver;
import driver.Config;
import frontend.tokenize.Token;

public class Compiler {
    public void runCompiler() {

    }

    public static void main(String[] args) {
        Config.getInstance().parseArgs(args);
        CompilerDriver driver = new CompilerDriver();
        driver.run();
    }
}
