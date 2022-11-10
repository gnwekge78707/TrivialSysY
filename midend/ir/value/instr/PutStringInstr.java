package midend.ir.value.instr;

import backend.MipsAssembly;
import backend.template.MipsFuncTemplate;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;

public class PutStringInstr extends Instruction {
    private String strCon;
    private int addr;

    public PutStringInstr(String strCon, int addr, BasicBlock parent) {
        super(InstrType.PUTSTR, LLVMType.Void.getVoid(), 0, parent);
        setHasDst(false);
        this.strCon = strCon;
        this.addr = addr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t");
        for (int i = 0; i < strCon.length(); i++) {
            int chInt = strCon.charAt(i);
            if (strCon.charAt(i) == '\\' && i + 1 < strCon.length() && strCon.charAt(i + 1) == 'n') {
                chInt = '\n';
                i++;
            }
            sb.append("\t").append("call void @putch(i32 ").append(chInt).append(")\n");
        }
        return sb.toString();
    }

    @Override
    public void toAssembly(MipsAssembly assembly) {
        MipsFuncTemplate.mipsPrintStrTemplate(addr, assembly);
    }
}
