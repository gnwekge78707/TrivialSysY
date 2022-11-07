package midend.ir.value;

import midend.ir.User;
import midend.ir.Value;
import midend.ir.type.LLVMType;

import java.util.ArrayList;

public class GlobalVariable extends User {
    private ArrayList<Integer> dimensions = new ArrayList<>();
    private ArrayList<Value> inits;
    private Value init;

    public GlobalVariable(String name, LLVMType type, Value init) {
        super(new LLVMType.Pointer(type), name, 0);
        this.init = init;
    }

    public GlobalVariable(String name, LLVMType type, int length, ArrayList<Value> inits) {
        super(new LLVMType.Pointer(type), name, 0);
        this.dimensions.add(length);
        this.inits = inits;
    }

    public Value getInit() {
        return init;
    }

    public ArrayList<Value> getInits() {
        return inits;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName()).append(" = dso_local ").append("global ");
        if (((LLVMType.Pointer) this.getType()).getPointedTo() instanceof LLVMType.Int) {
            sb.append(((LLVMType.Pointer) this.getType()).getPointedTo().toString()).append(" ");
            sb.append(this.init == null ?
                    "0 " : ((Constant) this.init).getConstVal()
            );
        } else if (((LLVMType.Pointer) this.getType()).getPointedTo() instanceof LLVMType.Array) {
            if (this.inits == null) {
                sb.append(((LLVMType.Pointer) this.getType()).getPointedTo().toString()).append(" ");
                sb.append("zeroinitializer ");
            } else {
                //FIXME!! inits toString method unimplemented
                sb.append(this.inits);
            }
        }
        return sb.toString();
    }

}
