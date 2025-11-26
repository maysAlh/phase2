package phase2;

public class ReviewList<T> {
    private Node<Review> head;
    private Node<Review> current;
    private int size;

    public ReviewList() {
        head = null;
        current = null;
        size = 0;
    }

    public boolean empty() {
        return head == null;
    }

    public boolean last() {
        return current != null && current.getNext() == null;
    }

    public void findFirst() {
        current = head;
    }

    public void findNext() {
        if (current != null)
            current = current.getNext();
    }

    public Review retrieve() {
        if (current == null) return null;
        return current.getData();
    }

    public void insert(Review val) {
        Node<Review> newNode = new Node<>(val);

        if (head == null) {
            head = newNode;
            current = head;
        } else if (current == null) {
            Node<Review> temp = head;
            while (temp.getNext() != null)
                temp = temp.getNext();
            temp.setNext(newNode);
            current = newNode;
        } else {
            newNode.setNext(current.getNext());
            current.setNext(newNode);
            current = newNode;
        }

        size++;
    }

    public void update(Review val) {
        if (current != null)
            current.setData(val);
    }

    public void remove() {
        if (current == null || head == null)
            return;
        
        if (current == head) {
            head = head.getNext();
            current = head;
        } else {
            Node<Review> prev = head;
            while (prev.getNext() != current)
                prev = prev.getNext();

            prev.setNext(current.getNext());
            current = prev.getNext();
        }

        size--;
    }

    public int getSize() { return size; }

    public Node<Review> getHead() {
        return head;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<Review> current = getHead();
        while (current != null) {
        	sb.append("[");
            sb.append(current.getData()).append("]\n"); 
            current = current.getNext();
        }
        return sb.toString();
    }
}