/**
 * BinomialHeap
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */


public class BinomialHeap {
    //todo - size
    //todo - rank?
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

    public int getMinKey() {
        return this.min.item.key;
    }

    //todo delete print functions submitting
    public void printHeap() {
        if (empty()) {
            System.out.println("Heap is empty");
            return;
        }
        System.out.println("Binomial Heap:");
        System.out.println("Min Key:" + this.getMinKey() + " Size:" + this.size());
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
        int origSize = this.size();
        // create new node and item
        HeapItem newItem = new HeapItem(key, info);
        BinomialHeap newHeap = new BinomialHeap(newItem);

        if (this.empty()) {
            //copy new heap to "this"
            this.size = newHeap.size;
            this.min = newHeap.min;
            this.last = newHeap.last;
        } else {
            meld(newHeap);
            this.size = origSize + 1;
        }
        return newItem;
    }

    /**
     * Delete the minimal item
     */
    public void deleteMin() {
        if (this.empty()) {
            return;
        }
        int origSize = this.size;
        if (this.min.rank == 0) {
            detachFirst();
            return;
        }

        //create new heap of min's children
        BinomialHeap newHeap = new BinomialHeap();
        newHeap.size = this.calcSizeByRank(this.min.rank);
        HeapNode newLast = this.min.child;
        newHeap.last = newLast;

        // find new min from children of min
        HeapNode current = this.min.child.next; //start current at first
        HeapNode minNode = newLast;
        while (current != newLast) {
            if (current.item.key < minNode.item.key) { // i deleted : && current != this.min
                minNode = current;
            }
            current = current.next;
        }
        newHeap.min = minNode;

        detachMinNode();
        meldAfterDelete(newHeap);

        this.size = origSize - 1;
    }

    /**
     * Detach the first node in the heap. Used only when deleteMin is called and min node's rank is 0.
     */
    private void detachFirst() {
        int origSize = this.size;
        if (this.size == 1) { // if this is the only node in the heap
            this.emptyHeap();
        } else {
            nullifyParentForChildrenOf(this.min);
            HeapNode secondNode = this.last.next.next;
            this.last.next = secondNode;
            this.min = findNewMin();
        }
        this.size = origSize - 1;
    }

    private void nullifyParentForChildrenOf(HeapNode node){
        if(node.child == null){
            return;
        }
        HeapNode lastChild = node.child;
        lastChild.parent = null;
        HeapNode current = lastChild.next;

        while (current!=lastChild){
            current.parent = null;
            current = current.next;
        }

    }

    /**
     * Return node with min key.
     */
    private HeapNode findNewMin() {
        HeapNode last = this.last;
        HeapNode current = this.last.next;
        HeapNode minNode = last;
        while (current != last) {
            if (current.item.key < minNode.item.key) {
                minNode = current;
            }
            current = current.next;
        }
        return minNode;
    }

    /**
     * Nullify all class fields
     */
    private void emptyHeap() {
        this.last = null;
        this.size = 0;
        this.min = null;
    }

    private void meldAfterDelete(BinomialHeap newHeap) {
        if (this.empty()) {
            this.last = newHeap.last;
            this.min = newHeap.min;
            this.size = newHeap.size;
        } else {
            meld(newHeap);
        }
    }

    /**
     * Delete min node from list of nodes (same concept as deletion in linked list).
     */
    private void detachMinNode() {
        //if the min node is the only node
        if (this.numTrees() == 1) {
            this.emptyHeap();
            return;
        }

        nullifyParentForChildrenOf(this.min);

        //get node before min node
        HeapNode current = this.last;
        while (current.next != this.min) {
            current = current.next;
        }

        //update last if min is last
        if (this.min == this.last) {
            this.last = current;
        }

        //detach min node (like in regular linked list)
        current.next = current.next.next;
        //update min - find new min for original heap
        this.min = this.findNewMin();
    }

    public int calcSizeByRank(int rank) {
        int sum = 0;
        for (int i = 0; i <= rank; i++) {
            sum += Math.pow(2, i);
        }
        return sum;
    }

    /**
     * Return the minimal HeapItem
     */
    public HeapItem findMin() {
        return this.min.item;
    }

    /**
     * pre: 0 < diff < item.key
     * <p>
     * Decrease the key of item by diff and fix the heap.
     */
    public void decreaseKey(HeapItem item, int diff) {
        HeapNode node = item.node;
        HeapNode parent = node.parent;
        //First check if item is present or not

        //Update the key of the item
        item.key -= diff;

        // If the node has a parent and nodes key is smaller than its parent's key, do heapify up
        while (parent != null && node.item.key < parent.item.key) {
            //Update pointers of the items to the right nodes
            item.node = parent;
            parent.item.node = node;

            //Update nodes pointers to the right items
            node.item = parent.item;
            parent.item = item;

            //Update node and its parent
            node = parent;
            item = node.item;
            //item = node.item;
            parent = node.parent;
        }
        //Update min if needed
        if (node.item.key < getMinKey()) {
            this.min = node;
        }

    }


    /**
     * Delete the item from the heap.
     */
    public void delete(HeapItem item) {
        decreaseKey(item, Integer.MIN_VALUE);
        deleteMin();
    }

