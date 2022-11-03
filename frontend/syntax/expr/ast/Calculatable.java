package frontend.syntax.expr.ast;

import midend.ir.Value;

public interface Calculatable {
    ExpContext getExpContext();
    void checkErrors();
    Value getDst();
    //void calculate();
}
