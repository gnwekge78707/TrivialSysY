package backend.isa;

public abstract class MipsInstruction { // Instr Factory
    private int indent; // indent 4 spaces by default
    private final String instrName;

    public MipsInstruction(String name) {
        this.instrName = name;
        this.indent = 4;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

    public int getIndent() {
        return indent;
    }

    public String getInstrName() {
        return instrName;
    }

    //-----------------------------------factory from here-----------------------------------
    /**
     *  Cal Instr on Reg op Reg ============================================
     */
    public static MipsRegRegCal getAddu(int dst, int src1, int src2) {
        return new MipsRegRegCal("addu", dst, src1, src2);
    }

    public static MipsRegRegCal getSubu(int dst, int src1, int src2) {
        return new MipsRegRegCal("subu", dst, src1, src2);
    }

    public static MipsRegRegCal getAdd(int dst, int src1, int src2) {
        return new MipsRegRegCal("add", dst, src1, src2);
    }

    public static MipsRegRegCal getSub(int dst, int src1, int src2) {
        return new MipsRegRegCal("sub", dst, src1, src2);
    }

    public static MipsRegRegCal getSlt(int dst, int src1, int src2) {
        return new MipsRegRegCal("slt", dst, src1, src2);
    }

    public static MipsRegRegCal getSltu(int dst, int src1, int src2) {
        return new MipsRegRegCal("sltu", dst, src1, src2);
    }

    public static MipsRegRegCal getXor(int dst, int src1, int src2) {
        return new MipsRegRegCal("xor", dst, src1, src2);
    }
    /**
     *  Cal Instr on Reg op Reg ============================================
     */
    public static MipsMulDiv getMult(int src1, int src2) {
        return new MipsMulDiv("mult", src1, src2);
    }

    public static MipsMulDiv getDiv(int src1, int src2) {
        return new MipsMulDiv("div", src1, src2);
    }
    /**
     *  Cal Instr on Reg op Imm ============================================
     */
    public static MipsRegImmCal getAddiu(int dst, int src, int imm) {
        return new MipsRegImmCal("addiu", dst, src, imm);
    }

    public static MipsRegImmCal getSll(int dst, int src, int imm) {
        return new MipsRegImmCal("sll", dst, src, imm);
    }

    public static MipsRegImmCal getSrl(int dst, int src, int imm) {
        return new MipsRegImmCal("srl", dst, src, imm);
    }

    public static MipsRegImmCal getSra(int dst, int src, int imm) {
        return new MipsRegImmCal("sra", dst, src, imm);
    }

    public static MipsRegImmCal getSlti(int dst, int src, int imm) {
        return new MipsRegImmCal("slti", dst, src, imm);
    }

    public static MipsRegImmCal getSltiu(int dst, int src, int imm) {
        return new MipsRegImmCal("sltiu", dst, src, imm);
    }

    public static MipsRegImmCal getOri(int dst, int src, int imm) {
        return new MipsRegImmCal("ori", dst, src, imm);
    }

    public static MipsRegImmCal getXori(int dst, int src, int imm) {
        return new MipsRegImmCal("xori", dst, src, imm);
    }

    public static MipsRegImmCal getSllv(int dst, int src, int imm) {
        return new MipsRegImmCal("sllv", dst, src, imm);
    }
    /**
     *  Move Move on Reg op Reg ============================================
     */
    public static MipsMove getMove(int dst, int src) {
        return new MipsMove("move", dst, src);
    }

    public static MipsMove getMfhi(int dst) {
        return new MipsMove("mfhi", dst, -1);
    }

    public static MipsMove getMflo(int dst) {
        return new MipsMove("mflo", dst, -1);
    }
    /**
     *  Load Store on Reg off add ============================================
     */
    public static MipsMem getLw(int reg, int offset, int addr) {
        return new MipsMem("lw", reg, offset, addr);
    }

    public static MipsMem getSw(int reg, int offset, int addr) {
        return new MipsMem("sw", reg, offset, addr);
    }
    /**
     *  Br Instr on Reg op tag ============================================
     */
    public static MipsBranch getBgez(int src, String tag) {
        return new MipsBranch("bgez", src, tag);
    }

    public static MipsBranch getBgtz(int src, String tag) {
        return new MipsBranch("bgtz", src, tag);
    }

    public static MipsBranch getBlez(int src, String tag) {
        return new MipsBranch("blez", src, tag);
    }

    public static MipsBranch getBltz(int src, String tag) {
        return new MipsBranch("bltz", src, tag);
    }
    /**
     *  Other Instr on 4 sth ============================================
     */
    public static MipsOther getComment(String comment) {
        return new MipsOther("#", -1, -1, comment, (x, y, z, w) -> x + " " + y);
    }

    public static MipsOther getSyscall() {
        return new MipsOther("syscall", -1, -1, null, (x, y, z, w) -> x);
    }

    public static MipsOther getLui(int dst, int imm) {
        return new MipsOther("lui", dst, imm, null, (x, y, z, w) -> x + " $" + z + ", " + w);
    }

    public static MipsOther getTag(String tag) {
        return new MipsOther(tag + ":", -1, -1, null, (x, y, z, w) -> x);
    }

    public static MipsOther getJ(String tag) {
        return new MipsOther("j", -1, -1, tag, (x, y, z, w) -> x + " " + y);
    }

    public static MipsOther getLa(int dst, String tag) {
        return new MipsOther("la", dst, -1, tag, (x, y, z, w) -> x + " $" + z + ", " + y);
    }

    public static MipsOther getJaL(String tag) {
        return new MipsOther("jal", -1, -1, tag, (x, y, z, w) -> x + " " + y);
    }

    public static MipsOther getJr(int dst) {
        return new MipsOther("jr", dst, -1, null, (x, y, z, w) -> x + " $" + z);
    }
}
