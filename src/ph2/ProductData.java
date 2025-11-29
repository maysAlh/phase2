package ph2;

import java.io.File;
import java.util.Scanner;

public class ProductData {

    public static AVL<Integer, Product> products = new AVL<Integer, Product>();

    public ProductData(String fileName) {
        try {
            File docsfile = new File(fileName);
            Scanner reader = new Scanner(docsfile);
            String line = reader.nextLine(); 

            int count = 0; 

            while (reader.hasNext()) {
                line = reader.nextLine();
                String[] data = line.split(",");

                int pid = Integer.parseInt(data[0].trim());
                String name = data[1].trim();
                double price = Double.parseDouble(data[2].trim());
                int stock = Integer.parseInt(data[3].trim());

                Product product = new Product(pid, name, price, stock);
                products.insert(pid, product);
                count++;
            }
            reader.close();

            System.out.println("Products loaded from CSV = " + count);

        } catch (Exception ex) {
            System.out.println("Error loading products: " + ex.getMessage());
        }
    }

    public AVL<Integer, Product> getProducts() {
        return products;
    }
}
