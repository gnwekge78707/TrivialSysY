package frontend.syntax;

import frontend.syntax.decl.parse.ConstDeclParser;
import frontend.syntax.decl.parse.VarDeclParser;
import frontend.syntax.func.parse.FuncDefParser;
import frontend.syntax.func.parse.MainFuncDefParser;
import frontend.tokenize.Token;
import frontend.tokenize.Tokenizer;

public class CompUnitParser extends ParserBase {

    // CompUnit := {ConstDecl | VarDecl} {FuncDef} MainFuncDef
    public CompUnitParser(Tokenizer tokenizer) {
        super(tokenizer);
        while (true) {
            updateToken();
            if (lookForward(Token.TokenType.CONSTTK)) {
                reverse();
                new ConstDeclParser(tokenizer);
                continue;
            } else if (lookForward(Token.TokenType.INTTK)) {
                updateToken();
                if (lookForward(Token.TokenType.IDENFR)) {
                    updateToken();
                    if (!lookForward(Token.TokenType.LPARENT)) {
                        reverse();
                        new VarDeclParser(tokenizer);
                        continue;
                    }
                }
            }
            reverse();
            break;
        }
        while (true) {
            updateToken();
            if (lookForward(Token.TokenType.VOIDTK)) {
                reverse();
                new FuncDefParser(tokenizer);
                continue;
            } else if (lookForward(Token.TokenType.INTTK)) {
                updateToken();
                if (!lookForward(Token.TokenType.MAINTK)) {
                    reverse();
                    new FuncDefParser(tokenizer);
                    continue;
                }
            }
            reverse();
            break;
        }
        new MainFuncDefParser(tokenizer);
        display();
    }
}
