package cinema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MovieDetailPage implements ActionListener {
    JFrame frame = new JFrame("TGC Cinema - Movie Detail");
    JFrame homeFrame;
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




    }

    @Override
    public void actionPerformed(ActionEvent e) {

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