    /**
     * Meld the heap with heap2
     */
    public void meld(BinomialHeap heap2) {
        if ((this.empty() && heap2.empty()) || heap2.empty()) {
            return;
        }
        if (this.empty()) {
            this.size = heap2.size;
            this.min = heap2.min;
            this.last = heap2.last;
            return;
        }
        this.size += heap2.size;
        HeapNode newMin = (this.getMinKey() < heap2.getMinKey()) ? this.min : heap2.min;
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
                    HeapNode nextCurrentNode1 = currentNode1.next; //BUG FIX - save the next current before link ruins them
                    HeapNode nextCurrentNode2 = currentNode2.next;
                    carry = link(currentNode1, currentNode2);
                    currentNode1 = nextCurrentNode1;
                    currentNode2 = nextCurrentNode2;
                } else {
                    if (currentRank1 < currentRank2) {
                        HeapNode nextCurrentNode1 = currentNode1.next; // BUG FIX
                        handled2 = currentNode2 == heap2.last;
                        if (carry.rank == currentRank1) {
                            carry = link(carry, currentNode1);
                        } else {
                            newHeap.appendNode(carry);
                            notInsertedCarry = false;
                        }
                        currentNode1 = nextCurrentNode1;
                    } else {  // rank2 < rank1
                        HeapNode nextCurrentNode2 = currentNode2.next; // BUG FIX
                        handled2 = currentNode2 == heap2.last;
                        if (carry.rank == currentRank2) {
                            carry = link(carry, currentNode2);
                        } else {
                            newHeap.appendNode(carry);
                            notInsertedCarry = false;
                        }
                        currentNode2 = nextCurrentNode2;
                    }
                }
            } else { // doesnt have not inserted carry
                if (currentRank1 == currentRank2) {
                    handled1 = currentNode1 == this.last;
                    handled2 = currentNode2 == heap2.last;
                    HeapNode nextCurrentNode1 = currentNode1.next;  //BUG FIX - save the next current before link ruins them
                    HeapNode nextCurrentNode2 = currentNode2.next;
                    carry = link(currentNode1, currentNode2);
                    currentNode1 = nextCurrentNode1;
                    currentNode2 = nextCurrentNode2;
                    notInsertedCarry = true;
                } else {
                    if (currentRank1 < currentRank2) {
                        handled1 = currentNode1 == this.last;
                        HeapNode nextNode = currentNode1.next;
                        newHeap.appendNode(currentNode1);
                        currentNode1 = nextNode;
                    } else {
                        handled2 = currentNode2 == heap2.last;
                        HeapNode nextNode = currentNode2.next;
                        newHeap.appendNode(currentNode2);
                        currentNode2 = nextNode;
                    }

                }
            }
        }
        HeapNode longerNode = !handled1 ? currentNode1 : currentNode2;
        BinomialHeap longerHeap = !handled1 ? this : heap2;
        boolean doNotConnect = false;
        boolean handledLonger = false;
        if (notInsertedCarry) {
            if (handled1 && handled2) {  // same max rank
                if (newHeap.last == null) {
                    newHeap = new BinomialHeap(carry);
                    doNotConnect = true;
                } else {
                    newHeap.appendNode(carry);
                }
            } else {
                while (!handledLonger && carry.rank == longerNode.rank) { //longerNode != longerHeap.last
                    handledLonger = longerNode == longerHeap.last;
                    HeapNode nextLongerNode = longerNode.next;
                    carry = link(carry, longerNode);
                    longerNode = nextLongerNode;
                }
                newHeap.appendNode(carry);
            }
        }
        if (newHeap.last.rank < longerNode.rank && !doNotConnect) {  //currentNode1.rank != currentNode2.rank - changed this because once carry was created, node1/node2 changed and the actual rank grew
            connect(newHeap, longerNode, longerHeap);
        }

        this.last = newHeap.last;
        this.min = newMin;
    }

    /**
     * Connect end of biggerRankHeap (starting at node) to shorterHeap
     */
    public void connect(BinomialHeap shorterHeap, HeapNode node, BinomialHeap biggerRankHeap) {
        HeapNode first = shorterHeap.last.next;
        shorterHeap.last.next = node;
        shorterHeap.last = biggerRankHeap.last;
        shorterHeap.last.next = first;
    }

    /**
     * Append node to a non-empty heap.
     */
    public void appendNode(HeapNode node) {
        if (this.last == null) // is empty heap
        {
            BinomialHeap temp = new BinomialHeap(node);
            this.last = temp.last;
            this.size = temp.size;
            this.min = temp.min;
            this.last.next = this.last;
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
        return this.size;
    }

    /**
     * The method returns true if and only if the heap
     * is empty.
     */
    public boolean empty() {
        return this.last == null;
    }

    /**
     * Return the number of trees in the heap.
     */
    public int numTrees() {
        if (this.empty()) {
            return 0;
        }
        int counter = 1;
        HeapNode current = this.last.next; // start counting from first node
        while (current != this.last) {
            counter++;
            current = current.next;
        }
        return counter;
    }

    /**
     * Link 2 heaps of the same size (as seen in class).
     */
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
