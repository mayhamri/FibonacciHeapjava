import javafx.scene.transform.Rotate;

/**
 *
 * AVLTree
 *
 * An implementation of aמ AVL Tree with
 * distinct integer keys and info.
 *
 */

public class AVLTree {
    private IAVLNode root;
    private IAVLNode min;
    private IAVLNode max;


    public AVLTree(){
        this.root = null;
        this.min = null;
        this.max = null;

    }



    /**
     * public boolean empty()
     *
     * Returns true if and only if the tree is empty.
     *O(1)
     */

    public boolean empty() {
        return this.root == null ;
    }



    /**
     * updates the minimum after insertion
     * O(1)
     */
    private void UpdateMinInsert(IAVLNode newNode){
        if(this.min == null){
            this.min = newNode;
        }
        if (newNode.getKey() < this.min.getKey()){//checks if the newnode is smaller than current min
            this.min = newNode;
        }

    }

    /**
     * updates the maximum after insertion
     * O(1)
     */
    private void UpdateMaxInsert(IAVLNode newNode){
        if(this.max == null){
            this.max = newNode;
        }
        if (newNode.getKey() > this.max.getKey()){//checks if the newnode is bigger than current max
            this.max = newNode;
        }

    }

    /** gets a key . returns the node that has this key in case that the key exists in the tree. else, returns the node that we
     * should insert this key as their son.
     *
     *O(logn)
     */
    private IAVLNode getNodeBykey( int key){
        IAVLNode p = this.root;
        IAVLNode y = null;
        while (p != null){ //search for k O(logn)
            if (  (p.getValue()!= null)){ //make sure p not virtual
                y = p;
                if ( key == p.getKey()){//found it
                    return p;
                }
                else if(key < p.getKey()){ //goleft
                    p = p.getLeft();
                }
                else{
                    p = p.getRight();//go right
                }
            }
            else{break;}

        }
        return y;
    }
    /**
     * public String search(int k)
     *
     * Returns the info of an item with key k if it exists in the tree.
     * otherwise, returns null.
     * getting a result from getnodebykey. if k != key, node doesnt exist and we return null.
     * O(log n)
     */
    public String search(int k)
    {
        IAVLNode node = getNodeBykey(k);//gets the node that k is it's key. O(log n)
        if( node == null){ //checks if the tree is empty
            return null;
        }
        if ( node.getKey() == k){//checks if k in the tree(only if the keys are equal)
            return node.getValue();
        }

        return null;
    }

    /**
     * gets the new node after Insertion, and updates the size of the nodes in the tree that needs to be update.
     * O(logn)
     */
    private void UpdateParentsSize(IAVLNode node){
        while(node != null){//goes up to the root
            node.UpdateSize(); //node.size = node.size +1
            node= node.getParent();
        }

    }

    /**
     * check where to insert; then check if father is leaf or not ; then if a leaf check what type of rebalncing.
     *
     * public int insert(int k, String i)
     *
     * Inserts an item with key k and info i to the AVL tree.
     * The tree must remain valid, i.e. keep its invariants.
     * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
     * Returns -1 if an item with key k already exists in the tree.
     * O(log n)
     */
    public int insert(int k, String i) {
        IAVLNode father = getNodeBykey(k); //gets the node that should be the father
        if ( father == null){ //the tree was empty
            IAVLNode x = new AVLNode(i,k);
            CreateVirtualSonLeft(x);
            CreateVirtualSonRight(x);
            this.root = x;
            this.UpdateMinInsert(x);
            this.UpdateMaxInsert(x);
            this.root.UpdateSize();
            return 0;
        }
        else if(father.getKey() == k){//make sure k is not a key in the tree.
            return -1;
        }
        int numofop = 0;
        if (!isAleaf(father)){ //if father is not a leaf , needs to insert father is not a leaf.
            InsertIfFatherIsNotALeaf(father,i,k); //O(log n) . inserts the new node.

        }

        else{ //father is a leaf!
            IAVLNode x = new AVLNode(i,k); //the new node we insert
            CreateVirtualSonRight(x);
            CreateVirtualSonLeft(x);
            x.setParent(father);

            if (k > father.getKey()){ //rightson
                father.setRight(x);
            }
            else{
                father.setLeft(x);//leftson
            }
            this.UpdateParentsSize(x); //O(log n).
            father.setHeight(father.getHeight()+1); //promote father
            numofop +=1;//the promote
            boolean isbalanced = false;//indicates whether the tree got balanced
            while (!isbalanced){ //while the tree is not balanced. O(log n) WC.
                if(father.getParent() == null){//we got to the root
                    isbalanced = true;
                    break;
                }
                if ((father.getParent().getHeight()- father.getHeight())>0){//the tree is balanced.
                    isbalanced = true;
                }
                else{ //tree is still not balanced.
                    int type = FatherLeafType(father); // type of balance we need to do.O(1)
                    if ( type ==1 ){
                        RebalanceCase1(father);//O(1)
                        numofop +=1;
                        father = father.getParent();//continue the check
                    }
                    else if ( type == 2){
                        numofop +=2;
                        RebalanceCase2(father); //O(1)
                        isbalanced = true; //finished ! the tree is balanced
                    }
                    else if( type ==3){
                        numofop +=5;
                        RebalanceCase33(father);//O(1)
                        isbalanced = true;// finished ! the tree is balanced
                    }
                }



            }
            this.UpdateMaxInsert(x);//updates min O(1)
            this.UpdateMinInsert(x);//updated max O(1)

        }

        return numofop;

    }


