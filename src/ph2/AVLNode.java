package ph2;

public class AVLNode<T> {
    public int key;
    public T data;
    public AVLNode<T> left, right;
    public int height;  // Height of the node for balance factor calculation
    
 
    public AVLNode(int k, T val) {
        key = k;
        data = val;
        left = right = null;
        height = 1;  // New node is initially at height 1
    }
    
   
    public AVLNode(int k, T val, AVLNode<T> l, AVLNode<T> r) {
        key = k;
        data = val;
        left = l;
        right = r;
        height = 1;  // Height will be updated after insertion
    }
}

