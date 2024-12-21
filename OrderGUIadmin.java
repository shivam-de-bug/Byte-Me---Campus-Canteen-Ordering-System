
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class OrderGUIadmin {
    private JFrame frame;
    private JPanel panel;
    private OrderManager orderManager;

    public OrderGUIadmin( OrderManager orderManager) {
        this.orderManager=orderManager; 
        initialize();
    }
   

    public void initialize() {
        frame = new JFrame("All Pending Orders");
        frame.setSize(800, 600);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel title = new JLabel("Pending Orders", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Order ID", "Items", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        ArrayList<Order> orders = orderManager.getOrderHistory();  
        for (Order order : orders) {
            if (!(order.getStatus().equalsIgnoreCase("Delivered") || order.getStatus().equalsIgnoreCase("Completed"))) {
                model.addRow(new Object[]{
                        order.getOrderId(),
                        FoodItem.formatOrder(order.getItems()),
                        order.getStatus()
                });
            }
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel);
        Main.jFrameadminorder=frame;
    }

    public void display() {
        frame.setVisible(true);
    }
    public void dispose(){
        Main.jFrameadminorder=null;
        frame.dispose();
    }
    
}
