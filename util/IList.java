package util;

import java.util.Iterator;

public class IList<Value, Holder> implements Iterable<IList.INode<Value, Holder>> {
    private final Holder holder;
    // 持有链表头的元素（也是一种Value），比如一个 BasicBlock 持有了一个包含多个 Inst 的链表,那这个IList的Holder就是这个BasicBlock
    private INode<Value, Holder> head; // 哨兵节点，不存储数据
    private INode<Value, Holder> tail; // 哨兵节点，不存储数据
    private int nodeNum = 0; // 节点数

    public IList(Holder holder) {
        this.holder = holder;
        head = new INode<>();
        head.setParent(this);
        tail = new INode<>();
        tail.setParent(this);
        head.next = tail;
        tail.prev = head;
    }

    public void clear() {
        this.head = null;
        this.tail = null;
        this.nodeNum = 0;
    }

    public boolean isEmpty() {
        if (head.next == tail && tail.prev == head) {
            return true;
        } else if (head.next == tail || tail.prev == head) {
            throw new RuntimeException();
        }
        return false;
    }

    public int getNodeNum() {
        return nodeNum;
    }

    public Holder getHolder() {
        return holder;
    }

    public INode<Value, Holder> getEntry() {
        return head.next;
    }

    public INode<Value, Holder> getLast() {
        return tail.prev;
    }

    @Override
    public Iterator<INode<Value, Holder>> iterator() {
        return new IIterator(head, tail);
    }

    public static class INode<Value, Holder> {
        public final boolean isGuard;
        private Value value;
        private INode<Value, Holder> prev = null; //前驱
        private INode<Value, Holder> next = null; //后继
        private IList<Value, Holder> parent = null;

        public INode() {
            this.isGuard = true; //只用来产生哨兵
        }

        public INode(Value value) {
            this.value = value;
            this.isGuard = false;
        }

        public Value getValue() {
            return value;
        }

        public void setValue(Value newValue) {
            this.value = newValue;
        }

        public void setParent(IList<Value, Holder> parent) {
            this.parent = parent;
        }

        public IList<Value, Holder> getParent() {
            return parent;
        }

        public INode<Value, Holder> getPrev() {
            return prev;
        }

        public INode<Value, Holder> getNext() {
            return next;
        }

        // 将调用者自己（this）插入目标节点后面（把参数的节点当成当前节点前驱节点，将当前节点插入到链表中）
        public void insertPrev(INode<Value, Holder> prev) { // insert this as prev's next
            this.parent = prev.parent;
            this.parent.nodeNum++;
            this.prev = prev;
            this.next = prev.next;
            prev.next = this;
            if (this.next != null) this.next.prev = this;
        }

        // 将调用者自己（this）插入目标节点前面（把参数的节点当成当前节点后继节点，将当前节点插入到链表中）
        public void insertNext(INode<Value, Holder> next) { // insert this as next's prev
            this.parent = next.parent;
            this.parent.nodeNum++;
            this.prev = next.prev;
            this.next = next;
            next.prev = this;
            if (this.prev != null) this.prev.next = this;
        }

        // 将自己插入目标IList的开头
        public void insertAtEntry(IList<Value, Holder> father) {
            this.setParent(father);
            insertPrev(father.head);
        }

        // 将自己插入目标IList的末尾
        public void insertAtEnd(IList<Value, Holder> father) {
            this.setParent(father);
            insertNext(father.tail);
        }

        /*
         * 在链表中 === parent不为空
         * 成功从链表中删除 return ture
         * 不在链表中 return false
         */
        public boolean removeSelf() {
            if (this.parent == null) return false;
            this.next.prev = this.prev;
            this.prev.next = this.next;
            this.parent.nodeNum--;
            this.parent = null;
            return true;
        }
    }

    class IIterator implements Iterator<INode<Value, Holder>> {
        INode<Value, Holder> head;
        INode<Value, Holder> tail;
        INode<Value, Holder> temp;

        IIterator(INode<Value, Holder> head, INode<Value, Holder> tail) {
            this.head = head;
            this.tail = tail;
            temp = head;
        }

        @Override
        public boolean hasNext() {
            return temp.next != tail;
        }

        @Override
        public INode<Value, Holder> next() {
            INode<Value, Holder> t = temp.next;
            temp = temp.next;
            return t;
        }

        @Override
        public void remove() { // 暂时无删除上的需求 需要时可以调用INode的removeSelf()
            throw new UnsupportedOperationException();
        }
    }
}
