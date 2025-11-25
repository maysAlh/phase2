package inventory;

public class ProductPriorityQueue<T> implements Queue<Product>{
	private int size;
	private Node<PQElement<Product>> head;
	
	public ProductPriorityQueue() {
		head= null;
		size= 0;
		
	}
	
	public boolean full() {
		return false;
	}
	
	public int length() {
		return size;
	}
	
	public void enqueue(Product e) {
	    PQElement<Product> p = new PQElement<>(e, e.getPriority());
	    Node<PQElement<Product>> tmp = new Node<>(p, p.p);

	    if (head == null || p.data.getPriority() < head.getPriority()) {
	        tmp.setNext(head);
	        head = tmp;
	    } else {
	        Node<PQElement<Product>> current = head;
	        while (current.getNext() != null &&
	               current.getNext().getData().data.getPriority() <= p.data.getPriority()) {
	            current = current.getNext();
	        }
	        tmp.setNext(current.getNext());
	        current.setNext(tmp);
	    }
	    size++;
	}

	public Product serve() {
		if(head == null) {
			System.out.println("There is no product in tha queue!");
			return null;
		}
		PQElement<Product> tmp = head.getData();
		head= head.getNext();
		size--;
		
		return tmp.data;	
	}

	public Node<PQElement<Product>> getHead(){
		return head;
	}
	public void setHead(Node<PQElement<Product>> n) {
		head= n;
	}
	public int getSize() {
		return size;
	}
	
	public void setSize(int s) {
		size= s;
	}
	
	public String toString() {
	    String result = "Queue size: " + size + "\n";
	    Node<PQElement<Product>> current = head;
	    while (current != null) {
	        result += current.getData() + "\n"; //calling PQElement's toString() 
	        current = current.getNext();
	    }
	    return result;
	}
}