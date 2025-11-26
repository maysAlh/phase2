package phase2;

import java.io.File;
import java.util.Scanner;

public class orderDate {
	
	//
	public static Scanner input = new Scanner (System.in);
    public static LinkedList<Order> orders = new LinkedList<Order> ();
  
  public orderDate( String fileName)
    {
        try{
                File docsfile = new File(fileName);
                Scanner reader = new Scanner (docsfile);
                String line = reader.nextLine();
                
                while(reader.hasNext())
                {
                    line = reader.nextLine();
                    String [] data = line.split(","); 
                    int oid = Integer.parseInt(data[0]);
                    int cid = Integer.parseInt(data[1]);
                    
                    String  pp =  data[2].replaceAll("\"", "");
                    String [] p = pp.split(";");
                    Integer [] pids = new Integer [p.length];
                    for ( int i = 0 ; i< pids.length ; i++)
                        pids[i] = new Integer(Integer.parseInt(p[i]));
                    double price = Double.parseDouble(data[3]);
                    String date = data[4];
                    String status = data[5];
                            
                    Order orderobj = new Order(oid, cid, pids, price, date, status );
                    orders.insert(orderobj);
                    
                }
                reader.close();
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
   
    }

  public LinkedList<Order> getOrders() {
	    return orders; 
	}

  
  
  
  
} 