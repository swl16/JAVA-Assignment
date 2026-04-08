package cinema;

import javax.swing.*;
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
    JFrame frame = new JFrame("TGC Cinema - Movie Detail");
    String username;
    JFrame homeFrame;
    Movie movie;
    JButton backButton;
    Hall[] hall;
    ShowTime[] showTime;
    JButton showtimebutton;
    JButton dateButton;
    private int hallCount=0;
    private int showTimeCount = 0;
    private JPanel showTimeContainer;

    private static final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd MMM");


    MovieDetailPage (JFrame homeFrame, Movie movieSelected,String username){
        this.username = username;
        movie = movieSelected;
        this.homeFrame = homeFrame;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setSize(500,700);
        frame.getContentPane().setBackground(new Color(0x242424));
        frame.setVisible(true);
        
        backButton = new JButton("< Back");
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusable(false);
        backButton.setBounds(8,10,400,40);
        backButton.setFont(new Font("Courier New",Font.PLAIN,17));
        backButton.setHorizontalAlignment(JButton.LEFT);
        backButton.addActionListener(this);
        backButton.setForeground(new Color(0xF7F7F7));
        frame.add(backButton);

        JLabel posterLabel = new JLabel();
        Image scaledPoster = movie.getPoster().getImage().getScaledInstance(108, 160, Image.SCALE_SMOOTH);
        ImageIcon poster = new ImageIcon(scaledPoster);
        posterLabel.setForeground(new Color(0xF7F7F7));
        posterLabel.setFont(new Font("Courier New",Font.PLAIN,15));
        posterLabel.setIcon(poster);
        posterLabel.setText(movie.getTitle());
        posterLabel.setHorizontalAlignment(JButton.CENTER);
        posterLabel.setHorizontalTextPosition(JButton.CENTER);
        posterLabel.setVerticalTextPosition(JButton.BOTTOM);
        posterLabel.setBounds(0,50,500,200);
        frame.add(posterLabel);
        
        JButton info = new JButton("More Info");
        info.setBounds(180, 220, 140, 30);
        info.setBackground(new Color(0x3B3B3B));
        info.setForeground(new Color(0xF7F7F7));
        info.setFocusPainted(false);
        info.setBorderPainted(false);

        info.addActionListener(e -> showMoreInfo());

        frame.add(info);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panel.setBackground(new Color(0x242424));
        panel.setBounds(0,260,460,60);
        frame.add(panel);

        showTimeContainer = new JPanel(new GridLayout(0, 1, 5, 5));
        showTimeContainer.setBackground(new Color(0x242424));
        showTimeContainer.setBounds(20, 330, 460, 300);
        frame.add(showTimeContainer);

        hall = new Hall[100];
        showTime = new ShowTime[100];
        loadHall();
        loadShowTime();

        LocalDate now = LocalDate.now();
        LinkedHashSet<LocalDate> seenDates = new LinkedHashSet<>();
        boolean isEmpty = false;

        for (int i = 0 ; i < showTimeCount ; i++){
            if (!showTime[i].getStartTime().toLocalDate().isBefore(now) && showTime[i].getStartTime().toLocalDate().isBefore(now.plusWeeks(1))){
                isEmpty = true;
                if (seenDates.add(showTime[i].getStartTime().toLocalDate())) {
                    panel.add(dateButton(showTime[i].getStartTime().toLocalDate()));
                }
            }
        }
        datePanel(now);
        if (!isEmpty) {
            JLabel emptyLabel = new JLabel("There is no show time in the next 7 days.");
            emptyLabel.setForeground(new Color(0xF7F7F7));
            emptyLabel.setFont(new Font("Courier New", Font.BOLD, 15));
            emptyLabel.setBounds(0, 400, 500, 50);
            emptyLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(emptyLabel);
        }
    }


    public void loadHall(){
        try(BufferedReader readLine = new BufferedReader(new FileReader("Hall.txt"))){
            String line;
            int i = 0;
            while((line = readLine.readLine()) != null){
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(" , ");
                hall[i] = new Hall(parts[0],parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]),Double.parseDouble(parts[4]));
                i++;
                hallCount++;
            }
        }
        catch (IOException e){
            System.out.println("Error reading hall file");
        }
    }

    public void loadShowTime(){
        try(BufferedReader readLine = new BufferedReader(new FileReader("Showtime.txt"))){
            String line;
            int i = 0;
            
            while((line = readLine.readLine()) != null){
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(" , ");

            if(parts.length == 3 && parts[0].equals(movie.getTitle())){

                showTime[i] = new ShowTime(
                    parts[0],
                    parts[1],
                    LocalDateTime.parse(parts[2])
                );
                    i++;
                    showTimeCount++;
                }
            }
            Arrays.sort(showTime,0,showTimeCount, Comparator.comparing(ShowTime::getStartTime));
        }
        catch (IOException e){
            System.out.println("Error reading show time file");
        }
    }
    
    private void showMoreInfo(){
        JDialog dialog = new JDialog(frame, "Movie Information", true);
        dialog.setSize(500, 700);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(0x242424));

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
        info.setBackground(new Color(0x242424));
        info.setForeground(new Color(0xF7F7F7));
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

    public JButton dateButton(LocalDate date){
        dateButton = new JButton(date.format(dateFmt));
        dateButton.setBackground(new Color(0x3B3B3B));
        dateButton.setBorderPainted(false);
        dateButton.setFocusable(false);
        dateButton.setFont(new Font("Courier New",Font.PLAIN,13));
        dateButton.setHorizontalAlignment(JButton.CENTER);
        dateButton.addActionListener(this);
        dateButton.setActionCommand(date.toString());
        dateButton.setForeground(new Color(0xF7F7F7));
        return dateButton;
    }

    public JButton showTimeButton(ShowTime showTime, LocalTime startTime, String hallType){
        showtimebutton = new JButton(startTime.format(timeFmt)+"  "+ hallType);
        showtimebutton.setBackground(new Color(0x3B3B3B));
        showtimebutton.setBorderPainted(false);
        showtimebutton.setFocusable(false);
        showtimebutton.setFont(new Font("Courier New",Font.PLAIN,13));
        showtimebutton.setHorizontalAlignment(JButton.CENTER);
       // showtimebutton.setHorizontalTextPosition(JButton.CENTER);
       // showtimebutton.setVerticalTextPosition(JButton.BOTTOM);
       // showtimebutton.setActionCommand(String.valueOf(index));
        showtimebutton.addActionListener(e -> callSeatSelect(showTime));
        showtimebutton.setForeground(new Color(0xF7F7F7));
        return showtimebutton;
    }

    public void datePanel(LocalDate date){
        showTimeContainer.removeAll();
        String hallType="";

        for (int i = 0; i < showTimeCount; i++) {
            if (showTime[i].getStartTime().toLocalDate().isEqual(date)) {
                for (int j = 0; j < hallCount; j++) {
                    if (showTime[i].getHallName().equals(hall[j].getName())) {
                        hallType = hall[j].getHallType();
                        break;
                    }
                }
                showTimeContainer.add(showTimeButton(showTime[i], showTime[i].getStartTime().toLocalTime(), hallType));
            }
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

 class Movie{
    private String title;
    private String genre;
    private String language;
    private Date date;
    private String rating;
    private int duration;
    private String director;
    private String cast;
    private String subtitles;
    private String description;
    private ImageIcon poster;

    Movie(String title, String genre, String language,String rating,Date date,int duration, String director, String cast, String subtitles, String description, ImageIcon poster){
        this.title = title;
        this.genre = genre;
        this.language = language;
        this.date = date;
        this.rating = rating;
        this.duration = duration;
        this.director = director;
        this.cast = cast;
        this.subtitles = subtitles;
        this.description = description;
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(String subtitles) {
        this.subtitles = subtitles;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageIcon getPoster() {
        return poster;
    }

    public void setPoster(ImageIcon poster) {
        this.poster = poster;
    }
    
    
    
}