    /**
     * in cade of an insrtion that the father is not a leaf , this function inserts the new IAVLnode.
     * O(log n)
     */
    private void InsertIfFatherIsNotALeaf(IAVLNode father, String i, int k){
        IAVLNode x = new AVLNode(i,k); //creates the new node
        x.setParent(father);
        CreateVirtualSonLeft(x);
        CreateVirtualSonRight(x);
        if (k > father.getKey()){//x is the new right son
            father.setRight(x);
        }
        else{//x is the new left son
            father.setLeft(x);
        }
        this.UpdateMaxInsert(x);//updates max O(1)
        this.UpdateMinInsert(x);//updates min O(1)
        this.UpdateParentsSize(x); //updates size O(log n)

    }

    /**
     * gets here only!! if there is a problem and one of the diffrences iz zero;
     * returns what type of rebalancing we need to do.
     * O(1)
     */
    private int FatherLeafType(IAVLNode father){
        IAVLNode grandfather = father.getParent();
        int leftdiffrence =grandfather.getHeight()- grandfather.getLeft().getHeight();//gets left BF
        int rightdiffrence= grandfather.getHeight()- grandfather.getRight().getHeight();// gets right BF
        if (( (rightdiffrence ==1) && (leftdiffrence == 0)) || ((rightdiffrence == 0) && (leftdiffrence == 1))){ // BF IS (0,1)(1,0)
            return 1;
        }
        if(IsRightSon(grandfather,father)){
            if ((father.getHeight() - father.getRight().getHeight())==2){
                return 3; // BF (0,2)(2,1) up to symmetric
            }
        }
        else {
            if((father.getHeight() - father.getLeft().getHeight())==2){
                return 3;// BF (0,2)(2,1) up to symmetric
            }
        }
        return 2;// BF (0,2)(1,2) up to symmetric

    }

    /**
     *
     * returns true if son is a right son of father.
     * O(1)
     */
    private boolean IsRightSon(IAVLNode father, IAVLNode son){
        return (father.getKey() < son.getKey());
    }

    /**
     * THIS FUNCTION IS REBALNCING WHEN AFTER PROMOTIONG WE GOT INTO CASE ONE
     *O(1)
     */
    private void RebalanceCase1 (IAVLNode father){

        father.getParent().setHeight(father.getParent().getHeight()+1); // promotes father
    }

    /**
     *
     * THIS FUNCTION IS REBALNCING WHEN AFTER PROMOTIONG WE GOT INTO CASE 2
     * O(1)
     */
    private void RebalanceCase2 ( IAVLNode father){
        father.getParent().setHeight(father.getParent().getHeight()-1); // demote father
        Rotate(father.getParent(),father); // rotates

    }
    /**
     *
     * THIS FUNCTION IS REBALNCING WHEN AFTER PROMOTIONG WE GOT INTO CASE 3
     * O(1)
     */
    private void RebalanceCase33 ( IAVLNode father){
        int fatherH = father.getHeight();
        int grandfH = father.getParent().getHeight();
        father.setHeight(fatherH-1); //demote father
        father.getParent().setHeight(grandfH-1);//demote granfather
        if (( fatherH - father.getRight().getHeight()) == 1){//right son has BF 1
            father.getRight().setHeight(father.getRight().getHeight()+1);//promote son
            Rotate(father,father.getRight());// first rotate
            Rotate(father.getParent().getParent(),father.getParent());//second rotate
        }
        else { //left son has BF 1
            father.getLeft().setHeight(father.getLeft().getHeight()+1);//promote son
            Rotate(father,father.getLeft());//first rotate
            Rotate(father.getParent().getParent(),father.getParent());//second rotate
        }
    }

