package frontend.syntax.expr.ast;

import frontend.error.Error;
import frontend.symbol.FuncSymbol;
import frontend.symbol.LValSymbol;
import frontend.syntax.NodeBase;
import midend.ir.ModuleBuilder;
import midend.ir.Value;

import java.util.ArrayList;

// <FuncRParams>   := <Exp> { ',' <Exp> }
public class FuncRParamsNode extends NodeBase {
    private ArrayList<NodeBase> exps;

    public FuncRParamsNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(ArrayList<NodeBase> exps) {
        this.exps = exps;
    }

    public void checkErrors(FuncSymbol symbol) {
        updateLastSymbolTable();
        if (symbol == null) {
            exps.forEach(i -> ((Calculatable) i).checkErrors());
            return;
        }
        ArrayList<LValSymbol> funcParams = symbol.getParams();
        if (funcParams.size() != exps.size()) {
            for (NodeBase nodeBase : exps) {
                ((Calculatable) nodeBase).checkErrors();
            }
            handleError(Error.ErrorType.MISMATCHED_PARAM_NUM);
            return;
        }
        for (int i = 0; i < exps.size(); i++) {
            Calculatable exp = (Calculatable) exps.get(i);
            exp.checkErrors();
            if (exp.getExpContext().getDimensions() == null) {
                handleError(Error.ErrorType.MISMATCHED_PARAM_TYPE);
                continue;
            }
            ArrayList<Integer> newDims = exp.getExpContext().getDimensions();
            ArrayList<Integer> oldDims = funcParams.get(i).getDimensions();
            if (newDims.size() != oldDims.size()) {
                handleError(Error.ErrorType.MISMATCHED_PARAM_TYPE);
                continue;
            }
            //FIXME! call function -> void foo(int a[][10]),
            // first implicit dim do not need to make clear %% wont affect indexing
            for (int j = 1; j < newDims.size(); j++) {
                if (newDims.get(j) != oldDims.get(j)) {
                    handleError(Error.ErrorType.MISMATCHED_PARAM_TYPE);
                    break;
                }
            }
        }
    }

    public ArrayList<NodeBase> getExps() {
        return exps;
    }

    @Override
    public void buildIR(ModuleBuilder builder) {
        for (NodeBase nodeBase : exps) {
            ((BinaryExpNode) nodeBase).buildIR(builder);
        }
    }
}