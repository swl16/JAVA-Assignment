package cinema;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;

// Helper class to store cart items
class Item {
    String name;
    String detail;
    double price;
    int quantity;

    Item(String name, String detail, double price, int quantity) {
        this.name = name;
        this.detail = detail;
        this.price = price;
        this.quantity = quantity;
    }
}

public class Concession {

    JFrame frame;
    JPanel mainPanel;
    CardLayout cardLayout;

    boolean fromTicket = false;

    // UI Style Colors
    final Color bgcolor = new Color(0x242424);
    final Color panelcolor = new Color(0x2E2E2E);
    final Color bordercolor = new Color(0x444444);
    final Color textcolor = new Color(0xF7F7F7);
    final Color cardcolor = new Color(0x363636);
    final Color redcolor = new Color(0xD44444);
    final Color inputbg = new Color(0x1E1E1E);

    DefaultTableModel cartModel;
    JTable cartTable;
    JLabel totalLabel;

    ArrayList<Item> basket = new ArrayList<>();

    // Constructor
    public Concession(boolean fromTicket) {
        this.fromTicket = fromTicket;

        frame = new JFrame("TGC Cinema - Concession");
        frame.setSize(650, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add views to CardLayout
        if (fromTicket) {
            mainPanel.add(concessionUI(), "CONCESSION");
            cardLayout.show(mainPanel, "CONCESSION");
        } else {
            mainPanel.add(menu(), "MENU");
            mainPanel.add(concessionUI(), "CONCESSION");
            cardLayout.show(mainPanel, "MENU");
        }

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Initial Welcome Menu
    private JPanel menu() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(bgcolor);

        JLabel title = new JLabel("F&B CONCESSION");
        title.setFont(new Font("Courier New", Font.BOLD, 32));
        title.setForeground(textcolor);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(150));
        panel.add(title);
        panel.add(Box.createVerticalStrut(40));

        JButton enterBtn = styledButton("ENTER", true);
        enterBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        enterBtn.addActionListener(e -> cardLayout.show(mainPanel, "CONCESSION"));

        panel.add(enterBtn);
        return panel;
    }