    /**
     * ROTATE FATHER AND SON. updates the new size after rotation.
      son
     O(1)
     */
    private void Rotate(IAVLNode father,IAVLNode son){ //checks if the rotate should be right or left and rotate.
        if (father.getRight() == son){//left rotate
            father.setRight(son.getLeft());
            son.setLeft(father);
            IAVLNode grandf = father.getParent();
            if (grandf!= null){ //father is not the root . updates the grandf to be son's parent.
                if (grandf.getKey() > son.getKey()){
                    grandf.setLeft(son);
                }
                else{
                    grandf.setRight(son);
                }
                son.setParent(grandf);
                father.setParent(son);
                father.getRight().setParent(father);
            }
            else{ // father was the root. updates son to be the new root
                this.root = son;
                son.setParent(null);
                father.setParent(son);
                father.getRight().setParent(father);
            }


        }
        else{//rightrotate . same as left .
            father.setLeft(son.getRight());
            son.setRight(father);
            IAVLNode grandf = father.getParent();
            if (grandf!= null){
                if (grandf.getKey() > son.getKey()){
                    grandf.setLeft(son);
                }
                else{
                    grandf.setRight(son);
                }
                son.setParent(grandf);
                father.setParent(son);
                father.getLeft().setParent(father);
            }
            else{
                this.root = son;
                son.setParent(null);
                father.setParent(son);
                father.getLeft().setParent(father);
            }

        }
        son.UpdateSize(father.getSize()); // updates son size to be the previous father size
        father.UpdateSize(father.getLeft().getSize()+father.getRight().getSize()+1); // updates father's size to be his new size.
    }
    /**
     *static function that get a node and returns true if this node is a leaf and else false;
     * O(1)
     */
    private static boolean isAleaf(IAVLNode node){
        if(node == null){
            return false;
        }
        if((node.getRight()==null)||(node.getLeft()==null)){
            return false;
        }
        return (!node.getRight().isRealNode())&&(!node.getLeft().isRealNode());
    }

    /**
     *
     * gets A IAVLnode and sets his right son to be a virtual leaf.
     * O(1) complexity
     */
    private void CreateVirtualSonRight(IAVLNode node){
        IAVLNode son = new AVLNode(null,-1);
        son.setParent(node);
        son.setHeight(-1);
        node.setRight(son);
    }

