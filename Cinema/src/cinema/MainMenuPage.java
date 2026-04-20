package cinema;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class MainMenuPage implements ActionListener {
    private JFrame frame = new JFrame("TGC Cinema");
    private JPanel homePanel, profilePanel, cardPanel;
    private CardLayout cardLayout;
    private JButton movie,mainPageButton,FnBButton,profileButton;
    private JButton myTicket,contactUs,setting,logOut;
    //UI color
    private final Color background = new Color(0x242424);
    private final Color buttonRed = new Color(0xD44444);
    private final Color textWhite = new Color(0xF7F7F7);
    private final Color lightGrey = new Color(0x3B3B3B);

    private List<Movie> movieDetail = new ArrayList<>();
    private String username;

    public MainMenuPage(String username){
        this.username = username;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setSize(500,700);
        frame.getContentPane().setBackground(background);
        frame.setVisible(true);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        homePanel = createHomePanel();
        profilePanel = createProfilePanel();

        cardPanel.add(homePanel,"home");
        cardPanel.add(profilePanel,"profile");

        frame.add(cardPanel);

        cardLayout.show(cardPanel,"home");

        //Button Below
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(background);

        mainPageButton = new JButton("Main Menu");
        mainPageButton.setFocusable(false);
        mainPageButton.setFont(new Font("Courier New",Font.BOLD,20));
        mainPageButton.addActionListener(this);
        mainPageButton.setForeground(textWhite);
        mainPageButton.setBackground(buttonRed);
        buttonPanel.add(mainPageButton);
        FnBButton = new JButton("F & B");
        FnBButton.setFocusable(false);
        FnBButton.setFont(new Font("Courier New",Font.BOLD,20));
        FnBButton.addActionListener(this);
        FnBButton.setForeground(textWhite);
        FnBButton.setBackground(buttonRed);
        buttonPanel.add(FnBButton);

        profileButton = new JButton("Profile");
        profileButton.setFocusable(false);
        profileButton.setFont(new Font("Courier New",Font.BOLD,20));
        profileButton.addActionListener(this);
        profileButton.setForeground(textWhite);
        profileButton.setBackground(buttonRed);
        buttonPanel.add(profileButton);

        frame.add(buttonPanel,BorderLayout.SOUTH);

    }

    private JPanel createHomePanel(){
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(background);

        JLabel titleLabel = new JLabel("TGC Cinema");
        titleLabel.setForeground(textWhite);
        titleLabel.setFont(new Font("Courier New",Font.BOLD,30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(35,140,15,0));
        headerPanel.add(titleLabel);

        JLabel nowShowing = new JLabel("Now Showing");
        nowShowing.setForeground(textWhite);
        nowShowing.setFont(new Font("Courier New",Font.BOLD,20));
        nowShowing.setBorder(BorderFactory.createEmptyBorder(10,75,0,0));
        headerPanel.add(nowShowing);

        JPanel moviePanel = new JPanel(new GridLayout(0,2,15,20));
        moviePanel.setBackground(lightGrey);
        moviePanel.setBorder(new EmptyBorder(10,10,10,10));

        loadMovie();

        int movieCount = 0;
        for (Movie movie : movieDetail) {
            moviePanel.add(MovieCard(movie.getTitle(),movie.getPoster(),movieCount));
            movieCount++;
        }

        moviePanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(moviePanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setPreferredSize(new Dimension(350,450));
        scrollPane.setBackground(lightGrey);

        // Make the scroll bar invisible
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(background);
        wrapper.add(scrollPane);

        headerPanel.add(wrapper,BorderLayout.CENTER);

        return headerPanel;
    }

    private void loadMovie() {
        try(BufferedReader readLine = new BufferedReader(new FileReader("MovieDetails.txt"))){
            String line;
            while((line = readLine.readLine()) != null){
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|", -1);
                if(parts.length == 11){
                    ImageIcon poster = new ImageIcon(parts[10]);
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                    Date date = null; // safe
                    
                    try {
                        date = sdf.parse(parts[4]);
                    } catch (ParseException e) {
                        date = new Date(); // fallback
                    }
                    movieDetail.add(new Movie(parts[0], parts[1], parts[2], parts[3], date, Integer.parseInt(parts[5]), parts[6], parts[7], parts[8], parts[9], poster));
                }
            }
        }
        catch (IOException e){
        System.out.println("Error reading movies file");
        }
    }

    private JButton MovieCard(String title, ImageIcon poster, int index){
        movie = new JButton(title);
        Image scaledPoster = poster.getImage().getScaledInstance(90, 130, Image.SCALE_SMOOTH);
        poster = new ImageIcon(scaledPoster);
        movie.setBackground(lightGrey);
        movie.setBorderPainted(false);
        movie.setFocusable(false);
        movie.setFont(new Font("Courier New",Font.PLAIN,15));
        movie.setIcon(poster);
        movie.setHorizontalAlignment(JButton.CENTER);
        movie.setHorizontalTextPosition(JButton.CENTER);
        movie.setVerticalTextPosition(JButton.BOTTOM);
        movie.setActionCommand(String.valueOf(index));
        movie.addActionListener(this);
        movie.setForeground(textWhite);
        
        movie.setPreferredSize(new Dimension(120, 180));
        movie.setMaximumSize(new Dimension(120, 180));
        return movie;
    }

    private JPanel createProfilePanel(){
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(null);
        profilePanel.setBackground(background);

        JLabel welcomeUser = new JLabel("Welcome, " + username.substring(0, 1).toUpperCase() + username.substring(1));
        welcomeUser.setForeground(textWhite);
        welcomeUser.setFont(new Font("Courier New",Font.BOLD,40));
        welcomeUser.setBounds(40,60,500,50);
        profilePanel.add(welcomeUser);

        myTicket = new JButton("My Ticket");
        myTicket.setBorderPainted(false);
        myTicket.setFocusable(false);
        myTicket.setBounds(40,170,400,40);
        myTicket.setFont(new Font("Courier New",Font.PLAIN,25));
        myTicket.setHorizontalAlignment(JButton.LEFT);
        myTicket.addActionListener(this);
        myTicket.setForeground(textWhite);
        myTicket.setBackground(lightGrey);
        profilePanel.add(myTicket);

        contactUs = new JButton("Contact Us");
        contactUs.setBorderPainted(false);
        contactUs.setFocusable(false);
        contactUs.setBounds(40,230,400,40);
        contactUs.setFont(new Font("Courier New",Font.PLAIN,25));
        contactUs.setHorizontalAlignment(JButton.LEFT);
        contactUs.addActionListener(this);
        contactUs.setForeground(textWhite);
        contactUs.setBackground(lightGrey);
        profilePanel.add(contactUs);

        setting = new JButton("Setting");
        setting.setBorderPainted(false);
        setting.setFocusable(false);
        setting.setBounds(40,290,400,40);
        setting.setFont(new Font("Courier New",Font.PLAIN,25));
        setting.setHorizontalAlignment(JButton.LEFT);
        setting.addActionListener(this);
        setting.setForeground(textWhite);
        setting.setBackground(lightGrey);
        profilePanel.add(setting);

        logOut = new JButton("Log Out");
        logOut.setBorderPainted(false);
        logOut.setFocusable(false);
        logOut.setBounds(160,500,180,50);
        logOut.setFont(new Font("Courier New",Font.BOLD,30));
        logOut.addActionListener(this);
        logOut.setForeground(textWhite);
        logOut.setBackground(buttonRed);
        profilePanel.add(logOut);

        return profilePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String commandNum = e.getActionCommand();
        try{
            int index = Integer.parseInt(commandNum);
            Movie selectedMovie = movieDetail.get(index);
            frame.setVisible(false);
            new MovieDetailPage(frame,selectedMovie,username);

        }catch (NumberFormatException ex) {
            if (e.getSource() == mainPageButton) {
                cardLayout.show(cardPanel, "home");

            } else if (e.getSource() == FnBButton) {
                frame.dispose();
                new Concession(username);

            } else if (e.getSource() == profileButton) {
                cardLayout.show(cardPanel, "profile");

            } else if (e.getSource() == myTicket) {
                frame.setVisible(false);
                new SubWindowProfile(frame, 1, username);

            } else if (e.getSource() == contactUs) {
                frame.setVisible(false);
                new SubWindowProfile(frame, 2, username);

            } else if (e.getSource() == setting) {
                frame.setVisible(false);
                new SubWindowProfile(frame, 3, username);

            } else if (e.getSource() == logOut) {
                frame.dispose();
                new LoginPage();
            }
        }
    }

    //testing use only
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenuPage("tang"));
    }
}
