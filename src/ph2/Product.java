
package ph2;

import java.util.Scanner;


public class Product {

    private String name;
    private double price;
    private int productId, stock;

    private LinkedList<Review> review = new LinkedList<>();
    
    private static AVL<Integer, Product> products = new AVL<>();

    private Scanner input = new Scanner(System.in);

    public Product() {
        productId = 0;
        name = "";
        stock = 0;
    }

    public Product(int id, String n, double pr, int s) {
        productId = id;
        name = n;
        price = pr;
        stock = s;
    }

    //==========================================================
    // Add new product (insert in AVL by productId)
    public void add() {
        int id;

        while (true) {
            System.out.print("Enter product's ID: ");
            id = input.nextInt();
            input.nextLine(); 

            if (checkProductID(id)) {
                System.out.println("This ID already exists! Re-enter a new ID:");
            } else {
                break;
            }
        }

        System.out.print("Enter product's name: ");
        String name = input.nextLine();

        System.out.print("Enter product's price: ");
        double price = input.nextDouble();

        System.out.print("Enter product's stock: ");
        int stock = input.nextInt();

        Product newProduct = new Product(id, name, price, stock);
        boolean ok = products.insert(id, newProduct);
        if (ok)
            System.out.println("Product added successfully.");
        else
            System.out.println("Cannot add the product (duplicate key).");
    }

    //==========================================================
    // Update existing product by ID
    public void update() {
        System.out.print("Enter product's ID to update: ");
        int updatedId = input.nextInt();
        input.nextLine();

        boolean found = products.findkey(updatedId);
        if (!found) {
            System.out.println("No product found with ID: " + updatedId);
            return;
        }

        Product Pdata = products.retrieve();
        if (Pdata.getProductId() == updatedId) {
            System.out.print("Enter new name: ");
            String updatedName = input.nextLine();

            System.out.print("Enter new price: ");
            double updatedPrice = input.nextDouble();

            System.out.print("Enter new stock: ");
            int updatedStock = input.nextInt();

            Pdata.setName(updatedName);
            Pdata.setPrice(updatedPrice);
            Pdata.setStock(updatedStock);

            System.out.println("Product updated successfully.");
        }
    }

    //==========================================================
    // Remove product by ID 
    public Product removeProduct() {

        // 1) لو ما فيه ولا منتج في الـ AVL
        if (products.empty()) {
            System.out.println("There are no products to be removed!");
            return null;
        }

        System.out.print("Enter product ID: ");
        int productID = input.nextInt();

        // 2) نبحث عن المنتج في الـ AVL
        boolean found = products.findkey(productID);
        if (!found) {
            System.out.println("No such product ID");
            return null;
        }

        // 3) curr في الـ AVL صار واقف على هذا النود
        Product product = products.retrieve();  // هذا هو المنتج نفسه

        // 4) نحذفه من الشجرة الـ AVL
        boolean removed = products.removeKey(productID);
        if (!removed) {
            System.out.println("Cannot remove the product.");
            return null;
        }

        // 5) نحدّث الـ stock عشان لو أرشفناه نعرف إنه مو متوفر للبيع
        product.setStock(-1);

        return product;
    }


    //==========================================================
    // Search product by ID using AVL
    public Product searchProducID() {
        if (products.empty()) {
            System.out.println("empty Products data");
            return null;
        }

        System.out.print("Enter product ID: ");


       int productID = input.nextInt();

        boolean found = products.findkey(productID);
        if (!found) {
            System.out.println("No such product ID");
            return null;
        }

        return products.retrieve();
    }

    //==========================================================
    // Search product by Name (linear over in-order traversal)
    public Product searchProducName(String name) {
        if (products.empty()) {
            System.out.println("there are no products");
            return null;
        }

        LinkedList<Product> allProduct = products.inOrderTraversal();
        Node<Product> cur = allProduct.getHead();

        while (cur != null) {
            Product p = cur.getData();
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
            cur = cur.getNext();
        }

        System.out.println("No such product Name");
        return null;
    }

    //==========================================================
    // Print only products that are out of stock (stock <= 0)
    public void trackOutOfStockProduct() {
        if (products.empty()) {
            System.out.println("No products available.");
            return;
        }

        LinkedList<Product> all = products.inOrderTraversal();
        Node<Product> cur = all.getHead();
        boolean any = false;

        while (cur != null) {
            Product p = cur.getData();
            if (p.getStock() <= 0) {
                System.out.println(p);
                any = true;
            }
            cur = cur.getNext();
        }

        if (!any)
            System.out.println("No out-of-stock products.");
    }

    //==========================================================
    // Link each review to its product (using in-order traversal list)
    public void linkReviewsToProducts(LinkedList<Review> reviews) {
        if (products.empty() || reviews == null || reviews.getHead() == null)
            return;

        LinkedList<Product> prodList = products.inOrderTraversal();

        Node<Review> currentReview = reviews.getHead();
        while (currentReview != null) {
            Review reviewObj = currentReview.getData();

            Node<Product> currentProduct = prodList.getHead();
            while (currentProduct != null) {
                Product product = currentProduct.getData();

                if (product.getProductId() == reviewObj.getProductId()) {
                    reviewObj.setReviewedProduct(product);

                    if (product.getReview() == null) {
                        product.setReview(new LinkedList<Review>());
                    }
                    product.getReview().insert(reviewObj);
                    break;
                }
                currentProduct = currentProduct.getNext();
            }

            currentReview = currentReview.getNext();
        }
    }

    //==========================================================
    // View all products from a LinkedList (used after inOrderTraversal)
    public void viewAllProducts() {
        LinkedList<Product> all = products.inOrderTraversal();
        Node<Product> cur = all.getHead();

        if (cur == null) {
            System.out.println("No products available.");
            return;
        }

        while (cur != null) {
            System.out.println(cur.getData());
            cur = cur.getNext();
        }
    }

    
    //==========================================================
    
    public LinkedList<Product> productsInPriceRange(double minPrice, double maxPrice) {
        LinkedList<Product> result = new LinkedList<>();
        LinkedList<Product> all = products.inOrderTraversal();
        Node<Product> cur = all.getHead();

        while (cur != null) {
            Product p = cur.getData();
            if (p.getPrice() >= minPrice && p.getPrice() <= maxPrice) {
                result.insert(p);
            }


cur = cur.getNext();
        }
        return result;
    }

    //==========================================================
    // Check if product ID exists in AVL
    public boolean checkProductID(int PID) {
        return products.findkey(PID);
    }

    //==========================================================
    // Get product object by ID using AVL
    public Product getProductData(int pID) {
        if (!products.findkey(pID)) return null;
        return products.retrieve();
    }

    //==========================================================
    // Getters & Setters

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public LinkedList<Review> getReview() {
        return review;
    }

    public void setReview(LinkedList<Review> review) {
        this.review = review;
    }

    //==========================================================
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(productId)
          .append(" | Name: ").append(name)
          .append(" | Price: ").append(price)
          .append(" | Stock: ").append(stock);

        if (review != null && !review.empty()) {
            sb.append(" | Reviews: [");
            review.findFirst();
            while (true) {
                Review r = review.retrieve();
                sb.append(r.getReviewId());
                if (review.last())
                    break;
                sb.append(" ");
                review.findNext();
            }
            sb.append("]");
        }

        return sb.toString();
    }

    //==========================================================
   
    public static AVL<Integer, Product> getProducts() {
        return products;
    }

    public static void setProducts(AVL<Integer, Product> prods) {
        products = prods;
    }
}