    /**
     *
     * gets A IAVLnode and sets his left son to be a virtual leaf.
     * O(1) complexity
     */
    private void CreateVirtualSonLeft(IAVLNode node){
        IAVLNode son = new AVLNode(null,-1);
        son.setHeight(-1);
        son.setParent(node);
        node.setLeft(son);
    }
    /**
     * public int delete(int k)
     *
     * Deletes an item with key k from the binary tree, if it is there.
     * The tree must remain valid, i.e. keep its invariants.
     * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
     * Returns -1 if an item with key k was not found in the tree.
     * O(log n)
     */
    public int delete(int k)
    {
        IAVLNode delete = getNodeBykey(k);
        if( delete == null){ //makes sure the tree is not empty
            return -1;
        }
        if (delete.getKey() != k){ //makes sure that theres a node that his key is k in this tree.
            return -1;
        }
        int numofop = 0;
        boolean changed = false; //indicites whether we deleted the successor or not.
        IAVLNode succsesor = Successor(delete);
        IAVLNode backupdelete = delete; //makes sure that we have a backup of the deleted node.
        IAVLNode father = delete.getParent();
        if ((delete.getLeft().isRealNode())&&(delete.getRight().isRealNode())){ //checks if the node has 2 sons, if so replace it with its successor
            delete = succsesor; //now we want to delete the successor.
            father = delete.getParent();
            changed = true;
        }
        if (isAleaf(delete)){
            if (father == null){ //checks if the tree is empty
                this.root = null;
                this.max = null;
                this.min = null;
            }
            else{//deletes node - makes it virtual son.
                if(IsRightSon(father,delete)){
                    CreateVirtualSonRight(father);
                }
                else{
                    CreateVirtualSonLeft(father);
                }
            }

        }
        else{//delete is unary
            if (father == null){ //checks if the tree is empty
                if (delete.getRight().isRealNode()){ //delets has only right son
                    this.root = delete.getRight();
                    root.setParent(null);
                    this.min = root;
                    this.max = root;
                }
                else{//delete onlt has left son
                    this.root = delete.getLeft();
                    root.setParent(null);
                    this.min = root;
                    this.max = root;
                }
            }
            else{
                //deletes the unary node.
                if(IsRightSon(father,delete)){ // delete is a right son
                    if (delete.getRight().isRealNode()){ //right son , right son
                        father.setRight(delete.getRight());
                        delete.getRight().setParent(father);
                    }
                    else{ //right son . left son
                        father.setRight(delete.getLeft());
                        delete.getLeft().setParent(father);
                    }
                }
                else{ // left son , right son
                    if (delete.getRight().isRealNode()){
                        father.setLeft(delete.getRight());
                        delete.getRight().setParent(father);
                    }
                    else{ // left son , left son
                        father.setLeft(delete.getLeft());
                        delete.getLeft().setParent(father);
                    }

                }

            }

        }
        if(changed){ //replace delete with successor
            succsesor.setRight(backupdelete.getRight());
            succsesor.setLeft(backupdelete.getLeft());
            succsesor.getRight().setParent(succsesor);
            succsesor.getLeft().setParent(succsesor);
            succsesor.setHeight(backupdelete.getHeight());
            succsesor.UpdateSize(backupdelete.getSize());
            if(backupdelete.getParent()!=null){
                if(IsRightSon(backupdelete.getParent(),backupdelete)){
                    backupdelete.getParent().setRight(succsesor);
                    succsesor.setParent(backupdelete.getParent());
                }
                else{
                    backupdelete.getParent().setLeft(succsesor);
                    succsesor.setParent(backupdelete.getParent());
                }
            }
            else{ //backupdelete perant is null
                succsesor.setParent(null);
                this.root = succsesor;
                this.root.setHeight(backupdelete.getHeight());
                this.root.UpdateSize(backupdelete.getSize());
            }
            if(father == backupdelete){
                father = succsesor;
            }

        }
        if ( father == null){//finished.
            return numofop;
        }
        DecreaseSizeParents(father);//updates the size of the parents after the deletion.
        int type = CheckCaseAfterDelete(father); //returns which type of rebalanceing we need to do. O(1).
        while ( type != 0 ){//while the tree is not balanced. O(logn) iterations WC
            if ( type ==1){
                father.setHeight(father.getHeight()-1); //demote
                father = father.getParent();//continue the check
                numofop+=1;
                if (father == null){ //if we got to root - break.
                    break;
                }
                type =CheckCaseAfterDelete(father);//updates new type
            }
            else{
                IAVLNode son ; //checks which son has the BF 1.
                if(father.getHeight()-father.getRight().getHeight() == 1){
                    son = father.getRight();
                }
                else{
                    son = father.getLeft();
                }

                if ( type == 2){ //rebalance after type 2
                    Rotate(father,son);
                    father.setHeight(father.getHeight()-1);//demote
                    son.setHeight(son.getHeight()+1);//promote
                    numofop +=3;
                    break; //in this case, the tree is balanced.
                }
                else if( type == 3){ //rebalance after type 3
                    Rotate(father,son);
                    father.setHeight(father.getHeight()-2); // 2 demote
                    numofop +=2;
                    father = father.getParent().getParent();//continue the check
                    if ( father == null){ // break if we got to the root
                        break;
                    }
                    type = CheckCaseAfterDelete(father);//checks the new type
                }
                else if ( type ==4){//rebalance after type 4
                    IAVLNode grandson; //checks which grandSon has the 1 BF .
                    if ( son.getHeight()-son.getRight().getHeight() == 1){
                        grandson = son.getRight();
                    }
                    else{
                        grandson = son.getLeft();
                    }
                    Rotate(son,grandson);
                    Rotate(father,grandson);

                    father.setHeight(father.getHeight()-2);//2 demotes
                    son.setHeight(son.getHeight()-1);//1 demote
                    grandson.setHeight(grandson.getHeight()+1);//promote
                    father = grandson.getParent();//continue the check
                    numofop += 5;
                    if (father == null){ // break if we got to the root
                        break;
                    }
                    type = CheckCaseAfterDelete(father);//checks the new type
                }
            }
        }
        this.updateMinAfterDelete(); //update min O(logn)
        this.updateMaxAfterDelete();//update max o(log n)
        return numofop;
    }

    /**
     * updates min after deletion
     * O(log n )
     */

    private void updateMinAfterDelete(){
        IAVLNode x = this.root;
        if(x == null){//checks if thr tree is empty
            this.min = null;
        }
        else{// tree is not empty
            while(x.getLeft().isRealNode()){//go left to the min
                x = x.getLeft();
            }
            this.min = x;//updates min
        }

    }

    /**
     * updates max after deletion
     * O(log n )
     */
    private void updateMaxAfterDelete(){
        IAVLNode x = this.root;
        if(x == null){ //checks if thr tree is empty
            this.max = null;
        }
        else{ // tree is not empty
            while(x.getRight().isRealNode()){ //go right to the max
                x = x.getRight();
            }
            this.max = x; //updates max

        }

    }


