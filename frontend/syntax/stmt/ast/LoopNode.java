package frontend.syntax.stmt.ast;

import driver.Config;
import frontend.syntax.NodeBase;
import frontend.syntax.expr.ast.BinaryExpNode;
import frontend.syntax.expr.ast.Calculatable;
import frontend.syntax.expr.ast.ExpContext;
import midend.ir.ModuleBuilder;
import midend.ir.value.BasicBlock;

public class LoopNode extends NodeBase implements Stmt {
    private NodeBase cond;
    private NodeBase stmt;

    public LoopNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(NodeBase cond, NodeBase stmt) {
        this.cond = cond;
        this.stmt = stmt;
    }

    @Override
    public void checkErrors() {
        updateLastSymbolTable();
        ((Calculatable) cond).checkErrors();
        if (stmt != null) {
            // incase of :: while(0);
            ((Stmt) stmt).checkErrors();
        }
    }

    @Override
    public void buildIR(ModuleBuilder builder) {
        // if stmt is null, should we execute condition?
        // fixme: true, because condition can be a function call. we also need to execute stmt (even is null)
        ExpContext condAns = ((Calculatable) cond).getExpContext();
        if (Config.getInstance().hasOptimize(Config.Optimize.syntaxTreeExpressionOptimize) &&
                condAns.hasValue() && condAns.getValue() == 0) {
            return;
        }
        BasicBlock condBB = builder.putBasicBlock("cond");
        BasicBlock bodyBB = builder.putBasicBlock("loop_body");
        BasicBlock nextBB = builder.putBasicBlock("after_loop");
        builder.putBrInstr(condBB, builder.getCurBasicBlock());
        ((BinaryExpNode) cond).setCondBlock(bodyBB, nextBB);
        builder.setCurBasicBlock(condBB);
        cond.buildIR(builder);
        builder.setCurBasicBlock(bodyBB);
        //push loop cond stack for break/continue
        if (stmt != null) {
            builder.getLoopCondBB().push(condBB);
            builder.getLoopNextBB().push(nextBB);
            stmt.buildIR(builder);
            builder.getLoopCondBB().pop();
            builder.getLoopNextBB().pop();
        }
        //pop
        builder.putBrInstr(condBB, builder.getCurBasicBlock());
        //fixme: curBB may change as builder encounter new bb in loop_body
        builder.setCurBasicBlock(nextBB);
    }
}
