package frontend.syntax.decl.ast;

import frontend.error.Error;
import frontend.symbol.FuncSymbol;
import frontend.symbol.LValSymbol;
import frontend.syntax.NodeBase;
import frontend.syntax.expr.ast.BinaryExpNode;
import frontend.tokenize.Token;

import java.util.ArrayList;

public class VarDefNode extends NodeBase {
    private Token ident;
    private ArrayList<NodeBase> constExps;
    private NodeBase initVal;

    public VarDefNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(Token ident, ArrayList<NodeBase> constExps, NodeBase initVal) {
        this.ident = ident;
        this.constExps = constExps;
        this.initVal = initVal;
    }

    public void checkErrors(LValSymbol symbol) {
        updateLastSymbolTable();
        symbol.setIdent(ident);
        for (NodeBase nodeBase : constExps) {
            BinaryExpNode binaryExpNode = (BinaryExpNode) nodeBase;
            binaryExpNode.checkErrors();
            if (!binaryExpNode.getExpContext().isConst()) {
                handleError(Error.ErrorType.UNDEFINED_ERROR);
            }
            symbol.addDimension(binaryExpNode.getExpContext().getValue());
        }
        if (initVal != null) {
            if (initVal instanceof InitValNode) {
                ((InitValNode) initVal).checkErrors(symbol, new ArrayList<>());
            } else if (initVal instanceof BinaryExpNode) {
                ((BinaryExpNode) initVal).checkErrors();
                symbol.addInit(new ArrayList<>(), ((BinaryExpNode) initVal));
            }
        }
        if (getLastSymbolTable().getSymbol(ident.toString(), true) == null) {
            getLastSymbolTable().addSymbol(symbol);
        } else {
            handleError(Error.ErrorType.DUPLICATED_IDENT);
        }
    }
}