    // Main Shopping UI
    private JPanel concessionUI() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgcolor);

        panel.add(sectionHeader("SELECT ITEMS"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(bgcolor);

        // Sidebar with Category Buttons
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(bgcolor);
        left.setBorder(new EmptyBorder(20, 20, 20, 20));

        JButton foodBtn = styledButton("ADD FOOD", true);
        JButton drinkBtn = styledButton("ADD DRINK", true);
        JButton comboBtn = styledButton("ADD COMBO", true);

        foodBtn.addActionListener(e -> addItem("food"));
        drinkBtn.addActionListener(e -> addItem("drink"));
        comboBtn.addActionListener(e -> addItem("combo"));

        left.add(foodBtn);
        left.add(Box.createVerticalStrut(15));
        left.add(drinkBtn);
        left.add(Box.createVerticalStrut(15));
        left.add(comboBtn);

        // Shopping Cart Table
        String[] cols = {"Item", "Price", "Qty", "Total"};
        cartModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        cartTable = new JTable(cartModel);
        styleTable(cartTable);

        JScrollPane scroll = new JScrollPane(cartTable);
        scroll.setBorder(new LineBorder(bordercolor));
        scroll.getViewport().setBackground(inputbg);

        // Footer Section (Total and Actions)
        totalLabel = new JLabel("Total: RM 0.00");
        totalLabel.setForeground(textcolor);
        totalLabel.setFont(new Font("Courier New", Font.BOLD, 18));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(bgcolor);
        bottom.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(bgcolor);

        JButton removeBtn = styledButton("REMOVE", false);
        JButton checkoutBtn = styledButton("CHECKOUT", true);
        JButton backBtn = styledButton("BACK", false);

        removeBtn.addActionListener(e -> removeItem());
        checkoutBtn.addActionListener(e -> checkout());
        backBtn.addActionListener(e -> {
            if (fromTicket) frame.dispose();
            else cardLayout.show(mainPanel, "MENU");
        });

        btnPanel.add(backBtn);
        btnPanel.add(removeBtn);
        btnPanel.add(checkoutBtn);

        bottom.add(totalLabel, BorderLayout.WEST);
        bottom.add(btnPanel, BorderLayout.EAST);

        content.add(left, BorderLayout.WEST);
        content.add(scroll, BorderLayout.CENTER);

        panel.add(content, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    // Logic for Adding Items with Sub-Options
    private void addItem(String type) {
        String[] options;
        double[] prices;

        if (type.equals("food")) {
            options = new String[]{"Popcorn", "Hotdog", "Nachos"};
            prices = new double[]{15.00, 12.00, 10.00};
        } else if (type.equals("drink")) {
            options = new String[]{"Carbonated soft drinks", "Water", "Coffee"};
            prices = new double[]{8.00, 3.00, 7.00};
        } else {
            options = new String[]{"Combo A (Popcorn + Soda)", "Combo B (Hotdog + Soda)", "Combo C (Nachos + Soda)"};
            prices = new double[]{22.00, 20.00, 18.00};
        }

        JComboBox<String> itemBox = new JComboBox<>(options);

        // Define sub-option menus
        JComboBox<String> popFlavorBox = new JComboBox<>(new String[]{"Classic Butter", "Caramel", "Cheddar"});
        JComboBox<String> sodaBox = new JComboBox<>(new String[]{"Coca-Cola", "Fanta", "Sprite"});
        JComboBox<String> coffeeBox = new JComboBox<>(new String[]{"Latte", "Cappuccino", "Mocha"});

        JSpinner qtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        JLabel contextLabel = new JLabel("Option:");

        panel.add(new JLabel("Select Item:"));
        panel.add(itemBox);
        panel.add(contextLabel);
        panel.add(new JLabel("-")); // Placeholder slot at index 3
        panel.add(new JLabel("Quantity:"));
        panel.add(qtySpinner);

        // Update the pop-up panel based on selection
        Runnable updateFields = () -> {
            String selected = (String) itemBox.getSelectedItem();
            panel.remove(3);

            if (selected.equals("Popcorn")) {
                contextLabel.setText("Flavour:");
                panel.add(popFlavorBox, 3);
            } else if (selected.equals("Carbonated soft drinks") || selected.startsWith("Combo")) {
                contextLabel.setText("Soda Choice:");
                panel.add(sodaBox, 3);
            } else if (selected.equals("Coffee")) {
                contextLabel.setText("Coffee Type:");
                panel.add(coffeeBox, 3);
            } else {
                contextLabel.setText("-");
                panel.add(new JLabel("N/A"), 3);
            }
            panel.revalidate();
            panel.repaint();
        };

        itemBox.addActionListener(e -> updateFields.run());
        updateFields.run();

        int result = JOptionPane.showConfirmDialog(frame, panel, "Item Customization", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            int idx = itemBox.getSelectedIndex();
            String name = options[idx];
            double price = prices[idx];
            int qty = (int) qtySpinner.getValue();

            String detail = "-";
            if (name.equals("Popcorn")) detail = (String) popFlavorBox.getSelectedItem();
            else if (name.equals("Carbonated soft drinks") || name.startsWith("Combo")) detail = (String) sodaBox.getSelectedItem();
            else if (name.equals("Coffee")) detail = (String) coffeeBox.getSelectedItem();

            String display = (detail.equals("-")) ? name : name + " (" + detail + ")";
            cartModel.addRow(new Object[]{display, String.format("%.2f", price), qty, String.format("%.2f", price * qty)});
            basket.add(new Item(name, detail, price, qty));
            updateTotal();
        }
    }

    private void removeItem() {
        int row = cartTable.getSelectedRow();
        if (row >= 0) {
            basket.remove(row);
            cartModel.removeRow(row);
            updateTotal();
        }
    }

    private void checkout() {
        if (basket.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Your cart is empty!");
            return;
        }
        double total = basket.stream().mapToDouble(i -> i.price * i.quantity).sum();
        JOptionPane.showMessageDialog(frame, "Order Received!\nTotal Paid: RM " + String.format("%.2f", total));
        basket.clear();
        cartModel.setRowCount(0);
        updateTotal();
    }

    private void updateTotal() {
        double total = basket.stream().mapToDouble(i -> i.price * i.quantity).sum();
        totalLabel.setText("Total: RM " + String.format("%.2f", total));
    }

    // Styling Methods
    private JPanel sectionHeader(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(panelcolor);
        p.setBorder(new MatteBorder(0, 0, 3, 0, redcolor));
        JLabel lbl = new JLabel("  " + title);
        lbl.setForeground(textcolor);
        lbl.setFont(new Font("Courier New", Font.BOLD, 18));
        lbl.setPreferredSize(new Dimension(0, 50));
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    private JButton styledButton(String text, boolean primary) {
        JButton btn = new JButton(text);
        btn.setForeground(textcolor);
        btn.setBackground(primary ? redcolor : cardcolor);
        btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(new LineBorder(bordercolor), new EmptyBorder(10, 15, 10, 15)));
        btn.setFont(new Font("Courier New", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleTable(JTable table) {
        table.setBackground(inputbg);
        table.setForeground(textcolor);
        table.setRowHeight(35);
        table.setGridColor(bordercolor);
        table.setSelectionBackground(redcolor);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JTableHeader header = table.getTableHeader();
        header.setBackground(panelcolor);
        header.setForeground(redcolor);
        header.setFont(new Font("Courier New", Font.BOLD, 14));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }

        // Start the application
        new Concession(false);
    }
}