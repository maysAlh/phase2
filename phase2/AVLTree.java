package phase2;

public class AVLTree<T> {

    AVLNode<T> root, current;

 
    public AVLTree() {
        root = current = null;
    }

  
    public boolean empty() {
        return root == null;
    }

  
    public boolean full() {
        return false;
    }

 
    public T retrieve() {
        if (current == null) return null;
        return current.data;
    }

   
    public boolean update(T data) {
        if (current == null) return false;
        int key = current.key;
        remove_key(key);
        return insert(key, data);
    }

  
    public boolean update(int key, T data) {
        remove_key(key);
        return insert(key, data);
    }

 
    public void deleteSubtree() {
        if (current == root) {
            current = root = null;
        } else {
            AVLNode<T> p = current;
            find(Relative.Parent);
            if (current.left == p)
                current.left = null;
            else
                current.right = null;

            current = root;
        }
    }

   
    public boolean find(Relative rel) {
        switch (rel) {
            case Root:
                current = root;
                return true;

            case Parent:
                if (current == root) return false;
                current = findparent(current, root);
                return true;

            case LeftChild:
                if (current.left == null) return false;
                current = current.left;
                return true;

            case RightChild:
                if (current.right == null) return false;
                current = current.right;
                return true;

            default:
                return false;
        }
    }

 
    private AVLNode<T> findparent(AVLNode<T> p, AVLNode<T> t) {
        if (t == null) return null;
        if (t.left == p || t.right == p) return t;

        AVLNode<T> q = findparent(p, t.left);
        if (q != null) return q;

        return findparent(p, t.right);
    }

    // ===================== HEIGHT AND BALANCE =====================

   
    private int getHeight(AVLNode<T> node) {
        return (node == null ? 0 : node.height);
    }

 
    private int getBalanceFactor(AVLNode<T> node) {
        if (node == null) return 0;
        return getHeight(node.left) - getHeight(node.right);
    }

   
    private void updateHeight(AVLNode<T> node) {
        if (node != null)
            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    // ===================== ROTATIONS =====================


    private AVLNode<T> rightRotate(AVLNode<T> y) {
        AVLNode<T> x = y.left;
        AVLNode<T> T2 = x.right;

        // Rotate
        x.right = y;
        y.left = T2;

        // Update heights
        updateHeight(y);
        updateHeight(x);

        return x;  // New subtree root
    }

  
    private AVLNode<T> leftRotate(AVLNode<T> x) {
        AVLNode<T> y = x.right;
        AVLNode<T> T2 = y.left;

        // Rotate
        y.left = x;
        x.right = T2;

        // Update heights
        updateHeight(x);
        updateHeight(y);

        return y; // New subtree root
    }

    // ===================== SEARCH =====================

 
    private boolean findkey(int key) {
        AVLNode<T> p = root, q = root;

        while (p != null) {
            q = p;
            if (p.key == key) {
                current = p;
                return true;
            } else if (key < p.key) {
                p = p.left;
            } else {
                p = p.right;
            }
        }
        current = q;
        return false;
    }

   
    public boolean find(int key) {
        return findkey(key);
    }

    // ===================== INSERT (AVL BALANCED) =====================

   
    public boolean insert(int k, T val) {
        if (findkey(k)) return false;  // Key already exists

        root = insertRecursive(root, k, val);
        findkey(k);
        return true;
    }

    private AVLNode<T> insertRecursive(AVLNode<T> node, int key, T value) {

     
        if (node == null) return new AVLNode<>(key, value);

        if (key < node.key)
            node.left = insertRecursive(node.left, key, value);
        else if (key > node.key)
            node.right = insertRecursive(node.right, key, value);
        else {
            node.data = value; 
            return node;
        }

        // Update height
        updateHeight(node);

        // Balance factor
        int balance = getBalanceFactor(node);

        // 4 imbalance cases
        if (balance > 1 && key < node.left.key)          // Left-Left
            return rightRotate(node);

        if (balance < -1 && key > node.right.key)        // Right-Right
            return leftRotate(node);

        if (balance > 1 && key > node.left.key) {        // Left-Right
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && key < node.right.key) {      // Right-Left
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    // ===================== DELETE (AVL BALANCED) =====================

  
    public boolean remove_key(int key) {
        BooleanWrapper removed = new BooleanWrapper(false);
        root = removeRecursive(root, key, removed);
        current = root;
        return removed.getValue();
    }

    public boolean delete(int key) {
        return remove_key(key);
    }

  
    private AVLNode<T> removeRecursive(AVLNode<T> node, int key, BooleanWrapper flag) {

        if (node == null) return null;

        if (key < node.key) {
            node.left = removeRecursive(node.left, key, flag);

        } else if (key > node.key) {
            node.right = removeRecursive(node.right, key, flag);

        } else {
            // Node found
            flag.setValue(true);

            // Node with one child or none
            if (node.left == null || node.right == null) {

                AVLNode<T> temp =
                        (node.left != null ? node.left : node.right);

                if (temp == null) {
                    temp = node;
                    node = null;
                } else {
                    node = temp;
                }

            } else {
              
                AVLNode<T> temp = findMin(node.right);
                node.key = temp.key;
                node.data = temp.data;
                node.right =
                        removeRecursive(node.right, temp.key, new BooleanWrapper(false));
            }
        }

        if (node == null) return null;

        // Update height
        updateHeight(node);

        // Rebalance
        int balance = getBalanceFactor(node);

        // 4 cases
        if (balance > 1 && getBalanceFactor(node.left) >= 0)   // LL
            return rightRotate(node);

        if (balance > 1 && getBalanceFactor(node.left) < 0) {  // LR
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && getBalanceFactor(node.right) <= 0) // RR
            return leftRotate(node);

        if (balance < -1 && getBalanceFactor(node.right) > 0) { // RL
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    /** Returns the minimum node in a subtree */
    private AVLNode<T> findMin(AVLNode<T> p) {
        while (p != null && p.left != null)
            p = p.left;
        return p;
    }

    /** Finds minimum value in tree */
    public T findMin() {
        AVLNode<T> node = findMin(root);
        return (node != null ? node.data : null);
    }

    /** Finds maximum value in tree */
    public T findMax() {
        AVLNode<T> node = root;
        if (node == null) return null;
        while (node.right != null)
            node = node.right;
        return node.data;
    }

    /** Count all nodes */
    public int getSize() {
        return countNodes(root);
    }

    private int countNodes(AVLNode<T> node) {
        if (node == null) return 0;
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    @Override
    public String toString() {
        return "AVLTree[size=" + getSize() +
                ", root=" + (root != null ? root.key : "null") + "]";
    }
}
