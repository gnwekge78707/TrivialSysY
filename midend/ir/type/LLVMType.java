package midend.ir.type;

import java.util.ArrayList;
import java.util.Objects;

public class LLVMType {
    public static class Array extends LLVMType {
        private final int length;
        private final LLVMType type;

        public Array(LLVMType type, int length) {
            this.length = length;
            this.type = type;
        }

        public LLVMType getType() {
            return type;
        }

        public int getLength() {
            return length;
        }

        @Override
        public String toString() {
            return "[" + length + " x " + type.toString() + "]";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Array array = (Array) o;
            return length == array.length && Objects.equals(type, array.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(length, type);
        }
    }

    public static class Function extends LLVMType {
        private final LLVMType retType;
        private final ArrayList<LLVMType> paramsType;

        public Function(LLVMType retType, ArrayList<LLVMType> paramsType) {
            this.retType = retType;
            this.paramsType = paramsType;
        }

        public ArrayList<LLVMType> getParamsType() {
            return paramsType;
        }

        public LLVMType getRetType() {
            return retType;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public static class Int extends LLVMType {
        private static final Int i32 = new Int(32);
        private static final Int i1 = new Int(1);

        private final int bitsWidth;

        private Int(int bitsWidth) { this.bitsWidth = bitsWidth; }

        public static Int getI1() { return i1; }

        public static Int getI32() { return i32; }

        @Override
        public String toString() { return "i" + bitsWidth; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Int anInt = (Int) o;
            return bitsWidth == anInt.bitsWidth;
        }

        @Override
        public int hashCode() {
            return Objects.hash(bitsWidth);
        }
    }

    public static class Label extends LLVMType {
        private final static Label label = new Label();

        private Label() {}

        public static Label getLabel() { return label; }

        @Override
        public String toString() { return "label"; }
    }

    public static class Pointer extends LLVMType {
        private final LLVMType pointedTo;

        public Pointer(LLVMType type) { this.pointedTo = type; }

        public LLVMType getPointedTo() { return pointedTo; }

        public String toString() { return pointedTo.toString() + "*"; }
    }

    public static class Void extends LLVMType {
        private final static Void type = new Void();

        private Void() {}

        public static Void getVoid() { return type; }

        @Override
        public String toString() { return "void"; }
    }
}
