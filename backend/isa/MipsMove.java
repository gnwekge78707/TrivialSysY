package backend.isa;

public class MipsMove extends MipsInstruction {
    private final int dst;
    private final int src;

    protected MipsMove(String instrName, int dst, int src) {
        super(instrName);
        this.dst = dst;
        this.src = src;
    }

    @Override
    public String toString() {
        return getInstrName() + (getInstrName().length() >= 4 ? "\t" : "\t\t") +
                " $" + dst +
                (src >= 0 ? ", $" + src : "");
    }
}
