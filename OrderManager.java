
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Comparator;

class OrderManager {
   
    public PriorityQueue<Order> pendingOrders; 
    public PriorityQueue<Order> preparingOrders;
    public PriorityQueue<Order> outofdeliveyOrders;

    public PriorityQueue<Order> deliveredOrders;

    public PriorityQueue<Order> cancelledOrders;
    public PriorityQueue<Order> refundedOrders;

    public static ArrayList<Order> orderHistory;
     static Scanner scanner=new Scanner(System.in);
    public OrderManager() {
        Comparator<Order> vipComparator = Comparator.comparing(Order::isVipOrder).reversed();
    
        pendingOrders = new PriorityQueue<>(vipComparator);
        preparingOrders = new PriorityQueue<>(vipComparator);
        outofdeliveyOrders = new PriorityQueue<>(vipComparator); 
        deliveredOrders = new PriorityQueue<>(vipComparator);
        cancelledOrders = new PriorityQueue<>(vipComparator);
        refundedOrders = new PriorityQueue<>(vipComparator);
    
        orderHistory = new ArrayList<>();
    }
    

    public void addOrder(Order order) {
        if (order != null) {
            pendingOrders.offer(order); 
            DataManager.saveOrderHistory(orderHistory); // file

            System.out.println("Order added to pending orders: " + order.getOrderId());
        }
    }
    
    

    public void updateOrderStatus(Order order, String newStatus) {
        String currentStatus = order.getStatus();
    
        switch (currentStatus) {
            case "Pending":
                if (pendingOrders.remove(order)) {
                    preparingOrders.add(order);
                    System.out.println("Order " + order.getOrderId() + " updated to Preparing.");
                }
                break;
            case "Preparing":
                if (preparingOrders.remove(order)) {
                    outofdeliveyOrders.add(order);
                    System.out.println("Order " + order.getOrderId() + " updated to Out for Delivery.");
                }
                break;
            case "Out for Delivery":
                if (outofdeliveyOrders.remove(order)) {
                    deliveredOrders.add(order);
                    System.out.println("Order " + order.getOrderId() + " updated to Delivered.");
                }
                break;
            case "Cancelled":
            case "Denied":
                if (cancelledOrders.remove(order)) {
                    refundedOrders.add(order);
                    System.out.println("Order " + order.getOrderId() + " updated to Refunded.");
                }
                break;
            default:
                System.out.println("Unknown status. Cannot update.");
                return;
        }
        order.setStatus(newStatus);
    }
    public  boolean confirmStatusUpdate(String orderType) {
        String response; 
        while (true) { 
            System.out.print("Do you want to update the status of a " + orderType + " order? (yes/no): ");
            response = scanner.nextLine().trim(); 
    
            if (response.equalsIgnoreCase("yes")) {
                return true; 
            } else if (response.equalsIgnoreCase("no")) {
                return false; 
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'."); 
            }
        }
    }
    
   
    public ArrayList<Order> getOrderHistory() {
        return orderHistory;
    }

    
    public void viewOrderHistory() {
        System.out.println("\n--- Order History ---");
        if (orderHistory.isEmpty()) {
            System.out.println("No order history available.");
        } else {
            for (Order order : orderHistory) {
                System.out.println(order+"\n");

            }
        }
    }
}

