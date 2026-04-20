package cinema;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;


public class AdminPage{

    private JFrame frame;
    private JPanel adminPanel;
    private CardLayout cardLayout;

    private final Color bgcolor = new Color(0x242424);
    private final Color panelcolor = new Color(0x2E2E2E);
    private final Color bordercolor = new Color(0x444444);
    private final Color textcolor = new Color(0xF7F7F7);
    private final Color textmutedcolor = new Color(0xAAAAAA);
    private final Color cardcolor = new Color(0x363636);
    private final Color redcolor = new Color(0xD44444);
    private final Color inputBg = new Color(0x1E1E1E);
    private final Color hovercolor = new Color(0xE85555);

    private final String movieFile = "MovieDetails.txt";
    private final String scheduleFile = "Showtime.txt";

    private JTextField titleEnter,genreEnter,languageEnter,durationEnter,directorEnter,castEnter,subtitlesEnter;
    private JComboBox<String> ratingEnter;
    private JTextArea descriptionEnter;
    private JSpinner releaseDateEnter;
    private ImageIcon poster;


    public AdminPage(){
        frame = new JFrame("TGC Cinema - Admin Page");
        frame.setSize(500,700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.getContentPane().setBackground(bgcolor);
        frame.setVisible(true);

        cardLayout = new CardLayout();
        adminPanel = new JPanel(cardLayout);
        adminPanel.setBackground(bgcolor);

        adminPanel.add(menu(), "MENU");
        adminPanel.add(addMovie(), "Add_Movie");
        adminPanel.add(viewMovie(), "View_Movie");
        adminPanel.add(showtimeSchedule(), "Showtime_Schedule");
        adminPanel.add(salesReport(), "SalesReport");



        frame.add(adminPanel);
        frame.setVisible(true);

        cardLayout.show(adminPanel, "MENU");

    }

    private JPanel menu(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgcolor);

        JLabel title = new JLabel("TGC Cinema Admin", SwingConstants.CENTER);
        title.setForeground(textcolor);
        title.setFont(new Font("Courier New", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(35,40,15,0));
        panel.add(title,BorderLayout.NORTH);

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(bgcolor);
        inner.setBorder(new EmptyBorder(20, 40, 20, 40));

        String [][] buttons = {
                {"ADD MOVIE", "Add_Movie"}, {"VIEW MOVIE", "View_Movie"},
                {"SHOWTIME SCHEDULE","Showtime_Schedule"}, {"SALES REPORT","SalesReport"},
        };

        for(String[]btn : buttons){
            JButton b = menuBtn(btn[0]);
            String card = btn[1];

            b.addActionListener(e -> {
                cardLayout.show(adminPanel, card);
                if(card.equals("View_Movie")) refreshViewTable();
                if(card.equals("Showtime_Schedule")) refreshScheduleTable();
                if(card.equals("SalesReport")) salesReport();
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
            new LoginPage(); // go back to log in
        });

        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(logoutBtn);

        panel.add(inner, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;

    }

    private JButton menuBtn(String text){
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

    private JLabel moviePoster;
    private String posterPath = "";
    private JLabel posterFileLabel;

    private JPanel addMovie(){
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(bgcolor);
        wrap.add(sectionHeader("ADD MOVIE", "MENU"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(20,0));
        content.setBackground(bgcolor);
        content.setBorder(new EmptyBorder(10,20,10,20));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(bgcolor);
        left.setPreferredSize(new Dimension(220,550));
        left.setBorder(new EmptyBorder(10,10,10,10));

        JLabel posterTitle = new JLabel("MOVIE POSTER");
        posterTitle.setFont(new Font("Courier NEW", Font.BOLD, 12));
        posterTitle.setForeground(textcolor);
        posterTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        left.add(posterTitle);
        left.add(Box.createVerticalStrut(15));

        moviePoster = new JLabel("No Poster", SwingConstants.CENTER);
        moviePoster.setPreferredSize(new Dimension(180,260));
        moviePoster.setMinimumSize(new Dimension(180,260));
        moviePoster.setMaximumSize(new Dimension(180,260));
        moviePoster.setOpaque(true);
        moviePoster.setBackground(inputBg);
        moviePoster.setForeground(textcolor);
        moviePoster.setFont(new Font("Courier New", Font.PLAIN, 13));
        moviePoster.setBorder(new LineBorder(redcolor, 2));
        moviePoster.setAlignmentX(Component.CENTER_ALIGNMENT);

        left.add(moviePoster);
        left.add(Box.createVerticalStrut(15));

        JPanel posterControl = new JPanel();
        posterControl.setLayout(new BoxLayout(posterControl, BoxLayout.Y_AXIS));
        posterControl.setBackground(bgcolor);

        posterFileLabel = new JLabel("No file chosen");
        posterFileLabel.setFont(new Font("Courier New", Font.PLAIN, 13));
        posterFileLabel.setForeground(textcolor);
        posterFileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton uploadPosterBtn = styledButton("UPLOAD POSTER", true);
        uploadPosterBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadPosterBtn.addActionListener(e -> choosePoster());

        JButton removePoster = styledButton("REMOVE POSTER",false);
        removePoster.setAlignmentX(Component.CENTER_ALIGNMENT);

        removePoster.addActionListener(e -> {
            posterPath ="";
            posterFileLabel.setText("No file chosen");
            posterFileLabel.setForeground(textcolor);
            moviePoster.setIcon(null);
            moviePoster.setText("No Poster");
        });

        posterControl.add(posterFileLabel);
        posterControl.add(Box.createVerticalStrut(4));
        posterControl.add(uploadPosterBtn);
        posterControl.add(Box.createVerticalStrut(10));
        posterControl.add(removePoster);

        left.add(posterControl);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(bgcolor);
        form.setBorder(new EmptyBorder(10, 10, 10, 10));

        titleEnter = formField(form, "Movie Title");
        genreEnter = formField(form, "Genre");
        languageEnter = formField(form, "Language");

        ratingEnter = formCombo(form, "Movie Rating" , new String[]{"U","P12","P13","P16","P18"});

        form.add(fieldLabel("Release Date"));
        form.add(Box.createVerticalStrut(4));
        SpinnerDateModel dateModel = new SpinnerDateModel();
        releaseDateEnter = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(releaseDateEnter, "dd/MM/yyyy");
        releaseDateEnter.setEditor(dateEditor);
        styleSpinner(releaseDateEnter);
        form.add(releaseDateEnter);
        form.add(Box.createVerticalStrut(10));

        durationEnter = formField(form, "Duration(mins)");
        directorEnter = formField(form, "Director");
        castEnter = formField(form, "Cast");
        subtitlesEnter = formField(form, "Subtitles");

        form.add(fieldLabel("Description"));
        form.add(Box.createVerticalStrut(4));
        descriptionEnter = new JTextArea(5, 4);
        descriptionEnter.setLineWrap(true);
        descriptionEnter.setWrapStyleWord(true);
        styleTextArea(descriptionEnter);
        JScrollPane descScroll = new JScrollPane(descriptionEnter);
        descScroll.setBorder(new LineBorder(bordercolor));
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        descScroll.getViewport().setBackground(inputBg);
        form.add(descScroll);
        form.add(Box.createVerticalStrut(10));

        JPanel btnBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12,0));
        btnBottom.setBackground(bgcolor);

        JButton clearBtn = styledButton("CLEAR", false);
        JButton saveBtn  = styledButton("SAVE MOVIE", true);

        saveBtn.addActionListener(e -> saveMovie());
        clearBtn.addActionListener(e -> clearAddForm());


        btnBottom.add(clearBtn);
        btnBottom.add(saveBtn);

        form.add(Box.createVerticalStrut(5));
        form.add(btnBottom);
        form.add(Box.createVerticalStrut(5));

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(bgcolor);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        styleScrollBar(scroll);

        content.add(left,BorderLayout.WEST);
        content.add(scroll,BorderLayout.CENTER);

        wrap.add(content, BorderLayout.CENTER);
        return wrap;
    }

    private void choosePoster(){
        JFileChooser chooseFile = new JFileChooser();
        chooseFile.setDialogTitle("Select Movie Poster");

        int result = chooseFile.showOpenDialog(frame);

        if(result == JFileChooser.APPROVE_OPTION){
            File selectfile = chooseFile.getSelectedFile();
            posterPath = selectfile.getAbsolutePath();

            poster = new ImageIcon(posterPath);
            posterFileLabel.setText(selectfile.getName());
            posterFileLabel.setForeground(textcolor);
            Image img = poster.getImage().getScaledInstance(180,260,Image.SCALE_SMOOTH);
            moviePoster.setText("");
            moviePoster.setIcon(new ImageIcon(img));

        }

        if (posterPath != null && !posterPath.isEmpty()) {
            String copyPath = copyPoster(posterPath);
            if (copyPath != null) {
                posterPath = copyPath;
            }
        }
    }

    private String copyPoster(String oriPath){
        try{
            File sourcefile = new File(oriPath);

            File folder = new File("src/posters/");
            if(!folder.exists()){
                folder.mkdirs();
            }

            String newFileName = sourcefile.getName();
            File destination = new File(folder, newFileName);

            Files.copy(
                    sourcefile.toPath(),
                    destination.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

            return "src/posters/" + newFileName;

        }catch(IOException e){
            showMessage("Error copying poster: " + e.getMessage(),false);
        }

        return null;
    }

    private void deletePoster(String path){
        try{
            if(path != null && !path.trim().isEmpty()){
                File file = new File(path);
                if(file.exists()){
                    file.delete();
                }
            }
        }catch(Exception e){
            showMessage("Failed to delete poster: " + e.getMessage(),false);
        }
    }



    private boolean containsNumber(String text){
        return text.matches(".*\\d.*");
    }

    private boolean validateText(String fieldName, String value){
        if(value.isEmpty()){
            showMessage(fieldName + " cannot be empty!", false);
            return false;
        }

        if(containsNumber(value)){
            showMessage(fieldName + " cannot contain numbers! Please try again.", false);
            return false;
        }

        return true;
    }

    private JTable movieTable;
    private DefaultTableModel tableModel;

    private JPanel viewMovie(){
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(bgcolor);
        wrap.add(sectionHeader("VIEW MOVIES", "MENU"), BorderLayout.NORTH);

        String[] cols = {"Title","Genre","Rating", "Duration"};
        tableModel = new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r, int c){return false;}
        };
        movieTable = new JTable(tableModel);
        styleTable(movieTable);

        JScrollPane scroll = new JScrollPane(movieTable);
        scroll.setBorder(new LineBorder(bordercolor));
        scroll.getViewport().setBackground(inputBg);
        styleScrollBar(scroll);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12,8));
        btnRow.setBackground(bgcolor);

        JButton editBtn   = styledButton("EDIT",   false);
        JButton deleteBtn = styledButton("DELETE", true);


        editBtn.addActionListener(e   -> editSelectedMovie());
        deleteBtn.addActionListener(e -> deleteSelectedMovie());

        btnRow.add(editBtn);
        btnRow.add(deleteBtn);

        wrap.add(scroll,  BorderLayout.CENTER);
        wrap.add(btnRow,  BorderLayout.SOUTH);
        return wrap;
    }

