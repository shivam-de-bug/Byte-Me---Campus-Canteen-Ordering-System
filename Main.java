
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.*;
public class Main {
    private static Menu menu;
    private static OrderManager orderManager;
    public static Map<String, Customer> customers;
    private static Admin admin;
    private static DataManager dataManager;
    public static JFrame jFramemenu;
    public static JFrame jFrameorder;
    public static JFrame jFrameadminorder;



    public static void main(String[] args) {
        menu = new Menu();
        orderManager = new OrderManager();
        customers = new HashMap<>();
        
        
        initializeMenu();
        dataManager=new DataManager(orderManager, menu);
        customers=dataManager.loadUsersFromFile();
        admin = new Admin(menu, orderManager);
        for(Customer customer : customers.values()){
           
            customer.orderHistory=dataManager.loadOrderHistoryofcustomer(customer.getRollNumber());
            for (Order order: customer.orderHistory){
                orderManager.orderHistory.add(order);
                if(order.getStatus().equalsIgnoreCase("Pending")){
                    orderManager.pendingOrders.add(order);

                }else  if(order.getStatus().equalsIgnoreCase("Preparing")){
                    orderManager.preparingOrders.add(order);

                }else if(order.getStatus().equalsIgnoreCase("Out for Delivery")){
                    orderManager.outofdeliveyOrders.add(order);

                } else if(order.getStatus().equalsIgnoreCase("Delivered")){
                    orderManager.deliveredOrders.add(order);

                } else if(order.getStatus().equalsIgnoreCase("Cancelled")||order.getStatus().equalsIgnoreCase("Denied")){
                    orderManager.cancelledOrders.add(order);

                } else if(order.getStatus().equalsIgnoreCase("Refunded")){
                    orderManager.refundedOrders.add(order);

                }
            }
        }

        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            System.out.println("\n=====================================");
            System.out.println("        Welcome to 'Byte Me!'        ");
            System.out.println("      Food Ordering System Menu      ");
            System.out.println("=====================================");
            System.out.println("1. Login or sign up as Customer");
            System.out.println("2. Login as Admin");
            System.out.println("3. View Menu");
            System.out.println("4. Exit");
            System.out.print("\nPlease select an option (1-4): ");
            
             input = scanner.nextLine().trim();
        
            switch (input) {
                    case "1":
                    dispose();
                    handleCustomerLogin(scanner);
                    break;
                    
                case "2":
                dispose();
                    handleAdminLogin(scanner);
                    break;
                case "3":
                    MenuGUI menuGUI = null;
                    Customer customer=null;
                    Admin admingui=null;
                    dispose();
                    if (menuGUI == null) { 
                        menuGUI = new MenuGUI(menu, customer,admingui);
                        menuGUI.display();
                        System.out.println(" The Menu are displayed in the GUI. Please check the application window.");

                    }
                    break;
                case "4":
                dispose();
                    DataManager.saveUsersToFile(customers); 
                    DataManager.saveOrderHistory(orderManager.orderHistory);
                    System.out.println("\nThank you for using 'Byte Me!'");
                    System.out.println("We hope to see you again soon!");
                    scanner.close();
                    return;
                    
                default:
                    System.out.println("\nInvalid option. Please enter a number between 1 and 4.");
            }
        }
        
    }


    private static void handleCustomerLogin(Scanner scanner) {
        while (true) { 
            System.out.println("\n--- Customer Login / Sign-Up ---");
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit to Main Menu");
            System.out.print("Choose an option (1, 2, or 3): ");
            String choice = scanner.nextLine();
        
            switch (choice) {
                case "1":
                    performLogin(scanner);
                    return; 
                    
                case "2":
                    performSignUp(scanner);
                    return; 
                    
                case "3":
                    System.out.println("Returning to the main menu...");
                    return; 
                    
                default:
                    System.out.println("Invalid choice. Please select 1, 2, or 3.");
            }
        }
    }
    
    
    private static void performLogin(Scanner scanner) {
        System.out.print("Enter your Roll Number: ");
        String rollNumber = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
    
        Customer customer = customers.get(rollNumber);
    
        if (customer == null) {
            System.out.println("Invalid Roll Number or Not Registered. Please try again.");
        } else if (!customer.getPassword().equals(password)) {
            System.out.println("Incorrect password. Please try again.");
        } else {
            System.out.println("Welcome back, " + rollNumber + "!");
            openCustomerMenu(scanner, customer);
        }
    }
    
    
    private static void performSignUp(Scanner scanner) {
        System.out.print("Enter your Roll Number: ");
        String rollNumber = scanner.nextLine();
    
        if (customers.containsKey(rollNumber)) {
            System.out.println("An account with this Roll Number already exists. Try logging in.");
            return;
        }
    
        System.out.print("Enter your Hostel Room Number: ");
        String hostelRoom = scanner.nextLine();
        System.out.print("Create a password: ");
        String password = scanner.nextLine();
    
       
        Customer customer = new Customer(rollNumber, hostelRoom, password, false, orderManager, menu);
        customers.put(rollNumber, customer);
        DataManager.saveUsersToFile(customers); 
    
        System.out.println("Sign-up successful! Welcome to Byte Me!");
        openCustomerMenu(scanner, customer); 
    }
    
    private static void openCustomerMenu(Scanner scanner, Customer customer) {
        if (!customer.isvipcustomer()) {
            handleVIPUpgrade(scanner, customer);
        } else {
            System.out.println("You are a VIP customer!");
        }
    
        String action;
        while (true) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. Browse Menu");
            System.out.println("2. Cart Operations");
            System.out.println("3. Order Tracking");
            System.out.println("4. Item Reviews");
            System.out.println("5. Logout");
            System.out.print("Select a category: ");
            action = scanner.nextLine();
    
            switch (action) {
                case "1":
                    handleBrowseMenu(scanner, customer);
                    break;
                case "2":
                    handleCartOperations(scanner, customer);
                    break;
                case "3":
                    handleOrderTracking(scanner, customer);
                    break;
                case "4":
                    handleItemReview(scanner, customer);
                    break;
                case "5":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }
    
    private static void handleVIPUpgrade(Scanner scanner, Customer customer) {
        System.out.println("Do you want to become a VIP customer? Pay Rs.1000 to become a VIP customer. (y or n)");
        String response;
        while (true) {
            response = scanner.nextLine().trim();
            if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("n")) {
                break;
            } else {
                System.out.println("Invalid choice. Please respond with 'y' or 'n'.");
            }
        }
    
        if (response.equalsIgnoreCase("y")) {
            while (true) {
                System.out.print("Enter payment amount: ");
                if (scanner.hasNextDouble()) {
                    double payment = scanner.nextDouble();
                    scanner.nextLine(); 
                    if (payment >= 1000) {
                        customer.makevipcustomer(true);
                        System.out.println("Thank you! You are now a VIP customer!");
                        DataManager.saveUsersToFile(customers); 
                        break;
                    } else {
                        System.out.println("Insufficient amount. Please pay the full Rs.1000 to become a VIP customer.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a numeric value for the payment.");
                    scanner.nextLine();
                }
            }
        } else {
            System.out.println("You chose not to become a VIP customer.");
        }
    }
    
    private static void handleBrowseMenu(Scanner scanner, Customer customer) {
        String action;
        Admin admingui=null;
        MenuGUI menuGUI = null;
        while (true) {
            //GUI
            dispose();
            if (menuGUI == null) { 
                menuGUI = new MenuGUI(menu, customer,admingui);
                menuGUI.display();
                System.out.println(" The Menu and Pending Orders are displayed in the GUI. Please check the application window.");

            }


       
            System.out.println("\n--- Browse Menu ---");
            System.out.println("1. View all items");
            System.out.println("2. Search by name");
            System.out.println("3. Filter by category");
            System.out.println("4. Sort by price");
            System.out.println("5. Back to Customer Menu");
            System.out.print("Select an action: ");
            action = scanner.nextLine();
    
            switch (action) {
                
                case "1":
                    menu.displayMenu();
                    

                    break;
                case "2":
                    System.out.print("Enter item name to search: ");
                    String itemName = scanner.nextLine();
                    menu.searchItemByName(itemName);
                    break;
                case "3":
                    System.out.print("Enter category to filter by: ");
                    String category = scanner.nextLine();
                    menu.filterMenuByCategory(category); 
                    break;
                case "4":
                    String choice;
                    System.out.println("1. Sort all items by price (across all categories)");
                    System.out.println("2. Sort items by price within each category");
                    System.out.print("Enter your choice (1 or 2): ");
                    choice=scanner.nextLine();
                    menu.sortMenuByPrice(choice);
                    break;
                case "5":
               
                if (jFramemenu != null) {
                    jFramemenu.dispose(); 
                }
                if (jFrameorder != null) {
                    jFrameorder.dispose(); 
                }
                
                
                    return;  
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }
    
    private static void handleCartOperations(Scanner scanner, Customer customer) {
        String action;
        while (true) {
            System.out.println("\n--- Cart Operations ---");
            System.out.println("1. Add items to cart");
            System.out.println("2. Modify item quantities");
            System.out.println("3. Remove items from cart");
            System.out.println("4. View total");
            System.out.println("5. Checkout");
            System.out.println("6. Back to Customer Menu");
            System.out.print("Select an action: ");
            action = scanner.nextLine();
    
            switch (action) {
                case "1":
                    boolean adding = true;
                    System.out.println("Enter food item and if you completed adding item to cart then write 'exit'");

                    while (adding) {
                        System.out.print("Enter food item name or 'exit' to finish: ");
                        String foodItemName = scanner.nextLine();

                        if (foodItemName.equalsIgnoreCase("exit")) {
                            System.out.println("Exiting add to cart process...");
                            break;
                        }

                       
                        FoodItem foodItem = menu.getFoodItemByName(foodItemName);
                        
                        if (foodItem != null && foodItem.isAvailable()) {
                            System.out.print("Enter quantity: ");
                            int quantity;
                            try {
                                quantity = Integer.parseInt(scanner.nextLine());
                                if (quantity > 0) {
                                    customer.addItemToCartByName(foodItemName, quantity);
                                    System.out.println("Added " + quantity + " of " + foodItem.getName() + " to your cart.");
                                } else {
                                    System.out.println("Please enter a valid quantity greater than 0.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid quantity input. Please enter a numeric value.");
                            }
                        }else if(foodItem != null && !foodItem.isAvailable()) {
                            System.out.println("Food item is out of stock(Unavailable).");
                        }else {
                            System.out.println("Food item not found. Please try again.");
                        }
                    }
                    break;

                case "2":
                boolean  modifying= true;
                    System.out.println("Modifing food quan and if you completed modigy quan of item then write 'exit'");
                    while (modifying) {
                        if(1==customer.cartempty()){
                            System.out.println("Cart is empty"); 
                            modifying=false;
                            break;
                        }
                    System.out.print("Enter item name to modify quantity  or 'exit' to finish: ");
                    String itemname= scanner.nextLine();
                    if (itemname.equalsIgnoreCase("exit")) {
                        System.out.println("Exiting modifying process...");

                        break;
                    }
                    System.out.print("Enter new quantity: ");
                    int newQuantity = Integer.parseInt(scanner.nextLine());
                    customer.modifyCartItemQuantity(itemname, newQuantity); 
                    }
                    break;
                    case "3":
                    boolean removing = true;
                    while (removing) {
                        if(1==customer.cartempty()){
                            System.out.println("Cart is empty"); 
                            removing=false;
                            break;
                        }
                        System.out.print("Enter item name to remove from cart or 'exit' to finish: ");
                        String itemToRemove = scanner.nextLine();
                
                        if (itemToRemove.equalsIgnoreCase("exit")) {
                            System.out.println("Exiting removal process...");
                            removing = false;  
                            continue;         
                        }
                        customer.removeItemFromCart(itemToRemove);  
                    
                    }
                    break;
                
                case "4":
    
                    System.out.println("Total amount: Rs. " + customer.getTotalCartAmount()); 
                    break;
                case "5":
                    customer.checkout();
                    break;
                case "6":
                    return; 
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }
    
    private static void handleOrderTracking(Scanner scanner, Customer customer) {
        String action;
        while (true) {
            System.out.println("\n--- Order Tracking ---");
            System.out.println("1. View order status");
            System.out.println("2. Cancel order");
            System.out.println("3. View order history");
            System.out.println("4. Back to Customer Menu");
            System.out.print("Select an action: ");
            action = scanner.nextLine();
    
            switch (action) {
                case "1":
                    customer.viewOrderStatus(); 
                    break;
                case "2":
                    customer.cancelOrder(); 
                    break;
                case "3":
                    customer.viewOrderHistory();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }
    private static void handleItemReview(Scanner scanner, Customer customer){
        String action;
        while (true) {
            System.out.println("\n---Item Reviews ---");
            System.out.println("1. Provide review");
            System.out.println("2. View reviews");
            System.out.println("3. Back to Customer Menu");
            System.out.print("Select an action: ");
            action = scanner.nextLine();
    
            switch (action) {
                case "1":
                    customer.leaveReview();
                    break;
                    case "2":
                    boolean see = true;
                    while (see) {
                        System.out.print("Enter item name to search (or type 'exit' to go back): ");
                        String itemName = scanner.nextLine();
                
                        if (itemName.equalsIgnoreCase("exit")) {
                            System.out.println("Exiting view review process...");
                            see = false;
                            break;
                        }
                
                        FoodItem item = menu.getFoodItemByName(itemName);
                
                        if (item != null) {
                            item.viewReviews();
                        } else {
                            System.out.println("Item not found. Please try again.");
                        }
                    }
                    break;
                
                case "3":
                    return; 
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }

    }
    
    private static void handleAdminLogin(Scanner scanner) {
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();
    
        if ("1234".equals(password)) { 
            String adminAction;
            while (true) {
                System.out.println("\n--- Admin Menu ---");

                System.out.println("1. Menu Management");
                System.out.println("2. Order Management");
                System.out.println("3. Report Generation");
                System.out.println("4. View Menu and Pending Order");

                System.out.println("5. Logout");
                System.out.print("Select an action: ");
                adminAction = scanner.nextLine();
    
                switch (adminAction) {
                    case "1":
                            dispose();
                        handleMenuManagement(scanner);
                        break;
                    case "2":
                            dispose();
                        handleOrderManagement(scanner,orderManager);
                        break;
                    case "3":
                            dispose();
                        admin.generateDailySalesReport();
                        break;
                    case"4":
                            Customer customergui=null;
                            dispose();
                            MenuGUI menuGUI = null;
                            if (menuGUI == null) { 
                                menuGUI = new MenuGUI(menu, customergui,admin);
                                menuGUI.display();
                                System.out.println(" The Menu and Pending Orders are displayed in the GUI. Please check the application window.");

                            }

                        break;
                    case "5":
                            dispose();
                        System.out.println("Logging out...");
                        return; 
                    default:
                            dispose();
                        System.out.println("Invalid option, please try again.");
                }
            }
        } else {
            System.out.println("Invalid password. Access denied.");
        }
    }
    
    private static void handleMenuManagement(Scanner scanner) {
        String action;
        while (true) {
            



            System.out.println("\n--- Menu Management ---");
            System.out.println("1. Add new items");
            System.out.println("2. Update existing items");
            System.out.println("3. Remove items");
            System.out.println("4. Back to Admin Menu");
            System.out.print("Select an action: ");
            action = scanner.nextLine();
    
            switch (action) {
                case "1":
                    System.out.print("Enter food name: ");
                    String name = scanner.nextLine();
                    
                    System.out.print("Enter food category: ");
                    String category = scanner.nextLine();
                    
                    System.out.print("Enter food price: ");
                    double price = Double.parseDouble(scanner.nextLine());
                    
                    System.out.print("Enter availability (true/false): ");
                    boolean availability = scanner.nextBoolean();
                    scanner.nextLine(); 
                    
                    admin.addMenuItem(name, category, price, availability);
                    break;
                case "2":
                    admin.updateItemDetails();
                    break;
                case "3":
                    System.out.print("Enter food item name to remove: "); 
                    String removeItemName = scanner.nextLine();
                    admin.removeMenuItem(removeItemName);
                    break;
                case "4":
                    dispose();
                    return; 
                default:
                    dispose();
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

        public static void dispose(){
            if (jFramemenu != null) {
                jFramemenu.dispose(); 
            }
            if (jFrameorder != null) {
                jFrameorder.dispose(); 
            }
        }
    
    private static void handleOrderManagement(Scanner scanner,OrderManager orderManager) {
        String action;
        OrderGUIadmin adminordergui;
        while (true) {
            System.out.println("\n--- Order Management ---");
            System.out.println("1. View pending orders");
            System.out.println("2. Update order status");
            System.out.println("3. Process refunds");
            System.out.println("4. Handle special requests");
            System.out.println("5. View all orders details");
            System.out.println("6. View a order details");
            System.out.println("7. Back to Admin Menu");
            System.out.print("Select an action: ");
            
            action = scanner.nextLine();
            
            switch (action) {
                case "1":
                    
                    admin.viewPendingOrders();

                    break;
                case "2":
                    if(Main.jFrameadminorder!=null){
                        jFrameadminorder.dispose();
                    }
                    adminordergui= new OrderGUIadmin(orderManager);
                    adminordergui.display();
                    System.out.println(" The not Completed Orders are also displayed in the GUI. Please check the application window.");

                    admin.updateOrderStatus();  
                    adminguidispose();;                  
                    break;
                case "3":
                    admin.processRefund(); 
                    break;
                case "4":
                   admin.handleSpecialRequests();
                    break;
                case "5":
                    admin.viewOrderHistory();
                    break;
                case "6":
                    admin.viewaorder();
                case "7":
                    if(Main.jFrameadminorder!=null){
                        jFrameadminorder.dispose();
                    }
                    return; 
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }
    
    public static  void adminguidispose(){
        if(Main.jFrameadminorder!=null){
            jFrameadminorder.dispose();
        }
    }
    
    

    

    

    private static void initializeMenu() {
        // North Indian Category
        menu.addItem(new FoodItem("Aloo Paratha", "North Indian", 40, true));
        menu.addItem(new FoodItem("Paneer Butter Masala", "North Indian", 120, true));
        menu.addItem(new FoodItem("Rajma Chawal", "North Indian", 90, true));
        menu.addItem(new FoodItem("Kadhi Chawal", "North Indian", 85, true));
        menu.addItem(new FoodItem("Aloo Gobi", "North Indian", 80, true));
    
        // Punjabi Category
        menu.addItem(new FoodItem("Chole Bhature", "Punjabi", 100, true));
        menu.addItem(new FoodItem("Shahi Paneer", "Punjabi", 130, true));
        menu.addItem(new FoodItem("Butter Naan", "Punjabi", 30, true));
        menu.addItem(new FoodItem("Garlic Naan", "Punjabi", 35, true));
        menu.addItem(new FoodItem("Dahi Bhalla", "Punjabi", 40, true));
    
        // Chinese Category
        menu.addItem(new FoodItem("Chilli Potato", "Chinese", 60, true));
        menu.addItem(new FoodItem("Veg Chowmein", "Chinese", 70, true));
        menu.addItem(new FoodItem("Chicken Chowmein", "Chinese", 90, false));
        menu.addItem(new FoodItem("Veg Momos", "Chinese", 50, true));
        menu.addItem(new FoodItem("Chicken Momos", "Chinese", 70, false));
    
        // Western Category
        menu.addItem(new FoodItem("White Sauce Pasta", "Western", 100, true));
        menu.addItem(new FoodItem("Red Sauce Pasta", "Western", 110, true));
        menu.addItem(new FoodItem("Mix Sauce Pasta", "Western", 120, true));
        menu.addItem(new FoodItem("Veg Burger", "Western", 50, true));
        menu.addItem(new FoodItem("Chicken Burger", "Western", 80, false));
    
        // Fast Food Category
        menu.addItem(new FoodItem("Plain Maggi", "Fast Food", 30, true));
        menu.addItem(new FoodItem("Masala Maggi", "Fast Food", 35, true));
        menu.addItem(new FoodItem("Cheese Maggi", "Fast Food", 50, true));
        menu.addItem(new FoodItem("Cold Drink", "Fast Food", 20, true));
        menu.addItem(new FoodItem("Soda", "Fast Food", 15, true));
        menu.addItem(new FoodItem("Cheese Burger", "Fast Food", 80, true));
        menu.addItem(new FoodItem("Arrabiata Pasta", "Fast Food", 100, true));
    
        // Dessert Category
        menu.addItem(new FoodItem("Gulab Jamun", "Dessert", 30, true));
        menu.addItem(new FoodItem("Rasmalai", "Dessert", 50, true));
        menu.addItem(new FoodItem("Gajar Halwa", "Dessert", 60, true));
        menu.addItem(new FoodItem("Kesar Kulfi", "Dessert", 40, true));
        menu.addItem(new FoodItem("Ice Cream", "Dessert", 40, true));
    
        // Rice & Pulses Category
        menu.addItem(new FoodItem("Plain Rice", "Rice & Pulses", 50, true));
        menu.addItem(new FoodItem("Veg Pulav", "Rice & Pulses", 90, true));
        menu.addItem(new FoodItem("Paneer Pulav", "Rice & Pulses", 110, true));
        menu.addItem(new FoodItem("Chicken Biryani", "Rice & Pulses", 180, false));
        menu.addItem(new FoodItem("Veg Biryani", "Rice & Pulses", 120, true));
    
        // Beverages Category
        menu.addItem(new FoodItem("Lassi", "Beverage", 30, true));
        menu.addItem(new FoodItem("Cold Drink", "Beverage", 20, true));
        menu.addItem(new FoodItem("Soda", "Beverage", 15, true));
      
        // West Indian Category
        menu.addItem(new FoodItem("Pav Bhaji", "West Indian", 100, true));
        menu.addItem(new FoodItem("Vada Pav", "West Indian", 30, true));
        menu.addItem(new FoodItem("Misal Pav", "West Indian", 80, true));
    
        // South Indian Category
        menu.addItem(new FoodItem("Plain Dosa", "South Indian", 50, true));
        menu.addItem(new FoodItem("Masala Dosa", "South Indian", 70, true));
        menu.addItem(new FoodItem("Onion Dosa", "South Indian", 80, true));
        menu.addItem(new FoodItem("Rava Dosa", "South Indian", 75, true));
        menu.addItem(new FoodItem("Uttapam", "South Indian", 60, true));
        menu.addItem(new FoodItem("Idli Sambar", "South Indian", 40, true));
        menu.addItem(new FoodItem("Medu Vada", "South Indian", 45, true));
    }
    
}