    /**
     * returns which type of problem we have. if 0 - no problem. if 1 - case 1 (2,2) , if 2 - case 2((3,1)(1,1))
     * , if 3 - case 3((3,1)(2,1)), if 4 - case 4 ((3,1)(1,2))
     * O(1)
     */
    private int CheckCaseAfterDelete(IAVLNode father){
        int leftdif = father.getHeight()-father.getLeft().getHeight();//left BF
        int rightdif = father.getHeight()-father.getRight().getHeight();//rightBF
        if ( (rightdif ==2)&&(leftdif ==2)){ //BF(2,2) up to symmetric
            return 1;
        }
        if((leftdif ==3)&&(rightdif==1)){ //BF(3,1) up to symmetric
            IAVLNode son = father.getRight();
            int leftsondif = son.getHeight()-son.getLeft().getHeight();
            int rightsondif = son.getHeight() - son.getRight().getHeight();
            if ( (leftsondif == 1) &&(rightsondif == 1)){ //(1,1) up to symmetric
                return 2;
            }
            else if((rightsondif ==1)&&(leftsondif==2)){//(1,2) up to symmetric
                return 3;
            }
            else if((leftsondif ==1)&&(rightsondif ==2)){//(2,1) up to symmetric
                return 4;
            }

        }
        if ((rightdif ==3 )&&(leftdif ==1)){ //symmetric to the prev condition
            IAVLNode son = father.getLeft();
            int leftsondif = son.getHeight()-son.getLeft().getHeight();
            int rightsondif = son.getHeight() - son.getRight().getHeight();
            if((leftsondif ==1) && (rightsondif == 1)){
                return 2;
            }
            if((leftsondif == 1) && (rightsondif == 2)){
                return 3;
            }
             if((rightsondif == 1) && (leftsondif == 2)){
                return 4;
            }
        }
        return 0 ; //we didnt find any problem.
    }
    /**
     * updats the size in the path to the root after deletion node node.
     * O(log n )
     */
    private void DecreaseSizeParents(IAVLNode node){
        IAVLNode x = node;
        while (x!= null){ //O(log n)
            x.DecreaseSize(); //x.size = x.size -1
            x = x.getParent(); //goes up to the root
        }
    }






    /**
     * finds the successor of the node node.
     * O(log n)
     *
     */
    private IAVLNode Successor(IAVLNode node){
        if(this.max == node){ //no successor
            return null;
        }
        if(node.getRight().isRealNode()){//checks if this node has a right son .
            IAVLNode p = node;
            p = p.getRight();
            while(p.getLeft().isRealNode()){//O(log n)
                p = p.getLeft();
            }
            return p;

        }
        IAVLNode y = node.getParent();
        while(y!=null) {//node doesnt have a right son.  O(log n )
            if (node == y.getRight()) {
                node = y;
                y = node.getParent();
            }
            else{
                break;
            }
        }
        return y;
    }
    /**
     * public String min()
     *
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty.
     * O(1)
     */
    public String min()
    {
        return this.min.getValue();
    }

    /**
     * public String max()
     *
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty.
     * O(1)
     */
    public String max()
    {
      return this.max.getValue();
    }

    /**
     * public int[] keysToArray()
     *
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     * O(nlogn)
     */
    public int[] keysToArray()
    {
        if(this.root.getSize() == 0){ //checks if the tree is empty
            return new int[0];
        }
        return KeysToArrayHelp(this.root);//O(nlogn)
    }

    /**
     * help recursive function to keys to array
     *O( n log n)
     */
    private int[] KeysToArrayHelp(IAVLNode root){
        if (root.isRealNode()){
            int [] smaller = KeysToArrayHelp(root.getLeft());
            int [] larger = KeysToArrayHelp(root.getRight());
            int [] total = new int[smaller.length+larger.length+1];
            for (int i = 0 ; i < smaller.length ; i ++){//insert the smaller part to the array
                total[i] = smaller[i];
            }
            total[smaller.length]= root.getKey();//insert current root key to the array
            for ( int i=0; i < larger.length ; i ++){//insert the bigger part to the array
                total[i+1+ smaller.length] = larger[i];
            }
            return total;
        }
        return new int[0];//root is not a real node, so we return empty array.
    }

    /**
     * public String[] infoToArray()
     *
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     * O(nlogn)
     */
    public String[] infoToArray()
    {
        if(this.root.getSize() == 0){ //checks if the tree is not empty
            return new String[0];
        }
        return infoToArrayHelp(this.root); //O(nlogn)
    }

    /**
     * help recursive function to info to array
     * O(nlogn)
     */
    private String[] infoToArrayHelp(IAVLNode root){
        if (root.isRealNode()){
            String [] smaller = infoToArrayHelp(root.getLeft());
            String [] larger = infoToArrayHelp(root.getRight());
            String [] total = new String[smaller.length+larger.length+1];
            for (int i = 0 ; i < smaller.length ; i ++){ //insert the smaller part to the array
                total[i] = smaller[i];
            }
            total[smaller.length]= root.getValue(); //insert current root value to the array
            for ( int i=0; i < larger.length ; i ++){
                total[i+1+ smaller.length] = larger[i];//insert the bigger part to the array
            }
            return total;
        }
        return new String[0]; //root is not a real node, so we return empty array.
    }