    private JTable scheduleTable;
    private DefaultTableModel scheduleModel;

    private JComboBox<String> scheduleMovieBox;
    private JComboBox<String> scheduleTimeBox;
    private JComboBox<String> scheduleHallBox;

    private JSpinner scheduleDate;

    private JPanel showtimeSchedule(){
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(bgcolor);
        wrap.add(sectionHeader("SHOWTIME SCHEDULE", "MENU"), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(bgcolor);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(bgcolor);
        form.setBorder(new EmptyBorder(12, 25, 12, 25));

        form.add(fieldLabel("Select Movie"));
        form.add(Box.createVerticalStrut(4));
        scheduleMovieBox = new JComboBox<>();
        styleCombo(scheduleMovieBox);
        form.add(scheduleMovieBox);
        form.add(Box.createVerticalStrut(10));

        form.add(fieldLabel("Schedule Date"));
        form.add(Box.createVerticalStrut(4));
        SpinnerDateModel scheduleDateModel = new SpinnerDateModel();
        scheduleDate = new JSpinner(scheduleDateModel);
        JSpinner.DateEditor scheduleDateEditor = new JSpinner.DateEditor(scheduleDate, "dd/MM/yyyy");
        scheduleDate.setEditor(scheduleDateEditor);
        styleSpinner(scheduleDate);
        form.add(scheduleDate);
        form.add(Box.createVerticalStrut(10));

        form.add(fieldLabel("Show Time"));
        form.add(Box.createVerticalStrut(4));
        scheduleTimeBox = new JComboBox<>(new String[]{"2:00 PM", "5:00 PM", "8:00 PM"});
        styleCombo(scheduleTimeBox);
        form.add(scheduleTimeBox);
        form.add(Box.createVerticalStrut(10));

        // Hall
        form.add(fieldLabel("Hall Number"));
        form.add(Box.createVerticalStrut(4));
        Map<String, String> hallMap = new TreeMap<>(loadHallType());
        scheduleHallBox = new JComboBox<>();

        for (String hallName : hallMap.keySet()) {
            scheduleHallBox.addItem(hallName + " (" + hallMap.get(hallName) + ")");
        }

        styleCombo(scheduleHallBox);
        form.add(scheduleHallBox);
        form.add(Box.createVerticalStrut(14));

        // Buttons
        JPanel formBtnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        formBtnRow.setBackground(bgcolor);

        JButton clearBtn = styledButton("CLEAR", false);
        JButton addScheduleBtn = styledButton("ADD SCHEDULE", true);


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
        tableScroll.getViewport().setBackground(inputBg);
        tableScroll.setPreferredSize(new Dimension(450, 280));
        styleScrollBar(tableScroll);

        // Bottom buttons
        JPanel bottomBtnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomBtnRow.setBackground(bgcolor);


        JButton refreshBtn = styledButton("REFRESH", false);
        JButton editBtn = styledButton("EDIT", false);
        JButton deleteBtn = styledButton("DELETE", true);

        editBtn.addActionListener(e -> editSelectedSchedule());
        deleteBtn.addActionListener(e -> deleteSelectedSchedule());
        refreshBtn.addActionListener(e -> {
            loadMovie();
            refreshScheduleTable();
        });


        bottomBtnRow.add(editBtn);
        bottomBtnRow.add(deleteBtn);
        bottomBtnRow.add(refreshBtn);

        mainPanel.add(form, BorderLayout.NORTH);
        mainPanel.add(tableScroll, BorderLayout.CENTER);
        mainPanel.add(bottomBtnRow, BorderLayout.SOUTH);

        wrap.add(mainPanel, BorderLayout.CENTER);

        // Load data initially
        loadMovie();
        refreshScheduleTable();

        return wrap;
    }

    private JPanel salesReport(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgcolor);
        panel.add(sectionHeader("SALES REPORT", "MENU"), BorderLayout.NORTH);

        int fnbOrders = 0;
        double fnbRevenue = 0.0;
        int totalItems = 0;

        Map<String, Integer> itemCount = new HashMap<>();

        // read OrderHistory.txt file
        try(BufferedReader read = new BufferedReader(new FileReader("OrderHistory.txt"))){

            String line;
            while((line = read.readLine())!= null){
                if(line.startsWith("----ORDER----")){
                    fnbOrders++;
                }else if(line.startsWith("Total: RM ")){
                    double value = Double.parseDouble(line.replace("Total: RM ", "").trim());
                    fnbRevenue += value;
                }else if(line.contains(" x")){
                    try{
                        String[] parts = line.split(" x");
                        String name = parts[0].trim();
                        int qty = Integer.parseInt(parts[1].split(" ")[0]);

                        totalItems += qty;

                        itemCount.put(name, itemCount.getOrDefault(name,0) + qty);
                    }catch(NumberFormatException e){

                    }
                }
            }
        }catch(IOException e){
            showMessage("Error reading file:" + e.getMessage(),false);
        }

        int totalTickets = 0;
        int ticketOrders = 0;
        double ticketRevenue = 0.0;

        try(BufferedReader read = new BufferedReader(new FileReader("BookingDetail.txt"))){
            String line;

            while((line = read.readLine()) != null){
                String data[] = line.split("\\|");

                if(!line.trim().isEmpty()){
                    ticketOrders++;
                }

                if(data.length >= 14){
                    String[] ticketParts = data[7].split(",");

                    int tickets = 0;

                    for(String t : ticketParts){
                        tickets += Integer.parseInt(t);
                    }

                    totalTickets += tickets;

                    ticketRevenue += Double.parseDouble(data[9]);

                    fnbRevenue += Double.parseDouble(data[10]);

                    if (data[8].equals("null")) continue;

                    if(data[8] != null  && !data[8].equals("null") && !data[8].isEmpty()){

                        fnbOrders++;

                        String[] items = data[8].split(",");

                        for(String item : items){

                            if(item == null || item.trim().isEmpty()) continue;

                            String[] parts = item.split(":");

                            if(parts.length < 2) continue;

                            String name = parts[0].trim();

                            int qty = 0;
                            try{
                                qty = Integer.parseInt(parts[1].trim());
                            }catch(NumberFormatException e){
                                continue;
                            }

                            totalItems += qty;

                            itemCount.put(name, itemCount.getOrDefault(name, 0) + qty);
                        }
                    }
                }
            }
        }catch(IOException e){
            showMessage("Error reading file:" + e.getMessage(),false);
        }

        double totalRevenue = fnbRevenue + ticketRevenue;
        int totalOrders = fnbOrders + ticketOrders;

        ArrayList<Map.Entry<String, Integer>> sorted = new ArrayList<>(itemCount.entrySet());
        sorted.sort((a,b) -> b.getValue() - a.getValue());

        StringBuilder report = new StringBuilder();

        report.append(String.format("F&B Orders     : %d\n", fnbOrders));
        report.append(String.format("Ticket Orders  : %d\n", ticketOrders));
        report.append(String.format("Total Orders   : %d\n\n", totalOrders));

        report.append(String.format("F&B Revenue    : RM %.2f\n", fnbRevenue));
        report.append(String.format("Ticket Revenue : RM %.2f\n", ticketRevenue));
        report.append(String.format("Total Revenue  : RM %.2f\n\n", totalRevenue));

        report.append(String.format("F&B Sold       : %d\n", totalItems));
        report.append(String.format("Tickets Sold   : %d\n\n", totalTickets));

        report.append("TOP SELLING ITEMS\n");

        int limit = Math.min(3, sorted.size());

        for(int i=0; i<limit; i++){
            Map.Entry<String, Integer> e = sorted.get(i);

            report.append(String.format("%d. %-20s (%d)\n",
                    i+1,
                    e.getKey(),
                    e.getValue()
            ));
        }

        if(limit == 0){
            report.append("No sales data available");
        }

        JTextArea area = new JTextArea(report.toString());
        area.setEditable(false);
        area.setFont(new Font("Courier New", Font.PLAIN, 20));
        area.setBackground(bgcolor);
        area.setForeground(textcolor);
        area.setBorder(new EmptyBorder(20,20,20,20));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(bgcolor);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }


