package ph2;



public class AVL<K extends Comparable<K>, T> {

    private AVLNode<K,T> root;
    private AVLNode<K,T> curr;
    private int count;

    public AVL() {
        root = curr = null;
        count = 0;
    }

    public boolean empty() {
        return root == null;
    }

    public boolean full() {
        return false;
    }

    public int size() {
        return count;
    }

    public void clear() {
        root = curr = null;
        count = 0;
    }

    public T retrieve() {
        if (curr == null)
            return null;
        return curr.data;
    }

    public void update(T e) {
        if (curr != null)
            curr.data = e;
    }

    private T searchTree(AVLNode<K,T> node, K key) {
        if (node == null)
            return null;

        int cmp = key.compareTo(node.key);

        if (cmp == 0) {
            curr = node;
            return node.data;
        } else if (cmp < 0) {
            return searchTree(node.left, key);
        } else {
            return searchTree(node.right, key);
        }
    }

    public boolean findkey(K key) {
        T data = searchTree(root, key);
        return data != null;
    }

    

    private void updateBalance(AVLNode<K,T> node) {
        if (node == null) return;

        if (node.bf < -1 || node.bf > 1) {
            rebalance(node);
            return;
        }

        if (node.parent != null) {
            if (node == node.parent.left)
                node.parent.bf -= 1;
            if (node == node.parent.right)
                node.parent.bf += 1;

            if (node.parent.bf != 0)
                updateBalance(node.parent);
        }
    }

    private void rebalance(AVLNode<K,T> node) {
        if (node.bf > 0) {
            if (node.right != null && node.right.bf < 0) {
                rightRotate(node.right);
                leftRotate(node);
            } else {
                leftRotate(node);
            }
        } else if (node.bf < 0) {
            if (node.left != null && node.left.bf > 0) {
                leftRotate(node.left);
                rightRotate(node);
            } else {
                rightRotate(node);
            }
        }
    }

    private void leftRotate(AVLNode<K,T> x) {
        AVLNode<K,T> y = x.right;
        x.right = y.left;
        if (y.left != null)
            y.left.parent = x;

        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;

        x.bf = x.bf - 1 - Math.max(0, y.bf);
        y.bf = y.bf - 1 + Math.min(0, x.bf);
    }

    private void rightRotate(AVLNode<K,T> x) {
        AVLNode<K,T> y = x.left;
        x.left = y.right;
        if (y.right != null)
            y.right.parent = x;

        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;

        x.bf = x.bf + 1 - Math.min(0, y.bf);
        y.bf = y.bf + 1 + Math.max(0, x.bf);
    }

    public boolean insert(K key, T data) {
        AVLNode<K,T> node = new AVLNode<K,T>(key, data);

        AVLNode<K,T> p = null;
        AVLNode<K,T> current = this.root;

        while (current != null) {
                p = current;
                if (node.key.compareTo(current.key) ==0) {
                        return false;
                }else if (node.key.compareTo(current.key) <0 ) {
                        current = current.left;
                } else {
                        current = current.right;
                }
        }
        // p  is parent of current
        node.parent = p;
        if (p == null) {
                root = node;
                curr = node;
        } else if (node.key.compareTo(p.key) < 0 ) {
                p.left = node;
        } else {
                p.right = node;
        }
        count ++;

        //  re-balance the node if necessary
        updateBalance(node);
        return true;        
    }

