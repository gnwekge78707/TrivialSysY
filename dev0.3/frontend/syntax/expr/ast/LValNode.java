package frontend.syntax.expr.ast;

import frontend.error.Error;
import frontend.symbol.LValSymbol;
import frontend.symbol.Symbol;
import frontend.syntax.NodeBase;
import frontend.tokenize.Token;

import java.util.ArrayList;

// <LVal>          := Ident { '[' <Exp> ']' }
public class LValNode extends NodeBase implements Calculatable {
    private Token ident;
    private ArrayList<NodeBase> exps;

    public LValNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(Token ident, ArrayList<NodeBase> exps) {
        this.ident = ident;
        this.exps = exps;
    }

    private ExpContext expContext;

    public ExpContext getExpContext() {
        return expContext;
    }

    public void checkErrors() {
        updateLastSymbolTable();
        //todo :: can only calculate value for const, incase of control flow

        LValSymbol symbol = getLastSymbolTable().getLValSymbol(ident.toString(), false);
        if (symbol == null) {
            handleError(Error.ErrorType.UNDEFINED_IDENT);
            this.expContext = new ExpContext(0, false, false, new ArrayList<>());
        }
        for (NodeBase nodeBase : exps) {
            Calculatable exp = (Calculatable) nodeBase;
            exp.checkErrors();
        }
        if (symbol != null) calculate(symbol);
    }

    // TODO -> CAUTION !! better made it !hasValue than hasValue
    // TODO slicing:: DECL: a[2][2] -> REF: a[0][1], a[0]
    // TODO dimension dont hasValue:: still needs to proceed
    public void calculate(LValSymbol symbol) {
        if (exps.size() == symbol.getDimensions().size()) {
            ArrayList<Integer> dims = new ArrayList<>();
            for (NodeBase nodeBase : exps) {
                Calculatable exp = (Calculatable) nodeBase;
                if (!exp.getExpContext().hasValue()) {
                    this.expContext = new ExpContext(0, symbol.isConst(), false, new ArrayList<>());
                    return;
                }
                if (exp.getExpContext().getValue() >= symbol.getDimensions().get(dims.size())) {
                    handleError(Error.ErrorType.UNDEFINED_ERROR);
                }
                dims.add((exp.getExpContext().getValue()));
            }
            if (!symbol.isConst()) {
                this.expContext = new ExpContext(0, false, false, new ArrayList<>());
                return;
            }
            BinaryExpNode initVal = symbol.getInit(dims);
            if (initVal == null) {
                this.expContext = new ExpContext(0, true, false, new ArrayList<>());
            } else {
                this.expContext = new ExpContext(initVal.getExpContext().getValue(),
                        initVal.getExpContext().isConst(),
                        initVal.getExpContext().hasValue(),
                        new ArrayList<>());
            }
        } else if (exps.size() < symbol.getDimensions().size()) {
            ArrayList<Integer> newDims = new ArrayList<>(
                    symbol.getDimensions().subList(exps.size(), symbol.getDimensions().size())
            );
            this.expContext = new ExpContext(0, symbol.isConst(), false, newDims);
        } else { //TODO -> ERROR!
            handleError(Error.ErrorType.UNDEFINED_ERROR);
            this.expContext = new ExpContext(0, symbol.isConst(), false, new ArrayList<>());
        }
    }
}