    /**
     * public int size()
     *
     * Returns the number of nodes in the tree.
     * O(1)
     */
    public int size()
    {
        if ( root == null){ //checks if tree is empty
            return 0;
        }
        return this.root.getSize(); //tree not empty
    }

    /**
     * public int getRoot()
     *
     * Returns the root AVL node, or null if the tree is empty
     * O(1)
     */
    public IAVLNode getRoot()
    {
        return this.root;
    }

    /**
     * public AVLTree[] split(int x)
     *
     * splits the tree into 2 trees according to the key x.
     * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
     *
     * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
     * postcondition: none
     * O(log n )
     */
    public AVLTree[] split(int x)
    {
        IAVLNode splitter = getNodeBykey(x); //gets the node that x is it's key. O(log n)
        AVLTree t2 = new AVLTree();//creates the new bigger tree
        AVLTree t1 = new AVLTree(); //creates the new smaller tree
        if (splitter.getRight().isRealNode()){ //makes the right son to be the root of the bigger tree.O(1)
            t2.root = splitter.getRight();
        }
        if(splitter.getLeft().isRealNode()){ //makes the left son to be the root of the smaller tree.O(1)
            t1.root = splitter.getLeft();
        }
        IAVLNode father = splitter.getParent();
        while(father!= null){ //O(log n)
            if(father.getRight() == splitter){ //fathers left tree  is in the smaller tree.
                AVLTree lefttree = new AVLTree();
                lefttree.root = father.getLeft();
                lefttree.root.setParent(null);
                IAVLNode copyfather = new AVLNode(father.getValue(), father.getKey());
                t1.join(copyfather,lefttree);
            }
            else{ // father right tree is in the bigger tree.
                AVLTree righttree = new AVLTree();
                righttree.root = father.getRight();
                righttree.root.setParent(null);
                IAVLNode copyfather = new AVLNode(father.getValue(), father.getKey());
                t2.join(copyfather,righttree);
            }
            father = father.getParent();
            splitter = splitter.getParent();
        }
        t1.updateMaxAfterDelete(); //O(log n)
        t1.updateMinAfterDelete(); // O(log n)
        t2.updateMaxAfterDelete();//O(log n)
        t2.updateMinAfterDelete();//O(logn)
        AVLTree [] res = new AVLTree[2];
        res[0] = t1;
        res[1] = t2;
        return res;


    }

    /**
     * public int join(IAVLNode x, AVLTree t)
     *
     * joins t and x with the tree.
     * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
     *
     * precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
     * postcondition: none
     * O(logn)
     */
    public int join(IAVLNode x, AVLTree t)
    {

        if ((this.root == null)&&(t.root == null)){  // checks if the trees are empty
            this.root =x; // creates new tree that x is its root and it's only node.
            this.min = x;
            this.max = x;
            x.setHeight(1);
            x.UpdateSize(1);
            CreateVirtualSonLeft(x);//O(1)
            CreateVirtualSonRight(x);//O(1)
            return 1;
        }
        if (t.root == null){ // checks if the other tree is empty
            int k = x.getKey();
            String i = x.getValue();
            this.insert(k,i); // insert x to my tree. O(log n )
            return this.root.getHeight()+1;
        }

        if(this.root == null){ // checks if this tree is empty
            this.root = t.getRoot(); // makes this tree to be the other tree, and then insert x.
            this.max = t.max;
            this.min = t.min;
            int k = x.getKey();
            String i = x.getValue();
            this.insert(k,i);
            return t.root.getHeight()+1;

        }
        int res = Math.abs(this.root.getHeight()-t.root.getHeight())+1; // calculates the return value .
        if (this.root.getHeight() == t.root.getHeight()){ // checks if the trees has the same height.
            x.setHeight(this.root.getHeight() +1);// update new height for x
            if(this.root.getKey() >x.getKey()){ // checks which tree has the larger keys
                x.setRight(this.root);
                x.setLeft(t.root);
                x.setParent(null);
                this.root.setParent(x);
                t.root.setParent(x);
                this.root =x; // makes x to be the root and then addd both trees to be his left and right sons.
                this.min = t.min;
                x.UpdateSize(x.getRight().getSize() + x.getLeft().getSize() +1);
            }
            else{ //the other case when t has the larger keys. same as before , replace right and lest sons.
                x.setRight(t.root);
                x.setLeft(this.root);
                x.setParent(null);
                this.root.setParent(x);
                t.root.setParent(x);
                this.root =x;
                this.max = t.max;
                x.UpdateSize(x.getRight().getSize() + x.getLeft().getSize() +1);
            }
            return res;
        }
        if (this.root.getHeight() >t.root.getHeight()){// case that this tree is higher.
            if (this.root.getKey() > x.getKey()){ // case that this tree has the larger keys.
                this.min = t.min; // updates the min to be the new min
                joinWithOutRebalanceBiggerFirst(this.root,x,t.root); //O(logn)  help function to the case that the higher tree has bigger keys. gets the higher tree first.
            }
            else{
                this.max = t.max;// updates the max to be the new max
                joinWithOutRebalanceSmallerFirst(this.root,x,t.root); //O(logn) help function to the case that the higher tree has smaller  keys. gets the higher tree first.
            }
        }

        else{ // the higher tree is t
            if (t.root.getKey() > x.getKey()){
                this.max = t.max;// updates the max to be the new max
                joinWithOutRebalanceBiggerFirst(t.root,x,this.root);//O(logn)  help function to the case that the higher tree has bigger keys. gets the higher tree first.
            }
            else{
                this.min = t.min; // updates the min to be the new min
                joinWithOutRebalanceSmallerFirst(t.root,x,this.root); // O(logn) help function to the case that the higher tree has smaller  keys. gets the higher tree first.
            }

        }
        //the help functions joined the trees. (now we need to balance them and find the root.

        IAVLNode newRoot = x;
        while(newRoot.getParent()!= null){ // loop to find the new root of the joined tree
            newRoot = newRoot.getParent();
        }
        this.root = newRoot;

        this.RebalanceAfterJoin(x.getParent(),x); // O(log n)help function the rebalance the tree.
        return res;
    }


