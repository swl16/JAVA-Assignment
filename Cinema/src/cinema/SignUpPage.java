package cinema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class SignUpPage implements ActionListener {

    JTextField usernameEnter;
    JPasswordField passwordEnter;
    JButton signUpButton;
    JButton backUserLogin;
    JFrame frame = new JFrame("TGC Cinema");

    public SignUpPage(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setSize(400,400);
        frame.getContentPane().setBackground(new Color(0x242424));
        frame.setVisible(true);

        JLabel loginLabel = new JLabel("Sign Up");
        loginLabel.setForeground(new Color(0xF7F7F7));
        loginLabel.setFont(new Font("Courier New",Font.BOLD,30));
        loginLabel.setBounds(0,30,400,50);
        loginLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(loginLabel);

        JLabel tipsLabel = new JLabel("*Password must at least 6 characters.");
        tipsLabel.setForeground(new Color(0xD44444));
        tipsLabel.setFont(new Font("Courier New",Font.BOLD,12));
        tipsLabel.setBounds(40,90,400,15);
        frame.add(tipsLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(new Color(0xF7F7F7));
        usernameLabel.setFont(new Font("Courier New",Font.PLAIN,20));
        usernameLabel.setBounds(40,100,350,50);
        frame.add(usernameLabel);

        usernameEnter = new JTextField();
        usernameEnter.setBounds(160,115,160,20);
        usernameEnter.setFont(new Font("Courier New",Font.PLAIN,15));
        usernameEnter.setForeground(new Color(0xF7F7F7));
        usernameEnter.setBackground(new Color(0x3B3B3B));
        usernameEnter.setCaretColor(new Color(0xF7F7F7));
        usernameEnter.setBorder(null);
        frame.add(usernameEnter);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(new Color(0xF7F7F7));
        passwordLabel.setFont(new Font("Courier New",Font.PLAIN,20));
        passwordLabel.setBounds(40,155,350,50);
        frame.add(passwordLabel);

        passwordEnter = new JPasswordField();
        passwordEnter.setBounds(160,170,160,20);
        passwordEnter.setFont(new Font("Courier New",Font.PLAIN,15));
        passwordEnter.setForeground(new Color(0xF7F7F7));
        passwordEnter.setBackground(new Color(0x3B3B3B));
        passwordEnter.setCaretColor(new Color(0xF7F7F7));
        passwordEnter.setBorder(null);
        frame.add(passwordEnter);

        signUpButton = new JButton("Sign Up");
        signUpButton.setFocusable(false);
        signUpButton.setBounds(95,230,190,50);
        signUpButton.setFont(new Font("Courier New",Font.BOLD,30));
        signUpButton.addActionListener(this);
        signUpButton.setForeground(new Color(0xF7F7F7));
        signUpButton.setBackground(new Color(0xD44444));
        frame.add(signUpButton);

        backUserLogin = new JButton("< Back");
        backUserLogin.setBorderPainted(false);
        backUserLogin.setFocusPainted(false);
        backUserLogin.setContentAreaFilled(false);
        backUserLogin.setFocusable(false);
        backUserLogin.setHorizontalAlignment(JButton.LEFT);
        backUserLogin.setBounds(5,325,200,25);
        backUserLogin.setFont(new Font("Courier New",Font.PLAIN,12));
        backUserLogin.addActionListener(this);
        backUserLogin.setForeground(new Color(0xF7F7F7));
        frame.add(backUserLogin);
    }
    
    public void saveuser(String username, String password){
        
        try (BufferedWriter saveusers = new BufferedWriter(new FileWriter("Users.txt",true))){
         saveusers.write(username + ","+ password + "\n");
        }catch(IOException e){
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }
    
    public boolean usernameExists(String username){
        try(BufferedReader reader = new BufferedReader(new FileReader("Users.txt"))){
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(" , ");
                if (parts[0].equals(username)){
                    return true;
                }
            }
        }catch(IOException e){
            System.out.println("Error reading users file");
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == signUpButton){
            String username = usernameEnter.getText();
            String password = new String(passwordEnter.getPassword());
            
            if(username.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(frame, "Username and password cannot be empty! Please try again.", "Sign Up Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (password.length() < 6){
                JOptionPane.showMessageDialog(frame, "Password must at least 6 characters! Please try again.", "Sign Up Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(usernameExists(username)){
                JOptionPane.showMessageDialog(frame, "Username already taken! Please change another one", "Sign Up Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            saveuser(username,password);
            JOptionPane.showMessageDialog(frame, "Account created successfully!", "Sign Up", JOptionPane.INFORMATION_MESSAGE);
            
            frame.dispose();
            new LoginPage();

        } else if (e.getSource() == backUserLogin) {
            frame.dispose();
            new LoginPage();
        }
    }
}

