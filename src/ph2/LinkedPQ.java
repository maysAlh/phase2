package ph2;

public class LinkedPQ<T> {
	 
    class PQNode<T> {
        public T data;
        public float priority;
        public PQNode<T> next;

        public PQNode() {
                next = null;
        }

        public PQNode(T e, float p) {
                data = e;
                priority = p;
        }

        // Setters/Getters?

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public float getPriority() {
            return priority;
        }

        public PQNode<T> getNext() {
            return next;
        }

        public void setNext(PQNode<T> next) {
            this.next = next;
        }

    }
    
    private int size;
    private PQNode<T> head;


    /* tail is of no use here. */
    public LinkedPQ() {
            head = null;
            size = 0;
    }

                public int length (){
            return size;
    }

    public boolean full () {
            return false;
    }

    public void enqueue(T e, float pty) {
            PQNode<T> tmp = new PQNode<T>(e, pty);
            if((size == 0) || (pty > head.priority)) {
                    tmp.next = head;
                    head = tmp;
            }
            else {
                    PQNode<T> p = head;
                    PQNode<T> q = null;
                    while((p != null) && (pty <= p.priority)) {
                            q = p;
                            p = p.next;
                    }
                    tmp.next = p;
                    q.next = tmp;
            }
            size++;
    }

    public PQElement<T> serve(){
            PQNode<T> node = head;
            PQElement<T> pqe=new PQElement<T>(node.data,node.priority);
            head = head.next;
            size--;
            return pqe;
    }
}