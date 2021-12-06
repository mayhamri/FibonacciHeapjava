import java.util.ArrayList;
import java.util.Collections;

public class testtt {
    public static void main(String[] args){
     AVLTree t1 = new AVLTree();
        int n = (int) (1000 * (Math.pow(2, 6)));
        //int n = 100;
        ArrayList<Integer> list = new ArrayList<Integer>();
        //int [] num = {85, 44, 54, 60, 32, 95, 83, 42, 1, 45, 59, 74, 56, 92, 12, 62, 81, 35, 7, 72, 18, 52, 4, 78, 14, 50, 89, 98, 53, 3, 46, 57, 22, 47, 88, 49, 65, 90, 31, 24, 97, 30, 28, 38, 68, 99, 73, 87, 13, 71, 2, 15, 84, 79, 39, 34, 27, 36, 26, 23, 67, 21, 48, 37, 55, 11, 66, 91, 9, 6, 5, 17, 70, 58, 29, 0, 75, 77, 93, 86, 96, 20, 82, 80, 10, 25, 40, 41, 64, 69, 61, 43, 19, 76, 94, 8, 51, 63, 16, 33};
        for (int j = 0; j < n; j++) {
            list.add(j);
        }
        Collections.shuffle(list);
        //System.out.println(list.toString());
        for (int j = 0; j < n; j++) {
            t1.insert(list.get(j), "tom");
        }
        System.out.println("before delte");
        t1.testSize(t1.getRoot());
        t1.testRanks(t1.getRoot());


        System.out.println("after delte");
        for (int j = 0 ; j < n ; j +=3){
            t1.delete(j);
        }
        t1.testRanks(t1.getRoot());
        t1.testSize(t1.getRoot());


//        int n= 12;
//        for (int i = 0 ; i <n; i++){
//            t1.insert(i,"ma");
//        }
//        AVLTree.printBinaryTree(t1.getRoot(),0);
//        for ( int i = 0 ; i < n ; i +=3 ){
//            t1.delete(i);
//        }
//
//
//
//





    }
}
