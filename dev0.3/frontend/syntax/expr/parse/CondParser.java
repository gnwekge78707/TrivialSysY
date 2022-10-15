package frontend.syntax.expr.parse;

import frontend.syntax.ParserBase;
import frontend.tokenize.Tokenizer;

public class CondParser extends ParserBase {

    //<Cond>          := <LOrExp>
    public CondParser(Tokenizer tokenizer) {
        super(tokenizer);
        new LOrExpParser(tokenizer);
        this.display();
    }
}
