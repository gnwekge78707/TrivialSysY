package frontend.syntax.expr.ast;

public interface Calculatable {
    ExpContext getExpContext();
    void checkErrors();
    //void calculate();
}
