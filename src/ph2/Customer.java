package ph2;

import java.util.Scanner;

public class Customer {


	private Scanner input = new Scanner(System.in);


	private int id;
	private String Name;
	private String email;


	AVL<Integer,Integer> orders = new AVL<Integer,Integer> ();
	
	public static AVL<Integer, Customer> customers = new AVL<Integer, Customer> (); 
    public static AVL<String, Customer> customersNames = new AVL<String, Customer> ();




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


	 public void addOrder(int orderId) {
	        
	        orders.insert(orderId, orderId);
	    }

	 public boolean removeOrder(int orderId) {
	        return orders.removeKey(orderId);
	    }
	//===============================    

	 public void RegisterCustomer() {
		    System.out.print("Enter customer ID : ");
		    int id = input.nextInt();
		    input.nextLine(); 

		   
		    while (customers.findkey(id)) {
		        System.out.print("Re-Enter again, ID already available: ");
		        id = input.nextInt();
		        input.nextLine();
		    }

		    System.out.print("Enter customer Name : ");
		    String name = input.nextLine();

		    System.out.print("Enter customer Email : ");
		    String email = input.nextLine();

		    Customer customer = new Customer(id, name, email);

		    
		    customers.insert(id, customer);

		    
		    customersNames.insert(name, customer);

		    System.out.println("Customer registered successfully.");
		}


	//==================================================================== 
  
	 public void OrderHistory()
	 {
	         if (customers.empty())
	             System.out.println("empty Customers data");
	         else
	         {
	             System.out.println("Enter customer ID: ");
	             int customerID = input.nextInt();
	             
	             if (customers.findkey(customerID))
	             {
	                 if (customers.retrieve().getOrders().empty())
	                     System.out.println("No Order History for " + customers.retrieve().getCustomerID());
	                 else
	                 {
	                     System.out.println("Order History");
	                     System.out.println(customers.retrieve().getOrders());
	                 }
	             }
	             else
	                 System.out.println("No such customer ID");
	         }
	     }

	 
	 public void printNamesAlphabetically()
	    {
	        customersNames.printKeys();
	    }


	//==================================================================			    
	 public boolean checkCustomerID(int customerID) {
	        return customers.findkey(customerID);
	    }

	//=============================================================	
	 public Customer getCustomerID() {
	        if (customers.empty()) {
	            System.out.println("empty Customers data");
	            return null;
	        }

	        System.out.print("Enter customer ID: ");
	        int customerID = input.nextInt();

	        if (customers.findkey(customerID)) {
	            Customer c = customers.retrieve();
	            System.out.println(c);
	            return c;
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

	 public AVL<Integer, Customer> getcustomers ( )
	    {
	        return customers;
	    }

	
	    public AVL<String, Customer> getcustomersNames ( )
	    {
	        return customersNames;
	    }
	   
	    public static void setCustomers(AVL<Integer, Customer> tree) {
	        customers = tree;
	    }

	    public static void setCustomersNames(AVL<String, Customer> tree) {
	        customersNames = tree;
	    }

	public AVL<Integer, Integer> getOrders() {
			return orders;
		}

		public void setOrders(AVL<Integer, Integer> orders) {
			this.orders = orders;
		}

	public String toString() {
		return "Customer [Id: " + id + ", Name: " + Name + ", Email: " + email + ", Orders:" + orders + "]";
	}			
}