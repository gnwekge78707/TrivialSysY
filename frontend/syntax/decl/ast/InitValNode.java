package frontend.syntax.decl.ast;

import frontend.error.Error;
import frontend.symbol.LValSymbol;
import frontend.syntax.NodeBase;
import frontend.syntax.expr.ast.BinaryExpNode;
import midend.ir.ModuleBuilder;

import java.util.ArrayList;

public class InitValNode extends NodeBase {
    private ArrayList<NodeBase> initVals;

    public InitValNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(ArrayList<NodeBase> initVals) {
        this.initVals = initVals;
    }

    public void checkErrors(LValSymbol symbol, ArrayList<Integer> dims) {
        updateLastSymbolTable();
        int newDim = 0;
        for (NodeBase nodeBase : initVals) {
            ArrayList<Integer> curDims = new ArrayList<>(dims);
            curDims.add(newDim);
            if ((curDims.size() > symbol.getDimensions().size()) ||
                    (symbol.getDimensions().get(curDims.size() - 1) != null &&
                            newDim >= symbol.getDimensions().get(curDims.size() - 1))) {
                handleError(Error.ErrorType.UNDEFINED_ERROR);
                break;
            }
            if (nodeBase instanceof BinaryExpNode) {
                BinaryExpNode binaryExpNode = (BinaryExpNode) nodeBase;
                binaryExpNode.checkErrors();
                if ((symbol.isConst() && !binaryExpNode.getExpContext().isConst()) ||
                        (!binaryExpNode.getExpContext().hasValue()) ||
                        (curDims.size() != symbol.getDimensions().size())) {
                    handleError(Error.ErrorType.UNDEFINED_ERROR);
                }
                symbol.addInit(curDims, binaryExpNode);
            } else {
                InitValNode initValNode = (InitValNode) nodeBase;
                initValNode.checkErrors(symbol, curDims);
            }
            newDim += 1;
        }
    }

    @Override
    public void buildIR(ModuleBuilder builder) {
        for (NodeBase nodeBase : initVals) {
            if (nodeBase instanceof BinaryExpNode) {
                ((BinaryExpNode) nodeBase).buildIR(builder);
            } else {
                ((InitValNode) nodeBase).buildIR(builder);
            }
        }
    }

}
