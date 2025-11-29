package ph2;

public class PQElement<T>
{
   public T data;
   float priority;
       
   public PQElement(T e, float pr){
                  data = e;
                  priority = pr;
  }
}