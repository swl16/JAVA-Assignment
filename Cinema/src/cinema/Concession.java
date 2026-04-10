package cinema;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class Concession {
    JFrame frame;
    JPanel mainPanel, menuView, checkoutView, selectionView;
    CardLayout layout;

    // UI Theme
    final Color BG = new Color(0x121212), CARD = new Color(0x1E1E1E), ACCENT = new Color(0xFFD700);
    final Color TEXT = Color.WHITE, MUTED_TEXT = new Color(0xAAAAAA), INPUT_BG = new Color(0x2A2A2A);
    final Dimension UI_ELEMENT_SIZE = new Dimension(350, 45);

    JLabel totalLabel, footerQtyLabel, receiptLabel;
    Map<String, Integer> basket = new HashMap<>();
    Map<String, Double> prices = new HashMap<>();
    String selRegion, selCinema, selTime;

    public Concession() {
        frame = new JFrame("TGC Cinema - Concession");
        frame.setSize(480, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        JLabel title = new JLabel("Collection Details");
        title.setForeground(ACCENT);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JComboBox<String> regionBox = createStyledCombo(new String[]{"Klang Valley"});
        JComboBox<String> cinemaBox = createStyledCombo(new String[]{"TGC Mid Valley"});
        JComboBox<String> typeBox = createStyledCombo(new String[]{"Collect Today", "Schedule for Later"});
        JComboBox<String> dateBox = createStyledCombo(getNextSevenDays());
        dateBox.setVisible(false);

        typeBox.addActionListener(e -> {
            dateBox.setVisible(typeBox.getSelectedIndex() == 1);
            selectionView.revalidate();
            selectionView.repaint();
        });

        JButton nextBtn = new JButton("START ORDERING");
        styleButton(nextBtn, ACCENT, Color.BLACK);
        nextBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        nextBtn.setMaximumSize(new Dimension(350, 55));
        nextBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        nextBtn.addActionListener(e -> {
            selRegion = (String) regionBox.getSelectedItem();
            selCinema = (String) cinemaBox.getSelectedItem();
            selTime = typeBox.getSelectedIndex() == 0 ? "Today" : (String) dateBox.getSelectedItem();
            layout.show(mainPanel, "MENU");
        });

        selectionView.add(title);
        selectionView.add(Box.createRigidArea(new Dimension(0, 50)));
        addInputGroup(selectionView, "SELECT REGION", regionBox);
        addInputGroup(selectionView, "SELECT CINEMA", cinemaBox);
        addInputGroup(selectionView, "PICKUP TIME", typeBox);
        selectionView.add(dateBox);
        selectionView.add(Box.createVerticalGlue());
        selectionView.add(nextBtn);
    }

    private JComboBox<String> createStyledCombo(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBackground(INPUT_BG);
        combo.setForeground(TEXT);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        combo.setBorder(new LineBorder(new Color(0x444444), 1));
        combo.setMaximumSize(UI_ELEMENT_SIZE);
        combo.setPreferredSize(UI_ELEMENT_SIZE);
        combo.setAlignmentX(Component.CENTER_ALIGNMENT);
        return combo;
    }

    private void addInputGroup(JPanel p, String label, JComponent combo) {
        JLabel l = new JLabel(label);
        l.setForeground(MUTED_TEXT);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
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
        JButton backBtn = new JButton(" ← Back to Details");
        styleButton(backBtn, BG, MUTED_TEXT);
        backBtn.addActionListener(e -> layout.show(mainPanel, "SELECT_DETAILS"));
        header.add(backBtn, BorderLayout.WEST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("SNACKS", createGrid("snack"));
        tabs.addTab("DRINKS", createGrid("drink"));
        tabs.addTab("COMBOS", createGrid("combo"));

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(ACCENT);
        footer.setPreferredSize(new Dimension(0, 75));
        footer.setBorder(new EmptyBorder(0, 25, 0, 25));
        footerQtyLabel = new JLabel("0 Items Selected");
        totalLabel = new JLabel("RM 0.00");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        footer.add(footerQtyLabel, BorderLayout.WEST);
        footer.add(totalLabel, BorderLayout.EAST);
        footer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if(!basket.isEmpty()) { updateReceiptText(); layout.show(mainPanel, "CHECKOUT"); }
            }
        });

        menuView.add(header, BorderLayout.NORTH);
        menuView.add(tabs, BorderLayout.CENTER);
        menuView.add(footer, BorderLayout.SOUTH);
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
        nl.setFont(new Font("SansSerif", Font.BOLD, 14));
        JLabel pl = new JLabel("RM " + String.format("%.2f", price), SwingConstants.CENTER);
        pl.setForeground(ACCENT);
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
            basket.put(name, 1);
            qLbl.setText("1");
            ((CardLayout)actionArea.getLayout()).show(actionArea, "QTY");
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
                ((CardLayout)actionArea.getLayout()).show(actionArea, "ADD");
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
        scroll.setOpaque(false); scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);

        JButton payBtn = new JButton("PAY NOW");
        styleButton(payBtn, ACCENT, Color.BLACK);
        payBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        payBtn.setPreferredSize(new Dimension(0, 55));
        payBtn.addActionListener(e -> { JOptionPane.showMessageDialog(frame, "Order Successful!"); System.exit(0); });

        JButton backBtn = new JButton("← Back to Menu");
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
        if (type.equals("snack")) { addMenuCard(container, "Popcorn", 15.0); addMenuCard(container, "Nuggets", 12.0); }
        else if (type.equals("drink")) { addMenuCard(container, "Coke", 8.0); }
        else { addMenuCard(container, "Combo A", 22.0); }
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

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch(Exception e){}
        new Concession();
    }
}