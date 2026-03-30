
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
    final String schedulefile = "ShowtimeSchedule.txt";
    
    JTextField titleEnter,genreEnter,languageEnter,durationEnter,directorEnter,castEnter,subtitlesEnter;
    JComboBox<String> ratingEnter;
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
        adminpanel.add(showtimeschedule(), "Showtime_Schedule");
        adminpanel.add(salesreport(), "Salesreport");
        
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
            {"SHOWTIME SCHEDULE","Showtime_Schedule"}, {"SALES REPORT","Salesreport"},
        };
        
        for(String[]btn : buttons){
            JButton b = menubtn(btn[0]);
            String card = btn[1];
            
            b.addActionListener(e -> {cardlayout.show(adminpanel, card);
            if(card.equals("View_Movie")) refreshViewTable();
            if(card.equals("Showtime_Schedule")) refreshScheduleTable();
            if(card.equals("Salesreport")) generatereport();
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
    
    private JTable movieTable;
    private DefaultTableModel tableModel;
  
    private JPanel viewmovie(){
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(bgcolor);
        wrap.add(sectionHeader("VIEW MOVIES", "View_Movie"), BorderLayout.NORTH);
        
        String[] cols = {"Title","Genre","Rating", "Duration"};
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
        JButton backBtn = styledButton("BACK", false);
        editBtn.addActionListener(e   -> editSelectedMovie());
        deleteBtn.addActionListener(e -> deleteSelectedMovie());
        backBtn.addActionListener(e -> cardlayout.show(adminpanel, "MENU"));
        
        btnrow.add(editBtn);
        btnrow.add(deleteBtn);
        btnrow.add(backBtn);
        
        wrap.add(scroll,  BorderLayout.CENTER);
        wrap.add(btnrow,  BorderLayout.SOUTH);
        return wrap;
    }
    
    private JTable scheduleTable;
    private DefaultTableModel scheduleModel;
    
    private JComboBox<String> schedulemoviebox;
    private JComboBox<String> scheduletimebox;
    private JComboBox<String> schedulehallbox;
    
    private JSpinner scheduledate;
    
    private JPanel showtimeschedule(){
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(bgcolor);
        wrap.add(sectionHeader("SHOWTIME SCHEDULE", "Showtime_Schedule"), BorderLayout.NORTH);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(bgcolor);
        
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(bgcolor);
        form.setBorder(new EmptyBorder(12, 25, 12, 25));
        
        form.add(fieldLabel("Select Movie"));
        form.add(Box.createVerticalStrut(4));
        schedulemoviebox = new JComboBox<>();
        styleCombo(schedulemoviebox);
        form.add(schedulemoviebox);
        form.add(Box.createVerticalStrut(10));
        
        form.add(fieldLabel("Schedule Date"));
        form.add(Box.createVerticalStrut(4));
        SpinnerDateModel scheduleDateModel = new SpinnerDateModel(new Date(), new Date(), null, Calendar.DAY_OF_MONTH);
        scheduledate = new JSpinner(scheduleDateModel);
        JSpinner.DateEditor scheduleDateEditor = new JSpinner.DateEditor(scheduledate, "dd/MM/yyyy");
        scheduledate.setEditor(scheduleDateEditor);
        styleSpinner(scheduledate);
        form.add(scheduledate);
        form.add(Box.createVerticalStrut(10));
        
        form.add(fieldLabel("Show Time"));
        form.add(Box.createVerticalStrut(4));
        scheduletimebox = new JComboBox<>(new String[]{"2:00 PM", "5:00 PM", "8:00 PM"});
        styleCombo(scheduletimebox);
        form.add(scheduletimebox);
        form.add(Box.createVerticalStrut(10));

        // Hall
        form.add(fieldLabel("Hall Number"));
        form.add(Box.createVerticalStrut(4));
        schedulehallbox = new JComboBox<>(new String[]{"Hall 1", "Hall 2", "Hall 3", "Hall 4", "Hall 5"});
        styleCombo(schedulehallbox);
        form.add(schedulehallbox);
        form.add(Box.createVerticalStrut(14));

        // Buttons
        JPanel formBtnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        formBtnRow.setBackground(bgcolor);

        JButton addScheduleBtn = styledButton("ADD SCHEDULE", true);
        JButton clearBtn = styledButton("CLEAR", false);

        addScheduleBtn.addActionListener(e -> addShowtimeSchedule());
        clearBtn.addActionListener(e -> clearScheduleForm());

        formBtnRow.add(addScheduleBtn);
        formBtnRow.add(clearBtn);

        form.add(formBtnRow);
        form.add(Box.createVerticalStrut(12));
        
        String[] cols = {"Movie Title", "Date", "Show Time", "Hall"};
        scheduleModel = new DefaultTableModel(cols, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
        
        scheduleTable = new JTable(scheduleModel);
        styleTable(scheduleTable);

        JScrollPane tableScroll = new JScrollPane(scheduleTable);
        tableScroll.setBorder(new LineBorder(bordercolor));
        tableScroll.getViewport().setBackground(inputbg);
        tableScroll.setPreferredSize(new Dimension(450, 280));
        styleScrollBar(tableScroll);

        // Bottom buttons
        JPanel bottomBtnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomBtnRow.setBackground(bgcolor);

        JButton editBtn = styledButton("EDIT", false);
        JButton deleteBtn = styledButton("DELETE", true);
        JButton refreshBtn = styledButton("REFRESH", false);
        JButton backBtn = styledButton("BACK", false);

        editBtn.addActionListener(e -> editSelectedSchedule());
        deleteBtn.addActionListener(e -> deleteSelectedSchedule());
        refreshBtn.addActionListener(e -> {
            loadmovie();
            refreshScheduleTable();
        });
        
        backBtn.addActionListener(e -> cardlayout.show(adminpanel, "MENU"));

        bottomBtnRow.add(editBtn);
        bottomBtnRow.add(deleteBtn);
        bottomBtnRow.add(refreshBtn);
        bottomBtnRow.add(backBtn);

        mainPanel.add(form, BorderLayout.NORTH);
        mainPanel.add(tableScroll, BorderLayout.CENTER);
        mainPanel.add(bottomBtnRow, BorderLayout.SOUTH);

        wrap.add(mainPanel, BorderLayout.CENTER);

        // Load data initially
        loadmovie();
        refreshScheduleTable();

        return wrap;
    }
    
    private JPanel salesreport(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgcolor);
        panel.add(sectionHeader("SALES REPORT", "Salesreport"), BorderLayout.NORTH);

        JLabel label = new JLabel("Sales Report Page (Coming Soon)", SwingConstants.CENTER);
        label.setForeground(textcolor);
        label.setFont(new Font("Courier New", Font.BOLD, 18));

        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
    
    private void generatereport(){
        
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
        String description = descriptionEnter.getText().trim().replace("\n", " ");
        
        if(title.isEmpty() || genre.isEmpty() || language.isEmpty() || date.isEmpty()||
                rating.isEmpty()|| duration.isEmpty()|| director.isEmpty()||cast.isEmpty()||
                subtitles.isEmpty()|| description.isEmpty()){
            showMsg("Please fill in all required fields.", false);
            return;
        }
        
        try(BufferedWriter saveMovie = new BufferedWriter(new FileWriter(moviefile, true))){
            saveMovie.write(title + "|" + genre + "|" + language + "|" + rating + "|" + date + "|" + duration + "|" + 
                    director + "|" + cast + "|" + subtitles + "|" + description);
            saveMovie.newLine();
            showMsg("Movie saved successfully!", true);
            clearAddForm();
            
        }catch(IOException e){
            showMsg("Error saving: " + e.getMessage(), false);
        }
    }
    
    private void saveschedulefile(){
        try(BufferedWriter save = new BufferedWriter(new FileWriter(schedulefile))){
            
            for(int i=0; i<scheduleModel.getRowCount();i++){
                String line = scheduleModel.getValueAt(i, 0).toString() + "|" +
                        scheduleModel.getValueAt(i, 1).toString() + "|" +
                        scheduleModel.getValueAt(i, 2).toString() + "|" +
                        scheduleModel.getValueAt(i, 3).toString();
                
                save.write(line);
                save.newLine();
            }
        }catch(IOException e){
            showMsg("Error saving schedule file: " + e.getMessage(), false);
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
                if (details.length == 10){
                    tableModel.addRow(details);
                }
            }
        }catch(IOException e){
            showMsg("Error loading movies" + e.getMessage(),false);
        }
    }
    
    private void editSelectedSchedule(){
        int row = scheduleTable.getSelectedRow();
        
        if(row<0){
            showMsg("Please select a schedule to edit", false);
            return;
        }
        
        JDialog editDialog = new JDialog(frame, "Edit Showtime Schedule", true);
        editDialog.setSize(450, 420);
        editDialog.setLayout(new BorderLayout());
        editDialog.setLocationRelativeTo(frame);
        editDialog.getContentPane().setBackground(bgcolor);
        editDialog.setResizable(false);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(bgcolor);
        form.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Movie
        form.add(fieldLabel("Select Movie"));
        form.add(Box.createVerticalStrut(4));
        JComboBox<String> movieBox = new JComboBox<>();
        styleCombo(movieBox);
        
        File file = new File(moviefile);
        HashSet<String> titles = new HashSet<>();
        
        if(file.exists()){
            try(BufferedReader read = new BufferedReader(new FileReader(file))){
                String line;
                while((line = read.readLine()) != null){
                    String[] details = line.split("\\|", -1);
                    if(details.length == 10){
                        titles.add(details[0].trim());
                    }
                }
            }catch(IOException e){
                showMsg("Error loading movies: " + e.getMessage(), false);
            }
        }
        
        ArrayList<String> sortedTitles = new ArrayList<>(titles);
        Collections.sort(sortedTitles);

        for(String title : sortedTitles){
            movieBox.addItem(title);
        }

        movieBox.setSelectedItem(scheduleModel.getValueAt(row, 0).toString());
        form.add(movieBox);
        form.add(Box.createVerticalStrut(10));

        form.add(fieldLabel("Schedule Date"));
        form.add(Box.createVerticalStrut(4));
        JSpinner editDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(editDateSpinner, "dd/MM/yyyy");
        editDateSpinner.setEditor(dateEditor);
        styleSpinner(editDateSpinner);

        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            Date parsedDate = sdf.parse(scheduleModel.getValueAt(row, 1).toString());
            editDateSpinner.setValue(parsedDate);
        } catch (Exception ex) {
            editDateSpinner.setValue(new Date());
        }

        form.add(editDateSpinner);
        form.add(Box.createVerticalStrut(10));

        form.add(fieldLabel("Show Time"));
        form.add(Box.createVerticalStrut(4));
        JComboBox<String> timeBox = new JComboBox<>(new String[]{"2:00 PM", "5:00 PM", "8:00 PM"});
        styleCombo(timeBox);
        timeBox.setSelectedItem(scheduleModel.getValueAt(row, 2).toString());
        form.add(timeBox);
        form.add(Box.createVerticalStrut(10));

        form.add(fieldLabel("Hall Number"));
        form.add(Box.createVerticalStrut(4));
        JComboBox<String> hallBox = new JComboBox<>(new String[]{"Hall 1", "Hall 2", "Hall 3", "Hall 4", "Hall 5"});
        styleCombo(hallBox);
        hallBox.setSelectedItem(scheduleModel.getValueAt(row, 3).toString());
        form.add(hallBox);
        form.add(Box.createVerticalStrut(18));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setBackground(bgcolor);

        JButton saveBtn = styledButton("SAVE CHANGES", true);
        JButton cancelBtn = styledButton("CANCEL", false);

        saveBtn.addActionListener(e -> {
            if (movieBox.getItemCount() == 0) {
                showMsg("No movies available.", false);
                return;
            }

            String movieTitle = movieBox.getSelectedItem().toString();
            String scheduleDate = ((JSpinner.DateEditor) editDateSpinner.getEditor())
                .getFormat().format(editDateSpinner.getValue());
            String showTime = timeBox.getSelectedItem().toString();
            String hall = hallBox.getSelectedItem().toString();

            Date selectedDate = (Date) editDateSpinner.getValue();
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            Calendar chosen = Calendar.getInstance();
            chosen.setTime(selectedDate);
            chosen.set(Calendar.HOUR_OF_DAY, 0);
            chosen.set(Calendar.MINUTE, 0);
            chosen.set(Calendar.SECOND, 0);
            chosen.set(Calendar.MILLISECOND, 0);

            if (chosen.before(today)) {
                showMsg("Schedule date cannot be in the past.", false);
                return;
            }

            for (int i = 0; i < scheduleModel.getRowCount(); i++) {
                if (i == row) continue;

                String existingDate = scheduleModel.getValueAt(i, 1).toString();
                String existingTime = scheduleModel.getValueAt(i, 2).toString();
                String existingHall = scheduleModel.getValueAt(i, 3).toString();

                if (existingDate.equals(scheduleDate) &&
                    existingTime.equals(showTime) &&
                    existingHall.equals(hall)) {
                    showMsg("This hall already has a movie scheduled at the same date and time.", false);
                    return;
                }
            }

            scheduleModel.setValueAt(movieTitle, row, 0);
            scheduleModel.setValueAt(scheduleDate, row, 1);
            scheduleModel.setValueAt(showTime, row, 2);
            scheduleModel.setValueAt(hall, row, 3);

            saveschedulefile();
            showMsg("Schedule updated successfully!", true);
            editDialog.dispose();
        });

        cancelBtn.addActionListener(e -> editDialog.dispose());

        btnRow.add(saveBtn);
        btnRow.add(cancelBtn);
        form.add(btnRow);

        editDialog.add(form, BorderLayout.CENTER);
        editDialog.setVisible(true);
    }
    
    private void deleteSelectedSchedule(){
        int row = scheduleTable.getSelectedRow();
        
        if(row<0){
            showMsg("Please select a schedule to delete", false);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(frame, "Delete selected showtime schedule?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if(confirm == JOptionPane.YES_OPTION){
            scheduleModel.removeRow(row);
            saveschedulefile();
            showMsg("Schedule deleted successfully!", true);
        }
    }
    
    private void editSelectedMovie(){
    
     int row = movieTable.getSelectedRow();

     if (row < 0) {
        showMsg("Please select a movie to edit.", false);
        return;
     }
     
     String selectedTitle = tableModel.getValueAt(row, 0).toString();

     String[] movieDetails = null;

     File file = new File(moviefile);
     if (file.exists()) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split("\\|", -1);
                if (details.length == 10 && details[0].equals(selectedTitle)) {
                    movieDetails = details;
                    break;
                }
            }
        } catch (IOException e) {
            showMsg("Error loading movie details: " + e.getMessage(), false);
            return;
        }
     }

     if (movieDetails == null) {
        showMsg("Movie details not found.", false);
        return;
     }

     JDialog editDialog = new JDialog(frame, "Edit Movie", true);
     editDialog.setSize(500, 700);
     editDialog.setLayout(new BorderLayout());
     editDialog.setLocationRelativeTo(frame);
     editDialog.getContentPane().setBackground(bgcolor);
     editDialog.setResizable(false);

    // ===== MAIN FORM PANEL =====
     JPanel form = new JPanel();
     form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
     form.setBackground(bgcolor);
     form.setBorder(new EmptyBorder(15, 25, 15, 25));

     // Title
     form.add(fieldLabel("Movie Title"));
     form.add(Box.createVerticalStrut(4));
     JTextField titleField = new JTextField(movieDetails[0]);
     styleTextField(titleField);
     form.add(titleField);
     form.add(Box.createVerticalStrut(10));

     form.add(fieldLabel("Genre"));
     form.add(Box.createVerticalStrut(4));
     JTextField genreField = new JTextField(movieDetails[1]);
     styleTextField(genreField);
     form.add(genreField);
     form.add(Box.createVerticalStrut(10));

     form.add(fieldLabel("Language"));
     form.add(Box.createVerticalStrut(4));
     JTextField languageField = new JTextField(movieDetails[2]);
     styleTextField(languageField);
     form.add(languageField);
     form.add(Box.createVerticalStrut(10));

     form.add(fieldLabel("Movie Rating"));
     form.add(Box.createVerticalStrut(4));
     JComboBox<String> ratingBox = new JComboBox<>(new String[]{"U", "P12", "P13", "P16", "P18"});
     styleCombo(ratingBox);
     ratingBox.setSelectedItem(movieDetails[3]);
     form.add(ratingBox);
     form.add(Box.createVerticalStrut(10));

     form.add(fieldLabel("Release Date"));
     form.add(Box.createVerticalStrut(4));
     JTextField releaseDateField = new JTextField(movieDetails[4]);
     styleTextField(releaseDateField);
     form.add(releaseDateField);
     form.add(Box.createVerticalStrut(10));

     form.add(fieldLabel("Duration (mins)"));
     form.add(Box.createVerticalStrut(4));
     JTextField durationField = new JTextField(movieDetails[5]);
     styleTextField(durationField);
     form.add(durationField);
     form.add(Box.createVerticalStrut(10));

     form.add(fieldLabel("Director"));
     form.add(Box.createVerticalStrut(4));
     JTextField directorField = new JTextField(movieDetails[6]);
     styleTextField(directorField);
     form.add(directorField);
     form.add(Box.createVerticalStrut(10));

     form.add(fieldLabel("Cast"));
     form.add(Box.createVerticalStrut(4));
     JTextField castField = new JTextField(movieDetails[7]);
     styleTextField(castField);
     form.add(castField);
     form.add(Box.createVerticalStrut(10));

     form.add(fieldLabel("Subtitles"));
     form.add(Box.createVerticalStrut(4));
     JTextField subtitlesField = new JTextField(movieDetails[8]);
     styleTextField(subtitlesField);
     form.add(subtitlesField);
     form.add(Box.createVerticalStrut(10));

     form.add(fieldLabel("Description"));
     form.add(Box.createVerticalStrut(4));
     JTextArea descriptionArea = new JTextArea(movieDetails[9], 4, 10);
     descriptionArea.setLineWrap(true);
     descriptionArea.setWrapStyleWord(true);
     styleTextArea(descriptionArea);

     JScrollPane descScroll = new JScrollPane(descriptionArea);
     descScroll.setBorder(new LineBorder(bordercolor));
     descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
     descScroll.getViewport().setBackground(inputbg);
     form.add(descScroll);
     form.add(Box.createVerticalStrut(10));

    // ===== BUTTON PANEL =====
     JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
     buttonPanel.setBackground(bgcolor);

     JButton saveButton = styledButton("SAVE", true);
     JButton cancelButton = styledButton("CANCEL", false);

     saveButton.addActionListener(e -> {
        String title = titleField.getText().trim();
        String genre = genreField.getText().trim();
        String language = languageField.getText().trim();
        String rating = ratingBox.getSelectedItem().toString();
        String releaseDate = releaseDateField.getText().trim();
        String duration = durationField.getText().trim();
        String director = directorField.getText().trim();
        String cast = castField.getText().trim();
        String subtitles = subtitlesField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (title.isEmpty() || genre.isEmpty() || language.isEmpty() || releaseDate.isEmpty()
                || duration.isEmpty() || director.isEmpty() || cast.isEmpty()
                || subtitles.isEmpty() || description.isEmpty()) {
            showMsg("Please fill in all fields.", false);
            return;
        }

        try {
            Integer.parseInt(duration);
        } catch (NumberFormatException ex) {
            showMsg("Duration must be a number.", false);
            return;
        }

        ArrayList<String[]> allMovies = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(moviefile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split("\\|", -1);
                if (details.length == 10) {
                    if (details[0].equals(selectedTitle)) {
                        allMovies.add(new String[]{
                            title, genre, language, rating, releaseDate,
                            duration, director, cast, subtitles, description
                        });
                    } else {
                        allMovies.add(details);
                    }
                }
            }
        } catch (IOException ex) {
            showMsg("Error reading movies: " + ex.getMessage(), false);
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(moviefile))) {
            for (String[] m : allMovies) {
                bw.write(String.join("|", m));
                bw.newLine();
            }
        } catch (IOException ex) {
            showMsg("Error saving movies: " + ex.getMessage(), false);
            return;
        }

        refreshViewTable();
        
        showMsg("Movie updated successfully!", true);
        editDialog.dispose();
        });

     cancelButton.addActionListener(e -> editDialog.dispose());

     buttonPanel.add(saveButton);
     buttonPanel.add(cancelButton);

     form.add(buttonPanel);

     JScrollPane scroll = new JScrollPane(form);
     scroll.setBorder(null);
     scroll.getViewport().setBackground(bgcolor);
     scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
     styleScrollBar(scroll);

     editDialog.add(scroll, BorderLayout.CENTER);
     editDialog.setVisible(true);
    }

    
    private void deleteSelectedMovie(){
        int row = movieTable.getSelectedRow();
        
        if(row<0){
            showMsg("Please select a movie to delete.", false);
            return;
        }
        
        String selectedTitle = tableModel.getValueAt(row, 0).toString();
        
        int confirm = JOptionPane.showConfirmDialog(frame,"Delete selected movie?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if(confirm == JOptionPane.YES_OPTION){
             ArrayList<String[]> allMovies = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(moviefile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] details = line.split("\\|", -1);
                    if (details.length == 10 && !details[0].equals(selectedTitle)) {
                        allMovies.add(details);
                    }
                }
            } catch (IOException e) {
                showMsg("Error reading movies: " + e.getMessage(), false);
                return;
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(moviefile))) {
                for (String[] m : allMovies) {
                    bw.write(String.join("|", m));
                    bw.newLine();
                }
            } catch (IOException e) {
                showMsg("Error saving movies: " + e.getMessage(), false);
                return;
            }

            refreshViewTable();
            loadmovie();
            showMsg("Movie deleted successfully!", true);
        }
        
    }
    
    private void addShowtimeSchedule(){
        if(schedulemoviebox.getItemCount() == 0){
            showMsg("No movies available. Please add movie first.", false);
            return;
        }
        
        String movieTitle = schedulemoviebox.getSelectedItem().toString();
        String scheduleDate = ((JSpinner.DateEditor) scheduledate.getEditor())
                .getFormat().format(scheduledate.getValue());
        String showTime = scheduletimebox.getSelectedItem().toString();
        String hall = schedulehallbox.getSelectedItem().toString();
        
        Date selectedDate = (Date) scheduledate.getValue();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        Calendar chosen = Calendar.getInstance();
        chosen.setTime(selectedDate);
        chosen.set(Calendar.HOUR_OF_DAY, 0);
        chosen.set(Calendar.MINUTE, 0);
        chosen.set(Calendar.SECOND, 0);
        chosen.set(Calendar.MILLISECOND, 0);

        if (chosen.before(today)) {
            showMsg("Schedule date cannot be in the past.", false);
            return;
        }

        // Prevent duplicate hall+date+time clash
        for (int i = 0; i < scheduleModel.getRowCount(); i++) {
            String existingDate = scheduleModel.getValueAt(i, 1).toString();
            String existingTime = scheduleModel.getValueAt(i, 2).toString();
            String existingHall = scheduleModel.getValueAt(i, 3).toString();

            if (existingDate.equals(scheduleDate) &&
                existingTime.equals(showTime) &&
                existingHall.equals(hall)) {
                showMsg("This hall already has a movie scheduled at the same date and time.", false);
                return;
            }   
        }

        try (BufferedWriter showtime = new BufferedWriter(new FileWriter(schedulefile, true))) {
            showtime.write(movieTitle + "|" + scheduleDate + "|" + showTime + "|" + hall);
            showtime.newLine();

            showMsg("Showtime schedule added successfully!", true);
            refreshScheduleTable();
            clearScheduleForm();

        } catch (IOException e) {
            showMsg("Error saving schedule: " + e.getMessage(), false);
        }
    }
    
