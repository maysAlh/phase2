package ph2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;




public class Order {
	
	 public static Scanner input = new Scanner (System.in);

	private int orderId;
	private int customerReference;
	AVL <Integer,Integer> products = new AVL<Integer,Integer> (); 
	private double totalPrice;
	private LocalDate date;
	// "pending", "shipped", "delivered", "canceled"
	private String status;


	 public static AVL<Integer, Order> orders = new AVL <Integer, Order> ();
	 
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

		for (int i = 0 ; i < pids.length ; i++)
	          products.insert(pids[i],pids[i]);

	}

	

	//==================================================

	public int cancelOrder(int oid) { // Cancel order based on the state of order

	    if (orders.empty())
	        return 0;

	    
	    if (!orders.findkey(oid)) {
	        return 0;  // no order found
	    }

	    Order cur = orders.retrieve();   

	    String st = cur.getStatus();

	    if (st.equalsIgnoreCase("canceled") || st.equalsIgnoreCase("cancelled")) {
	        System.out.println("Order " + cur.getOrderId() + " was canceled before");
	        return 2;
	    }

	    if (st.equalsIgnoreCase("delivered")) {
	        System.out.println("Order " + cur.getOrderId() + " already delivered; cannot cancel.");
	        return -1;
	    }

	    cur.setStatus("canceled");  
	    orders.update(cur);          

	    return 1;
	}


	//=======================================================================================

	public boolean UpdateOrder(int orderID) {

	    if (orders.empty()) {
	        System.out.println("No such Order");
	        return false;
	    }

	    
	    if (!orders.findkey(orderID)) {
	        System.out.println("No such Order");
	        return false;
	    }

	    Order obj = orders.retrieve();

	    if (obj.getStatus().equalsIgnoreCase("cancelled")
	            || obj.getStatus().equalsIgnoreCase("canceled")) {
	        System.out.println("could not change status for cancelled order");
	        return false;
	    }

	    System.out.println("Status of order is " + obj.getStatus());
	    System.out.println("Enter new status  (pending, shipped, delivered, cancelled)....");

	    String str = input.next();

	    obj.setStatus(str);    
	    orders.update(obj);    

	    return true;
	}

	//=============================================================

	public Order searchOrderID(int orderID) {

	    if (!orders.findkey(orderID)) {
	        System.out.println("No such Order");
	        return null;
	    }

	    return orders.retrieve();
	}



	//=============================================================================

	public boolean checkOrderID(int oid) {
	    return orders.findkey(oid);
	}



	//=============================================================================

	 public AVL<Date, Order> BetweenTwoDates(String date1, String date2)
	    {
	        AVL<Date, Order> ordersbetweenDates = new AVL<Date, Order>();
	        
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	        LocalDate Ldate1 = LocalDate.parse(date1, formatter);
	        LocalDate Ldate2 = LocalDate.parse(date2, formatter);
	       // ordersbetweenDates = orders.inOrdertraverseData(Ldate1, Ldate2);
	        
	        return ordersbetweenDates;
	   }


	//============================================================

	public void addProduct (Integer product )
    {
        products.insert(product, product);
    }

    public boolean removeProduct( Integer P)
    {
        return products.removeKey(P);
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

	public AVL<Integer,Integer> getProducts() {
        return products;
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


	

	 public String toString() {
	        String str =  "\nOrder{" + "orderId=" + orderId + ", customer Refrence=" + customerReference 
	                + ",total price=" + totalPrice 
	                + " , status =" + status
	                + ", date =" + date;
	        if ( ! products.empty())
	        {
	            str += "( products List" ;
	            str += products;
	            str += " )";
	        }
	        str +=  " }";
	        return str;        
	    }    
}