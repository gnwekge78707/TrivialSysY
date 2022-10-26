package frontend.syntax.expr.parse;

import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.tokenize.Tokenizer;

public class ConstExpParser extends ParserBase {

    // <ConstExp>      := <AddExp>
    public ConstExpParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        this.node = (new AddExpParser(tokenizer, parent)).getNode();
        display();
    }
}
