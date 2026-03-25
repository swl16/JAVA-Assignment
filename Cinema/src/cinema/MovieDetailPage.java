package cinema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MovieDetailPage implements ActionListener {
    JFrame frame = new JFrame("TGC Cinema - Movie Detail");
    JFrame homeFrame;
    Movie movie;
    JButton backButton;
//    JPanel cardPanel;
//    CardLayout cardLayout;

    MovieDetailPage (JFrame homeFrame, Movie movieSelected){
        this.homeFrame = homeFrame;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(500,700);
        frame.getContentPane().setBackground(new Color(0x242424));
        frame.setVisible(true);

//        cardLayout = new CardLayout();
//        cardPanel = new JPanel(cardLayout);

        JPanel moviePanel = new JPanel();
        moviePanel.setLayout(null);
        moviePanel.setBackground(new Color(0x242424));
        
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
        backButton.setBackground(new Color(0x3B3B3B));
        frame.add(backButton);

        JLabel posterLabel = new JLabel();
        Image scaledPoster = movieSelected.getPoster().getImage().getScaledInstance(108, 160, Image.SCALE_SMOOTH);
        ImageIcon poster = new ImageIcon(scaledPoster);
        posterLabel.setBackground(new Color(0x3B3B3B));
        posterLabel.setForeground(new Color(0xF7F7F7));
        posterLabel.setFont(new Font("Courier New",Font.PLAIN,15));
        posterLabel.setIcon(poster);
        posterLabel.setText(movieSelected.getTitle());
        posterLabel.setHorizontalAlignment(JButton.CENTER);
        posterLabel.setHorizontalTextPosition(JButton.CENTER);
        posterLabel.setVerticalTextPosition(JButton.BOTTOM);
        posterLabel.setBounds(0,10,500,200);
        frame.add(posterLabel);





    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton){
            frame.dispose();
            homeFrame.setVisible(true);
        }
    }
}

 class Movie{
    String title;
    String genre;
    String duration;
    ImageIcon poster;

    Movie(String title, String genre, String duration, ImageIcon poster){
        this.title = title;
        this.genre = genre;
        this.duration =duration;
        this.poster = poster;
    }

     public String getTitle() {
         return title;
     }

     public String getGenre() {
         return genre;
     }

     public String getDuration() {
         return duration;
     }

     public ImageIcon getPoster() {
         return poster;
     }
 }
