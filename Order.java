
package inventory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

//-------



public class Order {

          private int orderId;
          private int customerReference;
          private LinkedList<Integer> products = new LinkedList<Integer>(); // list of products
          private double totalPrice;
          private LocalDate date;
          // "pending", "shipped", "delivered", "canceled"
          private String status;

        
          public static LinkedList<Order> orders = new LinkedList<Order> ();

          // Constructors 
          public Order() {
              this.orderId = 0;
              this.customerReference = 0;
              this.totalPrice = 0;
              this.status = "";
          }

          public Order(int orderId, int customerReference, Integer[] pids, double totalPrice, String date, String status) {
              this.orderId = orderId;
              this.customerReference = customerReference;
              this.totalPrice = totalPrice;
              this.date = LocalDate.parse(date);
              this.status = status;
              
              if (pids != null) {
                  for (int i = 0; i < pids.length; i++) {
                      products.insert(pids[i]);
                  }
              }
          }

          //===================================================================================
          // Operations 

          public boolean createOrder(int orderId, int customerRef, Integer[] pids, double total, LocalDate orderDate) {

              if (!orders.empty()) {
                  orders.findFirst();
                  while (true) {
                      if (orders.retrieve().getOrderId() == orderId) return false; 
                      if (orders.last()) break;
                      orders.findNext();
                  }
              }
          
              Order o = new Order(orderId, customerRef, pids, total, orderDate.toString(), "pending");
              orders.insert(o);
              return true;
          }

          public int cancelOrder(int oid) {
              if (orders.empty()) return 0;

              orders.findFirst();
              while (true) {
                  Order cur = orders.retrieve();
                  if (cur.getOrderId() == oid) {
                      String st = cur.getStatus();
                      if (st.compareToIgnoreCase("canceled") == 0 ) {
                          System.out.println("Order " + cur.getOrderId() + " was canceled before");
                          return 2;
                      }
                      if (st.compareToIgnoreCase("delivered") == 0) {
                          System.out.println("Order " + cur.getOrderId() + " already delivered; cannot cancel.");
                          return -1;
                      }
                      cur.setStatus("canceled");
                      return 1;
                  }
                  if (orders.last()) break;
                  orders.findNext();
              }
              return 0;
          }
          
          //=======================================================================================

          public boolean UpdateOrder(int orderID) {  //update order
              boolean found = false;

              if (orders.empty()) {
                  System.out.println("No such Order");
                  return false;
              }

              orders.findFirst();
              while (!orders.last()) {
                  if (orders.retrieve().getOrderId() == orderID) {
                      found = true;
                      break;
                  }
                  orders.findNext();
              }
              if (orders.retrieve().getOrderId() == orderID)
                  found = true;

                           if (found) {
	                   if (orders.retrieve().getStatus().equalsIgnoreCase("canceled"))
                      { 
	                	   
                      } else {
                      Order obj = orders.retrieve();

                      System.out.println("Status of order is " + orders.retrieve().getStatus());
                      System.out.println("Enter new status  (pending, shipped, delivered, cancelled)....");

                      Scanner input = new Scanner(System.in);
                      String str = input.next();

                      
                      orders.remove();
                      obj.status = str;
                      orders.insert(obj);
                      return true;
                  }
              }
              System.out.println("No such Order");
              return false;
          }

      //=============================================================
          
          public Order searchOrderID(int orderID) 
          { 
              boolean found = false; 
       
              orders.findFirst(); 
              while (!orders.last()) 
              { 
                  if (orders.retrieve().getOrderId()== orderID) 
                  { 
                      found = true; 
                      break; 
                  } 
                  orders.findNext(); 
              } 
              if (orders.retrieve().getOrderId()== orderID) 
                  found = true; 
       
              if (found ) 
                  return orders.retrieve(); 
       
              System.out.println("No such Order"); 
              return null; 
          }

          
          
          //=============================================================================
          
          public boolean checkOrderID(int oid) 
          { 
              if (!orders.empty()) 
              { 
                  orders.findFirst(); 
                  while (!orders.last()) 
                  { 
                      if (orders.retrieve().getOrderId()== oid) 
                          return true; 
                      orders.findNext(); 
                  } 
                  if (orders.retrieve().getOrderId()== oid) 
                      return true; 
              } 
              return false; 
          } 
      
     
          
          
      //=============================================================================
      
      public boolean betweenTwoDates(String fromStr, String toStr) {
    	    DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    	    LocalDate from = LocalDate.parse(fromStr.trim(), f);
    	    LocalDate to   = LocalDate.parse(toStr.trim(),   f);

    	   
    	    if (from.isAfter(to)) {
    	        LocalDate tmp = from; from = to; to = tmp;
    	    }

    	    if (orders.empty()) { 
    	        System.out.println("No orders loaded.");
    	        return false;
    	    }

    	    int hits = 0;
    	    orders.findFirst();
    	    while (true) {
    	        Order o = orders.retrieve();
    	        LocalDate d = o.getDate();  
    	        //ّ
    	        if (!d.isBefore(from) && !d.isAfter(to)) {
    	            System.out.println(o); 
    	            hits++;
    	        }
    	        if (orders.last()) break;
    	        orders.findNext();
    	    }

    	    if (hits == 0) {
    	        System.out.println("No orders between " + from + " and " + to + ".");
    	        return false;
    	    } else {
    	        System.out.println("Total: " + hits);
    	        return true;
    	    }
    	}
      
      //============================================================
    
      public void addProduct(Integer pid) {
    	    if (pid == null) return;
    	    this.products.insert(pid);
    	}

//===============================================================================================================================================          
          
          // setter and getter

        public int getOrderId() {
          return orderId;
        }

        public void setOrderId(int orderId) {
          this.orderId = orderId;
        }

        public int getCustomerReference() {
          return customerReference;
        }

        public void setCustomerReference(int customerReference) {
          this.customerReference = customerReference;
        }

        public LinkedList<Integer> getProducts() {
          return products;
        }

        public void setProducts(LinkedList<Integer> products) {
          this.products = products;
        }

        public double getTotalPrice() {
          return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
          this.totalPrice = totalPrice;
        }

        public LocalDate getDate() {
          return date;
        }

        public void setDate(LocalDate date) {
          this.date = date;
        }

        public String getStatus() {
          return status;
        }

        public void setStatus(String status) {
          this.status = status;
        }


   public static LinkedList<Order> getOrders() {
          return orders;
        }

        public static void setOrders(LinkedList<Order> orders) {
          Order.orders = orders;
        }

        @Override
        public String toString() {
            String str =  "\nOrder{" + "orderId=" + orderId + ", customer Refrence=" + customerReference 
                    + ",total price=" + totalPrice 
                    + " , status =" + status
                    + ", date =" + date;
            if ( ! products.empty())
            {
                str += "(products List" ;
                products.findFirst();
                while(! products.last())
                {
                    str += products.retrieve() + " ";
                    products.findNext();
                }
                str += products.retrieve() + " )";
            }
            str +=  " }";
            return str;        
        }
          
         

         
      }