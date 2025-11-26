package phase2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
	public static Scanner input = new Scanner(System.in);

	public static customersData loederC = new customersData( "C:/Users/hamsa/Desktop/projectData/dataset/customers.csv");                                        
	public static orderDate loaderO = new orderDate("C:/Users/hamsa/Desktop/projectData/dataset/orders.csv");
	public static ProductData loaderP= new ProductData("C:/Users/hamsa/Desktop/projectData/dataset/prodcuts.csv");

	public static ReviewData loaderR= new ReviewData("C:/Users/hamsa/Desktop/projectData/dataset/reviews.csv");

	public static LinkedList<Customer> customers;
	public static LinkedList<Order> orders;
	public static LinkedList<Product> products;
	public static LinkedList<Review> reviews;

	public static Order odata = new Order();
	public static Customer Cdata = new Customer();
	public static Product Pdata = new Product();
	public static Review Rdata = new Review();

	public static void loadData() {
		System.out.println("loading data from CSVs files");

		customers = loederC.getCustomers();
		Customer.setCustomers(customers);

		orders = loaderO.getOrders();
		Order.setOrders(orders);

		products = loaderP.getProducts();
		Product.setProducts(products);

		reviews = loaderR.getReviews();
		Review.setReviews(reviews);

		// Link orders with customers
		if (!customers.empty() && !orders.empty()) {
			customers.findFirst();
			while (true) {
				Customer cur = customers.retrieve();
				int cid = cur.getId();

				orders.findFirst();
				while (true) {
					Order od = orders.retrieve();
					if (od.getCustomerReference() == cid) {
						cur.addOrder(od.getOrderId());
					}
					if (orders.last()) break;
					orders.findNext();
				}

				if (customers.last()) break;
				customers.findNext();
			}
		}

		// links reviews with their products
		Pdata.linkReviewsToProducts(products, reviews);
	}

	public static int menu1() {
		System.out.println("*********************");
		System.out.println("1. Products Menu");
		System.out.println("2. Customers Menu");
		System.out.println("3. Orders Menu");
		System.out.println("4. Reviews Menu");
		System.out.println("5. Exit");
		System.out.println("Enter your choice:");
		return input.nextInt();
	}

	public static void ProductMenu() {
		int choice;

		do {
			System.out.println("------ Product Menu ------\n");
			System.out.println("Choose from the following:");
			System.out.println("1. Add Product");
			System.out.println("2. Update Product");
			System.out.println("3. Remove Product");
			System.out.println("4. Search by product id");
			System.out.println("5. Search by product name");
			System.out.println("6. View all products");
			System.out.println("7. View out-of-stock products");
			System.out.println("8. Return Main menu");
			System.out.print("Enter your choice: ");
			choice = input.nextInt();

			switch (choice) {
			case 1: // Add product
				Pdata.add();
				break;

			case 2: // Update product
				Pdata.setProducts(products);
				Pdata.update();
				break;

			case 3:
            {
            	 System.out.println("Note: products linked to orders or reviews will be archived rather than permanently deleted.");
                Product p = Pdata.removeProduct();
                boolean deleted = true;       
                // check if there is order for this products 
                if (p != null)
                {
                    if (p.getReview().size() == 0)
                    {
                        boolean found = false;
                        
                        orders.findFirst();
                        for ( int x = 0 ; x < orders.size() ; x++)
                        {
                            if (! orders.retrieve().getProducts().empty())
                            {
                                orders.retrieve().getProducts().findFirst();
                                for ( int counter = 0 ; counter < orders.retrieve().getProducts().size() ; counter ++)
                                {
                                    if (orders.retrieve().getProducts().retrieve() == p.getProductId())
                                    {
                                         found = true;   
                                         break;
                                    }
                                    orders.retrieve().getProducts().findNext();
                                }
                            }
                            if (found )
                                break;
                            orders.findNext();
                        }

                        // return back the products , it has orders
                        if( found )
                        {
                            products.insert(p);
                            System.out.println("Product has previous orders; it has been archived instead of deleted.");
                            deleted = false;
                        }
                    }
                    else// it has reviews
                    {
                        products.insert(p);
                        System.out.println("Product has existing reviews; it has been archived instead of deleted.");
                        deleted = false;
                    }
                }
                if ( deleted)
                	 System.out.println("Product removed successfully.");
            }
            break;

			case 4: // search by product id
			{
                Product pro = Pdata.searchProducID();
                if (pro != null)
                    System.out.println(pro);
            }
            break;
				
			case 5: // search by product name
            {
                Product pro = Pdata.searchProducName();
                if (pro != null)
                    System.out.println(pro);
            }
            break;
            
			case 6: // View all products
				Pdata.viewAllProducts(products);
				break;

			case 7:
				System.out.println("---- Out-of-Stock Products ----");
				Pdata.trackOutOfStockProduct();
				break;

			case 8: // Return to main menu
				break;

			default:
				System.out.println("Invalid choice!");
			}

		} while (choice != 8);
	}

	public static void OrdersMenu() {
		int choice;
		do {
			Order.setOrders(orders);
            
			System.out.println("------ Order Menu ------\n");
			System.out.println("Choose from the following:");
			System.out.println("1. Place New Order");
			System.out.println("2. Cancel Order");
			System.out.println("3. Update Order (Status)");
			System.out.println("4. Search By ID");
			System.out.println("5. All orders between two dates");
			System.out.println("6. Return Main menu");
			System.out.println("Enter your choice:");
			choice = input.nextInt();

			switch (choice) {
			case 1:
				
				Product.setProducts(products);
				PlaceOrder();
				break;

			case 2:
				CancelOrder();
				break;

			 case 3:
	            {
	                System.out.println("update to new status...");
	                if (orders.empty())
	                    System.out.println("empty Orders data");
	                else
	                {
	                    System.out.println("Enter order ID: ");
	                    int orderID = input.nextInt();
	                    odata.UpdateOrder(orderID);
	                }
	            }
	            break;

			case 4: {
				System.out.print("Enter order ID: ");
				int orderID = input.nextInt();
				Order found = odata.searchOrderID(orderID);
				System.out.println(found);
			}
			break;

			case 5: {
				Order.setOrders(orders);

				System.out.println("Enter first date (dd/MM/yyyy)");
				String date1 = input.next();

				System.out.println("Enter second date (dd/MM/yyyy)");
				String date2 = input.next();

				odata.BetweenTwoDates(date1, date2);
			}
			break;

			case 6:
				break;

			default:
				System.out.println("Return to Main menu");
			}

		} while (choice != 6);
	}

	public static void CustomersMenu() {
		int choice;
		do {
			Customer.setCustomers(customers);

			System.out.println("------ Customer Menu ------\n");
			System.out.println("Choose from the following:");
			System.out.println("1. Register new customer");
			System.out.println("2. Place New Order for specific customer");
			System.out.println("3. View Order history  for specific customer");
			System.out.println("4. Add Review");
			System.out.println("5. Extract reviews from a specific customer for all products.");
			System.out.println("6. Return Main menu");
			System.out.println("Enter your choice:");
			choice = input.nextInt();
			;
			switch (choice) {
			case 1:
				Cdata.RegisterCustomer();
				break;
			case 2:
				PlaceOrder();
				break;
			case 3:
				Cdata.orderHistory();
				break;
			case 4:
				System.out.print("Enter customer ID: ");
				int cID = input.nextInt();
				while (!Cdata.checkCustomerID(cID)) {
					System.out.print("customer ID not available, Re-enter again: ");
					cID = input.nextInt();
				}

				System.out.print("Enter product ID: ");
				int pID = input.nextInt();
				while (!Pdata.checkProductID(pID)) {
					System.out.print("product ID not available, Re-enter again: ");
					pID = input.nextInt();
				}

				Review r = Rdata.AddReview(cID, pID);
				if (r != null) {
					System.out.println("Review added successfully:");
					System.out.println(r);
				} else {
					System.out.println("Failed to add review.");
				}
				break;

			case 5:
				AllReviewsForCustomer();
				break;
			case 6:
				break;
			default:
				System.out.println(" Return to Main menu");
			}
		} while (choice != 6);
	}

	public static void ReviewMenu() {
		int choice;
		do {
			
			System.out.println("------ Review Menu ------\n");
			System.out.println("Choose from the following:");
			System.out.println("1. Add Review");
			System.out.println("2. View Reviews");
			System.out.println("3. View Top 3 Reviewed Products");
			System.out.println("4. Get an average rating for product");
			System.out.println("5. List Of common Products Reviewed By Two Customer");
			System.out.println("6. Edit reviews");
			System.out.println("7. Return Main menu");

			System.out.println("Enter Your Choice:");
			choice = input.nextInt();
			switch (choice) {

			case 1: {
				System.out.println("------ Add Review ------");

				System.out.print("Enter customer ID: ");
				int cID = input.nextInt();
				while (!Cdata.checkCustomerID(cID)) {
					System.out.print("customer ID not available, Re-enter again: ");
					cID = input.nextInt();
				}

				System.out.print("Enter product ID: ");
				int pID = input.nextInt();
				while (!Pdata.checkProductID(pID)) {
					System.out.print("product ID not available, Re-enter again: ");
					pID = input.nextInt();
				}

				Review r = Rdata.AddReview(cID, pID);
				if (r != null) {
					System.out.println("Review added successfully:");
					System.out.println(r);
				} else {
					System.out.println("Failed to add review.");
				}
				break;
			}

			case 2:
				reviews.findFirst();
				System.out.println("------Reviews List ------\n");
				while (reviews.retrieve() != null) {
					System.out.println(reviews.retrieve());
					reviews.findNext();
				}
				break;
			case 3:
				TopThreeProducts();
				break;

			case 4: {
				System.out.println("Enter product ID to Get an average rating :");
				int pid = input.nextInt();

				while (!Pdata.checkProductID(pid)) {
					System.out.println("Re- Enter product id again (ID is not available)...");
					pid = input.nextInt();
				}
				float AVG = avgRating(pid);
				System.out.println("Average Rating for " + pid + " is " + AVG);
				break;
			}

			case 5:
			{
				Customer c1 = Cdata.getCustomerID();
				Customer c2 = Cdata.getCustomerID();
				if (c1 != null && c2 != null) commonReviewed(c1.getId(), c2.getId());

			}
			break;



			case 6: {
				if (reviews == null || reviews.empty()) {
					System.out.println("No reviews data");
					break;
				}

				System.out.print("Product ID to edit: ");
				int pid = input.nextInt();
				while (!Pdata.checkProductID(pid)) {
					System.out.print("Product ID not available, re-enter: ");
					pid = input.nextInt();
				}

				int newRating;
				while (true) {
					System.out.print("New rating (1-5): ");
					if (input.hasNextInt()) {
						newRating = input.nextInt();
						if (newRating >= 1 && newRating <= 5) break;
					} else {
						input.next();
					}
					System.out.println("Invalid rating. Enter an integer 1..5.");
				}
				input.nextLine();

				System.out.print("New comment (leave empty to keep current): ");
				String newComment = input.nextLine();

				Review helper = new Review();
				helper.editByProductId(reviews, pid, newRating, newComment);
				break;
			}

			case 7:
				break;
			default:
				System.out.println(" Return to Main menu");

			}

		} while (choice != 7);
	}

