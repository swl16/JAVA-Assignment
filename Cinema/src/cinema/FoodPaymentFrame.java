package cinema;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FoodPaymentFrame extends JFrame {
    private JFrame parentFrame;
    private ArrayList<Item> basketItems;
    private double totalAmount;
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private JTextArea receiptArea;
    private String paymentMethod = "";

    // --- Premium Cinema Color Palette ---
    private final Color obsidian = new Color(0x121212); // Deeper black
    private final Color surface = new Color(0x1E1E1E);  // Elevates panels
    private final Color gold = new Color(0xFFD700);     // For totals/accents
    private final Color tgcRed = new Color(0xD44444);   // Your brand red
    private final Color softGray = new Color(0xAAAAAA);
    private final Color offWhite = new Color(0xF7F7F7);

    public FoodPaymentFrame(JFrame parentFrame, ArrayList<Item> basketItems, double totalAmount) {
        this.parentFrame = parentFrame;
        this.basketItems = basketItems;
        this.totalAmount = totalAmount;

        setTitle("TGC Cinema - Premium Checkout");
        setSize(460, 780);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(obsidian);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setOpaque(false);

        mainContainer.add(methodSelectionPanel(), "METHODS");
        mainContainer.add(qrPaymentPanel(), "QR");
        mainContainer.add(cardPaymentPanel(), "CARD");
        mainContainer.add(receiptPanel(), "RECEIPT");

        add(mainContainer);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // --- VIEW 1: Selection Menu ---
    private JPanel methodSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(obsidian);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("SECURE CHECKOUT", SwingConstants.CENTER);
        title.setForeground(offWhite);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        gbc.gridy = 0; panel.add(title, gbc);

        // Visual Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(0x333333));
        gbc.gridy = 1; panel.add(sep, gbc);

        JButton cardBtn = createMenuButton("CREDIT / DEBIT CARD", "Secure Encryption");
        cardBtn.addActionListener(e -> { paymentMethod = "Credit/Debit Card"; cardLayout.show(mainContainer, "CARD"); });
        gbc.gridy = 2; panel.add(cardBtn, gbc);

        JButton qrBtn = createMenuButton("E-WALLET QR", "TNG / DuitNow / Grab");
        qrBtn.addActionListener(e -> { paymentMethod = "Mobile QR"; cardLayout.show(mainContainer, "QR"); });
        gbc.gridy = 3; panel.add(qrBtn, gbc);

        JLabel totalLbl = new JLabel("TOTAL PAYABLE: RM " + String.format("%.2f", totalAmount), SwingConstants.CENTER);
        totalLbl.setForeground(gold);
        totalLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridy = 4; gbc.insets = new Insets(30, 0, 0, 0);
        panel.add(totalLbl, gbc);

        return panel;
    }

    // --- VIEW 2: Premium Card Input ---
    private JPanel cardPaymentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(obsidian);
        panel.setBorder(new EmptyBorder(30, 45, 30, 45));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("CARD DETAILS", SwingConstants.CENTER);
        title.setForeground(offWhite);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridy = 0; panel.add(title, gbc);

        gbc.gridy++; panel.add(createInputLabel("CARDHOLDER NAME"), gbc);
        JTextField holder = createStyledTextField("John Doe");
        gbc.gridy++; panel.add(holder, gbc);

        gbc.gridy++; panel.add(createInputLabel("CARD NUMBER"), gbc);
        JTextField cardNum = createStyledTextField("0000 0000 0000 0000");
        gbc.gridy++; panel.add(cardNum, gbc);

        JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
        row.setOpaque(false);
        row.add(createFieldGroup("EXPIRY", "MM/YY"));
        row.add(createFieldGroup("CVC", "123"));
        gbc.gridy++; panel.add(row, gbc);

        JButton submit = createActionBtn("AUTHORIZE PAYMENT");
        submit.addActionListener(e -> processSuccess());
        gbc.gridy++; gbc.insets = new Insets(30, 0, 10, 0);
        panel.add(submit, gbc);

        return panel;
    }

    // --- VIEW 3: QR Payment View ---
    private JPanel qrPaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 25));
        panel.setBackground(obsidian);
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JLabel qrTitle = new JLabel("SCAN TO PAY", SwingConstants.CENTER);
        qrTitle.setForeground(offWhite);
        qrTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(qrTitle, BorderLayout.NORTH);

        JPanel qrBox = new JPanel(new BorderLayout());
        qrBox.setBackground(Color.WHITE);
        qrBox.setBorder(new LineBorder(tgcRed, 5));

        try {
            ImageIcon rawIcon = new ImageIcon("Cinema/src/cinema/qr_code.png");
            Image scaledImg = rawIcon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
            qrBox.add(new JLabel(new ImageIcon(scaledImg)), BorderLayout.CENTER);
        } catch (Exception e) {
            qrBox.add(new JLabel("QR ERROR", SwingConstants.CENTER), BorderLayout.CENTER);
        }
        panel.add(qrBox, BorderLayout.CENTER);

        JButton paid = createActionBtn("I HAVE PAID");
        paid.addActionListener(e -> processSuccess());
        panel.add(paid, BorderLayout.SOUTH);

        return panel;
    }

    // --- VIEW 4: Professional Receipt ---
    private JPanel receiptPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(obsidian);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel ticket = new JPanel(new BorderLayout());
        ticket.setBackground(offWhite);
        ticket.setBorder(new LineBorder(new Color(0xDDDDDD), 1));

        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptArea.setBackground(offWhite);
        receiptArea.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel footer = new JPanel(new GridBagLayout());
        footer.setBackground(Color.WHITE);
        footer.setPreferredSize(new Dimension(0, 180));
        footer.setBorder(new MatteBorder(2, 0, 0, 0, new Color(0xEEEEEE)));

        JPanel qr = new JPanel(new BorderLayout());
        qr.setPreferredSize(new Dimension(100, 100));
        qr.setBorder(new LineBorder(Color.BLACK, 2));
        try {
            ImageIcon raw = new ImageIcon("Cinema/src/cinema/entry_qr.png");
            qr.add(new JLabel(new ImageIcon(raw.getImage().getScaledInstance(90, 90, 1))), 0);
        } catch(Exception e) { qr.add(new JLabel("QR")); }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0; footer.add(qr, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(10,0,0,0);
        footer.add(new JLabel("<html><center><b>REDEMPTION QR</b><br>Show at counter</center></html>"), gbc);

        ticket.add(receiptArea, BorderLayout.CENTER);
        ticket.add(footer, BorderLayout.SOUTH);

        JButton done = createActionBtn("FINISH & RETURN");
        done.addActionListener(e -> { dispose(); parentFrame.dispose(); });

        panel.add(ticket, BorderLayout.CENTER);
        panel.add(done, BorderLayout.SOUTH);
        return panel;
    }

    private void processSuccess() {
        updateReceipt();
        cardLayout.show(mainContainer, "RECEIPT");
    }

    private void updateReceipt() {
        String dt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        StringBuilder items = new StringBuilder();
        for (Item i : basketItems) items.append(String.format(" %-20s x%d\n", i.name, i.quantity));

        receiptArea.setText(
                "\n         TGC CINEMA CONCESSION\n" +
                        "------------------------------------------\n" +
                        " DATE: " + dt + "\n" +
                        " ID:   " + System.currentTimeMillis() + "\n" +
                        "------------------------------------------\n" +
                        " ITEMS:\n" + items.toString() +
                        "------------------------------------------\n" +
                        " TOTAL PAID: RM " + String.format("%.2f", totalAmount) + "\n" +
                        " METHOD: " + paymentMethod + "\n" +
                        "------------------------------------------\n" +
                        "   KEEP THIS QR FOR FOOD COLLECTION"
        );
    }

    // --- CUSTOM UI HELPERS ---
    private JButton createMenuButton(String main, String sub) {
        JButton b = new JButton("<html><center><b>" + main + "</b><br><font size='3' color='#888888'>" + sub + "</font></center></html>");
        b.setBackground(surface);
        b.setForeground(offWhite);
        b.setFocusPainted(false);
        b.setBorder(new LineBorder(new Color(0x333333), 1));
        b.setPreferredSize(new Dimension(320, 70));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton createActionBtn(String text) {
        JButton b = new JButton(text);
        b.setBackground(tgcRed);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(0, 50));
        b.setBorder(null);
        return b;
    }

    private JTextField createStyledTextField(String p) {
        JTextField f = new JTextField(p);
        f.setBackground(surface);
        f.setForeground(softGray);
        f.setCaretColor(Color.WHITE);
        f.setPreferredSize(new Dimension(0, 45));
        f.setBorder(new CompoundBorder(new LineBorder(new Color(0x333333)), new EmptyBorder(0, 15, 0, 15)));
        return f;
    }

    private JPanel createFieldGroup(String l, String p) {
        JPanel g = new JPanel(new BorderLayout(0, 5));
        g.setOpaque(false);
        g.add(createInputLabel(l), BorderLayout.NORTH);
        g.add(createStyledTextField(p), BorderLayout.CENTER);
        return g;
    }

    private JLabel createInputLabel(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(softGray);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        return l;
    }
}