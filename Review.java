package phase2;

import java.util.Scanner;

public class Review {

	 
    private int ratingScore;
    private String comment;
    private Product reviewedProduct;
    private int customerId, reviewId, productId;

    public static Scanner input = new Scanner(System.in);  
    public static LinkedList<Review> reviews = new LinkedList<>();

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

    // Made static so it can be called as Rdata.AddReview(...)
    public static Review AddReview(int cID, int pID) {
        System.out.println("Enter Review ID :");
        int reviewID = input.nextInt();
        while (checkReviewID(reviewID)) {
            System.out.println("Re- Enter Review ID, already available :");
            reviewID = input.nextInt();
        }        

        System.out.println("Enter rating (5..1): ");
        int rate = input.nextInt();
        while (rate > 5 || rate < 1) {
            System.out.println("Re-Enter rating (5..1) :");
            rate = input.nextInt();
        }

        input.nextLine();
        System.out.println("Enter comment: ");
        String comment = input.nextLine();

        Review review = new Review(reviewID, pID, cID, rate, comment);
        reviews.findFirst();
        reviews.insert(review);
        
        // FIX: Link review to product
        if (Main.products != null && !Main.products.empty()) {
            Main.products.findFirst();
            while (true) {
                Product product = Main.products.retrieve();
                if (product.getProductId() == pID) {
                    product.getReview().insert(review);
                    break;
                }
                if (Main.products.last()) break;
                Main.products.findNext();
            }
        }
        
        return review;
    }

    public void editByProductId(LinkedList<Review> reviews, int productId, int newRating, String newComment) {
        if (reviews == null || reviews.empty()) {
            System.out.println("No reviews data");
            return;
        }
        if (newRating < 1 || newRating > 5) {
            System.out.println("Invalid rating. Must be between 1 and 5.");
            return;
        }

        boolean updated = false;
        reviews.findFirst();
        while (true) {
            Review r = reviews.retrieve();
            if (r.getProductId() == productId) {
                r.setRatingScore(newRating);
                if (newComment != null && !newComment.trim().isEmpty()) {
                    r.setComment(newComment);
                }
                System.out.println("Review " + r.getReviewId() + " updated (productId: " + productId + ")");
                System.out.println(r);
                updated = true;
                break;
            }
            if (reviews.last()) break;
            reviews.findNext();
        }

        if (!updated) {
            System.out.println("No review found for productId: " + productId);
        }
    }

    private static boolean checkReviewID(int id) {
        if (reviews == null || reviews.getHead() == null) return false;
        Node<Review> n = reviews.getHead();
        while (n != null) {
            Review r = n.getData();
            if (r != null && r.getReviewId() == id) return true;
            n = n.getNext();
        }
        return false;
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

    public static LinkedList<Review> getReviews() { return reviews; }
    public static void setReviews(LinkedList<Review> reviews) { Review.reviews = reviews; }

    public String toString() {
        return String.format(
            "Review Id: %d  Product Id: %d  Customer Id: %d  Score: %d  Comment: '%s'",
            reviewId, productId, customerId, ratingScore, comment);
    }
}