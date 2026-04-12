package cinema;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.io.*;

public class Concession {

    JFrame frame;
    JPanel mainPanel, menuView, checkoutView, selectionView;
    CardLayout layout;
    JButton backButton;

    // UI Theme
    final Color BG = new Color(0x121212), CARD = new Color(0x1E1E1E), ACCENT = new Color(0xFFD700),panelcolor = new Color(0x2E2E2E);
    final Color TEXT = Color.WHITE, MUTED_TEXT = new Color(0xAAAAAA), INPUT_BG = new Color(0x2A2A2A), redcolor = new Color(0xD44444);
    ;
    final Dimension UI_ELEMENT_SIZE = new Dimension(350, 45);

    JLabel totalLabel, footerQtyLabel, receiptLabel;
    Map<String, Integer> basket = new HashMap<>();
    Map<String, Double> prices = new HashMap<>();
    String selRegion, selCinema, selTime;
    
    String username;

    ArrayList<fnbitem> items = new ArrayList<>();

    public Concession() {
        frame = new JFrame("TGC Cinema - Concession F&B");
        frame.setSize(500, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        layout = new CardLayout();
        mainPanel = new JPanel(layout);
        
        backButton = new JButton("< Back");
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusable(false);
        backButton.setBounds(8,10,400,40);
        backButton.setFont(new Font("Courier New",Font.PLAIN,17));
        backButton.setHorizontalAlignment(JButton.LEFT);
        backButton.addActionListener(e -> backmenu());
        backButton.setForeground(new Color(0xF7F7F7));
        frame.add(backButton);

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
        JComboBox<String> typeBox = createStyledCombo(new String[]{"Collect Today", "Schedule for Later"});
        JComboBox<String> dateBox = createStyledCombo(getNextSevenDays());
        dateBox.setVisible(false);

        typeBox.addActionListener(e -> {
            dateBox.setVisible(typeBox.getSelectedIndex() == 1);
            selectionView.revalidate();
            selectionView.repaint();
        });

        JButton nextBtn = new JButton("START ORDERING");
        styleButton(nextBtn, redcolor, TEXT);
        nextBtn.setFont(new Font("Courier New", Font.BOLD, 16));
        nextBtn.setMaximumSize(new Dimension(350, 52));
        nextBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextBtn.setBorder(new EmptyBorder(10, 20, 10, 20));

        nextBtn.addActionListener(e -> {
            selRegion = (String) regionBox.getSelectedItem();
            selCinema = (String) cinemaBox.getSelectedItem();
            selTime = typeBox.getSelectedIndex() == 0 ? "Today" : (String) dateBox.getSelectedItem();
            layout.show(mainPanel, "MENU");
        });
        
        JButton historybtn = new JButton("ORDER HISTORY");
        styleButton(historybtn, panelcolor, TEXT);
        historybtn.setFont(new Font("Courier New", Font.BOLD, 16));
        historybtn.setMaximumSize(new Dimension(350, 52));
        historybtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        historybtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        historybtn.addActionListener(e -> historyview());
        
        selectionView.add(title);
        selectionView.add(Box.createRigidArea(new Dimension(0, 4)));
        selectionView.add(sub);
        selectionView.add(Box.createRigidArea(new Dimension(0, 20)));
        selectionView.add(sep);
        selectionView.add(Box.createRigidArea(new Dimension(0, 28)));
        addInputGroup(selectionView, "SELECT REGION", regionBox);
        addInputGroup(selectionView, "SELECT CINEMA", cinemaBox);
        addInputGroup(selectionView, "PICKUP TIME", typeBox);
        selectionView.add(dateBox);
        selectionView.add(Box.createVerticalGlue());
        selectionView.add(nextBtn);
        selectionView.add(Box.createRigidArea(new Dimension(0, 12)));
        selectionView.add(historybtn);
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
        
        footerQtyLabel = new JLabel("0 Items Selected");
        footerQtyLabel.setForeground(TEXT);
        footerQtyLabel.setFont(new Font("Courier New", Font.PLAIN, 13));
        
        totalLabel = new JLabel("RM 0.00");
        totalLabel.setForeground(TEXT);
        totalLabel.setFont(new Font("Courier New", Font.BOLD, 18));

        footer.add(footerQtyLabel, BorderLayout.WEST);
        footer.add(totalLabel, BorderLayout.EAST);
        footer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!basket.isEmpty()) {
                    updateReceiptText();
                    layout.show(mainPanel, "CHECKOUT");
                }
            }
        });

        menuView.add(header, BorderLayout.NORTH);
        menuView.add(tabs, BorderLayout.CENTER);
        menuView.add(footer, BorderLayout.SOUTH);
    }

    private void loadstock() {
        try (BufferedReader read = new BufferedReader(new FileReader("FnBStock.txt"))) {
            String line;

            while ((line = read.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 3) continue;
                
                String name = parts[0];
                String category = parts[1];
                double price = Double.parseDouble(parts[2]);
                String desc = parts.length > 6 ? parts[6] : "";

                items.add(new fnbitem(name, category, price, desc));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void historyview(){
        JPanel historypanel = new JPanel(new BorderLayout());
        historypanel.setBackground(BG);
        
        JLabel title = new JLabel("Order History");
        title.setForeground(TEXT);
        title.setFont(new Font("Courier New", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG);
        contentPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        loadhistory(contentPanel);
        
        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        JButton backbtn = new JButton("< Back");
        styleButton(backbtn, BG, MUTED_TEXT);
        backbtn.setFont(new Font("Courier New", Font.PLAIN, 15));
        backbtn.addActionListener(e -> layout.show(mainPanel, "MENU"));
        
        historypanel.add(title, BorderLayout.CENTER);
        historypanel.add(scroll, BorderLayout.CENTER);
        historypanel.add(backbtn, BorderLayout.SOUTH);
        
        mainPanel.add(historypanel, "HISTORY");
        layout.show(mainPanel, "HISTORY");
    }
    
    private void loadhistory(JPanel panel){
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
                    Collections.reverse(blocks);
                    
                    for(List<String> block : blocks){
                        
                        String orderuser = "";
                        String cinema = "";
                        String pickup = "";
                        String Item = "";
                        String total = "";
                        String datetime = "";
                        
                        for(String l : block){
                            if(l.startsWith("User: ")){
                                orderuser = l.substring(6).trim();
                            }else if(l.startsWith("Cinema: ")){
                                cinema = l.substring(8).trim();
                            }else if(l.startsWith("Pick Up: ")){
                                pickup = l.substring(9).trim();
                            }else if(l.startsWith("Item(s): ")){
                                Item = l.substring(9).trim();
                            }else if(l.startsWith("Total: ")){
                                total = l.substring(7).trim();
                            }else if(l.startsWith("Date: ")){
                                datetime = l.substring(6).trim();
                            }
                        }
                        
                        if (!orderuser.equals(username)) continue;
 
                        found = true;
                        panel.add(buildHistoryCard(datetime, cinema, pickup, Item, total));
                        panel.add(Box.createRigidArea(new Dimension(0, 12)));
                    }
                    
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
    
    private JPanel buildHistoryCard(String date, String cinema, String pickup,
                                    String item, String grandTotal) {
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
        card.setMaximumSize(new Dimension(440, Integer.MAX_VALUE));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));
 
        // ── Top row: date + total ──
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
 
        JLabel dateLabel = new JLabel(date);
        dateLabel.setForeground(MUTED_TEXT);
        dateLabel.setFont(new Font("Courier New", Font.PLAIN, 12));
 
        JLabel totalLbl = new JLabel(grandTotal);
        totalLbl.setForeground(ACCENT);
        totalLbl.setFont(new Font("Courier New", Font.BOLD, 15));
 
        topRow.add(dateLabel, BorderLayout.WEST);
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
        itemsPanel.setBorder(new EmptyBorder(6, 0, 0, 0));
 
        for (String itemLine : item.split(",")) {
            JLabel il = new JLabel("  • " + itemLine);
            il.setForeground(MUTED_TEXT);
            il.setFont(new Font("Courier New", Font.PLAIN, 12));
            itemsPanel.add(il);
        }
 
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.add(midRow);
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

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(15, 10, 10, 10));
        JLabel nl = new JLabel(name, SwingConstants.CENTER);
        nl.setForeground(TEXT);
        nl.setFont(new Font("Courier New", Font.BOLD, 14));
        JLabel pl = new JLabel("RM " + String.format("%.2f", price), SwingConstants.CENTER);
        pl.setForeground(ACCENT);
        info.add(nl);
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

    private void createCheckoutView() {
        checkoutView = new JPanel(new BorderLayout());
        checkoutView.setBackground(BG);
        checkoutView.setBorder(new EmptyBorder(30, 30, 30, 30));

        receiptLabel = new JLabel();
        receiptLabel.setVerticalAlignment(SwingConstants.TOP);
        JScrollPane scroll = new JScrollPane(receiptLabel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);

        JButton payBtn = new JButton("PAY NOW");
        styleButton(payBtn, ACCENT, Color.BLACK);
        payBtn.setFont(new Font("Courier New", Font.BOLD, 16));
        payBtn.setPreferredSize(new Dimension(0, 55));
        payBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Order Successful!");
            System.exit(0);
        });

        JButton backBtn = new JButton("< Back");
        styleButton(backBtn, BG, MUTED_TEXT);
        backBtn.setPreferredSize(new Dimension(0, 40));
        backBtn.addActionListener(e -> layout.show(mainPanel, "MENU"));

        JPanel btnPanel = new JPanel(new BorderLayout(0, 10));
        btnPanel.setOpaque(false);
        btnPanel.add(payBtn, BorderLayout.CENTER);
        btnPanel.add(backBtn, BorderLayout.SOUTH);

        checkoutView.add(scroll, BorderLayout.CENTER);
        checkoutView.add(btnPanel, BorderLayout.SOUTH);
    }

    private void updateReceiptText() {
        StringBuilder sb = new StringBuilder("<html><body style='width: 310px; color:white; font-family:sans-serif;'>");
        sb.append("<h2 style='color:#FFD700;'>ORDER SUMMARY</h2>");
        sb.append("<div style='background-color:#1E1E1E; border: 1px solid #333; padding:12px; margin:15px 0;'>");
        sb.append("<small style='color:#AAAAAA;'>CINEMA</small><br><b>").append(selCinema).append("</b><br>");
        sb.append("<small style='color:#AAAAAA;'>TIME</small><br><b>").append(selTime).append("</b></div>");

        sb.append("<table style='width:100%;'>");
        double grandTotal = 0;
        for (String name : basket.keySet()) {
            int qty = basket.get(name);
            double price = prices.get(name);
            double subtotal = qty * price;
            grandTotal += subtotal;
            sb.append("<tr><td style='padding:8px 0;'><b>").append(name).append("</b><br>")
                    .append("<small style='color:#AAAAAA;'>").append(qty).append(" x RM ").append(String.format("%.2f", price)).append("</small></td>")
                    .append("<td style='text-align:right; color:#FFD700;'>RM ").append(String.format("%.2f", subtotal)).append("</td></tr>");
        }
        sb.append("</table><hr style='border:0; border-top:1px solid #444; margin:15px 0;'>");
        sb.append("<div style='text-align:right;'>GRAND TOTAL<br><b style='color:#FFD700; font-size:24px;'>RM ")
                .append(String.format("%.2f", grandTotal)).append("</b></div></body></html>");
        receiptLabel.setText(sb.toString());
    }

    private JScrollPane createGrid(String type) {
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        container.setBackground(BG);
        container.setPreferredSize(new Dimension(420, 650));

        for (fnbitem item : items) {
            if ((type.equals("snack") && item.category.equalsIgnoreCase("Snacks"))
                    || (type.equals("drink") && item.category.equalsIgnoreCase("Drinks"))
                    || (type.equals("combo") && item.category.equalsIgnoreCase("Combo Deals"))) {

                addMenuCard(container, item.itemname, item.price);
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
    
    
    private void backmenu(){
        frame.dispose();
        new MainMenuPage(username);
    }

//    public static void main(String[] args) {
//        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch(Exception e){}
//        new Concession();
//    }
}

class fnbitem {

    String itemname, category, desc;
    double price;

    public fnbitem(String itemname, String category, double price, String desc) {
        this.itemname = itemname;
        this.category = category;
        this.price = price;
        this.desc = desc;
    }
}
