package midend.ir.value.instr.mem;

import backend.MipsAssembly;
import backend.template.MipsMemTemplate;
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
        //System.out.println(value);
        if (((LLVMType.Pointer) value.getType()).getPointedTo() instanceof LLVMType.Array) {
            LLVMType.Array pointedArrType = (LLVMType.Array) ((LLVMType.Pointer) value.getType()).getPointedTo();
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
        if (((LLVMType.Pointer) basePointer.getType()).getPointedTo() instanceof LLVMType.Array) {
            LLVMType.Array pointedArrType = (LLVMType.Array) ((LLVMType.Pointer) basePointer.getType()).getPointedTo();
            sb.append(", ").append(pointedArrType.getType()).append(" ").append(0)
                    .append(", ").append(getOperand(1).getType()).append(" ").append(getOperand(1).getName());
        } else {
            sb.append(", ").append(getOperand(1).getType())
                    .append(" ").append(getOperand(1).getName());
        }
        return sb.toString();
    }

    @Override
    public void toAssembly(MipsAssembly assembly) {
        //TODO
        // %dst = gep [i32 x 10], [i32 x 10]* %base, i32 0, i32 %offset
        // %dst = gep i32, i32* %base, i32 %offset
        // base是函数参数指针，那它本身就是计算好的地址值 (旧的栈帧里面 有效)，只要加上偏移即可
        // base是局部数组，那需要计算出 base的绝对地址，然后加上偏移量
        Value dst = hasExplicitDstVar() ? dstVar : this;
        MipsMemTemplate.mipsGEPTemplate(dst, basePointer, getOperand(1), assembly);
        dst.getMipsMemContex().updateMem(assembly);
    }

    public static boolean checkSame(GEPInstr i1, GEPInstr i2) {
        if (i1.getOperandNum() != i2.getOperandNum()) {
            return false;
        }
        for (int i = 0; i < i1.getOperandNum(); ++i) {
            if (!i1.getOperand(i).equals(i2.getOperand(i))) {
                return false;
            }
        }
        return true;
    }
}
