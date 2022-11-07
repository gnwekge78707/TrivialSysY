package backend.isa;

public class MipsBranch extends MipsInstruction {
    private final int src1;
    private final int src2;
    private final String tag;

    protected MipsBranch(String instrName, int src, String tag) {
        super(instrName);
        this.tag = tag;
        this.src1 = src;
        this.src2 = -1;
    }

    protected MipsBranch(String instrName, int src1, int src2, String tag) {
        super(instrName);
        this.tag = tag;
        this.src1 = src1;
        this.src2 = src2;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return getInstrName() + (getInstrName().length() >= 4 ? "\t" : "\t\t") +
                " $" + src1 +
                (src2 >= 0 ? ", $" + src2 : "") +
                ", " + tag;
    }
}
