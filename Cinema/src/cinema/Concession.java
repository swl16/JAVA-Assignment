package cinema;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.List;

public class Concession {
    private JFrame frame;
    private JPanel mainPanel, menuView, checkoutView, selectionView;
    private CardLayout layout;
    private JLabel totalLabel, footerQtyLabel;
    // UI Theme
    private final Color BG = new Color(0x121212), CARD = new Color(0x1E1E1E), ACCENT = new Color(0xFFD700),panelcolor = new Color(0x2E2E2E);
    private final Color TEXT = Color.WHITE, MUTED_TEXT = new Color(0xAAAAAA), INPUT_BG = new Color(0x2A2A2A), redcolor = new Color(0xD44444);
    private final Dimension UI_ELEMENT_SIZE = new Dimension(350, 45);

    private Map<String, Integer> basket = new HashMap<>();
    private Map<String, Double> prices = new HashMap<>();
    private String selRegion, selCinema, selTime;
    private String username;
    private ArrayList<FnbItem> items = new ArrayList<>();

    public Concession(String username) {
        this.username = username;
        loadStock();
        
        frame = new JFrame("TGC Cinema - Concession F&B");
        frame.setSize(500, 700);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        layout = new CardLayout();
        mainPanel = new JPanel(layout);

        createSelectionView();
        createMenuView();
        createCheckoutView();

        mainPanel.add(selectionView, "SELECT_DETAILS");
        mainPanel.add(menuView, "MENU");
        mainPanel.add(checkoutView, "CHECKOUT");

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void createSelectionView() {
        selectionView = new JPanel();
        selectionView.setLayout(new BoxLayout(selectionView, BoxLayout.Y_AXIS));
        selectionView.setBackground(BG);
        selectionView.setBorder(new EmptyBorder(60, 45, 40, 45));
        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        header.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JButton backBtn = new JButton("< Back");
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setOpaque(false);
        backBtn.setFont(new Font("Courier New", Font.PLAIN, 13));
        backBtn.setForeground(MUTED_TEXT);
        backBtn.setMargin(new Insets(2, 4, 2, 4));
        backBtn.setHorizontalAlignment(SwingConstants.LEFT);
        backBtn.addActionListener(e -> {
            frame.dispose();
            new MainMenuPage(username);
                });
        header.add(backBtn, BorderLayout.WEST);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel title = new JLabel("CONCESSION");
        title.setForeground(redcolor);
        title.setFont(new Font("Courier New", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel sub = new JLabel("Food & Beverage Order");
        sub.setForeground(MUTED_TEXT);
        sub.setFont(new Font("Courier New", Font.PLAIN, 13));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(0x333333));
        sep.setMaximumSize(new Dimension(350, 2));

        JComboBox<String> regionBox = createStyledCombo(new String[]{"Klang Valley"});
        JComboBox<String> cinemaBox = createStyledCombo(new String[]{"TGC Mit Valley"});
        JComboBox<String> dateBox = createStyledCombo(getNextSevenDays());
        dateBox.setVisible(true);


        JButton nextBtn = new JButton("START ORDERING");
        styleButton(nextBtn, redcolor, TEXT);
        nextBtn.setFont(new Font("Courier New", Font.BOLD, 16));
        nextBtn.setMaximumSize(new Dimension(350, 52));
        nextBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextBtn.setBorder(new EmptyBorder(10, 20, 10, 20));

        nextBtn.addActionListener(e -> {
            selRegion = (String) regionBox.getSelectedItem();
            selCinema = (String) cinemaBox.getSelectedItem();
            selTime = (String) dateBox.getSelectedItem();
            layout.show(mainPanel, "MENU");
        });
        
        JButton historyBtn = new JButton("ORDER HISTORY");
        styleButton(historyBtn, panelcolor, TEXT);
        historyBtn.setFont(new Font("Courier New", Font.BOLD, 16));
        historyBtn.setMaximumSize(new Dimension(350, 52));
        historyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        historyBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        historyBtn.addActionListener(e -> historyView());
        
        selectionView.add(header);
        selectionView.add(title);
        selectionView.add(Box.createRigidArea(new Dimension(0, 4)));
        selectionView.add(sub);
        selectionView.add(Box.createRigidArea(new Dimension(0, 20)));
        selectionView.add(sep);
        selectionView.add(Box.createRigidArea(new Dimension(0, 28)));
        addInputGroup(selectionView, "SELECT REGION", regionBox);
        addInputGroup(selectionView, "SELECT CINEMA", cinemaBox);
        addInputGroup(selectionView, "PICKUP DATE", dateBox);
        selectionView.add(Box.createVerticalGlue());
        selectionView.add(nextBtn);
        selectionView.add(Box.createRigidArea(new Dimension(0, 12)));
        selectionView.add(historyBtn);
    }

    private JComboBox<String> createStyledCombo(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBackground(INPUT_BG);
        combo.setForeground(TEXT);
        combo.setFont(new Font("Courier New", Font.PLAIN, 15));
        combo.setBorder(new LineBorder(new Color(0x444444), 1));
        combo.setMaximumSize(UI_ELEMENT_SIZE);
        combo.setPreferredSize(UI_ELEMENT_SIZE);
        combo.setAlignmentX(Component.CENTER_ALIGNMENT);
        return combo;
    }

    private void addInputGroup(JPanel p, String label, JComponent combo) {
        JLabel l = new JLabel(label);
        l.setForeground(MUTED_TEXT);
        l.setFont(new Font("Courier New", Font.BOLD, 11));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(l);
        p.add(Box.createRigidArea(new Dimension(0, 8)));
        p.add(combo);
        p.add(Box.createRigidArea(new Dimension(0, 20)));
    }

    private void createMenuView() {
        menuView = new JPanel(new BorderLayout());
        menuView.setBackground(BG);

        // Header with Back Button
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        header.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton backBtn = new JButton("< Back");
        styleButton(backBtn, BG, MUTED_TEXT);
        backBtn.addActionListener(e -> layout.show(mainPanel, "SELECT_DETAILS"));
        header.add(backBtn, BorderLayout.WEST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("SNACKS", createGrid("snack"));
        tabs.addTab("DRINKS", createGrid("drink"));
        tabs.addTab("COMBOS", createGrid("combo"));

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(redcolor);
        footer.setPreferredSize(new Dimension(0, 75));
        footer.setBorder(new EmptyBorder(0, 25, 0, 25));
        footer.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        footerQtyLabel = new JLabel("0 Items Selected");
        footerQtyLabel.setForeground(TEXT);
        footerQtyLabel.setFont(new Font("Courier New", Font.PLAIN, 13));
        
        totalLabel = new JLabel("RM 0.00");
        totalLabel.setForeground(TEXT);
        totalLabel.setFont(new Font("Courier New", Font.BOLD, 18));

        JButton continueBtn = new JButton("CONTINUE");
        styleButton(continueBtn, redcolor, TEXT);
        continueBtn.setFont(new Font("Courier New", Font.BOLD, 18));

        continueBtn.addActionListener(e -> {
            if (!basket.isEmpty()) {
                updateReceiptText();
                layout.show(mainPanel, "CHECKOUT");
            } else {
                // ADD THIS: Pop-out alert when basket is empty
                JOptionPane.showMessageDialog(frame,
                        "Your cart is empty! Please select at least one item before proceeding.",
                        "No Items Selected",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // LEFT: items
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        footer.add(footerQtyLabel, gbc);

        // CENTER: total
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        footer.add(totalLabel, gbc);

        // RIGHT: continue
        gbc.gridx = 2;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        footer.add(continueBtn, gbc);

        
        menuView.add(header, BorderLayout.NORTH);
        menuView.add(tabs, BorderLayout.CENTER);
        menuView.add(footer, BorderLayout.SOUTH);
    }

    private void loadStock() {
        try (BufferedReader read = new BufferedReader(new FileReader("FnBStock.txt"))) {
            String line;

            while ((line = read.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 3) continue;
                
                String name = parts[0];
                String category = parts[1];
                double price = Double.parseDouble(parts[2]);
                String desc = parts.length > 6 ? parts[6] : "";

                items.add(new FnbItem(name, category, price, desc));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void historyView(){
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(BG);
        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        header.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JButton backBtn = new JButton("< Back");
        styleButton(backBtn, BG, MUTED_TEXT);
        backBtn.setFont(new Font("Courier New", Font.PLAIN, 15));
        backBtn.addActionListener(e -> layout.show(mainPanel, "SELECT_DETAILS"));
        
        JLabel title = new JLabel("Order History");
        title.setForeground(TEXT);
        title.setFont(new Font("Courier New", Font.BOLD, 25));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        
        header.add(backBtn, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG);
        contentPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        loadHistory(contentPanel);
        
        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        
        historyPanel.add(header, BorderLayout.NORTH);
        historyPanel.add(scroll, BorderLayout.CENTER);
        
        mainPanel.add(historyPanel, "HISTORY");
        layout.show(mainPanel, "HISTORY");
    }
    
    private void loadHistory(JPanel panel){
        File file = new File("OrderHistory.txt");
        boolean found = false;
        
        if(file.exists()){
            try(BufferedReader read = new BufferedReader(new FileReader(file))){
                
                 List<List<String>> blocks = new ArrayList<>();
                 List<String> current = null;
                
                String line;
                while((line = read.readLine()) != null){
                    if (line.startsWith("----ORDER----")){
                        current = new ArrayList<>();
                    }else if(line.startsWith("--------------") && current != null){
                        blocks.add(current);
                        current = null;
                    }else if(current != null && !line.trim().isEmpty()){
                        current.add(line.trim());
                    }
                    
                }
                
                Collections.reverse(blocks);
                    
                for(List<String> block : blocks){
                        
                    String orderUser = "";
                    String orderId = "";
                    String cinema = "";
                    String pickup = "";
                    String total = "";
                    String datetime = "";
                    List<String> itemLines = new ArrayList<>();
                        
                        for(String l : block){
                            if (l.startsWith("User: ")) {
                                orderUser = l.substring(6).trim();

                            } else if (l.startsWith("OrderID: ")) {
                                orderId = l.substring(9).trim();
                                
                            } else if (l.startsWith("Cinema: ")) {
                                cinema = l.substring(8).trim();

                            } else if (l.startsWith("Pick Up: ")) {
                                pickup = l.substring(9).trim();

                            } else if (l.startsWith("Total: ")) {
                                total = l.substring(7).trim();

                            } else if (l.startsWith("Date: ")) {
                                datetime = l.substring(6).trim();

                            }else {
                                itemLines.add(l);
                            }
                        }
                        
                        if (!orderUser.equals(username)) continue;
 
                        found = true;
                        panel.add(buildHistoryCard(orderId, datetime, cinema, pickup, itemLines, total));
                        panel.add(Box.createRigidArea(new Dimension(0, 12)));
                    }
                
                
                
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        
        if(!found){
            JLabel empty = new JLabel("No orders found.");
            empty.setForeground(MUTED_TEXT);
            empty.setFont(new Font("Courier New", Font.ITALIC, 14));
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(Box.createRigidArea(new Dimension(0, 40)));
            panel.add(empty);
        }
        
    }
    
    private JPanel buildHistoryCard(String orderId, String date, String cinema, String pickup,
                                    List<String> itemName, String grandTotal) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                // top accent stripe
                g2.setColor(redcolor);
                g2.fillRoundRect(0, 0, getWidth(), 5, 4, 4);
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(440, 150));
        card.setBorder(new EmptyBorder(10, 12, 10, 12));
        
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        JPanel topLeft = new JPanel();
        topLeft.setLayout(new BoxLayout(topLeft, BoxLayout.Y_AXIS));
        topLeft.setOpaque(false);
        
        JLabel orderLbl = new JLabel("Order: " + orderId);
        orderLbl.setForeground(MUTED_TEXT);
        orderLbl.setFont(new Font("Courier New", Font.PLAIN, 11));
 
        JLabel dateLabel = new JLabel(date);
        dateLabel.setForeground(MUTED_TEXT);
        dateLabel.setFont(new Font("Courier New", Font.PLAIN, 12));
 
        JLabel totalLbl = new JLabel(grandTotal);
        totalLbl.setForeground(TEXT);
        totalLbl.setFont(new Font("Courier New", Font.BOLD, 15));
        
        topLeft.add(orderLbl);
        topLeft.add(dateLabel);
        topRow.add(topLeft, BorderLayout.WEST);
        topRow.add(totalLbl, BorderLayout.EAST);
 
        // ── Middle: cinema + pickup ──
        JPanel midRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        midRow.setOpaque(false);
        JLabel cinemaLbl = new JLabel(cinema + "  ·  Pickup: " + pickup);
        cinemaLbl.setForeground(TEXT);
        cinemaLbl.setFont(new Font("Courier New", Font.BOLD, 13));
        midRow.add(cinemaLbl);
 
        // ── Items list (raw lines from the block, e.g. "Caramel Popcorn x 2  (RM 24.00)") ──
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setOpaque(false);
        itemsPanel.setBorder(new EmptyBorder(4, 0, 0, 0));
 
        for (String itemLine : itemName) {
            JLabel il = new JLabel("  • " + itemLine);
            il.setForeground(MUTED_TEXT);
            il.setFont(new Font("Courier New", Font.PLAIN, 12));
            itemsPanel.add(il);
        }
 
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.add(midRow);
        body.add(Box.createRigidArea(new Dimension(0, 6)));
        body.add(itemsPanel);
 
        card.add(topRow, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
 
        return card;
    }

    private void addMenuCard(JPanel parent, String name, double price) {
        prices.put(name, price);
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD);
        card.setPreferredSize(new Dimension(195, 180));
        card.setBorder(new LineBorder(new Color(0x333333), 1));

        JPanel info = new JPanel(new GridLayout(3,1));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(15, 10, 10, 10));
        
        FnbItem currentItem = null;
        
        for(FnbItem i : items){
            if(i.getItemName().equals(name)){
                currentItem = i;
                break;
            }
        }
        
        String formattedDesc = currentItem.getDescription().replace(" + ", "\n");
        
        
        JLabel nl = new JLabel(name, SwingConstants.CENTER);
        nl.setForeground(TEXT);
        nl.setFont(new Font("Courier New", Font.BOLD, 14));
        
        JTextArea descArea = new JTextArea();
        descArea.setText(formattedDesc);
        descArea.setForeground(MUTED_TEXT);
        descArea.setFont(new Font("Courier New", Font.PLAIN, 11));
        descArea.setBackground(CARD);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setFocusable(false);
        descArea.setOpaque(false);
        
        JLabel pl = new JLabel("RM " + String.format("%.2f", price), SwingConstants.CENTER);
        pl.setForeground(TEXT);
        
        info.add(nl);
        info.add(descArea);
        info.add(pl);

        JPanel actionArea = new JPanel(new CardLayout());
        actionArea.setOpaque(false);
        actionArea.setPreferredSize(new Dimension(0, 45));

        JButton addBtn = new JButton("ADD TO CART");
        styleButton(addBtn, INPUT_BG, redcolor);
        addBtn.setBorder(new LineBorder(new Color(0x444444), 1));

        JPanel qtyPanel = new JPanel(new BorderLayout());
        qtyPanel.setBackground(INPUT_BG);
        qtyPanel.setBorder(new LineBorder(new Color(0x444444), 1));

        JButton btnM = new JButton(" - ");
        JButton btnP = new JButton(" + ");
        styleButton(btnM, INPUT_BG, ACCENT);
        styleButton(btnP, INPUT_BG, ACCENT);

        JLabel qLbl = new JLabel("1", SwingConstants.CENTER);
        qLbl.setForeground(TEXT);
        qLbl.setFont(new Font("Courier New", Font.BOLD, 14));

        qtyPanel.add(btnM, BorderLayout.WEST);
        qtyPanel.add(qLbl, BorderLayout.CENTER);
        qtyPanel.add(btnP, BorderLayout.EAST);

        actionArea.add(addBtn, "ADD");
        actionArea.add(qtyPanel, "QTY");

        addBtn.addActionListener(e -> {
            basket.put(name, 1);
            qLbl.setText("1");
            ((CardLayout) actionArea.getLayout()).show(actionArea, "QTY");
            updateFooter();
        });

        btnP.addActionListener(e -> {
            int q = basket.get(name) + 1;
            basket.put(name, q);
            qLbl.setText("" + q);
            updateFooter();
        });

        btnM.addActionListener(e -> {
            int q = basket.get(name) - 1;
            if (q <= 0) {
                basket.remove(name);
                ((CardLayout) actionArea.getLayout()).show(actionArea, "ADD");
            } else {
                basket.put(name, q);
                qLbl.setText("" + q);
            }
            updateFooter();
        });

        card.add(info, BorderLayout.CENTER);
        card.add(actionArea, BorderLayout.SOUTH);
        parent.add(card);
    }
    
    JPanel receiptPanel;

    private void createCheckoutView() {
        checkoutView = new JPanel(new BorderLayout());
        checkoutView.setBackground(BG);
        checkoutView.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        header.setBorder(new EmptyBorder(10, 10, 0, 10));

        JButton backBtn = new JButton("< Back");
        styleButton(backBtn, BG, MUTED_TEXT);
        backBtn.setFont(new Font("Courier New", Font.PLAIN, 14));

        backBtn.addActionListener(e -> layout.show(mainPanel, "MENU"));
        
        JLabel title = new JLabel("ORDER SUMMARY");
        title.setForeground(redcolor);
        title.setFont(new Font("Courier New", Font.BOLD, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        header.add(backBtn, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        header.add(Box.createHorizontalStrut(60), BorderLayout.EAST);
        
        receiptPanel = new JPanel();
        receiptPanel.setLayout(new BoxLayout(receiptPanel, BoxLayout.Y_AXIS));
        receiptPanel.setBackground(BG);

        JScrollPane scroll = new JScrollPane(receiptPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);

        JButton payBtn = new JButton("CHECKOUT");
        styleButton(payBtn, redcolor, TEXT);
        payBtn.setFont(new Font("Courier New", Font.BOLD, 16));
        payBtn.setPreferredSize(new Dimension(0, 55));
        payBtn.addActionListener(e -> {
            frame.dispose();
            new FoodPayment(basket, prices, selCinema, selTime, username);
                });


        JPanel btnPanel = new JPanel(new BorderLayout(0, 10));
        btnPanel.setOpaque(false);
        btnPanel.add(payBtn, BorderLayout.CENTER);
        
        checkoutView.add(header, BorderLayout.NORTH);
        checkoutView.add(scroll, BorderLayout.CENTER);
        checkoutView.add(btnPanel, BorderLayout.SOUTH);
    }

    private void updateReceiptText() {
        
        receiptPanel.removeAll();
        
        JPanel infoBoxWrapper = new JPanel();
        infoBoxWrapper.setBackground(BG);
        infoBoxWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        
        JPanel infoBox = new JPanel();
        infoBox.setLayout(new BoxLayout(infoBox, BoxLayout.Y_AXIS));
        infoBox.setBackground(CARD);
        infoBox.setPreferredSize(new Dimension(260, 70));
        infoBox.setBorder(new EmptyBorder(12, 15, 12, 15));
        
        JLabel cinemaLbl = new JLabel("Cinema: " + selCinema);
        cinemaLbl.setForeground(TEXT);
        cinemaLbl.setFont(new Font("Courier New", Font.BOLD, 13));

        JLabel timeLbl = new JLabel("Time: " + selTime);
        timeLbl.setForeground(TEXT);
        timeLbl.setFont(new Font("Courier New", Font.PLAIN, 12));
        
        infoBox.add(cinemaLbl);
        infoBox.add(Box.createRigidArea(new Dimension(0, 6)));
        infoBox.add(timeLbl);
        infoBoxWrapper.add(infoBox);

        receiptPanel.add(infoBoxWrapper);
        receiptPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        double grandTotal = 0.0;
        for (String name : basket.keySet()) {
            int qty = basket.get(name);
            double price = prices.get(name);
            double subtotal = qty * price;
            grandTotal += subtotal;
            
            JPanel row = new JPanel(new GridLayout(1, 3, 20, 0));
            row.setOpaque(false);

            // name
            JLabel nameLbl = new JLabel(name);
            nameLbl.setForeground(TEXT);

            // qty (center)
            JLabel qtyLbl = new JLabel("" + qty, SwingConstants.CENTER);
            qtyLbl.setForeground(TEXT);

            // price (right)
            JLabel priceLbl = new JLabel("RM " + String.format("%.2f", subtotal), SwingConstants.RIGHT);
            priceLbl.setForeground(TEXT);

            row.add(nameLbl);
            row.add(qtyLbl);
            row.add(priceLbl);
            
            row.setBorder(new EmptyBorder(5, 0, 5, 0));
            receiptPanel.add(row);
            receiptPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        }

        // ===== TOTAL =====
        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(false);

        JLabel totalText = new JLabel("TOTAL");
        totalText.setForeground(TEXT);

        JLabel totalValue = new JLabel("RM " + String.format("%.2f", grandTotal));
        totalValue.setForeground(TEXT);
        totalValue.setFont(new Font("Courier New", Font.BOLD, 18));

        totalRow.add(totalText, BorderLayout.WEST);
        totalRow.add(totalValue, BorderLayout.EAST);

        receiptPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        receiptPanel.add(totalRow);

        receiptPanel.revalidate();
        receiptPanel.repaint();
        
    }

    private JScrollPane createGrid(String type) {
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        container.setBackground(BG);
        container.setPreferredSize(new Dimension(420, 650));

        for (FnbItem item : items) {
            if ((type.equals("snack") && item.getCategory().equalsIgnoreCase("Snacks"))
                    || (type.equals("drink") && item.getCategory().equalsIgnoreCase("Drinks"))
                    || (type.equals("combo") && item.getCategory().equalsIgnoreCase("Combo Deals"))) {

                addMenuCard(container, item.getItemName(), item.getPrice());
            }
        }

        JScrollPane sp = new JScrollPane(container);
        sp.setBorder(null);
        return sp;
    }

    private void styleButton(JButton b, Color bg, Color fg) {
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
    }

    private void updateFooter() {
        double total = 0;
        int count = 0;
        for (String name : basket.keySet()) {
            total += (prices.get(name) * basket.get(name));
            count += basket.get(name);
        }
        footerQtyLabel.setText(count + " Items");
        totalLabel.setText("RM " + String.format("%.2f", total));
    }

    private String[] getNextSevenDays() {
        String[] dates = new String[7];
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM");
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            dates[i] = sdf.format(cal.getTime());
            cal.add(Calendar.DATE, 1);
        }
        return dates;
    }
    
}
