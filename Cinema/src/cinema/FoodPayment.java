package cinema;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.io.*;

public class FoodPayment{
    private JFrame frame = new JFrame();
    
    private final ArrayList<PaymentItem> basketItems;
    private double totalAmount;
    private final CardLayout cardLayout;
    private final JPanel mainContainer;
    private JTextArea receiptArea;
    private String paymentMethod = "";

    // --- Premium Cinema Color Palette ---
    private final Color bg = new Color(0x242424); // Deeper black
    private final Color panel = new Color(0x3B3B3B);  // Elevates panels
    private final Color gold = new Color(0xFFD700);     // For totals/accents
    private final Color tgcRed = new Color(0xD44444);   // Your brand red
    private final Color softGray = new Color(0xAAAAAA);
    private final Color text = new Color(0xF7F7F7);
    
    private final String username, cinema, time;
    private final String orderId;

    public FoodPayment(Map<String, Integer> basket, Map<String, Double> prices,
                   String cinema, String time, String username) {
        
        this.username = username;
        this.cinema   = cinema;
        this.time     = time;
        this.orderId  = generateOrderId();
 
        double total = 0.0;
        
        this.basketItems = new ArrayList<>();
        
        for (String name : basket.keySet()) {
            int qty = basket.get(name);
            double price = prices.get(name);

            basketItems.add(new PaymentItem(name, qty, price));
            total += qty * price;
        }
        this.totalAmount = total;


        frame.setTitle("TGC Cinema - Checkout");
        frame.setSize(500, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(bg);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setOpaque(false);
        
        mainContainer.setOpaque(false);
        mainContainer.add(methodSelectionPanel(), "METHODS");
        mainContainer.add(cardPaymentPanel(), "CARD");
        mainContainer.add(qrPaymentPanel(), "QR");
        mainContainer.add(processingPanel(), "PROCESSING");
        mainContainer.add(receiptPanel(), "RECEIPT");

        frame.add(mainContainer);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // --- VIEW 1: Selection Menu ---
    private JPanel methodSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bg);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("SECURE CHECKOUT", SwingConstants.CENTER);
        title.setForeground(text);
        title.setFont(new Font("Courier New", Font.BOLD, 26));
        gbc.gridy = 0; 
        panel.add(title, gbc);
        
        JLabel orderLbl = new JLabel("Order: " + orderId, SwingConstants.CENTER);
        orderLbl.setForeground(softGray);
        orderLbl.setFont(new Font("Courier New", Font.PLAIN, 12));
        gbc.gridy = 1;
        panel.add(orderLbl, gbc);

        // Visual Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(0x333333));
        gbc.gridy = 2; 
        panel.add(sep, gbc);

        JButton cardBtn = createMenuButton("CREDIT / DEBIT CARD", "Visa / Mastercard");
        cardBtn.addActionListener(e -> { paymentMethod = "Credit/Debit Card"; cardLayout.show(mainContainer, "CARD"); });
        gbc.gridy = 3; 
        panel.add(cardBtn, gbc);

        JButton qrBtn = createMenuButton("E-WALLET QR", "TNG / DuitNow / Grab");
        qrBtn.addActionListener(e -> { paymentMethod = "Mobile QR"; cardLayout.show(mainContainer, "QR"); });
        gbc.gridy = 4; 
        panel.add(qrBtn, gbc);

        JLabel totalLbl = new JLabel("TOTAL PAYABLE: RM " + String.format("%.2f", totalAmount), SwingConstants.CENTER);
        totalLbl.setForeground(text);
        totalLbl.setFont(new Font("Courier New", Font.BOLD, 18));
        gbc.gridy = 5; 
        gbc.insets = new Insets(30, 0, 0, 0);
        panel.add(totalLbl, gbc);

        return panel;
    }