    private void saveMovie(){
        String title = titleEnter.getText().trim();
        String date = ((JSpinner.DateEditor) releaseDateEnter.getEditor()).getFormat().format(releaseDateEnter.getValue());
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
            showMessage("Please fill in all required fields.", false);
            return;
        }

        if(!validateText("Genre", genre)) return;
        if(!validateText("Language", language)) return;
        if(!validateText("Director", director)) return;
        if(!validateText("Cast", cast)) return;
        if(!validateText("Subtitles", subtitles)) return;

        try {
            Integer.valueOf(duration);
        } catch (NumberFormatException ex) {
            showMessage("Duration must be a number.", false);
            return;
        }

        if (posterPath == null || posterPath.isEmpty()) {
            showMessage("Please upload a movie poster!", false);
            return;
        }



        try(BufferedWriter saveMovie = new BufferedWriter(new FileWriter(movieFile, true))){
            saveMovie.write(title + "|" + genre + "|" + language + "|" + rating + "|" + date + "|" + duration + "|" +
                    director + "|" + cast + "|" + subtitles + "|" + description + "|" + posterPath);

            saveMovie.newLine();
            showMessage("Movie saved successfully!", true);
            clearAddForm();
            refreshViewTable();
            loadMovie();

        }catch(IOException e){
            showMessage("Error saving: " + e.getMessage(), false);
        }
    }

    private void saveScheduleFile(){
        try(BufferedWriter save = new BufferedWriter(new FileWriter(scheduleFile))){

            for(int i=0; i<scheduleModel.getRowCount();i++){
                String line = scheduleModel.getValueAt(i, 0).toString() + "|" +
                        scheduleModel.getValueAt(i, 1).toString() + "|" +
                        scheduleModel.getValueAt(i, 2).toString() + "|" +
                        scheduleModel.getValueAt(i, 3).toString();

                save.write(line);
                save.newLine();
            }
        }catch(IOException e){
            showMessage("Error saving schedule file: " + e.getMessage(), false);
        }
    }

    private void loadMovie(){
        if(scheduleMovieBox == null)return;

        scheduleMovieBox.removeAllItems();

        File file = new File(movieFile);
        if (!file.exists()) return;

        HashSet<String> titles = new HashSet<>();

        try(BufferedReader readMovie = new BufferedReader(new FileReader(file))){
            String line;

            while((line = readMovie.readLine()) != null){
                String[] details = line.split("\\|", -1);
                if (details.length == 11){
                    titles.add(details[0].trim());
                }
            }
        }catch(IOException e){
            showMessage("Error loading movies" + e.getMessage(),false);
        }

        ArrayList<String> sortedTitles = new ArrayList<>(titles);
        Collections.sort(sortedTitles);

        for(String title : sortedTitles){
            scheduleMovieBox.addItem(title);
        }
    }

    private void editSelectedSchedule(){
        int row = scheduleTable.getSelectedRow();

        if(row<0){
            showMessage("Please select a schedule to edit", false);
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

        File file = new File(movieFile);
        HashSet<String> titles = new HashSet<>();

        if(file.exists()){
            try(BufferedReader read = new BufferedReader(new FileReader(file))){
                String line;
                while((line = read.readLine()) != null){
                    String[] details = line.split("\\|", -1);
                    if(details.length == 11){
                        titles.add(details[0].trim());
                    }
                }
            }catch(IOException e){
                showMessage("Error loading movies: " + e.getMessage(), false);
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
        Map<String, String> hallMap = new TreeMap<>(loadHallType());
        scheduleHallBox = new JComboBox<>();

        for (String hallName : hallMap.keySet()) {
            scheduleHallBox.addItem(hallName + " (" + hallMap.get(hallName) + ")");
        }
        styleCombo(scheduleHallBox);
        scheduleHallBox.setSelectedItem(scheduleModel.getValueAt(row, 3).toString());
        form.add(scheduleHallBox);
        form.add(Box.createVerticalStrut(18));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setBackground(bgcolor);

        JButton saveBtn = styledButton("SAVE CHANGES", true);
        JButton cancelBtn = styledButton("CANCEL", false);

        saveBtn.addActionListener(e -> {
            if (movieBox.getItemCount() == 0) {
                showMessage("No movies available.", false);
                return;
            }

            String movieTitle = movieBox.getSelectedItem().toString();
            String scheduleDate = ((JSpinner.DateEditor) editDateSpinner.getEditor())
                    .getFormat().format(editDateSpinner.getValue());
            String showTime = timeBox.getSelectedItem().toString();
            String hall = scheduleHallBox.getSelectedItem().toString();

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
                showMessage("Schedule date cannot be in the past.", false);
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
                    showMessage("This hall already has a movie scheduled at the same date and time.", false);
                    return;
                }
            }

            scheduleModel.setValueAt(movieTitle, row, 0);
            scheduleModel.setValueAt(scheduleDate, row, 1);
            scheduleModel.setValueAt(showTime, row, 2);
            scheduleModel.setValueAt(hall, row, 3);

            saveScheduleFile();
            showMessage("Schedule updated successfully!", true);
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
            showMessage("Please select a schedule to delete", false);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "Delete selected showtime schedule?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if(confirm == JOptionPane.YES_OPTION){
            scheduleModel.removeRow(row);
            saveScheduleFile();
            showMessage("Schedule deleted successfully!", true);
        }
    }

    private JLabel editMoviePoster;
    private String editPosterPath = "";
    private JLabel editFileLabel;

    private void editSelectedMovie(){

        int row = movieTable.getSelectedRow();

        if (row < 0) {
            showMessage("Please select a movie to edit.", false);
            return;
        }

        String selectedTitle = tableModel.getValueAt(row, 0).toString();


        //  ArrayList<String[]> movies = new ArrayList<>();
        String[] movieDetails = null;

        File file = new File(movieFile);

        if(!file.exists()){
            showMessage("Movie file not found!",false);
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split("\\|", -1);
                if (details.length == 11 && details[0].equals(selectedTitle)) {
                    movieDetails = details;
                    break;
                }
            }
        } catch (IOException e) {
            showMessage("Error loading movie details: " + e.getMessage(), false);
            return;
        }

        if (movieDetails == null) {
            showMessage("Movie details not found.", false);
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

        JLabel posterTitle = new JLabel("Movie Poster");
        posterTitle.setFont(new Font("Courier New", Font.BOLD, 13));
        posterTitle.setForeground(textcolor);
        posterTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(posterTitle);
        form.add(Box.createVerticalStrut(10));

        JPanel editPoster = new JPanel(new BorderLayout(14,0));
        editPoster.setBackground(bgcolor);
        editPoster.setMaximumSize(new Dimension(Integer.MAX_VALUE,175));
        editPoster.setAlignmentX(Component.LEFT_ALIGNMENT);

        editMoviePoster = new JLabel("No Poster", SwingConstants.CENTER);
        editMoviePoster.setPreferredSize(new Dimension(180,260));
        editMoviePoster.setMinimumSize(new Dimension(180,260));
        editMoviePoster.setMaximumSize(new Dimension(180,260));
        editMoviePoster.setOpaque(true);
        editMoviePoster.setBackground(inputBg);
        editMoviePoster.setForeground(textcolor);
        editMoviePoster.setFont(new Font("Courier New", Font.PLAIN, 13));
        editMoviePoster.setBorder(new LineBorder(redcolor, 2));
        editMoviePoster.setAlignmentX(Component.CENTER_ALIGNMENT);

        editPosterPath = movieDetails[10];
        if(editPosterPath != null && !editPosterPath.trim().isEmpty() && new File(editPosterPath).exists()){
            ImageIcon icon = new ImageIcon(editPosterPath);
            Image img = icon.getImage().getScaledInstance(180, 250, Image.SCALE_SMOOTH);
            editMoviePoster.setIcon(new ImageIcon(img));
            editMoviePoster.setText("");
        }else{
            editMoviePoster.setIcon(null);
            editMoviePoster.setText("No Poster");
        }

        JPanel editPosterControl = new JPanel();
        editPosterControl.setLayout(new BoxLayout(editPosterControl, BoxLayout.Y_AXIS));
        editPosterControl.setBackground(bgcolor);

        editFileLabel = new JLabel(editPosterPath.isEmpty() ? "No file chosen" :
                new File(editPosterPath).getName());
        editFileLabel.setFont(new Font("Courier New", Font.PLAIN, 13));
        editFileLabel.setForeground(textcolor);
        editFileLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton changePoster = styledButton("CHANGE POSTER",true);
        JButton removePoster = styledButton("REMOVE POSTER", false);
        changePoster.setAlignmentX(Component.LEFT_ALIGNMENT);
        removePoster.setAlignmentX(Component.LEFT_ALIGNMENT);

        changePoster.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Movie Poster");

            int result = fileChooser.showOpenDialog(frame);

            if(result == JFileChooser.APPROVE_OPTION){
                File selectedfile = fileChooser.getSelectedFile();
                editPosterPath = selectedfile.getAbsolutePath();
                editFileLabel.setText(selectedfile.getName());
                editFileLabel.setForeground(textcolor);
                ImageIcon icon = new ImageIcon(editPosterPath);
                Image img = icon.getImage().getScaledInstance(180, 250, Image.SCALE_SMOOTH);

                editMoviePoster.setIcon(new ImageIcon(img));
                editMoviePoster.setText("");
                editMoviePoster.revalidate();
                editMoviePoster.repaint();
            }
            String copiedPath = copyPoster(editPosterPath);

            if (copiedPath != null) {
                editPosterPath = copiedPath;
            }

        });

        removePoster.addActionListener(e -> {
            editPosterPath = "";
            editFileLabel.setText("No file chosen");
            editFileLabel.setForeground(textcolor);
            editMoviePoster.setIcon(null);
            editMoviePoster.setText("No Poster");
        });

        editPosterControl.add(editFileLabel);
        editPosterControl.add(Box.createVerticalStrut(10));
        editPosterControl.add(changePoster);
        editPosterControl.add(Box.createVerticalStrut(8));
        editPosterControl.add(removePoster);

        editPoster.add(editMoviePoster, BorderLayout.WEST);
        editPoster.add(editPosterControl, BorderLayout.CENTER);
        form.add(editPoster);
        form.add(Box.createVerticalStrut(12));

        // Title
        form.add(fieldLabel("Movie Title"));
        form.add(Box.createVerticalStrut(4));
        JTextField titleField = new JTextField(movieDetails[0]);
        styleTextField(titleField);
        form.add(titleField);
        form.add(Box.createVerticalStrut(8));

        form.add(fieldLabel("Genre"));
        form.add(Box.createVerticalStrut(4));
        JTextField genreField = new JTextField(movieDetails[1]);
        styleTextField(genreField);
        form.add(genreField);
        form.add(Box.createVerticalStrut(8));

        form.add(fieldLabel("Language"));
        form.add(Box.createVerticalStrut(4));
        JTextField languageField = new JTextField(movieDetails[2]);
        styleTextField(languageField);
        form.add(languageField);
        form.add(Box.createVerticalStrut(8));

        form.add(fieldLabel("Movie Rating"));
        form.add(Box.createVerticalStrut(4));
        JComboBox<String> ratingBox = new JComboBox<>(new String[]{"U", "P12", "P13", "P16", "P18"});
        styleCombo(ratingBox);
        ratingBox.setSelectedItem(movieDetails[3]);
        form.add(ratingBox);
        form.add(Box.createVerticalStrut(8));

        form.add(fieldLabel("Release Date"));
        form.add(Box.createVerticalStrut(4));
        JTextField releaseDateField = new JTextField(movieDetails[4]);
        styleTextField(releaseDateField);
        form.add(releaseDateField);
        form.add(Box.createVerticalStrut(8));

        form.add(fieldLabel("Duration (mins)"));
        form.add(Box.createVerticalStrut(4));
        JTextField durationField = new JTextField(movieDetails[5]);
        styleTextField(durationField);
        form.add(durationField);
        form.add(Box.createVerticalStrut(8));

        form.add(fieldLabel("Director"));
        form.add(Box.createVerticalStrut(4));
        JTextField directorField = new JTextField(movieDetails[6]);
        styleTextField(directorField);
        form.add(directorField);
        form.add(Box.createVerticalStrut(8));

        form.add(fieldLabel("Cast"));
        form.add(Box.createVerticalStrut(4));
        JTextField castField = new JTextField(movieDetails[7]);
        styleTextField(castField);
        form.add(castField);
        form.add(Box.createVerticalStrut(8));

        form.add(fieldLabel("Subtitles"));
        form.add(Box.createVerticalStrut(4));
        JTextField subtitlesField = new JTextField(movieDetails[8]);
        styleTextField(subtitlesField);
        form.add(subtitlesField);
        form.add(Box.createVerticalStrut(8));

        form.add(fieldLabel("Description"));
        form.add(Box.createVerticalStrut(4));
        JTextArea descriptionArea = new JTextArea(movieDetails[9], 4, 10);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        styleTextArea(descriptionArea);

        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setBorder(new LineBorder(bordercolor));
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        descScroll.getViewport().setBackground(inputBg);
        form.add(descScroll);
        form.add(Box.createVerticalStrut(12));

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
                showMessage("Please fill in all fields.", false);
                return;
            }

            if(!validateText("Genre", genre)) return;
            if(!validateText("Language", language)) return;
            if(!validateText("Director", director)) return;
            if(!validateText("Cast", cast)) return;
            if(!validateText("Subtitles", subtitles)) return;

            try {
                Integer.valueOf(duration);
            } catch (NumberFormatException ex) {
                showMessage("Duration must be a number.", false);
                return;
            }

            if (editPosterPath == null || editPosterPath.isEmpty()) {
                showMessage("Please upload a movie poster!", false);
                return;
            }


            ArrayList<String[]> allMovies = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(movieFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] details = line.split("\\|", -1);
                    if (details.length == 11) {
                        if (details[0].equals(selectedTitle)) {
                            allMovies.add(new String[]{
                                    title, genre, language, rating, releaseDate,
                                    duration, director, cast, subtitles, description, editPosterPath
                            });
                        } else {
                            allMovies.add(details);
                        }
                    }
                }
            } catch (IOException ex) {
                showMessage("Error reading movies: " + ex.getMessage(), false);
                return;
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(movieFile))) {
                for (String[] m : allMovies) {
                    bw.write(String.join("|", m));
                    bw.newLine();
                }
            } catch (IOException ex) {
                showMessage("Error saving movies: " + ex.getMessage(), false);
                return;
            }

            refreshViewTable();

            showMessage("Movie updated successfully!", true);
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
            showMessage("Please select a movie to delete.", false);
            return;
        }

        String selectedTitle = tableModel.getValueAt(row, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(frame,"Delete selected movie?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if(confirm == JOptionPane.YES_OPTION){
            ArrayList<String[]> allMovies = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(movieFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] details = line.split("\\|", -1);

                    if (details.length == 11){
                        if(details[0].equals(selectedTitle)){
                            deletePoster(details[10]);
                        }else{
                            allMovies.add(details);
                        }
                    }
                }
            } catch (IOException e) {
                showMessage("Error reading movies: " + e.getMessage(), false);
                return;
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(movieFile))) {
                for (String[] m : allMovies) {
                    bw.write(String.join("|", m));
                    bw.newLine();
                }
            } catch (IOException e) {
                showMessage("Error saving movies: " + e.getMessage(), false);
                return;
            }

            refreshViewTable();
            loadMovie();
            showMessage("Movie deleted successfully!", true);
        }

    }

    private void addShowtimeSchedule(){
        if(scheduleMovieBox.getItemCount() == 0){
            showMessage("No movies available. Please add movie first.", false);
            return;
        }

        String movieTitle = scheduleMovieBox.getSelectedItem().toString();
        String scheduleDate = ((JSpinner.DateEditor) this.scheduleDate.getEditor())
                .getFormat().format(this.scheduleDate.getValue());
        String showTime = scheduleTimeBox.getSelectedItem().toString();
        String hallFull = scheduleHallBox.getSelectedItem().toString();
        String hall = hallFull.split(" \\(")[0]; // remove (2D)

        Date selectedDate = (Date) this.scheduleDate.getValue();
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
            showMessage("Schedule date cannot be in the past.", false);
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
                showMessage("This hall already has a movie scheduled at the same date and time.", false);
                return;
            }
        }

        try (BufferedWriter showtime = new BufferedWriter(new FileWriter(scheduleFile, true))) {
            showtime.write(movieTitle + "|" + scheduleDate + "|" + showTime + "|" + hall);
            showtime.newLine();

            showMessage("Showtime schedule added successfully!", true);
            refreshScheduleTable();
            clearScheduleForm();

        } catch (IOException e) {
            showMessage("Error saving schedule: " + e.getMessage(), false);
        }
    }

    private void refreshViewTable(){
        if(tableModel == null) return;

        tableModel.setRowCount(0);

        File file = new File(movieFile);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] details = line.split("\\|", -1);

                if (details.length == 11) {
                    tableModel.addRow(new Object[]{
                            details[0], // Title
                            details[1], // Genre
                            details[3], // Rating
                            details[5]  // Duration
                    });
                }
            }
        } catch (IOException e) {
            showMessage("Error loading movies: " + e.getMessage(), false);
        }
    }

    private void refreshScheduleTable(){
        if(scheduleModel == null) return;

        scheduleModel.setRowCount(0);
        File file = new File(scheduleFile);
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
            showMessage("Error loading schedule: " + e.getMessage(), false);
        }
    }

    private Map<String, String> loadHallType() {

        Map<String, String> hallMap = new HashMap<>();

        File file = new File("Hall.txt");
        if (!file.exists()) return hallMap;

        try (BufferedReader br = new BufferedReader(new FileReader("Hall.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    hallMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            showMessage("Error loading hall file", false);
        }

        return hallMap;
    }


    private JPanel sectionHeader(String title, String backCard){
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(panelcolor);
        p.setBorder(new EmptyBorder(15,15,15,15));

        JButton backBtn = styledButton("← BACK",false);
        backBtn.addActionListener(e -> cardLayout.show(adminPanel,backCard));

        JLabel lbl = new JLabel("  " + title);
        lbl.setFont(new Font("Courier New", Font.BOLD, 16));
        lbl.setForeground(textcolor);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setBackground(panelcolor);
        left.add(backBtn);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        center.setBackground(panelcolor);
        center.add(lbl);

        p.add(left, BorderLayout.WEST);
        p.add(center, BorderLayout.CENTER);

        return p;
    }

    private JTextField formField(JPanel formPanel, String label){
        formPanel.add(fieldLabel(label));
        formPanel.add(Box.createVerticalStrut(4));
        JTextField text = new JTextField();
        styleTextField(text);
        formPanel.add(text);
        formPanel.add(Box.createVerticalStrut(10));
        return text;
    }

    private JComboBox<String> formCombo(JPanel formPanel, String label, String[] options){
        formPanel.add(fieldLabel(label));
        formPanel.add(Box.createVerticalStrut(4));
        JComboBox<String> choice = new JComboBox<>(options);
        styleCombo(choice);
        formPanel.add(choice);
        formPanel.add(Box.createVerticalStrut(10));
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
        ta.setBackground(inputBg);
        ta.setForeground(textcolor);
        ta.setCaretColor(redcolor);
        ta.setFont(new Font("Courier New", Font.PLAIN, 13));
        ta.setBorder(new EmptyBorder(6, 8, 6, 8));
        ta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        ta.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setBackground(inputBg);
        combo.setForeground(textcolor);
        combo.setFont(new Font("Courier New", Font.PLAIN, 13));
        combo.setBorder(new LineBorder(bordercolor));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> l, Object v,
                                                          int i, boolean sel, boolean foc) {
                super.getListCellRendererComponent(l, v, i, sel, foc);
                setBackground(sel ? redcolor : inputBg);
                setForeground(textcolor);
                setFont(new Font("Courier New", Font.PLAIN, 13));
                setBorder(new EmptyBorder(4, 8, 4, 8));
                return this;
            }
        });
    }

    private void styleSpinner(JSpinner sp) {
        sp.setBackground(inputBg);
        sp.setForeground(textcolor);
        sp.setFont(new Font("Courier New", Font.PLAIN, 13));
        sp.setBorder(new LineBorder(bordercolor));
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        JComponent editor = sp.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setBackground(inputBg);
            tf.setForeground(textcolor);
            tf.setCaretColor(redcolor);
            tf.setFont(new Font("Courier New", Font.PLAIN, 13));
            tf.setBorder(new EmptyBorder(4, 8, 4, 8));
        }
    }

    private void styleTable(JTable table) {
        table.setBackground(inputBg);
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
        releaseDateEnter.setValue(new Date());

        posterPath = "";
        if(moviePoster != null){
            moviePoster.setIcon(null);
            moviePoster.setText("No Poster");
        }

        if(posterFileLabel != null){
            posterFileLabel.setText("No file chosen");
            posterFileLabel.setForeground(textcolor);
        }
    }


    private void clearScheduleForm(){
        if(scheduleMovieBox.getItemCount() > 0){
            scheduleMovieBox.setSelectedIndex(0);
        }

        scheduleTimeBox.setSelectedIndex(0);
        scheduleHallBox.setSelectedIndex(0);
        scheduleDate.setValue(new Date());
    }


    private void showMessage(String msg, boolean success) {
        JOptionPane.showMessageDialog(frame, msg, success ? "Success" : "Error",
                success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }







}

