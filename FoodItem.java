
import java.util.ArrayList;
import java.util.Map;


class FoodItem implements Comparable<FoodItem> {
    private String name;
    private String category;
    private double price;
    private boolean isAvailable;
    private ArrayList<String> reviews;

    public FoodItem(String name, String category, double price, boolean Available) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.isAvailable = Available;
        this.reviews = new ArrayList<>();
    }
    public void addReview(String review) {
        reviews.add(review);
    }
    public void viewReviews() {
        if (reviews.isEmpty()) {
            System.out.println("No reviews yet for " + name);
        } else {
            System.out.println("Reviews for " + name + ":");
            for (String review : reviews) {
                System.out.println("- " + review);
            }
        }
    }
    public void setPrice(double price) {
        this.price = price;
    }
    
    public void setAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getName() {
        return name;
    }
    public void setName(String newname) {
       this.name=newname;
    }
    
    public String getCategory() {
        return category;
    }
    public void setCategory(String newcategory) {
        this.category=newcategory;
    }
    
    public double getPrice() {
        return price;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    public String getAvailability(){
        String s;
        if(isAvailable()){
            s="Available";
        }
        else{
            s="Unavailable";
        }
        return s;
    }
public static String formatOrder(Map<FoodItem, Integer> foodItems) {
    StringBuilder formattedOrder = new StringBuilder();

    for (Map.Entry<FoodItem, Integer> entry : foodItems.entrySet()) {
        FoodItem item = entry.getKey();       
        Integer quantity = entry.getValue(); 

        formattedOrder.append("=>").append(item.getName())               
                      .append(" | ")                        
                      .append(item.getCategory())         
                      .append(" | Rs. ").append(String.format("%.2f", item.getPrice())) // Price with two decimal places
                      .append(" | Quantity: ").append(quantity)  
                      .append(" | ")                       
                      .append(item.getAvailability())      
                      .append("\n");                      
    }

    return formattedOrder.toString(); 
}

    @Override
    public String toString() {
        String detail; 
        if (isAvailable()) {
            detail = String.format("%s | %s | Rs. %.2f | Available", name, category, price);
        } else {
            detail = String.format("%s | %s | Rs. %.2f | Unavailable", name, category, price);
        }
        return detail; 
    }

    @Override
    public int compareTo(FoodItem other) {
        return this.name.compareToIgnoreCase(other.name); 
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FoodItem other = (FoodItem) obj;
        return name.equalsIgnoreCase(other.name) && category.equalsIgnoreCase(other.category);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode() + category.toLowerCase().hashCode();
    }
}
