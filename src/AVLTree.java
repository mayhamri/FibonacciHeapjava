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
     *
     */

    public boolean empty() {
        return (this.root.getSize() == 0); // to be replaced by student code
    }

    /** needs to be deleted
     *
     * @param root
     * @param level
     */
    public static void printBinaryTree(IAVLNode root, int level){
        if(root==null)
            return;
        printBinaryTree(root.getRight(), level+1);
        if(level!=0){
            for(int i=0;i<level-1;i++)
                System.out.print("|\t");
            System.out.println("|-------"+root.getKey());
        }
        else
            System.out.println(root.getKey());
        printBinaryTree(root.getLeft(), level+1);
    }

    /**
     * updates the minimum after insertion
     */
    public void UpdateMinInsert(IAVLNode newNode){
        if(this.min == null){
            this.min = newNode;
        }
        if (newNode.getKey() < this.min.getKey()){
            this.min = newNode;
        }

    }

    /**
     * updates the maxium after insertion
     */
    public void UpdateMaxInsert(IAVLNode newNode){
        if(this.max == null){
            this.max = newNode;
        }
        if (newNode.getKey() > this.max.getKey()){
            this.max = newNode;
        }

    }

    /** gets a key . returns the node that has this key in case that the key exists in the tree. else, returns the node that we
     * should insert this key as their son.
     *
     *
     */
    public IAVLNode getNodeBykey( int key){
        IAVLNode p = this.root;
        IAVLNode y = null;
        while (p != null){
            if (  (p.getValue()!= null)){
                y = p;
                if ( key == p.getKey()){
                    return p;
                }
                else if(key < p.getKey()){
                    p = p.getLeft();
                }
                else{
                    p = p.getRight();
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
     */
    public String search(int k)
    {
        IAVLNode node = getNodeBykey(k);
        if ( node.getKey() == k){
            return node.getValue();
        }

        return null;
    }

    /**
     * gets the new node after Insertion, and updates the size of the nodes in the tree that needs to be update.
     * @param node
     */
    public void UpdateParentsSize(IAVLNode node){
        while(node != null){
            node.UpdateSize();
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
     */
    public int insert(int k, String i) {
        IAVLNode father = getNodeBykey(k);
        if ( father == null){
            IAVLNode x = new AVLNode(i,k);
            CreateVirtualSonLeft(x);
            CreateVirtualSonRight(x);
            this.root = x;
            this.UpdateMinInsert(x);
            this.UpdateMaxInsert(x);
            this.root.UpdateSize();
            return 0;
        }
        else if(father.getKey() == k){
            return -1;
        }
        int numofop = 0;
        if (!isAleaf(father)){
            InsertIfFatherIsNotALeaf(father,i,k);

        }

        else{
            IAVLNode x = new AVLNode(i,k);
            CreateVirtualSonRight(x);
            CreateVirtualSonLeft(x);
            x.setParent(father);

            if (k > father.getKey()){
                father.setRight(x);
            }
            else{
                father.setLeft(x);
            }
            father.setHeight(father.getHeight()+1);
            numofop +=1;
            boolean isbalanced = false;
            while (!isbalanced){
                if(father.getParent() == null){
                    isbalanced = true;
                    break;
                }
                if ((father.getParent().getHeight()- father.getHeight())>0){
                    isbalanced = true;
                }
                else{
                    int type = FatherLeafType(father);
                    if ( type ==1 ){
                        RebalanceCase1(father);
                        numofop +=1;
                        father = father.getParent();
                    }
                    else if ( type == 2){
                        numofop +=2;
                        RebalanceCase2(father);
                        isbalanced = true;
                    }
                    else if( type ==3){
                        numofop +=5;
                        RebalanceCase33(father);
                        isbalanced = true;
                    }
                }



            }
            this.UpdateMaxInsert(x);
            this.UpdateMinInsert(x);
            this.UpdateParentsSize(x);
        }

        return numofop;

    }

    /**
     * in cade of an insrtion that the father is not a leaf , this function inserts the new IAVLnode.
     */
    public void InsertIfFatherIsNotALeaf(IAVLNode father, String i, int k){
        IAVLNode x = new AVLNode(i,k);
        x.setParent(father);
        CreateVirtualSonLeft(x);
        CreateVirtualSonRight(x);
        if (k > father.getKey()){
            father.setRight(x);
        }
        else{
            father.setLeft(x);
        }
        this.UpdateMaxInsert(x);
        this.UpdateMinInsert(x);
        this.UpdateParentsSize(x);

    }

    /**
     * gets here only!! if there is a problem and one of the diffrences iz zero;
     * returns what type of rebalancing we need to do.
     */
    public int FatherLeafType(IAVLNode father){
        IAVLNode grandfather = father.getParent();
        int leftdiffrence =grandfather.getHeight()- grandfather.getLeft().getHeight();
        int rightdiffrence= grandfather.getHeight()- grandfather.getRight().getHeight();
        if (( (rightdiffrence ==1)&(leftdiffrence == 0)) | ((rightdiffrence == 0)&(leftdiffrence == 1))){
            return 1;
        }
        if(IsRightSon(grandfather,father)){
            if ((father.getHeight() - father.getRight().getHeight())==2){
                return 3;
            }
        }
        else {
            if((father.getHeight() - father.getLeft().getHeight())==2){
                return 3;
            }
        }
        return 2;

    }

    /**
     *
     * returns true if son is a right son of father.
     */
    public boolean IsRightSon(IAVLNode father, IAVLNode son){
        if (father.getKey() > son.getKey()){
            return false;
        }
        return true;
    }

    /**
     * THIS FUNCTION IS REBALNCING WHEN AFTER PROMOTIONG WE GOT INTO CASE ONE
     *
     */
    public void RebalanceCase1 (IAVLNode father){

        father.getParent().setHeight(father.getParent().getHeight()+1);
    }

    /**
     *
     * THIS FUNCTION IS REBALNCING WHEN AFTER PROMOTIONG WE GOT INTO CASE 2
     */
    public void RebalanceCase2 ( IAVLNode father){
        father.getParent().setHeight(father.getParent().getHeight()-1);
        Rotate(father.getParent(),father);

    }
    /**
     *
     * THIS FUNCTION IS REBALNCING WHEN AFTER PROMOTIONG WE GOT INTO CASE 3
     */
    public void RebalanceCase33 ( IAVLNode father){
        int fatherH = father.getHeight();
        int grandfH = father.getParent().getHeight();
        father.setHeight(fatherH-1);
        father.getParent().setHeight(grandfH-1);
        if (( fatherH - father.getRight().getHeight()) == 1){
            father.getRight().setHeight(father.getRight().getHeight()+1);
            Rotate(father,father.getRight());
            Rotate(father.getParent().getParent(),father.getParent());
        }
        else {
            father.getLeft().setHeight(father.getLeft().getHeight()+1);
            Rotate(father,father.getLeft());
            Rotate(father.getParent().getParent(),father.getParent());
        }
    }

    /**
     * ROTATE FATHER AND SON. updates the new size after rotation.
      son
     */
    public void Rotate(IAVLNode father,IAVLNode son){
        if (father.getRight() == son){//left rotate
            father.setRight(son.getLeft());
            son.setLeft(father);
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
            }
            else{
                this.root = son;
                son.setParent(null);
                father.setParent(son);
            }


        }
        else{//rightrotate
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
            }
            else{
                this.root = son;
                son.setParent(null);
                father.setParent(son);
            }

        }
        son.UpdateSize(father.getSize());
        father.UpdateSize(father.getLeft().getSize()+father.getRight().getSize()+1);
    }
    /**
     *static function that get a node and returns true if this node is a leaf and else false;
     */
    public static boolean isAleaf(IAVLNode node){
        if ((node.getLeft().getValue() == null)&(node.getRight().getValue()==null)){
            return true;
        }
        return false;
    }

    /**
     *
     * gets A IAVLnode and sets his right son to be a virtual leaf.
     */
    public void CreateVirtualSonRight(IAVLNode node){
        IAVLNode son = new AVLNode(null,-1);
        son.setParent(node);
        son.setHeight(-1);
        node.setRight(son);
    }

    /**
     *
     * gets A IAVLnode and sets his left son to be a virtual leaf.
     */
    public void CreateVirtualSonLeft(IAVLNode node){
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
     */
    public int delete(int k)
    {
        IAVLNode delete = getNodeBykey(k);
        if (delete.getKey() != k){
            return -1;
        }
        int numofop = 0;
        boolean changed = false;
        IAVLNode succsesor = Successor(delete);
        IAVLNode backupdelete = delete;
        IAVLNode father = delete.getParent();
        if ((delete.getLeft().isRealNode())&(delete.getRight().isRealNode())){
            delete = succsesor;
            father = delete.getParent();
            changed = true;
        }
         else if (isAleaf(delete)){
            if (father == null){
                this.root = null;
                this.max = null;
                this.min = null;
            }
            else{
                if(IsRightSon(father,delete)){
                    CreateVirtualSonRight(father);
                }
                else{
                    CreateVirtualSonLeft(father);
                }
            }

        }
        else{
            if (father == null){
                if (delete.getRight().isRealNode()){
                    this.root = delete.getRight();
                    root.setParent(null);
                    this.min = root;
                    this.max = root;
                }
                else{
                    this.root = delete.getLeft();
                    root.setParent(null);
                    this.min = root;
                    this.max = root;
                }
            }
            else{
                if (delete.getRight().isRealNode()){
                    father.setRight(delete.getRight());
                    delete.getRight().setParent(father);
                }
                else{
                    father.setLeft(delete.getLeft());
                    delete.getLeft().setParent(father);
                }
            }

        }
        if(changed){
            succsesor.setRight(backupdelete.getRight());
            succsesor.setLeft(backupdelete.getLeft());
            succsesor.getRight().setParent(succsesor);
            succsesor.getLeft().setParent(succsesor);
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
            else{
                succsesor.setParent(null);
                this.root = succsesor;
            }

        }
        if ( father == null){
            return numofop;
        }
        DecreaseSizeParents(father);
        int type = CheckCaseAfterDelete(father);
        while ( type != 0 ){
            if ( type ==1){
                father.setHeight(father.getHeight()-1);
                father = father.getParent();
                numofop+=1;
                if (father == null){
                    break;
                }
                type =CheckCaseAfterDelete(father);
            }
            else{
                IAVLNode son ;
                if(father.getHeight()-father.getRight().getHeight() == 1){
                    son = father.getRight();
                }
                else{
                    son = father.getLeft();
                }

                if ( type == 2){
                    Rotate(father,son);
                    son.UpdateSize(father.getSize());
                    father.UpdateSize(father.getLeft().getSize() + father.getRight().getSize()+1);
                    father.setHeight(father.getHeight()-1);
                    son.setHeight(son.getHeight()+1);
                    numofop +=3;
                    break;
                }
                if( type == 3){
                    Rotate(father,son);
                    son.UpdateSize(father.getSize());
                    father.UpdateSize(father.getLeft().getSize() + father.getRight().getSize()+1);
                    father.setHeight(father.getHeight()-2);
                    numofop +=3;
                    father = father.getParent().getParent();
                    if ( father == null){
                        break;
                    }
                    type = CheckCaseAfterDelete(father);
                }
                if ( type ==4){
                    IAVLNode grandson;
                    if ( son.getHeight()-son.getRight().getHeight() == 1){
                        grandson = son.getRight();
                    }
                    else{
                        grandson = son.getLeft();
                    }
                    Rotate(son,grandson);
                    grandson.UpdateSize(son.getSize());
                    son.UpdateSize(son.getLeft().getSize() + son.getRight().getSize()+1);
                    Rotate(father,grandson);
                    grandson.UpdateSize(father.getSize());
                    father.UpdateSize(father.getLeft().getSize() + father.getRight().getSize()+1);
                    father.setHeight(father.getHeight()-2);
                    son.setHeight(son.getHeight()-1);
                    grandson.setHeight(grandson.getHeight()+1);
                    father = grandson.getParent();
                    numofop += 6;
                    if (father == null){
                        break;
                    }
                    type = CheckCaseAfterDelete(father);
                }
            }
        }
        this.updateMinAfterDelete();
        this.updateMaxAfterDelete();
        return numofop;
    }

    /**
     * updates min after deletion
     */

    public void updateMinAfterDelete(){
        IAVLNode x = this.root;
        while(x.getLeft().isRealNode()){
            x = x.getLeft();
        }
        this.min = x;
    }

    /**
     * updates max after deletion
     */
    public void updateMaxAfterDelete(){
        IAVLNode x = this.root;
        while(x.getRight().isRealNode()){
            x = x.getRight();
        }
        this.max = x;
    }


    /**
     * returns which type of problem we have. if 0 - no problem. if 1 - case 1 (2,2) , if 2 - case 2((3,1)(1,1))
     * , if 3 - case 3((3,1)(2,1)), if 4 - case 4 ((3,1)(1,2))
     * @param father
     * @return
     */
    public int CheckCaseAfterDelete(IAVLNode father){
        int leftdif = father.getHeight()-father.getLeft().getHeight();
        int rightdif = father.getHeight()-father.getRight().getHeight();
        if ( (rightdif ==2)&(leftdif ==2)){
            return 1;
        }
        if((leftdif ==3)&(rightdif==1)){
            IAVLNode son = father.getRight();
            int leftsondif = son.getHeight()-son.getLeft().getHeight();
            int rightsondif = son.getHeight() - son.getRight().getHeight();
            if ( (leftsondif == 1) &(rightsondif == 1)){
                return 2;
            }
            else if((rightsondif ==1)&(leftsondif==2)){
                return 3;
            }
            else if((leftsondif ==1)&(rightsondif ==2)){
                return 4;
            }

        }
        if ((rightdif ==3 )&(leftdif ==1)){
            IAVLNode son = father.getLeft();
            int leftsondif = son.getHeight()-son.getLeft().getHeight();
            int rightsondif = son.getHeight() - son.getRight().getHeight();
            if((leftsondif ==1) & (rightsondif == 1)){
                return 2;
            }
            if((leftsondif == 1) & (rightsondif == 2)){
                return 3;
            }
             if((rightsondif == 1) & (leftsondif == 2)){
                return 4;
            }
        }
        return 0 ;
    }
    /**
     * updats the size in the path to the root after deletion node node.
     * @param node
     */
    public void DecreaseSizeParents(IAVLNode node){
        IAVLNode x = node.getParent();
        while (x!= null){
            x.DecreaseSize();
            x = x.getParent();
        }
    }






    /**
     * finds the successor of the node node.
     * @param node
     * @return
     */
    public IAVLNode Successor(IAVLNode node){
        if(this.max == node){
            return null;
        }
        if(node.getRight().isRealNode()){
            IAVLNode p = node;
            p = p.getRight();
            while(p.getLeft().isRealNode()){
                p = p.getLeft();
            }
            return p;

        }
        IAVLNode y = node.getParent();
        while(y!=null) {
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
     */
    public String min()
    {
        return this.min.getValue(); // to be replaced by student code
    }

    /**
     * public String max()
     *
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty.
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
     */
    public int[] keysToArray()
    {
        if(this.root.getSize() == 0){
            return new int[0];
        }
        return KeysToArrayHelp(this.root);
    }

    public int[] KeysToArrayHelp(IAVLNode root){
        if (root.isRealNode()){
            int [] smaller = KeysToArrayHelp(root.getLeft());
            int [] larger = KeysToArrayHelp(root.getRight());
            int [] total = new int[smaller.length+larger.length+1];
            for (int i = 0 ; i < smaller.length ; i ++){
                total[i] = smaller[i];
            }
            total[smaller.length]= root.getKey();
            for ( int i=0; i < larger.length ; i ++){
                total[i+1+ smaller.length] = larger[i];
            }
            return total;
        }
        return new int[0];
    }

    /**
     * public String[] infoToArray()
     *
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray()
    {
        if(this.root.getSize() == 0){
            return new String[0];
        }
        return infoToArrayHelp(this.root);
    }

    public String[] infoToArrayHelp(IAVLNode root){
        if (root.isRealNode()){
            String [] smaller = infoToArrayHelp(root.getLeft());
            String [] larger = infoToArrayHelp(root.getRight());
            String [] total = new String[smaller.length+larger.length+1];
            for (int i = 0 ; i < smaller.length ; i ++){
                total[i] = smaller[i];
            }
            total[smaller.length]= root.getValue();
            for ( int i=0; i < larger.length ; i ++){
                total[i+1+ smaller.length] = larger[i];
            }
            return total;
        }
        return new String[0];
    }

    /**
     * public int size()
     *
     * Returns the number of nodes in the tree.
     */
    public int size()
    {
        if ( root == null){
            return 0;
        }
        return this.root.getSize(); // to be replaced by student code
    }

    /**
     * public int getRoot()
     *
     * Returns the root AVL node, or null if the tree is empty
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
     */
    public AVLTree[] split(int x)
    {
        return null;
    }

    /**
     * public int join(IAVLNode x, AVLTree t)
     *
     * joins t and x with the tree.
     * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
     *
     * precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
     * postcondition: none
     */
    public int join(IAVLNode x, AVLTree t)
    {
        return -1;
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
    public class AVLNode implements IAVLNode{
        private String info;
        private int key;
        private int rank;
        private IAVLNode right;
        private IAVLNode left;
        private IAVLNode parent;
        private int size;

        public AVLNode(String info, int key){
            this.info = info;
            this.key = key;
            this.rank = 0;
            this.right = null;
            this.left = null;
            this.parent = null;
            this.size =0;
        }

        public int getKey()
        {
            return this.key; // to be replaced by student code
        }


        public String getValue()
        {

            return this.info; // to be replaced by student code
        }

        public void setLeft(IAVLNode node)
        {

            this.left = node;
        }
        public IAVLNode getLeft()
        {
            return this.left; // to be replaced by student code
        }
        public void setRight(IAVLNode node)
        {
            this.right = node;
        }
        public IAVLNode getRight()
        {

            return this.right; // to be replaced by student code
        }
        public void setParent(IAVLNode node)
        {

            this.parent = node; // to be replaced by student code
        }
        public IAVLNode getParent()
        {
            return this.parent; // to be replaced by student code
        }
        public boolean isRealNode()
        {

            if ( this.key == -1){
                return false; // to be replaced by student code
            }
            return true;
        }

        public void setHeight(int height)
        {
            this.rank = height;
            // to be replaced by student code
        }
        public int getHeight()
        {
            return this.rank; // to be replaced by student code
        }

        public int getSize(){
            return this.size;
        }
        /**
         * updates the size of a node in 1;
         */
        public void UpdateSize(){
            this.size+=1;

        }
        /**
         * update size to be k
         */
        public void UpdateSize(int k){
            this.size = k;
        }
        /**
         * decrease size by 1
         */
        public  void  DecreaseSize(){
            this.size -=1;
        }

    }

}