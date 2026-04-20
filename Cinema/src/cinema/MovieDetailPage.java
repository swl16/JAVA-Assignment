package cinema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class MovieDetailPage implements ActionListener {
    private JFrame frame = new JFrame("TGC Cinema - Movie Detail");
    private JFrame homeFrame;
    private JButton backButton, showtimeButton,dateButton;
    private JPanel showTimeContainer;

    private final Color background = new Color(0x242424);
    private final Color textWhite = new Color(0xF7F7F7);
    private final Color lightGrey = new Color(0x3B3B3B);
    private static final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd MMM");

    private String username;
    private Movie movie;
    private Hall[] hall;
    private ShowTime[] showTime;

    private static int hallCount=0;
    private static int showTimeCount =0;

    public MovieDetailPage (JFrame homeFrame, Movie movieSelected,String username){
        this.username = username;
        movie = movieSelected;
        this.homeFrame = homeFrame;

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setSize(500,700);
        frame.getContentPane().setBackground(background);
        
        backButton = new JButton("< Back");
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusable(false);
        backButton.setBounds(8,10,400,40);
        backButton.setFont(new Font("Courier New",Font.PLAIN,17));
        backButton.setHorizontalAlignment(JButton.LEFT);
        backButton.addActionListener(this);
        backButton.setForeground(textWhite);
        frame.add(backButton);

        JLabel posterLabel = new JLabel();
        Image scaledPoster = movie.getPoster().getImage().getScaledInstance(108, 160, Image.SCALE_SMOOTH);
        ImageIcon poster = new ImageIcon(scaledPoster);
        posterLabel.setForeground(textWhite);
        posterLabel.setFont(new Font("Courier New",Font.PLAIN,15));
        posterLabel.setIcon(poster);
        posterLabel.setText(movie.getTitle());
        posterLabel.setHorizontalAlignment(JButton.CENTER);
        posterLabel.setHorizontalTextPosition(JButton.CENTER);
        posterLabel.setVerticalTextPosition(JButton.BOTTOM);
        posterLabel.setBounds(0,50,500,200);
        frame.add(posterLabel);
        
        JButton info = new JButton("ⓘ");
        info.setBounds(170, 221, 50, 20);
        info.setBackground(lightGrey);
        info.setForeground(textWhite);
        info.setFocusPainted(false);
        info.setBorderPainted(false);
        info.setContentAreaFilled(false);
        info.setFocusable(false);
        info.setToolTipText("More Information");
        info.addActionListener(e -> showMoreInfo());
        frame.add(info);

        JPanel showDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        showDatePanel.setBackground(background);
        showDatePanel.setBounds(0,260,480,50);
        frame.add(showDatePanel);

        showTimeContainer = new JPanel();
        showTimeContainer.setLayout(new BoxLayout(showTimeContainer,BoxLayout.Y_AXIS));
        showTimeContainer.setBackground(background);
        showTimeContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(showTimeContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(background);
        scrollPane.setBackground(background);
        scrollPane.setBounds(10,310,460,330);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        frame.add(scrollPane);

        hall = new Hall[100];
        showTime = new ShowTime[100];
        loadHall();
        loadShowTime();

        LocalDateTime today = LocalDateTime.now();
        LinkedHashSet<LocalDate> seenDates = new LinkedHashSet<>();
        boolean hasShowtime = false;

        for (int i = 0 ; i < showTimeCount ; i++){
            LocalDateTime d = showTime[i].getStartTime();
            if (!d.isBefore(today) && d.isBefore(today.plusWeeks(1))) {
                hasShowtime = true;
                if (seenDates.add(d.toLocalDate())) {
                    showDatePanel.add(dateButton(d));
                }
            }
        }
         if (hasShowtime) {

            LocalDate firstDate = seenDates.iterator().next();
            datePanel(firstDate);
        } else {
            JLabel emptyLabel = new JLabel("No show times in the next 7 days.");
            emptyLabel.setForeground(textWhite);
            emptyLabel.setFont(new Font("Courier New", Font.BOLD, 14));
            emptyLabel.setHorizontalAlignment(JLabel.CENTER);
            showTimeContainer.add(emptyLabel);
        }
 
        frame.setVisible(true);
    }


    private void loadHall(){
        try(BufferedReader readLine = new BufferedReader(new FileReader("Hall.txt"))){
            String line;
            while((line = readLine.readLine()) != null){
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                hall[hallCount++] = new Hall(parts[0],parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]),Double.parseDouble(parts[4]));

            }
        }
        catch (IOException e){
            System.out.println("Error reading hall file" + e.getMessage());
        }
    }

    private void loadShowTime(){
        try(BufferedReader readLine = new BufferedReader(new FileReader("Showtime.txt"))){
            String line;
            
            while((line = readLine.readLine()) != null){
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                if(parts.length < 4) continue;
                
                if(!parts[0].trim().equals(movie.getTitle())) continue;
                
                String dateTimeStr = parts[1].trim() + " " + parts[2].trim();
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mm a", Locale.ENGLISH);
                LocalDateTime dt = LocalDateTime.parse(dateTimeStr, fmt);


                showTime[showTimeCount++] = new ShowTime(
                    parts[0].trim(),
                    parts[3].trim(),
                    dt
                );
            }
            Arrays.sort(showTime,0,showTimeCount, Comparator.comparing(ShowTime::getStartTime));
        }
        catch (IOException e){
            System.out.println("Error reading show time file" + e.getMessage());
        }
    }
    
    private void showMoreInfo(){
        JDialog dialog = new JDialog(frame, "Movie Information", true);
        dialog.setSize(500, 700);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(background);

        // ===== Poster =====
        JLabel posterLabel = new JLabel();
        Image img = movie.getPoster().getImage().getScaledInstance(120, 180, Image.SCALE_SMOOTH);
        posterLabel.setIcon(new ImageIcon(img));
        posterLabel.setHorizontalAlignment(JLabel.CENTER);

        dialog.add(posterLabel, BorderLayout.NORTH);

        // ===== Info =====
        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setBackground(background);
        info.setForeground(textWhite);
        info.setFont(new Font("Courier New", Font.PLAIN, 13));

        info.setText(
            "Title: " + movie.getTitle() + "\n\n" +
            "Genre: \n" + movie.getGenre() + "\n\n" +
            "Language: \n" + movie.getLanguage() + "\n\n" +
            "Rating: \n" + movie.getRating() + "\n\n" +
            "Release Date: \n" + formatDate(movie.getDate()) + "\n\n" +
            "Duration: \n" + movie.getDuration() + " mins\n\n" +
            "Director: \n" + movie.getDirector() + "\n\n" +
            "Cast: \n" + movie.getCast() + "\n\n" +
            "Subtitles: \n" + movie.getSubtitles() + "\n\n" +
            "Description:\n" + movie.getDescription()
        );

        JScrollPane scroll = new JScrollPane(info);
        scroll.setBorder(null);

        dialog.add(scroll, BorderLayout.CENTER);

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
    
    private String formatDate(Date date) {
        if (date == null) return "N/A";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    private JButton dateButton(LocalDateTime date){
        dateButton = new JButton(date.format(dateFmt));
        dateButton.setBackground(lightGrey);
        dateButton.setBorderPainted(false);
        dateButton.setFocusable(false);
        dateButton.setFont(new Font("Courier New",Font.PLAIN,13));
        dateButton.setHorizontalAlignment(JButton.CENTER);
        dateButton.addActionListener(this);
        dateButton.setActionCommand(date.toLocalDate().toString());
        dateButton.setForeground(textWhite);
        return dateButton;
    }

    private JButton showTimeButton(ShowTime showTime, LocalTime startTime, String hallType) {
        showtimeButton = new JButton(startTime.format(timeFmt) + "  " + hallType);
        showtimeButton.setBackground(lightGrey);
        showtimeButton.setBorderPainted(false);
        showtimeButton.setFocusable(false);
        showtimeButton.setFont(new Font("Courier New", Font.PLAIN, 13));
        showtimeButton.setHorizontalAlignment(JButton.CENTER);
        showtimeButton.addActionListener(e -> callSeatSelect(showTime));
        showtimeButton.setForeground(textWhite);
        showtimeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        showtimeButton.setPreferredSize(new Dimension(400, 40));
        showtimeButton.setMaximumSize(new Dimension(400, 40));
        return showtimeButton;
    }

    private void datePanel(LocalDate date){
        showTimeContainer.removeAll();
        String hallType="";
        
        for (int i = 0; i < showTimeCount; i++) {
            ShowTime st = showTime[i];
            if (!st.getStartTime().toLocalDate().isEqual(date)) continue;
 
            // Look up hall type from the hall array
            for (int j = 0; j < hallCount; j++) {
                if (hall[j].getName().equalsIgnoreCase(st.getHallName())) {
                    hallType = hall[j].getHallType();
                    break;
                }
            }
 
            LocalTime startTime = st.getStartTime().toLocalTime();
            showTimeContainer.add(showTimeButton(st, startTime, hallType));
            showTimeContainer.add(Box.createVerticalStrut(10));
        }
        showTimeContainer.revalidate(); // refresh layout
        showTimeContainer.repaint();
    }

    public void callSeatSelect(ShowTime showTime){
        frame.setVisible(false);
        new SeatSelection(frame, showTime, username);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            frame.dispose();
            homeFrame.setVisible(true);
        } else {
            try {
                LocalDate selectedDate = LocalDate.parse(e.getActionCommand());
                datePanel(selectedDate);
            } catch (Exception ex) {
                System.out.println("Unknown action: " + e.getActionCommand());
            }
        }
    }
}

