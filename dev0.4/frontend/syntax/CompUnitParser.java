package frontend.syntax;

import frontend.syntax.decl.parse.ConstDeclParser;
import frontend.syntax.decl.parse.VarDeclParser;
import frontend.syntax.func.parse.FuncDefParser;
import frontend.syntax.func.parse.MainFuncDefParser;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

import java.util.ArrayList;

public class CompUnitParser extends ParserBase {

    // CompUnit := {ConstDecl | VarDecl} {FuncDef} MainFuncDef
    public CompUnitParser(Tokenizer tokenizer) {
        super(tokenizer);
        this.node = new RootNode(null, NodeBase.SyntaxType.COMPUNIT); //todo
        ArrayList<NodeBase> varDecls = new ArrayList<>(); //todo
        while (true) {
            updateToken();
            if (lookForward(Token.TokenType.CONSTTK)) {
                reverse();
                varDecls.add((new ConstDeclParser(tokenizer, this.node)).getNode());
                continue;
            } else if (lookForward(Token.TokenType.INTTK)) {
                updateToken();
                if (lookForward(Token.TokenType.IDENFR)) {
                    updateToken();
                    if (!lookForward(Token.TokenType.LPARENT)) {
                        reverse();
                        varDecls.add((new VarDeclParser(tokenizer, this.node)).getNode());
                        continue;
                    }
                }
            }
            reverse();
            break;
        }
        ArrayList<NodeBase> funcDefs = new ArrayList<>(); //todo
        while (true) {
            updateToken();
            if (lookForward(Token.TokenType.VOIDTK)) {
                reverse();
                funcDefs.add((new FuncDefParser(tokenizer, this.node)).getNode());
                continue;
            } else if (lookForward(Token.TokenType.INTTK)) {
                updateToken();
                if (!lookForward(Token.TokenType.MAINTK)) {
                    reverse();
                    funcDefs.add((new FuncDefParser(tokenizer, this.node)).getNode());
                    continue;
                }
            }
            reverse();
            break;
        }
        NodeBase mainFuncDef = (new MainFuncDefParser(tokenizer, this.node)).getNode();
        ((RootNode) node).register(varDecls, funcDefs, mainFuncDef); //todo
        display();
    }
}
