package cinema;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentFrame extends JFrame {

    private CardLayout layout;
    private JPanel container;

    private Order order;
    private PaymentService service;

    private double finalAmount = 0;
    private String paymentMethod = "";

    private JLabel qrAmountLabel;
    private JLabel timerLabel;
    private JTextArea receiptArea;
    private Timer countdownTimer;
    private int secondsRemaining = 300; // 5 minutes

    public PaymentFrame(Order order) {
        this.order = order;
        this.service = new PaymentService();
        this.finalAmount = order.getSubtotal();

        layout = new CardLayout();
        container = new JPanel(layout);

        container.add(summaryPanel(), "summary");
        container.add(discountPanel(), "discount");
        container.add(paymentPanel(), "payment");
        container.add(qrPanel(), "qr");
        container.add(receiptPanel(), "receipt");

        add(container);

        setTitle("Cinema Payment System");
        setSize(420, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        layout.show(container, "summary");
    }

    // ================= DYNAMIC TIMER LOGIC =================
    private void startTimer() {
        if (countdownTimer != null) countdownTimer.stop();
        secondsRemaining = 300;

        countdownTimer = new Timer(1000, e -> {
            secondsRemaining--;
            if (secondsRemaining >= 0) {
                int mins = secondsRemaining / 60;
                int secs = secondsRemaining % 60;
                timerLabel.setText(String.format("Expires in %02d:%02d", mins, secs));
            } else {
                countdownTimer.stop();
                JOptionPane.showMessageDialog(this, "Session expired. Please restart payment.", "Timed Out", JOptionPane.ERROR_MESSAGE);
                layout.show(container, "payment");
            }
        });
        countdownTimer.start();
    }

    // ================= DISCOUNT PANEL =================
    private JPanel discountPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(18, 18, 18));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = createValueLabel("Member Discount", 22, Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        JTextField memberField = new JTextField(15);
        memberField.setPreferredSize(new Dimension(200, 35));
        memberField.setBackground(new Color(30, 30, 30));
        memberField.setForeground(Color.WHITE);
        memberField.setCaretColor(Color.WHITE);
        memberField.setBorder(new LineBorder(Color.GRAY));
        gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(memberField, gbc);

        JButton applyBtn = new JButton("Apply Member ID");
        styleButton(applyBtn, Color.DARK_GRAY, Color.WHITE);
        gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(applyBtn, gbc);

        JLabel totalLabel = createValueLabel("Payable: RM " + String.format("%.2f", finalAmount), 18, new Color(255, 204, 0));
        gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(totalLabel, gbc);

        JButton nextBtn = new JButton("Proceed to Payment");
        styleButton(nextBtn, new Color(255, 204, 0), Color.BLACK);
        gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(nextBtn, gbc);

        applyBtn.addActionListener(e -> {
            String id = memberField.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Member ID!", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                double sub = service.calculateTotal(order);
                double discounted = service.applyDiscount(sub, id);

                if (discounted < sub) {
                    finalAmount = discounted;
                    totalLabel.setText("Payable: RM " + String.format("%.2f", finalAmount));
                    qrAmountLabel.setText("Amount: RM " + String.format("%.2f", finalAmount));
                    JOptionPane.showMessageDialog(this, "Discount Applied Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Member ID. No discount applied.", "Invalid ID", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        nextBtn.addActionListener(e -> layout.show(container, "payment"));

        return panel;
    }

    // ================= QR PANEL (WITH REAL-TIME TIMER) =================
    private JPanel qrPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(18, 18, 18));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel centerContent = new JPanel();
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));
        centerContent.setBackground(new Color(18, 18, 18));

        qrAmountLabel = createValueLabel("Amount: RM " + String.format("%.2f", finalAmount), 20, Color.WHITE);
        qrAmountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel qrBox = new JPanel(new GridBagLayout());
        qrBox.setPreferredSize(new Dimension(200, 200));
        qrBox.setMaximumSize(new Dimension(200, 200));
        qrBox.setBackground(Color.WHITE);
        qrBox.setBorder(new LineBorder(new Color(255, 204, 0), 4));
        JLabel qrText = new JLabel("SCAN QR");
        qrText.setFont(new Font("Monospaced", Font.BOLD, 22));
        qrBox.add(qrText);
        qrBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        timerLabel = new JLabel("Expires in 05:00", SwingConstants.CENTER);
        timerLabel.setForeground(new Color(200, 200, 200));
        timerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerContent.add(qrAmountLabel);
        centerContent.add(Box.createVerticalStrut(25));
        centerContent.add(qrBox);
        centerContent.add(Box.createVerticalStrut(15));
        centerContent.add(timerLabel);

        JButton confirm = new JButton("I have paid");
        styleButton(confirm, new Color(255, 204, 0), Color.BLACK);

        // Inside qrPanel's confirm.addActionListener:
        confirm.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(this,
                    "Confirm payment of RM " + String.format("%.2f", finalAmount) + "?",
                    "Confirm Transaction",
                    JOptionPane.YES_NO_OPTION); //

            if (response == JOptionPane.YES_OPTION) {
                if (countdownTimer != null) countdownTimer.stop(); // Stop timer on success
                updateReceipt();
                layout.show(container, "receipt");
            }
        });

        panel.add(centerContent, BorderLayout.CENTER);
        panel.add(confirm, BorderLayout.SOUTH);

        // Start timer only when this panel is shown
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent e) {
                if (container.getComponent(3).isVisible()) startTimer();
            }
        });

        return panel;
    }

    // ================= REMAINING PANELS (STYLED) =================
    private JPanel summaryPanel() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(18, 18, 18));
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(18, 18, 18));
        content.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        content.add(createValueLabel("Review Summary", 22, Color.WHITE));
        content.add(Box.createVerticalStrut(20));
        content.add(createRow("Movie", order.getMovieTitle()));
        content.add(createRow("Cinema", order.getCinemaLocation()));
        content.add(createRow("Seats", String.join(", ", order.getSeats())));
        content.add(Box.createVerticalStrut(15));
        content.add(new JSeparator());
        content.add(Box.createVerticalStrut(15));
        double ticketOnly = order.getSubtotal() - order.getFoodTotal();
        content.add(createRow("Tickets Total", "RM " + String.format("%.2f", ticketOnly)));
        content.add(createRow("F&B Total", "RM " + String.format("%.2f", order.getFoodTotal())));
        content.add(Box.createVerticalStrut(20));
        content.add(createTotalRow("Total Price", "RM " + String.format("%.2f", order.getSubtotal())));

        JButton next = new JButton("Next Step");
        styleButton(next, new Color(255, 204, 0), Color.BLACK);
        next.addActionListener(e -> layout.show(container, "discount"));
        main.add(content, BorderLayout.CENTER);
        main.add(next, BorderLayout.SOUTH);
        return main;
    }

    private JPanel paymentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(18, 18, 18));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0; panel.add(createValueLabel("Select Payment", 20, Color.WHITE), gbc);

        JButton tng = new JButton("TNG eWallet");
        styleButton(tng, new Color(0, 122, 255), Color.WHITE);
        tng.addActionListener(e -> { paymentMethod = "TNG eWallet"; startTimer(); layout.show(container, "qr"); });
        gbc.gridy = 1; panel.add(tng, gbc);

        JButton duitnow = new JButton("DuitNow QR");
        styleButton(duitnow, new Color(237, 27, 36), Color.WHITE);
        duitnow.addActionListener(e -> { paymentMethod = "DuitNow"; startTimer(); layout.show(container, "qr"); });
        gbc.gridy = 2; panel.add(duitnow, gbc);

        return panel;
    }

    private JPanel receiptPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(18, 18, 18));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setBackground(new Color(240, 240, 240));
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        receiptArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton done = new JButton("Finish");
        styleButton(done, Color.DARK_GRAY, Color.WHITE);

        done.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Booking Successful! Enjoy your movie.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
        panel.add(new JScrollPane(receiptArea), BorderLayout.CENTER);
        panel.add(done, BorderLayout.SOUTH);
        return panel;
    }

    private void updateReceipt() {
        String dt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        // Join the list ["A1", "A2"] into "A1, A2"
        String seatDisplay = String.join(", ", order.getSeats());

        receiptArea.setText("      CINEMA RECEIPT\n" +
                "--------------------------\n" +
                "Date: " + dt + "\n" +
                "Movie: " + order.getMovieTitle() + "\n" +
                "Seats: " + seatDisplay + "\n" +
                "--------------------------\n" +
                "Paid: RM " + String.format("%.2f", finalAmount) + "\n" +
                "Method: " + paymentMethod + "\n" +
                "--------------------------\n" +
                "    THANK YOU FOR BUYING");
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg); btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private JLabel createValueLabel(String text, int size, Color color) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setForeground(color); l.setFont(new Font("Segoe UI", Font.BOLD, size));
        return l;
    }

    private JPanel createRow(String left, String right) {
        JPanel p = new JPanel(new BorderLayout()); p.setBackground(new Color(18, 18, 18));
        JLabel l = new JLabel(left); l.setForeground(Color.GRAY);
        JLabel r = new JLabel(right); r.setForeground(Color.WHITE);
        p.add(l, BorderLayout.WEST); p.add(r, BorderLayout.EAST);
        return p;
    }

    private JPanel createTotalRow(String left, String right) {
        JPanel p = new JPanel(new BorderLayout()); p.setBackground(new Color(18, 18, 18));
        JLabel l = new JLabel(left); l.setForeground(new Color(255, 204, 0));
        l.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JLabel r = new JLabel(right); r.setForeground(new Color(255, 204, 0));
        r.setFont(new Font("Segoe UI", Font.BOLD, 18));
        p.add(l, BorderLayout.WEST); p.add(r, BorderLayout.EAST);
        return p;
    }
}