package midend.ir.value;

import midend.ir.User;
import midend.ir.Value;
import midend.ir.type.LLVMType;

import java.util.ArrayList;

public class NormalVariable extends User {
    private ArrayList<Integer> dimensions = new ArrayList<>();
    private ArrayList<Value> inits;
    private Value init;

    public NormalVariable(String name, LLVMType type, Value init) {
        super(new LLVMType.Pointer(type), name, 0);
        this.init = init;
    }

    public NormalVariable(String name, LLVMType type, int length, ArrayList<Value> inits) {
        super(new LLVMType.Pointer(type), name, 0);
        assert type instanceof LLVMType.Array;
        this.dimensions.add(length);
        this.inits = inits;
    }
}