package ph2;

import java.io.File;
import java.util.Scanner;

public class OrderData {

    public static Scanner input = new Scanner(System.in);
    public static AVL<Integer, Order> orders = Order.orders;


    public OrderData(String fileName) {
        try {
            File docsfile = new File(fileName);
            Scanner reader = new Scanner(docsfile);

            // تخطي الهيدر
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
                String date = data[4].trim();   // مثلا 1/25/2025
                String status = data[5].trim();

                Order orderobj = new Order(oid, cid, pids, price, date, status);

                orders.insert(oid, orderobj);
            }

            reader.close();
            System.out.println("Loaded orders from CSV: " + orders.size());


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public AVL<Integer, Order> getOrders() {
        return orders;
    }
}
