
package cinema;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;


public class AdminPage{
    
    JFrame frame;
    JPanel adminpanel;
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
    
    final String moviefile = "MovieDetails.txt";
    
    Movie movie;
    JTextField titleEnter,genreEnter,languageEnter,durationEnter,directorEnter,castEnter,subtitlesEnter;
    JComboBox<String> ratingEnter,showtimeEnter,hallEnter;
    JTextArea descriptionEnter;
    JSpinner releasedateEnter;
    ImageIcon poster;
    
    
    public AdminPage(){
        frame = new JFrame("TGC Cinema - Admin Page");
        frame.setSize(500,700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.getContentPane().setBackground(bgcolor);
        frame.setVisible(true);
        
        cardlayout = new CardLayout();
        adminpanel = new JPanel(cardlayout);
        adminpanel.setBackground(bgcolor);
        
        adminpanel.add(menu(), "MENU");
        adminpanel.add(addmovie(), "Add_Movie");
        adminpanel.add(viewmovie(), "View_Movie");
        adminpanel.add(checkstock(), "Check_Stock");
        adminpanel.add(replenishstock(), "Replenish_Stock");
        
        frame.add(adminpanel);
        frame.setVisible(true);
        
        cardlayout.show(adminpanel, "MENU");
        
    }
    
    public JPanel menu(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(bgcolor);
        
        JLabel title = new JLabel("TGC Cinema Admin", SwingConstants.CENTER);
        title.setForeground(textcolor);
        title.setFont(new Font("Courier New", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(35,140,15,0));
        panel.add(title);
        
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(bgcolor);
        inner.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        String [][] buttons = {
            {"ADD MOVIE", "Add_Movie"}, {"VIEW MOVIE", "View_Movie"}, 
            {"CHECK STOCK","Check_Stock"}, {"REPLENISH STOCK","Replenish_Stock"},
        };
        
        for(String[]btn : buttons){
            JButton b = menubtn(btn[1] + " " + btn[0]);
            String card = btn[2];
            b.addActionListener(e -> {cardlayout.show(adminpanel, card);
            if(card.equals("View_Movie")) refreshViewTable();
            if(card.equals("Check_Stock")) refreshStockTable();
            if(card.equals("Replenish_Stock")) refreshReplenishTable();
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
    
    
    private JPanel addmovie(){
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(bgcolor);
        wrap.add(sectionHeader("ADD MOVIE", "Add_Movie"), BorderLayout.NORTH);
        
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(bgcolor);
        form.setBorder(new EmptyBorder(10, 30, 10, 30));
        
        titleEnter = formField(form, "Movie Title");
        genreEnter = formField(form, "Genre");
        languageEnter = formField(form, "Language");
        
        ratingEnter = formCombo(form, "Movie Rating" , new String[]{"U","P12","P13","P16","P18"});
        
        form.add(fieldLabel("Release Date"));
        form.add(Box.createVerticalStrut(4));
        SpinnerDateModel datemodel = new SpinnerDateModel();
        releasedateEnter = new JSpinner(datemodel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(releasedateEnter, "dd/MM/yyyy");
        releasedateEnter.setEditor(dateEditor);
        styleSpinner(releasedateEnter);
        form.add(releasedateEnter);
        form.add(Box.createVerticalStrut(10));
        
        durationEnter = formField(form, "Duration(mins)");
        directorEnter = formField(form, "Director");
        castEnter = formField(form, "Cast");
        subtitlesEnter = formField(form, "Subtitles");
        
        form.add(fieldLabel("Description"));
        form.add(Box.createVerticalStrut(4));
        descriptionEnter = new JTextArea(3, 10);
        descriptionEnter.setLineWrap(true);
        descriptionEnter.setWrapStyleWord(true);
        styleTextArea(descriptionEnter);
        JScrollPane descScroll = new JScrollPane(descriptionEnter);
        descScroll.setBorder(new LineBorder(bordercolor));
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        descScroll.getViewport().setBackground(inputbg);
        form.add(descScroll);
        form.add(Box.createVerticalStrut(10));
        
        showtimeEnter = formCombo(form, "Show Time", new String[]{"2:00 PM","5:00 PM","8:00 PM"});
        hallEnter = formCombo(form, "Hall Number", new String[]{"Hall 1","Hall 2","Hall 3","Hall 4","Hall 5"});
        
        JPanel btnbottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12,0));
        btnbottom.setBackground(bgcolor);
        JButton saveBtn  = styledButton("💾 SAVE MOVIE", true);
        JButton clearBtn = styledButton("✖ CLEAR", false);
        JButton backbtn = styledButton("Back", true);
        saveBtn.addActionListener(e -> savemovie());
        clearBtn.addActionListener(e -> clearAddForm());
        backbtn.addActionListener(e -> backmenu());
        btnbottom.add(saveBtn);
        btnbottom.add(clearBtn);
        btnbottom.add(backbtn);
        form.add(Box.createVerticalStrut(10));
        form.add(btnbottom);
        form.add(Box.createVerticalStrut(10));
        
        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(bgcolor);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        styleScrollBar(scroll);
 
        wrap.add(scroll, BorderLayout.CENTER);
        return wrap;
    }
    
    private JPanel movieTable;
    private DefaultTableModel tableModel;
  
    private JPanel viewmovie(){
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(bgcolor);
        wrap.add(sectionHeader("VIEW MOVIES", "View_Movie"), BorderLayout.NORTH);
        
        String[] cols = {"Title","Genre","Rating","Show Time","Hall","Duration"};
        tableModel = new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r, int c){return false;}
        };
        movieTable = new JTable(tableModel);
        styleTable(movieTable);
        
        JScrollPane scroll = new JScrollPane(movieTable);
        scroll.setBorder(new LineBorder(bordercolor));
        scroll.getViewport().setBackground(inputbg);
        styleScrollBar(scroll);
        
        JPanel btnrow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12,8));
        btnrow.setBackground(bgcolor);
        JButton editBtn   = styledButton("✏EDIT",   false);
        JButton deleteBtn = styledButton("🗑DELETE", true);
        editBtn.addActionListener(e   -> editSelectedMovie());
        deleteBtn.addActionListener(e -> deleteSelectedMovie());
        btnrow.add(editBtn);
        btnrow.add(deleteBtn);
        
        wrap.add(scroll,  BorderLayout.CENTER);
        wrap.add(btnrow,  BorderLayout.SOUTH);
        return wrap;
    }
    
    private JPanel showtime(){
    
    }
    
    private JPanel checkstock(){
    
    }
    
    private JPanel replenishstock(){
    
    }
    
    
    private void savemovie(){
        String title = titleEnter.getText().trim();
        String date = ((JSpinner.DateEditor) releasedateEnter.getEditor()).getFormat().format(releasedateEnter.getValue());
        String genre = genreEnter.getText().trim();
        String language = languageEnter.getText().trim();
        String rating = ratingEnter.getSelectedItem().toString();
        String duration = durationEnter.getText().trim();
        String director = directorEnter.getText().trim();
        String cast = castEnter.getText().trim(); 
        String subtitles = subtitlesEnter.getText().trim();
        String description = descriptionEnter.getText().trim().replace("/n", " ");
        String showtime = showtimeEnter.getSelectedItem().toString();
        String hall = hallEnter.getSelectedItem().toString();
        
        if(title.isEmpty() || genre.isEmpty() || language.isEmpty() || date.isEmpty()||
                rating.isEmpty()|| duration.isEmpty()|| director.isEmpty()||cast.isEmpty()||
                subtitles.isEmpty()|| description.isEmpty()|| showtime.isEmpty()||hall.isEmpty()){
            showMsg("Please fill in all required fields.", false);
            return;
        }
        
        try(BufferedWriter saveMovie = new BufferedWriter(new FileWriter(moviefile, true))){
            saveMovie.write(title + "|" + genre + "|" + language + "|" + rating + "|" + date + "|" + duration + "|" + 
                    director + "|" + cast + "|" + subtitles + "|" + description + "|" + showtime + "|" + hall);
            saveMovie.newLine();
            showMsg("Movie saved successfully!", true);
            clearAddForm();
            
        }catch(IOException e){
            showMsg("Error saving: " + e.getMessage(), false);
        }
    }
    
    private void loadmovie(){
        tableModel.setRowCount(0);
        
        File file = new File(moviefile);
        if (!file.exists()) return;
        
        try(BufferedReader readmovie = new BufferedReader(new FileReader(file))){
            String line;
            
            while((line = readmovie.readLine()) != null){
                String[] details = line.split("\\|", -1);
                if (details.length == 12){
                    tableModel.addRow(details);
                }
            }
        }catch(IOException e){
            showMsg("Error loading movies" + e.getMessage(),false);
        }
    }
    
    private void editSelectedMovie(){
        
    }
    
    private void deleteSelectedMovie(){
        
    }
    
    
    private JPanel sectionHeader(String title, String backCard){
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(panelcolor);
        p.setBorder(new MatteBorder(0, 0, 2, 0, redcolor));
        p.setPreferredSize(new Dimension(500, 48));
        
        JLabel lbl = new JLabel("  " + title);
        lbl.setFont(new Font("Courier New", Font.BOLD, 16));
        lbl.setForeground(textcolor);
        p.add(lbl, BorderLayout.CENTER);
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
    
    private void styleTextArea(JTextArea ta) {
        ta.setBackground(inputbg);
        ta.setForeground(textcolor);
        ta.setCaretColor(redcolor);
        ta.setFont(new Font("Courier New", Font.PLAIN, 13));
        ta.setBorder(new EmptyBorder(6, 8, 6, 8));
        ta.setAlignmentX(Component.LEFT_ALIGNMENT);
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
 
    private void styleSpinner(JSpinner sp) {
        sp.setBackground(inputbg);
        sp.setForeground(textcolor);
        sp.setFont(new Font("Courier New", Font.PLAIN, 13));
        sp.setBorder(new LineBorder(bordercolor));
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        JComponent editor = sp.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setBackground(inputbg);
            tf.setForeground(textcolor);
            tf.setCaretColor(redcolor);
            tf.setFont(new Font("Courier New", Font.PLAIN, 13));
            tf.setBorder(new EmptyBorder(4, 8, 4, 8));
        }
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
    
    
    private void clearAddForm(){
        titleEnter.setText(""); genreEnter.setText(""); languageEnter.setText("");
        durationEnter.setText(""); directorEnter.setText(""); castEnter.setText("");
        subtitlesEnter.setText(""); descriptionEnter.setText("");
        ratingEnter.setSelectedIndex(0); showtimeEnter.setSelectedIndex(0); hallEnter.setSelectedIndex(0);
        releasedateEnter.setValue(new Date());
    }
    
    
    private void backmenu(){
        frame.dispose();
        menu();
    }
    
    private void showMsg(String msg, boolean success) {
        JOptionPane.showMessageDialog(frame, msg, success ? "Success" : "Error",
            success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }
    
}

