package frontend.syntax.stmt.ast;

import driver.Config;
import frontend.syntax.NodeBase;
import frontend.syntax.expr.ast.BinaryExpNode;
import frontend.syntax.expr.ast.Calculatable;
import frontend.syntax.expr.ast.ExpContext;
import midend.ir.ModuleBuilder;
import midend.ir.value.BasicBlock;

public class ConditionNode extends NodeBase implements Stmt {
    private NodeBase cond;
    private NodeBase stmt;
    private NodeBase elseStmt;

    public ConditionNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(NodeBase cond, NodeBase stmt, NodeBase elseStmt) {
        this.cond = cond;
        this.stmt = stmt;
        this.elseStmt = elseStmt;
    }

    @Override
    public void checkErrors() {
        updateLastSymbolTable();
        ((Calculatable) cond).checkErrors();
        if (stmt != null) {
            // in case of :: if(1);
            ((Stmt) stmt).checkErrors();
        }
        if (elseStmt != null) {
            ((Stmt) elseStmt).checkErrors();
        }
    }

    @Override
    public void buildIR(ModuleBuilder builder) {
        ExpContext condAns = ((Calculatable) cond).getExpContext();
        boolean passThen = condAns.hasValue() && condAns.getValue() == 0;
        boolean passElse = condAns.hasValue() && condAns.getValue() != 0;
        if (Config.getInstance().hasOptimize(Config.Optimize.syntaxTreeExpressionOptimize) && (passElse || passThen)) {
            if (passElse && stmt != null) {
                stmt.buildIR(builder);
            } else if (passThen && elseStmt != null) {
                elseStmt.buildIR(builder);
            }
            return;
        }
        BasicBlock nxtBlock = builder.putBasicBlock("after_br");
        BasicBlock trueBlock = (stmt == null) ? nxtBlock :
                builder.putBasicBlock("then");
        BasicBlock falseBlock = (elseStmt == null) ? nxtBlock :
                builder.putBasicBlock("else");
        ((BinaryExpNode) cond).setCondBlock(trueBlock, falseBlock);
        cond.buildIR(builder);
        if (stmt != null) {
            builder.setCurBasicBlock(trueBlock);
            stmt.buildIR(builder);
            builder.putBrInstr(nxtBlock, builder.getCurBasicBlock());
        }
        if (elseStmt != null) {
            builder.setCurBasicBlock(falseBlock);
            elseStmt.buildIR(builder);
            builder.putBrInstr(nxtBlock, builder.getCurBasicBlock());
        }
        builder.setCurBasicBlock(nxtBlock);
    }
}
