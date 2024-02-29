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

    public BinomialHeap(HeapItem item) {
        HeapNode node = new HeapNode(item);
        item.node = node;
        this.size = 1;
        this.last = node;
        this.last.next = this.last;
        this.min = node;
    }

    public BinomialHeap(HeapNode node) {
        this.size = 1;
        this.last = node;
        this.last.next = this.last;
        this.min = node;

    }

    public void printHeap() {
        if (empty()) {
            System.out.println("Heap is empty");
            return;
        }
        System.out.println("Binomial Heap:");
        HeapNode currentRoot = last;
        HeapNode stopNode = last.next; // Stop condition for circular list of roots
        boolean stop = false;

        do {
            System.out.println("Root: " + currentRoot.item.key);
            printTree(currentRoot, 0, currentRoot); // Print the tree rooted at current root
            currentRoot = currentRoot.next;
            if (currentRoot == stopNode) {
                stop = true; // We've visited all roots
            }
        } while (!stop);
    }

    private void printTree(HeapNode node, int depth, HeapNode initialRoot) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("  "); // Adjust spacing for depth
        }
        sb.append(node.item.key).append(" [").append(node.rank).append("]");

        System.out.println(sb.toString());

        if (node.child != null) {
            printTree(node.child, depth + 1, node.child); // Print child recursively
        }

        if (node.next != node.parent && node.next != null && node.next != initialRoot) {
            printTree(node.next, depth, initialRoot); // Print sibling recursively until we reach the initial root
        }
    }

    /**
     * pre: key > 0
     * <p>
     * Insert (key,info) into the heap and return the newly generated HeapItem.
     */
    public HeapItem insert(int key, String info) {

        // create new node and item
        HeapItem item = new HeapItem(key, info);
        BinomialHeap newHeap = new BinomialHeap(item);


        // if heap is empty
        if (this.size == 0) {
            //make "this" the new heap
            this.size = newHeap.size;
            this.min = newHeap.min;
            this.last = newHeap.last;
        } else {
            meld(newHeap);
            this.size++;
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
        int minRank1;
        int minRank2;

        HeapNode carry = null; // todo initialize?
        boolean handledLast1 = false;
        boolean handledLast2 = false;
        boolean hasCarry = false;
        while (!handledLast1 && !handledLast2) {  //todo - change this
            minRank1 = minNode1.rank;
            minRank2 = minNode2.rank;

            if (minRank1 == minRank2 && carry == null) {
                handledLast1 = minNode1 == this.last;
                handledLast2 = minNode2 == heap2.last;
                carry = link(minNode1, minNode2);
                hasCarry = true;
                minNode1 = minNode1.next;
                minNode2 = minNode2.next;
                if (newHeap.last == null) {  // newHeap is empty
                    newHeap = new BinomialHeap(carry);
                } else {
                    newHeap.last.next = carry;
                }
                hasCarry = false;
            } else if (minRank1 == minRank2) {  // there is a carry
                handledLast1 = minNode1 == this.last;
                handledLast2 = minNode2 == heap2.last;
                if (newHeap.last == null) {  // newHeap is empty
                    newHeap = new BinomialHeap(carry);
                } else {
                    newHeap.appendNode(carry);
                }
                carry = link(minNode1, minNode2);
                hasCarry = true;
                minNode1 = minNode1.next;
                minNode2 = minNode2.next;
            } else if (minRank1 > minRank2) {
                carry = link(minNode2, carry);
                hasCarry = true;
                minNode2 = minNode2.next;
                handledLast2 = minNode2 == heap2.last;
            } else {
                carry = link(minNode1, carry);
                hasCarry = true;
                minNode1 = minNode1.next;
                handledLast1 = minNode1 == this.last;
            }
        }
        if (hasCarry) {
            if (newHeap.last == null) {  // newHeap is empty
                newHeap = new BinomialHeap(carry);
            } else {
                newHeap.appendNode(carry);
            }
        }
        if (!handledLast1 && handledLast2) {
            connect(newHeap, minNode1, this);
        } else if (!handledLast2) {
            connect(newHeap, minNode2, heap2);
        }


//        if(hasCarry && handledLast1 && handledLast2){
//
//
//        } else-if (hasCarry && handledLast1) {
//
//        }else-if (hasCarry && handledLast2){
//
//        }

        this.last = newHeap.last;
        // todo min!!

        return; // should be replaced by student code
    }

    public void connect(BinomialHeap heap, HeapNode node, BinomialHeap biggerRankHeap) {
        HeapNode first = heap.last.next;
        heap.last.next = node;
        heap.last = biggerRankHeap.last;
        heap.last.next = first;
    }

    public void appendNode(HeapNode node) {  // append to non-empty heap at end of linked list
        HeapNode first = this.last.next;  // save first node
        this.last.next = node;
        this.last = this.last.next;
        this.last.next = first;
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
        x.rank += 1;
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
