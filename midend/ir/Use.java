package midend.ir;

public class Use {
    public final User user;
    public final Value value;
    public final int idxOfValueInUser; // opRand -> value's rank in user
    //TODO: operand就是user里面使用的value

    public Use(User user, Value value, int idxOfValueInUser) {
        this.user = user;
        this.value = value;
        this.idxOfValueInUser = idxOfValueInUser;
    }
}
