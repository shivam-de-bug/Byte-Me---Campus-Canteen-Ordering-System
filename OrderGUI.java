
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class OrderGUI {
    private JFrame frame;
    private JPanel panel;
    private Customer customer;
    private Menu menu; 
    private Admin admingui;
    public OrderGUI( Menu menu,Customer customer,Admin admingui) {
        this.menu = menu; 
        this.customer=customer;
        this.admingui=admingui;
        initialize();
    }
   

    public void initialize() {
        frame = new JFrame("Your Pending Orders");
        
            frame.setSize(800, 600);
        
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel title = new JLabel("Pending Orders", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Order ID", "Items", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        ArrayList<Order> orders=new ArrayList<>() ;
        if((admingui==null)&&(customer!=null)){
         orders = customer.orderHistory;}
         else if((admingui!=null)&&(customer==null)){
            orders = OrderManager.orderHistory;
         }
        for (Order order : orders) {
            if (!(order.getStatus().equalsIgnoreCase("Delivered")
             || order.getStatus().equalsIgnoreCase("Completed")||
            order.getStatus().equalsIgnoreCase("Cancelled")||
            order.getStatus().equalsIgnoreCase("Refunded")||
            order.getStatus().equalsIgnoreCase("Denied"))){
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

        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> switchToMenuPage());
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
        Main.jFrameorder=frame;
    }

    private void switchToMenuPage() {

        MenuGUI menuGUI = new MenuGUI(menu,customer,admingui);
        menuGUI.display();
        frame.dispose(); 
    }

    public void display() {
        frame.setVisible(true);
    }
    public void dispose(){
        Main.jFrameorder=null;
        frame.dispose();
    }
    
}