//==============================================================================================

	public static void commonReviewed(int cid1, int cid2)
	{
		LinkedList<Integer> pcustomer1 = new LinkedList<Integer>();
		LinkedList<Integer> pcustomer2 = new LinkedList<Integer>();

		reviews = Rdata.getReviews();
		if (reviews == null || reviews.empty()) {
			System.out.println("Reviews not available for all products");
			return;
		}


		reviews.findFirst();
		for (int i = 1; i <= reviews.size(); i++) {
			int c = reviews.retrieve().getCustomerId();
			int p = reviews.retrieve().getProductId();

			if (c == cid1) {
				pcustomer1.findFirst();
				boolean found1 = false;
				for (int x = 1; x <= pcustomer1.size(); x++) {
					if (pcustomer1.retrieve() == p) { found1 = true; break; }
					pcustomer1.findNext();
				}
				pcustomer1.findFirst();
				if (!found1) pcustomer1.insert(p);
			}

			if (c == cid2) {
				pcustomer2.findFirst();
				boolean found2 = false;
				for (int x = 1; x <= pcustomer2.size(); x++) {
					if (pcustomer2.retrieve() == p) { found2 = true; break; }
					pcustomer2.findNext();
				}
				pcustomer2.findFirst();
				if (!found2) pcustomer2.insert(p);
			}

			reviews.findNext();
		}


		printIdsOneLine(pcustomer1);
		printIdsOneLine(pcustomer2);


		LinkedList<Product> commonAbove4 = new LinkedList<Product>();
		boolean hasIntersection = false;

		if (!pcustomer1.empty() && !pcustomer2.empty()) {
			pcustomer1.findFirst();
			for (int m = 1; m <= pcustomer1.size(); m++) {
				int pid = pcustomer1.retrieve();


				boolean inSecond = false;
				pcustomer2.findFirst();
				for (int n = 1; n <= pcustomer2.size(); n++) {
					if (pid == pcustomer2.retrieve()) { inSecond = true; break; }
					pcustomer2.findNext();
				}

				if (inSecond) {
					hasIntersection = true;
					float avg = avgRating(pid);
					if (avg >= 4.0f) {
						Product p = findProductById(pid);
						if (p != null) commonAbove4.insert(p);
					}
				}

				pcustomer1.findNext();
			}
		}

		System.out.println("Common Products with rate above 4 are: ");
		if (commonAbove4.empty()) {
			if (!hasIntersection)
				System.out.println("NO common products between the two customers");
			else
				System.out.println("All common products have average rating below 4");
			return;
		}


		commonAbove4.findFirst();
		for (int k = 1; k <= commonAbove4.size(); k++) {
			Product prod = commonAbove4.retrieve();
			float rate = avgRating(prod.getProductId());

			System.out.println(" Product (" + prod.getProductId() + ") " +
					prod.getName() + " with rate " + String.format("%.1f", rate));
			System.out.println();
			System.out.println("productId=" + prod.getProductId() +
					", name=" + prod.getName() +
					", price=" + String.format("%.2f", prod.getPrice()) +
					", stock=" + prod.getStock());
			System.out.println();

			commonAbove4.findNext();
		}
	}


	private static void printIdsOneLine(LinkedList<Integer> ids) {
		if (ids == null || ids.empty()) { System.out.println(); return; }
		ids.findFirst();
		for (int i = 1; i <= ids.size(); i++) {
			System.out.print(ids.retrieve());
			if (i < ids.size()) System.out.print(" ");
			ids.findNext();
		}
		System.out.println();
	}


