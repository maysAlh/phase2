package inventory;

 public class Product {

    private String  name;
    private double price;
    private int productId , stock;
    private int priority;
   
    private ReviewList<Review> review = new ReviewList<>();

    public Product() {
    	productId= 0;
    	name= "";
    	stock= 0;
    	priority= 0;
    }
    
    public Product(int id, String n, double pr, int s , int p) {
        productId = id;
        name = n;
        price = pr;
        stock = s;
        priority= p;
    }

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

	public ReviewList<Review> getReview() {
		return review;
	}

	public void setReview(ReviewList<Review> review) {
		this.review = review;
	}
    
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int p) {
		priority= p;
		}
	
	
	public String toString() {
	    String reviewStr = review.toString();
	    return "ID: " + productId +
	           ", Name: " + name +
	           ", Price: " + price +
	           ", Stock: " + stock +
	           ", Priority: " + priority +
	           ", Reviews: " + reviewStr+"\n";
	}

}