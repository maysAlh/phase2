package phase2;

import java.util.Scanner;

public class Customer {


	private Scanner input = new Scanner(System.in);


	private int id;
	private String Name;
	private String email;


	private LinkedList<Integer> orders = new LinkedList<Integer>();  //list for order id 
	private static LinkedList<Customer> customers = new LinkedList<Customer>(); 



	public Customer() {

		this.id = 0;
		this.Name = "";
		this.email = "";
	}

	public Customer(int id, String Name, String email) {
		this.id = id;
		this.Name = Name;
		this.email = email;
	}


	////////////////////////////////////////////


	public void addOrder(Integer order) { // take order id and insert it into orde list for custumer
		this.orders.insert(order);
	}


	public boolean removeOrder( Integer o) 
	{ 
		if ( ! orders.empty()) 
		{ 
			orders.findFirst(); 
			while(! orders.last()) 
			{ 
				if (orders.retrieve() .equals(o) ) 
				{ 
					orders.remove(); 
					return true; 
				} 
				else 
					orders.findNext(); 
			} 
			if (orders.retrieve() .equals(o)) 
			{ 
				orders.remove(); 
				return true; 
			} 
		} 
		return false; 
	}

	//===============================    

	public void RegisterCustomer() {  // add new customer
		System.out.println("Enter customer ID : ");
		int id = input.nextInt();
		input.nextLine();

		while (true) {

			boolean found = false;
			if (!customers.empty()) {
				customers.findFirst();
				while (!customers.last()) {
					if (customers.retrieve().getId() == id) { found = true; break; } // id already in the list 
					customers.findNext();
				}
				if (!found && customers.retrieve().getId() == id) {     
					found = true;
				}

			} //end if
			if (!found) break;

			System.out.println("Re-Enter agian, ID already avialable: "); 
			id = input.nextInt();
			input.nextLine();
		} // end while

		System.out.println("Enter customer Name : ");
		String name = input.nextLine();



		System.out.println("Enter customer Email : ");
		String email = input.nextLine();

		Customer customer = new Customer(id, name, email); 
		customers.insert(customer);
		System.out.println("Customer registered successfully.");
	}


	//==================================================================== 
  

	public void orderHistory() {   // Retrieve order history for a specific customer

		if (customers.empty()) {
			System.out.println("empty Customers data");
			return;
		}


		System.out.print("Enter customer ID: ");
		int customerID = input.nextInt();
		input.nextLine(); // consume the trailing newline

		// to check customer id
		if (!checkCustomerID(customerID)) {
			System.out.println("customer ID NOT found");
			return;
		}


		Customer c = null;   // to get customer id and saved it 
		if (!customers.empty()) {
			customers.findFirst();
			while (true) {
				if (customers.retrieve().getId() == customerID) {
					c = customers.retrieve();
					break;
				}
				if (customers.last()) break;
				customers.findNext();
			}
		}


		if (c == null) {
			System.out.println("customer ID NOT found");
			return;
		}


		LinkedList<Integer> orders = c.getOrders(); // get customer's order ids
		if (orders.empty()) {
			System.out.println("No Order History for " + c.getId());
			return;
		}


		System.out.println("Order History for customer " + c.getId() + ":");
		orders.findFirst();
		int count = 0;
		while (!orders.last()) {
			System.out.println("- " + orders.retrieve());
			orders.findNext();
			count++;
		}
		System.out.println("- " + orders.retrieve());
		System.out.println("Total orders: " + (count + 1));
	}


	//==================================================================			    
	public boolean checkCustomerID( int customerID )
	{
		boolean found = false;
		if (! customers.empty())
		{
			customers.findFirst();
			while (!customers.last())
			{
				if (customers.retrieve().getId() == customerID)
					found = true;
				customers.findNext();
			}
			if (customers.retrieve().getId() == customerID)
				found = true;
		}
		return found ;
	}
	//=============================================================	
	public Customer getCustomerID()
	{
		if (customers.empty())
		{
			System.out.println("empty Customers data");
		}
		else
		{
			System.out.println("Enter customer ID: ");
			int customerID = input.nextInt();

			customers.findFirst();
			while (!customers.last())
			{
				if (customers.retrieve().getId() == customerID)
					return customers.retrieve();
				customers.findNext();
			}
			if (customers.retrieve().getId() == customerID)
				return customers.retrieve();        

		}
		System.out.println("No such customer ID");
		return null;
	}
	//==========================

	// setter and getter

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LinkedList<Integer> getOrders() {
		return orders;
	}

	public void setOrders(LinkedList<Integer> orders) {
		this.orders = orders;
	}

	public static LinkedList<Customer> getCustomers() {
		return customers;
	}

	public static void setCustomers(LinkedList<Customer> customers) {
		Customer.customers = customers;
	}


	public String toString() {
		return "Customer [Id: " + id + ", Name: " + Name + ", Email: " + email + ", Orders:" + orders + "]";
	}			
}