package backend.isa;

public class MipsMulDiv extends MipsInstruction {
    private final int src1;
    private final int src2;

    protected MipsMulDiv(String instrName, int src1, int src2) {
        super(instrName);
        this.src1 = src1;
        this.src2 = src2;
    }

    @Override
    public String toString() {
        return getInstrName() +
                " $" + src1 + ", $" + src2;
    }
}
