package frontend.syntax.expr.ast;

import frontend.syntax.NodeBase;
import frontend.syntax.TokenNode;
import frontend.syntax.stmt.ast.Stmt;
import frontend.tokenize.Token;

import javax.management.remote.JMXServerErrorException;
import java.util.ArrayList;
import java.util.Arrays;

// BinaryExp: Arithmetic, Relation, Logical
// BinaryExp -> Exp { Op Exp }, calc from left to right
public class BinaryExpNode extends NodeBase implements Calculatable, Stmt {
    private NodeBase firstExp;
    private ArrayList<Token> ops;
    private ArrayList<NodeBase> exps;

    public BinaryExpNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(NodeBase firstExp, ArrayList<Token> ops, ArrayList<NodeBase> exps) {
        this.firstExp = firstExp;
        this.ops = ops;
        this.exps = exps;
    }

    private ExpContext expContext;

    public ExpContext getExpContext() {
        return expContext;
    }

    public void checkErrors() {
        updateLastSymbolTable();
        if (!(firstExp instanceof Calculatable)) {
            throw new Error("CHECKERROR -> BinaryExp children not calculatable ::" + (firstExp==null));
        }
        ((Calculatable) firstExp).checkErrors();
        for (NodeBase nodeBase : exps) {
            if (!(firstExp instanceof Calculatable)) {
                throw new Error("CHECKERROR -> BinaryExp children not calculatable");
            }
            ((Calculatable) nodeBase).checkErrors();
        }
        calculate();
    }

    public void calculate() {
        int value = 0;
        boolean isConst = true, hasValue = true;
        if (firstExp instanceof Calculatable) {
            isConst = ((Calculatable) firstExp).getExpContext().isConst();
            hasValue = ((Calculatable) firstExp).getExpContext().hasValue();
            if (hasValue) {
                value = ((Calculatable) firstExp).getExpContext().getValue();
            }
        }
        for (int i = 0; i < exps.size(); i++) {
            Calculatable exp = (Calculatable) (exps.get(i));
            isConst &= exp.getExpContext().isConst();
            hasValue &= exp.getExpContext().hasValue();
            if (hasValue) {
                switch (ops.get(i).getTokenType()) {
                    case PLUS:
                        value = value + exp.getExpContext().getValue();
                        break;
                    case MINU:
                        value = value - exp.getExpContext().getValue();
                        break;
                    case MULT:
                        value = value * exp.getExpContext().getValue();
                        break;
                    case DIV:
                        value = value / exp.getExpContext().getValue();
                        break;
                    case MOD:
                        value = value % exp.getExpContext().getValue();
                        break;
                    case LSS:
                        value = (value < exp.getExpContext().getValue())? 1 : 0;
                        break;
                    case LEQ:
                        value = (value <= exp.getExpContext().getValue())? 1 : 0;
                        break;
                    case GRE:
                        value = (value > exp.getExpContext().getValue())? 1 : 0;
                        break;
                    case GEQ:
                        value = (value >= exp.getExpContext().getValue())? 1 : 0;
                        break;
                    case AND:
                        value = ((value > 0) && (exp.getExpContext().getValue() > 0))? 1 : 0;
                        break;
                    case OR:
                        value = ((value > 0) || (exp.getExpContext().getValue() > 0))? 1 : 0;
                        break;
                    case EQL:
                        value = (value == exp.getExpContext().getValue())? 1 : 0;
                        break;
                    case NEQ:
                        value = (value != exp.getExpContext().getValue())? 1 : 0;
                        break;
                    default:
                }
            } else {
                value = 0;
            }
        }
        //fixme: void+void? void+char? int+char?
        ArrayList<Integer> childDims = ((Calculatable) firstExp).getExpContext().getDimensions();
        ArrayList<Integer> dimension = exps.size() > 0 ?
                new ArrayList<>() :
                childDims == null ?
                null : new ArrayList<>(childDims);
        expContext = new ExpContext(value, isConst, hasValue, dimension);
    }
}
