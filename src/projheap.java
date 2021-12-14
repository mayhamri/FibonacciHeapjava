public class projheap {
    public static void main(String [] args){
        int k = 10;
        int m = (int) Math.pow(2,k);
        long start = System.currentTimeMillis();
        FibonacciHeap myheap = new FibonacciHeap();
        FibonacciHeap.HeapNode [] mynodes = new FibonacciHeap.HeapNode[m];
        for( int i = m-1; i > -1; i--){
            FibonacciHeap.HeapNode newNode = myheap.insert(i);
            mynodes[i] = newNode;
        }
        myheap.insert(-1);
        myheap.deleteMin();
        int d =(int)(Math.log(m)/Math.log(2));
        for(int i = d; i>0 ; i --){
            int num = m-(int)Math.pow(2,i)+1;
            myheap.decreaseKey(mynodes[num],m+1);
        }
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("time: "+timeElapsed);
        System.out.println("link: "+FibonacciHeap.totalLinks());
        System.out.println("cuts: "+FibonacciHeap.totalCuts());
        System.out.println("potenial: "+myheap.potential());
    }
}
