package cinema.payment.ui;

import cinema.payment.model.Order;
import cinema.payment.service.PaymentService;

import javax.swing.*;
import java.awt.*;

public class PaymentFrame extends JFrame {

    private CardLayout layout;
    private JPanel container;

    private Order order;
    private PaymentService service;

    private double finalAmount = 0;
    private String paymentMethod = "";

    public PaymentFrame(Order order) {
        this.order = order;
        this.service = new PaymentService();

        layout = new CardLayout();
        container = new JPanel(layout);

        container.add(summaryPanel(), "summary");
        container.add(discountPanel(), "discount");
        container.add(paymentPanel(), "payment");
        container.add(qrPanel(), "qr");
        container.add(receiptPanel(), "receipt");

        add(container);

        setTitle("Payment Module");
        setSize(400, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        layout.show(container, "summary");
    }

    // ================= SUMMARY PANEL =================
    private JPanel summaryPanel() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Color.BLACK);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.BLACK);
        content.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Review Summary");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        content.add(title);
        content.add(Box.createVerticalStrut(15));

        content.add(createSectionLabel("Movie"));
        content.add(createValueLabel(order.getMovieTitle()));

        content.add(createSectionLabel("Cinema"));
        content.add(createValueLabel(order.getCinemaLocation()));

        content.add(createSectionLabel("Time"));
        content.add(createValueLabel(order.getTime()));

        content.add(createSectionLabel("Hall"));
        content.add(createValueLabel(order.getHall()));

        content.add(createSectionLabel("Seats"));
        content.add(createValueLabel(String.join(", ", order.getSeats())));

        content.add(Box.createVerticalStrut(15));
        content.add(createDivider());

        content.add(Box.createVerticalStrut(10));
        content.add(createRow("Tickets",
                order.getTicketQuantity() + " x RM " + order.getTicketPrice()));

        content.add(createRow("F&B", "RM " + order.getFoodTotal()));

        content.add(Box.createVerticalStrut(10));
        content.add(createDivider());

        content.add(Box.createVerticalStrut(10));
        content.add(createTotalRow("Total", "RM " + order.getSubtotal()));

        JButton checkoutBtn = new JButton("Checkout & Pay");
        checkoutBtn.setBackground(Color.YELLOW);
        checkoutBtn.setForeground(Color.BLACK);
        checkoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        checkoutBtn.setFocusPainted(false);

        checkoutBtn.addActionListener(e -> layout.show(container, "discount"));

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.BLACK);
        bottom.add(checkoutBtn);

        main.add(content, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);

        return main;
    }

    // ================= DISCOUNT PANEL =================
    private JPanel discountPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = createValueLabel("Apply Member Discount");

        JTextField memberField = new JTextField();
        JLabel totalLabel = createValueLabel("Total: RM " + order.getSubtotal());

        JButton applyBtn = new JButton("Apply");
        JButton nextBtn = new JButton("Proceed");

        applyBtn.addActionListener(e -> {
            double total = service.calculateTotal(order);
            finalAmount = service.applyDiscount(total, memberField.getText());
            totalLabel.setText("Total: RM " + finalAmount);
        });

        nextBtn.addActionListener(e -> layout.show(container, "payment"));

        panel.add(title);
        panel.add(Box.createVerticalStrut(15));
        panel.add(memberField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(totalLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(applyBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(nextBtn);

        return panel;
    }

    // ================= PAYMENT PANEL =================
    private JPanel paymentPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = createValueLabel("Select Payment Method");

        JButton tng = new JButton("Touch 'n Go eWallet");
        JButton duitnow = new JButton("DuitNow QR");

        styleButton(tng);
        styleButton(duitnow);

        tng.addActionListener(e -> {
            paymentMethod = "TNG";
            layout.show(container, "qr");
        });

        duitnow.addActionListener(e -> {
            paymentMethod = "DuitNow";
            layout.show(container, "qr");
        });

        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(tng);
        panel.add(Box.createVerticalStrut(10));
        panel.add(duitnow);

        return panel;
    }

    // ================= QR PANEL =================
    private JPanel qrPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel qrBox = new JPanel();
        qrBox.setPreferredSize(new Dimension(200, 200));
        qrBox.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        qrBox.setBackground(Color.BLACK);

        JLabel qrLabel = new JLabel("SCAN QR", SwingConstants.CENTER);
        qrLabel.setForeground(Color.WHITE);
        qrBox.add(qrLabel);

        JLabel amount = createValueLabel("Amount: RM " + finalAmount);

        JButton confirm = new JButton("Confirm Payment");
        styleButton(confirm);

        confirm.addActionListener(e -> layout.show(container, "receipt"));

        JPanel center = new JPanel();
        center.setBackground(Color.BLACK);
        center.add(qrBox);

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.BLACK);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.add(amount);
        bottom.add(Box.createVerticalStrut(10));
        bottom.add(confirm);

        panel.add(center, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private void styleButton(JButton btn) {
        btn.setBackground(Color.YELLOW);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
    }

    // ================= RECEIPT PANEL =================
    private JPanel receiptPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea receipt = new JTextArea();
        receipt.setEditable(false);
        receipt.setBackground(Color.BLACK);
        receipt.setForeground(Color.WHITE);
        receipt.setFont(new Font("Monospaced", Font.PLAIN, 13));

        // Generate receipt data
        String receiptId = "RCPT" + System.currentTimeMillis();
        String dateTime = java.time.LocalDateTime.now().toString();

        StringBuilder sb = new StringBuilder();

        sb.append("=========== CINEMA RECEIPT ===========\n");
        sb.append("Receipt ID : ").append(receiptId).append("\n");
        sb.append("Date       : ").append(dateTime).append("\n");
        sb.append("--------------------------------------\n");

        sb.append(String.format("%-15s : %s\n", "Movie", order.getMovieTitle()));
        sb.append(String.format("%-15s : %s\n", "Cinema", order.getCinemaLocation()));
        sb.append(String.format("%-15s : %s\n", "Time", order.getTime()));
        sb.append(String.format("%-15s : %s\n", "Hall", order.getHall()));
        sb.append(String.format("%-15s : %s\n", "Seats",
                String.join(", ", order.getSeats())));

        sb.append("--------------------------------------\n");

        sb.append(String.format("%-20s RM %.2f\n",
                "Tickets", order.getTicketQuantity() * order.getTicketPrice()));

        sb.append(String.format("%-20s RM %.2f\n",
                "F&B", order.getFoodTotal()));

        sb.append("--------------------------------------\n");

        sb.append(String.format("%-20s RM %.2f\n", "TOTAL", finalAmount));
        sb.append(String.format("%-20s %s\n", "Payment", paymentMethod));

        sb.append("--------------------------------------\n");
        sb.append("        THANK YOU & ENJOY!\n");

        receipt.setText(sb.toString());

        panel.add(receipt, BorderLayout.CENTER);

        return panel;
    }

    // ================= UI HELPERS =================

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.GRAY);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        return label;
    }

    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private JSeparator createDivider() {
        return new JSeparator();
    }

    private JPanel createRow(String left, String right) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        JLabel l = new JLabel(left);
        l.setForeground(Color.WHITE);

        JLabel r = new JLabel(right);
        r.setForeground(Color.WHITE);

        panel.add(l, BorderLayout.WEST);
        panel.add(r, BorderLayout.EAST);

        return panel;
    }

    private JPanel createTotalRow(String left, String right) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        JLabel l = new JLabel(left);
        l.setForeground(Color.YELLOW);
        l.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel r = new JLabel(right);
        r.setForeground(Color.YELLOW);
        r.setFont(new Font("Arial", Font.BOLD, 16));

        panel.add(l, BorderLayout.WEST);
        panel.add(r, BorderLayout.EAST);

        return panel;
    }
}