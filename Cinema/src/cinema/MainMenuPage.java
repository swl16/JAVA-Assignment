package cinema;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPage extends JFrame implements ActionListener {

    JFrame frame = new JFrame("TGC Cinema");
    JPanel homePanel;
    JPanel profilePanel;
    JPanel cardPanel;
    CardLayout cardLayout;
    JButton mainPageButton;
    JButton FnBButton;
    JButton profileButton;
    JButton myTicket;
    JButton orderHistory;
    JButton contactUs;
    JButton logOut;

    public MainMenuPage(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setSize(500,700);
        frame.getContentPane().setBackground(new Color(0x242424));
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
        buttonPanel.setBackground(new Color(0x242424));

        mainPageButton = new JButton("Main Menu");
        mainPageButton.setFocusable(false);
        mainPageButton.setFont(new Font("Courier New",Font.BOLD,20));
        mainPageButton.addActionListener(this);
        mainPageButton.setForeground(new Color(0xF7F7F7));
        mainPageButton.setBackground(new Color(0xD44444));
        buttonPanel.add(mainPageButton);

        FnBButton = new JButton("F & B");
        FnBButton.setFocusable(false);
        FnBButton.setFont(new Font("Courier New",Font.BOLD,20));
        FnBButton.addActionListener(this);
        FnBButton.setForeground(new Color(0xF7F7F7));
        FnBButton.setBackground(new Color(0xD44444));
        buttonPanel.add(FnBButton);

        profileButton = new JButton("Profile");
        profileButton.setFocusable(false);
        profileButton.setFont(new Font("Courier New",Font.BOLD,20));
        profileButton.addActionListener(this);
        profileButton.setForeground(new Color(0xF7F7F7));
        profileButton.setBackground(new Color(0xD44444));
        buttonPanel.add(profileButton);

        frame.add(buttonPanel,BorderLayout.SOUTH);


    }

    private JPanel createHomePanel(){
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS)); // Stack items vertically
        headerPanel.setBackground(new Color(0x242424));

        JLabel titleLabel = new JLabel("TGC Cinema");
        titleLabel.setForeground(new Color(0xF7F7F7));
        titleLabel.setFont(new Font("Courier New",Font.BOLD,30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(35,140,15,0));
        headerPanel.add(titleLabel);

        JLabel nowShowing = new JLabel("Now Showing");
        nowShowing.setForeground(new Color(0xF7F7F7));
//        nowShowing.setBackground(new Color(0xD44444));
//        nowShowing.setOpaque(true);
        nowShowing.setFont(new Font("Courier New",Font.BOLD,20));
        nowShowing.setBorder(BorderFactory.createEmptyBorder(10,75,0,0));
        headerPanel.add(nowShowing);

        JPanel moviePanel = new JPanel();
        moviePanel.setLayout(new BoxLayout(moviePanel,BoxLayout.Y_AXIS));
        moviePanel.setBackground(new Color(0x3B3B3B));
        moviePanel.setPreferredSize(new Dimension(330,1000));

        for (int i = 1; i <= 10; i++) {
            moviePanel.add(Box.createVerticalStrut(10));
            moviePanel.add(Box.createHorizontalStrut(10));
            moviePanel.add(new JButton("Movie " + i));
        }

        JButton moreButton = new JButton("More...");
        moreButton.setFocusable(false);
        moreButton.setFont(new Font("Courier New",Font.BOLD,20));
        //moreButton.addActionListener(this);
        moreButton.setForeground(new Color(0xF7F7F7));
        moreButton.setBackground(new Color(0xD44444));
        moviePanel.add(moreButton);

        JScrollPane scrollPane = new JScrollPane(moviePanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setPreferredSize(new Dimension(350,450));
        scrollPane.setBackground(new Color(0x3B3B3B));

        //scroll bar ui 待定
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(0x444444);      // The moving part
                this.trackColor = new Color(0x242424);      // The background track
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton(); // Remove the arrow button
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton(); // Remove the arrow button
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
        });

