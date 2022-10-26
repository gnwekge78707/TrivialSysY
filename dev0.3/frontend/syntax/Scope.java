package frontend.syntax;

import frontend.symbol.SymbolTable;

public interface Scope {
    SymbolTable getSymbolTable();
}
