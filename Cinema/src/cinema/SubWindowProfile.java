package cinema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class SubWindowProfile extends JFrame implements ActionListener {
    JFrame frame = new JFrame("TGC Cinema");
    String username;
    JFrame homeFrame;
    JPanel cardPanel;
    CardLayout cardLayout;
    JButton backButton1,backButton2,backButton3,backButton4;
    JButton delAccBtn;

    Map<String,List<String[]>> combineOrder;


    public SubWindowProfile(JFrame homeFrame, int choice, String username){
        this.homeFrame = homeFrame;
        this.username = username;
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
        JPanel settingPanel = createSettingPanel();
        JPanel changePassPanel = createChangePassPanel();

        cardPanel.add(ticketPanel,"ticket");
        cardPanel.add(historyPanel,"history");
        cardPanel.add(contactUsPanel,"contactUs");
        cardPanel.add(settingPanel,"setting");
        cardPanel.add(changePassPanel, "changePass");

        frame.add(cardPanel);

        switch (choice) {
            case 1 : cardLayout.show(cardPanel, "ticket"); break;
            case 2 : cardLayout.show(cardPanel, "history"); break;
            case 3 : cardLayout.show(cardPanel, "contactUs"); break;
            case 4 : cardLayout.show(cardPanel,"setting"); break;
        }

    }

    private JPanel createTicketPanel(){
        JPanel ticketPanel = new JPanel();
        ticketPanel.setLayout(null);
        ticketPanel.setBackground(new Color(0x242424));

        loadUserOrder();

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

    private JPanel createOrderPanel(String movieName, LocalDateTime dateTime, String hallName, String seatId){
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(0x3B3B3B));

        JLabel movieLabel = new JLabel(movieName);
        movieLabel.setForeground(new Color(0xF7F7F7));
        movieLabel.setFont(new Font("Courier New",Font.BOLD,20));
        movieLabel.setBounds(10,10,200,20);
        panel.add(movieLabel);



        return panel;
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

    private JPanel createSettingPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(0x242424));

        backButton4 = new JButton("< Back");
        backButton4.setBorderPainted(false);
        backButton4.setFocusPainted(false);
        backButton4.setContentAreaFilled(false);
        backButton4.setFocusable(false);
        backButton4.setBounds(8,10,400,40);
        backButton4.setFont(new Font("Courier New",Font.PLAIN,17));
        backButton4.setHorizontalAlignment(JButton.LEFT);
        backButton4.addActionListener(this);
        backButton4.setForeground(new Color(0xF7F7F7));
        panel.add(backButton4);

        JLabel settingLabel = new JLabel("Setting");
        settingLabel.setForeground(new Color(0xF7F7F7));
        settingLabel.setFont(new Font("Courier New",Font.BOLD,40));
        settingLabel.setBounds(40,60,500,50);
        panel.add(settingLabel);

        JLabel securityLabel = new JLabel("Security");
        securityLabel.setForeground(new Color(0x6e7075));
        securityLabel.setFont(new Font("Courier New",Font.BOLD,20));
        securityLabel.setBounds(40,120,500,30);
        panel.add(securityLabel);

        JButton changePassBtn = new JButton("Change Password                   >");
        changePassBtn.setBorderPainted(false);
        changePassBtn.setFocusPainted(false);
        changePassBtn.setContentAreaFilled(false);
        changePassBtn.setFocusable(false);
        changePassBtn.setBounds(40,150,400,30);
        changePassBtn.setFont(new Font("Courier New",Font.PLAIN,17));
        changePassBtn.setHorizontalAlignment(JButton.LEFT);
        changePassBtn.addActionListener(e -> cardLayout.show(cardPanel, "changePass"));
        changePassBtn.setForeground(new Color(0xF7F7F7));
        panel.add(changePassBtn);

        JLabel accManageLabel = new JLabel("Account Management");
        accManageLabel.setForeground(new Color(0x6e7075));
        accManageLabel.setFont(new Font("Courier New",Font.BOLD,20));
        accManageLabel.setBounds(45,200,500,30);
        panel.add(accManageLabel);

        delAccBtn = new JButton("Delete Account                    >");
        delAccBtn.setBorderPainted(false);
        delAccBtn.setFocusPainted(false);
        delAccBtn.setContentAreaFilled(false);
        delAccBtn.setFocusable(false);
        delAccBtn.setBounds(40,230,400,30);
        delAccBtn.setFont(new Font("Courier New",Font.PLAIN,17));
        delAccBtn.setHorizontalAlignment(JButton.LEFT);
        delAccBtn.addActionListener(this);
        delAccBtn.setForeground(new Color(0xF7F7F7));
        panel.add(delAccBtn);



        return panel;
    }

    private JPanel createChangePassPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(0x242424));

        JButton backButton = new JButton("< Back");
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusable(false);
        backButton.setBounds(8,10,400,40);
        backButton.setFont(new Font("Courier New",Font.PLAIN,17));
        backButton.setHorizontalAlignment(JButton.LEFT);
        backButton.addActionListener(e -> cardLayout.show(cardPanel,"setting") );
        backButton.setForeground(new Color(0xF7F7F7));
        backButton.setBackground(new Color(0x3B3B3B));
        panel.add(backButton);

        JLabel changePassLabel = new JLabel("Change Password");
        changePassLabel.setForeground(new Color(0xF7F7F7));
        changePassLabel.setFont(new Font("Courier New",Font.BOLD,30));
        changePassLabel.setBounds(40,60,500,50);
        panel.add(changePassLabel);

        JLabel tipsLabel = new JLabel("*Password must at least 6 characters.");
        tipsLabel.setForeground(new Color(0xD44444));
        tipsLabel.setFont(new Font("Courier New",Font.BOLD,12));
        tipsLabel.setBounds(40,120,500,15);
        panel.add(tipsLabel);

        JLabel currentPassLabel = new JLabel("Current Password");
        currentPassLabel.setForeground(new Color(0xF7F7F7));
        currentPassLabel.setFont(new Font("Courier New",Font.BOLD,20));
        currentPassLabel.setBounds(40,150,500,30);
        panel.add(currentPassLabel);

        JTextField currentPassType = new JTextField();
        currentPassType.setBounds(40,180,400,20);
        currentPassType.setFont(new Font("Courier New",Font.PLAIN,15));
        currentPassType.setForeground(new Color(0xF7F7F7));
        currentPassType.setBackground(new Color(0x3B3B3B));
        currentPassType.setCaretColor(new Color(0xF7F7F7));
        currentPassType.setBorder(null);
        panel.add(currentPassType);

        JLabel newPassLabel = new JLabel("New Password");
        newPassLabel.setForeground(new Color(0xF7F7F7));
        newPassLabel.setFont(new Font("Courier New",Font.BOLD,20));
        newPassLabel.setBounds(40,220,500,30);
        panel.add(newPassLabel);

        JTextField newPassType = new JTextField();
        newPassType.setBounds(40,250,400,20);
        newPassType.setFont(new Font("Courier New",Font.PLAIN,15));
        newPassType.setForeground(new Color(0xF7F7F7));
        newPassType.setBackground(new Color(0x3B3B3B));
        newPassType.setCaretColor(new Color(0xF7F7F7));
        newPassType.setBorder(null);
        panel.add(newPassType);

        JLabel repeatPassLabel = new JLabel("Re-type Password");
        repeatPassLabel.setForeground(new Color(0xF7F7F7));
        repeatPassLabel.setFont(new Font("Courier New",Font.BOLD,20));
        repeatPassLabel.setBounds(40,290,500,30);
        panel.add(repeatPassLabel);

        JTextField repeatPassType = new JTextField();
        repeatPassType.setBounds(40,320,400,20);
        repeatPassType.setFont(new Font("Courier New",Font.PLAIN,15));
        repeatPassType.setForeground(new Color(0xF7F7F7));
        repeatPassType.setBackground(new Color(0x3B3B3B));
        repeatPassType.setCaretColor(new Color(0xF7F7F7));
        repeatPassType.setBorder(null);
        panel.add(repeatPassType);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setBorderPainted(false);
        confirmButton.setFocusPainted(false);
        confirmButton.setFont(new Font("Courier New",Font.BOLD,15));
        confirmButton.setBounds(25,600,430,30);
        confirmButton.setForeground(new Color(0xF7F7F7));
        confirmButton.setBackground(new Color(0xD44444));
        confirmButton.addActionListener(e -> {

            String oldPassword = currentPassType.getText();
            String newPassword = newPassType.getText();
            String reTypePassword = repeatPassType.getText();

            changePassword(oldPassword, newPassword, reTypePassword);
        });
        panel.add(confirmButton);

        return panel;
    }

    public void loadUserOrder(){
        combineOrder = new LinkedHashMap<>();

        try(BufferedReader readLine = new BufferedReader(new FileReader("BookingDetail.txt"))){
            String line;
            int i = 0;

            while((line = readLine.readLine()) != null){
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|", -1);

                if (username.equals(parts[1])){
                    String[] order = {parts[2],parts[3], parts[6], parts[4] + parts[5]};
                    String id = parts[0] ;
//                    String dateTime = ;
//                    String hall = ;
//                    String seatId = ;

                    if (combineOrder.containsKey(id)){
                        List<String[]> existing = combineOrder.get(id);
                        existing.add(order);
                    } else {
                        List<String[]> newList = new ArrayList<>();
                        newList.add(order);
                        combineOrder.put(id,newList);
                    }
                }

            }
        }
        catch (IOException e){
            System.out.println("Error reading booking file");
        }
    }

    public void changePassword(String oldPassword, String newPassword, String reTypePassword){
        List <String> lines = new ArrayList<>();
        boolean success = false;
        String password = "";
        try(BufferedReader readUser = new BufferedReader(new FileReader("Cinema/Users.txt"))){
            String line;
            while((line = readUser.readLine()) != null){
                String[] parts = line.split(",");
                if(parts[0].equals(username) ){
                    password = parts[1];

                    if (!password.equals(oldPassword)){
                        JOptionPane.showMessageDialog(null,
                                "Incorrect current password, please try again!", "Incorrect password", JOptionPane.WARNING_MESSAGE);
                        lines.add(line);
                    } else if (newPassword.length() < 6) {
                        JOptionPane.showMessageDialog(null,
                                "New password must at least 6 character!", "New password error", JOptionPane.WARNING_MESSAGE);
                        lines.add(line);
                    } else if (!newPassword.equals(reTypePassword)) {
                        JOptionPane.showMessageDialog(null,
                                "Re-type password are different with new password!", "Incorrect password", JOptionPane.WARNING_MESSAGE);
                        lines.add(line);
                    } else if (password.equals(newPassword)) {
                        JOptionPane.showMessageDialog(null,
                                "New password cannot be same as the current password!", "", JOptionPane.WARNING_MESSAGE);
                        lines.add(line);
                    } else{
                        lines.add(username + "," + newPassword);
                        success = true;
                    }
                }else {
                    lines.add(line);
                }
            }
        }catch(IOException e){
            System.out.println("Error reading users file");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Cinema/Users.txt"))){
            for (String l : lines){
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (success) {
            JOptionPane.showMessageDialog(null, "Password updated successfully!");
            cardLayout.show(cardPanel,"setting");
        }
    }

    public void deleteAccount(){
        List <String> lines = new ArrayList<>();
        try(BufferedReader readUser = new BufferedReader(new FileReader("Cinema/Users.txt"))){
            String line;
            while((line = readUser.readLine()) != null){
                String[] parts = line.split(",");
                if(parts[0].equals(username) ){
                    continue;
                }else {
                    lines.add(line);
                }
            }
        }catch(IOException e){
            System.out.println("Error reading users file");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Cinema/Users.txt"))){
            for (String l : lines){
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton1 || e.getSource() == backButton2 || e.getSource() == backButton3 || e.getSource() == backButton4){
            frame.dispose();
            homeFrame.setVisible(true);
        } else if (e.getSource() == delAccBtn) {
            int choice = JOptionPane.showConfirmDialog(null, "This action will permanently delete your account, along with all of your information. Are you sure you want to delete your account?", "Delete Account", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION){
                deleteAccount();
                JOptionPane.showMessageDialog(null, "Your account is deleted successfully!");
                frame.dispose();
                new LoginPage();
            }
        }

    }

    //testing use only
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SubWindowProfile(new JFrame(),1,"tang"));
    }
}
