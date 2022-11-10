package frontend.syntax.stmt.ast;

import frontend.error.Error;
import frontend.symbol.LValSymbol;
import frontend.symbol.SymbolTable;
import frontend.syntax.NodeBase;
import frontend.syntax.RootNode;
import frontend.syntax.Scope;
import frontend.syntax.decl.ast.VarDeclNode;
import frontend.syntax.expr.ast.Calculatable;
import frontend.syntax.func.ast.FuncDefNode;
import frontend.tokenize.Token;
import midend.ir.ModuleBuilder;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.instr.mem.AllocaInstr;
import midend.ir.value.instr.mem.StoreInstr;

import java.util.ArrayList;

public class BlockNode extends NodeBase implements Scope, Stmt {
    private ArrayList<NodeBase> blockItems;

    public BlockNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(ArrayList<NodeBase> blockItems) {
        this.blockItems = blockItems;
    }

    private SymbolTable symbolTable;

    @Override
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    //TODO: in block, **dont** use getLastSymbolTable to query symbol in this scope
    public void checkErrors(SymbolTable symbolTable) {
        updateLastSymbolTable();
        this.symbolTable = symbolTable;
        for (NodeBase nodeBase : blockItems) {
            if (nodeBase instanceof Stmt) {
                ((Stmt) nodeBase).checkErrors();
            } else if (nodeBase instanceof VarDeclNode) {
                ((VarDeclNode) nodeBase).checkErrors();
            } else if (nodeBase instanceof Calculatable) {
                ((Calculatable) nodeBase).checkErrors();
            } else if (nodeBase != null){
                // in case of a single ';' per blockItem
                throw new java.lang.Error("in checkError : Block -> has other type of children: "+(nodeBase == null));
            }
        }
        if (this.getParent() instanceof FuncDefNode || this.getParent() instanceof RootNode) {
            if (this.symbolTable.getCurrentFunction().getDeclType() != Token.TokenType.VOIDTK) {
                if (this.blockItems.size() == 0 || !(this.blockItems.get(blockItems.size() - 1) instanceof ReturnNode)) {
                    handleError(Error.ErrorType.MISSING_RETURN);
                }
            }
        }
    }

    @Override
    public void checkErrors() {
        //TODO: this is call when block is defined inside function
        updateLastSymbolTable();
        checkErrors(new SymbolTable(getLastSymbolTable(), getLastSymbolTable().getCurrentFunction()));
    }

    @Override
    public void buildIR(ModuleBuilder builder) {
        if (this.getParent() instanceof FuncDefNode) {
            ArrayList<LValSymbol> paramSymbols = symbolTable.getCurrentFunction().getParams();
            for (int i = 0; i < paramSymbols.size(); i++) {
                //TODO!! 对于函数参数取数组，再在函数开始时分配变量会造成二阶指针
                // / 但是不分配又会造成 符号表里 变量没有对应 pointer的问题
                // / 可以新设置一个 Value类： Params，类似 globalVar管理
                // / by the way, for local array, what is the type? nothing wrong with array pointer
                LValSymbol lValSymbol = paramSymbols.get(i);
                Value param = builder.getCurFunction().getParams().get(i);
                Value paramPointer;
                if (param.getType() instanceof LLVMType.Int) {
                    paramPointer = new AllocaInstr(lValSymbol.getLLVMType(), builder.getCurBasicBlock());
                    new StoreInstr(paramPointer, param, builder.getCurBasicBlock());
                } else if (param.getType() instanceof LLVMType.Pointer) {
                    paramPointer = param;
                } else {
                    throw new java.lang.Error("unsupported function param type");
                }
                if (symbolTable.getLValSymbol(lValSymbol.getName(), true) == null) {
                    throw new java.lang.Error("func param not found in func scope");
                }
                symbolTable.getLValSymbol(lValSymbol.getName(), true).setPointer(paramPointer);
            }
        }
        for (NodeBase nodeBase : blockItems) {
            if (nodeBase != null){
                // in case of a single ';' per blockItem
                nodeBase.buildIR(builder);
            }
        }
    }
}
