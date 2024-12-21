
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class MenuGUI {
    private JFrame frame;
    private JPanel panel;
    private Menu menu;
    private Customer customer;
    private OrderGUI orderGUI;
    private Admin admingui;
    public MenuGUI(Menu menu,Customer customer,Admin admingui) {
        this.menu = menu;
        this.customer=customer;
        this.admingui=admingui;
        initialize();
    }

    public void initialize() {
        frame = new JFrame("Canteen Menu");
        
        frame.setSize(800, 600);
        

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel title = new JLabel("Canteen Menu", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Food Item", "Category", "Price", "Availability"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        Map<String, ArrayList<FoodItem>> menuItems = menu.getMenuItems();
        for (Map.Entry<String, ArrayList<FoodItem>> entry : menuItems.entrySet()) {
            String category = entry.getKey();
            for (FoodItem item : entry.getValue()) {
                model.addRow(new Object[]{
                        item.getName(), category, item.getPrice(),
                        item.isAvailable() ? "Available" : "Unavailable"
                });
            }
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
       if((admingui==null)&&(customer!=null)){
        JPanel buttonPanel = new JPanel();
        JButton ordersButton = new JButton("View Pending Orders");
        ordersButton.addActionListener(e -> switchToOrdersPage());
        buttonPanel.add(ordersButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
       }else if((admingui!=null)&&(customer==null)){
        JPanel buttonPanel = new JPanel();
        JButton ordersButton = new JButton("View Pending Orders");
        ordersButton.addActionListener(e -> switchToOrdersPage());
        buttonPanel.add(ordersButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
       }
        frame.add(panel);
        frame.setVisible(true);
        Main.jFramemenu=frame;

    }

    private void switchToOrdersPage() {
        orderGUI = new OrderGUI(menu,customer,admingui); 
        Main.jFramemenu=null;

        orderGUI.display();
        frame.dispose(); 
    }
    public void display() {
        frame.setVisible(true);
    }
    public JFrame getFrame() {
        if(orderGUI!=null){
            orderGUI.dispose();
        }
        
        return frame;
    }
    
    
}
