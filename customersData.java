package phase2;

import java.io.File;
import java.util.Scanner;

public class customersData {
	
	public static Scanner input = new Scanner (System.in);
   public static LinkedList<Customer> customers = new LinkedList<Customer> ();
    
  
    
    
    public customersData(String fileName)
    {
            try{
                File docsfile = new File(fileName);
                Scanner reader = new Scanner (docsfile);
                String line = reader.nextLine();
                
                while(reader.hasNext())
                {
                    line = reader.nextLine();
                    String [] data = line.split(","); 
                    Customer customer = new Customer(Integer.parseInt(data[0]),data[1], data[2] ); // custumer id, name,email
                    customers.insert(customer); // insert it into custimers lists
                }
                reader.close();
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
    }




	public  LinkedList<Customer> getCustomers() {
		return customers;
	}




    
   

}