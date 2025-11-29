package ph2;

import java.io.File;
import java.util.Scanner;

public class orderDate {

    public static Scanner input = new Scanner(System.in);

    
    public static AVL<Integer, Order> orders = new AVL<Integer, Order>();

    public orderDate(String fileName) {
        try {
            File docsfile = new File(fileName);
            Scanner reader = new Scanner(docsfile);

            
            String line = reader.nextLine();

            while (reader.hasNext()) {
                line = reader.nextLine();
                if (line.trim().isEmpty())
                    continue;

                String[] data = line.split(",");

                int oid = Integer.parseInt(data[0].trim());
                int cid = Integer.parseInt(data[1].trim());

                String pp = data[2].replaceAll("\"", "").trim();
                String[] p = pp.split(";");
                Integer[] pids = new Integer[p.length];
                for (int i = 0; i < pids.length; i++)
                    pids[i] = Integer.parseInt(p[i].trim());

                double price = Double.parseDouble(data[3].trim());
                String date = data[4].trim();
                String status = data[5].trim();

                Order orderobj = new Order(oid, cid, pids, price, date, status);

                
                orders.insert(oid, orderobj);
                
             
            }

            reader.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public AVL<Integer, Order> getOrders() {
        return orders;
    }
}
