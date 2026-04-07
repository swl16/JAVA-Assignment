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

    private double finalAmount = 0;
    private String paymentMethod = "";

    private JLabel qrAmountLabel;
    private JLabel timerLabel;
    private JTextArea receiptArea;
    private Timer countdownTimer;
    private int secondsRemaining = 300;

    public PaymentFrame(Order order) {
        this.order = order;
        this.finalAmount = order.getSubtotal();

        layout = new CardLayout();
        container = new JPanel(layout);

        container.add(summaryPanel(), "summary");
        container.add(paymentPanel(), "payment");
        container.add(qrPanel(), "qr");
        container.add(cardPanel(), "card_input");
        container.add(receiptPanel(), "receipt");

        add(container);

        setTitle("TGC Cinema - Payment");
        setSize(420, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        layout.show(container, "summary");
    }

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
                JOptionPane.showMessageDialog(this, "Session expired. Returning to payment selection.", "Timed Out", JOptionPane.ERROR_MESSAGE);
                layout.show(container, "payment");
            }
        });
        countdownTimer.start();
    }

    private JPanel summaryPanel() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(18, 18, 18));
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(18, 18, 18));
        content.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        content.add(createLabel("Booking Summary", 22, Color.WHITE));
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
        content.add(createTotalRow("Grand Total", "RM " + String.format("%.2f", order.getSubtotal())));

        JButton next = new JButton("Proceed to payment");
        styleButton(next, new Color(212, 68, 68), Color.WHITE);
        next.addActionListener(e -> layout.show(container, "payment"));

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

        gbc.gridy = 0; panel.add(createLabel("Select Payment", 20, Color.WHITE), gbc);

        JButton creditBtn = new JButton("Pay by Credit / Debit Card");
        styleButton(creditBtn, new Color(60, 60, 60), Color.WHITE);
        creditBtn.addActionListener(e -> layout.show(container, "card_input"));
        gbc.gridy = 1; panel.add(creditBtn, gbc);

        JButton tng = new JButton("Pay by QR (TNG / DuitNow)");
        styleButton(tng, new Color(0, 122, 255), Color.WHITE);
        tng.addActionListener(e -> {
            paymentMethod = "Mobile Wallet QR";
            startTimer();
            layout.show(container, "qr");
        });
        gbc.gridy = 2; panel.add(tng, gbc);

        return panel;
    }

    private JPanel cardPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBackground(new Color(18, 18, 18));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        panel.add(createLabel("Card Details", 20, Color.WHITE));
        JTextField cardNum = new JTextField("Card Number (16-digits)");
        JTextField expiry = new JTextField("MM/YY");
        JTextField cvv = new JTextField("CVV");

        JButton payBtn = new JButton("Pay RM " + String.format("%.2f", finalAmount));
        styleButton(payBtn, new Color(212, 68, 68), Color.WHITE);

        payBtn.addActionListener(e -> {
            if (cardNum.getText().trim().isEmpty() || cardNum.getText().equals("Card Number (16-digits)")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid card number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            paymentMethod = "Credit/Debit Card";
            updateReceipt();
            layout.show(container, "receipt");
        });

        panel.add(cardNum); panel.add(expiry); panel.add(cvv); panel.add(payBtn);
        return panel;
    }

    private JPanel qrPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(18, 18, 18));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel centerContent = new JPanel();
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));
        centerContent.setBackground(new Color(18, 18, 18));

        qrAmountLabel = createLabel("Amount: RM " + String.format("%.2f", finalAmount), 20, Color.WHITE);
        qrAmountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // QR Image Box
        JPanel qrBox = new JPanel(new BorderLayout());
        qrBox.setPreferredSize(new Dimension(200, 200));
        qrBox.setMaximumSize(new Dimension(200, 200));
        qrBox.setBackground(Color.WHITE);
        qrBox.setBorder(new LineBorder(new Color(255, 204, 0), 4));

        try {
            ImageIcon rawIcon = new ImageIcon("Cinema/src/cinema/qr_code.png");
            Image scaledImg = rawIcon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            qrBox.add(new JLabel(new ImageIcon(scaledImg)), BorderLayout.CENTER);
        } catch (Exception e) {
            qrBox.add(new JLabel("SCAN TO PAY", SwingConstants.CENTER), BorderLayout.CENTER);
        }
        qrBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        timerLabel = new JLabel("Expires in 05:00", SwingConstants.CENTER);
        timerLabel.setForeground(Color.LIGHT_GRAY);
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerContent.add(qrAmountLabel);
        centerContent.add(Box.createVerticalStrut(20));
        centerContent.add(qrBox);
        centerContent.add(Box.createVerticalStrut(15));
        centerContent.add(timerLabel);

        JButton confirm = new JButton("I have paid");
        styleButton(confirm, new Color(212, 68, 68), Color.WHITE);
        confirm.addActionListener(e -> {
            if (countdownTimer != null) countdownTimer.stop();
            updateReceipt();
            layout.show(container, "receipt");
        });

        panel.add(centerContent, BorderLayout.CENTER);
        panel.add(confirm, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel receiptPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setBackground(new Color(245, 245, 245));
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // Redemption Section with Entry QR Image
        JPanel redemptionPanel = new JPanel(new GridBagLayout());
        redemptionPanel.setBackground(Color.WHITE);
        redemptionPanel.setPreferredSize(new Dimension(420, 160));

        JPanel entryQR = new JPanel(new BorderLayout());
        entryQR.setPreferredSize(new Dimension(100, 100));
        entryQR.setBackground(Color.WHITE);
        entryQR.setBorder(new LineBorder(Color.BLACK, 2));

        try {
            ImageIcon rawEntry = new ImageIcon("Cinema/src/cinema/entry_qr.png");
            Image scaledEntry = rawEntry.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
            entryQR.add(new JLabel(new ImageIcon(scaledEntry)), BorderLayout.CENTER);
        } catch (Exception e) {
            entryQR.add(new JLabel("ENTRY", SwingConstants.CENTER), BorderLayout.CENTER);
        }

        JLabel instruct = new JLabel("<html><center>Scan for Hall Entry &<br>Food Redemption</center></html>");
        instruct.setFont(new Font("Segoe UI", Font.BOLD, 12));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; redemptionPanel.add(entryQR, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(5, 0, 0, 0); redemptionPanel.add(instruct, gbc);

        JButton done = new JButton("Save & Close");
        styleButton(done, new Color(50, 50, 50), Color.WHITE);
        done.addActionListener(e -> dispose());

        panel.add(new JScrollPane(receiptArea), BorderLayout.CENTER);
        panel.add(redemptionPanel, BorderLayout.SOUTH);
        panel.add(done, BorderLayout.NORTH);
        return panel;
    }

    private void updateReceipt() {
        String dt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        String seatDisplay = String.join(", ", order.getSeats());

        receiptArea.setText("\n" +
                "         TGC CINEMA OFFICIAL RECEIPT\n" +
                "==========================================\n" +
                " TRANSACTION ID: " + System.currentTimeMillis() + "\n" +
                " DATE/TIME:    " + dt + "\n" +
                " MOVIE:        " + order.getMovieTitle() + "\n" +
                " LOCATION:     " + order.getCinemaLocation() + "\n" +
                " SEATS:        " + seatDisplay + "\n" +
                "==========================================\n" +
                " TICKETS:      RM " + String.format("%.2f", order.getSubtotal() - order.getFoodTotal()) + "\n" +
                " CONCESSIONS:  RM " + String.format("%.2f", order.getFoodTotal()) + "\n" +
                " TOTAL PAID:   RM " + String.format("%.2f", finalAmount) + "\n" +
                " METHOD:       " + paymentMethod + "\n" +
                "==========================================\n" +
                "      ENJOY YOUR CINEMATIC EXPERIENCE!");
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg); btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
    }

    private JLabel createLabel(String text, int size, Color color) {
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