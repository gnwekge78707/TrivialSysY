package backend.isa;

public class MipsOther extends MipsInstruction {
    private final int src1;
    private final int src2;
    private final String tag;
    private final DumpMips dumpMips;

    protected MipsOther(String instrName, int src1, int src2, String tag, DumpMips dumpMips) {
        super(instrName);
        this.tag = tag;
        this.src1 = src1;
        this.src2 = src2;
        this.dumpMips = dumpMips;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return dumpMips.accept(getInstrName(), tag, src1, src2);
    }

    @FunctionalInterface
    protected interface DumpMips {
        String accept(String instrName, String tag, int src1, int src2);
    }
}
