/**
 * BinomialHeap
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */
public class BinomialHeap {
    public int size;
    public HeapNode last;
    public HeapNode min;

    public BinomialHeap() {
        this.size = 0;
        this.last = null;
        this.min = null;
    }

    /**
     * pre: key > 0
     * <p>
     * Insert (key,info) into the heap and return the newly generated HeapItem.
     */
    public HeapItem insert(int key, String info) {
        // create new node and item
        HeapItem item = new HeapItem(key, info);
        HeapNode node = new HeapNode(item);
        item.node = node;

        // if heap is empty
        if (this.size == 0) {
            this.last = node;
            this.min = node;
            this.size++;
        } else {

        }


        // dont forget to maintain min
        return null; // should be replaced by student code
    }

    /**
     * Delete the minimal item
     */
    public void deleteMin() {
        return; // should be replaced by student code

    }

    /**
     * Return the minimal HeapItem
     */
    public HeapItem findMin() {
        return null; // should be replaced by student code
    }

    /**
     * pre: 0 < diff < item.key
     * <p>
     * Decrease the key of item by diff and fix the heap.
     */
    public void decreaseKey(HeapItem item, int diff) {
        return; // should be replaced by student code
    }

    /**
     * Delete the item from the heap.
     */
    public void delete(HeapItem item) {
        return; // should be replaced by student code
    }

    /**
     * Meld the heap with heap2
     */
    public void meld(BinomialHeap heap2) {
        BinomialHeap newHeap = new BinomialHeap();
        HeapNode minNode1 = this.last.next;
        HeapNode minNode2 = heap2.last.next;
        int minRank1 = minNode1.rank;
        int minRank2 = minNode2.rank;

        HeapNode carry = null; // todo initialize?
        boolean handledLast1 = false;
        boolean handledLast2 = false;
        while (!handledLast1 && !handledLast2) {  //todo - change this
            handledLast1 = minNode1 == this.last;
            handledLast2 = minNode2 == heap2.last;

            if (minRank1 == minRank2 && carry == null) {
                carry = link(minNode1, minNode2);
                minNode1 = minNode1.next;
                minNode2 = minNode2.next;
            } else if (minRank1 == minRank2) {
                if(newHeap.last == null){
                    newHeap.last = carry;
                }
                else{
                    HeapNode first = newHeap.last.next;  // save first node
                    newHeap.last.next = carry;
                    newHeap.last = newHeap.last.next;
                    newHeap.last.next = first;
                }
                carry = link(minNode1, minNode2);
                minNode1 = minNode1.next;
                minNode2 = minNode2.next;
            } else if (minRank1 > minRank2) {
                carry = link(minNode2, carry);
                minNode2 = minNode2.next;
            } else {
                carry = link(minNode1, carry);
                minNode1 = minNode1.next;
            }
        }
        if(carry != null){
            HeapNode first = newHeap.last.next;  // save first node
            newHeap.last.next = carry;
            newHeap.last = newHeap.last.next;
            newHeap.last.next = first;
        }

        this.last = newHeap.last;
        this.size = newHeap.size;
        // todo min!!

        return; // should be replaced by student code
    }


    /**
     * Return the number of elements in the heap
     */
    public int size() {
        return 42; // should be replaced by student code
    }

    /**
     * The method returns true if and only if the heap
     * is empty.
     */
    public boolean empty() {
        return false; // should be replaced by student code
    }

    /**
     * Return the number of trees in the heap.
     */
    public int numTrees() {
        return 0; // should be replaced by student code
    }

    public HeapNode link(HeapNode x, HeapNode y) {
        if (y == null) {
            return x;
        }
        // make sure x < y
        if (x.item.key > y.item.key) {
            HeapNode temp = x;
            x = y;
            y = temp;
        }
        if (x.child == null) {  // rank is 0
            y.next = y;
        } else {
            y.next = x.child.next;
            x.child.next = y;
        }
        x.child = y;
        y.parent = x;
        x.rank += y.rank;
        return x;
    }

    /**
     * Class implementing a node in a Binomial Heap.
     */
    public class HeapNode {
        public HeapItem item;
        public HeapNode child;
        public HeapNode next;
        public HeapNode parent;
        public int rank;

        public HeapNode(HeapItem item) {
            this.item = item;
            this.rank = 0;
            this.child = null;
            this.next = null;
            this.parent = null;
        }
    }

    /**
     * Class implementing an item in a Binomial Heap.
     */
    public class HeapItem {
        public HeapNode node;
        public int key;
        public String info;

        public HeapItem(int key, String info) {
            this.node = null;
            this.key = key;
            this.info = info;
        }
    }


}