//    private void saveMoviesToFile(){
//        try(BufferedWriter savemovie = new BufferedWriter(new FileWriter(moviefile))){
//            for(int i=0; i<tableModel.getRowCount(); i++){
//                StringBuilder line = new StringBuilder();
//                
//                for(int j=0; j < tableModel.getColumnCount();j++){
//                    line.append(tableModel.getValueAt(i, j).toString());
//                    if(j < tableModel.getColumnCount()-1){
//                        line.append("|");
//                    }
//                }
//                
//                savemovie.write(line.toString());
//                savemovie.newLine();
//            }
//        }catch(IOException e){
//            showMsg("Error saving movies: " + e.getMessage(), false);
//        }
//    }
    
    private void refreshViewTable(){
        if(tableModel == null) return;
        
        tableModel.setRowCount(0);
        
        File file = new File(moviefile);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] details = line.split("\\|", -1);

                if (details.length == 10) {
                    tableModel.addRow(new Object[]{
                    details[0], // Title
                    details[1], // Genre
                    details[3], // Rating
                    details[5]  // Duration
                    });
                }
            }
        } catch (IOException e) {
            showMsg("Error loading movies: " + e.getMessage(), false);
        }
    }
    
    private void refreshScheduleTable(){
        if(scheduleModel == null) return;
        
        scheduleModel.setRowCount(0);
        File file = new File(schedulefile);
        if(!file.exists()) return;
        
        try(BufferedReader read = new BufferedReader(new FileReader(file))){
            String line;
            
            while((line = read.readLine())!= null){
                String[] details = line.split("\\|", -1);
                
                if(details.length ==4){
                    scheduleModel.addRow(new Object[]{
                        details[0], //movie title
                        details[1], //date
                        details[2], //show time
                        details[3]  //hall
                    });
                }
            }
        }catch(IOException e){
            showMsg("Error loading schedule: " + e.getMessage(), false);
        }
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
        ratingEnter.setSelectedIndex(0);
        releasedateEnter.setValue(new Date());
    }
    
    
    private void clearScheduleForm(){
        if(schedulemoviebox.getItemCount() > 0){
            schedulemoviebox.setSelectedIndex(0);
        }
        
        scheduletimebox.setSelectedIndex(0);
        schedulehallbox.setSelectedIndex(0);
        scheduledate.setValue(new Date());
    }
    
    private void backmenu(){
        cardlayout.show(adminpanel, "MENU");
    }
    
    private void showMsg(String msg, boolean success) {
        JOptionPane.showMessageDialog(frame, msg, success ? "Success" : "Error",
            success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }
    
    private int parseInt(String s, int def) {
        try { 
            return Integer.parseInt(s == null ? "" : s.trim()); 
        }catch (NumberFormatException e) {
            return def;
        } 
    }
    
    
    
    
    
    
}