// Make it thinner
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(new Color(0x242424));
        wrapper.add(scrollPane);

        headerPanel.add(wrapper,BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel MovieCard(){
        JPanel movie = new JPanel();
        movie.setBackground(new Color(0x3B3B3B));



        return movie;
    }

    private JPanel createProfilePanel(){
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(null);
        profilePanel.setBackground(new Color(0x242424));

        JLabel welcomeUser = new JLabel("Welcome, ");
        welcomeUser.setForeground(new Color(0xF7F7F7));
        welcomeUser.setFont(new Font("Courier New",Font.BOLD,40));
        welcomeUser.setBounds(40,60,500,50);
        profilePanel.add(welcomeUser);

        myTicket = new JButton("My Ticket");
        myTicket.setBorderPainted(false);
//        myTicket.setFocusPainted(false);
//        myTicket.setContentAreaFilled(false);
        myTicket.setFocusable(false);
        myTicket.setBounds(40,170,400,40);
        myTicket.setFont(new Font("Courier New",Font.PLAIN,25));
        myTicket.setHorizontalAlignment(JButton.LEFT);
        myTicket.addActionListener(this);
        myTicket.setForeground(new Color(0xF7F7F7));
        myTicket.setBackground(new Color(0x3B3B3B));
        profilePanel.add(myTicket);

        orderHistory = new JButton("Order History");
        orderHistory.setBorderPainted(false);
//        orderHistory.setFocusPainted(false);
//        orderHistory.setContentAreaFilled(false);
        orderHistory.setFocusable(false);
        orderHistory.setBounds(40,230,400,40);
        orderHistory.setFont(new Font("Courier New",Font.PLAIN,25));
        orderHistory.setHorizontalAlignment(JButton.LEFT);
        orderHistory.addActionListener(this);
        orderHistory.setForeground(new Color(0xF7F7F7));
        orderHistory.setBackground(new Color(0x3B3B3B));
        profilePanel.add(orderHistory);

        contactUs = new JButton("Contact Us");
        contactUs.setBorderPainted(false);
//        contactUs.setFocusPainted(false);
//        contactUs.setContentAreaFilled(false);
        contactUs.setFocusable(false);
        contactUs.setBounds(40,290,400,40);
        contactUs.setFont(new Font("Courier New",Font.PLAIN,25));
        contactUs.setHorizontalAlignment(JButton.LEFT);
        contactUs.addActionListener(this);
        contactUs.setForeground(new Color(0xF7F7F7));
        contactUs.setBackground(new Color(0x3B3B3B));
        profilePanel.add(contactUs);

        logOut = new JButton("Log Out");
        logOut.setBorderPainted(false);
//        logOut.setFocusPainted(false);
//        logOut.setContentAreaFilled(false);
        logOut.setFocusable(false);
        logOut.setBounds(160,500,180,50);
        logOut.setFont(new Font("Courier New",Font.BOLD,30));
        //logOut.setHorizontalAlignment(JButton.CENTER);
        logOut.addActionListener(this);
        logOut.setForeground(new Color(0xF7F7F7));
        logOut.setBackground(new Color(0xD44444));
        profilePanel.add(logOut);

        return profilePanel;
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mainPageButton) {
            cardLayout.show(cardPanel, "home");
        } else if (e.getSource() == FnBButton) {
            frame.dispose();

        } else if (e.getSource() == profileButton) {
            cardLayout.show(cardPanel, "profile");

        } else if (e.getSource() == myTicket) {
            frame.setVisible(false);
            new SubWindowProfile(frame,1);

        } else if (e.getSource() == orderHistory) {
            frame.setVisible(false);
            new SubWindowProfile(frame,2);

        } else if (e.getSource() == contactUs) {
            frame.setVisible(false);
            new SubWindowProfile(frame,3);

        } else if (e.getSource() == logOut) {
            frame.dispose();
            new LoginPage();

        }
    }

    //testing use only
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenuPage());
    }
}
