
import java.util.HashMap; 
import java.util.Map;

class Order {
    private static int orderCounter = 1;  
    private String orderId;
    private Map<FoodItem, Integer> items;
    private String customerRollNumber;
    private String deliveryRoom;
    private String status;
    private String[] specialRequest; 
    private String date;
    private String time;
    private boolean vipOrder;
    double totalpayment;

    public Order(String customerRollNumber, String deliveryRoom) {
        this.orderId = generateOrderId();
        this.items = new HashMap<>();
        this.customerRollNumber = customerRollNumber;
        this.deliveryRoom = deliveryRoom;
        this.status = "Pending";
        this.specialRequest  = new String[2];specialRequest[0]="None";specialRequest[1]="Not set";
        this.date = " ";
        this.time = " ";
        this.vipOrder = false;
        this.totalpayment=0;
    }
    public void setpayment(double payment) {
        this.totalpayment=payment;
    }

    public double getpayment() {
        return this.totalpayment;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setreqStatus(String status) {
        this.specialRequest[1] = status;
    }

    public String getDate() {
        return this.date;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return this.time;
    }

    public boolean isVipOrder() {
        return vipOrder;
    }


    public void makeVipOrder(boolean isVip) {
        this.vipOrder = isVip;
    }

    private String generateOrderId() {
        return "ORD" + orderCounter++;  
    }
    public void setOrderId(String id){
        this.orderId=id;
    }

    public void addItem(FoodItem item, int quantity) {
        if (item.isAvailable()) {
            items.merge(item, quantity, Integer::sum);
        } else {
            System.out.println("Item " + item.getName() + " is not available.");
        }
    }

    public void modifyItemQuantity(String itemName, int newQuantity) {
        for (Map.Entry<FoodItem, Integer> entry : items.entrySet()) {
            FoodItem item = entry.getKey();
            if (item.getName().equalsIgnoreCase(itemName)) {
                if (newQuantity <= 0) {
                    removeItem(itemName);
                } else {
                    items.put(item, newQuantity);
                    System.out.println("Updated " + item.getName() + " quantity to " + newQuantity + ".");
                }
                return;
            }
        }
        System.out.println("Item " + itemName + " not found in the order.");
    }

    public void writeSpecialRequest(String request) {
        this.specialRequest[0]=request;
    }
    public void req_accept_reject(String req){
        specialRequest[1]=req;
    }
    public String getSpecialRequestStatus(){
        return specialRequest[1];
        
    }
    public String getspecialReq(){
        return specialRequest[0];
        
    }

    public void removeItem(String itemName) {
        for (FoodItem item : items.keySet()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                items.remove(item);
                System.out.println("Removed " + item.getName() + " from the order.");
                return;
            }
        }
        System.out.println("Item " + itemName + " not found in the cart.");
    }

    public double calculateTotal() {
        double total = 0;
        for (Map.Entry<FoodItem, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerRollNumber() {
        return customerRollNumber;
    }

    public String getDeliveryRoom() {
        return deliveryRoom;
    }

    public void setDeliveryRoom(String newDeliveryRoom) {
        this.deliveryRoom = newDeliveryRoom;
    }

    public String getStatus() {
        return status;
    }

    public boolean cancelOrder() {
        if ("Pending".equalsIgnoreCase(status)) {
            status = "Cancelled";
            return true;
        } else {
            System.out.println("Order cannot be cancelled at this stage.");
            return false;
        }
    }

    public Map<FoodItem, Integer> getItems() {
        return items;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int cartIsEmpty() {
        return items.isEmpty() ? 1 : 0;
    }

    public void copyItemsFrom(Order other) {
        for (Map.Entry<FoodItem, Integer> entry : other.getItems().entrySet()) {
            FoodItem item = entry.getKey();
            int quantity = entry.getValue();
            this.addItem(item, quantity);
        }
    }

   
    @Override
public String toString() {
    StringBuilder orderDetails = new StringBuilder("Order ID: " + orderId + "\n" +
            "Customer Roll Number: " + customerRollNumber + "\n" +
            "Delivery Room: " + deliveryRoom + "\n" +
            "Order Date: " + date + "\n" +
            "Order Time: " + time + "\n" +
            "Special Request: " + specialRequest[0]  + "\n" +
            "Special Request Status: " + getSpecialRequestStatus() + "\n" +
            "Items:\n");

    for (Map.Entry<FoodItem, Integer> entry : items.entrySet()) {
        FoodItem item = entry.getKey();
        int quantity = entry.getValue();
        orderDetails.append(item.getName()).append(" x").append(quantity)
                .append(" | Rs.").append(item.getPrice()).append("\n");
    }

    orderDetails.append("Total: Rs.").append(String.format("%.2f", calculateTotal())).append("\n" +
            "Status: " + status + "\n");

    return orderDetails.toString();
}

}

