import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
public class projectAVL {
    public static int numOfPairs(ArrayList<Integer> arr){
        int sum = 0;
        for(int i = 0; i< arr.size() ; i ++){
            for (int j = 0 ; j< i ; j ++){
                if (arr.get(j)>arr.get(i)){
                    sum ++;
                }
            }
        }
        return sum;

    }

    public static int getDistFromMax(AVLTree t, AVLTree.IAVLNode node1){
        AVLTree.IAVLNode root = t.getRoot();
        AVLTree.IAVLNode max = t.getMaxIAVL();
        while(node1.getKey() >root.getKey() ){
            if((root.getRight() != null)&&(root.getRight().isRealNode())){
                root = root.getRight();
            }
            else{
                break;
            }
        }
        int path = BinarySearch(root,node1)+BinarySearch(root,max) +1 ;

        return path;
    }

    public static int BinarySearch(AVLTree.IAVLNode root, AVLTree.IAVLNode node1){
        int cnt = 0 ;
        while ( root!= node1){
            if(root.getKey()>node1.getKey()){
                cnt +=1;
                root = root.getLeft();
            }
            else{
                cnt +=1;
                root = root.getRight();
            }
        }
        return cnt;
    }

    public static void main(String[] args){
        AVLTree [] trees = new AVLTree[10];
        for( int i = 0;i < trees.length; i ++){
           trees[i] = new AVLTree();
        }

        AVLTree tr = new AVLTree();
        for ( int i = 0 ; i < 5 ; i ++ ){
            tr.insert(i , "maaaa ");
        }
        int lol = getDistFromMax(tr,tr.getNodeBykey(0));
        int lol2 = getDistFromMax(tr,tr.getNodeBykey(2));
        if ( (lol2!= 3)||(lol!=4)){
            System.out.println("erororrrrrrr");
        }


        for (int i =0 ; i < 5 ; i+=1 ){
            int n = (int)(1000*(Math.pow(2,i+1)));
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int j = 0; j < n ; j ++){
                list.add(j);
            }
            Collections.reverse(list);
            int firstnum = numOfPairs(list);
            trees[i].insert(list.get(0), "tom");
            int cnt= 0;
            for ( int j = 1; j< n ; j++ ) {
                AVLTree.IAVLNode node1 = trees[i].getNodeBykey(list.get(j));
                int num = getDistFromMax(trees[i],node1);
                cnt += num;
                trees[i].insert(list.get(j),"tom");
            }
            System.out.println("search cost first "+ cnt);




            Collections.shuffle(list);
            int secnum = numOfPairs(list);
            trees[i+5].insert(list.get(0), "tom");
            cnt= 0;
            for ( int j = 1; j< n ; j++ ) {
                AVLTree.IAVLNode node1 = trees[i+5].getNodeBykey(list.get(j));
                int num = getDistFromMax(trees[i+5],node1);
                cnt += num;
                trees[i+5].insert(list.get(j),"tom");
            }
            System.out.println("search cost second "+ cnt);
        }











    }


}
