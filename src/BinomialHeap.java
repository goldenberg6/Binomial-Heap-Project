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
        HeapNode currentNode1 = this.last.next;
        HeapNode currentNode2 = heap2.last.next;
        boolean notInsertedCarry = false;
        boolean handled1 = false;
        boolean handled2 = false;
        int currentRank1, currentRank2;
        HeapNode carry = new HeapNode();
        while (!handled1 && !handled2) {
            currentRank1 = currentNode1.rank;
            currentRank2 = currentNode2.rank;
            if (notInsertedCarry) {
                if (currentRank1 == currentRank2) {
                    handled1 = currentNode1 == this.last;
                    handled2 = currentNode2 == heap2.last;
                    newHeap.appendNode(carry);
                    carry = link(currentNode1, currentNode2);
                    currentNode1 = currentNode1.next;
                    currentNode2 = currentNode2.next;
                } else {
                    if (currentRank1 < currentRank2) {
                        handled2 = currentNode2 == heap2.last;
                        if (carry.rank == currentRank1) {
                            carry = link(carry, currentNode1);
                        } else {
                            newHeap.appendNode(carry);
                            notInsertedCarry = false;
                        }
                        currentNode1 = currentNode1.next;
                    } else {  // rank2 < rank1
                        handled2 = currentNode2 == heap2.last;
                        if (carry.rank == currentRank2) {
                            carry = link(carry, currentNode2);
                        } else {
                            newHeap.appendNode(carry);
                            notInsertedCarry = false;
                        }
                        currentNode2 = currentNode2.next;
                    }
                }
            } else { // doesnt have not inserted carry
                if (currentRank1 == currentRank2) {
                    handled1 = currentNode1 == this.last;
                    handled2 = currentNode2 == heap2.last;
                    carry = link(currentNode1, currentNode2);
                    currentNode1 = currentNode1.next;
                    currentNode2 = currentNode2.next;
                    notInsertedCarry = true;
                } else {
                    if (currentRank1 < currentRank2) {
                        handled1 = currentNode1 == this.last;
                        newHeap.appendNode(currentNode1);
                        currentNode1 = currentNode1.next;
                    } else {
                        handled2 = currentNode2 == heap2.last;
                        newHeap.appendNode(currentNode2);
                        currentNode2 = currentNode2.next;
                    }

                }
            }
        }
        HeapNode longerNode = !handled1 ? currentNode1 : currentNode2;
        BinomialHeap longerHeap = !handled1 ? this : heap2;
        boolean handledLonger = false;
        if (notInsertedCarry) {
            if (handled1 && handled2) {  // same max rank
                if(newHeap.last == null){
                    newHeap = new BinomialHeap(carry);
                }else {
                    newHeap.appendNode(carry);
                }
            } else {
                while (!handledLonger && carry.rank == longerNode.rank) { //longerNode != longerHeap.last
                    carry = link(carry, longerNode);
                    longerNode = longerNode.next;
                    handledLonger = longerNode != longerHeap.last;
                }
                newHeap.appendNode(carry);
            }
        } else if (handled1 && handled2) {
            longerNode = currentNode1.rank > currentNode2.rank ? currentNode1 : currentNode2;
            longerHeap = currentNode1.rank > currentNode2.rank ? this : heap2;
            HeapNode shorterNode = currentNode1.rank < currentNode2.rank ? currentNode1 : currentNode2;
            if (currentNode1.rank == currentNode2.rank) {
                HeapNode linkedNode = link(currentNode1, currentNode2);
                newHeap = new BinomialHeap(linkedNode);
            } else {
                newHeap = new BinomialHeap(shorterNode);
            }
        }
        else if (currentNode1.rank != currentNode2.rank) {
            connect(newHeap, longerNode, longerHeap);
        }

        this.last = newHeap.last;
        this.min = newHeap.min;

        //todo min!

    }

//    public void linkNeededCheck(BinomialHeap longerHeap, HeapNode longerNode, BinomialHeap newHeap) {
//        HeapNode linkedNode = newHeap.last;
//        while (longerNode != longerHeap.last && longerNode.rank == linkedNode.rank) {  // not is last and same rank
//            linkedNode = link(linkedNode, longerNode);
//            longerNode = longerNode.next;
//        }
//        overrideLast(linkedNode, newHeap);
//
//    }

    public void connect(BinomialHeap heap, HeapNode node, BinomialHeap biggerRankHeap) {
        HeapNode first = heap.last.next;
        heap.last.next = node;
        heap.last = biggerRankHeap.last;
        heap.last.next = first;
    }

    public void appendNode(HeapNode node) {  // append to non-empty heap at end of linked list
        if (this.last == null) // is empty heap
        {
            BinomialHeap temp = new BinomialHeap(node);
            this.last = temp.last;
            this.size = temp.size;
            this.min = temp.min;
//            this.last.next = this.last;
        } else {
            HeapNode first = this.last.next;  // save first node
            this.last.next = node;
            this.last = this.last.next;
            this.last.next = first;
        }
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

        public HeapNode() {
            this.item = null;
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
