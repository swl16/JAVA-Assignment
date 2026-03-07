package cinema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SubWindowProfile extends JFrame implements ActionListener {
    JFrame frame = new JFrame("TGC Cinema");
    JFrame homeFrame;
    JPanel cardPanel;
    CardLayout cardLayout;
    JButton backButton1;
    JButton backButton2;
    JButton backButton3;

    public SubWindowProfile(JFrame homeFrame, int choice){
        this.homeFrame = homeFrame;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(500,700);
        frame.getContentPane().setBackground(new Color(0x242424));
        frame.setVisible(true);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel ticketPanel = createTicketPanel();
        JPanel historyPanel = createHistoryPanel();
        JPanel contactUsPanel = createContactUsPanel();

        cardPanel.add(ticketPanel,"ticket");
        cardPanel.add(historyPanel,"history");
        cardPanel.add(contactUsPanel,"contactUs");

        frame.add(cardPanel);

        switch (choice) {
            case 1 -> cardLayout.show(cardPanel, "ticket");
            case 2 -> cardLayout.show(cardPanel, "history");
            case 3 -> cardLayout.show(cardPanel, "contactUs");
        }

    }

    private JPanel createTicketPanel(){
        JPanel ticketPanel = new JPanel();
        ticketPanel.setLayout(null);
        ticketPanel.setBackground(new Color(0x242424));

        backButton1 = new JButton("< Back");
        backButton1.setBorderPainted(false);
        backButton1.setFocusPainted(false);
        backButton1.setContentAreaFilled(false);
        backButton1.setFocusable(false);
        backButton1.setBounds(8,10,400,40);
        backButton1.setFont(new Font("Courier New",Font.PLAIN,17));
        backButton1.setHorizontalAlignment(JButton.LEFT);
        backButton1.addActionListener(this);
        backButton1.setForeground(new Color(0xF7F7F7));
        backButton1.setBackground(new Color(0x3B3B3B));
        ticketPanel.add(backButton1);

        JLabel ticketLabel = new JLabel("My Ticket");
        ticketLabel.setForeground(new Color(0xF7F7F7));
        ticketLabel.setFont(new Font("Courier New",Font.BOLD,40));
        ticketLabel.setBounds(40,60,500,50);
        ticketPanel.add(ticketLabel);

        return ticketPanel;
    }

    private JPanel createHistoryPanel(){
        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(null);
        historyPanel.setBackground(new Color(0x242424));

        backButton2 = new JButton("< Back");
        backButton2.setBorderPainted(false);
        backButton2.setFocusPainted(false);
        backButton2.setContentAreaFilled(false);
        backButton2.setFocusable(false);
        backButton2.setBounds(8,10,400,40);
        backButton2.setFont(new Font("Courier New",Font.PLAIN,17));
        backButton2.setHorizontalAlignment(JButton.LEFT);
        backButton2.addActionListener(this);
        backButton2.setForeground(new Color(0xF7F7F7));
        backButton2.setBackground(new Color(0x3B3B3B));
        historyPanel.add(backButton2);

        JLabel historyLabel = new JLabel("Order History");
        historyLabel.setForeground(new Color(0xF7F7F7));
        historyLabel.setFont(new Font("Courier New",Font.BOLD,40));
        historyLabel.setBounds(40,60,500,50);
        historyPanel.add(historyLabel);

        return historyPanel;
    }

    private JPanel createContactUsPanel(){
        JPanel contactUsPanel = new JPanel();
        contactUsPanel.setLayout(null);
        contactUsPanel.setBackground(new Color(0x242424));

        backButton3 = new JButton("< Back");
        backButton3.setBorderPainted(false);
        backButton3.setFocusPainted(false);
        backButton3.setContentAreaFilled(false);
        backButton3.setFocusable(false);
        backButton3.setBounds(8,10,400,40);
        backButton3.setFont(new Font("Courier New",Font.PLAIN,17));
        backButton3.setHorizontalAlignment(JButton.LEFT);
        backButton3.addActionListener(this);
        backButton3.setForeground(new Color(0xF7F7F7));
        backButton3.setBackground(new Color(0x3B3B3B));
        contactUsPanel.add(backButton3);

        JLabel contactLabel = new JLabel("Contact Us");
        contactLabel.setForeground(new Color(0xF7F7F7));
        contactLabel.setFont(new Font("Courier New",Font.BOLD,40));
        contactLabel.setBounds(40,60,500,50);
        contactUsPanel.add(contactLabel);

        return contactUsPanel;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton1 || e.getSource() == backButton2 || e.getSource() == backButton3){
            frame.dispose();
            homeFrame.setVisible(true);
        }

    }

    //testing use only
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SubWindowProfile(new JFrame(),1));
    }
}
