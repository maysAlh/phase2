package ph2;

import java.io.File;
import java.util.Scanner;

public class ReviewData {
	
   
    
    public static LinkedList<Review> reviews = new LinkedList<Review> ();

    public ReviewData( String fileName)
    {
        
        try{
                File docsfile = new File(fileName);
                Scanner reader = new Scanner (docsfile);
                String line = reader.nextLine();
                
                while(reader.hasNext())
                {
                    line = reader.nextLine();
                    String [] data = line.split(","); 
                    int rid = Integer.parseInt(data[0]);
                    int pid = Integer.parseInt(data[1]);
                    int cid = Integer.parseInt(data[2]);
                    int rating =  Integer.parseInt(data[3]);
                    String comment =  data[4];
                    
                    Review review = new Review(rid, pid, cid, rating, comment );
                    reviews.insert(review);
                }
                reader.close();
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
    }
    public static LinkedList<Review> getReviews() {
        return reviews;  
    }
}