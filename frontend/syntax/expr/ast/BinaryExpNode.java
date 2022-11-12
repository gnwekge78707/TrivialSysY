package frontend.syntax.expr.ast;

import driver.Config;
import frontend.syntax.NodeBase;
import frontend.syntax.stmt.ast.Stmt;
import frontend.tokenize.Token;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.Constant;
import midend.ir.ModuleBuilder;
import midend.ir.value.instr.Instruction;
import midend.ir.value.instr.binary.BinaryInstr;
import midend.ir.value.instr.mem.ZextInstr;
import midend.ir.value.instr.terminator.BrInstr;

import java.util.ArrayList;

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

    //=============================================BUILD_IR=================================================
    //fixme: do not have dst value for shortcut LOR

    private Value dst; // Constant | Instr | NormalVar

    public Value getDst() {
        return dst;
    }

    private BasicBlock trueBlock, falseBlock; //for LAnd and LOr

    public void setCondBlock(BasicBlock trueBlock, BasicBlock falseBlock) {
        this.trueBlock = trueBlock;
        this.falseBlock = falseBlock;
    }

    @Override
    public void buildIR(ModuleBuilder builder) {
        if (expContext.hasValue() &&
                Config.getInstance().hasOptimize(Config.Optimize.syntaxTreeExpressionOptimize)) {
            // TODO: consider optimize for :: (i + 1 + var + 1) -> (3 + var)
            if (getSyntaxType().equals(SyntaxType.RELEXP) ||
                    getSyntaxType().equals(SyntaxType.EQEXP) ||
                    getSyntaxType().equals(SyntaxType.LANDEXP) ||
                    getSyntaxType().equals(SyntaxType.LOREXP)) {
                if (expContext.getValue() != 0 && expContext.getValue() != 1) {
                    dst = new Constant(LLVMType.Int.getI32(), expContext.getValue());
                    /*if (getSyntaxType().equals(SyntaxType.RELEXP)) {
                        dst = new Constant(LLVMType.Int.getI32(), expContext.getValue());
                    } else {
                        throw new Error("condExp has value other than 0,1 ---" + expContext.getValue());
                    }*/
                } else {
                    dst = new Constant(LLVMType.Int.getI1(), expContext.getValue());
                }
                //TODO: next, will cope with if(114); if(i || 114)
                if (getSyntaxType().equals(SyntaxType.LOREXP) ||
                        getSyntaxType().equals(SyntaxType.LANDEXP)) {
                    new BrInstr(expContext.getValue() != 0 ? trueBlock : falseBlock, builder.getCurBasicBlock());
                    builder.setCurBasicBlock(expContext.getValue() != 0 ? trueBlock : falseBlock);
                }
            } else {
                dst = new Constant(LLVMType.Int.getI32(), expContext.getValue());
            }
        } else if (getSyntaxType() != SyntaxType.LANDEXP && getSyntaxType() != SyntaxType.LOREXP) {
            // TODO: (+,-)(*,/,%)(<,<=,>,>=)(==,!=)  (&&)(||)
            // TODO: mind, can only zest i1 to i32
            // TODO: shortCut, (don't consider -> a = (a||b) + 1;)
            firstExp.buildIR(builder);
            dst = ((Calculatable) firstExp).getDst();
            for (int i = 0; i < exps.size(); i++) {
                exps.get(i).buildIR(builder);
                dst = processBinaryInstr(ops.get(i), dst, ((Calculatable) exps.get(i)).getDst(), builder);
            }
        } else {
            if (getSyntaxType() == SyntaxType.LANDEXP) buildLAndIR(builder);
            if (getSyntaxType() == SyntaxType.LOREXP) buildLOrIR(builder);
        }
    }

    private Value processBinaryInstr(Token op, Value src1, Value src2, ModuleBuilder builder) {
        //在乘元素（sonNode 不仅为 binary），操作数为常量数组的引用非常数类型 cause -> mulExp 直接连着 LVal 没有拆类型的包
        if (!(src1.getType() instanceof LLVMType.Int && src2.getType() instanceof LLVMType.Int)) {
            throw new Error("BUILD_IR_ERROR: binary operation cannot be enforced on none-integer operands");
        }
        if (src1.getType() == LLVMType.Int.getI1()) {
            src1 = new ZextInstr(src1, LLVMType.Int.getI32(), builder.getCurBasicBlock());
        }
        if (src2.getType() == LLVMType.Int.getI1()){
            src2 = new ZextInstr(src2, LLVMType.Int.getI32(), builder.getCurBasicBlock());
        }
        Value res;
        switch (op.getTokenType()) {
            case PLUS:
                res = new BinaryInstr(Instruction.InstrType.ADD, src1, src2, builder.getCurBasicBlock());
                break;
            case MINU:
                res = new BinaryInstr(Instruction.InstrType.SUB, src1, src2, builder.getCurBasicBlock());
                break;
            case MULT:
                res = new BinaryInstr(Instruction.InstrType.MUL, src1, src2, builder.getCurBasicBlock());
                break;
            case DIV:
                res = new BinaryInstr(Instruction.InstrType.SDIV, src1, src2, builder.getCurBasicBlock());
                break;
            case MOD:
                res = new BinaryInstr(Instruction.InstrType.SREM, src1, src2, builder.getCurBasicBlock());
                break;
            case LEQ:
                res = new BinaryInstr(Instruction.InstrType.SLE, src1, src2, builder.getCurBasicBlock());
                break;
            case LSS:
                res = new BinaryInstr(Instruction.InstrType.SLT, src1, src2, builder.getCurBasicBlock());
                break;
            case GEQ:
                res = new BinaryInstr(Instruction.InstrType.SGE, src1, src2, builder.getCurBasicBlock());
                break;
            case GRE:
                res = new BinaryInstr(Instruction.InstrType.SGT, src1, src2, builder.getCurBasicBlock());
                break;
            case EQL:
                res = new BinaryInstr(Instruction.InstrType.EQ, src1, src2, builder.getCurBasicBlock());
                break;
            case NEQ:
                res = new BinaryInstr(Instruction.InstrType.NE, src1, src2, builder.getCurBasicBlock());
                break;
            default:
                res = src1;
        }
        return res;
    }

    public void buildLAndIR(ModuleBuilder builder) { //AND
        ArrayList<NodeBase> children = new ArrayList<>(exps);
        children.add(0, firstExp);
        for (int i = 0; i < children.size(); i++) {
            children.get(i).buildIR(builder);
            dst = ((Calculatable) children.get(i)).getDst();
            BasicBlock nextBB = (i + 1 == children.size()) ?
                    trueBlock : builder.putBasicBlock("lAnd_nxt");
            // TODO! maybe can have less icmp?
            dst = new BinaryInstr(Instruction.InstrType.NE, dst, Constant.getConst0(), builder.getCurBasicBlock());
            builder.putBrInstr(dst, nextBB, this.falseBlock, builder.getCurBasicBlock());
            builder.setCurBasicBlock(nextBB);
        }
    }

    public void buildLOrIR(ModuleBuilder builder) { //OR
        ArrayList<NodeBase> landExps = new ArrayList<>(exps);
        landExps.add(0, firstExp);
        for (int i = 0; i < landExps.size() - 1; i++) {
            BasicBlock nextBB = builder.putBasicBlock("lOr_nxt");
            ((BinaryExpNode) landExps.get(i)).setCondBlock(this.trueBlock, nextBB);
            landExps.get(i).buildIR(builder);
            builder.setCurBasicBlock(nextBB);
        }
        BinaryExpNode lastLandExp = (BinaryExpNode) landExps.get(landExps.size() - 1);
        lastLandExp.setCondBlock(this.trueBlock, this.falseBlock);
        lastLandExp.buildIR(builder);
    }
}
