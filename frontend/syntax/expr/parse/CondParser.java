package frontend.syntax.expr.parse;

import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.tokenize.Tokenizer;

public class CondParser extends ParserBase {

    //<Cond>          := <LOrExp>
    public CondParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        this.node = (new LOrExpParser(tokenizer, parent)).getNode();
        this.display();
    }
}
