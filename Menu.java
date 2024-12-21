import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Menu {
    public  Map<String, ArrayList<FoodItem>> menuItems;

    public Menu() {
        this.menuItems = new HashMap<>();
    }

    public boolean addItem(FoodItem item) {
        String category = item.getCategory();
        ArrayList<FoodItem> items = menuItems.get(category);
        if (items != null && items.contains(item)) {
            System.out.println("Item already exists in the menu.");
            return false;
        }
        
        menuItems.computeIfAbsent(category, k -> new ArrayList<>()).add(item);
        return true;
    }
    

    public FoodItem getFoodItemByName(String name) {
        for (ArrayList<FoodItem> items : menuItems.values()) {
            for (FoodItem item : items) {
                if (item.getName().equalsIgnoreCase(name)) {
                    return item;
                }
            }
        }
        return null; 
    }

    public void updateItemPrice(String name, double newPrice) {
        FoodItem item = getFoodItemByName(name);
        if (item != null) {
            item.setPrice(newPrice); 
            System.out.println("Updated price of " + name + " to Rs. " + newPrice);
        } else {
            System.out.println("Food item " + name + " not found in the menu.");
        }
    }

    public void setItemAvailability(String name, boolean isAvailable) {
        FoodItem item = getFoodItemByName(name);
        if (item != null) {
            item.setAvailability(isAvailable);
            System.out.println("Updated availability of " + name + " to " + (isAvailable ? "Available" : "Unavailable"));
        } else {
            System.out.println("Food item " + name + " not found in the menu.");
        }
    }

    public void removeItem(String name) {
        FoodItem itemToRemove = getFoodItemByName(name);

        if (itemToRemove != null) {
            String category = itemToRemove.getCategory();
            menuItems.get(category).remove(itemToRemove);
            System.out.println("Menu item " + name + " removed successfully.");
        } else {
            System.out.println("Food item " + name + " not found in the menu.");
        }
    }

    public void displayMenu() {
        System.out.println("\n---- Menu ----");
        for (Map.Entry<String, ArrayList<FoodItem>> entry : menuItems.entrySet()) {
            System.out.println("\nCategory: " + entry.getKey());
            if (entry.getValue().isEmpty()) {
                System.out.println("No items available in this category.");
            } else {
                for (FoodItem item : entry.getValue()) {
                    if(item.isAvailable()){
                        String s= String.format(" %s  | Rs. %.2f ",item.getName(), item.getPrice());
                        System.out.println(s);
                    }
                }
            }
        }
    }

    public void searchItemByName(String itemName) {
        boolean itemFound = false;

        for (ArrayList<FoodItem> items : menuItems.values()) {
            for (FoodItem item : items) {
                if (item.getName().toLowerCase().contains(itemName.toLowerCase())) {
                    if(item.isAvailable()){
                    System.out.println("Item found: " + item.getName() + " |Category: " + item.getCategory()+ " |Price: Rs. " + item.getPrice());
                    itemFound = true;}
                    else{
                        System.out.println("Item found but currently unavailable: " + item.getName() + " |Category: " + item.getCategory()+ " |Price: Rs. " + item.getPrice());

                    }
                }
                
            }
        }

        if (!itemFound) {
            System.out.println("Item not found in the menu.");
        }
    }

    public void filterMenuByCategory(String category) {
        boolean categoryFound = false;

        for (String key : menuItems.keySet()) {
            if (key.equalsIgnoreCase(category)) {
                categoryFound = true;
                ArrayList<FoodItem> items = menuItems.get(key);

                System.out.println("\nItems in category: " + key);
                if (items.isEmpty()) {
                    System.out.println("No items available in this category.");
                } else {
                    for (FoodItem item : items) {
                        String availability = item.isAvailable() ? "Available" : "Unavailable";
                        System.out.println(item.getName() + " | Price: Rs. " + item.getPrice() + " | " + availability);
                    }
                }
                break;
            }
        }

        if (!categoryFound) {
            System.out.println("Category '" + category + "' not found in the menu.");
        }
    }

    
    

public void sortMenuByPrice(String choice) {
    switch (choice) {
        case "1":
            System.out.println("\n---- All Items Sorted by Price ----");

            ArrayList<FoodItem> allItems = new ArrayList<>();
            for (ArrayList<FoodItem> items : menuItems.values()) {
                allItems.addAll(items);
            }
            Collections.sort(allItems, Comparator.comparingDouble(FoodItem::getPrice));

            for (FoodItem item : allItems) {
                System.out.println("Category: " + item.getCategory() + " | " +
                "Name: " + item.getName() + " | " +
                "Price: Rs." + item.getPrice() + " | " +
                (item.isAvailable() ? "Available" : "Unavailable"));
            }
            break;

        case "2":
            System.out.println("\n---- Menu Sorted by Price (Category-wise) ----");

            for (Map.Entry<String, ArrayList<FoodItem>> entry : menuItems.entrySet()) {
                String category = entry.getKey();
                ArrayList<FoodItem> items = entry.getValue(); 

                Collections.sort(items, Comparator.comparingDouble(FoodItem::getPrice));

                System.out.println("\nCategory: " + category);
                for (FoodItem item : items) {
                    System.out.println("Name: " + item.getName() + " | " +
                         "Price: Rs." + item.getPrice() + " | " +
                        (item.isAvailable() ? "Available" : "Unavailable"));
                }
            }
            break;

        default:
            System.out.println("Invalid choice. Please enter 1 or 2.");
            break;
    }
}


    public Map<String, ArrayList<FoodItem>> getMenuItems() {
        return menuItems;
    }
}
