package frontend.syntax.stmt.ast;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.expr.ast.Calculatable;
import frontend.tokenize.FormatStr;
import frontend.tokenize.Token;

import java.util.ArrayList;

public class PrintNode extends NodeBase implements Stmt {
    private Token formatString;
    private ArrayList<NodeBase> exps;

    public PrintNode(NodeBase parent, SyntaxType syntaxType) {
        super(parent, syntaxType);
    }

    public void register(Token formatString, ArrayList<NodeBase> exps) {
        this.exps = exps;
        this.formatString = formatString;
    }

    @Override
    public void checkErrors() {
        updateLastSymbolTable();
        for (NodeBase nodeBase : exps) {
            ((Calculatable) nodeBase).checkErrors();
        }
        String formatStr = ((FormatStr) formatString).getFormatStr();
        int cnt = 0;
        boolean formatStrErr = false;
        for (int i = 1; i < formatStr.length() - 1; i++) {
            char cur = formatStr.charAt(i);
            char last = formatStr.charAt(i - 1);
            if (cur != 32 && cur != 33 && cur != '%' && (cur < 40 || cur > 126)) {
                formatStrErr = true;
            } else if (last == '\\' && cur != 'n') {
                formatStrErr = true;
            } else if (last == '%' && cur != 'd') {
                formatStrErr = true;
            } else if (cur == '\\' && i + 1 >= formatStr.length() - 1) {
                formatStrErr = true;
            }
            if (last == '%' && cur == 'd') {
                cnt++;
            }
        }
        if (cnt != exps.size()) {
            handleError(Error.ErrorType.MISMATCHED_STRCON);
        }
        if (formatStrErr) {
            handleError(Error.ErrorType.ILLEGAL_CHAR);
        }
    }
}
