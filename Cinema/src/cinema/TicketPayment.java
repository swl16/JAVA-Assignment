package cinema;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class TicketPayment extends JFrame{
    private CardLayout layout;
    private JPanel container;
    private UserOrder order;

    private JLabel qrAmountLabel;
    private JLabel timerLabel;
    private JTextArea receiptArea;
    private Timer countdownTimer;
    private int secondsRemaining = 300;

    private double finalAmount = 0;
    private String paymentMethod = "";

    // --- Premium Cinema Color Palette ---
    private final Color obsidian = new Color(0x242424);
    private final Color tgcRed = new Color(0xD44444);
    private final Color softGray = new Color(0xAAAAAA);
    private final Color offWhite = new Color(0xF7F7F7);

    public TicketPayment(UserOrder order){
        this.order = order;
        this.finalAmount = order.calculateTotalPrice();

        layout = new CardLayout();
        container = new JPanel(layout);
        container.setOpaque(false);

        container.add(paymentPanel(), "payment");
        container.add(qrPanel(), "qr");
        container.add(cardPanel(), "card_input");
        container.add(receiptPanel(), "receipt");

        add(container);

        setTitle("TGC Cinema - Premium Payment");
        setSize(500, 700);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(obsidian);
        setVisible(true);

        layout.show(container, "payment");
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

    private JPanel paymentPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(obsidian);

        JPanel upperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        upperPanel.setOpaque(false);

        JButton backButton = new JButton("X Cancel Payment");
        backButton.setForeground(offWhite);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusable(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel payment?\nWarning:Your selected items will be discarded.", "Cancel Payment", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION){
                dispose();
                new MainMenuPage(order.getUsername());
            }
        });

        upperPanel.add(backButton);
        mainPanel.add(upperPanel,BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(obsidian);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0; panel.add(createHeaderLabel("SELECT PAYMENT", 22), gbc);

        JButton creditBtn = createMenuButton("CREDIT / DEBIT CARD", "Visa / Mastercard / AMEX");
        creditBtn.addActionListener(e -> layout.show(container, "card_input"));
        gbc.gridy = 1; panel.add(creditBtn, gbc);

        JButton tng = createMenuButton("MOBILE WALLET QR", "TNG / DuitNow / Grab");
        tng.addActionListener(e -> {
            paymentMethod = "Mobile Wallet QR";
            startTimer();
            layout.show(container, "qr");
        });
        gbc.gridy = 2; panel.add(tng, gbc);

        mainPanel.add(panel,BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel cardPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(obsidian);

        addBackButton(mainPanel,"payment");

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

        // Field Groups for Expiry and CVC
        JPanel expiryGroup = createFieldGroup("EXPIRY (MM/YY)", "MM / YY");
        JPanel cvcGroup = createFieldGroup("CVC", "•••");
        row.add(expiryGroup);
        row.add(cvcGroup);

        gbc.gridy++; panel.add(row, gbc);

        JButton payBtn = createActionBtn("AUTHORIZE PAYMENT");
        payBtn.addActionListener(e -> {
            // Retrieve values from the styled components
            String name = holder.getText().trim();
            String number = cardNum.getText().replaceAll("\\s", ""); // Remove spaces for validation
            String exp = ((JTextField)expiryGroup.getComponent(1)).getText().trim();
            String cvcCode = ((JTextField)cvcGroup.getComponent(1)).getText().trim();

            // Perform Strict Validation before proceeding
            if (validateCardDetails(name, number, exp, cvcCode)) {
                paymentMethod = "Credit/Debit Card";
                updateReceipt();
                layout.show(container, "receipt");
            }
        });

        gbc.gridy++; gbc.insets = new Insets(30, 0, 10, 0);
        panel.add(payBtn, gbc);

        mainPanel.add(panel,BorderLayout.CENTER);
        return mainPanel;
    }

    private boolean validateCardDetails(String name, String number, String exp, String cvc) {
        // 1. Basic Check: Ensure no fields are empty or still showing placeholders
        if (name.isEmpty() || name.equals("Full Name") ||
                number.isEmpty() || number.contains("•") ||
                exp.equals("MM / YY") || cvc.equals("•••")) {
            showError("All card fields are required!");
            return false;
        }

        // 2. Card Number Check: Must be exactly 16 numeric digits
        if (!number.matches("\\d{16}")) {
            showError("Invalid Card Number! Must be 16 digits.");
            return false;
        }

        // 3. Expiry Format Check: Must match MM/YY (e.g., 05/28)
        if (!exp.matches("(0[1-9]|1[0-2])/[0-9]{2}")) {
            showError("Invalid Expiry Format! Use MM/YY (e.g., 12/26).");
            return false;
        }

        // 4. Expiry Logic Check: Ensure the card is not expired
        try {
            String[] parts = exp.split("/");
            int expMonth = Integer.parseInt(parts[0]);
            int expYear = Integer.parseInt("20" + parts[1]);

            LocalDateTime now = LocalDateTime.now();
            if (expYear < now.getYear() || (expYear == now.getYear() && expMonth < now.getMonthValue())) {
                showError("This card has expired!");
                return false;
            }
        } catch (Exception e) {
            showError("Invalid Expiry Date!");
            return false;
        }

        // 5. CVC Check: Must be exactly 3 numeric digits
        if (!cvc.matches("\\d{3}")) {
            showError("Invalid CVC! Must be 3 numeric digits.");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Security Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private JPanel qrPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(obsidian);

        JPanel topContainer = new JPanel(new GridLayout(2,1));
        topContainer.setOpaque(false);
        addBackButton(topContainer,"payment");

        JLabel qrTitle = createHeaderLabel("SCAN TO PAY", 24);
        topContainer.add(qrTitle);

        panel.add(topContainer,BorderLayout.NORTH);

        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));
        centerContainer.setOpaque(false);

        qrAmountLabel = new JLabel("TOTAL: RM " + String.format("%.2f", finalAmount), SwingConstants.CENTER);
        qrAmountLabel.setForeground(offWhite);
        qrAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        qrAmountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel qrBox = new JPanel(new BorderLayout());
        qrBox.setPreferredSize(new Dimension(220, 220));
        qrBox.setMaximumSize(new Dimension(220, 220));
        qrBox.setBackground(Color.WHITE);
        qrBox.setBorder(new LineBorder(tgcRed, 5));

        try {
            ImageIcon rawIcon = new ImageIcon("src/cinema/qr_code.png");
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
            ImageIcon rawEntry = new ImageIcon("src/cinema/entry_qr.png");
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
        done.addActionListener(e -> {dispose(); new MainMenuPage(order.getUsername());});

        panel.add(ticket, BorderLayout.CENTER);
        panel.add(done, BorderLayout.SOUTH);
        return panel;
    }

    private void updateReceipt() {
        String dt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        String orderID = "ORD" + System.currentTimeMillis();
        saveBooking(orderID,dt);

        String seatDisplay = String.join(", ", order.getSelectedSeats().stream().map(Seat::getSeatId).collect(Collectors.toList()));

        receiptArea.setText(
                "        TGC CINEMA OFFICIAL E-TICKET\n" +
                        "==========================================\n" +
                        " ID:             " + orderID + "\n" +
                        " DATE:           " + dt + "\n" +
                        " LOCATION:       Mit Valley Megamall\n" +
                        "------------------------------------------\n" +
                        " MOVIE:          " + order.getMovie().getTitle().toUpperCase() + "\n" +
                        " SEATS:          " + seatDisplay + "\n" +
                        "------------------------------------------\n" +
                        " TICKET(S):      RM " + String.format("%.2f", order.getTicketTotalPrice()) + "\n" +
                        " F&B ADD-ON:     RM " + String.format("%.2f", order.getFoodTotalPrice()) + "\n" +
                        " PROCESSING FEE: RM " + String.format("%.2f", order.calculateProcessingFee()) + "\n" +
                        " TOTAL:          RM " + String.format("%.2f", finalAmount) + "\n" +
                        " METHOD:         " + paymentMethod + "\n" +
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
        b.setBackground(obsidian);
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
        f.setBackground(obsidian);
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

    private void addBackButton(JPanel panel,String targetCard){
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setOpaque(false);

        JButton backBtn = new JButton("< BACK");
        backBtn.setForeground(offWhite);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backBtn.addActionListener(e -> {
            // Stop the timer if we leave the QR page
            if (countdownTimer != null) countdownTimer.stop();
            layout.show(container, targetCard);
        });

        topBar.add(backBtn);
        panel.add(topBar, BorderLayout.NORTH);
    }

    private void saveBooking(String orderID, String orderTime){
        StringBuilder seatData = new StringBuilder();
        for (int i = 0; i < order.getSelectedSeats().size(); i++){
            Seat s = order.getSelectedSeats().get(i);
            seatData.append(s.getSeatId());
            if (i < order.getSelectedSeats().size() - 1)  seatData.append(",");
        }

        StringBuilder foodData = new StringBuilder();
        if (order.getSelectedFood().isEmpty()){
            foodData.append("-");
        }else {
            int count = 0;
            for (Map.Entry<fnbitem, Integer> entry : order.getSelectedFood().entrySet()){
                foodData.append(entry.getKey().getItemname()).append(":").append(entry.getValue());
                if (++count < order.getSelectedFood().size()) foodData.append(",");
            }
        }

        StringBuilder typeData = new StringBuilder();
        for (int i = 0; i < order.getSeatTypeCount().length; i++){
            typeData.append(order.getSeatTypeCount()[i]);
            if (i < order.getSeatTypeCount().length - 1) typeData.append(",");
        }
        
        deductStock();
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("BookingDetail.txt",true))){
            writer.write(orderID + "|" + order.getUsername() + "|" + order.getMovie().getTitle() + "|" + order.getMovie().getDuration() + "|" + order.getShowTime().getStartTime() + "|" + order.getShowTime().getHallName() + "|" + seatData.toString() + "|" + typeData.toString() + "|" + foodData.toString() + "|" + order.getTicketTotalPrice() + "|" + order.getFoodTotalPrice() + "|" + order.calculateProcessingFee() + "|" + order.calculateTotalPrice() + "|" + orderTime);
            writer.newLine();

        } catch (IOException e) {
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
                for (fnbitem item : order.getSelectedFood().keySet()) {
                    int qty = order.getSelectedFood().get(item);

                    if (stockName.equalsIgnoreCase(item.getItemname())) {

                        int currentQty = Integer.parseInt(data[3]);
                        int newQty = currentQty - qty;

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

        // WRITE BACK TO FILE
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

