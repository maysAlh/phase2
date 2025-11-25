package inventory;

public class Node<T>{

	private T data;
	private Node<T> next;
	private Node<T> previous; //DL
	private int priority; //PQ
	
	
	public Node() { //PQ
		next= null;
	}
	public Node(T val) {
		data= val;
		next= null;
		previous= null;
	}

	public Node(T e , int p) { //PQ
		setPriority(p);
		data= e;
	}

	public Node<T> getPrevious() { 
		return previous;
	}

	public void setPrevious(Node<T> previous) {
		this.previous = previous;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Node<T> getNext() {
		return next;
	}

	public void setNext(Node<T> next) {
		this.next = next;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	
	public String toString() {
	    return data.toString();
	}
}