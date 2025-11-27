package ph2;

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
        return (current == null ? null : current.data);
    }

    public boolean find(int key) {
        return findkey(key);
    }

    private boolean findkey(int tkey) {
        AVLNode<T> p = root, q = root;
        if (empty())
            return false;

        while (p != null) {
            q = p;
            if (p.key == tkey) {
                current = p;
                return true;
            } else if (tkey < p.key) {
                p = p.left;
            } else {
                p = p.right;
            }
        }
        current = q;
        return false;
    }

    public boolean insert(int k, T val) {
        if (findkey(k))
            return false;

        root = insertRecursive(root, k, val);
        findkey(k);
        return true;
    }

    private AVLNode<T> insertRecursive(AVLNode<T> node, int key, T value) {
        if (node == null)
            return new AVLNode<>(key, value);

        if (key < node.key)
            node.left = insertRecursive(node.left, key, value);
        else if (key > node.key)
            node.right = insertRecursive(node.right, key, value);
        else {
            node.data = value;
            return node;
        }

        updateHeight(node);

        int balance = getBalanceFactor(node);

        if (balance > 1 && key < node.left.key)
            return rightRotate(node);

        if (balance < -1 && key > node.right.key)
            return leftRotate(node);

        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    public boolean update(T data) {
        if (current == null) return false;
        current.data = data;
        return true;
    }

    public boolean update(int key, T data) {
        if (!findkey(key)) return false;
        current.data = data;
        return true;
    }

    public boolean remove_key(int key) {
        int before = getSize();
        root = removeRecursive(root, key);
        current = root;
        int after = getSize();
        return after < before;
    }

    public boolean delete(int key) {
        return remove_key(key);
    }

    private AVLNode<T> removeRecursive(AVLNode<T> node, int key) {
        if (node == null)
            return null;

        if (key < node.key) {
            node.left = removeRecursive(node.left, key);
        } else if (key > node.key) {
            node.right = removeRecursive(node.right, key);
        } else {
            if (node.left == null || node.right == null) {
                AVLNode<T> temp = (node.left != null) ? node.left : node.right;
                if (temp == null) {
                    node = null;
                } else {
                    node = temp;
                }
            } else {
                AVLNode<T> temp = findMinNode(node.right);
                node.key = temp.key;
                node.data = temp.data;
                node.right = removeRecursive(node.right, temp.key);
            }
        }

        if (node == null)
            return null;

        updateHeight(node);

        int balance = getBalanceFactor(node);

        if (balance > 1 && getBalanceFactor(node.left) >= 0)
            return rightRotate(node);

        if (balance > 1 && getBalanceFactor(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && getBalanceFactor(node.right) <= 0)
            return leftRotate(node);

        if (balance < -1 && getBalanceFactor(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    private AVLNode<T> findMinNode(AVLNode<T> node) {
        if (node == null) return null;
        while (node.left != null)
            node = node.left;
        return node;
    }

    public T findMin() {
        AVLNode<T> n = findMinNode(root);
        return (n == null ? null : n.data);
    }

    public T findMax() {
        AVLNode<T> n = root;
        if (n == null) return null;
        while (n.right != null)
            n = n.right;
        return n.data;
    }

    private int getHeight(AVLNode<T> node) {
        return (node == null ? 0 : node.height);
    }

    private void updateHeight(AVLNode<T> node) {
        if (node != null)
            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    private int getBalanceFactor(AVLNode<T> node) {
        if (node == null) return 0;
        return getHeight(node.left) - getHeight(node.right);
    }

    private AVLNode<T> rightRotate(AVLNode<T> y) {
        AVLNode<T> x = y.left;
        AVLNode<T> T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private AVLNode<T> leftRotate(AVLNode<T> x) {
        AVLNode<T> y = x.right;
        AVLNode<T> T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    public int getSize() {
        return countNodes(root);
    }

    private int countNodes(AVLNode<T> node) {
        if (node == null) return 0;
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    public LinkedList<T> inOrderTraversal() {
        LinkedList<T> res = new LinkedList<>();
        inOrderRecursive(root, res);
        return res;
    }

    private void inOrderRecursive(AVLNode<T> node, LinkedList<T> list) {
        if (node == null) return;
        inOrderRecursive(node.left, list);
        list.insert(node.data);
        inOrderRecursive(node.right, list);
    }

    public LinkedList<Integer> getKeysSorted() {
        LinkedList<Integer> res = new LinkedList<>();
        getKeysRecursive(root, res);
        return res;
    }

    private void getKeysRecursive(AVLNode<T> node, LinkedList<Integer> list) {
        if (node == null) return;
        getKeysRecursive(node.left, list);
        list.insert(node.key);
        getKeysRecursive(node.right, list);
    }

    public LinkedList<T> getAllValues() {
        return inOrderTraversal();
    }

    public LinkedList<T> rangeQuery(int minKey, int maxKey) {
        LinkedList<T> res = new LinkedList<>();
        rangeQueryRecursive(root, minKey, maxKey, res);
        return res;
    }

    private void rangeQueryRecursive(AVLNode<T> node, int minKey, int maxKey, LinkedList<T> list) {
        if (node == null) return;

        if (node.key > minKey)
            rangeQueryRecursive(node.left, minKey, maxKey, list);

        if (node.key >= minKey && node.key <= maxKey)
            list.insert(node.data);

        if (node.key < maxKey)
            rangeQueryRecursive(node.right, minKey, maxKey, list);
    }

    @Override
    public String toString() {
        return "AVLTree[size=" + getSize() +
               ", root=" + (root != null ? root.key : "null") + "]";
    }
}
