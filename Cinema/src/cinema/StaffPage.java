package cinema;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class StaffPage {

    private JFrame frame;
    private JPanel staffpanel;
    private CardLayout cardlayout;
    private JTextField itemname, price, currentqty, minstockqty, combodetails;
    private JComboBox<String> category;
    private JTable checktable;
    private DefaultTableModel checkmodel;
    private JTable reptable;
    private DefaultTableModel repmodel;

    private final Color bgcolor = new Color(0x242424);
    private final Color panelcolor = new Color(0x2E2E2E);
    private final Color bordercolor = new Color(0x444444);
    private final Color textcolor = new Color(0xF7F7F7);
    private final Color textmutedcolor = new Color(0xAAAAAA);
    private final Color cardcolor = new Color(0x363636);
    private final Color redcolor = new Color(0xD44444);
    private final Color inputbg = new Color(0x1E1E1E);
    private final Color greencolor = new Color(0x44AA66);
    private final Color hovercolor = new Color(0xE85555);
    
    private final String fnbfile = "FnBStock.txt";

    public StaffPage(){
        frame = new JFrame("TGC Cinema - Staff Page");
        frame.setSize(550,700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.getContentPane().setBackground(bgcolor);
        frame.setVisible(true);

        cardlayout = new CardLayout();
        staffpanel = new JPanel(cardlayout);
        staffpanel.setBackground(bgcolor);

        staffpanel.add(menu(), "MENU");
        staffpanel.add(addstock(), "Add_stock");
        staffpanel.add(checkstock(), "Check_stock");
        staffpanel.add(replenishstock(), "Replenish_stock");
        
        frame.add(staffpanel);
        frame.setVisible(true);

        cardlayout.show(staffpanel, "MENU");

    }


    private JPanel menu(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgcolor);

        JLabel title = new JLabel("TGC Cinema Staff", SwingConstants.CENTER);
        title.setForeground(textcolor);
        title.setFont(new Font("Courier New", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(35,0,15,0));
        panel.add(title,BorderLayout.NORTH);

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(bgcolor);
        inner.setBorder(new EmptyBorder(20, 40, 20, 40));

        String [][] buttons = {
            {"ADD F&B ITEM","Add_stock"},{"CHECK STOCK", "Check_stock"}, {"REPLENISH STOCK", "Replenish_stock"}};

        for(String[]btn : buttons){
            JButton b = menubtn(btn[0]);
            String card = btn[1];

            b.addActionListener(e -> {cardlayout.show(staffpanel, card);
            if(card.equals("Check_stock")) refreshchecktable();
            if(card.equals("Replenish_stock")) loadreptable();
            });

            inner.add(b);
            inner.add(Box.createVerticalStrut(14));
        }
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(bgcolor);
        bottomPanel.setBorder(new EmptyBorder(20, 40, 30, 40));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        JButton logoutBtn = new JButton("LOGOUT");
        logoutBtn.setMaximumSize(new Dimension(100, 40));
        logoutBtn.setPreferredSize(new Dimension(100, 40));
        logoutBtn.setFont(new Font("Courier New", Font.BOLD, 14));
        logoutBtn.setBackground(redcolor);
        logoutBtn.setForeground(textcolor);
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
        logoutBtn.addActionListener(e -> {
            frame.dispose(); // close admin page
            new LoginPage(); // go back to login
        });

        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(logoutBtn);

        panel.add(inner, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;

    }

    private JButton menubtn(String text){
        JButton btn = new JButton(text);
        btn.setFont(new Font("Courier New", Font.BOLD, 20));
        btn.setForeground(textcolor);
        btn.setBackground(cardcolor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 4, 0, 0, redcolor),
            new EmptyBorder(14, 20, 14, 20)
        ));
        btn.setMaximumSize(new Dimension(400, 60));
        btn.setMinimumSize(new Dimension(400, 60));
        btn.setPreferredSize(new Dimension(400, 60));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bordercolor); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(cardcolor); }
        });
        return btn;
    }
    

    
    private JPanel addstock () {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(bgcolor);
        page.add(sectionHeader("ADD F&B ITEM", "MENU"), BorderLayout.NORTH);
        
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(bgcolor);
        form.setBorder(new EmptyBorder(15,30,15,30));
        
        itemname = formField(form, "Item Name");
        category = formCombo(form, "Category", new String[] {"Snacks","Drinks","Combo Deals"});
        price = formField(form, "Price (RM)");
        
        currentqty = formField(form, "Current Quantity");
        minstockqty = formField(form, "Minimum Stock Quantity");
        
        combodetails = formField(form, "Combo Items (use + to separate)");

        combodetails.setVisible(false);

        category.addActionListener(e -> {
            String selected = category.getSelectedItem().toString();
            combodetails.setVisible(selected.equals("Combo Deals"));
            form.revalidate();
            form.repaint();
        });
        
        JPanel btnrow = new JPanel(new FlowLayout(FlowLayout.CENTER,15,5));
        btnrow.setBackground(bgcolor);
        
        JButton clearbtn = styledButton("CLEAR", false);
        JButton savebtn = styledButton("SAVE", true);
        
        clearbtn.addActionListener(e -> clearAddForm());
        savebtn.addActionListener(e -> savestock());
        
        btnrow.add(clearbtn);
        btnrow.add(savebtn);
        
        form.add(btnrow);
        
        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(bgcolor);
        page.add(scroll, BorderLayout.CENTER);
        
        return page;
    }
    
    private void savestock(){
        String name = itemname.getText().trim();
        if(name.isEmpty()){
            showMsg("Item name is required.",false);
            return;
        }
        
        String ctg = category.getSelectedItem().toString();
        
        String itemprice = price.getText().trim();
        if(!itemprice.matches("\\d+(\\.\\d{1,2})?")){
            showMsg("Price must be a valid number",false);
            return;
        }
        
        String qty = currentqty.getText().trim();
        if(!qty.matches("\\d+")){
            showMsg("Quantity must be a whole number",false);
            return;
        }
        
        String minqty = minstockqty.getText().trim();
        if(!minqty.matches("\\d+")){
            showMsg("Minimum stock must be a whole number",false);
            return;
        }
        
        int quantity = Integer.parseInt(qty);
        int min = Integer.parseInt(minqty);
        
        if(quantity < min){
                int confirm = JOptionPane.showConfirmDialog(frame,"\""+ name +"\" stock ("+ quantity + ") is below the minimum level (" + min + "). \n Save anyway?",
                        "Stock Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(confirm != JOptionPane.YES_OPTION){
                    return;
                }
            }
        
        try(BufferedReader read = new BufferedReader(new FileReader(fnbfile))){
            String line;
            
            while((line = read.readLine()) != null){
                String[] data = line.split("\\|");
                if(data[0].equalsIgnoreCase(name)){
                    showMsg("Item already exists!",false);
                    return;
                }
            }
        }catch(IOException e){
            //File may not exist yet. We will create it on write
        }
        
        String today = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        
        String details = "";
        if(ctg.equals("Combo Deals")){
            details = combodetails.getText().trim();
        }
        
        try(BufferedWriter saveItem = new BufferedWriter(new FileWriter(fnbfile, true))){
            saveItem.write(name + "|" + ctg + "|" + itemprice + "|" + qty + "|" + minqty + "|" + today + "|" + details);
            
            saveItem.newLine();
            showMsg("Item saved successfully", true);
            
            clearAddForm();
            
        }catch(IOException e){
            showMsg("Error saving:" + e.getMessage(),false);
        }
    }

    private JPanel checkstock(){
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(bgcolor);
        page.add(sectionHeader("CHECK STOCK", "MENU"), BorderLayout.NORTH);
        
        JPanel filterbar = new JPanel(new BorderLayout(8,0));
        filterbar.setBackground(panelcolor);
        filterbar.setBorder(new EmptyBorder(8,15,8,15));
        
        JLabel filterlbl = new JLabel("Filter by Category: ");
        filterlbl.setFont(new Font("Courier NEW", Font.BOLD, 12));
        filterlbl.setForeground(textcolor);
        
        JComboBox<String> filtercombo = new JComboBox<>(
            new String[]{"All Categories", "Snacks", "Drinks", "Combo Deals"});
        styleCombo(filtercombo);
        
        JButton filterbtn = styledButton("FILTER",false);
        
        JPanel filterpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        filterpanel.setBackground(panelcolor);
        filterpanel.add(filtercombo);
        filterpanel.add(filterbtn);
        
        filterbar.add(filterlbl, BorderLayout.WEST);
        filterbar.add(filterpanel, BorderLayout.EAST);
        
        String[] cols = {"ITEM NAME","CATEGORY","PRICE(RM)","QUANTITY","MIN STOCK","STATUS","DETAILS"};
        checkmodel = new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r, int c){return false;}
        };
        checktable = new JTable(checkmodel);
        styleTable(checktable);
        
        checktable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean focus,
                    int row, int col){
                
                Component c = super.getTableCellRendererComponent(table, value, isSelected, focus, row, col);
                if(isSelected){
                    setBackground(redcolor);
                    setForeground(textcolor);
                    return c;
                }
                String status = String.valueOf(table.getValueAt(row, 5));
                switch(status){
                    case "OUT OF STOCK":
                        setBackground(new Color(0x5A1A1A));
                        setForeground(redcolor);
                        break;
                    case "LOW":
                        setBackground(new Color(0x3A2E00));
                        setForeground(new Color(0xFFAA33));
                        break;
                    default : 
                        setBackground(bgcolor);
                        setForeground(greencolor);
                        break;
                }
                
                return c;
            }
        });
        
        JScrollPane scroll = new JScrollPane(checktable);
        styleScrollBar(scroll);
        scroll.getViewport().setBackground(bgcolor);
        
        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(panelcolor);
        south.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, bordercolor));
        
        JPanel label = new JPanel(new FlowLayout(FlowLayout.LEFT,16,6));
        label.setBackground(panelcolor);
        label.add(colorlabel("● OK",greencolor));
        label.add(colorlabel("● LOW",new Color(0xFFAA33)));
        label.add(colorlabel("● OUT OF STOCK", redcolor));
        
        JPanel btnrow = new JPanel(new FlowLayout(FlowLayout.CENTER,12,6));
        btnrow.setBackground(panelcolor);
        
        JButton editbtn = styledButton("EDIT",true);
        editbtn.addActionListener(e -> edititem());
        
        
        JButton delete = styledButton("DELETE",false);
        delete.addActionListener(e -> {
            int row = checktable.getSelectedRow();
            if(row<0){
                showMsg("Please select an item",false);
                return;
            }
            String name = checkmodel.getValueAt(row, 0).toString();
            
            int confirm = JOptionPane.showConfirmDialog(frame, "Delete \"" + name + "\" from stock?","Confirm Delete",JOptionPane.YES_NO_OPTION);
            
            if(confirm == JOptionPane.YES_OPTION){
                ArrayList<String[]> fnb = new ArrayList<>();
                
                try (BufferedReader br = new BufferedReader(new FileReader(fnbfile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] details = line.split("\\|", -1);
                        
                        if (details.length != 7) {
                            showMsg("Skipped malformed line: " + line, false);
                            continue;
                        }
                        if (!details[0].equals(name)) {
                            fnb.add(details);
                        }
                    }
                } catch (IOException ex) {
                    showMsg("Error reading FnB: " + ex.getMessage(), false);
                    return;
                }
                
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(fnbfile))) {
                    for (String[] m : fnb) {
                        bw.write(String.join("|", m));
                        bw.newLine();
                    }
                }catch(IOException ex) {
                    showMsg("Error saving movies: " + ex.getMessage(), false);
                    return;
                }
                
                refreshchecktable();
                showMsg("Item deleted successfully!", true);
            }
            
        });
        btnrow.add(editbtn);
        btnrow.add(delete);
        
        south.add(label, BorderLayout.NORTH);
        south.add(btnrow, BorderLayout.CENTER);
        
        filtercombo.addActionListener(e -> {
            String selected = filtercombo.getSelectedItem().toString();
            filterStock(selected);
        });
        
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(bgcolor);
        center.add(filterbar, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        
        page.add(center, BorderLayout.CENTER);
        page.add(south,BorderLayout.SOUTH);
        
        refreshchecktable();
        
        return page;
    }
    
    private void edititem(){
        int row = checktable.getSelectedRow();
        
        if (row < 0) {
            showMsg("Please select an item to edit.", false);
            return;
        }
        
        String selecteditem = checkmodel.getValueAt(row, 0).toString();
        
        String[] itemDetails = null;
        
        File file = new File(fnbfile);
     
        if(!file.exists()){
         showMsg("FnB file not found!",false);
         return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split("\\|", -1);
                if (details.length == 7 && details[0].equals(selecteditem)) {
                    itemDetails = details;
                    break;
                }
            }
        } catch (IOException e) {
            showMsg("Error loading item details: " + e.getMessage(), false);
            return;
        }

        if (itemDetails == null) {
            showMsg("Item details not found.", false);
            return;
        }
        
        JDialog editDialog = new JDialog(frame, "Edit Item", true);
        editDialog.setSize(500, 700);
        editDialog.setLayout(new BorderLayout());
        editDialog.setLocationRelativeTo(frame);
        editDialog.getContentPane().setBackground(bgcolor);
        editDialog.setResizable(false);
        
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(bgcolor);
        form.setBorder(new EmptyBorder(15,30,15,30));
        
        form.add(fieldLabel("Item Name"));
        form.add(Box.createVerticalStrut(4));
        JTextField itemField = new JTextField(itemDetails[0]);
        styleTextField(itemField);
        form.add(itemField);
        form.add(Box.createVerticalStrut(8));
        
        form.add(fieldLabel("Category"));
        form.add(Box.createVerticalStrut(4));
        JComboBox<String> ctg = new JComboBox<>(new String[]{"Snacks","Drinks","Combo Deals"});
        styleCombo(ctg);
        ctg.setSelectedItem(itemDetails[1]);
        form.add(ctg);
        form.add(Box.createVerticalStrut(8));
        
        form.add(fieldLabel("Price (RM)"));
        form.add(Box.createVerticalStrut(4));
        JTextField priceField = new JTextField(itemDetails[2]);
        styleTextField(priceField);
        form.add(priceField);
        form.add(Box.createVerticalStrut(8));
        
        form.add(fieldLabel("Combo Items (use + to separate)"));
        form.add(Box.createVerticalStrut(4));
        JTextField comboField = new JTextField(itemDetails[6]);
        styleTextField(comboField);
        comboField.setVisible("Combo Deals".equals(itemDetails[1]));
        form.add(comboField);
        form.add(Box.createVerticalStrut(8));
        
        comboField.setVisible(false);

        ctg.addActionListener(e -> {
            String selected = ctg.getSelectedItem().toString();
            comboField.setVisible(selected.equals("Combo Deals"));
            form.revalidate();
            form.repaint();
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttonPanel.setBackground(bgcolor);

        JButton cancelButton = styledButton("CANCEL", false);
        JButton saveButton = styledButton("SAVE", true);
        
        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(bgcolor);
        editDialog.add(scroll, BorderLayout.CENTER);
        
        cancelButton.addActionListener(e -> editDialog.dispose());
        
        saveButton.addActionListener(e -> {
            
            String editname = itemField.getText().trim();
            String editctg = ctg.getSelectedItem().toString();
            String editprice = priceField.getText().trim();
            String combo = "Combo Deals".equals(editctg) ? comboField.getText().trim() : "";
            
            if(editname.isEmpty()){
                showMsg("Item name is required.",false);
                return;
            }
            
            if(!editprice.matches("\\d+(\\.\\d{1,2})?")){
                showMsg("Price must be a valid number",false);
                return;
            }
            
            ArrayList<String[]> allitems = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(fnbfile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] details = line.split("\\|", -1);
                        if (details.length == 7) {
                            if (details[0].equals(selecteditem)) {
                            allitems.add(new String[]{
                            editname, editctg, editprice, details[3],
                            details[4],
                            new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()),
                            combo
                            });
                            } else {
                                allitems.add(details);
                            }
                        }
                }
            } catch (IOException ex) {
                showMsg("Error reading items: " + ex.getMessage(), false);
                return;
            }
        
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fnbfile))) {
                for (String[] m : allitems) {
                    bw.write(String.join("|", m));
                    bw.newLine();
                }
            } catch (IOException ex) {
                showMsg("Error saving items: " + ex.getMessage(), false);
                return;
            }
        
        
            refreshchecktable();
            loadreptable();
            showMsg("Item updated successfully!", true);
            editDialog.dispose();
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        form.add(buttonPanel);
        
        editDialog.setVisible(true);
    }
    

    
    private JPanel replenishstock(){
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(bgcolor);
        page.add(sectionHeader("REPLENISH STOCK", "MENU"), BorderLayout.NORTH);
        
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(panelcolor);
        form.setBorder(new EmptyBorder(8,15,8,15));
        
        JLabel formtitle = new JLabel("QUICK REPLENISH - Select a row to replenish.");
        formtitle.setFont(new Font("Courier New", Font.BOLD, 13));
        formtitle.setForeground(redcolor);
        formtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(formtitle);
        form.add(Box.createVerticalStrut(10));
        
        JPanel row = new JPanel(new GridLayout(1,4,8,0));
        row.setBackground(panelcolor);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE,50));
        
        JPanel itempanel = new JPanel(new BorderLayout(0,3));
        itempanel.setBackground(panelcolor);
        
        JLabel itemlabel = new JLabel("Selected Item");
        itemlabel.setFont(new Font("Courier New", Font.PLAIN, 13));
        itemlabel.setForeground(textcolor);
        
        JTextField itemfield = new JTextField("select a row");
        itemfield.setEditable(false);
        styleTextField(itemfield);
        itemfield.setForeground(textmutedcolor);
        
        itempanel.add(itemlabel,BorderLayout.NORTH);
        itempanel.add(itemfield, BorderLayout.CENTER);
        
        JPanel addqty = new JPanel(new BorderLayout(0,3));
        addqty.setBackground(panelcolor);
        
        JLabel addlabel = new JLabel("Add Quantity");
        addlabel.setFont(new Font("Courier New", Font.PLAIN, 13));
        addlabel.setForeground(textcolor);
        
        JTextField addfield = new JTextField("0");
        styleTextField(addfield);
        
        addqty.add(addlabel, BorderLayout.NORTH);
        addqty.add(addfield, BorderLayout.CENTER);
        
        JPanel minPanel = new JPanel(new BorderLayout(0, 3));
        minPanel.setBackground(panelcolor);
        
        JLabel minLbl = new JLabel("New Min Stock");
        minLbl.setFont(new Font("Courier New", Font.PLAIN, 13));
        minLbl.setForeground(textcolor);
        
        JTextField repMinField = new JTextField("");
        styleTextField(repMinField);
        
        minPanel.add(minLbl, BorderLayout.NORTH);
        minPanel.add(repMinField, BorderLayout.CENTER);
        
        JPanel addbtnpanel = new JPanel(new BorderLayout(0,3));
        addbtnpanel.setBackground(panelcolor);
        addbtnpanel.add(new JLabel(" "), BorderLayout.NORTH);
        
        JButton addbtn = styledButton("ADD STOCK",true);
        addbtn.setEnabled(false);
        addbtnpanel.add(addbtn, BorderLayout.CENTER);
        
        row.add(itempanel);
        row.add(addqty);
        row.add(minPanel);
        row.add(addbtnpanel);
        
        form.add(row);
        
        String[] cols = {"Item Name", "Category","Current QTY","Min Stock","Status","Last Updated"};
        repmodel = new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r, int c){return false;}
        };
        reptable = new JTable(repmodel);
        styleTable(reptable);
        
        reptable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable t, Object val, boolean select, boolean focus, int row2, int col){
                
                Component c = super.getTableCellRendererComponent(t, val, select, focus, row2, col);
                if(select){
                    setBackground(redcolor);
                    setForeground(textcolor);
                    return c;
                }
                String status = String.valueOf(t.getValueAt(row2, 4));
                switch(status){
                    case "OUT OF STOCK":
                        setBackground(new Color(0x5A1A1A));
                        setForeground(redcolor);
                        break;
                    case "LOW":
                        setBackground(new Color(0x3A2E00));
                        setForeground(new Color(0xFFAA33));
                        break;
                    default : 
                        setBackground(bgcolor);
                        setForeground(greencolor);
                        break;
                }
                
                return c;
            }
        });
        
        reptable.getSelectionModel().addListSelectionListener(ex ->{
            if(!ex.getValueIsAdjusting()){
                int selrow = reptable.getSelectedRow();
                if(selrow >= 0){
                    String itemName = String.valueOf(repmodel.getValueAt(selrow,0));
                    itemfield.setText(itemName);
                    itemfield.setForeground(textcolor);
                    
                    repMinField.setText(String.valueOf(repmodel.getValueAt(selrow, 3)));
                    addbtn.setEnabled(true);
                }
            }
        });
        
        JScrollPane scroll = new JScrollPane(reptable);
        styleScrollBar(scroll);
        scroll.getViewport().setBackground(bgcolor);
        
        JPanel center = new JPanel(new BorderLayout());
        center.add(form, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        
        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(panelcolor);
        south.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, bordercolor));
        
        JPanel label = new JPanel(new FlowLayout(FlowLayout.LEFT,16,6));
        label.setBackground(panelcolor);
        label.add(colorlabel("● OK",greencolor));
        label.add(colorlabel("● LOW",new Color(0xFFAA33)));
        label.add(colorlabel("● OUT OF STOCK", redcolor));
        
        addbtn.addActionListener(e -> replenishitem(itemfield,addfield,repMinField));
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,15,10));
        bottomPanel.setBackground(panelcolor);
        
        JButton clearBtn = styledButton("CLEAR", false);
        
        clearBtn.addActionListener(e -> {
        itemfield.setText("select a row");
        itemfield.setForeground(textmutedcolor);
        addfield.setText("0");
        repMinField.setText("");
        reptable.clearSelection();
        });
        
        south.add(label, BorderLayout.NORTH);
        
        page.add(center,BorderLayout.CENTER);
        page.add(south,BorderLayout.SOUTH);
        
        bottomPanel.add(clearBtn);
        south.add(Box.createVerticalStrut(10));
        south.add(bottomPanel);

        loadreptable();
        
        return page;
    }
    
    
    
    private void refreshchecktable(){
        if(checkmodel == null) return;
        
        checkmodel.setRowCount(0);
        
        File file = new File(fnbfile);
        if(!file.exists()) return;
        
        try(BufferedReader read = new BufferedReader(new FileReader(file))){
            String line;
            
            while((line = read.readLine()) != null){
                String[] details = line.split("\\|", -1);
                
                int qty = Integer.parseInt(details[3]);
                int min = Integer.parseInt(details[4]);

                String status;
                if(qty == 0){
                    status = "OUT OF STOCK";
                } else if(qty < min){
                    status = "LOW";
                } else {
                    status = "OK";
                }
                
                String detail = (details.length > 6) ? details[6] : "";

                checkmodel.addRow(new Object[]{
                    details[0],      // name
                    details[1],      // category
                    details[2],      // price
                    qty,             // quantity
                    details[4],      // min stock
                    status,          // ✅ correct status
                    detail
                });
            }    
        }catch(IOException e){
            showMsg("Error loading stocks: " + e.getMessage(),false);
            }
    }
    
    private void filterStock(String categoryFilter){
        checkmodel.setRowCount(0); // clear table

        File file = new File(fnbfile);
        if(!file.exists()) return;

        try(BufferedReader read = new BufferedReader(new FileReader(file))){
            String line;

            while((line = read.readLine()) != null){
                String[] data = line.split("\\|",-1);

                if(data.length != 7) continue;
                
                String cat = data[1];

             // ✅ filter logic
                if(categoryFilter.equals("All Categories") || cat.equals(categoryFilter)){

                    int qty = Integer.parseInt(data[3]);
                    int min = Integer.parseInt(data[4]);

                    String status;
                    if(qty == 0){
                        status = "OUT OF STOCK";
                    } else if(qty < min){
                        status = "LOW";
                    } else {
                        status = "OK";
                    }

                    checkmodel.addRow(new Object[]{
                        data[0], data[1], data[2], qty, min, status, data[6]
                    });
                }
            }   

        }catch(IOException e){
            showMsg("Error filtering stock: " + e.getMessage(), false);
        }
    }
    
    private void replenishitem(JTextField itemfield, JTextField addfield, JTextField repMinField){
        String itemName = itemfield.getText().trim();
        if(itemName.isEmpty() || itemName.equals("select a row")){
            showMsg("Please select an item from the table",false);
            return;
        }
        
        String addqty = addfield.getText().trim();
        if(!addqty.matches("\\d+")){
            showMsg("Add quantity must be a positive number.",false);
            return;
        }
        
        int addQty = Integer.parseInt(addqty);
        if(addQty <= 0){
            showMsg("Add Quantity must be greater than zero.",false);
            return;
        }
        
        String newMinStr = repMinField.getText().trim();
        if (!newMinStr.isEmpty() && !newMinStr.matches("\\d+")) {
            showMsg("New minimum stock must be a whole number (or leave blank to keep current).", false);
            return;
        }
        
        ArrayList<String[]> items = new ArrayList<>();
        boolean found = false;
        
        try(BufferedReader read = new BufferedReader(new FileReader(fnbfile))){
            String line;
            
            while((line = read.readLine())!= null){
                String[] data = line.split("\\|", -1);
                if (data.length != 7) continue;
            
                if(data[0].equalsIgnoreCase(itemName)){
                    int current = Integer.parseInt(data[3]);
                    data[3] = String.valueOf(current + addQty);
                
                    if (!newMinStr.isEmpty()) {
                        data[4] = newMinStr;
                    }

                
                    data[5] = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
                
                    found = true;
                }
                items.add(data);
            }
        }catch(IOException e){
            showMsg("Error reading file: " + e.getMessage(),false);
        }
        
        if(!found){
            showMsg("Item not found in stock file",false);
            return;
        }
        
        try(BufferedWriter wr = new BufferedWriter(new FileWriter(fnbfile))){
            for(String[] item : items){
                wr.write(String.join("|", item));
                wr.newLine();
            }
        }catch(IOException e){
            showMsg("Error saving file:" + e.getMessage(),false);
            return;
        }
        
        refreshchecktable();
        loadreptable();
        addfield.setText("0");
        
        showMsg("Added "+ addQty + " units to \"" + itemName + "\".", true);
    }

    private void loadreptable(){
        if (repmodel == null) return;
        repmodel.setRowCount(0);

        File file = new File(fnbfile);
        if(!file.exists()) return;

        try(BufferedReader read = new BufferedReader(new FileReader(file))){
            String line;

            while((line = read.readLine())!= null){
                String[] data = line.split("\\|",-1);
                 if (data.length != 7) continue;

                int qty = Integer.parseInt(data[3]);
                int min = Integer.parseInt(data[4]);

                String status;
                if(qty == 0){
                    status = "OUT OF STOCK";
                }else if(qty < min){
                    status = "LOW";
                }else{
                    status = "OK";
                }

                repmodel.addRow(new Object[]{
                    data[0],data[1],data[3],data[4],status,data[5]
                });
            }
        }catch(IOException e){
            showMsg("Error loading file: " +e.getMessage(),false);
        }
    }
    
    
    private JPanel sectionHeader(String title, String backCard){
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(panelcolor);
        p.setBorder(new EmptyBorder(15,15,15,15));
        
        JButton backbtn = styledButton("← BACK",false);
        backbtn.addActionListener(e -> cardlayout.show(staffpanel,backCard));
        
        JLabel lbl = new JLabel("  " + title);
        lbl.setFont(new Font("Courier New", Font.BOLD, 16));
        lbl.setForeground(textcolor);
        
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setBackground(panelcolor);
        left.add(backbtn);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        center.setBackground(panelcolor);
        center.add(lbl);

        p.add(left, BorderLayout.WEST);
        p.add(center, BorderLayout.CENTER);
        
        return p;
    }

    private JTextField formField(JPanel formpanel, String label){
        formpanel.add(fieldLabel(label));
        formpanel.add(Box.createVerticalStrut(4));
        JTextField text = new JTextField();
        styleTextField(text);
        formpanel.add(text);
        formpanel.add(Box.createVerticalStrut(10));
        return text;
    }

    private JComboBox<String> formCombo(JPanel formpanel, String label, String[] options){
        formpanel.add(fieldLabel(label));
        formpanel.add(Box.createVerticalStrut(4));
        JComboBox<String> choice = new JComboBox<>(options);
        styleCombo(choice);
        formpanel.add(choice);
        formpanel.add(Box.createVerticalStrut(10));
        return choice;
    }

    private JLabel fieldLabel(String text){
        JLabel l = new JLabel(text);
        l.setFont(new Font("Courier New", Font.BOLD, 12));
        l.setForeground(textmutedcolor);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void styleTextField(JTextField text){
        text.setBackground(bgcolor);
        text.setForeground(textcolor);
        text.setCaretColor(redcolor);
        text.setFont(new Font("Courier New", Font.PLAIN, 13));
        text.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(bordercolor),
            new EmptyBorder(6, 8, 6, 8)
        ));
        text.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        text.setAlignmentX(Component.LEFT_ALIGNMENT);
    }


    private void styleCombo(JComboBox<String> combo) {
        combo.setBackground(inputbg);
        combo.setForeground(textcolor);
        combo.setFont(new Font("Courier New", Font.PLAIN, 13));
        combo.setBorder(new LineBorder(bordercolor));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> l, Object v,
                    int i, boolean sel, boolean foc) {
                super.getListCellRendererComponent(l, v, i, sel, foc);
                setBackground(sel ? redcolor : inputbg);
                setForeground(textcolor);
                setFont(new Font("Courier New", Font.PLAIN, 13));
                setBorder(new EmptyBorder(4, 8, 4, 8));
                return this;
            }
        });
    }


    private void styleTable(JTable table) {
        table.setBackground(inputbg);
        table.setForeground(textcolor);
        table.setFont(new Font("Courier New", Font.PLAIN, 12));
        table.setGridColor(bordercolor);
        table.setRowHeight(28);
        table.setSelectionBackground(redcolor);
        table.setSelectionForeground(textcolor);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        JTableHeader header = table.getTableHeader();
        header.setBackground(panelcolor);
        header.setForeground(redcolor);
        header.setFont(new Font("Courier New", Font.BOLD, 12));
        header.setBorder(new MatteBorder(0, 0, 2, 0, redcolor));
        header.setReorderingAllowed(false);
    }

    private void styleScrollBar(JScrollPane sp) {
        sp.getVerticalScrollBar().setBackground(bgcolor);
        sp.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            protected void configureScrollBarColors() {
                this.thumbColor = redcolor; this.trackColor = panelcolor;
            }
            protected JButton createDecreaseButton(int o) { return zeroButton(); }
            protected JButton createIncreaseButton(int o) { return zeroButton(); }
            JButton zeroButton() {
                JButton b = new JButton(); b.setPreferredSize(new Dimension(0,0)); return b;
            }
        });
    }

    private JButton styledButton(String text, boolean primary) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Courier New", Font.BOLD, 13));
        btn.setForeground(textcolor);
        btn.setBackground(primary ? redcolor : cardcolor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Color base   = primary ? redcolor : cardcolor;
        Color hovered = primary ? hovercolor : bordercolor;
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hovered); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(base); }
        });
        return btn;
    }
    
    private JLabel colorlabel(String text, Color color){
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Courier New", Font.PLAIN, 13));
        lbl.setForeground(color);
        return lbl;
    }

    private void clearAddForm(){
        itemname.setText("");
        category.setSelectedIndex(0);
        price.setText("");
        currentqty.setText("");
        minstockqty.setText("");
        combodetails.setText("");
    }

    private void showMsg(String msg, boolean success) {
        JOptionPane.showMessageDialog(frame, msg, success ? "Success" : "Error",
            success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }
}
