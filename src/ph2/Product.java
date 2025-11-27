package ph2;

import java.util.Scanner;

public class Product {

    private String name;
    private double price;
    private int productId, stock;
    
    private LinkedList<Review> review = new LinkedList<>();
    private static LinkedList<Product> products = new LinkedList<>();

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

    public void add() { // add product
        Scanner input = new Scanner(System.in);

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
        products.insert(newProduct);

        System.out.println("Product added successfully.");
    }


    public void update() {//update product
        System.out.print("Enter product's ID to update: ");
        int updatedId = input.nextInt();
        input.nextLine();

        Node<Product> current = products.getHead();
        boolean foundUpdate = false;

        while (current != null) {
            Product Pdata = current.getData();
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
                foundUpdate = true;
                break;
            }
            current = current.getNext();
        }
        if (!foundUpdate)
            System.out.println("No product found with ID: " + updatedId);
        return;
    }

    public Product removeProduct() {

        
        if (products == null || products.getHead() == null) {
            System.out.println("empty Products data");
            return null;
        }

        System.out.print("Enter product ID: ");
        int productID = input.nextInt();

        Node<Product> current = products.getHead();
        Node<Product> previous = null; 

       
        while (current != null && current.getData().getProductId() != productID) {
            previous = current;
            current = current.getNext();
        }

        
        if (current == null) {
            System.out.println("No such product ID");
            return null;
        }

       
        Product p = current.getData();

      
        if (previous == null) {
           
            products.setHead(current.getNext());
        } else {
            
            previous.setNext(current.getNext());
        }

       
        p.setStock(-1);
        return p;
    }


    public Product searchProducID() {
        if (products.empty()) {
            System.out.println("empty Products data");
            return null;
        }

        System.out.print("Enter product ID: ");
        int productID = input.nextInt();

        products.findFirst();
        for (int i = 0; i < products.size(); i++) {
            if (products.retrieve().getProductId() == productID)
                return products.retrieve();
            products.findNext();
        }

        System.out.println("No such product ID");
        return null;
    }

    
    
    public Product searchProducName() {
        if (products.empty()) {
            System.out.println("empty Products data");
            return null;
        }

        System.out.print("Enter product Name: ");
        input.nextLine();
        String name = input.nextLine();

        products.findFirst();
        for (int i = 0; i < products.size(); i++) {
            if (products.retrieve().getName().compareToIgnoreCase(name) == 0)
                return products.retrieve();
            products.findNext();
        }

        System.out.println("No such product Name");
        return null;
    }

    
 //=======================================================
    
    // Print only products that are out of stock (stock <= 0)
    public void trackOutOfStockProduct() {
        Node<Product> cur = products.getHead();
        boolean any = false;
        while (cur != null) {
            Product p = cur.getData();
            if (p.getStock() <= 0) {
                System.out.println(p);
                any = true;
            }
            cur = cur.getNext();
        }
        if (!any) System.out.println("No out-of-stock products.");
    }

    public void linkReviewsToProducts(LinkedList<Product> products, LinkedList<Review> reviews) {
        Node<Review> currentReview = reviews.getHead();

        while (currentReview != null) {
            Review review = currentReview.getData();
            Node<Product> currentProduct = products.getHead();

            while (currentProduct != null) {
                Product product = currentProduct.getData();

                if (product.getProductId() == review.getProductId()) {
                    review.setReviewedProduct(product); // ensure link back to product
                    if (product.getReview() == null) {
                        product.setReview(new LinkedList<Review>());
                    }
                    product.getReview().insert(review);
                    break;
                }
                currentProduct = currentProduct.getNext();
            }

            currentReview = currentReview.getNext();
        }
    }

    public static void viewAllProducts(LinkedList<Product> products) {
        Node<Product> current = products.getHead();
        if (current == null) {
            System.out.println("No products available.");
            return;
        }

        while (current != null) {
            Product p = current.getData();
            System.out.println("ID: " + p.getProductId() + ", Name: " + p.getName() +
                    ", Price: " + p.getPrice() + ", Stock: " + p.getStock() + ", Reviews:");

            if (p.getReview() != null && !p.getReview().empty()) {
                Node<Review> reviewNode = p.getReview().getHead();
                while (reviewNode != null) {
                    System.out.println(reviewNode.getData());
                    reviewNode = reviewNode.getNext();
                }
            } else {
                System.out.println("No reviews yet.");
            }
            current = current.getNext();
        }
    }

    
   

    //==================================
    public boolean checkProductID(int PID) {
        if (products == null || products.getHead() == null) return false;
        Node<Product> cur = products.getHead();
        while (cur != null) {
            Product p = cur.getData();
            if (p != null && p.getProductId() == PID) return true;
            cur = cur.getNext();
        }
        return false;
    }
//=============================================   
    
    public Product getProductData(int pID) {
        if (products.empty()) return null;

        products.findFirst();
        while (true) {
            Product p = products.retrieve();
            if (p.getProductId() == pID)
                return p;

            if (products.last())
                break;
            products.findNext();
        }
        return null;
    }


//=====================================================
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

  
    @Override
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


    

    public static LinkedList<Product> getProducts() {
		return products;
	}

	public static void setProducts(LinkedList<Product> prods) {
        products = prods;
    }
}