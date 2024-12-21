import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Admin {
    private Menu menu;
    private OrderManager orderManager;
    Scanner scanner=new Scanner(System.in);

    public Admin(Menu menu, OrderManager orderManager) {
        this.menu = menu;
        this.orderManager = orderManager;
    }

    public void addMenuItem(String name, String category, double price,boolean availability) {
        FoodItem newItem = new FoodItem(name, category, price,availability);
        if (menu.addItem(newItem)) {
            System.out.println("Menu item " + name + " added successfully.");
        }
        else{
            System.out.println("Item is already present.");
        }
    }
    public void viewOrderHistory(){
        orderManager.viewOrderHistory();
    }
    public void viewaorder() {
        System.out.print("Enter the Order ID you want to view: ");
        String orderId = scanner.nextLine();  
        boolean found = false;
    
        for (Order order : orderManager.orderHistory) {
            if (order.getOrderId().equalsIgnoreCase(orderId)) {
                System.out.println("\n--- Order Details ---");
                System.out.println(order); 
                found = true;
                break;  
            }
        }
    
        if (!found) {
            System.out.println("Order with ID " + orderId + " not found.");
        }
    }
    
    
    public void updateItemDetails() {
        System.out.print("Enter food item name to update details: "); 
        String itemName = scanner.nextLine();
        FoodItem item = menu.getFoodItemByName(itemName);
        
        if (item != null) {
            boolean updating = true;
            
            while (updating) {
                System.out.println("\nSelect an option to update:");
                System.out.println("1. Change Item Name");
                System.out.println("2. Change Item Availability");
                System.out.println("3. Change Item Category");
                System.out.println("4. Change Item Price");
                System.out.println("5. Finish Updating");
                System.out.print("Select an action: ");
                
                String choice = scanner.nextLine();
                
                switch (choice) {
                    case "1":
                        System.out.print("Enter new item name: ");
                        String newName = scanner.nextLine();
                        item.setName(newName);
                        System.out.println("Item name updated to " + newName);
                        break;
                        
                    case "2":
                        System.out.print("Enter new availability (true/false): ");
                        if (scanner.hasNextBoolean()) {
                            boolean newAvailability = scanner.nextBoolean();
                            scanner.nextLine(); 
                            item.setAvailability(newAvailability);
                            System.out.println("Item availability updated to " + newAvailability);
                        } else {
                            System.out.println("Invalid input for availability.");
                            scanner.nextLine(); 
                        }
                        break;
                        
                    case "3":
                            System.out.print("Enter new category: ");
                            String newCategory = scanner.nextLine();

                            String oldCategory = item.getCategory();
                            menu.menuItems.getOrDefault(oldCategory, new ArrayList<>()).remove(item);

                            item.setCategory(newCategory);
                            menu.menuItems.computeIfAbsent(newCategory, k -> new ArrayList<>()).add(item);

                            System.out.println("Item category updated to " + newCategory);
                        break;

                        
                    case "4":
                        System.out.print("Enter new price: ");
                        if (scanner.hasNextDouble()) {
                            double newPrice = scanner.nextDouble();
                            scanner.nextLine(); 
                            item.setPrice(newPrice);
                            System.out.println("Item price updated to " + newPrice);
                        } else {
                            System.out.println("Invalid input for price.");
                            scanner.nextLine(); 
                        }
                        break;
                        
                    case "5":
                        System.out.println("Finished updating item details.");
                        updating = false;
                        break;
                        
                    default:
                        System.out.println("Invalid choice. Please select an option from 1 to 5.");
                        break;
                }
            }
        } else {
            System.out.println("Item not found.");
        }
    }
    
    

    public void removeMenuItem(String name) {
        FoodItem item;
        item=menu.getFoodItemByName(name);
        if(item!=null){
        for (Order order : orderManager.pendingOrders){
            if(order.getItems().containsKey(item)){
                orderManager.pendingOrders.remove(order);
                orderManager.cancelledOrders.add(order);
                order.setStatus("Denied");
                
            }
        }
        menu.removeItem(name);
        }
        else{
            System.out.println("Food item " + name + " not found in the menu.");

        }

    }

    public void viewPendingOrders() {
        System.out.println("\n--- Pending Orders ---");
        if (orderManager.pendingOrders.isEmpty()) {
            System.out.println("No Pending order available.");
        } else {
            for (Order order : orderManager.pendingOrders) {
                System.out.println(order+"\n");

            }
        }
    }


    public void updateOrderStatus() {
        boolean updating = true;
        while (updating) {
            System.out.println("\n--- Order Status Update ---");
            System.out.println("Which order status queue would you like to update?");
            System.out.println("1. Pending Orders");
            System.out.println("2. Preparing Orders");
            System.out.println("3. Out for Delivery Orders");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                   if(!orderManager.pendingOrders.isEmpty()){
                    List<Order> pendingOrdersCopy = new ArrayList<>(orderManager.pendingOrders);
                    for (Order order : pendingOrdersCopy) {
                        if (order != null) {
                            System.out.println(order + "\n");
                            if (!order.getspecialReq().equals("None")) {
                                System.out.print("Do you accept the special request of the order (yes/no)? ");
                                String req = scanner.nextLine();
                                order.req_accept_reject(req.equalsIgnoreCase("yes") ? "Accepted" : "Rejected");
                            }
                            if (orderManager.confirmStatusUpdate("Pending")) {
                                orderManager.updateOrderStatus(order, "Preparing");
                            }
                        }
                    }}
                    else{
                        System.out.println("No order is pending.");
                    }
                    break;

                case "2":
                    if(!orderManager.preparingOrders.isEmpty()){
                    List<Order> preparingOrdersCopy = new ArrayList<>(orderManager.preparingOrders);
                    for (Order order : preparingOrdersCopy) {
                        if (order != null) {
                            System.out.println(order);
                            if (orderManager.confirmStatusUpdate("Preparing")) {
                                orderManager.updateOrderStatus(order, "Out for Delivery");
                            }
                        }
                    }}
                    else{
                        System.out.println("No order is preparing."); 
                    }
                    break;

                case "3":
                    if (!orderManager.outofdeliveyOrders.isEmpty()){
                    List<Order> outOfDeliveryOrdersCopy = new ArrayList<>(orderManager.outofdeliveyOrders);
                    for (Order order : outOfDeliveryOrdersCopy) {
                        if (order != null) {
                            System.out.println(order);
                            if (orderManager.confirmStatusUpdate("Out for Delivery")) {
                                orderManager.updateOrderStatus(order, "Delivered");
                            }
                        }
                    }}
                    else{
                        System.out.println("No order is out for delivery.");
                    }
                    break;

                case "4":
                    Main.adminguidispose();;
                    System.out.println("Exiting order status update.");
                    updating = false;
                    break;

                default:
                    Main.adminguidispose();
                    System.out.println("Invalid choice. Please select an option from 1 to 4.");
                    break;
            }
        }
    }


    public void processRefund() {
        boolean refunding = true;
    
        while (refunding) {
            boolean foundRefundableOrder = false;
    
            for (Order order : orderManager.orderHistory) {
                if (order.getStatus().equalsIgnoreCase("Cancelled") || order.getStatus().equalsIgnoreCase("Denied")) {
                    System.out.println("\n--- Refundable Order Details ---");
                    System.out.println(order);
                    foundRefundableOrder = true;
    
                    System.out.print("Do you want to mark this order as Refunded? (yes/no): ");
                    if (scanner.nextLine().equalsIgnoreCase("yes")) {
                        orderManager.updateOrderStatus(order, "Refunded");
                    }
                }
            }
    
            if (!foundRefundableOrder) {
                System.out.println("No cancelled or denied orders available to process.");
                refunding = false;
            } else {
                System.out.print("Do you want to proceed with another refund? (yes/no): ");
                refunding = scanner.nextLine().equalsIgnoreCase("yes");
            }
        }
    }
    public void handleSpecialRequests() {
        System.out.print("Enter the Order ID: ");
        String orderId = scanner.nextLine();
        Order selectorder = null; 
        for (Order order : orderManager.orderHistory) {
            if (order.getOrderId().equalsIgnoreCase(orderId)) {
                selectorder = order;
                break; 
            }
        }
        
        if (selectorder != null) {
            if(!selectorder.getspecialReq().equals("None")){
                String currentStatus = selectorder.getSpecialRequestStatus();
    
                if (currentStatus.equals("Not set")) {
                    System.out.print("The special request status is currently not set. Would you like to accept or reject it? (accept/reject): ");
                    String response = scanner.nextLine();
        
                    if (response.equalsIgnoreCase("accept")) {
                        selectorder.req_accept_reject("Accepted");
                        System.out.println("Special request has been accepted.");
                    } else if (response.equalsIgnoreCase("reject")) {
                        selectorder.req_accept_reject("Rejected");
                        System.out.println("Special request has been rejected.");
                    } else {
                        System.out.println("Invalid input. Please enter 'accept' or 'reject'.");
                    }
                } else {
                System.out.println("Current special request status: " + currentStatus);
                System.out.print("Would you like to change the status? (yes/no): ");
                String changeResponse = scanner.nextLine();
    
                if (changeResponse.equalsIgnoreCase("yes")) {
                    System.out.print("Enter the new status (accept/reject): ");
                    String newStatus = scanner.nextLine();
    
                    if (newStatus.equalsIgnoreCase("accept")) {
                        selectorder.req_accept_reject("Accepted");
                        System.out.println("Special request status has been changed to accepted.");
                    } else if (newStatus.equalsIgnoreCase("reject")) {
                        selectorder.req_accept_reject("Rejected");
                        System.out.println("Special request status has been changed to rejected.");
                    } else {
                        System.out.println("Invalid input. Please enter 'accept' or 'reject'.");
                    }
                } else {
                    System.out.println("No changes made to the special request status.");
                }
            }}
            else{
                System.out.print("No special request requested by customer.");
            }
            
        } else {
            System.out.println("Order not found with the provided ID.");
        }
    }
    
    
        
    

    public void generateDailySalesReport() {
    double totalSales = 0;
    int totalOrders = 0;
    Map<String, Integer> itemQuantities = new HashMap<>(); 

    System.out.print("Enter the date of the day you want details for (in format DD-MM-YY): ");
    String day = scanner.nextLine();

    for (Order order : orderManager.getOrderHistory()) {
        if ((order.getDate().equalsIgnoreCase(day)) && (order.getStatus().equalsIgnoreCase("Delivered"))) {
            totalSales += order.getpayment(); 
            totalOrders++;  
            
            for (Map.Entry<FoodItem, Integer> entry : order.getItems().entrySet()) {
                String itemName = entry.getKey().getName();
                int quantity = entry.getValue();

                itemQuantities.put(itemName, itemQuantities.getOrDefault(itemName, 0) + quantity);
            }
        }
        
    }

    System.out.println("\n--- Daily Sales Report ---");
    System.out.println("Date: " + day);
    System.out.println("Total Sales: Rs. " + String.format("%.2f", totalSales));
    System.out.println("Total Orders: " + totalOrders);

    if (!itemQuantities.isEmpty()) {
        String mostPopularItem = Collections.max(itemQuantities.entrySet(), Map.Entry.comparingByValue()).getKey();
        System.out.println("Most Popular Item: " + mostPopularItem + " (Quantity Sold: " + itemQuantities.get(mostPopularItem) + ")");
        System.out.println("\n--- Item Breakdown ---");

        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            System.out.println("Item: " + entry.getKey() + " | Quantity Sold: " + entry.getValue());
        }
    } else {
        System.out.println("No sales data available for the specified date.");
    }
}

}

