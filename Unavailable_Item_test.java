import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Unavailable_Item_test {

    private Menu menu;
    private Order order;
    private FoodItem outOfStockItem;
    private FoodItem inStockItem;

    @Before
    public void setUp() {

        menu = new Menu();
        outOfStockItem = new FoodItem("Burger","fastfood", 100, false);  
        inStockItem = new FoodItem("Pizza","fastfood", 150, true);  
        menu.addItem(outOfStockItem);
        menu.addItem(inStockItem);

        order = new Order("12345", "101");
    }

    @Test
    public void testOrderOutOfStockItem() {
        order.addItem(outOfStockItem, 1);  
        order.addItem(inStockItem, 2);   

        assertFalse("Out-of-stock item should not be added to the order.",
                order.getItems().containsKey(outOfStockItem));
        assertTrue("In-stock item should be added to the order.",
                order.getItems().containsKey(inStockItem));
    }
    @Test
    public void testOrderingAvailableItem() {
        order.addItem(inStockItem, 2);  
        
        assertTrue("The item should be added to the order because it's available.",
                   order.getItems().containsKey(inStockItem));
        assertEquals("The quantity of the item should be 2.", 2, (int) order.getItems().get(inStockItem));
    }


    @Test
    public void testErrorMessageForOutOfStockItem() {
        order.addItem(outOfStockItem, 1);
        assertEquals("No items should be added for an out-of-stock order.",
                0, order.getItems().size());
    }

   
}
