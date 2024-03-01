public class Main {
    public static void main(String[] args) {
        BinomialHeap h1 = new BinomialHeap();
        h1.insert(1,"1");
        h1.insert(5,"5");
        h1.insert(7,"7");
        h1.insert(8,"8");
        h1.printHeap();

        BinomialHeap h2 = new BinomialHeap();
        h2.insert(10,"10");
        h2.insert(2,"6");
        h2.insert(3,"2");
        h2.insert(4,"4");
        h2.printHeap();

        h1.meld(h2);
        h1.printHeap();
  


    }

}
