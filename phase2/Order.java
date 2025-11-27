package phase2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;




public class Order {
	
	 public static Scanner input = new Scanner (System.in);

	private int orderId;
	private int customerReference;
	private LinkedList<Integer> products = new LinkedList<Integer>(); // list of products id
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

		for (int i = 0; i < pids.length; i++) { // to add pid in product lis 
			products.insert(pids[i]);
		}

	}

	//===================================================================================


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

	//==================================================

	public int cancelOrder(int oid) { // Cancel order based on the state of order
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
		return 0;// no order found 
	}

	//=======================================================================================

	public boolean UpdateOrder(int orderID) {
	    if (orders.empty()) {
	        System.out.println("No such Order");
	        return false;
	    }

	    boolean found = false;

	    orders.findFirst();
	    while (!orders.last()) {
	        if (orders.retrieve().getOrderId() == orderID) {
	            found = true;
	            break;
	        }
	        orders.findNext();
	    }
	    if (!found && orders.retrieve().getOrderId() == orderID)
	        found = true;

	    if (!found) {
	        System.out.println("No such Order");
	        return false;
	    }

	    Order obj = orders.retrieve();

	    if (obj.getStatus().equalsIgnoreCase("cancelled") ||
	        obj.getStatus().equalsIgnoreCase("canceled")) {
	        System.out.println("could not change status for cancelled order");
	        return false;
	    }

	    System.out.println("Status of order is " + obj.getStatus());
	    System.out.println("Enter new status  (pending, shipped, delivered, cancelled)....");

	    String str = input.next();

	    obj.status = str;      
	    orders.update(obj);

	    return true;
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

	public LinkedList<Order> BetweenTwoDates(String date1, String date2)
	{
		LinkedList<Order> ordersbetweenDates =  new LinkedList<Order>();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate Ldate1 = LocalDate.parse(date1, formatter);
		LocalDate Ldate2 = LocalDate.parse(date2, formatter);

		if (! orders.empty())
		{
			orders.findFirst();

			for ( int i = 0 ; i < orders.size() ; i++)
			{
				if ( orders.retrieve().getDate().isAfter(Ldate1) && orders.retrieve().getDate().isBefore(Ldate2))
				{
					ordersbetweenDates.insert(orders.retrieve());
					System.out.println(orders.retrieve());
				}
				orders.findNext();
			}
		}
		return ordersbetweenDates;
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


	public String toString() {
		String str =  "\nOrder{" + "orderId: " + orderId + ", customer Refrence: " + customerReference 
				+ ",total price: " + totalPrice 
				+ " , status: " + status
				+ ", date: " + date;
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