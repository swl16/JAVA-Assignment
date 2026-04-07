package cinema;

import javax.swing.*;
import javax.swing.border.*;
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

    // --- Premium Cinema Color Palette ---
    private final Color obsidian = new Color(0x121212);
    private final Color surface = new Color(0x1E1E1E);
    private final Color gold = new Color(0xFFD700);
    private final Color tgcRed = new Color(0xD44444);
    private final Color softGray = new Color(0xAAAAAA);
    private final Color offWhite = new Color(0xF7F7F7);

    public PaymentFrame(Order order) {
        this.order = order;
        this.finalAmount = order.getSubtotal();

        layout = new CardLayout();
        container = new JPanel(layout);
        container.setOpaque(false);

        container.add(summaryPanel(), "summary");
        container.add(paymentPanel(), "payment");
        container.add(qrPanel(), "qr");
        container.add(cardPanel(), "card_input");
        container.add(receiptPanel(), "receipt");

        add(container);

        setTitle("TGC Cinema - Premium Payment");
        setSize(460, 750);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(obsidian);
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
        main.setBackground(obsidian);
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));

        content.add(createHeaderLabel("BOOKING SUMMARY", 24));
        content.add(Box.createVerticalStrut(25));
        content.add(createRow("Movie", order.getMovieTitle().toUpperCase()));
        content.add(createRow("Cinema", order.getCinemaLocation()));
        content.add(createRow("Seats", String.join(", ", order.getSeats())));
        content.add(Box.createVerticalStrut(20));
        content.add(new JSeparator(JSeparator.HORIZONTAL));
        content.add(Box.createVerticalStrut(20));

        double ticketOnly = order.getSubtotal() - order.getFoodTotal();
        content.add(createRow("Tickets Total", "RM " + String.format("%.2f", ticketOnly)));
        content.add(createRow("F&B Total", "RM " + String.format("%.2f", order.getFoodTotal())));
        content.add(Box.createVerticalStrut(25));
        content.add(createTotalRow("GRAND TOTAL", "RM " + String.format("%.2f", order.getSubtotal())));

        JButton next = createActionBtn("PROCEED TO PAYMENT");
        next.addActionListener(e -> layout.show(container, "payment"));

        main.add(content, BorderLayout.CENTER);
        main.add(next, BorderLayout.SOUTH);
        return main;
    }

    private JPanel paymentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(obsidian);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0; panel.add(createHeaderLabel("SELECT PAYMENT", 22), gbc);

        JButton creditBtn = createMenuButton("CREDIT / DEBIT CARD", "Visa, Mastercard, AMEX");
        creditBtn.addActionListener(e -> layout.show(container, "card_input"));
        gbc.gridy = 1; panel.add(creditBtn, gbc);

        JButton tng = createMenuButton("MOBILE WALLET QR", "TNG / DuitNow / Grab");
        tng.addActionListener(e -> {
            paymentMethod = "Mobile Wallet QR";
            startTimer();
            layout.show(container, "qr");
        });
        gbc.gridy = 2; panel.add(tng, gbc);

        return panel;
    }

    private JPanel cardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(obsidian);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 45, 30, 45));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0; panel.add(createHeaderLabel("CARD DETAILS", 22), gbc);

        gbc.gridy++; panel.add(createInputLabel("CARDHOLDER NAME"), gbc);
        JTextField holder = createStyledTextField("Full Name");
        gbc.gridy++; panel.add(holder, gbc);

        gbc.gridy++; panel.add(createInputLabel("CARD NUMBER"), gbc);
        JTextField cardNum = createStyledTextField("••••  ••••  ••••  ••••");
        gbc.gridy++; panel.add(cardNum, gbc);

        JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
        row.setOpaque(false);
        row.add(createFieldGroup("EXPIRY (MM/YY)", "MM / YY"));
        row.add(createFieldGroup("CVC", "•••"));
        gbc.gridy++; panel.add(row, gbc);

        JButton payBtn = createActionBtn("AUTHORIZE PAYMENT");
        payBtn.addActionListener(e -> {
            if (cardNum.getText().trim().isEmpty() || cardNum.getText().contains("•")) {
                JOptionPane.showMessageDialog(this, "Please enter valid card details.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            paymentMethod = "Credit/Debit Card";
            updateReceipt();
            layout.show(container, "receipt");
        });
        gbc.gridy++; gbc.insets = new Insets(30, 0, 10, 0);
        panel.add(payBtn, gbc);

        return panel;
    }

    private JPanel qrPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 25));
        panel.setBackground(obsidian);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel qrTitle = createHeaderLabel("SCAN TO PAY", 24);
        panel.add(qrTitle, BorderLayout.NORTH);

        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));
        centerContainer.setOpaque(false);

        qrAmountLabel = new JLabel("TOTAL: RM " + String.format("%.2f", finalAmount), SwingConstants.CENTER);
        qrAmountLabel.setForeground(gold);
        qrAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        qrAmountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel qrBox = new JPanel(new BorderLayout());
        qrBox.setPreferredSize(new Dimension(220, 220));
        qrBox.setMaximumSize(new Dimension(220, 220));
        qrBox.setBackground(Color.WHITE);
        qrBox.setBorder(new LineBorder(tgcRed, 5));

        try {
            ImageIcon rawIcon = new ImageIcon("Cinema/src/cinema/qr_code.png");
            Image scaledImg = rawIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            qrBox.add(new JLabel(new ImageIcon(scaledImg)), BorderLayout.CENTER);
        } catch (Exception e) {
            qrBox.add(new JLabel("QR ERROR", SwingConstants.CENTER), BorderLayout.CENTER);
        }
        qrBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        timerLabel = new JLabel("Expires in 05:00", SwingConstants.CENTER);
        timerLabel.setForeground(softGray);
        timerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerContainer.add(qrAmountLabel);
        centerContainer.add(Box.createVerticalStrut(25));
        centerContainer.add(qrBox);
        centerContainer.add(Box.createVerticalStrut(15));
        centerContainer.add(timerLabel);

        panel.add(centerContainer, BorderLayout.CENTER);

        JButton confirm = createActionBtn("I HAVE PAID");
        confirm.addActionListener(e -> {
            if (countdownTimer != null) countdownTimer.stop();
            updateReceipt();
            layout.show(container, "receipt");
        });
        panel.add(confirm, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel receiptPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(obsidian);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 35, 20, 35));

        // Ticket Body
        JPanel ticket = new JPanel(new BorderLayout());
        ticket.setBackground(offWhite);
        ticket.setBorder(new LineBorder(new Color(0xDDDDDD), 1));

        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptArea.setBackground(offWhite);
        receiptArea.setBorder(new EmptyBorder(25, 25, 20, 25));

        // Redemption Stub
        JPanel stub = new JPanel(new GridBagLayout());
        stub.setBackground(Color.WHITE);
        stub.setPreferredSize(new Dimension(0, 180));
        stub.setBorder(new MatteBorder(2, 0, 0, 0, new Color(0xEEEEEE)));

        JPanel qrBox = new JPanel(new BorderLayout());
        qrBox.setPreferredSize(new Dimension(100, 100));
        qrBox.setBackground(Color.WHITE);
        qrBox.setBorder(new LineBorder(Color.BLACK, 2));

        try {
            ImageIcon rawEntry = new ImageIcon("Cinema/src/cinema/entry_qr.png");
            Image scaledEntry = rawEntry.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
            qrBox.add(new JLabel(new ImageIcon(scaledEntry)), BorderLayout.CENTER);
        } catch (Exception e) {
            qrBox.add(new JLabel("ENTRY", SwingConstants.CENTER), BorderLayout.CENTER);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0; stub.add(qrBox, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(10, 0, 0, 0);
        stub.add(new JLabel("<html><center><b>REDEMPTION QR</b><br>Scan for Entry & Food</center></html>"), gbc);

        ticket.add(receiptArea, BorderLayout.CENTER);
        ticket.add(stub, BorderLayout.SOUTH);

        JButton done = createActionBtn("FINISH & RETURN");
        done.addActionListener(e -> dispose());

        panel.add(ticket, BorderLayout.CENTER);
        panel.add(done, BorderLayout.SOUTH);
        return panel;
    }

    private void updateReceipt() {
        String dt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        String seatDisplay = String.join(", ", order.getSeats());

        receiptArea.setText(
                "        TGC CINEMA OFFICIAL E-TICKET\n" +
                        "==========================================\n" +
                        " ID:       " + System.currentTimeMillis() + "\n" +
                        " DATE:     " + dt + "\n" +
                        " LOCATION: " + order.getCinemaLocation() + "\n" +
                        "------------------------------------------\n" +
                        " MOVIE:    " + order.getMovieTitle().toUpperCase() + "\n" +
                        " SEATS:    " + seatDisplay + "\n" +
                        "------------------------------------------\n" +
                        " TICKETS:  RM " + String.format("%.2f", order.getSubtotal() - order.getFoodTotal()) + "\n" +
                        " F&B:      RM " + String.format("%.2f", order.getFoodTotal()) + "\n" +
                        " TOTAL:    RM " + String.format("%.2f", finalAmount) + "\n" +
                        " METHOD:   " + paymentMethod + "\n" +
                        "==========================================\n" +
                        "     ENJOY YOUR CINEMATIC EXPERIENCE!"
        );
    }

    // --- UI HELPERS (PREMIUM STYLE) ---
    private JLabel createHeaderLabel(String text, int size) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setForeground(offWhite);
        l.setFont(new Font("Segoe UI", Font.BOLD, size));
        return l;
    }

    private JButton createMenuButton(String main, String sub) {
        JButton b = new JButton("<html><center><b>" + main + "</b><br><font size='3' color='#888888'>" + sub + "</font></center></html>");
        b.setBackground(surface);
        b.setForeground(offWhite);
        b.setFocusPainted(false);
        b.setBorder(new LineBorder(new Color(0x333333), 1));
        b.setPreferredSize(new Dimension(340, 75));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton createActionBtn(String text) {
        JButton b = new JButton(text);
        b.setBackground(tgcRed);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(0, 55));
        b.setBorder(null);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField f = new JTextField(placeholder);
        f.setBackground(surface);
        f.setForeground(softGray);
        f.setCaretColor(Color.WHITE);
        f.setPreferredSize(new Dimension(0, 45));
        f.setBorder(new CompoundBorder(new LineBorder(new Color(0x333333)), new EmptyBorder(0, 15, 0, 15)));
        f.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (f.getText().equals(placeholder)) {
                    f.setText("");
                    f.setForeground(Color.WHITE);
                }
            }
        });
        return f;
    }

    private JPanel createFieldGroup(String label, String placeholder) {
        JPanel g = new JPanel(new BorderLayout(0, 5));
        g.setOpaque(false);
        g.add(createInputLabel(label), BorderLayout.NORTH);
        g.add(createStyledTextField(placeholder), BorderLayout.CENTER);
        return g;
    }

    private JLabel createInputLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(softGray);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        return l;
    }

    private JPanel createRow(String left, String right) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel l = new JLabel(left); l.setForeground(softGray);
        JLabel r = new JLabel(right); r.setForeground(offWhite);
        p.add(l, BorderLayout.WEST); p.add(r, BorderLayout.EAST);
        return p;
    }

    private JPanel createTotalRow(String left, String right) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel l = new JLabel(left); l.setForeground(gold);
        l.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JLabel r = new JLabel(right); r.setForeground(gold);
        r.setFont(new Font("Segoe UI", Font.BOLD, 18));
        p.add(l, BorderLayout.WEST); p.add(r, BorderLayout.EAST);
        return p;
    }
}