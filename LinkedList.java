package phase2;

public class LinkedList<T> implements List<T>{

	private Node<T> head;
	private Node<T> current;
	
	public LinkedList() {
		head= current= null;
	}
	
	public boolean empty() {
		return head == null;
	}
	
	public boolean last() {
		return current.getNext() == null;
	}
	
	public boolean full() {
		return false;
	}
	
	public void findFirst() {
		current= head;
	}
	
	public void findNext() {
		current= current.getNext();
	}
	
	public T retrieve() {
	    if (current == null) {
	        return null;  
	    }
	    return current.getData();
	}

	
	public void update(T val) {
		current.setData(val);
	}
	
	public void insert(T val) {
	    Node<T> tmp = new Node<>(val);
	    if (empty())
	        current = head = tmp;
	     else {
	        Node<T> curr = head;
	        while (curr.getNext() != null)
	            curr = curr.getNext();
	        curr.setNext(tmp);
	    }
	    current = tmp;
	}

	
	
	public void remove() {
		if(current==head)
			head= head.getNext();
		else {
			Node<T> tmp= head;
			
			while(tmp.getNext() != current) 
				tmp= tmp.getNext();
			tmp.setNext(current.getNext());
			
		if(current.getNext() == null)
			current= head;
		else
			current= current.getNext();
			
		}	}
	
	public boolean find(T key) {
		Node<T> temp= current;
		current= head;
		
		while(current!= null) {
			if(current.getData().equals(key))
				return true;
			current= current.getNext();
		}
		current= temp;
	    return false;
	}

	public Node<T> getHead() {
		return head;
	}

	public Node<T> getCurrent() {
		return current;
	}

	public void setCurrent(Node<T> current) {
		this.current = current;
	}

	public void setHead(Node<T> head) {
		this.head = head;
	}

	public int size() {
	    int c = 0;
	    Node<T> n = head;
	    while (n != null) {
	        c++;
	        n = n.getNext();
	    }
	    return c;
	}

	public void print() {
	    if (empty()) {
	        System.out.println("List is empty.");
	        return;
	    }

	    Node<T> temp = head;
	    while (temp != null) {
	        System.out.println(temp.getData());
	        temp = temp.getNext();
	    }
	}
	
	public String toString() {
	    if (head == null) return "No reviews";
	    
	    StringBuilder sb = new StringBuilder();
	    Node<T> current = head;
	    while (current != null) {
	        sb.append(current.getData().toString()); // calls Review.toString()
	        if (current.getNext() != null) sb.append("\n"); // separate reviews 
	        current = current.getNext();
	    }
	    return sb.toString();
	}

}