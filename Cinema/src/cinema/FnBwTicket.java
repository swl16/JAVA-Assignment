package cinema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FnBwTicket {
    JFrame frame = new JFrame("TGC Cinema - Select Combo");
    JPanel mainPanel, menuView, checkoutView;
    JLabel foodTypeLabel, foodPriceLabel, totalPriceLabel;
    CardLayout layout;
    UserOrder order;
    JFrame homeFrame;

    final Color BG = new Color(0x242424), CARD = new Color(0x1E1E1E), ACCENT = new Color(0xD44444);
    final Color TEXT = Color.WHITE, MUTED_TEXT = new Color(0xAAAAAA), INPUT_BG = new Color(0x2A2A2A);
    final Dimension UI_ELEMENT_SIZE = new Dimension(350, 45);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("EEE dd MMM , HH:mm");
    String[] seatType = {"Adult","Student","Senior","OKU"};

//    JLabel totalLabel, footerQtyLabel, receiptLabel;
    JButton confirmButton;
//    Map<String, Integer> basket = new HashMap<>();
//    Map<String, Double> prices = new HashMap<>();
    ArrayList<fnbitem> items = new ArrayList<>();

    FnBwTicket(JFrame homeFrame,UserOrder order){
        this.homeFrame = homeFrame;
        this.order = order;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(500,700);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(0x242424));
        frame.setVisible(true);

//        JButton backButton = new JButton("< Back");
//        backButton.setBorderPainted(false);
//        backButton.setFocusPainted(false);
//        backButton.setContentAreaFilled(false);
//        backButton.setFocusable(false);
//        backButton.setBounds(8,10,150,30);
//        backButton.setFont(new Font("Courier New",Font.PLAIN,17));
//        backButton.setHorizontalAlignment(JButton.LEFT);
//        backButton.addActionListener(e -> {
//            frame.dispose();
//            homeFrame.setVisible(true);});
//        backButton.setForeground(new Color(0xF7F7F7));
//        frame.add(backButton);

        layout = new CardLayout();
        mainPanel = new JPanel(layout);

        loadstock();
        createMenuView();
        createCheckoutView();

        mainPanel.add(menuView, "MENU");
        mainPanel.add(checkoutView, "CHECKOUT");

        frame.add(mainPanel);
    }

    private void createMenuView() {
//        menuView.removeAll();
//        menuView.revalidate();
//        menuView.repaint();
        menuView = new JPanel(new BorderLayout());
        menuView.setBackground(BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        header.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton backBtn = new JButton("< Back");
        styleButton(backBtn, BG, MUTED_TEXT);
        backBtn.addActionListener(e -> {
            frame.dispose();
            homeFrame.setVisible(true);});

        JLabel titleLabel = new JLabel("Select Combo", SwingConstants.CENTER);
        titleLabel.setForeground(new Color(0xF7F7F7));
        titleLabel.setFont(new Font("Courier New",Font.BOLD,20));
//        titleLabel.setBounds(165,20,200,20);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(BG);
        centerPanel.add(titleLabel);

        header.add(backBtn, BorderLayout.WEST);
        header.add(centerPanel, BorderLayout.CENTER);

        JPanel placeholder = new JPanel();
        placeholder.setBackground(BG);
        placeholder.setPreferredSize(backBtn.getPreferredSize());
        header.add(placeholder, BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("SNACKS", createGrid("snack"));
        tabs.addTab("DRINKS", createGrid("drink"));
        tabs.addTab("COMBOS", createGrid("combo"));

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG);
        footer.setPreferredSize(new Dimension(0, 75));
        footer.setBorder(new EmptyBorder(20, 25, 20, 25));

        confirmButton = new JButton("Total - 0 Item(s)                 RM 0.00");
        styleButton(confirmButton, ACCENT, TEXT);
        confirmButton.addActionListener(e -> { updateReceiptText(); layout.show(mainPanel, "CHECKOUT"); });
        confirmButton.setFont(new Font("Courier New",Font.BOLD,15));
        footer.add(confirmButton,BorderLayout.CENTER);

//        footerQtyLabel = new JLabel("0 Items Selected");
//        totalLabel = new JLabel("RM 0.00");
//        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
//
//        footer.add(footerQtyLabel, BorderLayout.WEST);
//        footer.add(totalLabel, BorderLayout.EAST);
//        footer.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent e) {
//                if (!basket.isEmpty()) { updateReceiptText(); layout.show(mainPanel, "CHECKOUT"); }
//            }
//        });

        menuView.add(header, BorderLayout.NORTH);
        menuView.add(tabs, BorderLayout.CENTER);
        menuView.add(footer, BorderLayout.SOUTH);
    }

    private void addMenuCard(JPanel parent, fnbitem item) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD);
        card.setPreferredSize(new Dimension(195, 180));
        card.setBorder(new LineBorder(new Color(0x333333), 1));

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(15, 10, 10, 10));
        JLabel nl = new JLabel(item.getItemname(), SwingConstants.CENTER);
        nl.setForeground(TEXT);
        nl.setFont(new Font("SansSerif", Font.BOLD, 14));
        JLabel pl = new JLabel("RM " + String.format("%.2f", item.getPrice()), SwingConstants.CENTER);
        pl.setForeground(TEXT);
        info.add(nl); info.add(pl);

        JPanel actionArea = new JPanel(new CardLayout());
        actionArea.setOpaque(false);
        actionArea.setPreferredSize(new Dimension(0, 45));

        JButton addBtn = new JButton("ADD TO CART");
        styleButton(addBtn, INPUT_BG, ACCENT);
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
        qLbl.setFont(new Font("SansSerif", Font.BOLD, 14));

        qtyPanel.add(btnM, BorderLayout.WEST);
        qtyPanel.add(qLbl, BorderLayout.CENTER);
        qtyPanel.add(btnP, BorderLayout.EAST);

        actionArea.add(addBtn, "ADD");
        actionArea.add(qtyPanel, "QTY");

        addBtn.addActionListener(e -> {
            order.addFoodItem(item,1);
            //basket.put(name, 1);
            qLbl.setText("1");
            ((CardLayout)actionArea.getLayout()).show(actionArea, "QTY");
            updateFooter();
        });

        btnP.addActionListener(e -> {
            int q = order.getSelectedFood().get(item) + 1;
            order.addFoodItem(item,q);
            qLbl.setText("" + q);
            updateFooter();
        });

        btnM.addActionListener(e -> {
            int q = order.getSelectedFood().get(item) - 1;
            if (q <= 0) {
                order.removeFood(item);
                ((CardLayout)actionArea.getLayout()).show(actionArea, "ADD");
            } else {
                order.addFoodItem(item, q);
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
//        checkoutView.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel upperPanel = new JPanel();
        upperPanel.setBackground(BG);
        upperPanel.setLayout(null);
        upperPanel.setPreferredSize(new Dimension(500, 50));
        checkoutView.add(upperPanel,BorderLayout.NORTH);

        JButton backButton = new JButton("< Back");
        styleButton(backButton,BG,TEXT);
        backButton.setContentAreaFilled(false);
        backButton.setFocusable(false);
        backButton.setBounds(8,10,150,30);
        backButton.setFont(new Font("Courier New",Font.PLAIN,17));
        backButton.setHorizontalAlignment(JButton.LEFT);
        backButton.addActionListener(e -> layout.show(mainPanel, "MENU"));
        upperPanel.add(backButton);

        JLabel titleLabel = new JLabel("REVIEW SUMMARY");
        titleLabel.setForeground(TEXT);
        titleLabel.setFont(new Font("Courier New", Font.BOLD, 22));
        titleLabel.setBounds(165,20,200,20);
        upperPanel.add(titleLabel);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(BG);
        centerPanel.setLayout(null);
//        centerPanel.setPreferredSize(new Dimension(500, 180));

//        receiptLabel = new JLabel();
//        receiptLabel.setVerticalAlignment(SwingConstants.TOP);
//        JScrollPane scroll = new JScrollPane(centerPanel);
//        scroll.setOpaque(false);
//        scroll.getViewport().setOpaque(false);
//        scroll.setBorder(null);

        JLabel movieLabel = new JLabel("Movie");
        movieLabel.setForeground(MUTED_TEXT);
        movieLabel.setFont(new Font("Courier New", Font.BOLD, 14));
        movieLabel.setBounds(10,10,200,10);
        centerPanel.add(movieLabel);

        JLabel movieNameLabel = new JLabel(order.getShowTime().getMovieName());
        movieNameLabel.setForeground(TEXT);
        movieNameLabel.setFont(new Font("Courier New", Font.BOLD, 16));
        movieNameLabel.setBounds(10,22,200,20);
        centerPanel.add(movieNameLabel);

        JLabel locationLabel = new JLabel("Cinema");
        locationLabel.setForeground(MUTED_TEXT);
        locationLabel.setFont(new Font("Courier New", Font.BOLD, 14));
        locationLabel.setBounds(10,60,200,10);
        centerPanel.add(locationLabel);

        JLabel locationNameLabel = new JLabel("Mit Valley Megamall");
        locationNameLabel.setForeground(TEXT);
        locationNameLabel.setFont(new Font("Courier New", Font.BOLD, 16));
        locationNameLabel.setBounds(10,72,400,20);
        centerPanel.add(locationNameLabel);

        JLabel timeLabel = new JLabel("Time");
        timeLabel.setForeground(MUTED_TEXT);
        timeLabel.setFont(new Font("Courier New", Font.BOLD, 14));
        timeLabel.setBounds(10,110,200,10);
        centerPanel.add(timeLabel);

        JLabel timeNameLabel = new JLabel(order.getShowTime().getStartTime().format(FMT));
        timeNameLabel.setForeground(TEXT);
        timeNameLabel.setFont(new Font("Courier New", Font.BOLD, 16));
        timeNameLabel.setBounds(10,122,400,20);
        centerPanel.add(timeNameLabel);

        JLabel hallLabel = new JLabel("Hall");
        hallLabel.setForeground(MUTED_TEXT);
        hallLabel.setFont(new Font("Courier New", Font.BOLD, 14));
        hallLabel.setBounds(10,160,200,10);
        centerPanel.add(hallLabel);

        JLabel hallNameLabel = new JLabel(order.getShowTime().getHallName());
        hallNameLabel.setForeground(TEXT);
        hallNameLabel.setFont(new Font("Courier New", Font.BOLD, 16));
        hallNameLabel.setBounds(10,172,400,20);
        centerPanel.add(hallNameLabel);

        JLabel seatLabel = new JLabel("Seat(s)");
        seatLabel.setForeground(MUTED_TEXT);
        seatLabel.setFont(new Font("Courier New", Font.BOLD, 14));
        seatLabel.setBounds(10,210,200,10);
        centerPanel.add(seatLabel);

        StringBuilder sbSeatNo = new StringBuilder();
        for (Seat s : order.getSelectedSeats()){
            sbSeatNo.append(s.getSeatId()).append(" ");
        }

        JLabel seatNameLabel = new JLabel(sbSeatNo.toString());
        seatNameLabel.setForeground(TEXT);
        seatNameLabel.setFont(new Font("Courier New", Font.BOLD, 16));
        seatNameLabel.setBounds(10,222,400,20);
        centerPanel.add(seatNameLabel);

        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setBounds(10,260,460,5);
        separator.setForeground(TEXT);
        centerPanel.add(separator);

        JLabel ticketLabel = new JLabel("Ticket(s)");
        ticketLabel.setForeground(MUTED_TEXT);
        ticketLabel.setFont(new Font("Courier New", Font.BOLD, 14));
        ticketLabel.setBounds(10,280,200,10);
        centerPanel.add(ticketLabel);

        StringBuilder sbType = new StringBuilder();
        int[] countType = order.getSeatTypeCount();
        boolean isFirst = true;
        for (int i = 0 ; i < 4; i++){
            if (countType[i] != 0){
                if (isFirst){
                    sbType.append(seatType[i]).append(" x ").append(countType[i]);
                    isFirst = false;
                }else {
                    sbType.append(", ").append(seatType[i]).append(" x ").append(countType[i]); }
            }
        }

        JLabel ticketTypeLabel = new JLabel(sbType.toString());
        ticketTypeLabel.setForeground(TEXT);
        ticketTypeLabel.setFont(new Font("Courier New", Font.PLAIN, 15));
        ticketTypeLabel.setBounds(10,292,350,20);
        centerPanel.add(ticketTypeLabel);

        JLabel ticketPriceLabel = new JLabel("RM " + String.format("%.2f",order.getTicketTotalPrice()));
        ticketPriceLabel.setForeground(TEXT);
        ticketPriceLabel.setFont(new Font("Courier New", Font.BOLD, 16));
        ticketPriceLabel.setBounds(380,292,100,20);
        centerPanel.add(ticketPriceLabel);

        StringBuilder sbFoodOrder = new StringBuilder();
        isFirst = true;
        double total = 0;
        if (order.getSelectedFood().isEmpty()){
            sbFoodOrder.append("-");
        }else {
            for (fnbitem item : order.getSelectedFood().keySet()) {
                int qty = order.getSelectedFood().get(item);
                total += (item.getPrice() * qty);

                if (isFirst){
                    sbFoodOrder.append(item.getItemname()).append(" x ").append(qty);
                    isFirst = false;
                }else {
                    sbFoodOrder.append(" , ").append(item.getItemname()).append(" x ").append(qty);
                }
            }
        }
        order.setFoodTotalPrice(total);

        JLabel foodLabel = new JLabel("Add-On: Food and Beverage");
        foodLabel.setForeground(MUTED_TEXT);
        foodLabel.setFont(new Font("Courier New", Font.BOLD, 14));
        foodLabel.setBounds(10,330,200,10);
        centerPanel.add(foodLabel);

        foodTypeLabel = new JLabel(sbFoodOrder.toString());
        foodTypeLabel.setForeground(TEXT);
        foodTypeLabel.setFont(new Font("Courier New", Font.BOLD, 15));
        foodTypeLabel.setBounds(10,342,350,20);
        centerPanel.add(foodTypeLabel);

        foodPriceLabel = new JLabel("RM " + String.format("%.2f",order.getFoodTotalPrice()));
        foodPriceLabel.setForeground(TEXT);
        foodPriceLabel.setFont(new Font("Courier New", Font.BOLD, 16));
        foodPriceLabel.setBounds(380,342,100,20);
        centerPanel.add(foodPriceLabel);

        JLabel processingFeeLabel = new JLabel("Processing Fee");
        processingFeeLabel.setForeground(TEXT);
        processingFeeLabel.setFont(new Font("Courier New", Font.BOLD, 16));
        processingFeeLabel.setBounds(10,392,350,20);
        centerPanel.add(processingFeeLabel);

        JLabel processingFeeTotalLabel = new JLabel("RM " + String.format("%.2f",order.calculateProcessingFee()));
        processingFeeTotalLabel.setForeground(TEXT);
        processingFeeTotalLabel.setFont(new Font("Courier New", Font.BOLD, 16));
        processingFeeTotalLabel.setBounds(380,392,100,20);
        centerPanel.add(processingFeeTotalLabel);

        JSeparator separator2 = new JSeparator(JSeparator.HORIZONTAL);
        separator2.setBounds(10,450,460,5);
        separator2.setForeground(TEXT);
        centerPanel.add(separator2);

        JLabel totalLabel = new JLabel("Total");
        totalLabel.setForeground(TEXT);
        totalLabel.setFont(new Font("Courier New", Font.BOLD, 16));
        totalLabel.setBounds(10,480,350,20);
        centerPanel.add(totalLabel);

        totalPriceLabel = new JLabel("RM " + String.format("%.2f",order.calculateTotalPrice()));
        totalPriceLabel.setForeground(TEXT);
        totalPriceLabel.setFont(new Font("Courier New", Font.BOLD, 16));
        totalPriceLabel.setBounds(380,480,100,20);
        centerPanel.add(totalPriceLabel);

        JButton payBtn = new JButton("Checkout & Pay Now");
        styleButton(payBtn, ACCENT, TEXT);
        payBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        payBtn.setPreferredSize(new Dimension(0, 55));
        payBtn.addActionListener(e -> { frame.dispose(); new TicketPayment(order); });

//        JButton backBtn = new JButton("← Back to Menu");
//        styleButton(backBtn, BG, MUTED_TEXT);
//        backBtn.setPreferredSize(new Dimension(0, 40));
//        backBtn.addActionListener(e -> layout.show(mainPanel, "MENU"));

        JPanel btnPanel = new JPanel(new BorderLayout(0, 10));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(25,25,25,25));
        btnPanel.add(payBtn, BorderLayout.CENTER);
        //btnPanel.add(backBtn, BorderLayout.SOUTH);

        checkoutView.add(centerPanel, BorderLayout.CENTER);
        checkoutView.add(btnPanel, BorderLayout.SOUTH);
    }

    private void updateReceiptText() {
        StringBuilder sbFoodOrder = new StringBuilder();
        boolean isFirst = true;
        double total = 0;
        if (order.getSelectedFood().isEmpty()){
            sbFoodOrder.append("-");
        }else {
            for (fnbitem item : order.getSelectedFood().keySet()) {
                int qty = order.getSelectedFood().get(item);
                total += (item.getPrice() * qty);

                if (isFirst){
                    sbFoodOrder.append(item.getItemname()).append(" x ").append(qty);
                    isFirst = false;
                }else {
                    sbFoodOrder.append(" , ").append(item.getItemname()).append(" x ").append(qty);
                }
            }
        }
        order.setFoodTotalPrice(total);

        foodTypeLabel.setText(sbFoodOrder.toString());
        foodPriceLabel.setText("RM " + String.format("%.2f",order.getFoodTotalPrice()));
        totalPriceLabel.setText("RM " + String.format("%.2f",order.calculateTotalPrice()));
    }

    private void loadstock() {
        try (BufferedReader read = new BufferedReader(new FileReader("Cinema/FnBStock.txt"))) {
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

    private JScrollPane createGrid(String type) {
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        container.setBackground(BG);
        container.setPreferredSize(new Dimension(420, 650));

        for (fnbitem item : items) {
            if ((type.equals("snack") && item.category.equalsIgnoreCase("Snacks"))
                    || (type.equals("drink") && item.category.equalsIgnoreCase("Drinks"))
                    || (type.equals("combo") && item.category.equalsIgnoreCase("Combo Deals"))) {

                addMenuCard(container, item);
            }
        }

//        if (type.equals("snack")) { addMenuCard(container, "Popcorn", 15.0); addMenuCard(container, "Nuggets", 12.0); }
//        else if (type.equals("drink")) { addMenuCard(container, "Coke", 8.0); }
//        else { addMenuCard(container, "Combo A", 22.0); }
        JScrollPane sp = new JScrollPane(container);
        sp.setBorder(null);
        return sp;
    }

    private void styleButton(JButton b, Color bg, Color fg) {
        b.setBackground(bg); b.setForeground(fg);
        b.setFocusPainted(false); b.setBorderPainted(false);
    }

    private void updateFooter() {
        double total = 0; int count = 0;
        for (fnbitem item : order.getSelectedFood().keySet()) {
            total += (item.getPrice() * order.getSelectedFood().getOrDefault(item, 0));
            count += order.getSelectedFood().getOrDefault(item, 0);
        }
        confirmButton.setText("Total - " + count + " Item(s)                 RM " + String.format("%.2f", total));
//        footerQtyLabel.setText(count + " Items");
//        totalLabel.setText("RM " + );
    }
}
