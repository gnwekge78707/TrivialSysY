package frontend.syntax.expr.ast;

import driver.Config;
import frontend.syntax.NodeBase;
import frontend.syntax.ParserBase;
import frontend.syntax.TokenNode;
import frontend.tokenize.Token;
import midend.ir.ModuleBuilder;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.Constant;
import midend.ir.value.instr.Instruction;
import midend.ir.value.instr.binary.BinaryInstr;
import midend.ir.value.instr.terminator.CallInstr;

import java.util.ArrayList;

public class UnaryExpNode extends NodeBase implements Calculatable {
    private NodeBase unaryOp;
    private NodeBase unaryExp;
    // <UnaryExp>      := <PrimaryExp> | Ident '(' [ <FuncRParams> ] ')' | <UnaryOp> <UnaryExp>

    public UnaryExpNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(NodeBase unaryOp, NodeBase unaryExp) {
        this.unaryExp = unaryExp;
        this.unaryOp = unaryOp;
    }

    private ExpContext expContext;

    public ExpContext getExpContext() {
        return expContext;
    }

    public void checkErrors() {
        updateLastSymbolTable();
        if (!(unaryExp instanceof Calculatable)) {
            throw new Error("CHECKERROR -> UnaryExp children not calculatable");
        }
        ((Calculatable) unaryExp).checkErrors();
        calculate();
    }

    public void calculate() {
        Calculatable exp = (Calculatable) unaryExp;
        boolean isConst = exp.getExpContext().isConst();
        boolean hasVal = exp.getExpContext().hasValue();
        int val = 0;
        if (hasVal) {
            val = exp.getExpContext().getValue();
            if (((TokenNode) unaryOp).getTokenType() == Token.TokenType.MINU) {
                val = -val;
            } else if (((TokenNode) unaryOp).getTokenType() == Token.TokenType.NOT) {
                val = (val == 0)? 1 : 0;
            }
        }
        ArrayList<Integer> childDims = exp.getExpContext().getDimensions();
        expContext = new ExpContext(val, isConst, hasVal,
                childDims == null ? null : new ArrayList<>(childDims));
    }

    private Value dst; // Constant | Instr | NormalVar

    public Value getDst() {
        return dst;
    }

    @Override
    public void buildIR(ModuleBuilder builder) {
        if (Config.getInstance().hasOptimize(Config.Optimize.syntaxTreeExpressionOptimize) && expContext.hasValue()) {
            if (((TokenNode) unaryOp).getTokenType() == Token.TokenType.NOT) {
                dst = new Constant(LLVMType.Int.getI1(), expContext.getValue());
            } else {
                dst = new Constant(LLVMType.Int.getI32(), expContext.getValue());
            }
        } else {
            unaryExp.buildIR(builder);
            Token.TokenType op = ((TokenNode) unaryOp).getTokenType();
            if (op == Token.TokenType.NOT) {
                dst = new BinaryInstr(Instruction.InstrType.EQ,
                        ((Calculatable) unaryExp).getDst(), Constant.getConst0(), builder.getCurBasicBlock());
            } else if (op == Token.TokenType.MINU) {
                dst = new BinaryInstr(Instruction.InstrType.SUB,
                        Constant.getConst0(), ((Calculatable) unaryExp).getDst(), builder.getCurBasicBlock());
            } else {
                dst = ((Calculatable) unaryExp).getDst();
            }
        }
    }
}
