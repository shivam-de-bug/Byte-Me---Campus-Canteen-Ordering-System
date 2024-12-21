

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
class Customer {
    private String rollNumber;
    private String hostelRoom;
    private String password;
    private OrderManager orderManager;
    private Menu menu;
    private boolean vip;
    private Order currentOrder;
    public ArrayList<Order> orderHistory;
    private DataManager dataManager;
    Scanner scan=new Scanner(System.in);

    public Customer(String rollNumber, String hostelRoom, String password,boolean vip,OrderManager orderManager, Menu menu) {
        this.rollNumber = rollNumber;
        this.hostelRoom = hostelRoom;
        this.orderManager = orderManager;
        this.password=password;
        this.menu = menu;
        this.vip=vip;
        this.currentOrder = new Order(rollNumber, hostelRoom); 
        this.orderHistory = new ArrayList<>();
        this.dataManager=new DataManager(orderManager, menu);
    }
    public String getPassword(){
        return password;
    }
    public boolean isvipcustomer(){
        return vip;
    }

    public void makevipcustomer(boolean sel_vip){
        this.vip=sel_vip;
    }

    public String getCurrentOrderDetails() {
        return currentOrder.toString(); 
    }
    public void addOrderToHistory(Order order) {
        orderHistory.add(order);
    }
    public String getRollNumber(){
        return this.rollNumber;
    }
    public String getHostelRoom(){
        return this.hostelRoom;
    }
    public void viewOrderStatus() {
        if (orderHistory.isEmpty()) {
            printString("No previous orders found.");
        } else {
            for (Order order : orderHistory) {
                printString(order.toString());
            }
    
            printString("Enter order ID to see order status: ");
            String enteredOrderId = scan.nextLine().trim();
    
            boolean orderFound = false;
            for (Order order : orderHistory) {
                if (order.getOrderId().equalsIgnoreCase(enteredOrderId)) {
                    printString("Order Status: " + order.getStatus());
                    orderFound = true;
                    break;
                }
            }
    
            if (!orderFound) {
                printString("No order found with ID: " + enteredOrderId);
            }
        }
    }
    
    public void cancelOrder() {
        List<Order> pendingOrders = new ArrayList<>();
        
        for (Order order : orderHistory) {
            if (order.getStatus().equalsIgnoreCase("Pending")) {
                pendingOrders.add(order);
            }
        }
    
        if (pendingOrders.isEmpty()) {
            printString("You have no orders that can be canceled.");
            return;
        }
    
        printString("Pending orders available for cancellation:");
        for (Order order : pendingOrders) {
            printString("Order ID: " + order.getOrderId() + " - " + order.toString());
        }
    
        printString("Enter the Order ID of the order you wish to cancel:");
        String orderIdToCancel = scan.nextLine().trim();
    
        boolean orderCanceled = false;
        for (Order order : pendingOrders) {
            if (order.getOrderId().equalsIgnoreCase(orderIdToCancel)) {
                orderManager.pendingOrders.remove(order);
                orderManager.cancelledOrders.add(order);
                order.setStatus("Cancelled"); 
                printString("Order " + orderIdToCancel + " has been successfully canceled.");
                orderCanceled = true;
                break;
            }
        }
            if (!orderCanceled) {
            printString("No pending order found with ID: " + orderIdToCancel);
        }
    }
    
