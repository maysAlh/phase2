package inventory;
public class LinkedList<T> {

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
  
  public T retrieve(){
    return current.getData();
  }
  
  public void update(T val) {
    current.setData(val);
  }
  
  public void insert(T val) {
    Node<T> temp;
    if(empty())
      current= head= new Node<T> (val);
    else {
      temp= current.getNext();
      current.setNext(new Node<T> (val));
      current= current.getNext();
      current.setNext(temp);
    }
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
      
    }
  }
  
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

public int size() {
	
	return 0;
}
}