    /**
     * rebalancing the tree after  join
     * O(log n)
     */
    private void RebalanceAfterJoin(IAVLNode father, IAVLNode son){
        int leftDif = father.getHeight()-father.getLeft().getHeight();// gets left BF
        int rightDif = father.getHeight() - father.getRight().getHeight(); //gets right BF
        if ( (leftDif == 1) && (rightDif ==1 )){ // BF IS (1,1),  NO PROBLEM
            return;
        }
        else if( ((rightDif == 2) && (leftDif ==0))||((rightDif == 0 ) && (leftDif ==2))){ //CASE 2
            Rotate(father,son);//rotate
            son.setHeight(son.getHeight()+1); //promote son
            father = son;
        }
        else{
            father.setHeight(father.getHeight() +1);//promote
        }
        boolean isbalanced = false;

        while (!isbalanced){ //O(logn) WC
            if(father.getParent() == null){ //father is the root
                isbalanced = true;
                break;
            }
            if ((father.getParent().getHeight()- father.getHeight())>0){ //balanced no problem
                isbalanced = true;
            }
            else{
                int type = FatherLeafType(father);
                if ( type ==1 ){
                    RebalanceCase1(father);

                    father = father.getParent();
                }
                else if ( type == 2){

                    RebalanceCase2(father);
                    isbalanced = true; //problem solved
                }
                else if( type ==3){

                    RebalanceCase33(father);
                    isbalanced = true; //problem solved
                }
            }



        }


    }

    /**
     * updates the sizes up to the root after joining 2 trees into 1 .
     * O(log n)
     * @param x
     */
    private static void  updateSizeAfterJoin(IAVLNode x){
        IAVLNode c = x.getParent();
        x.UpdateSize(x.getLeft().getSize() + x.getRight().getSize()+1);
        while(c!= null){ // updates the size from x up to the root.
            c.UpdateSize(c.getRight().getSize() + c.getLeft().getSize()+1);
            c = c.getParent();
        }
    }


    /**
     * does the join part before the rebalncing in case that the higher tree has the bigger keys.
     * O(log n)
     */
    private static void joinWithOutRebalanceBiggerFirst(IAVLNode t2,IAVLNode x, IAVLNode t1){// t2 is always higher than t1
        int h = t1.getHeight();
        IAVLNode a = t1;
        IAVLNode b = t2;
        x.setHeight(h+1);//updates new height for x
        while ( b.getHeight() > h ){ //O(log n) , finds the first node that his height is <= than the other's tree height.
            b= b.getLeft();
        }
        x.setRight(b);
        x.setLeft(a);
        IAVLNode c = b.getParent();
        a.setParent(x);
        b.setParent(x);
        x.setParent(c);
        c.setLeft(x);
        x.setHeight(h+1);
        updateSizeAfterJoin(x);//O(log n). updates the size after the trees are joined.

    }


