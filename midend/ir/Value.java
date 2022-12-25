package midend.ir;

import midend.ir.type.LLVMType;

import java.util.LinkedList;

public class Value {
    private LLVMType type;
    private String name;
    private LinkedList<Use> useList;

    public Value(LLVMType type, String name) {
        this.type = type;
        this.name = name;
        this.useList = new LinkedList<>();
        this.mipsMemContex = new MipsMemContex(this);
    }

    public LLVMType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Use> getUseList() {
        return useList;
    }

    public void addUse(Use use) {
        this.useList.add(use);
    }

    public void removeUse(User user, int idx) {
        this.useList.removeIf(u -> (u.user == user && u.idxOfValueInUser == idx));
    }

    public void replaceSelfWith(Value newValue) {
        for (int i = useList.size() - 1; i >= 0; i--) {
            Use use = useList.get(i);
            use.user.setOperand(use.idxOfValueInUser, newValue);
        }
        this.useList.clear();
    }

    @Override
    public String toString() {
        return type.toString() + " " + name;
    }

    //------------------------------------------------for backend

    private MipsMemContex mipsMemContex;

    public MipsMemContex getMipsMemContex() {
        return mipsMemContex;
    }

    public void setMipsMemContex(MipsMemContex mipsMemContex) {
        this.mipsMemContex = mipsMemContex;
    }
}
