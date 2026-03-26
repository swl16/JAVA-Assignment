
package cinema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;


public class AdminPage implements ActionListener{
    
    JFrame frame = new JFrame("TGC Cinema - Admin Page");
    JPanel adminpanel;
    
    Movie movie;
    JButton addButton;
    JButton backButton;
    JButton cancelButton;
    JButton editButton;
    JButton deleteButton;
    JTextField titleEnter;
    JTextField genreEnter;
    JTextField ratingEnter;
    JTextField durationEnter;
    JTextField castEnter;
    JTextField directorEnter;
    JTextField languageEnter;
    JTextField releasedateEnter;
    JTextField subtitlesEnter;
    JTextField showtimeEnter;
    JTextField hallEnter;
    JTextField descriptionEnter;
    ImageIcon poster;
    
    CardLayout cardLayout;
    
    
    public AdminPage(){
        frame.setSize(500,700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.getContentPane().setBackground(new Color(0x242424));
        frame.setVisible(true);
        
        cardLayout = new CardLayout();
        adminpanel = new JPanel(cardLayout);
        
        adminpanel.add(menu(), "MENU");
        adminpanel.add(addmovie(), "Add Movie");
        adminpanel.add(viewmovie(), "View Movie");
        adminpanel.add(showtime(), "Show Time");
        adminpanel.add(checkstock(), "Check Stock");
        adminpanel.add(replenishstock(), "Replenish Stock");
        
        frame.add(adminpanel);
        frame.setVisible(true);
        
    }
    
    public JPanel menu(){
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel title = new JLabel("TGC Cinema Admin", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
    }
    
    public JPanel addmovie(){
    
    }
    
    public JPanel viewmovie(){
    
    }
    
    public JPanel showtime(){
    
    }
    
    public JPanel checkstock(){
    
    }
    
    public JPanel replenishstock(){
    
    }
}

