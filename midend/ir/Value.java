package midend.ir;

import midend.ir.type.LLVMType;

import java.util.LinkedList;

public class Value {
    private LLVMType type;
    private String name;
    private LinkedList<Use> uses;

    public Value(LLVMType type, String name) {
        this.type = type;
        this.name = name;
        this.uses = new LinkedList<>();
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

    public LinkedList<Use> getUses() {
        return uses;
    }

    public void addUse(Use use) {
        this.uses.add(use);
    }

    public void removeUse(User user, int idx) {
        this.uses.removeIf(u -> (u.user == user && u.idxOfValueInUser == idx));
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
