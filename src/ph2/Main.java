
package ph2;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    public static Scanner input = new Scanner(System.in);

    public static customersData cdata = new customersData("C:/Users/hamsa/Desktop/projectData/dataset/customers.csv");

    public static orderDate loaderO = new orderDate("C:/Users/hamsa/Desktop/projectData/dataset/orders.csv");
    public static ProductData loaderP = new ProductData("C:/Users/hamsa/Desktop/projectData/dataset/prodcuts.csv");

    public static ReviewData loaderR = new ReviewData("C:/Users/hamsa/Desktop/projectData/dataset/reviews.csv");
    public static AVL<Integer, Customer> customers;
    public static AVL<Integer, Order> orders;
    public static AVL<Integer, Product> products;
    public static LinkedList<Review> reviews;

    public static Order odata = new Order();
    public static Customer Cdata = new Customer();
    public static Product Pdata = new Product();
    public static Review Rdata = new Review();

    public static void loadData() {
        System.out.println("loading data from CSV files...");

        customers = cdata.getCustomers();
        orders = loaderO.getOrders();
        products = loaderP.getProducts();
        reviews = loaderR.getReviews();

        // SYNC FIX: Copy Main.products to Product class static AVL
        if (products != null && !products.empty()) {
            Product.setProducts(products);
        }

        // SYNC FIX: Copy Main.reviews to Review class static list
        if (reviews != null && !reviews.empty()) {
            Review.setReviews(reviews);
        }

        // SYNC FIX for orders: If Order class has static orders, copy to it
        // This assumes Order.orders is accessible or there's another way to sync
        if (orders != null && !orders.empty()) {
            // If Order class has a way to set orders, use it
            // Otherwise, we'll handle synchronization in each method
        }

        // Link orders to customers using inOrder traversal
        if (!customers.empty() && !orders.empty()) {
            LinkedList<Order> allOrders = orders.inOrdertraverseData();
            allOrders.findFirst();
            while (true) {
                Order od = allOrders.retrieve();
                int cid = od.getCustomerReference();

                if (customers.findkey(cid)) {
                    Customer c = customers.retrieve();
                    c.addOrder(od.getOrderId());
                }

                if (allOrders.last())
                    break;
                allOrders.findNext();
            }
        }
        
        Pdata.linkReviewsToProducts(reviews);

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
            System.out.println("------ Product Menu ------");
            System.out.println("Choose from the following:");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product");
            System.out.println("3. Remove Product");
            System.out.println("4. Search by product id");
            System.out.println("5. Search by product name");
            System.out.println("6. List All Products Within a Price Range[minPrice, maxPrice]");
            System.out.println("7. View all products");
            System.out.println("8. View out-of-stock products");
            System.out.println("9. Display All Customers Who Reviewed specific product sorted by RATING.");
            System.out.println("10. Display All Customers Who Reviewed specific product sorted by CUSTOMER ID.");
            System.out.println("11. Return Main menu");
            System.out.print("Enter your choice: ");


     choice = input.nextInt();
     input.nextLine(); 
    

            switch (choice) {
                case 1: // Add product
                    Pdata.add();
                    
                    products = Product.getProducts();
                    break;

                case 2: // Update product
                    Pdata.update();
                    
                    products = Product.getProducts();
                    break;

                case 3: {
                    System.out.println("Note: products linked to orders or reviews will be archived rather than permanently deleted.");
                    Product p = Pdata.removeProduct();
                    if (p == null) {
                        break;
                    }

                    boolean deleted = true;
                    if (p.getReview() == null || p.getReview().empty()) {
                        boolean found = false;
                        LinkedList<Order> allOrders = orders.inOrdertraverseData();

                        if (!allOrders.empty()) {
                            allOrders.findFirst();
                            while (true) {
                                Order ord = allOrders.retrieve();
                                AVL<Integer, Integer> prodTree = ord.getProducts();
                                LinkedList<Integer> prodIds = prodTree.inOrdertraverseData();

                                if (!prodIds.empty()) {
                                    prodIds.findFirst();
                                    while (true) {
                                        if (prodIds.retrieve() == p.getProductId()) {
                                            found = true;
                                            break;
                                        }
                                        if (prodIds.last()) break;
                                        prodIds.findNext();
                                    }
                                }

                                if (found || allOrders.last()) break;
                                allOrders.findNext();
                            }
                        }

                        if (found) {
                            products.insert(p.getProductId(), p);
                            System.out.println("Product has previous orders; it has been archived instead of deleted.");
                            deleted = false;
                        }
                    } else {
                        products.insert(p.getProductId(), p);
                        System.out.println("Product has existing reviews; it has been archived instead of deleted.");
                        deleted = false;
                    }

                    if (deleted)
                        System.out.println("Product removed successfully.");
                    
                    // SYNC FIX: After remove, sync both AVLs
                    products = Product.getProducts();
                    break;
                }

                case 4: {
                    Product pro = Pdata.searchProducID();
                    if (pro != null)
                        System.out.println(pro);
                    break;
                }

                case 5: {
                    System.out.print("Enter product Name: ");
                    String name = input.nextLine().trim();   // نقرأ الاسم هنا

                    Product pro = Pdata.searchProducName(name); // نمرّر الاسم للدالة
                    if (pro != null)
                        System.out.println(pro);
                    break;
                }


                case 6: {
                    System.out.println("Enter range [minPrice, maxPrice]:");
                    System.out.print("minPrice: ");
                    double minPrice = input.nextDouble();
                    System.out.print("maxPrice: ");
                    double maxPrice = input.nextDouble();

                    while (minPrice >= maxPrice) {
                        System.out.println("Re-enter range [minPrice less than maxPrice]:");
                        System.out.print("minPrice: ");
                        minPrice = input.nextDouble();
                        System.out.print("maxPrice: ");


                     maxPrice = input.nextDouble();
                    }
                    LinkedList<Product> data = Pdata.productsInPriceRange(minPrice, maxPrice);
                    if (data.empty())
                        System.out.println("No products in this range");
                    else
                        data.print();
                    break;
                }

                case 7: // View all products
                    Pdata.viewAllProducts();
                    break;

                case 8:
                    System.out.println("---- Out-of-Stock Products ----");
                    Pdata.trackOutOfStockProduct();
                    break;

                case 9:
                    customersReviewedSortedByRating();
                    break;

                case 10:
                    customersReviewedSortedByCustomerID();
                    break;

                case 11:
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 11);
    }

    //-----------------------------------------------
    public static void customersReviewedSortedByRating() {
        System.out.print("Enter product ID: ");
        int pID = input.nextInt();

        while (!products.findkey(pID)) {
            System.out.println("Product ID not available, re-enter again:");
            pID = input.nextInt();
        }

        Product p = products.retrieve();
        LinkedList<Review> reviewsList = p.getReview();

        if (reviewsList == null || reviewsList.empty()) {
            System.out.println("No reviews are available for this product.");
            return;
        }

        LinkedPQ<Review> ratingPQ = new LinkedPQ<Review>();
        reviewsList.findFirst();
        while (true) {
            Review r = reviewsList.retrieve();
            ratingPQ.enqueue(r, r.getRatingScore());
            if (reviewsList.last()) break;
            reviewsList.findNext();
        }

        System.out.println("Customers who reviewed product " + pID + " sorted by rating:");
        System.out.println("-----------------------------------------------------------");

        while (ratingPQ.length() > 0) {
            PQElement elem = ratingPQ.serve();
            Review r = (Review) elem.data;
            System.out.println("Customer ID: " + r.getCustomerId());
            System.out.println("Rating: " + elem.priority);
            System.out.println(r);
            System.out.println();
        }
    }

    //-------------------------------------------------
    public static void customersReviewedSortedByCustomerID() {
        AVL<Integer, Customer> cIDs = new AVL<Integer, Customer>();
        System.out.print("Enter product ID: ");
        int pID = input.nextInt();

        while (!products.findkey(pID)) {
            System.out.println("Product ID not available, Re-enter again");
            pID = input.nextInt();
        }

        if (reviews.empty()) {
            System.out.println("No reviews are available");
            return;
        }

        reviews.findFirst();
        while (true) {
            Review r = reviews.retrieve();
            if (r.getProductId() == pID) {
                int cId = r.getCustomerId();
                if (customers.findkey(cId)) {
                    Customer c = customers.retrieve();
                    cIDs.insert(cId, c);
                }
            }
            if (reviews.last()) break;
            reviews.findNext();
        }
        cIDs.printKeys_Data();
    }

    //--------------------------------------------------
    public static void OrdersMenu() {
        int choice;
        do {
            System.out.println("------ Order Menu ------");
            System.out.println("Choose from the following:");
            System.out.println("1. Place New Order");
            System.out.println("2. Cancel Order");
            System.out.println("3. Update Order (Status)");
            System.out.println("4. Search By ID");
            System.out.println("5.All orders between two dates");
            System.out.println("6. Return Main menu");
            System.out.println("Enter your choice:");
            choice = input.nextInt();

            switch (choice) {
                case 1:
                    PlaceOrder();
                    break;
                case 2:
                    System.out.println("Enter order id:");
                    int oid = input.nextInt();
                    
                    //Check order status before cancellation
                    if (orders.findkey(oid)) {
                        Order orderBefore = orders.retrieve();
                        System.out.println("Order status before cancellation: " + orderBefore.getStatus());
                    }
                    
                    odata.cancelOrder(oid);
                    
                    // Check order status after cancellation
                    if (orders.findkey(oid)) {
                        Order orderAfter = orders.retrieve();
                        System.out.println("Order status after cancellation: " + orderAfter.getStatus());
                    }
                    break;
                case 3: {
                    System.out.println("Enter order id:");
                    int oid1 = input.nextInt();
                    odata.UpdateOrder(oid1);
                    break;
                }
                case 4: {
                    System.out.println("Enter order id:");
                    int oid2 = input.nextInt();
                    if (orders.findkey(oid2))
                        System.out.println(orders.retrieve());
                    else
                        System.out.println("No order id");
                    break;
                }
                case 5: {
                    System.out.println("Enter first date (dd/MM/yyyy)");
                    String date1 = input.next();
                    System.out.println("Enter second date (dd/MM/yyyy)");
                    String date2 = input.next();
                    AVL<Date, Order> o = odata.BetweenTwoDates(date1, date2);
                    o.printData();
                    break;
                }
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
            System.out.println("------ Customer Menu ------");
            System.out.println("Choose from the following:");
            System.out.println("1. Register new customer");
            System.out.println("2. Search for Customer by IDs");
            System.out.println("3. Place New Order for specific customer");
            System.out.println("4. View Order history for specific customer");
            System.out.println("5. Extract reviews from a specific customer for all products.");
            System.out.println("6. List All Customers Sorted Alphabetically.");
            System.out.println("7. List All Customers");
            System.out.println("8. Return Main menu");
            System.out.println("Enter your choice:");
            choice = input.nextInt();

            switch (choice) {
                case 1:
                    Cdata.RegisterCustomer();
                    break;
                case 2:
                    Cdata.getCustomerID();
                    break;
                case 3:
                    PlaceOrder();
                    break;
                case 4:
                    Cdata.OrderHistory();
                    break;
                case 5:
                    AllReviewsForCustomer();
                    break;
                case 6:
                    Cdata.printNamesAlphabetically();
                    break;
                case 7:
                    customers.printKeys_Data();
                    break;
                case 8:
                    break;
                default:
                    System.out.


println("Return to Main menu");
            }
        } while (choice != 8);
    }

    public static void ReviewMenu() {
        int choice;
        do {
            System.out.println("------ Review Menu ------");
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
                
                // FIX: Sync without duplicates - replace the entire list
                LinkedList<Review> reviewClassReviews = Review.getReviews();
                if (reviewClassReviews != null) {
                    reviews = reviewClassReviews;
                }
                
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
                    System.out.println("------ Reviews List ------");
                    while (reviews.retrieve() != null) {
                        System.out.println(reviews.retrieve());
                        reviews.findNext();
                    }
                    break;
                case 3:
                    TopThreeProducts();
                    break;
                case 4: {
                    System.out.println("Enter product ID to Get an average rating:");
                    int pid = input.nextInt();
                    while (!Pdata.checkProductID(pid)) {
                        System.out.println("Re-Enter product id again (ID is not available)...");
                        pid = input.nextInt();
                    }
                    float AVG = avgRating(pid);
                    System.out.println("Average Rating for " + pid + " is " + AVG);
                    break;
                }
                case 5: {
                    Customer c1 = Cdata.getCustomerID();
                    Customer c2 = Cdata.getCustomerID();
                    if (c1 != null && c2 != null) commonReviewed(c1.getId(), c2.getId());
                    break;
                }
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
                        System.out.


print("New rating (1-5): ");
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
                    System.out.println("Return to Main menu");
            }
        } while (choice != 7);
    }

    //==============================================================================================
    public static void commonReviewed(int cid1, int cid2) {
        LinkedList<Integer> pcustomer1 = new LinkedList<Integer>();
        LinkedList<Integer> pcustomer2 = new LinkedList<Integer>();

        // FIX: Use Main.reviews directly instead of Rdata.getReviews()
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
                    if (pcustomer1.retrieve() == p) {
                        found1 = true;
                        break;
                    }
                    pcustomer1.findNext();
                }
                pcustomer1.findFirst();
                if (!found1) pcustomer1.insert(p);
            }

            if (c == cid2) {
                pcustomer2.findFirst();
                boolean found2 = false;
                for (int x = 1; x <= pcustomer2.size(); x++) {
                    if (pcustomer2.retrieve() == p) {
                        found2 = true;
                        break;
                    }
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
                    if (pid == pcustomer2.retrieve()) {
                        inSecond = true;
                        break;
                    }
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
        if (ids == null || ids.empty()) {
            System.out.println();
            return;
        }
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
        if (reviews == null || reviews.empty()) {
            System.out.println("No products have reviews yet.");
            return;
        }

        // Arrays to store top 3 products and their ratings
        Product[] topProducts = new Product[3];
        double[] topRatings = new double[3];
        
        // Get all unique product IDs from reviews
        LinkedList<Integer> reviewedProductIds = new LinkedList<>();
        
        reviews.findFirst();
        while (true) {
            Review review = reviews.retrieve();
            int productId = review.getProductId();
            
            // Check if we've already processed this product
            boolean alreadyProcessed = false;
            if (!reviewedProductIds.empty()) {
                reviewedProductIds.findFirst();
                while (true) {
                    if (reviewedProductIds.retrieve() == productId) {
                        alreadyProcessed = true;
                        break;
                    }
                    if (reviewedProductIds.last()) break;
                    reviewedProductIds.findNext();
                }
            }
            
            if (!alreadyProcessed) {
                reviewedProductIds.insert(productId);
                
                // Calculate average rating for this product
                double totalRating = 0;
                int reviewCount = 0;
                
                // Go through all reviews for this product
                reviews.findFirst();
                while (true) {
                    Review r = reviews.retrieve();
                    if (r.getProductId() == productId) {
                        totalRating += r.getRatingScore();
                        reviewCount++;
                    }
                    if (reviews.last()) break;
                    reviews.findNext();
                }
                
                double avgRating = totalRating / reviewCount;
                Product product = findProductById(productId);
                
                if (product != null) {
                    // Check if this product should be in top 3
                    if (avgRating > topRatings[0] || topProducts[0] == null) {
                        // Shift current top products down
                        topProducts[2] = topProducts[1];
                        topRatings[2] = topRatings[1];
                        topProducts[1] = topProducts[0];
                        topRatings[1] = topRatings[0];
                        // Insert new top product
                        topProducts[0] = product;
                        topRatings[0] = avgRating;


} else if (avgRating > topRatings[1] || topProducts[1] == null) {
                        // Shift current #2 down to #3
                        topProducts[2] = topProducts[1];
                        topRatings[2] = topRatings[1];
                        // Insert new #2 product
                        topProducts[1] = product;
                        topRatings[1] = avgRating;
                    } else if (avgRating > topRatings[2] || topProducts[2] == null) {
                        // Insert new #3 product
                        topProducts[2] = product;
                        topRatings[2] = avgRating;
                    }
                }
            }
            
            if (reviews.last()) break;
            reviews.findNext();
        }

        System.out.println("*********************");
        System.out.println("Top 3 Products by Rating:\n");
        
        boolean foundAny = false;
        for (int i = 0; i < 3; i++) {
            if (topProducts[i] != null) {
                // Count reviews for this product to display
                int reviewCount = 0;
                reviews.findFirst();
                while (true) {
                    Review r = reviews.retrieve();
                    if (r.getProductId() == topProducts[i].getProductId()) {
                        reviewCount++;
                    }
                    if (reviews.last()) break;
                    reviews.findNext();
                }
                
                System.out.println((i + 1) + ") " + topProducts[i].getName() + 
                                 " — Average: " + String.format("%.1f", topRatings[i]) +
                                 " (" + reviewCount + " reviews)");
                foundAny = true;
            }
        }
        
        if (!foundAny) {
            System.out.println("No products have reviews yet.");
        }
    }

    //============================================================================================
    public static void PlaceOrder() {
        try {
            Customer.setCustomers(customers);
            AVL<Integer, Product> productTree = Product.getProducts();
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

                boolean found = productTree.findkey(pid);
                if (!found) {
                    System.out.println(" No product found with ID " + pid + ".");
                } else {
                    Product pp = productTree.retrieve();
                    if (pp.getStock() <= 0) {
                        System.out.println("Out of stock – choose a different product.");
                    } else {
                        pp.setStock(pp.getStock() - 1);
                        new_order.addProduct(pp.getProductId());

total_price += pp.getPrice();
                        System.out.println("Added. Total now: " + String.format("%.2f", total_price));
                    }
                }
            }

            System.out.print("Order date (dd/MM/yyyy): ");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate Ldate = LocalDate.parse(input.next(), formatter); // FIX: Declare Ldate
            new_order.setDate(Ldate);

            System.out.print("Status (pending/shipped/delivered/cancelled): ");
            new_order.setStatus(input.next());

            new_order.setTotalPrice(total_price);
            orders.insert(new_order.getOrderId(), new_order);

            if (customers.findkey(new_order.getCustomerReference())) {
                Customer cust = customers.retrieve();
                cust.addOrder(oid);
            } else {
                System.out.println("Warning: customer not found in customers tree when linking order history.");
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
    //============================================================================
    public static void CancelOrder() {
        System.out.print("Order ID to cancel: ");
        int oid = input.nextInt();

        Order cancel_order = odata.searchOrderID(oid);
        while (cancel_order == null) {
            System.out.print("No order found with this ID. Try another ID: ");
            oid = input.nextInt();
            cancel_order = odata.searchOrderID(oid);
        }

        if (odata.cancelOrder(oid) == 1) {
            int custId = cancel_order.getCustomerReference();
            if (customers.findkey(custId)) {
                Customer cust = customers.retrieve();
                cust.removeOrder(cancel_order.getOrderId());
                customers.update(cust);
            } else {
                System.out.println("Warning: customer with ID " + custId + " not found in customers AVL.");
            }

            AVL<Integer, Integer> prodsTree = cancel_order.getProducts();
            if (prodsTree != null && !prodsTree.empty()) {
                LinkedList<Integer> ids = prodsTree.keysInOrder();
                if (!ids.empty()) {
                    ids.findFirst();
                    while (true) {
                        int pid = ids.retrieve();
                        Product p = findProductById(pid);
                        if (p != null) {
                            p.setStock(p.getStock() + 1);
                        } else {
                            System.out.println("Warning: Product with ID " + pid + " not found when restoring stock.");
                        }
                        if (ids.last()) break;
                        ids.findNext();
                    }
                }
            }
            System.out.println("Order #" + cancel_order.getOrderId() + " has been cancelled.");
        }
    }

    //====================================================================
    private static Product findProductById(int pid) {
        AVL<Integer, Product> prodAVL = Product.getProducts();
        if (prodAVL == null || prodAVL.empty())
            return null;
        if (prodAVL.findkey(pid)) {
            return prodAVL.retrieve();
        }
        return null;
    }

    //==============================================
    public static LinkedList<Review> AllReviewsForCustomer() {
        System.out.println("Enter customer ID:");
        int cid = input.nextInt();
        while (!Cdata.


      checkCustomerID(cid)) {
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

    //===========================================
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

    //=====================================================================================================================================
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