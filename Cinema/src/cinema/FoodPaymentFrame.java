package cinema;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;

public class FoodPaymentFrame extends JFrame {
    private JFrame parentFrame;
    private ArrayList<Item> basketItems;
    private double totalAmount;
    private CardLayout cardLayout;
    private JPanel mainContainer;

    // UI Style Colors
    private final Color bgcolor = new Color(0x242424);
    private final Color redcolor = new Color(0xD44444);
    private final Color textcolor = new Color(0xF7F7F7);
    private final Color inputbg = new Color(0x1E1E1E);

    public FoodPaymentFrame(JFrame parentFrame, ArrayList<Item> basketItems, double totalAmount) {
        this.parentFrame = parentFrame;
        this.basketItems = basketItems;
        this.totalAmount = totalAmount;

        setTitle("TGC Cinema - F&B Secure Payment");
        setSize(450, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(bgcolor);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // Add different payment views
        mainContainer.add(methodSelectionPanel(), "METHODS");
        mainContainer.add(qrPaymentPanel(), "QR");
        mainContainer.add(cardPaymentPanel(), "CARD");

        add(mainContainer);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // --- VIEW 1: Selection between Card or QR ---
    private JPanel methodSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgcolor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Select Payment Method", SwingConstants.CENTER);
        title.setForeground(textcolor);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.gridy = 0; panel.add(title, gbc);

        JButton cardBtn = createMenuButton("Pay by Credit / Debit Card");
        cardBtn.addActionListener(e -> cardLayout.show(mainContainer, "CARD"));
        gbc.gridy = 1; panel.add(cardBtn, gbc);

        JButton qrBtn = createMenuButton("Pay by E-Wallet QR");
        qrBtn.addActionListener(e -> cardLayout.show(mainContainer, "QR"));
        gbc.gridy = 2; panel.add(qrBtn, gbc);

        JLabel totalLbl = new JLabel("Total: RM " + String.format("%.2f", totalAmount), SwingConstants.CENTER);
        totalLbl.setForeground(new Color(255, 204, 0));
        totalLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridy = 3; panel.add(totalLbl, gbc);

        return panel;
    }

    // --- VIEW 2: Modern Card Input (Based on your Reference) ---
    private JPanel cardPaymentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgcolor);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0; panel.add(createInputLabel("Card Holder Name"), gbc);
        JTextField holder = createStyledTextField("Full Name");
        gbc.gridy++; panel.add(holder, gbc);

        gbc.gridy++; panel.add(createInputLabel("Card Number"), gbc);
        JTextField cardNum = createStyledTextField("••••  ••••  ••••  ••••");
        gbc.gridy++; panel.add(cardNum, gbc);

        // Row for Expiry/CVC
        JPanel row = new JPanel(new GridLayout(1, 2, 15, 0));
        row.setBackground(bgcolor);
        row.add(createFieldGroup("Expiry (MM/YY)", "MM / YY"));
        row.add(createFieldGroup("CVC", "•••"));
        gbc.gridy++; panel.add(row, gbc);

        JButton submit = createStyledButton("Submit Payment", true);
        submit.addActionListener(e -> processSuccess());
        gbc.gridy++; gbc.insets = new Insets(25, 0, 5, 0);
        panel.add(submit, gbc);

        return panel;
    }

    // --- VIEW 3: QR Payment View ---
    private JPanel qrPaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 20));
        panel.setBackground(bgcolor);
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel qrTitle = new JLabel("Scan to Pay", SwingConstants.CENTER);
        qrTitle.setForeground(textcolor);
        qrTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panel.add(qrTitle, BorderLayout.NORTH);

        // --- QR Image Box ---
        JPanel qrBox = new JPanel(new BorderLayout()); // Changed to BorderLayout for image centering
        qrBox.setPreferredSize(new Dimension(220, 220));
        qrBox.setBackground(Color.WHITE);
        qrBox.setBorder(new LineBorder(redcolor, 4));

        try {
            // Use the same path format as your other Cinema modules
            ImageIcon rawIcon = new ImageIcon("Cinema/src/cinema/qr_code.png");
            Image scaledImg = rawIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            qrBox.add(new JLabel(new ImageIcon(scaledImg)), BorderLayout.CENTER);
        } catch (Exception e) {
            // Fallback text if the image file is missing
            qrBox.add(new JLabel("QR IMAGE NOT FOUND", SwingConstants.CENTER), BorderLayout.CENTER);
        }

        panel.add(qrBox, BorderLayout.CENTER);

        JButton paid = createStyledButton("I Have Paid", true);
        paid.addActionListener(e -> processSuccess());
        panel.add(paid, BorderLayout.SOUTH);

        return panel;
    }

    private void processSuccess() {
        JOptionPane.showMessageDialog(this, "Order Received!\nTotal Paid: RM " + String.format("%.2f", totalAmount));
        dispose();
        parentFrame.dispose();
    }

    // --- UI HELPERS ---
    private JPanel createFieldGroup(String label, String placeholder) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setBackground(bgcolor);
        p.add(createInputLabel(label), BorderLayout.NORTH);
        p.add(createStyledTextField(placeholder), BorderLayout.CENTER);
        return p;
    }

    private JLabel createInputLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(180, 180, 180));
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return l;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField f = new JTextField(placeholder);
        f.setPreferredSize(new Dimension(0, 40));
        f.setBackground(new Color(30, 30, 30));
        f.setForeground(Color.GRAY);
        f.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(60, 60, 60)), new EmptyBorder(0, 10, 0, 10)));
        return f;
    }

    private JButton createMenuButton(String text) {
        JButton b = createStyledButton(text, false);
        b.setPreferredSize(new Dimension(300, 50));
        return b;
    }

    private JButton createStyledButton(String text, boolean primary) {
        JButton btn = new JButton(text);
        btn.setForeground(textcolor);
        btn.setBackground(primary ? redcolor : new Color(0x3B3B3B));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}