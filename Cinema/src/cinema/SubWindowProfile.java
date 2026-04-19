package cinema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class SubWindowProfile extends JFrame implements ActionListener {
    private JFrame frame = new JFrame("TGC Cinema");
    private JFrame homeFrame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JButton backButton1, backButton2, backButton3, delAccBtn;

    private String username;
    private List<String[]> order = new ArrayList<>();
    private List<String[]> orderPast = new ArrayList<>();
    private String[] seatType = {"Adult", "Student", "Senior", "OKU"};

    private final Color background = new Color(0x242424);
    private final Color buttonRed = new Color(0xD44444);
    private final Color textWhite = new Color(0xF7F7F7);
    private final Color grey = new Color(0x3B3B3B);
    private final Color middleGrey = new Color(0x555555);
    private final Color lightGrey = new Color(0xAAAAAA);

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("EEE dd MMM , HH:mm");

    public SubWindowProfile(JFrame homeFrame, int choice, String username) {
        this.homeFrame = homeFrame;
        this.username = username;

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(500, 700);
        frame.getContentPane().setBackground(background);
        frame.setVisible(true);
        loadUserOrder();

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(createTicketPanel(), "ticket");
        cardPanel.add(createContactUsPanel(), "contactUs");
        cardPanel.add(createSettingPanel(), "setting");
        cardPanel.add(createChangePassPanel(), "changePass");

        frame.add(cardPanel);

        switch (choice) {
            case 1:
                cardLayout.show(cardPanel, "ticket");
                break;
            case 2:
                cardLayout.show(cardPanel, "contactUs");
                break;
            case 3:
                cardLayout.show(cardPanel, "setting");
                break;
        }


    }

    private JPanel createTicketPanel() {
        JPanel ticketPanel = new JPanel();
        ticketPanel.setLayout(null);
        ticketPanel.setBackground(background);

        backButton1 = makeBackButton();
        backButton1.addActionListener(this);
        ticketPanel.add(backButton1);

        JLabel ticketLabel = new JLabel("My Ticket");
        ticketLabel.setForeground(textWhite);
        ticketLabel.setFont(new Font("Courier New", Font.BOLD, 40));
        ticketLabel.setBounds(40, 60, 500, 50);
        ticketPanel.add(ticketLabel);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(background);

        JLabel currentLabel = new JLabel("Upcoming Tickets", SwingConstants.LEFT);
        currentLabel.setForeground(textWhite);
        currentLabel.setFont(new Font("Courier New", Font.BOLD, 20));
        currentLabel.setMaximumSize(new Dimension(440, 20));
        currentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(currentLabel);
        listPanel.add(Box.createVerticalStrut(20));

        if (order.isEmpty()) {
            JLabel emptyLabel = new JLabel("No Upcoming Ticket Records.", SwingConstants.CENTER);
            emptyLabel.setForeground(textWhite);
            emptyLabel.setFont(new Font("Courier New", Font.ITALIC, 15));
            emptyLabel.setMaximumSize(new Dimension(440, 60));
            emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(emptyLabel);
        } else {
            for (String[] ord : order) {
                JPanel card = createOrderCard(ord, true);
                card.setMaximumSize(new Dimension(430, 175));
                card.setMinimumSize(new Dimension(430, 175));
                card.setPreferredSize(new Dimension(430, 175));
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                listPanel.add(card);
                listPanel.add(Box.createVerticalStrut(15));
            }
        }

        listPanel.add(Box.createVerticalStrut(20));
        JLabel pastLabel = new JLabel("Past Order", SwingConstants.LEFT);
        pastLabel.setForeground(textWhite);
        pastLabel.setFont(new Font("Courier New", Font.BOLD, 20));
        pastLabel.setMaximumSize(new Dimension(430, 20));
        pastLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(pastLabel);
        listPanel.add(Box.createVerticalStrut(20));

        if (orderPast.isEmpty()) {
            JLabel emptyLabel1 = new JLabel("No Past Order Records.", SwingConstants.CENTER);
            emptyLabel1.setForeground(textWhite);
            emptyLabel1.setFont(new Font("Courier New", Font.ITALIC, 15));
            emptyLabel1.setMaximumSize(new Dimension(430, 60));
            emptyLabel1.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(emptyLabel1);
        } else {
            for (String[] ord : orderPast) {
                JPanel card = createOrderCard(ord, false);
                card.setMaximumSize(new Dimension(430, 175));
                card.setMinimumSize(new Dimension(430, 175));
                card.setPreferredSize(new Dimension(430, 175));
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                listPanel.add(card);
                listPanel.add(Box.createVerticalStrut(15));
            }
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBounds(25, 120, 480, 530);
        scrollPane.setBorder(null);
        scrollPane.setBackground(background);
        scrollPane.getViewport().setBackground(background);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        ticketPanel.add(scrollPane);

        return ticketPanel;
    }

    private JPanel createOrderCard(String[] order, Boolean upcoming) {
        String formattedDateTime = LocalDateTime.parse(order[3]).format(FMT);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(grey);

        JLabel movieLabel = new JLabel(order[1]);
        movieLabel.setForeground(textWhite);
        movieLabel.setFont(new Font("Courier New", Font.BOLD, 20));
        movieLabel.setBounds(10, 10, 250, 20);
        panel.add(movieLabel);

        JLabel badge = new JLabel(upcoming ? "  UPCOMING  " : "  COMPLETED  ", SwingConstants.CENTER);
        badge.setForeground(textWhite);
        badge.setFont(new Font("Courier New", Font.BOLD, 10));
        badge.setBackground(upcoming ? buttonRed : new Color(0x66cf46));
        badge.setOpaque(true);
        badge.setBounds(300, 12, 100, 18);
        panel.add(badge);

        JLabel locationLabel = new JLabel("Location: Mit Valley Megamall");
        locationLabel.setForeground(textWhite);
        locationLabel.setFont(new Font("Courier New", Font.BOLD, 15));
        locationLabel.setBounds(10, 40, 400, 20);
        panel.add(locationLabel);

        JLabel showtimeLabel = new JLabel("Show time: " + formattedDateTime + " at " + order[4]);
        showtimeLabel.setForeground(textWhite);
        showtimeLabel.setFont(new Font("Courier New", Font.BOLD, 15));
        showtimeLabel.setBounds(10, 65, 400, 20);
        panel.add(showtimeLabel);

        JLabel seatLabel = new JLabel("Seat(s): " + order[5]);
        seatLabel.setForeground(textWhite);
        seatLabel.setFont(new Font("Courier New", Font.BOLD, 15));
        seatLabel.setBounds(10, 90, 400, 20);
        panel.add(seatLabel);

        JSeparator sep = new JSeparator();
        sep.setBounds(15, 120, 415, 1);
        sep.setForeground(middleGrey);
        panel.add(sep);

        JLabel priceLabel = new JLabel("Total Amount Paid: RM" + String.format("%.2f", Double.parseDouble(order[11])));
        priceLabel.setForeground(textWhite);
        priceLabel.setFont(new Font("Courier New", Font.BOLD, 15));
        priceLabel.setBounds(10, 130, 260, 20);
        panel.add(priceLabel);

        JButton viewButton = new JButton(upcoming ? "View QR & Details" : "View Details");
        viewButton.setBorderPainted(false);
        viewButton.setFocusPainted(false);
        viewButton.setFont(new Font("Courier New", Font.PLAIN, 12));
        viewButton.setBounds(270, 130, 150, 25);
        viewButton.setForeground(textWhite);
        viewButton.setBackground(buttonRed);
        viewButton.setMargin(new Insets(0, 0, 0, 0));
        viewButton.addActionListener(e -> {
            String key = order[0];
            JPanel detailPanel = createOrderDetailPanel(order, upcoming);
            cardPanel.add(detailPanel, key);
            cardLayout.show(cardPanel, key);
        });
        panel.add(viewButton);


        return panel;
    }

    private JPanel createOrderDetailPanel(String[] order, boolean upcoming) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(background);

        JButton backButton = makeBackButton();
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "ticket"));
        panel.add(backButton);

        JLabel titleLabel = new JLabel("Order Details");
        titleLabel.setForeground(textWhite);
        titleLabel.setFont(new Font("Courier New", Font.BOLD, 20));
        titleLabel.setBounds(40, 60, 400, 30);
        panel.add(titleLabel);

        String formattedShow = LocalDateTime.parse(order[3]).format(FMT);

        int currentY = 10;
        int scrollStartY = 100;

        if (upcoming) {
            ImageIcon qrcode = new ImageIcon("src/cinema/qr_code.png");
            Image scaledQR = qrcode.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);

            JPanel qrPanel = new JPanel(new BorderLayout());
            qrPanel.setPreferredSize(new Dimension(100, 100));
            qrPanel.setBounds(200, 90, 100, 100);
            qrPanel.setBackground(Color.WHITE);
            qrPanel.add(new JLabel(new ImageIcon(scaledQR)), BorderLayout.CENTER);
            panel.add(qrPanel);

            JLabel qrHintLabel = new JLabel("Show this QR at the counter");
            qrHintLabel.setForeground(textWhite);
            qrHintLabel.setFont(new Font("Courier New", Font.PLAIN, 12));
            qrHintLabel.setHorizontalAlignment(SwingConstants.CENTER);
            qrHintLabel.setBounds(100, 210, 280, 18);
            panel.add(qrHintLabel);

            JSeparator sep = new JSeparator();
            sep.setBounds(40, 240, 400, 1);
            sep.setForeground(middleGrey);
            panel.add(sep);

            scrollStartY = 250;
        }

        JPanel row = new JPanel(null);
        row.setBackground(background);

        JLabel orderIdLabel = makeDetailsLabel("Order ID: " + order[0], currentY);
        row.add(orderIdLabel);
        currentY += 25;

        JLabel transDateLabel = makeDetailsLabel("Transaction Date: " + order[12], currentY);
        row.add(transDateLabel);
        currentY += 25;

        JSeparator sep1 = new JSeparator();
        sep1.setBounds(15, currentY, 400, 1);
        sep1.setForeground(middleGrey);
        row.add(sep1);
        currentY += 15;

        JPanel moviePanel = createLabelGroup("Movie", order[1]);
        moviePanel.setBounds(20, currentY, 400, 40);
        row.add(moviePanel);
        currentY += 45;

        JPanel locationPanel = createLabelGroup("Location", "Mit Valley Megamall");
        locationPanel.setBounds(20, currentY, 400, 40);
        row.add(locationPanel);
        currentY += 45;

        JPanel showTimePanel = createLabelGroup("Time", formattedShow);
        showTimePanel.setBounds(20, currentY, 400, 40);
        row.add(showTimePanel);
        currentY += 45;

        JPanel hallPanel = createLabelGroup("Cinema", order[4]);
        hallPanel.setBounds(20, currentY, 400, 40);
        row.add(hallPanel);
        currentY += 45;

        JPanel seatPanel = createLabelGroup("Seat(s)", order[5]);
        seatPanel.setBounds(20, currentY, 400, 40);
        row.add(seatPanel);
        currentY += 55;

        JSeparator sep2 = new JSeparator();
        sep2.setBounds(15, currentY, 400, 1);
        sep2.setForeground(middleGrey);
        row.add(sep2);
        currentY += 15;

        JPanel ticketPanel = createPriceRow("Ticket(s)", "RM " + String.format("%.2f", Double.parseDouble(order[8])));
        ticketPanel.setBounds(20, currentY, 400, 15);
        row.add(ticketPanel);
        currentY += 20;

        String[] typeCounts = order[6].split(",");
        for (int i = 0; i < typeCounts.length; i++) {
            int count = Integer.parseInt(typeCounts[i].trim());
            if (count > 0) {
                JLabel typeLabel = createSubLabel(seatType[i] + " x " + count);
                typeLabel.setBounds(25, currentY, 400, 15);
                row.add(typeLabel);
                currentY += 20;
            }
        }
        currentY += 5;

        JPanel processFeePanel = createPriceRow("Processing Fee", "RM " + String.format("%.2f", Double.parseDouble(order[10])));
        processFeePanel.setBounds(20, currentY, 400, 15);
        row.add(processFeePanel);
        currentY += 30;

        JSeparator sep3 = new JSeparator();
        sep3.setBounds(15, currentY, 400, 1);
        sep3.setForeground(middleGrey);
        row.add(sep3);
        currentY += 15;

        JPanel fnbPanel = createPriceRow("Food and Drinks", "RM " + String.format("%.2f", Double.parseDouble(order[9])));
        fnbPanel.setBounds(20, currentY, 400, 15);
        row.add(fnbPanel);
        currentY += 20;

        String[] foodDisplay = order[7].split(",");
        for (String food : foodDisplay) {
            JLabel foodLabel = createSubLabel(food);
            foodLabel.setBounds(25, currentY, 400, 15);
            row.add(foodLabel);
            currentY += 20;
        }

        currentY += 10;
        JSeparator sep4 = new JSeparator();
        sep4.setBounds(15, currentY, 400, 1);
        sep4.setForeground(middleGrey);
        row.add(sep4);
        currentY += 15;

        JPanel totalPanel = createPriceRow("Total Amount Paid", "RM " + String.format("%.2f", Double.parseDouble(order[11])));
        totalPanel.setBounds(20, currentY, 400, 15);
        row.add(totalPanel);
        currentY += 50;

        row.setPreferredSize(new Dimension(450, currentY));

        int windowHeight = 600 - scrollStartY;
        JScrollPane scrollPane = new JScrollPane(row);
        scrollPane.setBounds(20, scrollStartY, 483, windowHeight);
        scrollPane.setBorder(null);
        scrollPane.setBackground(background);
        scrollPane.getViewport().setBackground(background);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        panel.add(scrollPane);

        return panel;
    }

    private JPanel createContactUsPanel() {
        JPanel contactUsPanel = new JPanel();
        contactUsPanel.setLayout(null);
        contactUsPanel.setBackground(background);

        backButton2 = makeBackButton();
        backButton2.addActionListener(this);
        ;
        contactUsPanel.add(backButton2);

        JLabel contactLabel = new JLabel("Contact Us");
        contactLabel.setForeground(textWhite);
        contactLabel.setFont(new Font("Courier New", Font.BOLD, 40));
        contactLabel.setBounds(40, 50, 500, 50);
        contactUsPanel.add(contactLabel);

        JLabel infoLabel = new JLabel("Get in touch with us");
        infoLabel.setForeground(textWhite);
        infoLabel.setFont(new Font("Courier New", Font.BOLD, 20));
        infoLabel.setBounds(40, 120, 400, 20);
        contactUsPanel.add(infoLabel);

        addContactRow(contactUsPanel, "Address", "L3-01, Mit Valley Megamall, Kuala Lumpur", 150);
        addContactRow(contactUsPanel, "Customer Relations Hotline", "+603-1234 6677", 220);
        addContactRow(contactUsPanel, "Email Support", "support@tgccinema.com.my", 290);
        addContactRow(contactUsPanel, "Service Hours", "Mon to Fri 10:00 AM – 6:00 PM", 360);

        JLabel socialLabel = new JLabel("Follow Us");
        socialLabel.setForeground(lightGrey);
        socialLabel.setFont(new Font("Courier New", Font.BOLD, 14));
        socialLabel.setBounds(40, 450, 400, 20);
        contactUsPanel.add(socialLabel);

        JLabel fbLabel = new JLabel("Facebook : @TGCCinema");
        fbLabel.setForeground(textWhite);
        fbLabel.setFont(new Font("Courier New", Font.PLAIN, 14));
        fbLabel.setBounds(40, 480, 400, 20);
        contactUsPanel.add(fbLabel);

        JLabel insLabel = new JLabel("Instagram : @tgc_cinema.my");
        insLabel.setForeground(textWhite);
        insLabel.setFont(new Font("Courier New", Font.PLAIN, 14));
        insLabel.setBounds(40, 510, 400, 20);
        contactUsPanel.add(insLabel);

        JLabel xLabel = new JLabel("X : @TGCCinema");
        xLabel.setForeground(textWhite);
        xLabel.setFont(new Font("Courier New", Font.PLAIN, 14));
        xLabel.setBounds(40, 540, 400, 20);
        contactUsPanel.add(xLabel);

        return contactUsPanel;
    }

    private void addContactRow(JPanel panel, String label, String value, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(lightGrey);
        lbl.setFont(new Font("Courier New", Font.BOLD, 13));
        lbl.setBounds(40, y, 380, 20);
        panel.add(lbl);

        JLabel val = new JLabel(value);
        val.setForeground(textWhite);
        val.setFont(new Font("Courier New", Font.PLAIN, 14));
        val.setBounds(40, y + 22, 380, 20);
        panel.add(val);

        JSeparator sep = new JSeparator();
        sep.setBounds(40, y + 52, 400, 1);
        sep.setForeground(lightGrey);
        panel.add(sep);
    }

    private JPanel createSettingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(background);

        backButton3 = makeBackButton();
        backButton3.addActionListener(this);
        panel.add(backButton3);

        JLabel settingLabel = new JLabel("Setting");
        settingLabel.setForeground(textWhite);
        settingLabel.setFont(new Font("Courier New", Font.BOLD, 40));
        settingLabel.setBounds(40, 60, 500, 50);
        panel.add(settingLabel);

        JLabel securityLabel = new JLabel("Security");
        securityLabel.setForeground(lightGrey);
        securityLabel.setFont(new Font("Courier New", Font.BOLD, 20));
        securityLabel.setBounds(40, 120, 500, 30);
        panel.add(securityLabel);

        JButton changePassBtn = new JButton("Change Password                   >");
        changePassBtn.setBorderPainted(false);
        changePassBtn.setFocusPainted(false);
        changePassBtn.setContentAreaFilled(false);
        changePassBtn.setFocusable(false);
        changePassBtn.setBounds(40, 150, 400, 30);
        changePassBtn.setFont(new Font("Courier New", Font.PLAIN, 17));
        changePassBtn.setHorizontalAlignment(JButton.LEFT);
        changePassBtn.addActionListener(e -> cardLayout.show(cardPanel, "changePass"));
        changePassBtn.setForeground(textWhite);
        panel.add(changePassBtn);

        JLabel accManageLabel = new JLabel("Account Management");
        accManageLabel.setForeground(lightGrey);
        accManageLabel.setFont(new Font("Courier New", Font.BOLD, 20));
        accManageLabel.setBounds(45, 200, 500, 30);
        panel.add(accManageLabel);

        delAccBtn = new JButton("Delete Account                    >");
        delAccBtn.setBorderPainted(false);
        delAccBtn.setFocusPainted(false);
        delAccBtn.setContentAreaFilled(false);
        delAccBtn.setFocusable(false);
        delAccBtn.setBounds(40, 230, 400, 30);
        delAccBtn.setFont(new Font("Courier New", Font.PLAIN, 17));
        delAccBtn.setHorizontalAlignment(JButton.LEFT);
        delAccBtn.addActionListener(this);
        delAccBtn.setForeground(textWhite);
        panel.add(delAccBtn);

        return panel;
    }

    private JPanel createChangePassPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(background);

        JButton backButton = makeBackButton();
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "setting"));
        panel.add(backButton);

        JLabel changePassLabel = new JLabel("Change Password");
        changePassLabel.setForeground(textWhite);
        changePassLabel.setFont(new Font("Courier New", Font.BOLD, 30));
        changePassLabel.setBounds(40, 60, 500, 50);
        panel.add(changePassLabel);

        JLabel tipsLabel = new JLabel("*Password must at least 6 characters.");
        tipsLabel.setForeground(buttonRed);
        tipsLabel.setFont(new Font("Courier New", Font.BOLD, 12));
        tipsLabel.setBounds(40, 120, 500, 15);
        panel.add(tipsLabel);

        JLabel currentPassLabel = new JLabel("Current Password");
        currentPassLabel.setForeground(textWhite);
        currentPassLabel.setFont(new Font("Courier New", Font.BOLD, 20));
        currentPassLabel.setBounds(40, 150, 500, 30);
        panel.add(currentPassLabel);

        JPasswordField currentPassType = new JPasswordField();
        currentPassType.setBounds(40, 180, 400, 20);
        currentPassType.setFont(new Font("Courier New", Font.PLAIN, 15));
        currentPassType.setForeground(textWhite);
        currentPassType.setBackground(grey);
        currentPassType.setCaretColor(textWhite);
        currentPassType.setBorder(null);
        panel.add(currentPassType);

        JLabel newPassLabel = new JLabel("New Password");
        newPassLabel.setForeground(textWhite);
        newPassLabel.setFont(new Font("Courier New", Font.BOLD, 20));
        newPassLabel.setBounds(40, 220, 500, 30);
        panel.add(newPassLabel);

        JPasswordField newPassType = new JPasswordField();
        newPassType.setBounds(40, 250, 400, 20);
        newPassType.setFont(new Font("Courier New", Font.PLAIN, 15));
        newPassType.setForeground(textWhite);
        newPassType.setBackground(grey);
        newPassType.setCaretColor(textWhite);
        newPassType.setBorder(null);
        panel.add(newPassType);

        JLabel repeatPassLabel = new JLabel("Re-type Password");
        repeatPassLabel.setForeground(textWhite);
        repeatPassLabel.setFont(new Font("Courier New", Font.BOLD, 20));
        repeatPassLabel.setBounds(40, 290, 500, 30);
        panel.add(repeatPassLabel);

        JPasswordField repeatPassType = new JPasswordField();
        repeatPassType.setBounds(40, 320, 400, 20);
        repeatPassType.setFont(new Font("Courier New", Font.PLAIN, 15));
        repeatPassType.setForeground(textWhite);
        repeatPassType.setBackground(grey);
        repeatPassType.setCaretColor(textWhite);
        repeatPassType.setBorder(null);
        panel.add(repeatPassType);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setBorderPainted(false);
        confirmButton.setFocusPainted(false);
        confirmButton.setFont(new Font("Courier New", Font.BOLD, 15));
        confirmButton.setBounds(25, 600, 430, 30);
        confirmButton.setForeground(textWhite);
        confirmButton.setBackground(buttonRed);
        confirmButton.addActionListener(e -> {

            String oldPassword = new String(currentPassType.getPassword());
            String newPassword = new String(newPassType.getPassword());
            String reTypePassword = new String(repeatPassType.getPassword());

            changePassword(oldPassword, newPassword, reTypePassword);
        });
        panel.add(confirmButton);

        return panel;
    }

    private void loadUserOrder() {
        try (BufferedReader readLine = new BufferedReader(new FileReader("BookingDetail.txt"))) {
            String line;

            while ((line = readLine.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length < 13) continue;

                if (username.equals(parts[1])) {
                    String[] ord = {parts[0], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8].replace(":", " x "), parts[9], parts[10], parts[11], parts[12], parts[13]};
                    if (LocalDateTime.parse(parts[4]).plusMinutes(Integer.parseInt(parts[3])).isAfter(LocalDateTime.now())) {
                        order.add(ord);
                    } else {
                        orderPast.add(ord);
                    }
                }

            }
        } catch (IOException e) {
            System.out.println("Error reading booking file");
        }
    }

    private void changePassword(String oldPassword, String newPassword, String reTypePassword) {
        List<String> lines = new ArrayList<>();
        boolean success = false;
        String password = "";
        try (BufferedReader readUser = new BufferedReader(new FileReader("Users.txt"))) {
            String line;
            while ((line = readUser.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    password = parts[1];

                    if (!password.equals(oldPassword)) {
                        JOptionPane.showMessageDialog(null,
                                "Incorrect current password, please try again!", "Incorrect password", JOptionPane.WARNING_MESSAGE);
                        lines.add(line);
                    } else if (newPassword.length() < 6) {
                        JOptionPane.showMessageDialog(null,
                                "New password must at least 6 character!", "New password error", JOptionPane.WARNING_MESSAGE);
                        lines.add(line);
                    } else if (!newPassword.equals(reTypePassword)) {
                        JOptionPane.showMessageDialog(null,
                                "Re-type password are different with new password!", "Incorrect password", JOptionPane.WARNING_MESSAGE);
                        lines.add(line);
                    } else if (password.equals(newPassword)) {
                        JOptionPane.showMessageDialog(null,
                                "New password cannot be same as the current password!", "", JOptionPane.WARNING_MESSAGE);
                        lines.add(line);
                    } else {
                        lines.add(username + "," + newPassword);
                        success = true;
                    }
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users file");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Users.txt"))) {
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (success) {
            JOptionPane.showMessageDialog(null, "Password updated successfully!");
            frame.dispose();
            homeFrame.setVisible(true);
        }
    }

    private void deleteAccount() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader readUser = new BufferedReader(new FileReader("Users.txt"))) {
            String line;
            while ((line = readUser.readLine()) != null) {
                String[] parts = line.split(",");
                if (!parts[0].equals(username)) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users file");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Users.txt"))) {
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton1 || e.getSource() == backButton2 || e.getSource() == backButton3) {
            frame.dispose();
            homeFrame.setVisible(true);
        } else if (e.getSource() == delAccBtn) {
            int choice = JOptionPane.showConfirmDialog(null, "This action will permanently delete your account, along with all of your information. Are you sure you want to delete your account?", "Delete Account", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                deleteAccount();
                JOptionPane.showMessageDialog(null, "Your account is deleted successfully!");
                frame.dispose();
                new LoginPage();
            }
        }

    }

    private JButton makeBackButton() {
        JButton backButton = new JButton("< Back");
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusable(false);
        backButton.setBounds(8, 10, 150, 35);
        backButton.setFont(new Font("Courier New", Font.PLAIN, 17));
        backButton.setHorizontalAlignment(JButton.LEFT);
        backButton.setForeground(textWhite);
        return backButton;
    }

    private JLabel makeDetailsLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setForeground(textWhite);
        label.setFont(new Font("Courier New", Font.BOLD, 15));
        label.setBounds(20, y, 400, 10);

        return label;
    }

    private JPanel createLabelGroup(String title, String value) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setForeground(lightGrey);
        t.setFont(new Font("Courier New", Font.BOLD, 14));
        p.add(t);
        JLabel v = new JLabel(value);
        v.setForeground(textWhite);
        v.setFont(new Font("Courier New", Font.BOLD, 16));
        p.add(v);

        return p;
    }

    private JPanel createPriceRow(String label, String price) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setForeground(lightGrey);
        l.setFont(new Font("Courier New", Font.BOLD, 14));
        JLabel r = new JLabel(price);
        r.setForeground(textWhite);
        r.setFont(new Font("Courier New", Font.BOLD, 16));
        p.add(l, BorderLayout.WEST);
        p.add(r, BorderLayout.EAST);

        return p;
    }

    private JLabel createSubLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(textWhite);
        label.setFont(new Font("Courier New", Font.BOLD, 16));

        return label;
    }
}

