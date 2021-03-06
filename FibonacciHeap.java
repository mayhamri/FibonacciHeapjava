/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
    public HeapNode min;
    public HeapNode first;
    private int n;
    private static int numOfLinks =0;
    private static int numOfCascading =0;

    /**
     * constructor
     */
    public HeapNode(){
        this.min = null;
        this.first = null;
        this.n = 0;
    }
   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *   
    */
    public boolean isEmpty()
    {

        return this.n ==0 ;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    */
    public HeapNode insert(int key)
    {
        HeapNode newNode = new HeapNode(key);
        newNode.setNext(this.first);
        newNode.setPrev(first.prev);
        this.first.setPrev(newNode);
        newNode.prev.setNext(newNode);
        if (key < this.min.getKey()){
            min = newNode;
        }
        this.n +=1;
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
        HeapNode child = this.min.getChild();
        HeapNode lastChild = child.getPrev();
        if(min.getChild() == null){//min has no child
            if(first.getKey() == min.getKey()){//min is first in the heap
                if ( n>1){//heap has more than 1 tree
                    first = min.getNext();
                    min.getNext().setPrev(min.getPrev());
                    min.getPrev().setNext(min.getNext());
                }
                else{ //heap has only 1 tree- the min.
                    min = null;
                    first = null;
                }
            }
            else{//first is not the min
                min.getNext().setPrev(min.getPrev());
                min.getPrev().setNext(min.getNext());
            }
        }
        else{// the min has children
            if(first.getKey() == min.getKey()){//the first is the min
                first = min.getChild();
            }
            child.setPrev(min.getPrev());
            min.getPrev().setNext(child);
            lastChild.next = min.getNext();
            min.getNext().setPrev(lastChild);
            while(child.getKey()!= lastChild.getKey()){//goes over all children and updates parent to null
                child.setParent(null);
                child = child.getNext();
            }
            lastChild.setParent(null);

        }
        this.n -=1; //updates size
        this.successiveLinking();
        this.updateMin();//updates new min
    }

    private void updateMin(){
        HeapNode p = this.first;
        HeapNode curmin = this.first;
        int curminkey = this.first.key;
        while ( (p!= null) &&(p.getKey() != first.getKey())){
            if(curminkey > p.getKey()){
                curmin = p;
                curminkey = p.getKey()
            }
            p = p.getNext();
        }
        this.min = curmin;
    }

    /**
     * does successive linking to the heap
     */
    private void successiveLinking(){
        if (this.isEmpty()){
            return;
        }
        int k = floor(1.4404 * log(this.n))+1;//the maxismum rank of a tree
        HeapNode [] trees = new HeapNode[k];
        HeapNode p = first;
        trees[first.getRank()]=first;
        p = p.getNext();
        while(p.getKey() != first.getKey()){ //entres trees to array
            int k = p.getRank();//finds which cell is needed
            if (trees[k] == null){//empty cell - no other tree of rank k
                tree[k] = p; //enters p to the array
            }
            else{ //there is another tree in the needed cell
                HeapNode newroot = link(p,trees[k]); // links the trees to a tree of rank k+1;
                trees[k] = null; //updates the cell to be empty
                k +=1;
                while(trees[k] != null){//checks if the next cell is empty (k+1) , if not . countinue to link and check. until the needed cell is empty
                    newroot = link(newroot,trees[k]);
                    trees[k] = null;
                    k+=1;
                }
                trees[k] = newroot; //insert the tree to the needed cell
                p = p.getNext();

            }

            int index = 0;
            while(trees[index] == null){//finds the first tree
                index +=1;
            }
            first = trees[index];
            HeapNode lastseen = trees[index];
            for (int i = index+1 ; i <trees.length ; i++){//sets prev and next to the roots in the heap
                if(trees[i] != null){
                    trees[i].setPrev(lastseen);
                    lastseen.setNext(trees[i]);
                    lastseen = trees[i];
                }
            }

            lastseen.setNext(first);
            first.setPrev(lastseen);


        }


    }
    /**
     * links to trees of the same rank
     * @param a
     * @param b
     * @return
     */
    private HeapNode link(HeapNode a , HeapNode b){
        numOfLinks +=1;
        if(a.getKey()> b.getKey()){
            HeapNode tmp = a;
            a = b;
            b = tmp;
        }
        if(a.getChild() == null){
            a.setChild(b);
            b.getParent(a);
            b.setNext(b);
            b.setPrev(b);
        }
        else{
            b.setNext(a.getChild());
            b.setPrev(a.getChild().getPrev());
            b.getPrev().setNext(b);
            a.getChild().setPrev(b);
            a.setChild(b);
            b.setParent(a);
        }
        a.setRank(a.getRank()+1);
        return a;

    }
   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    */
    public HeapNode findMin()
    {
    	return this.min;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    */
    public void meld (FibonacciHeap heap2)
    {
        if(heap2.isEmpty()){ //THE SECOND HEAP IS EMPTY
            return; //do nothing
        }
        else if(this.isEmpty()){ // this heap is empty , makes it the other heap
            this.min = heap2.min;
            this.n = heap2.n;
            this.first = heap2.first;
        }

        else{ //both of the heaps are not empty!
            if(heap2.min < this.min){//updates min
                this.min = heap2.min
            }
            this.n += heap2.n; //updates n
            HeapNode lastMe = this.first.getPrev();
            HeapNode lastHeap2 = heap2.first.getPrev();
            this.first.setPrev(lastHeap2);
            lastHeap2.setNext(this.first);
            lastMe.setNext(heap2.first);
            heap2.first.setPrev(lastMe);

        }


    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *   
    */
    public int size()
    {
    	return this.n; // should be replaced by student code
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * Note: The size of of the array depends on the maximum order of a tree, and an empty heap returns an empty array.
    * 
    */
    public int[] countersRep()
    {
    	int[] arr = new int[100];
        return arr; //	 to be replaced by student code
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    */
    public void delete(HeapNode x) 
    {    
    	return; // should be replaced by student code
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	return; // should be replaced by student code
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * 
    * In words: The potential equals to the number of trees in the heap
    * plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {    
    	return -234; // should be replaced by student code
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    */
    public static int totalLinks()
    {    
    	return numOfLinks;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return -456; // should be replaced by student code
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H. 
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    
        int[] arr = new int[100];
        return arr; // should be replaced by student code
    }
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file. 
    *  
    */
    public static class HeapNode{

    	public int key;
        public int rank;
        public boolean marked;
        public HeapNode child;
        public HeapNode next;
        public HeapNode prev;
        public HeapNode parent;

       /**
        * constructor to a new node with key key
        * @param key
        */
       public HeapNode(int key) {
           this.key = key;
           this.rank =0;
           this.marked = false;
           this.prev = this;
           this.next = this;
       }

       /**\
        * sets marked
        */
       public void setMarked(boolean what) {
           this.marked = what;
       }

       /**
        * set child to be child
        * @param child
        */
        public void setChild(HeapNode child){
                this.child = child;
        }

       /**
        * sets the parent to be parent
        * @param parent
        */
       public void setParent(HeapNode parent){
           this.parent = parent;
       }

       /**
        * sets prev to be prev
        * @param prev
        */
       public void setPrev(HeapNode prev){
           this.prev = prev;
       }

       /**
        * sets next to be next
        * @param next
        */
       public void setNext(HeapNode next){
           this.next = next;
       }

       /**
        * sets rank to be k
        * @param k
        */
       public void setRank(int k){
            if ( k>0){
                this.rank = k;
            }
       }
       /**
        * return this.rank
        */
       public int getRank(){
            return this.rank;
       }

       /**
        * return this.child
        * @return
        */
       public HeapNode getChild() {
           return child;
       }

       /**
        * return this.next
        * @return
        */
       public HeapNode getNext() {
           return next;
       }

       /**
        * return this.parent
        * @return
        */
       public HeapNode getParent() {
           return parent;
       }


       /**
        * return this.prev
        * @return
        */
       public HeapNode getPrev() {
           return prev;
       }

       /**
        * reutrn is marked
        * @return
        */
       public boolean isMarked() {
           return marked;
       }

       /**
        * reutrn this.key
        * @return
        */

    	public int getKey() {
    		return this.key;
    	}
    }
}
