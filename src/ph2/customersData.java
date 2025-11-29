package ph2;

import java.io.File;
import java.util.Scanner;

public class customersData {

    public static Scanner input = new Scanner(System.in);

    public customersData(String fileName)
    {
        try {
            File docsfile = new File(fileName);
            Scanner reader = new Scanner(docsfile);

            
            String line = reader.nextLine();

            while (reader.hasNext()) {
                line = reader.nextLine();
                String[] data = line.split(",");

                // ID, Name, Email
                int id      = Integer.parseInt(data[0].trim());
                String name = data[1].trim();
                String email= data[2].trim();

                Customer customer = new Customer(id, name, email);

               
                Customer.customers.insert(id, customer);       // AVL<Integer, Customer>
                Customer.customersNames.insert(name, customer); // AVL<String, Customer>
            }

            reader.close();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public AVL<Integer, Customer> getCustomers() {
        return Customer.customers;
    }

    public AVL<String, Customer> getCustomersNames() {
        return Customer.customersNames;
    }
}
