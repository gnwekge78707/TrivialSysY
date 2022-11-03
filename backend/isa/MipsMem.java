package backend.isa;

public class MipsMem extends MipsInstruction {
    private final int reg; // can be src and dst
    private final int addr; // also a reg
    private final int offset;

    protected MipsMem(String instrName, int reg, int offset, int addr) {
        super(instrName);
        this.reg = reg;
        this.addr = addr;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return getInstrName() +
                " $" + reg + ", " + offset + "($" + addr + ")";
    }
}
