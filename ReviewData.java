package inventory;

import java.io.File;
import java.util.Scanner;

public class ReviewData {
	
    public static Scanner input = new Scanner(System.in);
    public static ReviewList<Review> reviews = new ReviewList<>();
    public static Product reviewedProduct;

    public ReviewData(String fileName) {
        try {
            File file = new File(fileName);
            Scanner reader = new Scanner(file);

            if (reader.hasNextLine()) reader.nextLine(); // skip header (optional)

            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue; // skip blank lines

                String[] data = line.split(",");
                if (data.length < 5) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                int rId = Integer.parseInt(data[0].trim());
                int pId = Integer.parseInt(data[1].trim());
                int cId = Integer.parseInt(data[2].trim());
                int rate = Integer.parseInt(data[3].trim());
                String comment = data[4].trim();

                Review R = new Review(rId, pId, cId, rate, comment);
                reviews.insert(R);
            }
            reader.close();
            System.out.println("Reviews loaded successfully! Total: " + reviews.getSize());

        } catch (Exception ex) {
            System.out.println("Error reading review file: " + ex.getMessage());
        }
    }
    public static ReviewList<Review> getReviews() {
        return reviews;  
   
    }








}