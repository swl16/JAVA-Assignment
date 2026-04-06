package cinema;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;

public class FoodPaymentFrame extends JFrame {
    private JFrame parentFrame;
    private ArrayList<Item> basketItems;
    private double totalAmount;

    // Matching your Concession.java UI Colors exactly
    private final Color bgcolor = new Color(0x242424);
    private final Color redcolor = new Color(0xD44444);
    private final Color textcolor = new Color(0xF7F7F7);
    private final Color inputbg = new Color(0x1E1E1E);

    public FoodPaymentFrame(JFrame parentFrame, ArrayList<Item> basketItems, double totalAmount) {
        this.parentFrame = parentFrame;
        this.basketItems = basketItems;
        this.totalAmount = totalAmount;

        setTitle("TGC Cinema - F&B Payment");
        setSize(450, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bgcolor);

        // --- Header ---
        JPanel header = new JPanel();
        header.setBackground(bgcolor);
        header.setBorder(new EmptyBorder(20, 0, 10, 0));
        JLabel title = new JLabel("F&B CHECKOUT");
        title.setFont(new Font("Courier New", Font.BOLD, 24));
        title.setForeground(textcolor);
        header.add(title);

        // --- Order Summary ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(bgcolor);
        centerPanel.setBorder(new EmptyBorder(10, 30, 10, 30));

        JTextArea summary = new JTextArea();
        summary.setEditable(false);
        summary.setBackground(inputbg);
        summary.setForeground(textcolor);
        summary.setFont(new Font("Courier New", Font.PLAIN, 13));
        summary.setBorder(new TitledBorder(new LineBorder(redcolor), "Receipt Preview",
                TitledBorder.LEFT, TitledBorder.TOP, null, redcolor));

        StringBuilder sb = new StringBuilder("\n");
        for (Item item : basketItems) {
            String displayName = item.detail.equals("-") ? item.name : item.name + " (" + item.detail + ")";
            sb.append(String.format(" %-22s x%d\n", displayName, item.quantity));
        }
        summary.setText(sb.toString());

        // --- QR Payment Area ---
        JPanel qrPanel = new JPanel(new GridBagLayout());
        qrPanel.setPreferredSize(new Dimension(200, 200));
        qrPanel.setMaximumSize(new Dimension(200, 200));
        qrPanel.setBackground(Color.WHITE);
        qrPanel.setBorder(new LineBorder(redcolor, 4));
        JLabel qrLabel = new JLabel("SCAN QR");
        qrLabel.setFont(new Font("Courier New", Font.BOLD, 22));
        qrPanel.add(qrLabel);

        centerPanel.add(new JScrollPane(summary));
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(qrPanel);
        centerPanel.add(Box.createVerticalStrut(15));

        JLabel amountLabel = new JLabel("TOTAL: RM " + String.format("%.2f", totalAmount));
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        amountLabel.setForeground(new Color(255, 204, 0));
        amountLabel.setFont(new Font("Courier New", Font.BOLD, 22));
        centerPanel.add(amountLabel);

        // --- Buttons ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        bottomPanel.setBackground(bgcolor);

        JButton backBtn = createStyledButton("BACK", false);
        JButton paidBtn = createStyledButton("PAID", true);

        backBtn.addActionListener(e -> {
            dispose();
            parentFrame.setVisible(true); // Returns to your Concession UI
        });

        paidBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Payment Successful!\nShow this screen at the counter.");
            dispose();
            parentFrame.dispose(); // Closes the concession app after successful purchase
        });

        bottomPanel.add(backBtn);
        bottomPanel.add(paidBtn);

        add(header, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createStyledButton(String text, boolean primary) {
        JButton btn = new JButton(text);
        btn.setForeground(textcolor);
        btn.setBackground(primary ? redcolor : new Color(0x3B3B3B));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Courier New", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(130, 45));
        btn.setBorder(new LineBorder(new Color(0x555555)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}