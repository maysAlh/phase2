package phase2;

public class PQElement<T> {

	public T data;
	public int p;
	
	public PQElement(T e , int pr) {
		data= e;
		p= pr;
	}
	 public String toString() {
	        return data.toString() + " (priority: " + p + ")";
	    }
}
