package midend.ir.value;

import midend.ir.User;
import midend.ir.type.LLVMType;

import java.util.Objects;

public class Constant extends User {
    private int constVal;
    private final static Constant const0 = new Constant(LLVMType.Int.getI32(), 0);

    public Constant(LLVMType type, int constVal) {
        super(type, "", 0);
        this.constVal = constVal;
        if (type == LLVMType.Int.getI1() && (constVal != 0 && constVal != 1)) {
            throw new Error("LLVM ERROR: i1 get value other than 0,1");
        }
    }

    public static Constant getConst0() {
        return const0;
    }

    public int getConstVal() {
        return constVal;
    }

    @Override
    public String toString() {
        return this.getType().toString() + " " + this.constVal;
    }

    @Override
    public String getName() {
        return String.valueOf(this.constVal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constant constant = (Constant) o;
        return constVal == constant.constVal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(constVal);
    }
}
