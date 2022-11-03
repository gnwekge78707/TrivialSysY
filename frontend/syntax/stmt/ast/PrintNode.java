package frontend.syntax.stmt.ast;

import frontend.error.Error;
import frontend.syntax.NodeBase;
import frontend.syntax.expr.ast.BinaryExpNode;
import frontend.syntax.expr.ast.Calculatable;
import frontend.tokenize.FormatStr;
import frontend.tokenize.Token;
import midend.ir.ModuleBuilder;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.Function;
import midend.ir.value.instr.PutStringInstr;
import midend.ir.value.instr.terminator.CallInstr;

import java.util.ArrayList;
import java.util.Arrays;

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

    private ArrayList<String> strCons = new ArrayList<>();

    @Override
    public void checkErrors() {
        updateLastSymbolTable();
        for (NodeBase nodeBase : exps) {
            ((Calculatable) nodeBase).checkErrors();
        }
        String formatStr = ((FormatStr) formatString).getFormatStr();
        int cnt = 0, lastIdx = 1;
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
                strCons.add(formatStr.substring(lastIdx, i - 1));
                lastIdx = i + 1;
            }
        }
        strCons.add(formatStr.substring(lastIdx, formatStr.length() - 1));
        if (cnt != exps.size()) {
            handleError(Error.ErrorType.MISMATCHED_STRCON);
        }
        if (formatStrErr) {
            handleError(Error.ErrorType.ILLEGAL_CHAR);
        }
    }

    @Override
    public void buildIR(ModuleBuilder builder) {
        for (NodeBase nodeBase : exps) {
            ((BinaryExpNode) nodeBase).buildIR(builder);
        }
        if (!strCons.get(0).isEmpty()) {
            int addr = builder.putStrCon(strCons.get(0));
            new PutStringInstr(strCons.get(0), addr, builder.getCurBasicBlock());
        }
        for (int i = 0; i < exps.size(); i++) {
            Function putInt = builder.getLibFunc("putint");
            Value num = ((BinaryExpNode) exps.get(i)).getDst();
            new CallInstr(putInt, new ArrayList<>(Arrays.asList(num)), builder.getCurBasicBlock());
            if (!strCons.get(i+1).isEmpty()) {
                int addr = builder.putStrCon(strCons.get(i + 1));
                new PutStringInstr(strCons.get(i + 1), addr, builder.getCurBasicBlock());
            }
        }
    }
}