    // --- VIEW 2: Premium Card Input ---
    private JPanel cardPaymentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bg);
        panel.setBorder(new EmptyBorder(30, 45, 30, 45));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 4, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("CARD DETAILS", SwingConstants.CENTER);
        title.setForeground(text);
        title.setFont(new Font("Courier New", Font.BOLD, 25));
        gbc.gridy = 0; 
        panel.add(title, gbc);

        gbc.gridy++; panel.add(createInputLabel("CARDHOLDER NAME"), gbc);
        JTextField holder = createStyledTextField("");
        gbc.gridy++; 
        panel.add(holder, gbc);
        JLabel holderErr = createErrorLabel();
        gbc.gridy++;
        panel.add(holderErr, gbc);

        gbc.gridy++; panel.add(createInputLabel("CARD NUMBER"), gbc);
        JTextField cardNum = createStyledTextField("");
        gbc.gridy++; 
        panel.add(cardNum, gbc);
        JLabel cardErr = createErrorLabel();
        gbc.gridy++;
        panel.add(cardErr, gbc);

        JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
        row.setOpaque(false);
        JTextField expiry = createStyledTextField("MM/YY");
        JTextField cvv    = createStyledTextField("");
        row.add(wrapFieldGroup("EXPIRY", expiry));
        row.add(wrapFieldGroup("CVV",    cvv));
        gbc.gridy++; 
        panel.add(row, gbc);
        
        JLabel expiryErr = createErrorLabel();
        gbc.gridy++;
        panel.add(expiryErr, gbc);

        JButton submit = createActionBtn("AUTHORIZE PAYMENT");
        submit.addActionListener(e -> {
            if (validateCard(holder, cardNum, expiry, cvv,
                             holderErr, cardErr, expiryErr)) {
                simulateProcessing();
            }
        });
        gbc.gridy++; gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(submit, gbc);
        
        JButton back = createBackBtn();
        back.addActionListener(e -> cardLayout.show(mainContainer, "METHODS"));
        gbc.gridy++;
        gbc.insets = new Insets(4, 0, 0, 0);
        panel.add(back, gbc);

        return panel;
    }
    
    private boolean validateCard(JTextField holder, JTextField cardNum,
                                 JTextField expiry, JTextField cvv,
                                 JLabel holderErr, JLabel cardErr,
                                 JLabel expiryErr) {
        boolean valid = true;
 
        // Name
        String name = holder.getText().trim();
        if (name.isEmpty() || name.length() < 3) {
            holderErr.setText("Enter a valid cardholder name.");
            valid = false;
        } else {
            holderErr.setText("");
        }
 
        // Card number — strip spaces, must be 16 digits
        String digits = cardNum.getText().replaceAll("\\s", "");
        if (!digits.matches("\\d{16}")) {
            cardErr.setText("Card number must be 16 digits.");
            valid = false;
        } else {
            cardErr.setText("");
        }
 
        // Expiry — MM/YY format, must not be in the past
        String exp = expiry.getText().trim();
        boolean expiryOk = false;
        if (exp.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            int month = Integer.parseInt(exp.substring(0, 2));
            int year  = 2000 + Integer.parseInt(exp.substring(3, 5));
            LocalDate expDate  = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);
            if (!expDate.isBefore(LocalDate.now())) {
                expiryOk = true;
            }
        }
 
        // CVV — 3 or 4 digits
        String cvvStr = cvv.getText().trim();
        boolean cvvOk = cvvStr.matches("\\d{3}");
 
        if (!expiryOk || !cvvOk) {
            StringBuilder msg = new StringBuilder("");
            if (!expiryOk) msg.append("Invalid/expired expiry. ");
            if (!cvvOk)    msg.append("CVV must be 3 digits.");
            expiryErr.setText(msg.toString());
            valid = false;
        } else {
            expiryErr.setText("");
        }
 
        return valid;
    }

    // --- VIEW 3: QR Payment View ---
    private JPanel qrPaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 25));
        panel.setBackground(bg);
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JLabel qrTitle = new JLabel("SCAN TO PAY", SwingConstants.CENTER);
        qrTitle.setForeground(text);
        qrTitle.setFont(new Font("Courier New", Font.BOLD, 24));
        
        JLabel amtLbl = new JLabel(
                "RM " + String.format("%.2f", totalAmount), SwingConstants.CENTER);
        amtLbl.setForeground(gold);
        amtLbl.setFont(new Font("Courier New", Font.BOLD, 20));
        
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 6));
        header.setOpaque(false);
        header.add(qrTitle);
        header.add(amtLbl);
        panel.add(header, BorderLayout.NORTH);
        
        
        JLabel qrLabel = new JLabel("");
        JPanel qrBox = new JPanel(new BorderLayout());
        qrBox.setBackground(Color.WHITE);
        qrBox.setBorder(new LineBorder(tgcRed, 5));
        
        qrBox.add(qrLabel, BorderLayout.CENTER);

        try {
            ImageIcon rawIcon = new ImageIcon("src/cinema/qr_code.png");
            Image scaledImg = rawIcon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
            qrBox.add(new JLabel(new ImageIcon(scaledImg)), BorderLayout.CENTER);
        } catch (Exception e) {
            qrBox.add(new JLabel("QR ERROR", SwingConstants.CENTER), BorderLayout.CENTER);
        }
        panel.add(qrBox, BorderLayout.CENTER);
        
        JLabel hint = new JLabel(
                "Use TNG / DuitNow / GrabPay to scan", SwingConstants.CENTER);
        hint.setForeground(softGray);
        hint.setFont(new Font("Courier New", Font.PLAIN, 11));
        
        JButton paid = createActionBtn("I HAVE PAID");
        paid.addActionListener(e -> simulateProcessing());
        
        JButton back = createBackBtn();
        back.addActionListener(e -> cardLayout.show(mainContainer, "METHODS"));
 
        JPanel south = new JPanel(new GridLayout(3, 1, 0, 8));
        south.setOpaque(false);
        south.add(hint);
        south.add(paid);
        south.add(back);
        panel.add(south, BorderLayout.SOUTH);

        return panel;
    }
    
    private JPanel processingPanel(){
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bg);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
 
        JLabel icon = new JLabel("⏳", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        gbc.gridy = 0;
        panel.add(icon, gbc);
 
        JLabel lbl = new JLabel("Processing payment…", SwingConstants.CENTER);
        lbl.setForeground(text);
        lbl.setFont(new Font("Courier New", Font.BOLD, 20));
        gbc.gridy = 1;
        panel.add(lbl, gbc);
 
        JLabel sub = new JLabel("Please do not close this window.", SwingConstants.CENTER);
        sub.setForeground(softGray);
        sub.setFont(new Font("Courier New", Font.PLAIN, 13));
        gbc.gridy = 2;
        panel.add(sub, gbc);
 
        return panel;
    }
    
    private void simulateProcessing() {
       cardLayout.show(mainContainer, "PROCESSING");
       SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(2000);   // fake network delay
                return null;
            }
            @Override
            protected void done() {
                updateReceipt();
                cardLayout.show(mainContainer, "RECEIPT");
            }
        };
        worker.execute();
    }
    

    // --- VIEW 4: Professional Receipt ---
    private JPanel receiptPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bg);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel ticket = new JPanel(new BorderLayout());
        ticket.setBackground(bg);
        ticket.setBorder(new LineBorder(new Color(0xDDDDDD), 1));

        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Courier New", Font.PLAIN, 15));
        receiptArea.setBackground(bg);
        receiptArea.setForeground(text); 
        receiptArea.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(bg);
        footer.setPreferredSize(new Dimension(0, 180));
        footer.setBorder(new MatteBorder(2, 0, 0, 0, new Color(0xEEEEEE)));

        JLabel footerLbl = new JLabel("KEEP THIS RECEIPT FOR FOOD COLLECTION", SwingConstants.CENTER);
        footerLbl.setForeground(new Color(0x888888));
        footerLbl.setFont(new Font("Courier New", Font.PLAIN, 11));
        footer.add(footerLbl,BorderLayout.CENTER);
        
        ticket.add(receiptArea, BorderLayout.CENTER);
        ticket.add(footer, BorderLayout.SOUTH);

        JButton done = createActionBtn("FINISH & RETURN");
        done.addActionListener(e -> {
            frame.dispose();
            new MainMenuPage(username);
                });

        panel.add(ticket, BorderLayout.CENTER);
        panel.add(done, BorderLayout.SOUTH);
        return panel;
    }
    
     private String generateOrderId() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String rand = Long.toHexString(new Random().nextLong() & 0xFFFFFFFFL)
                         .toUpperCase();
        return "TGC-" + date + "-" + String.format("%8s", rand).replace(' ', '0');
    }


    private void updateReceipt() {
        String dt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        StringBuilder items = new StringBuilder();
        for (PaymentItem i : basketItems) {
            String nameQty = String.format(" %-20s %d", i.getName(), i.getQuantity());
            String price   = String.format("RM %6.2f", i.getQuantity() * i.getPrice());
            int pad = 44 - nameQty.length() - price.length();
            items.append(nameQty)
                 .append(" ".repeat(Math.max(1, pad)))
                 .append(price)
                 .append("\n");
        }
        
        receiptArea.setText(
                "\n         TGC CINEMA CONCESSION\n" +
                        "------------------------------------------\n" +
                        " ORDER ID   : " + orderId + "\n" +
                        " DATE       : " + dt + "\n" +
                        " USER       : " + username + "\n" +
                        " CINEMA     : " + cinema + "\n" +
                        " PICKUP TIME: " + time + "\n" +
                        "------------------------------------------\n" +
                          items.toString() +
                        "------------------------------------------\n" +
                        " TOTAL PAID    : RM " + String.format("%.2f", totalAmount) + "\n" +
                        " PAYMENT METHOD: " + paymentMethod + "\n" +
                        "------------------------------------------\n"
        );
        
        saveOrder();
        deductStock();
    }

    // --- CUSTOM UI HELPERS ---
    private JButton createMenuButton(String title, String subtitle) {
        JButton btn = new JButton();
        btn.setLayout(new BoxLayout(btn, BoxLayout.Y_AXIS));
        btn.setBackground(panel);
        btn.setBorder(new LineBorder(new Color(0x333333), 1));
        btn.setPreferredSize(new Dimension(320, 70));

        JLabel t = new JLabel(title);
        t.setForeground(text);
        t.setFont(new Font("Courier New", Font.BOLD, 14));
        t.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel(subtitle);
        sub.setForeground(softGray);
        sub.setFont(new Font("Courier New", Font.PLAIN, 11));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.add(Box.createVerticalGlue());
        btn.add(t);
        btn.add(sub);
        btn.add(Box.createVerticalGlue());

        return btn;
    }

    private JButton createActionBtn(String text) {
        JButton b = new JButton(text);
        b.setBackground(tgcRed);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Courier New", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(0, 50));
        b.setBorder(null);
        return b;
    }

    private JTextField createStyledTextField(String p) {
        JTextField f = new JTextField(p);
        f.setBackground(panel);
        f.setForeground(softGray);
        f.setCaretColor(Color.WHITE);
        f.setPreferredSize(new Dimension(0, 45));
        f.setBorder(new CompoundBorder(new LineBorder(new Color(0x333333)), new EmptyBorder(0, 15, 0, 15)));
        return f;
    }

    private JLabel createInputLabel(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(softGray);
        l.setFont(new Font("Courier New", Font.BOLD, 11));
        return l;
    }
    
    private JLabel createErrorLabel() {
        JLabel l = new JLabel(" ");
        l.setForeground(tgcRed);
        l.setFont(new Font("Courier New", Font.PLAIN, 11));
        return l;
    }
 
    private JPanel wrapFieldGroup(String labelText, JTextField field) {
        JPanel g = new JPanel(new BorderLayout(0, 5));
        g.setOpaque(false);
        g.add(createInputLabel(labelText), BorderLayout.NORTH);
        g.add(field,                       BorderLayout.CENTER);
        return g;
    }
    
    private JButton createBackBtn() {
        JButton b = new JButton("← BACK");
        b.setBackground(panel);
        b.setForeground(softGray);
        b.setFont(new Font("Courier New", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(0, 36));
        b.setBorder(new LineBorder(new Color(0x333333), 1));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
    
    private void saveOrder(){
        String dt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        
        try(BufferedWriter wr = new BufferedWriter(new FileWriter("OrderHistory.txt", true))){
            
            wr.write("----ORDER----");
            wr.newLine();
            wr.write("User: " + username);
            wr.newLine();
            wr.write("OrderID: " + orderId);
            wr.newLine();
            wr.write("Cinema: " + cinema);
            wr.newLine();
            wr.write("Pick Up: " + time);
            wr.newLine();
            
            for (PaymentItem i : basketItems) {
                double subtotal = i.getQuantity() * i.getPrice();

                wr.write(i.getName() + " x" + i.getQuantity() +
                    " (RM " + String.format("%.2f", subtotal) + ")");
                wr.newLine();
            }

            wr.write("Total: RM " + String.format("%.2f", totalAmount));
            wr.newLine();
            
            wr.write("Date: " + dt);
            wr.newLine();
            
            wr.write("--------------");
            wr.newLine();
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    private void deductStock(){
        
        String file = "FnBStock.txt";
        ArrayList<String[]> items = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|", -1);

                if (data.length != 7) continue;

                String stockName = data[0];

                // check if this item was purchased
                for (PaymentItem i : basketItems) {

                    if (stockName.equalsIgnoreCase(i.getName())) {

                        int currentQty = Integer.parseInt(data[3]);
                        int newQty = currentQty - i.getQuantity();

                        // prevent negative stock
                        if (newQty < 0) newQty = 0;

                        data[3] = String.valueOf(newQty);

                        // update last updated time
                        data[5] = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                            .format(new java.util.Date());
                    }
                }

                items.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // 🔥 WRITE BACK TO FILE
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            for (String[] item : items) {
                bw.write(String.join("|", item));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}