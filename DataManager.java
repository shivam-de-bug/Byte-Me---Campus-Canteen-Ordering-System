import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataManager {
    private Menu menu;
    private OrderManager orderManager;
    DataManager(OrderManager orderManager,Menu menu){
        this.menu=menu;
        this.orderManager=orderManager;
    }

    private static final String USERS_FILE = "users.dat";
    private static final String ORDER_HISTORY_FILE = "orderHistory.dat";

    public static void saveUsersToFile(Map<String, Customer> customers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (Customer customer : customers.values()) {
                writer.println(customer.getRollNumber() + "," + 
                               customer.getHostelRoom() + "," + 
                               customer.getPassword() + "," + 
                               customer.isvipcustomer());
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
    
    
   
    public  Map<String, Customer> loadUsersFromFile() {
        Map<String, Customer> customers = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                boolean vip = Boolean.parseBoolean(parts[3]);               
                Customer customer = new Customer(parts[0], parts[1], parts[2],vip,orderManager , menu);  
                customer.makevipcustomer(Boolean.parseBoolean(parts[3]));
                customers.put(parts[0], customer);
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        return customers;
    }
    
    public static void saveOrderHistory(ArrayList<Order> orders) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ORDER_HISTORY_FILE))) {
            writer.println("RollNumber,OrderID,Items,DeliveryRoom,Status,Date,Time,VIPOrder,TotalPayment");
            for (Order order : orders) {
                String items = serializeItems(order.getItems());
                writer.println(order.getCustomerRollNumber() + "," +
                               order.getOrderId() + "," +
                               items + "," +
                               order.getDeliveryRoom() + "," +
                               order.getStatus() + "," +
                               order.getDate() + "," +
                               order.getTime() + "," +
                               order.isVipOrder() + "," +
                               order.getpayment()+ "," +
                               order.getspecialReq()+ "," +
                               order.getSpecialRequestStatus());
            }
        } catch (IOException e) {
            System.out.println("Error saving order history: " + e.getMessage());
        }
    }
    
    private static String serializeItems(Map<FoodItem, Integer> items) {
        StringBuilder serialized = new StringBuilder();
        for (Map.Entry<FoodItem, Integer> entry : items.entrySet()) {
            serialized.append(entry.getKey().getName()).append(":").append(entry.getValue()).append("|");
        }
        if (serialized.length() > 0) {
            serialized.setLength(serialized.length() - 1); 
        }
        return serialized.toString();
    }
    

    
    public ArrayList<Order> loadOrderHistoryofcustomer(String rollNumber) {
        ArrayList<Order> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDER_HISTORY_FILE))) {
            String line;
            reader.readLine(); 
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(rollNumber)) { 
                    Order order = new Order(parts[0], parts[3]); 
                    order.setOrderId(parts[1]);
                    order.setStatus(parts[4]);
                    order.setDate(parts[5]);
                    order.setTime(parts[6]);
                    order.makeVipOrder(Boolean.parseBoolean(parts[7]));
                    order.setpayment(Double.parseDouble(parts[8]));
                    order.writeSpecialRequest(parts[9]);
                    order.setreqStatus(parts[10]);
    
                    Map<FoodItem, Integer> items = deserializeItems(parts[2]);
                    for (Map.Entry<FoodItem, Integer> entry : items.entrySet()) {
                        order.addItem(entry.getKey(), entry.getValue());
                    }
    
                    orders.add(order);
                }
            }
        
        } catch (IOException e) {
            System.out.println("Error loading order history: " + e.getMessage());
        }
        return orders;
    }
    public ArrayList<Order> loadAllOrderHistory() {
        ArrayList<Order> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDER_HISTORY_FILE))) {
            String line;
            reader.readLine(); 
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
              
                    Order order = new Order(parts[0], parts[3]); 
                    order.setOrderId(parts[1]);
                    order.setStatus(parts[4]);
                    order.setDate(parts[5]);
                    order.setTime(parts[6]);
                    order.makeVipOrder(Boolean.parseBoolean(parts[7]));
                    order.setpayment(Double.parseDouble(parts[8]));
    
                    Map<FoodItem, Integer> items = deserializeItems(parts[2]);
                    for (Map.Entry<FoodItem, Integer> entry : items.entrySet()) {
                        order.addItem(entry.getKey(), entry.getValue());
                    }
    
                    orders.add(order);
                
            }
        
        } catch (IOException e) {
            System.out.println("Error loading order history: " + e.getMessage());
        }
        return orders;
    }
    
    private  Map<FoodItem, Integer> deserializeItems(String serialized) {
        Map<FoodItem, Integer> items = new HashMap<>();
        String[] pairs = serialized.split("\\|");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            FoodItem item = menu.getFoodItemByName(keyValue[0]);
            if (item != null) {
                int quantity = Integer.parseInt(keyValue[1]);
                items.put(item, quantity);}
            
        }
        return items;
    }
    
  
    
}
