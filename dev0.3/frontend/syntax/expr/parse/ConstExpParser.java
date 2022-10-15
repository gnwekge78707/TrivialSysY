package frontend.syntax.expr.parse;

import frontend.syntax.ParserBase;
import frontend.tokenize.Tokenizer;

public class ConstExpParser extends ParserBase {

    // <ConstExp>      := <AddExp>
    public ConstExpParser(Tokenizer tokenizer) {
        super(tokenizer);
        new AddExpParser(tokenizer);
        display();
    }
}