//==============================================================================
	public static void TopThreeProducts() {
		Product P1 = null, P2 = null, P3 = null;
		double avg1 = 0, avg2 = 0, avg3 = 0;

		Node<Product> currentProduct = products.getHead();

		while (currentProduct != null) {
			Product product = currentProduct.getData();

			// Calculate average rating
			Node<Review> currentReview = product.getReview().getHead();
			double total = 0;
			int count = 0;

			while (currentReview != null) {
				total += currentReview.getData().getRatingScore();
				count++;
				currentReview = currentReview.getNext();
			}

			double avg = (count > 0) ? total / count : 0;

			if (avg > avg1) {
				P3 = P2;
				avg3 = avg2;
				P2 = P1;
				avg2 = avg1;
				P1 = product;
				avg1 = avg;
			} else if (avg > avg2) {
				P3 = P2;
				avg3 = avg2;
				P2 = product;
				avg2 = avg;
			} else if (avg > avg3) {
				P3 = product;
				avg3 = avg;
			}

			currentProduct = currentProduct.getNext();
		}
		
		

		System.out.println("*********************\nTop 3 Products by Rating:\n");
		if (P1 != null) System.out.println(P1.getName() + " — Average: " + avg1);
		if (P2 != null) System.out.println(P2.getName() + " — Average: " + avg2);
		if (P3 != null) System.out.println(P3.getName() + " — Average: " + avg3);
	}

