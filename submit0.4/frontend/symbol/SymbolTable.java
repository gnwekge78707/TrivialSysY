package frontend.symbol;

import frontend.syntax.NodeBase;
import frontend.syntax.func.ast.FuncDefNode;

import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, Symbol> symbolMap = new HashMap<>();
    private SymbolTable lastSymbolTable;
    private FuncSymbol currentFunction;

    public SymbolTable(SymbolTable lastSymbolTable) {
        this.lastSymbolTable = lastSymbolTable;
    }

    public SymbolTable(SymbolTable lastSymbolTable, FuncSymbol currentFunction) {
        this.lastSymbolTable = lastSymbolTable;
        this.currentFunction = currentFunction;
    }

    public void addSymbol(Symbol symbol) {
        symbolMap.put(symbol.getName(), symbol);
    }

    public SymbolTable getLastSymbolTable() {
        return lastSymbolTable;
    }

    public FuncSymbol getCurrentFunction() {
        return currentFunction;
    }

    public Symbol getSymbol(String name, boolean inThisScope) {
        if (inThisScope) {
            return symbolMap.getOrDefault(name, null);
        } else {
            if (symbolMap.containsKey(name)) {
                return symbolMap.get(name);
            } else {
                for (SymbolTable table = lastSymbolTable; table != null; table = table.getLastSymbolTable()) {
                    if (table.getSymbol(name, true) != null) {
                        return table.getSymbol(name, true);
                    }
                }
            }
            return null;
        }
    }

    public FuncSymbol getFuncSymbol(String name, boolean inThisScope) {
        Symbol symbol = getSymbol(name, inThisScope);
        if (symbol == null) {
            return null;
        }
        if (symbol instanceof FuncSymbol) {
            return (FuncSymbol) symbol;
        } else {
            return null;
        }
    }

    public LValSymbol getLValSymbol(String name, boolean inThisScope) {
        Symbol symbol = getSymbol(name, inThisScope);
        if (symbol == null) {
            return null;
        }
        if (symbol instanceof LValSymbol) {
            return (LValSymbol) symbol;
        } else {
            return null;
        }
    }

}
