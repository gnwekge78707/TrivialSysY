package frontend.syntax.decl.ast;

import frontend.symbol.LValSymbol;
import frontend.symbol.SymbolTable;
import frontend.syntax.NodeBase;
import frontend.syntax.TokenNode;
import midend.ir.ModuleBuilder;

import java.util.ArrayList;

/*
<VarDecl>       := <BType> <VarDef> { ',' <VarDef> } ';'
------------------>
-----------------{|
 */
public class VarDeclNode extends NodeBase {
    private NodeBase btype;
    private ArrayList<NodeBase> varDefs;

    public VarDeclNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(NodeBase btype, ArrayList<NodeBase> varDefs) {
        this.btype = btype;
        this.varDefs = varDefs;
    }

    public void checkErrors() {
        updateLastSymbolTable();
        for (NodeBase nodeBase : varDefs) {
            LValSymbol symbol = new LValSymbol(
                    this.getSyntaxType().equals(SyntaxType.CONSTDECL),
                    ((TokenNode) btype).getTokenType());
            ((VarDefNode) nodeBase).checkErrors(symbol);
        }
    }

    @Override
    public void buildIR(ModuleBuilder builder) {
        for (NodeBase nodeBase : varDefs) {
            if (this.getSyntaxType() == SyntaxType.VARDECL) {
                nodeBase.buildIR(builder);
            }
        }
    }
}
