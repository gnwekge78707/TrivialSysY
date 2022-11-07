package backend.isa;

public class MipsRegImmCal extends MipsInstruction {
    private final int dst;
    private final int src;
    private final int imm;

    protected MipsRegImmCal(String instrName, int dst, int src, int imm) {
        super(instrName);
        this.dst = dst;
        this.src = src;
        this.imm = imm;
    }

    @Override
    public String toString() {
        return getInstrName() + (getInstrName().length() >= 4 ? "\t" : "\t\t") +
                " $" + dst + ", $" + src + ", " + imm;
    }
}
