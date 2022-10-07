package frontend.syntax.expr.parse;

import frontend.syntax.ParserBase;
import frontend.tokenize.Tokenizer;

public class ExpParser extends ParserBase {

    // <Exp>           := <AddExp>
    public ExpParser(Tokenizer tokenizer) {
        super(tokenizer);
        new AddExpParser(tokenizer);
        this.display();
    }
}