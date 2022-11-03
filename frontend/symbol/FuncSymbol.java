package frontend.symbol;

import frontend.syntax.expr.parse.AddExpParser;
import frontend.tokenize.Token;
import midend.ir.type.LLVMType;
import midend.ir.value.Function;

import java.util.ArrayList;
import java.util.Arrays;

public class FuncSymbol implements Symbol {
    private Token ident;
    private Token.TokenType declType;
    private ArrayList<LValSymbol> params;

    public FuncSymbol(Token ident, Token.TokenType declType, ArrayList<LValSymbol> params) {
        this.declType = declType;
        this.ident = ident;
        this.params = params;
    }

    public FuncSymbol(Token ident, Token.TokenType declType) {
        this.declType = declType;
        this.ident = ident;
        this.params = new ArrayList<>();
    }

    public void addParams(LValSymbol symbol) {
        params.add(symbol);
    }

    public ArrayList<LValSymbol> getParams() {
        return params;
    }

    public Token.TokenType getDeclType() {
        return declType;
    }

    public boolean hasParams() { return params.size() > 0; }

    public String getName() {
        return ident.toString();
    }

    //=================================================LLVM below====================================================//
    private Function llvmFunction;

    public void setLLVMFunction(Function llvmFunction) {
        this.llvmFunction = llvmFunction;
    }

    public Function getLLVMFunction() {
        return llvmFunction;
    }

    private LLVMType getRetType() {
        return (declType == Token.TokenType.INTTK) ?
                LLVMType.Int.getI32() : (declType == Token.TokenType.VOIDTK) ?
                LLVMType.Void.getVoid() : null;
    }

    public LLVMType getLLVMType() {
        ArrayList<LLVMType> paramsType = new ArrayList<>();
        for (LValSymbol lValSymbol : params) {
            if (lValSymbol.getLLVMType() instanceof LLVMType.Array) {
                paramsType.add(new LLVMType.Pointer(LLVMType.Int.getI32()));
            } else {
                paramsType.add(lValSymbol.getLLVMType());
            }
        }
        LLVMType retType = getRetType();
        if (retType == null) {
            throw new Error("unsupported llvm type");
        }
        return new LLVMType.Function(retType, paramsType);
    }
}
