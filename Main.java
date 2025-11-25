package inventory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
  
  public static Scanner input = new Scanner(System.in);
  
        
  public static customersData loederC = new customersData( "C:/Users/mysal/OneDrive/Desktop/customers.csv");                                        
  public static orderDate loaderO = new orderDate("C:/Users/mysal/OneDrive/Desktop/orders.csv");
  public static ProductData loaderP= new ProductData("C:/Users/mysal/OneDrive/Desktop/prodcuts.csv");
 
  public static ReviewData loaderR= new ReviewData("C:/Users/mysal/OneDrive/Desktop/reviews.csv");
         
          public static LinkedList<Customer> customers;
          public static LinkedList<Order> orders;
          public static ProductPriorityQueue<Product> products = new ProductPriorityQueue<>();
          public static ReviewList<Review> reviews= new ReviewList<>();
          
          public static Order odata = new Order();
          public static Customer Cdata = new Customer();
          public static Product Pdata= new Product();
          public static Review Rdata= new Review();
          
          public static void loadData() {
              System.out.println("loading data from CSVs files");

              customers = loederC.getCustomers();
              Customer.setCustomers(customers); 
              
              orders = loaderO.getOrders();
              Order.setOrders(orders);
              
              products = loaderP.getProduct();
             // Product
              reviews = loaderR.getReviews();
             // Review.
              //------------------------------------------------
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

              // Load all products
              Node<PQElement<Product>> current = products.getHead();
              while (current != null) {
                  Pdata = current.getData().data;
                  current = current.getNext();
              }

              // Load all reviews
              reviews.findFirst();
              while (reviews.retrieve() != null) {
                  Rdata = reviews.retrieve();
                  reviews.findNext();
              }

              // links reviews with their products
              linkReviewsToProducts(products, reviews);
          }


              private static void linkReviewsToProducts(ProductPriorityQueue products, ReviewList<Review> reviews) {
              Node<Review> currentReview = reviews.getHead();
              while (currentReview != null) {
                  Review review = currentReview.getData();
                  Node<PQElement<Product>> currentProduct = products.getHead();
                  while (currentProduct != null) {
                      Product product = currentProduct.getData().data;
                      if (product.getProductId() == review.getProductId()) {
                          product.getReview().insert(review);
                          break;
                      }
                      currentProduct= currentProduct.getNext();
                  }
                  currentReview = currentReview.getNext();
              }
          }
          public static void addCustomerReview() {
              System.out.println("------ Add Review ------");

              System.out.print("Enter Customer ID: ");
              int customerId = input.nextInt();

              System.out.print("Enter Product ID: ");
              int productId = input.nextInt();

              System.out.print("Enter Rating (1–5): ");
              int rate = input.nextInt();
              while (rate > 5 || rate < 1) {
                  System.out.print("Please Enter A Valid Rating (1–5): ");
                  rate = input.nextInt();
              }

              System.out.print("Enter Comment: ");
              input.nextLine(); // clean
              String comment = input.nextLine();

              int newReviewId = reviews.getSize() + 501; 

              Review newReview = new Review(newReviewId, productId, customerId, rate, comment);

              Node<PQElement<Product>> currentProductNode = products.getHead();
              boolean found = false;

              while (currentProductNode != null) {
                  Product product = currentProductNode.getData().data;

                  if (product.getProductId() == productId) {
                      product.getReview().insert(newReview);
                      reviews.insert(newReview);
                      newReview.setReviewedProduct(product);
                      found = true;
                      System.out.println("Review added successfully for product: " + product.getName());
                      break;
                  }

                  currentProductNode = currentProductNode.getNext();
              }

              if (!found) {
                  System.out.println("Product ID not found. Review not added.");
              }
          }

          public static int menu1() {
              System.out.println("*********************");
              System.out.println("1. Products");
              System.out.println("2. Customers");
              System.out.println("3. Orders");
              System.out.println("4. View Reviews");
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
              System.out.println("5. View all products");
              System.out.println("6. View out-of-stock products");
              System.out.println("7. Return Main menu");
              System.out.print("Enter your choice: ");
              choice = input.nextInt();

              switch (choice) {
              case 1: // Add product
                  System.out.print("Enter product's ID: ");
                  int id = input.nextInt();
                  input.nextLine();


// Duplicate check
                  if (productIdExists(id)) {
                      System.out.println("This product ID already exists. Please use another ID.");
                      break; 
                  }

                  System.out.print("Enter product's name: ");
                  String name = input.nextLine();

                  System.out.print("Enter product's price: ");
                  double price = input.nextDouble();

                  System.out.print("Enter product's stock: ");
                  int stock = input.nextInt();

                  System.out.print("Enter product's priority: ");
                  int priority = input.nextInt();

                  Product p = new Product(id, name, price, stock, priority);
                  products.enqueue(p);
                  System.out.println("Product added successfully.");
                  break;


                  case 2: // Update product
                	 
                	 // products = loaderP.getProduct();
                	  
                      System.out.print("Enter product's ID to update: ");
                      int updatedId = input.nextInt();
                      input.nextLine();

                      Node<PQElement<Product>> current = products.getHead();
                      boolean foundUpdate = false;

                      while (current != null) {
                          Product Pdata = current.getData().data;
                          if (Pdata.getProductId() == updatedId) {
                              System.out.print("Enter new name: ");
                              String updatedName = input.nextLine();

                              System.out.print("Enter new price: ");
                              double updatedPrice = input.nextDouble();

                              System.out.print("Enter new stock: ");
                              int updatedStock = input.nextInt();

                              System.out.print("Enter new priority: ");
                              int updatedPriority = input.nextInt();

                              Pdata.setName(updatedName);
                              Pdata.setPrice(updatedPrice);
                              Pdata.setStock(updatedStock);
                              Pdata.setPriority(updatedPriority);

                              System.out.println("Product updated successfully.");
                              foundUpdate = true;
                              break;
                          }
                          current = current.getNext();
                      }
                      if (!foundUpdate)
                          System.out.println("No product found with ID: " + updatedId);
                      break;

                  case 3: // Remove product
                      System.out.print("Enter product's ID to remove: ");
                      int removedId = input.nextInt();
                      input.nextLine();

                      current = products.getHead();
                      boolean foundRemove = false;

                      while (current != null) {
                          Product Pdata = current.getData().data;
                          if (Pdata.getProductId() == removedId) {
                              products.serve(); // removes the head
                              System.out.println(" Product removed successfully.");
                              foundRemove = true;
                              break;
                          }
                          current = current.getNext();
                      }
                      if (!foundRemove)
                          System.out.println("No product found with ID: " + removedId);
                      break;

                  case 4: // search by product id
                      System.out.print("Enter product's ID : ");
                      int viewedId = input.nextInt();
                      input.nextLine();

                      current = products.getHead();
                      boolean foundView = false;

                         while (current != null) {
                          Product Pdata = current.getData().data;
                          if (Pdata.getProductId() == viewedId) {
                              System.out.println(Pdata);
                              foundView = true;
                              break;
                          }
                          current = current.getNext();
                      }
                      if (!foundView)
                          System.out.println("No product found with ID: " + viewedId);
                      break;

                  case 5: // View all products
                      current = products.getHead();
                      if (current == null) {
                          System.out.println("No products in the queue!");
                      } else {
                          System.out.println("---- All Products ----");
                          while (current != null) {
                              Product Pdata = current.getData().data;
                              System.out.println(Pdata);
                              current = current.getNext();
                          }
                      }
                      break;
                      
                  case 6: {
                      System.out.println("---- Out-of-Stock Products ----");
                      printOutOfStock();
                      break;
                  }

                  case 7: // Return to main menu
                      break;

                  default:
                      System.out.println("Invalid choice!");
              }
              
              }while(choice!=7);
          }

          public static void OrdersMenu() {
            int choice;
            do {
            Order.setOrders(orders);
            
              
              System.out.println("1. Place New Order");
              System.out.println("2. Cancel Order");
              System.out.println("3. Update Order (Status)");
              System.out.println("4. Search By ID(liner)");
              System.out.println("5. All orders between two dates");
              System.out.println("6. Return Main menu");
              System.out.println("Enter your choice:");
              choice = input.nextInt();

              switch (choice) {
                  case 1:
                     
                      PlaceOrder();
                      break;

                  case 2:
                      CancelOrder();
                        break;

                    case 3: {
                        System.out.println("update to new status...");
                        System.out.println("Enter order ID: ");
                        int orderID = input.nextInt();
                        boolean ok = odata.UpdateOrder(orderID); 
                        if (!ok) System.out.println("Update failed.");
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
                      System.out.println("Enter first date (dd/MM/yyyy)");
                      String date1 = input.next();

                      System.out.println("Enter second date (dd/MM/yyyy)");
                      String date2 = input.next();
                      
                      odata.betweenTwoDates(date1, date2); 
                  }
                  break;

                  case 6:
                     
                      break;


                      default:
                      System.out.println(" Return to Main menu");
              }
              
            }while(choice!=6);
          }
          // =====================================================
          public static void CustomersMenu()
         
          {
              int choice;
            do {
             Customer.setCustomers(customers); 
             
              
              System.out.println("1. Register new customer");
              System.out.println("2. Place New Order for specific customer");
              System.out.println("3. View Order history  for specific customer");
              System.out.println("4. Add Review");
              System.out.println("5. Return Main menu");
              System.out.println("Enter your choice:");
              choice = input.nextInt();;
              switch (choice)
              {
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
                    addCustomerReview();
                    break;
              
                  case 5:
                      break;
                  default:
                      System.out.println(" Return to Main menu");
              }
            } while(choice!=5) ;
          }
          // =====================================================
          public static void ReviewMenu() {
            int choice;
            System.out.println("1. View Reviews");
            System.out.println("2. View Top 3 Reviewed Products"); 
            System.out.println("3. List Of common Products Reviewed By Two Customer"); 
            System.out.println("4. edit reviews"); 
            
            System.out.println("Enter Your Choice");
            choice= input.nextInt();
            switch(choice) {
            
            case 1: 
              reviews.findFirst();
              System.out.println("------Reviews List ------\n");
              while(reviews.retrieve()!= null) {
                System.out.println(reviews.retrieve());
                reviews.findNext();
              }
              break;
            case 2:
               TopThreeProducts();
                break;
            case 3:
              System.out.println("Enter the ID of the first customer: ");
              int id1 = input.nextInt();
              System.out.println("Enter the ID of the second customer: ");
              int id2 = input.nextInt();

              System.out.println("\n*****************");
              System.out.println("list of common products that have been reviewed with an average rating of more than 4 out of 5:\n");

              ProductPriorityQueue result = commonReviewed(id1, id2);
              System.out.println(result.toString());
              break;
            case 4:
              
               input.nextLine(); // consume newline

                  System.out.print("Product name to edit: ");
                  String productName = input.nextLine().trim();

                  System.out.print("New rating (1-5): ");
                  int newRating = input.nextInt();
                  input.nextLine();


                  System.out.print("New comment: ");
                  String newComment = input.nextLine();
                 
                  Review review = new Review();
                  review.edit(reviews,  productName, newRating, newComment);
                  break;
            
            }
          }
          
          public static ProductPriorityQueue commonReviewed(int ID1, int ID2) {
              ProductPriorityQueue result = new ProductPriorityQueue();
              
              ReviewList<Review> reviews1 = new ReviewList<>();
              ReviewList<Review> reviews2 = new ReviewList<>();
              
              Node<Review> current = reviews.getHead();
              while (current != null) {
                  Review rev = current.getData();
                  if (rev.getCustomerId() == ID1)
                      reviews1.insert(rev);
                  else if (rev.getCustomerId() == ID2)
                      reviews2.insert(rev);
                  current = current.getNext();         
              }

              boolean anyCommon = false; 
              boolean anyHighRated = false; 
              Node<Review> r1 = reviews1.getHead();

              while (r1 != null) {
                  int productId1 = r1.getData().getProductId();
                  Node<Review> r2 = reviews2.getHead();
                  
                  while (r2 != null) {
                      int productId2 = r2.getData().getProductId();
                      
                      if (productId1 == productId2) {
                          anyCommon = true;

                          Review rev1 = r1.getData();
                          Review rev2 = r2.getData();

                          int rating1 = rev1.getRatingScore();
                          int rating2 = rev2.getRatingScore();
                          int avg = (rating1 + rating2) / 2;

                          if (avg > 4) {
                              anyHighRated = true;
                              Node<PQElement<Product>> pNode = products.getHead();
                              while (pNode != null) {
                                  Product p = pNode.getData().data;
                                  if (p.getProductId() == productId1) {
                                      result.enqueue(p);
                                      break;
                                  }
                                  pNode = pNode.getNext();
                              }
                          }
                          break;
                      }
                      r2 = r2.getNext();
                  }
                  r1 = r1.getNext();
              }

              if (!anyCommon)
                  System.out.println("Invalid ID. Or the two customers dosen't share a reviewed product");
              else if (!anyHighRated)
                  System.out.println("One OR Both of the averages rating is less than 5");

              return result;
          }

          public static void TopThreeProducts() {
              Product P1 = null, P2 = null, P3 = null;
              double avg1 = 0, avg2 = 0, avg3 = 0;

              Node<PQElement<Product>> currentProduct = products.getHead();

              while (currentProduct != null) {
                  Product product = currentProduct.getData().data;

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

              
              System.out.println("******************\nTop 3 Products by Rating:\n");
              if (P1 != null) System.out.println(P1.getName() + " — Average: " + avg1);
              if (P2 != null) System.out.println(P2.getName() + " — Average: " + avg2);
              if (P3 != null) System.out.println(P3.getName() + " — Average: " + avg3);
          }

          //=======================================================
       
          
          
          
          
          //=======================================================
         
          
          public static void PlaceOrder() {
              try {
                  Order.setOrders(orders);
                  Customer.setCustomers(customers);

                  Order new_order = new Order();
                  double total_price = 0.0;

                  System.out.println("\n====================");
                  System.out.println("   Create New Order");
                  System.out.println("====================");

                  // Order ID
                  System.out.print("Order ID: ");
                  int oid = input.nextInt();
                  while (odata.checkOrderID(oid)) {
                      System.out.print("This Order ID is already in use. Enter another ID: ");
                      oid = input.nextInt();
                  }
                  new_order.setOrderId(oid);

                  // Customer ID
                  System.out.print("Customer ID: ");
                  int cid = input.nextInt();
                  while (!Cdata.checkCustomerID(cid)) {
                      System.out.print("No matching customer ID. Try again: ");
                      cid = input.nextInt();
                  }
                  new_order.setCustomerReference(cid);

                  // Add products — or 0 to stop
                  while (true) {
                      System.out.print("Product ID to add (or 0 to finish): ");
                      int pid = input.nextInt();
                      if (pid == 0) break; // // stop adding product

                      boolean found = false;
                      Node<PQElement<Product>> cur = products.getHead();

                      while (cur != null) {
                          Product pp = cur.getData().data;

                          if (pp.getProductId() == pid) {
                              if (pp.getStock() <= 0) {
                                  System.out.println("• Out of stock – choose a different product.");
                              } else {
                                  pp.setStock(pp.getStock() - 1);
                                  new_order.addProduct(pp.getProductId());

                                 
                                  total_price += pp.getPrice();
                                  System.out.println("Added. Total now: " + String.format("%.2f", total_price));

                                  found = true;
                              }
                              break;
                          }
                          cur = cur.getNext();
                      }

                      if (!found) {
                          System.out.println("• No product found with ID " + pid + ".");
                      }
                  
                  // Date
                  System.out.print("Order date (dd/MM/yyyy): ");
                  java.time.format.DateTimeFormatter formatter =
                          java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                  java.time.LocalDate Ldate = java.time.LocalDate.parse(input.next(), formatter);
                  new_order.setDate(Ldate);

                  // Status
                  System.out.print("Status (pending/shipped/delivered/cancelled): ");
                  new_order.setStatus(input.next());

                  // Set total
                  new_order.setTotalPrice(total_price);

                  // Save order
                  orders.insert(new_order);

                  // Link order to customer
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
                  
                  }

                  } catch (InputMismatchException ex) {
                  System.out.println("Invalid input detected. The order was not created.");
                  input.nextLine(); // clear buffer
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

                      Node<PQElement<Product>> cur = products.getHead();
                      while (cur != null) {
                          PQElement<Product> elem = cur.getData();
                          Product pp = elem.data;

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
          
          //=====================
       // Checks if a product ID already exists in the products queue
          private static boolean productIdExists(int targetId) {
              Node<PQElement<Product>> cur = products.getHead();
              while (cur != null) {
                  Product data = cur.getData().data;
                  if (data.getProductId() == targetId) return true;
                  cur = cur.getNext();
              }
              return false;
          }

          
//================================================================================

       // Print only products that are out of stock (stock <= 0)
          private static void printOutOfStock() {
              Node<PQElement<Product>> cur = products.getHead();
              boolean any = false;
              while (cur != null) {
                  Product p = cur.getData().data;
                  if (p.getStock() <= 0) {
                      System.out.println(p);
                      any = true;
                  }
                  cur = cur.getNext();
              }
              if (!any) System.out.println("No out-of-stock products.");
          }

//================================================================================

          public static void main(String[] args) {
             
            loadData();
              int choice;
              do {
                      choice = menu1();
                     switch (choice)
                      {
                         
                     
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
              }while(choice!=5);
              
          }
}