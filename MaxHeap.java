/* MaxHeap.java
 * 
 * Author: Wayne Snyder
 * Edited by: cynthia chan (ccynthia@bu.edu)
 * Date: 4/29/15
 * Purpose: Max heap for Nodes. Each Nodes contains an
 *          Article and cosine similarity value.
 * Bugs: This program does not account for heap underflow.
 */ 

class MaxHeap {
   
   private final int SIZE = 10;       // initial length of array ZA
   private int next = 0;              // limit of elements in array
   private Node[] A = new Node[SIZE];   // implements tree by storing elements in level order
   
   // to access next
   public int getNext() {
        return this.next;
    }
      
   // Node class
   public static class Node {
      public Article data;
      public double cosSim;
      
      public Node(Article data, double cosSim, Node n) {
         this.data = data;
         this.cosSim = cosSim;
      }
      
      public Node(Article data, double cosSim)
      {
        this.data = data;
        this.cosSim = cosSim;
      }

   } // end Node class
   
   
   // standard resize to avoid overflow
   
   private void resize() {
      Node[] B = new Node[A.length*2];
      for(int i = 0; i < A.length; ++i)
         B[i] = A[i];
      A = B; 
   }
   
   // methods to move up and down tree as array
      // represent index of the Nodes
   private int parent(int i) { return (i-1) / 2; }
   private int lchild(int i) { return 2 * i + 1; }
   private int rchild(int i) { return 2 * i + 2; }
   
   private boolean isLeaf(int i) { return (lchild(i) >= next); }
   private boolean isRoot(int i) { return i == 0; }
   
   // standard swap, using indices in array
   private void swap(int i, int j) {
      Node temp = A[i];
      A[i] = A[j];
      A[j] = temp;
   }
   
   // basic data structure methods
   
   public boolean isEmpty() {
      return (next == 0);
   }
   
   public int size() {
      return (next);
   }
   
   // insert an integer into array at next available location
   //    and fix any violations of heap property on path up to root
   
   public void insert(Article k, double cosSim) {
     
      if(size() == A.length) resize(); 
      A[next] = new Node(k, cosSim); 
      
      int i = next;
      int p = parent(i); 
      while(!isRoot(i) && A[i].cosSim > A[p].cosSim) {
         swap(i,p);
         i = p;
         p = parent(i); 
      }
      
      ++next;
   }
   
   
   // Remove top (maximum) element, and replace with last element in level
   //    order; fix any violations of heap property on a path downwards
   
   public Node getMax() {
      --next;
      swap(0,next);                // swap root with last element
      int i = 0;                   // i is location of new key as it moves down tree
 
      // while there is a maximum child and element out of order, swap with max child
      int mc = maxChild(i); 
      while(!isLeaf(i) && A[i].cosSim < A[mc].cosSim) { 
         swap(i,mc);
         i = mc; 
         mc = maxChild(i);
      }
      
 ///     printHeapAsTree(); 
      
      return A[next];
   }
   
   // return index of maximum child of i or -1 if i is a leaf node (no children)
   
   int maxChild(int i) {
      if(lchild(i) >= next)
         return -1;
      if(rchild(i) >= next)
         return lchild(i);
      else if(A[lchild(i)].cosSim > A[rchild(i)].cosSim)
         return lchild(i);
      else
         return rchild(i); 
   }
   
   // Apply heapsort to the array A. To use, fill A with keys and then call heapsort
   
   public  void heapSort() {
      next = 0;
      for(int i = 0; i < A.length; ++i)      // turn A into a heap
         insert(A[i].data, A[i].cosSim);
      for(int i = 0; i < A.length; ++i)      // delete root A.length times, which swaps max into
         getMax();                           //  right side of the array
   }
   
   // debug methods   
   private void printHeap() {
      for(int i = 0; i < A.length; ++i)
         System.out.print(A[i] + " ");
      System.out.println("\t next = " + next);
   }
   
