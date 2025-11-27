package ph2;

import java.io.File;
import java.util.Scanner;

public class ProductData {
	
    private LinkedList<Product> products = new LinkedList<Product>();

    public ProductData(String fileName) {
       try {
            File docsfile = new File(fileName);
            Scanner reader = new Scanner(docsfile);
            String line = reader.nextLine();
            
            while(reader.hasNext()) {
                line = reader.nextLine();
                String[] data = line.split(","); 
                
                int pid = Integer.parseInt(data[0]);
                String name = data[1].trim();
                double price = Double.parseDouble(data[2]);
                int stock = Integer.parseInt(data[3]);

                Product product = new Product(pid, name, price, stock);
                products.insert(product);
            }
            reader.close();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public LinkedList<Product> getProducts() {
        return products;
    }
}