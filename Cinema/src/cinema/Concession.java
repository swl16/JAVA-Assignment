package cinema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;

class Item {
    String name;
    double price;
    int quantity;

    Item(String name, double price, int quantity){
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}

public class Concession implements ActionListener {
    // UI Components matching MovieDetailPage style
    JFrame frame = new JFrame("TGC Cinema - Concessions");
    JFrame homeFrame;
    JButton backButton, foodBtn, drinkBtn, comboBtn, exitBtn;
    JTextArea displayArea;

    private Scanner scanner = new Scanner(System.in);
    private ArrayList<Item> basket = new ArrayList<>();

    public Concession(JFrame homeFrame) {
        this.homeFrame = homeFrame;

        // Frame Setup
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(500, 700);
        frame.getContentPane().setBackground(new Color(0x242424));
        frame.setLayout(null);

        // Back Button (UI Design Format)
        backButton = new JButton("< Back");
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusable(false);
        backButton.setBounds(8, 10, 100, 40);
        backButton.setFont(new Font("Courier New", Font.PLAIN, 17));
        backButton.setForeground(new Color(0xF7F7F7));
        backButton.setHorizontalAlignment(SwingConstants.LEFT);
        backButton.addActionListener(e -> {
            frame.dispose();
            if (homeFrame != null) homeFrame.setVisible(true);
        });
        frame.add(backButton);

        // Title
        JLabel title = new JLabel("======= F & B =======");
        title.setForeground(new Color(0xF7F7F7));
        title.setFont(new Font("Courier New", Font.BOLD, 20));
        title.setBounds(0, 60, 500, 30);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(title);

        // Menu Buttons
        foodBtn = createMenuButton("1. Buy Food", 110);
        drinkBtn = createMenuButton("2. Buy Drink", 160);
        comboBtn = createMenuButton("3. Buy Combo", 210);
        exitBtn = createMenuButton("4. Checkout / Exit", 260);

        // Instruction Label
        JLabel instructLabel = new JLabel("Check Terminal/Console for Input");
        instructLabel.setForeground(new Color(0xAAAAAA));
        instructLabel.setFont(new Font("Courier New", Font.ITALIC, 12));
        instructLabel.setBounds(0, 300, 500, 20);
        instructLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(instructLabel);

        // Output Display (The "Receipt" Area)
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setBackground(new Color(0x3B3B3B));
        displayArea.setForeground(new Color(0xF7F7F7));
        displayArea.setFont(new Font("Courier New", Font.PLAIN, 13));
        displayArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBounds(40, 330, 420, 280);
        scrollPane.setBorder(null);
        frame.add(scrollPane);

        frame.setVisible(true);
    }

    private JButton createMenuButton(String text, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(50, y, 400, 40);
        btn.setBackground(new Color(0x3B3B3B));
        btn.setForeground(new Color(0xF7F7F7));
        btn.setFont(new Font("Courier New", Font.PLAIN, 15));
        btn.setFocusable(false);
        btn.addActionListener(this);
        frame.add(btn);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == foodBtn) {
            selectFood();
            displayArea.append("Added Food item to basket...\n");
        } else if (e.getSource() == drinkBtn) {
            selectDrink();
            displayArea.append("Added Drink item to basket...\n");
        } else if (e.getSource() == comboBtn) {
            selectCombo();
            displayArea.append("Added Combo item to basket...\n");
        } else if (e.getSource() == exitBtn) {
            showTotal();
        }
    }

    private void selectFood(){
        System.out.println("\n---- FOOD MENU ----");
        System.out.println("1. Popcorn (RM15)  |  2. Hotdog (RM12)  |  3. Nachos (RM10)");
        System.out.print("Please enter food code (1-3) : ");
        int code = scanner.nextInt();

        String name = "";
        double price = 0.0;

        switch (code) {
            case 1: name = "Popcorn"; price = 15.0; break;
            case 2: name = "Hotdog"; price = 12.0; break;
            case 3: name = "Nachos"; price = 10.0; break;
            default: System.out.println("Invalid code"); return;
        }
        System.out.print("Please enter quantity for " + name + ": ");
        int quantity = scanner.nextInt();
        basket.add(new Item(name, price, quantity));
    }

    private void selectDrink(){
        System.out.println("\n---- DRINK MENU ----");
        System.out.println("\n1. Cola (RM8)  |  2. Water (RM3)  |  3. Coffee (RM7)");
        System.out.print("Please enter drink code (1-3) : ");
        int code = scanner.nextInt();

        String name = "";
        double price = 0.0;

        switch (code) {
            case 1: name = "Cola"; price = 8.0; break;
            case 2: name = "Water"; price = 3.0; break;
            case 3: name = "Coffee"; price = 7.0; break;
            default: System.out.println("Invalid code"); return;
        }
        System.out.print("Please enter quantity for " + name + ": ");
        int quantity = scanner.nextInt();
        basket.add(new Item(name, price, quantity));
    }

    private void selectCombo(){
        System.out.println("\n---- COMBO MENU ----");
        System.out.println("\n1. Combo A - Popcorn + Cola (RM22)  |  2. Combo B - Hotdog + Cola (RM20)  |  3. Combo C - Nachos + Cola (RM18)");
        System.out.print("Please enter combo code (1-3) : ");
        int code = scanner.nextInt();

        String name = "";
        double price = 0.0;

        switch (code) {
            case 1: name = "Popcorn + Cola"; price = 22.0; break;
            case 2: name = "Hotdog + Cola"; price = 20.0; break;
            case 3: name = "Nachos + Cola"; price = 18.0; break;
            default: System.out.println("Invalid code"); return;
        }
        System.out.print("Please enter quantity for " + name + ": ");
        int quantity = scanner.nextInt();
        basket.add(new Item(name, price, quantity));
    }

    private void showTotal() {
        displayArea.setText("----- FINAL ORDER SUMMARY -----\n\n");
        if (basket.isEmpty()){
            displayArea.append("No items were ordered.");
            return;
        }
    }
}