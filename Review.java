package inventory;

public class Review {

    private int ratingScore;
    private String comment;
    private Product reviewedProduct;
    private int customerId, reviewId, productId;

    public Review() {
        this.ratingScore = 0;
        this.comment = "";
    }
    public Review(int rId, int pId, int cId, int rate, String c) {
        this.reviewId = rId;
        this.productId = pId;
        this.customerId = cId;
        this.ratingScore = rate;
        this.comment = c != null ? c.trim() : "";
    }

    public void add(ProductPriorityQueue<Product> products) {
        if (products == null || products.getHead() == null) {
            System.out.println("No products available to add the review.");
            return;
        }

        Node<PQElement<Product>> current = products.getHead();
        boolean found = false;

        while (current != null) {
            PQElement<Product> pqElement = current.getData();
            if (pqElement == null || pqElement.data == null) {
                current = current.getNext();
                continue;
            }

            Product p = pqElement.data;
            if (p.getProductId() == this.productId) {
                this.reviewedProduct = p;
                if (p.getReview() == null) {
                    p.setReview(new ReviewList<Review>());
                }
                p.getReview().insert(this);
                System.out.println("Review added to product: " + p.getName());
                found = true;
                break;
            }
            current = current.getNext();
        }

        if (!found) {
            System.out.println("Product with ID " + productId + " not found. Review not linked.");
        }
    }

    public void edit(ReviewList<Review> reviewsList, String productName, int newRating, String newComment) {
        if (reviewsList == null || reviewsList.getHead() == null) {
            System.out.println("Review list is empty.");
            return;
        }

        Node<Review> tmp = reviewsList.getHead();
        boolean updated = false;

        while (tmp != null) {
            Review rev = tmp.getData();
            if (rev.getReviewedProduct() != null &&
                rev.getReviewedProduct().getName().equalsIgnoreCase(productName)) {
                rev.ratingScore = newRating;
                rev.comment = newComment != null ? newComment.trim() : "";
                System.out.println("Review updated successfully for product: " + productName);
                updated = true;
                break;
            }
            tmp = tmp.getNext();
        }

        if (!updated) {
            System.out.println("No review found for product name: " + productName);
        }
    }

  
    
    
    public int getRatingScore() { return ratingScore; }
    
    public void setRatingScore(int ratingScore) { this.ratingScore = ratingScore; }

    public String getComment() { return comment; }
    
    public void setComment(String comment) { this.comment = comment; }

    public Product getReviewedProduct() { return reviewedProduct; }
    
    public void setReviewedProduct(Product reviewedProduct) { this.reviewedProduct = reviewedProduct; }

    public int getCustomerId() { return customerId; }
    
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getProductId() { return productId; }
    
    public void setProductId(int productId) { this.productId = productId; }

    public int getReviewId() { return reviewId; }
    
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public String toString() {
        return String.format(
            "Review Id: %d  Product Id: %d  Customer Id: %d  Score: %d  Comment: '%s'",
            reviewId, productId, customerId, ratingScore, comment);
    }
}