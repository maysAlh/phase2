package inventory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ProductData {

    public static Scanner read = new Scanner(System.in);
    public static ProductPriorityQueue<Product> products = new ProductPriorityQueue<>();

    public ProductData(String fileName) {
        try {
            File docsfile = new File(fileName);
            Scanner reader = new Scanner(docsfile);

            // Skip header
            if (reader.hasNextLine()) {
                reader.nextLine();
            }

            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();

                // Skip empty lines
                if (line.isEmpty()) continue;

                String[] data = line.split(",");

                if (data.length < 4) {
                    System.out.println("Skipping invalid line (not enough fields): " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(data[0].trim());
                    String name = data[1].trim();
                    double price = Double.parseDouble(data[2].trim());
                    int stock = Integer.parseInt(data[3].trim());

                    // Assign default priority = 1
                    int defaultPriority = 1;
                    Product product = new Product(id, name, price, stock, defaultPriority);
                    products.enqueue(product);
                } catch (NumberFormatException nfe) {
                    System.out.println("Skipping invalid line (number format error): " + line);
                }
            }

            reader.close();

            if (products.length() == 0) {
                System.out.println("No products found!");
            } else {
                System.out.println("File loaded successfully! Products: " + products.length());
            }

        } catch (FileNotFoundException ex) {
            System.out.println("File not found: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error reading file: " + ex.getMessage());
        }
    }

    public static ProductPriorityQueue<Product> getProduct() {
        return products;
    }
}