//============================================================================================
	public static void PlaceOrder() {
	    try {
	        Order.setOrders(orders);
	        Customer.setCustomers(customers);

	        Order new_order = new Order();
	        double total_price = 0.0;

	        System.out.println("\n====================");
	        System.out.println("   Create New Order");
	        System.out.println("====================");

	        System.out.print("Order ID: ");
	        int oid = input.nextInt();
	        while (odata.checkOrderID(oid)) {
	            System.out.print("This Order ID is already in use. Enter another ID: ");
	            oid = input.nextInt();
	        }
	        new_order.setOrderId(oid);

	        System.out.print("Customer ID: ");
	        int cid = input.nextInt();
	        while (!Cdata.checkCustomerID(cid)) {
	            System.out.print("No matching customer ID. Try again: ");
	            cid = input.nextInt();
	        }
	        new_order.setCustomerReference(cid);

	        while (true) {
	            System.out.print("Product ID to add (or 0 to finish): ");
	            int pid = input.nextInt();
	            if (pid == 0) break;

	            while (!Pdata.checkProductID(pid)) {
	                System.out.print("No matching product ID. Re-enter (or 0 to cancel): ");
	                pid = input.nextInt();
	                if (pid == 0) break;
	            }
	            if (pid == 0) continue;

	            boolean found = false;
	            Node<Product> cur = products.getHead();
	            while (cur != null) {
	                Product pp = cur.getData();
	                if (pp.getProductId() == pid) {
	                    if (pp.getStock() <= 0) {
	                        System.out.println("• Out of stock – choose a different product.");
	                    } else {
	                        pp.setStock(pp.getStock() - 1);
	                        new_order.addProduct(pp.getProductId());
	                        total_price += pp.getPrice();
	                        System.out.println("Added. Total now: " + String.format("%.2f", total_price));
	                    }
	                    found = true;
	                    break;
	                }
	                cur = cur.getNext();
	            }

	            if (!found) {
	                System.out.println(" No product found with ID " + pid + ".");
	            }
	        }

	        System.out.print("Order date (dd/MM/yyyy): ");
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	        LocalDate Ldate = LocalDate.parse(input.next(), formatter);
	        new_order.setDate(Ldate);

	        System.out.print("Status (pending/shipped/delivered/cancelled): ");
	        new_order.setStatus(input.next());

	        new_order.setTotalPrice(total_price);
	        orders.insert(new_order);

	        customers.findFirst();
	        for (int i = 0; i < customers.size(); i++) {
	            if (customers.retrieve().getId() == new_order.getCustomerReference()) {
	                Customer cust = customers.retrieve();
	                customers.remove();
	                cust.addOrder(oid);
	                customers.insert(cust);
	                break;
	            }
	            customers.findNext();
	        }

	        System.out.println("\n Order saved successfully.");
	        System.out.println("   Order #" + oid + "  |  Customer: " + new_order.getCustomerReference());
	        System.out.println("   Date: " + Ldate + "  |  Status: " + new_order.getStatus());
	        System.out.println("   Total: " + total_price);
	        System.out.println("=====================================");

	    } catch (InputMismatchException ex) {
	        System.out.println("Invalid input detected. The order was not created.");
	        input.nextLine();
	    }
	}


	public static void CancelOrder() {
		System.out.print("Order ID to cancel: ");
		int oid = input.nextInt();

		// Search for the order and check it exists
		Order cancel_order = odata.searchOrderID(oid);
		while (cancel_order == null) {
			System.out.print("No order found with this ID. Try another ID: ");
			oid = input.nextInt();
			cancel_order = odata.searchOrderID(oid);
		}

		// Execute cancellation
		if (odata.cancelOrder(oid) == 1) {

			// remove the cancelled order from their list
			customers.findFirst();
			for (int i = 0; i < customers.size(); i++) {
				if (customers.retrieve().getId() == cancel_order.getCustomerReference()) {
					Customer cust = customers.retrieve();
					customers.remove();
					cust.removeOrder(cancel_order.getOrderId());
					customers.insert(cust);
					break;
				}
				customers.findNext();
			}

			// Restore stock for all products included in the cancelled order
			LinkedList<Integer> pids = cancel_order.getProducts();
			pids.findFirst();
			for (int x = 0; x < pids.size(); x++) {
				int pid = pids.retrieve();

				Node<Product> cur = products.getHead();
				while (cur != null) {
					Product pp = cur.getData();

					if (pp.getProductId() == pid) {
						pp.setStock(pp.getStock() + 1);
						break;
					}
					cur = cur.getNext();
				}

				pids.findNext();
			}

			System.out.println("Order #" + cancel_order.getOrderId() + " has been cancelled.");
		}
	}

	private static Product findProductById(int pid) {
		Node<Product> cur = products.getHead();
		while (cur != null) {
			Product p = cur.getData();
			if (p.getProductId() == pid) return p;
			cur = cur.getNext();
		}
		return null;
	}

	public static LinkedList<Review> AllReviewsForCustomer() {
		System.out.println("Enter customer ID:");
		int cid = input.nextInt();
		while (!Cdata.checkCustomerID(cid)) {
			System.out.println("Re-enter customer ID, is not available , try again");
			cid = input.nextInt();
		}
		input.nextLine();

		LinkedList<Review> selectedReviews = new LinkedList<Review>();

		if (!reviews.empty()) {
			reviews.findFirst();
			while (true) {
				Review r = reviews.retrieve();
				if (r.getCustomerId() == cid) {
					selectedReviews.insert(r);
				}
				if (reviews.last()) break;
				reviews.findNext();
			}
		}

		if (selectedReviews.empty()) {
			System.out.println("No reviews for customer " + cid);
			return selectedReviews;
		}

		selectedReviews.findFirst();
		while (true) {
			Review r = selectedReviews.retrieve();
			System.out.println(r);

			Product p = findProductById(r.getProductId());
			if (p != null) {
				System.out.println(p);
			} else {
				System.out.println("(Product not found for productId=" + r.getProductId() + ")");
			}
			System.out.println();

			if (selectedReviews.last()) break;
			selectedReviews.findNext();
		}

		return selectedReviews;
	}

	//Get an average rating for product.
	public static float avgRating(int pid) {
		int counter = 0;
		float rate = 0;

		reviews.findFirst();
		while (!reviews.last()) {
			if (reviews.retrieve().getProductId() == pid) {
				counter++;
				rate += reviews.retrieve().getRatingScore();
			}
			reviews.findNext();
		}
		if (reviews.retrieve().getProductId() == pid) {
			counter++;
			rate += reviews.retrieve().getRatingScore();
		}

		return (rate / counter);
	}

	public static void main(String[] args) {

		loadData();
		int choice;
		do {
			choice = menu1();
			switch (choice) {

			case 1:
				ProductMenu();
				break;
			case 2:
				CustomersMenu();
				break;
			case 3:
				OrdersMenu();
				break;

			case 4:
				ReviewMenu();
				break;
			case 5:
				System.out.println("Thank You");
				break;
			default:
				System.out.println("Invalid choice, Try again");
				menu1();
			}
		} while (choice != 5);

	}
}