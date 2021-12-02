import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class projectAVL {
    public static void main(String[] args){
        AVLTree [] trees = new AVLTree[20];
        for( int i = 0;i < trees.length; i ++){
           trees[i] = new AVLTree();
        }


        for (int i =0 ; i < 10 ; i+=1 ){
            int n = (int)(1000*(Math.pow(2,i+1)));
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int j = 0; j < n ; j ++){
                list.add(j);
            }
            Collections.shuffle(list);
            for ( int j = 0; j< n ; j++ ){
                trees[i].insert(list.get(j),"tom" );
                trees[i+10].insert(list.get(j),"may" );
            }
        }
        //test BF
        for (int i = 0 ; i < 10 ; i ++){
            trees[i].testRanks(trees[i].getRoot());
        }


        int num = (int)(1000*(Math.pow(2,1)));
        AVLTree testtree = new AVLTree();
        ArrayList<Integer> list2 = new ArrayList<Integer>();
        for (int j = 0; j< num*2 ; j++){
            list2.add(j + 2 + num);
        }
        Collections.shuffle(list2);
        for ( int j = 0; j< num*2 ; j++){
            testtree.insert(list2.get(j),"tom" );
            testtree.insert(list2.get(j),"may" );
        }
        AVLTree.IAVLNode x = new AVLTree.AVLNode("LOL", num+1);

        AVLTree [] ans = testtree.split(4174);
        ans[0].testRanks(ans[0].getRoot());
        ans[1].testRanks(ans[1].getRoot());


      //  for (int i = 0 ; i < num ; i +=3){
        //    trees[0].delete(i);
      //  }
      //  System.out.println("all goof insert");
     //   trees[0].testRanks(trees[0].getRoot());
      //  AVLTree.IAVLNode p = trees[0].getNodeBykey(919);




    }


}
