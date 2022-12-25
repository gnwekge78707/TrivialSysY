package midend.pass;

import midend.ir.Value;
import midend.ir.value.Function;
import midend.ir.value.GlobalVariable;
import midend.ir.value.instr.mem.AllocaInstr;
import midend.ir.value.instr.mem.GEPInstr;
import midend.ir.value.instr.mem.LoadInstr;

public class AliasAnalysis {
    public static Value getArray(Value pointer) {
        while (true) {
            if (pointer instanceof GEPInstr) {
                pointer = ((GEPInstr) pointer).getOperand(0);
            } else if (pointer instanceof LoadInstr) {
                pointer = ((LoadInstr) pointer).getOperand(0);
            } else if (pointer instanceof GlobalVariable) {
                return pointer;
            } else if (pointer instanceof AllocaInstr) {
                return pointer;
            } else {
                return null;
            }
        }
    }

    public static boolean isGlobal(Value arr) {
        return arr instanceof GlobalVariable;
    }

    public static boolean isParam(Value arr) {
        return arr instanceof Function.Param;
    }

    public static boolean isLocal(Value arr) {
        return !isGlobal(arr) && !isParam(arr);
    }

    public static boolean equal(Value arr1, Value arr2) {
        if ((isParam(arr1) && isParam(arr2)) || (isGlobal(arr1) && isGlobal(arr2) || (isLocal(arr1) && isLocal(arr2)))) {
            return arr1 == arr2;
        }
        return false;
    }

    public static boolean callUseArray(Value pointer) {
        return false;
    }
}