   private void printHeapAsTree() {
      printHeapTreeHelper(0, ""); 
      System.out.println(); 
   }
   
   private void printHeapTreeHelper(int i, String indent) {
      if(i < next) {
         
         printHeapTreeHelper(rchild(i), indent + "   "); 
         System.out.println(indent + "[" + A[i].data.getTitle() + " | " + A[i].cosSim + "]");
         printHeapTreeHelper(lchild(i), indent + "   "); 
      }
   }
   
   // Unit Test   
   public static  void main(String [] args) {
      
      MaxHeap H = new MaxHeap(); 
      
      Article dummyArticle = new Article("","");
      double dummyCos = 0;
      Node dummyNode = new Node(dummyArticle, dummyCos);
      Node[] S = {dummyNode, dummyNode, dummyNode, dummyNode, dummyNode, dummyNode};
      
      Article apple = new Article("apple", "apple are good to eat");
      S[0] = new Node(apple, 4);
      Article what = new Article("what", "wat r u doin");
      S[1] = new Node(what, 3);
      Article mayday = new Article("mayday", "mayday mayday we are alive");
      S[2] = new Node(mayday, 5);
      Article keyboard = new Article("keyboard", "my keyboard has food all over it");
      S[3] = new Node(keyboard, 8);
      Article no = new Article("no", "NO!");
      S[4] = new Node(no, 4);
      Article oh = new Article("oh", "oh, that sucks.");
      S[5] = new Node(oh, 1);      
      System.out.println("Inserting: [apple | 4], [what | 3], [mayday | 5], [keyboard | 8], [no | 4], [oh | 1], \n");
      for(int i = 0; i < S.length; ++i) {
         H.insert(S[i].data, S[i].cosSim);
         H.printHeapAsTree(); 
      }       
      
      
      System.out.println("getMax() and print out, until empty:\n");
      while(!H.isEmpty()) {
        System.out.println("Max: " + H.getMax().data.getTitle());
        H.printHeapAsTree(); 
      }
    
    Article yo = new Article("yo", "yo yo what's up");
    Article sweater = new Article("sweater", "it's sweater weather!");
    Article water = new Article("water", "Remember to stay hydrated, even during the hard winter months!");
    Article keychain = new Article("keychain", "it goes jingle jingle");
    Article ugh = new Article("ugh", "example of use: Ugh, I've spend like 35 hours on the assignment");
    Article cs112 = new Article("cs112", "a difficult class");
    Article potato = new Article("potato", "french fries, hash browns, mashed potatos, many other things");
    Article wow = new Article("wow", "wow I am tired");
    Article friday = new Article("friday", "today is friday. I am having no fun");
    Article flo = new Article("flo", "Hi! Flo here, from Progressive!");
    Article money = new Article("money", "something that I am lacking in");
    Article sneakers = new Article("sneakers", "Better than high heels and sandals");
    Node[] B = {dummyNode, dummyNode, dummyNode, dummyNode, dummyNode, dummyNode, 
      dummyNode, dummyNode, dummyNode, dummyNode, dummyNode, dummyNode};
    B[0] = new Node(yo, 3);
    B[1] = new Node(sweater, 6);
    B[2] = new Node(water, 2);
    B[3] = new Node(keychain, 8);
    B[4] = new Node(ugh, 5);
    B[5] = new Node(cs112, 23);
    B[6] = new Node(potato, -2);
    B[7] = new Node(wow, 6);
    B[8] = new Node(friday, 9);
    B[9] = new Node(flo, 4);
    B[10] = new Node(money, 8);
    B[11] = new Node(sneakers, 12);
    H.A = B;
    H.heapSort();
    System.out.println("Print out all values sorted smallest to largest in cosine similarity:");
    for(int i = 0; i < H.A.length; ++i)
      System.out.print("[" + H.A[i].data.getTitle() + " | " + H.A[i].cosSim + "] "); 
    System.out.println();
    
      
   }
}