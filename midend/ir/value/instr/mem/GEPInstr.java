package midend.ir.value.instr.mem;

import backend.MipsAssembly;
import midend.ir.Value;
import midend.ir.type.LLVMType;
import midend.ir.value.BasicBlock;
import midend.ir.value.instr.Instruction;

/*
example1::
int a[5][4];
a[2][3];
------>
@a = global [20 x i32] zeroinitializer
%1 = mul i32 2, 4
%2 = add i32 %1, 3
%3 = getelementptr [20 x i32], [20 x i32]* @a, i32 0, i32 %2
____________________________________________________________
example2::
void set1(int pos, int arr[]) {
    arr[pos] = 1;
}
------>
define dso_local void @set1(i32 %0, i32* %1) {
    %3 = alloca i32*
    %4 = alloca i32
    store i32 %0, i32* %4
    store i32*  %1, i32* * %3
    %5 = load i32* , i32* * %3
    %6 = load i32, i32* %4
    %7 = getelementptr i32, i32* %5, i32 %6
    store i32 1, i32* %7
    ret void
}
 */
/**
 * 2 usage
 * visit global arr | local arr -> pointer type is [n x i32]*
 * visit funcFParam arr -> pointer type is i32*
 */
public class GEPInstr extends Instruction {
    private Value basePointer;
    private Value dstVar;

    private static LLVMType getPointerValueType(Value value) {
        assert value.getType() instanceof LLVMType.Pointer;
        if (((LLVMType.Pointer) value.getType()).getPointedTo() instanceof LLVMType.Array pointedArrType) {
            return pointedArrType.getType();
        } else {
            if (!(((LLVMType.Pointer) value.getType()).getPointedTo() instanceof LLVMType.Int)) {
                throw new Error("we only support pointer point to array or funcFParam");
            }
            return ((LLVMType.Pointer) value.getType()).getPointedTo();
        }
    }

    public GEPInstr(Value basePointer, Value idx) { // one dimension array, for
        super(InstrType.GETELEMENTPTR, new LLVMType.Pointer(getPointerValueType(basePointer)), 2);
        setOperand(0, basePointer);
        setOperand(1, idx);
        this.basePointer = (basePointer instanceof GEPInstr)?
                ((GEPInstr) basePointer).basePointer : basePointer;
    }

    public GEPInstr(Value basePointer, Value idx, BasicBlock parent) { // one dimension array
        super(InstrType.GETELEMENTPTR, new LLVMType.Pointer(getPointerValueType(basePointer)), 2, parent);
        setOperand(0, basePointer);
        setOperand(1, idx);
        this.basePointer = (basePointer instanceof GEPInstr)?
                ((GEPInstr) basePointer).basePointer : basePointer;
    }

    public GEPInstr(Value basePointer, Value idx, BasicBlock parent, Value dstVar) { // one dimension array
        super(InstrType.GETELEMENTPTR, new LLVMType.Pointer(getPointerValueType(basePointer)), 2, parent);
        setOperand(0, basePointer);
        setOperand(1, idx);
        this.dstVar = dstVar;
        this.basePointer = (basePointer instanceof GEPInstr)?
                ((GEPInstr) basePointer).basePointer : basePointer;
    }

    public Value getBasePointer() {
        return basePointer;
    }

    public Value getDstVar() { return this.dstVar; }

    public boolean hasExplicitDstVar() {
        return this.dstVar != null;
    }

    public String getName() { return hasExplicitDstVar() ? dstVar.getName() : super.getName(); }

    @Override
    public String
    toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t").append(this.getName())
                .append(" = getelementptr ")
                .append(((LLVMType.Pointer) getOperand(0).getType()).getPointedTo())
                .append(", ")
                .append(getOperand(0).getType())
                .append(" ")
                .append(getOperand(0).getName());
        assert basePointer.getType() instanceof LLVMType.Pointer;
        if (((LLVMType.Pointer) basePointer.getType()).getPointedTo() instanceof LLVMType.Array pointedArrType) {
            sb.append(", ").append(pointedArrType.getType()).append(" ").append(0)
                    .append(", ").append(getOperand(1).getName());
        } else {
            sb.append(", ").append(getOperand(1).getType())
                    .append(" ").append(getOperand(1).getName());
        }
        return sb.toString();
    }

    @Override
    public void toAssembly(MipsAssembly assembly) {
        //TODO
    }
}
