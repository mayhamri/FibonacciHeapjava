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
            int n = (int)(1000*(Math.pow(2,i)));
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
        int n = 10;

    }


}