    public boolean removeKey(K key) {
        K k1 = key;
        AVLNode<K,T> p = root;
        AVLNode<K,T> q = null;

        while (p != null) {
            int cmp = k1.compareTo(p.key);
            if (cmp < 0) {
                q = p;
                p = p.left;
            } else if (cmp > 0) {
                q = p;
                p = p.right;
            } else {
                if (p.left != null && p.right != null) {
                    AVLNode<K,T> min = p.right;
                    q = p;
                    while (min.left != null) {
                        q = min;
                        min = min.left;
                    }
                    p.key = min.key;
                    p.data = min.data;
                    k1 = min.key;
                    p = min;
                }

                if (p.left != null) {
                    p = p.left;
                } else {
                    p = p.right;
                }

                if (q == null) {
                    root = p;
                    if (p != null) {
                        p.parent = null;
                        updateBalance(p);
                    }
                } else {
                    if (k1.compareTo(q.key) < 0)
                        q.left = p;
                    else
                        q.right = p;
                    if (p != null)
                        p.parent = q;
                    updateBalance(q);
                }
                count--;
                curr = root;
                return true;
            }
        }
        return false;
    }
//==========================================================
    @Override
    public String toString() {
        String str = inOrdersTraversal(root);
        if (str.length() >= 2)
            str = str.replace(str.substring(str.length() - 2), "");
        return "{" + str + "}";
    }

    private String inOrdersTraversal(AVLNode<K, T> node) {
        if (node == null)
            return "";
        return inOrdersTraversal(node.left) + " " + node.data + "; " + inOrdersTraversal(node.right);
    }
    
    public void printKeys()
    {
        private_printKeys(root);
    }
    
    private void private_printKeys(AVLNode<K, T>  node)
    {
        if (node == null)
            return ;
        private_printKeys(node.left);
        System.out.println(node.key);
        private_printKeys(node.right);
        
    } 
    
    
    public void printKeys_Data()
    {
        private_printKeys_Data(root);
    }
    
    private void private_printKeys_Data(AVLNode<K, T>  node)
    {
        if (node == null)
            return ;
        private_printKeys_Data(node.left);
        System.out.print(node.key);
        System.out.println(node.data);
        System.out.println("");
        private_printKeys_Data(node.right);
        
    }
    
    
    public void printData()
    {
        private_printData(root);
    }
    
    private void private_printData(AVLNode<K, T>  node)
    {
        if (node == null)
            return ;
        private_printData(node.left);
        System.out.println(node.data);
        System.out.println("");
        private_printData(node.right);
        
    }
   

    public void printInOrder() {
        printInOrderRec(root);
    }

    private void printInOrderRec(AVLNode<K, T> node) {
        if (node == null) return;
        printInOrderRec(node.left);
        System.out.println(node.key + " : " + node.data);
        printInOrderRec(node.right);
    }

  //==========================================================  
 // return list of all data of the tree inorder traversal

    public LinkedList<T> inOrdertraverseData() {
        LinkedList<T> data = new LinkedList<T>();
        private_inOrdertraverseData(root, data);
        return data;
    }

    private void private_inOrdertraverseData(AVLNode<K, T> node, LinkedList<T> data) {
        if (node == null)
            return;
        private_inOrdertraverseData(node.left, data);
        data.insert(node.data);
        private_inOrdertraverseData(node.right, data);
    }

    ///=============================================
    // search for interval between two keys
    
    public LinkedList<T> intervalSearch(K k1, K k2) {
        LinkedList<T> q = new LinkedList<T>();
        if (root != null)
            rec_intervalSearch(k1, k2, root, q);
        return q;
    }

    private void rec_intervalSearch(K k1, K k2, AVLNode<K, T> p, LinkedList<T> q) {
        if (p == null)
            return;
        rec_intervalSearch(k1, k2, p.left, q);
        if (p.key.compareTo(k1) >= 0 && p.key.compareTo(k2) <= 0)
            q.insert(p.data);
        rec_intervalSearch(k1, k2, p.right, q);
    }

    
    public LinkedList<T> inOrderTraversal() {
        return inOrdertraverseData();
    }

    public LinkedList<T> rangeQuery(K k1, K k2) {
        return intervalSearch(k1, k2);
    }

    public LinkedList<K> keysInOrder() {
        LinkedList<K> list = new LinkedList<>();
        private_keysInOrder(root, list);
        return list;
    }

    private void private_keysInOrder(AVLNode<K,T> node, LinkedList<K> list) {
        if (node == null) return;
        private_keysInOrder(node.left, list);
        list.insert(node.key);
        private_keysInOrder(node.right, list);
    }
    
    
}
