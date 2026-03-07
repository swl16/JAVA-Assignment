package cinema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame implements ActionListener {

    JTextField usernameEnter;
    JTextField passwordEnter;
    JButton loginButton;
    JButton staffLoginB;
    JButton signUpButton;
    JFrame frame = new JFrame("TGC Cinema");

    public LoginPage(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setSize(400,400);
        frame.getContentPane().setBackground(new Color(0x242424));
        frame.setVisible(true);

        JLabel loginLabel = new JLabel("User Login");
        loginLabel.setForeground(new Color(0xF7F7F7));
        loginLabel.setFont(new Font("Courier New",Font.BOLD,30));
        loginLabel.setBounds(0,30,400,50);
        loginLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(loginLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(new Color(0xF7F7F7));
        usernameLabel.setFont(new Font("Courier New",Font.PLAIN,20));
        usernameLabel.setBounds(40,80,350,50);
        frame.add(usernameLabel);

        usernameEnter = new JTextField();
        usernameEnter.setBounds(160,95,160,20);
        usernameEnter.setFont(new Font("Courier New",Font.PLAIN,15));
        usernameEnter.setForeground(new Color(0xF7F7F7));
        usernameEnter.setBackground(new Color(0x3B3B3B));
        usernameEnter.setCaretColor(new Color(0xF7F7F7));
        usernameEnter.setBorder(null);
        frame.add(usernameEnter);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(new Color(0xF7F7F7));
        passwordLabel.setFont(new Font("Courier New",Font.PLAIN,20));
        passwordLabel.setBounds(40,135,350,50);
        frame.add(passwordLabel);

        passwordEnter = new JTextField();
        passwordEnter.setBounds(160,150,160,20);
        passwordEnter.setFont(new Font("Courier New",Font.PLAIN,15));
        passwordEnter.setForeground(new Color(0xF7F7F7));
        passwordEnter.setBackground(new Color(0x3B3B3B));
        passwordEnter.setCaretColor(new Color(0xF7F7F7));
        passwordEnter.setBorder(null);
        frame.add(passwordEnter);

        loginButton = new JButton("Login");
        loginButton.setFocusable(false);
        loginButton.setBounds(120,210,150,50);
        loginButton.setFont(new Font("Courier New",Font.BOLD,30));
        loginButton.addActionListener(this);
        loginButton.setForeground(new Color(0xF7F7F7));
        loginButton.setBackground(new Color(0xD44444));
        frame.add(loginButton);

        signUpButton = new JButton("SIGN UP");
        signUpButton.setBorderPainted(false);
        signUpButton.setFocusPainted(false);
        signUpButton.setContentAreaFilled(false);
        signUpButton.setFocusable(false);
        signUpButton.setBounds(120,270,150,25);
        signUpButton.setFont(new Font("Courier New",Font.PLAIN,15));
        signUpButton.addActionListener(this);
        signUpButton.setForeground(new Color(0xF7F7F7));
        frame.add(signUpButton);

        staffLoginB = new JButton("Staff Login");
        staffLoginB.setBorderPainted(false);
        staffLoginB.setFocusPainted(false);
        staffLoginB.setContentAreaFilled(false);
        staffLoginB.setFocusable(false);
        staffLoginB.setHorizontalAlignment(JButton.LEFT);
        staffLoginB.setBounds(5,325,200,25);
        staffLoginB.setFont(new Font("Courier New",Font.PLAIN,12));
        staffLoginB.addActionListener(this);
        staffLoginB.setForeground(new Color(0xF7F7F7));
        frame.add(staffLoginB);

    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == loginButton){
            String username = usernameEnter.getText();
            String password = passwordEnter.getText();

            frame.dispose();
            new MainMenuPage();

        } else if (e.getSource() == staffLoginB) {
            frame.dispose();
            StaffLoginPage StaffLogin = new StaffLoginPage();
        } else if (e.getSource() == signUpButton) {
            frame.dispose();
            SignUpPage SignUp = new SignUpPage();
        }
    }
}

