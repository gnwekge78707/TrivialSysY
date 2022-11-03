package backend.isa;

public class MipsRegRegCal extends MipsInstruction {
    private final int dst;
    private final int src1;
    private final int src2;

    protected MipsRegRegCal(String instrName, int dst, int src1, int src2) {
        super(instrName);
        this.dst = dst;
        this.src1 = src1;
        this.src2 = src2;
    }

    @Override
    public String toString() {
        return getInstrName() +
                " $" + dst + ", $" + src1 + ", $" + src2;
    }
}