    public void viewOrderHistory() {
        if (orderHistory.isEmpty()) {
            printString("No previous orders found.");
            return;
        }
    
        printString("Order History:");
        for (Order order : orderHistory) {
            printString(order.toString()+"\n");
            
        }
    
        printString("Would you like to re-order a previous meal? (yes/no)");
        String reOrderResponse = scan.nextLine().trim();
    
        if (reOrderResponse.equalsIgnoreCase("no")) {
            printString("Exiting order history.");
            return;
        } else if (reOrderResponse.equalsIgnoreCase("yes")) {
            printString("Enter the Order ID you wish to re-order:");
            String orderIdToReOrder = scan.nextLine().trim();
    
            Order selectedOrder = null;
            for (Order order : orderHistory) {
                if (order.getOrderId().equalsIgnoreCase(orderIdToReOrder)) {
                    selectedOrder = order;
                    break;
                }
            }
    
            if (selectedOrder != null) {
                printString("Do you want to modify the quantity of any item in this order? (yes/no)");
                String modifyResponse = scan.nextLine().trim();
    
                Order newOrder = new Order(selectedOrder.getCustomerRollNumber(), selectedOrder.getDeliveryRoom());
                newOrder.copyItemsFrom(selectedOrder);
    
                if (modifyResponse.equalsIgnoreCase("yes")) {
                    boolean modifying = true;
                    while (modifying) {
                        printString("Enter the item name you want to modify:");
                        String itemName = scan.nextLine().trim();
    
                        printString("Enter the new quantity:");
                        int newQuantity = scan.nextInt();
                        scan.nextLine(); 
    
                        newOrder.modifyItemQuantity(itemName, newQuantity);
    
                        printString("Do you want to modify another item? (yes/no)");
                        String continueModifying = scan.nextLine().trim();
                        if (continueModifying.equalsIgnoreCase("no")) {
                            modifying = false;
                        }
                    }
                }
    
               
                currentOrder=newOrder;
                checkout();
                
            } else {
                printString("Invalid Order ID. Please try again.");
            }
        } else {
            printString("Invalid input. Please try again.");
        }
    }
    


    

public void leaveReview() {
    Set<FoodItem> deliveredItems = new HashSet<>();
    for (Order order : this.orderHistory) {
        if (order.getStatus().equalsIgnoreCase("Delivered")) {

            deliveredItems.addAll(order.getItems().keySet()); 
        }
    }
    if (deliveredItems.isEmpty()) {
        printString("No delivered items available for review.");
        return;
    }
    printString("Items available for review:");
    for (FoodItem item : deliveredItems) {
        printString("- " + item.getName());
    }

    printString("Enter the name of the item you want to review:");
    String itemName = scan.nextLine().trim();

    FoodItem selectedItem = null;
    for (FoodItem item : deliveredItems) {
        if (item.getName().equalsIgnoreCase(itemName)) {
            selectedItem = item;
            break;
        }
    }
    if (selectedItem != null) {
        printString("Enter your review for " + selectedItem.getName() + ":");
        String review = scan.nextLine().trim();
        selectedItem.addReview(review); 
        printString("Thank you for your review!");
    } else {
        printString("You haven't purchased or had " + itemName + " delivered.");
    }
}


    

    public void addItemToCartByName(String foodItemName, int quantity) {
        FoodItem foodItem = menu.getFoodItemByName(foodItemName);
        if (foodItem != null && foodItem.isAvailable()) { 
            currentOrder.addItem(foodItem, quantity); 
            System.out.println(quantity + " x " + foodItem.getName() + " added to cart.");
        } else {
            System.out.println("Food item not found or not available.");
        }
    }
    



    public void modifyCartItemQuantity(String itemname, int newQuantity){  
        currentOrder.modifyItemQuantity(itemname, newQuantity);
        
    }
    public void removeItemFromCart(String itemname){
        
        currentOrder.removeItem(itemname);
        
    }
    
    public int  cartempty(){
        return currentOrder.cartIsEmpty();
        
    }
    public double getTotalCartAmount(){
        return currentOrder.calculateTotal();
    }
    public void specialrequest(){
        System.out.print("Enter your request for the order: ");
        String req= scan.nextLine();
        currentOrder.writeSpecialRequest(req);
        System.out.println("Your request has been submitted.");

    }
    public void checkout() {
        
        if (currentOrder.calculateTotal() > 0) {
            System.out.print("If you have special request than press 'y' otherwise 'n' :");
            String req= scan.nextLine();
            if (req.equalsIgnoreCase("y") ){
                specialrequest();
            }
            printString("Enter the delivery room number:");
            String DeliveryRoom = scan.nextLine().trim();
            currentOrder.setDeliveryRoom(DeliveryRoom);
            System.out.print("Total amount to pay: Rs. "+currentOrder.calculateTotal()+" \n Enter amount to pay :");
            double total;
            total=scan.nextDouble();
            scan.nextLine();
            while (total < currentOrder.calculateTotal()) {
                System.out.println("Please pay the full amount.");
                System.out.print("Enter amount to pay: ");
                total = scan.nextDouble();
                scan.nextLine(); 
            }
            currentOrder.setpayment(total);
            String time;
            System.out.print("Enter current time (e.g. 3:00 AM): ");
            time=scan.nextLine();
            currentOrder.setTime(time);
            String date;
            System.out.print("Enter order date in the format DD-MM-YY: ");
            date=scan.nextLine();
            currentOrder.setDate(date);

            if(isvipcustomer()){
                currentOrder.makeVipOrder(true);
            }
            orderHistory.add(currentOrder);
            orderManager.pendingOrders.add(currentOrder);
            orderManager.orderHistory.add(currentOrder);


            System.out.println("Order placed successfully!");
            printString("Your order ID is :" + currentOrder.getOrderId());
            currentOrder = new Order(rollNumber, hostelRoom); 
        } else {
            System.out.println("Cannot checkout: No items in the current order.");
        }
    }

    public void printString(String message) {
        System.out.println(message);
    }
    public void printString_(String message) {
        System.out.print(message);
    }
    public void saveOrderHistory() {
        DataManager.saveOrderHistory(this.orderHistory);
    }
    
  
    
  
   
}