    /**
     * does the join part before the rebalncing in case that the higher tree has the smaller keys.
     * O(log n)
     */
    private static void joinWithOutRebalanceSmallerFirst(IAVLNode t1,IAVLNode x, IAVLNode t2){// t1 is always higher than t2
        int h = t2.getHeight();
        IAVLNode a = t2;
        IAVLNode b = t1;
        x.setHeight(h+1);//updates new height for x
        while ( b.getHeight() > h){ //O(log n) , finds the first node that his height is <= than the other's tree height.
            b= b.getRight();
        }
        x.setLeft(b);
        x.setRight(a);
        IAVLNode c = b.getParent();
        b.setParent(x);
        a.setParent(x);
        x.setParent(c);
        c.setRight(x);
        x.setHeight(h+1);
        updateSizeAfterJoin(x);//O(log n). updates the size after the trees are joined.
    }




    /**
     * public interface IAVLNode
     * ! Do not delete or modify this - otherwise all tests will fail !
     */
    public interface IAVLNode{
        public int getKey(); // Returns node's key (for virtual node return -1).
        public String getValue(); // Returns node's value [info], for virtual node returns null.
        public void setLeft(IAVLNode node); // Sets left child.
        public IAVLNode getLeft(); // Returns left child, if there is no left child returns null.
        public void setRight(IAVLNode node); // Sets right child.
        public IAVLNode getRight(); // Returns right child, if there is no right child return null.
        public void setParent(IAVLNode node); // Sets parent.
        public IAVLNode getParent(); // Returns the parent, if there is no parent return null.
        public boolean isRealNode(); // Returns True if this is a non-virtual AVL node.
        public void setHeight(int height); // Sets the height of the node.
        public int getHeight(); // Returns the height of the node (-1 for virtual nodes).
        public int getSize(); //Returns the size of the node (0 for virtual nodes).
        public void UpdateSize(); //Adds 1 to the size of the node.
        public void UpdateSize(int k); //updates size to be k.
        public  void  DecreaseSize(); //decrease size by1;
    }

    /**
     * public class AVLNode
     *
     * If you wish to implement classes other than AVLTree
     * (for example AVLNode), do it in this file, not in another file.
     *
     * This class can and MUST be modified (It must implement IAVLNode).
     */
    public static class AVLNode implements IAVLNode{
        private String info;
        private int key;
        private int rank;
        private IAVLNode right;
        private IAVLNode left;
        private IAVLNode parent;
        private int size;

        /**
         *
         *constructor to a new node with info and key.
         * O(1)
         */
        public AVLNode(String info, int key){
            this.info = info;
            this.key = key;
            this.rank = 0;
            this.right = null;
            this.left = null;
            this.parent = null;
            this.size =0;
        }

        /**
         *
         * @return the key of this node.
         * O(1)
         */
        public int getKey()
        {

            return this.key;
        }


        /**
         *
         * @return the value of this node.
         * O(1)
         */
        public String getValue()
        {

            return this.info;
        }

        /**
         * sets the left son to be this IAVLNODE
         * O(1)
         */
        public void setLeft(IAVLNode node)
        {

            this.left = node;
        }

        /**
         *
         * RETURNS the leftson. if doesnt exists - return null
         * O(1)
         */
        public IAVLNode getLeft()
        {

            return this.left;
        }

        /**
         * sets the right son to be this IAVLNODE
         * O(1)
         */
        public void setRight(IAVLNode node)
        {

            this.right = node;
        }

        /**
         *
         * RETURNS the righttson. if doesnt exists - return null
         * O(1)
         */
        public IAVLNode getRight()
        {

            return this.right;
        }

        /**
         * sets the parent to be this IAVLNODE
         * O(1)
         */
        public void setParent(IAVLNode node)
        {

            this.parent = node;
        }
        /**
         *
         * RETURNS the parent. if doesnt exists - return null
         * O(1)
         */
        public IAVLNode getParent()
        {
            return this.parent;
        }

        /**
         *
         * returns false if the node is a virtualson. else, returns true
         * O(1)
         */
        public boolean isRealNode()
        {
            return this.key != -1;
        }

        /**
         *change this rank to be height.
         * O(1)
         */
        public void setHeight(int height)
        {
            this.rank = height;
        }

        /**
         *
         * returns the height of this node(the height of this node is its rank)
         * O(1)
         *
         */
        public int getHeight()
        {
            return this.rank;
        }

        /**
         * returns the size of the tree that this node is it root.
         * O(1)
         */
        public int getSize(){
            return this.size;
        }
        /**
         * updates the size of a node in 1;
         * O(1)
         */
        public void UpdateSize(){
            this.size+=1;

        }
        /**
         * update size to be k
         * O(1)
         */
        public void UpdateSize(int k){
            this.size = k;
        }
        /**
         * decrease size by 1
         * O(1)
         */
        public  void  DecreaseSize(){
            this.size -=1;
        }

    }

}