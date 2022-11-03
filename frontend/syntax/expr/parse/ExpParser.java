package frontend.syntax.expr.parse;

import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.tokenize.Tokenizer;

public class ExpParser extends ParserBase {
    NodeBase parent;

    // <Exp>           := <AddExp>
    public ExpParser(Tokenizer tokenizer, NodeBase parent) {
        super(tokenizer);
        this.node = (new AddExpParser(tokenizer, parent)).getNode();
        this.display();
    }
}