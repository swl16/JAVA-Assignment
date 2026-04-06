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

    JFrame frame;
    JPanel staffpanel;
    CardLayout cardlayout;

    final Color bgcolor = new Color(0x242424);
    final Color panelcolor = new Color(0x2E2E2E);
    final Color bordercolor = new Color(0x444444);
    final Color textcolor = new Color(0xF7F7F7);
    final Color textmutedcolor = new Color(0xAAAAAA);
    final Color cardcolor = new Color(0x363636);
    final Color redcolor = new Color(0xD44444);
    final Color inputbg = new Color(0x1E1E1E);
    final Color greencolor = new Color(0x44AA66);
    final Color hovercolor = new Color(0xE85555);
    
    final String fnbfile = "FnBStock.txt";


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


    public JPanel menu(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(bgcolor);

        JLabel title = new JLabel("TGC Cinema Staff", SwingConstants.CENTER);
        title.setForeground(textcolor);
        title.setFont(new Font("Courier New", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(35,100,15,0));
        panel.add(title);

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

        panel.add(inner);

        return panel;

    }

    public JButton menubtn(String text){
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
    
    JTextField itemname, price, currentqty, minstockqty, combodetails;
    JComboBox<String> category;
    
    JPanel addstock(){
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
    
    void savestock(){
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
    
    private JTable checktable;
    private DefaultTableModel checkmodel;
    
    JPanel checkstock(){
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
                        setForeground(textcolor);
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
        JButton delete = styledButton("DELETE",true);
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
//                loadstock();
                showMsg("Item deleted successfully!", true);
            }
            
        });
        btnrow.add(delete);
        
        south.add(label, BorderLayout.NORTH);
        south.add(btnrow, BorderLayout.CENTER);
        
        filtercombo.addActionListener(e -> {
            String selected = filtercombo.getSelectedItem().toString();
            filterstock(selected);
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
    
    private JTable reptable;
    private DefaultTableModel repmodel;
    
    JPanel replenishstock(){
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
                        setForeground(textcolor);
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
        
        south.add(label, BorderLayout.NORTH);
        
        page.add(center,BorderLayout.CENTER);
        page.add(south,BorderLayout.SOUTH);

        loadreptable();
        
        return page;
    }
    
    
    
    void refreshchecktable(){
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
    
    void filterstock(String categoryFilter){
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
    
    void replenishitem(JTextField itemfield, JTextField addfield, JTextField repMinField){
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
    
    void loadreptable(){
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
    
    JLabel colorlabel(String text, Color color){
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
