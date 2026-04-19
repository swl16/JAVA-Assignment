package cinema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SeatSelection implements ActionListener {
    JFrame frame = new JFrame("TGC Cinema - Seat Selection");
    String username;
    JFrame homeFrame;
    JButton backButton;
    Hall hall;
    ShowTime showTime;
    Movie movieDetail;
    List<Seat> selectedSeat = new ArrayList<>();
    JLabel infoLabel, seatNoLabel, priceLabel;
    JButton expandPanelBtn,closeExpandBtn,confirmButton;
    JLayeredPane layeredPane;
    JPanel lowerPanel,pullOutPanel,shield;
    JLabel[] quantityLabel = new JLabel[4];
    int bookingID = 0;

    JButton[][] seatButtons;
    int[] countType = new int[4];  //0=Adult, 1=Student, 2=Senior, 3=OKU
    Seat[] typedSeat;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("EEE dd MMM , HH:mm");
    String[] seatType = {"Adult","Student","Senior","OKU"};

    SeatSelection(JFrame homeFrame,ShowTime showTime,String username){
        this.showTime = showTime;
        this.homeFrame = homeFrame;
        this.username = username;

        loadHall();
        loadMovie();
//        loadBookDetail();

        double price = hall.getPrice();
        // build a prototype of each type of seat 
        typedSeat = new Seat[]{new AdultSeat('-',0,price), new StudentSeat('_', 0, price), new SeniorSeat('_', 0, price), new OkuSeat('_', 0, price)};

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout(10,10));
        frame.setSize(500,700);
        frame.getContentPane().setBackground(new Color(0x242424));
        frame.setVisible(true);

        JPanel upperPanel = new JPanel();
        upperPanel.setBackground(new Color(0x242424));
        upperPanel.setLayout(null);
        upperPanel.setPreferredSize(new Dimension(500, 180));
        frame.add(upperPanel,BorderLayout.NORTH);

        backButton = new JButton("< Back");
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusable(false);
        backButton.setBounds(8,10,400,30);
        backButton.setFont(new Font("Courier New",Font.PLAIN,17));
        backButton.setHorizontalAlignment(JButton.LEFT);
        backButton.addActionListener(this);
        backButton.setForeground(new Color(0xF7F7F7));
        upperPanel.add(backButton);

        JPanel redPanel = new JPanel();
        redPanel.setBackground(new Color(0xD44444));
        redPanel.setLayout(null);
        redPanel.setBounds(0,50,500,50);
        upperPanel.add(redPanel);

        JLabel movieLabel = new JLabel(movieDetail.getTitle());
        movieLabel.setForeground(new Color(0xF7F7F7));
        movieLabel.setFont(new Font("Courier New",Font.BOLD,18));
        movieLabel.setBounds(10,5,400,20);
        redPanel.add(movieLabel);

        int duration = movieDetail.getDuration();
        int hours = duration / 60;
        int minutes = duration % 60;

        JLabel detailsLabel = new JLabel( hours + "h " + String.format("%02d",minutes) + "m   " + hall.getHallType());
        detailsLabel.setForeground(new Color(0xF7F7F7));
        detailsLabel.setFont(new Font("Courier New",Font.BOLD,13));
        detailsLabel.setBounds(10,25,400,20);
        redPanel.add(detailsLabel);

        JLabel locationLabel = new JLabel("Location: Mit Valley Megamall");
        locationLabel.setForeground(new Color(0xF7F7F7));
        locationLabel.setFont(new Font("Courier New",Font.BOLD,15));
        locationLabel.setBounds(10,105,400,20);
        upperPanel.add(locationLabel);

        JLabel showtimeLabel = new JLabel("Show time: " + showTime.getStartTime().format(FMT) + " at " + hall.getName());
        showtimeLabel.setForeground(new Color(0xF7F7F7));
        showtimeLabel.setFont(new Font("Courier New",Font.BOLD,15));
        showtimeLabel.setBounds(10,125,400,20);
        upperPanel.add(showtimeLabel);

        JLabel screenLabel = new JLabel("Screen", SwingConstants.CENTER);
        screenLabel.setForeground(new Color(0xF7F7F7));
        screenLabel.setFont(new Font("Courier New",Font.BOLD,20));
        screenLabel.setBounds(0,150,500,30);
        upperPanel.add(screenLabel);

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(500, 500));
        layeredPane.setLayout(null);
        frame.add(layeredPane,BorderLayout.CENTER);

        JPanel seatPanel = new JPanel(new GridLayout(hall.getRows(),hall.getColumn() + 2,5,5));
        seatPanel.setBackground(new Color(0x242424));
        seatPanel.setBounds(0,0,485,300);
        layeredPane.add(seatPanel,Integer.valueOf(0));

        lowerPanel = new JPanel();
        lowerPanel.setBackground(new Color(0x3B3B3B));
        lowerPanel.setLayout(null);
        lowerPanel.setBounds(0, 335, 500,140);
        layeredPane.add(lowerPanel,Integer.valueOf(2));

        pullOutPanel = new JPanel();
        pullOutPanel.setBounds(0,100,500,240);
        pullOutPanel.setBackground(new Color(0x3B3B3B));
        pullOutPanel.setLayout(null);
        layeredPane.add(pullOutPanel,Integer.valueOf(2));
        pullOutPanel.setVisible(false);

        seatButtons = new JButton[hall.getRows()][hall.getColumn()];

        for (int i = 0; i < hall.getRows(); i++){
            JLabel rowLabel = new JLabel(String.valueOf(hall.seats[i][0].getRow()), SwingConstants.CENTER);
            rowLabel.setFont(new Font("Arial", Font.BOLD, 12));
            rowLabel.setForeground(new Color(0xF7F7F7));
            seatPanel.add(rowLabel);

            for (int j = 0; j < hall.getColumn(); j++){
                Seat seat = hall.getSeat(i,j);

                JButton seatButton = createSeatButton(seat);
                seatButtons[i][j] = seatButton;
                if (isSeatBooked(seat.getRow(),seat.getColumn())){
                    seatButton.setBackground(new Color(0xD44444));
                    seatButton.setForeground(new Color(0xF7F7F7));
                    seatButton.setEnabled(false);
                    seatButton.setToolTipText(seat.getSeatId() + "  " + Seat.SeatStatus.BOOKED);
                }
                seatPanel.add(seatButton);
            }

            seatPanel.add(new JLabel(""));
        }

        expandPanelBtn = new JButton("↑ Ticket type options");
        expandPanelBtn.setBorderPainted(false);
        expandPanelBtn.setFocusPainted(false);
        expandPanelBtn.setFont(new Font("Courier New",Font.PLAIN,12));
        expandPanelBtn.setBounds(280,10,180,25);
        expandPanelBtn.setForeground(new Color(0xF7F7F7));
        expandPanelBtn.setBackground(new Color(0xD44444));
        expandPanelBtn.setMargin(new Insets(0,0,0,0));
        expandPanelBtn.addActionListener(this);
        lowerPanel.add(expandPanelBtn);

        JLabel seatLabel = new JLabel("Seat Selection: ");
        seatLabel.setForeground(new Color(0xF7F7F7));
        seatLabel.setFont(new Font("Courier New",Font.BOLD,18));
        seatLabel.setBounds(10,15,200,20);
        lowerPanel.add(seatLabel);

//        double totalPrice = 0;
//        StringBuilder sbSeatNo = new StringBuilder();
//        for (Seat s : selectedSeat){
//            sbSeatNo.append(s.getSeatId()).append(" ");
//        }
//        for (int i = 0; i < countType.length; i++){
//            totalPrice +=countType[i] * baseSeat.getPrice(allTypes[i]);
//        }
//
//        StringBuilder sbType = new StringBuilder();
//        boolean isFirst = true;
//        for (int i = 0 ; i < 4; i++){
//            if (countType[i] != 0){
//                if (isFirst){
//                sbType.append(seatType[i]).append(" x ").append(countType[i]);
//                isFirst = false;
//                }else {
//                sbType.append(" , ").append(seatType[i]).append(" x ").append(countType[i]); }
//            }
//        }

        infoLabel = new JLabel("");
        infoLabel.setForeground(new Color(0xF7F7F7));
        infoLabel.setFont(new Font("Courier New",Font.PLAIN,15));
        infoLabel.setBounds(10,40,450,20);
        lowerPanel.add(infoLabel);

        seatNoLabel = new JLabel("-");
        seatNoLabel.setForeground(new Color(0xF7F7F7));
        seatNoLabel.setFont(new Font("Courier New",Font.BOLD,15));
        seatNoLabel.setBounds(10,60,350,20);
        lowerPanel.add(seatNoLabel);

        priceLabel = new JLabel("RM 0.00");
        priceLabel.setForeground(new Color(0xF7F7F7));
        priceLabel.setFont(new Font("Courier New",Font.BOLD,15));
        priceLabel.setBounds(380,60,100,20);
        lowerPanel.add(priceLabel);

        confirmButton = new JButton("Confirm - " + selectedSeat.size() + " ticket(s)");
        confirmButton.setBorderPainted(false);
        confirmButton.setFocusPainted(false);
        confirmButton.setFont(new Font("Courier New",Font.BOLD,15));
        confirmButton.setBounds(20,90,430,30);
        confirmButton.setForeground(new Color(0xF7F7F7));
        confirmButton.setBackground(new Color(0xD44444));
        confirmButton.addActionListener(this);
        lowerPanel.add(confirmButton);

        closeExpandBtn = new JButton("↓ Close the options");
        closeExpandBtn.setBorderPainted(false);
        closeExpandBtn.setFocusPainted(false);
        closeExpandBtn.setFont(new Font("Courier New",Font.PLAIN,12));
        closeExpandBtn.setBounds(280,10,180,30);
        closeExpandBtn.setForeground(new Color(0xF7F7F7));
        closeExpandBtn.setBackground(new Color(0xD44444));
        closeExpandBtn.setMargin(new Insets(0,0,0,0));
        closeExpandBtn.addActionListener(this);
        pullOutPanel.add(closeExpandBtn);


        for (int i = 0;i < seatType.length ; i++){
            int index = i;
            pullOutPanel.add(SeatTypeLabel(seatType[i],i*40));
            
            pullOutPanel.add(seatPriceLabel(typedSeat[i].calculatePrice(),i * 40));

            JButton minusButton = new JButton("-");
            minusButton.setBorderPainted(false);
            minusButton.setFocusPainted(false);
            minusButton.setContentAreaFilled(false);
            minusButton.setFocusable(false);
            minusButton.setBounds(350,55 + i * 40,20,20);
            minusButton.setFont(new Font("Courier New",Font.BOLD,15));
            minusButton.setForeground(new Color(0xF7F7F7));
            minusButton.setMargin(new Insets(0,0,0,0));

            quantityLabel[i] = new JLabel(String.valueOf(countType[i]));
            quantityLabel[i].setForeground(new Color(0xF7F7F7));
            quantityLabel[i].setFont(new Font("Courier New",Font.BOLD,15));
            quantityLabel[i].setBounds(390,55 + i * 40,20,20);

            JButton addButton = new JButton("+");
            addButton.setBorderPainted(false);
            addButton.setFocusPainted(false);
            addButton.setContentAreaFilled(false);
            addButton.setFocusable(false);
            addButton.setBounds(415,55 + i * 40,20,20);
            addButton.setFont(new Font("Courier New",Font.BOLD,15));
            addButton.setForeground(new Color(0xF7F7F7));
            addButton.setMargin(new Insets(0,0,0,0));

            minusButton.addActionListener(e -> {
                if (countType[index] > 0 ) {
                    countType[index]--;
                    quantityLabel[index].setText(String.valueOf(countType[index]));
                    updateSeatSummary();
                }});
            addButton.addActionListener(e -> {
                int totalTicketsSelected = 0;
                for (int count : countType) {
                    totalTicketsSelected += count;
                }
                if (totalTicketsSelected < selectedSeat.size()){
                    countType[index]++;
                    quantityLabel[index].setText(String.valueOf(countType[index]));
                    updateSeatSummary();
                }else {
                    JOptionPane.showMessageDialog(null,
                            "You have already assigned a ticket type to every selected seat!",
                            "Limit Reac hed",
                            JOptionPane.WARNING_MESSAGE);
                }
            });

            pullOutPanel.add(minusButton);
            pullOutPanel.add(quantityLabel[i]);
            pullOutPanel.add(addButton);
        }
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setBounds(10,230,460,5);
        separator.setForeground(new Color(0xF7F7F7));
        pullOutPanel.add(separator);


    }


    JButton createSeatButton(Seat seat){
        JButton button = new JButton(seat.getSeatId());
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Courier New",Font.BOLD,8));
        button.setPreferredSize(new Dimension(40, 36));
        button.setForeground(new Color(0x242424));
        button.setMargin(new Insets(0,0,0,0));

        updateBtnColor(button,seat);
        button.addActionListener(e -> clickSeat(seat,button));

        button.setToolTipText(seat.getSeatId() + "  " + seat.getStatus());
        return button;
    }

    private boolean isSeatBooked(char row, int num) {
        File file = new File("BookingDetail.txt");
        if (!file.exists()) {return false;}

        try(BufferedReader readLine = new BufferedReader(new FileReader(file))){
            String line;

            while((line = readLine.readLine()) != null){
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");

                String startTime = parts[4];
                String hallName = parts[5];
                String bookedSeat = parts[6];

                if (startTime.equals(showTime.getStartTime().toString()) && hallName.equals(hall.getName())) {
                    String[] seatArray = bookedSeat.split(",");

                    for (String s : seatArray) {
                        if (s.trim().equals(row + String.valueOf(num))) {
                            return true;
                        }
                    }
                }
            }
        }
        catch (IOException e){
            System.out.println("Error reading booking file");
        }

        return false;
    }

    void clickSeat(Seat seat,JButton btn){
        if (!seat.isAvailable()){
            JOptionPane.showMessageDialog(frame,
                    "Seat " + seat.getSeatId() + " is already booked.",
                    "Unavailable", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedSeat.contains(seat)){
            selectedSeat.remove(seat);
            seat.cancel();
            
            if(countType[0] > 0){
                countType[0]--;
                quantityLabel[0].setText(String.valueOf(countType[0]));
            } else if (countType[1] > 0) {
                countType[1]--;
                quantityLabel[1].setText(String.valueOf(countType[1]));
            } else if (countType[2] > 0) {
                countType[2]--;
                quantityLabel[2].setText(String.valueOf(countType[2]));
            } else if (countType[3] > 0) {
                countType[3]--;
                quantityLabel[3].setText(String.valueOf(countType[3]));
            }

        }else {
            selectedSeat.add(seat);
            seat.select();
            countType[0]++;
            quantityLabel[0].setText(String.valueOf(countType[0]));
            btn.setBackground(new Color(0x6e7075));
            btn.setForeground(new Color(0xF7F7F7));
        }
        btn.setToolTipText(seat.getSeatId() + "  " + seat.getStatus());
        updateBtnColor(btn,seat);
        updateSeatSummary();
    }

    void updateBtnColor(JButton btn, Seat seat){
        if (!seat.isAvailable()){
            btn.setBackground(new Color(0xD44444));
            btn.setForeground(new Color(0xF7F7F7));
            btn.setEnabled(false);

        } else if (seat.getStatus() == Seat.SeatStatus.SELECTING) {
            btn.setBackground(new Color(0x6e7075));
            btn.setForeground(new Color(0xF7F7F7));
        } else {
            btn.setBackground(new Color(0xcccccc));
            btn.setForeground(new Color(0x242424));
        }
    }

    void updateSeatSummary(){
        double totalPrice = 0;
        for (int i = 0; i < countType.length; i++){
            totalPrice +=countType[i] * typedSeat[i].calculatePrice();
        }
        StringBuilder sbSeatNo = new StringBuilder();
        StringBuilder sbType = new StringBuilder();
        if (!selectedSeat.isEmpty()){
            for (Seat s : selectedSeat){
                sbSeatNo.append(s.getSeatId()).append(" ");
            }
            boolean isFirst = true;
            for (int i = 0 ; i < 4; i++){
                if (countType[i] != 0){
                    if (isFirst){
                        sbType.append(seatType[i]).append(" x ").append(countType[i]);
                        isFirst = false;
                    }else {
                        sbType.append(", ").append(seatType[i]).append(" x ").append(countType[i]); }
                }
            }
        }else {
            sbSeatNo.append("-");
        }

        infoLabel.setText(sbType.toString());
        seatNoLabel.setText(sbSeatNo.toString());
        priceLabel.setText("RM " + String.format("%.2f", totalPrice));
        confirmButton.setText("Confirm - " + selectedSeat.size() + " ticket(s)");
    }

    JLabel SeatTypeLabel(String type, int i){
        JLabel label = new JLabel(type);
        label.setForeground(new Color(0xF7F7F7));
        label.setFont(new Font("Courier New",Font.BOLD,20));
        label.setBounds(10,55 + i,200,20);
        return label;
    }

    JLabel seatPriceLabel(double price, int i){
        JLabel label = new JLabel("RM" + String.format("%.2f",price) + " x");
        label.setForeground(new Color(0xF7F7F7));
        label.setFont(new Font("Courier New",Font.BOLD,18));
        label.setBounds(210,55 + i,120,20);
        return label;
    }

//    public void loadBookDetail(){
//        File file = new File("BookingDetail.txt");
//        if (!file.exists()) {return;}
//
//        try(BufferedReader readLine = new BufferedReader(new FileReader(file))){
//            String line;
//
//            while((line = readLine.readLine()) != null){
//                if (line.trim().isEmpty()) continue;
//
//                String[] parts = line.split("\\|");
//
//                if (new ShowTime(parts[2]).equals(showTime)) {
//
//                    bookedSeat.add(new Seat(parts[4].charAt(0),Integer.parseInt(parts[5]),hall.getPrice()));
//                }
//                bookingID = Integer.valueOf(parts[0]);
//            }
//            for (Seat s : bookedSeat){
//                s.book();
//            }
//        }
//        catch (IOException e){
//            System.out.println("Error reading booking file");
//        }
//
//    }


    public void loadHall(){
        try(BufferedReader readLine = new BufferedReader(new FileReader("Hall.txt"))){
            String line;

            while((line = readLine.readLine()) != null){
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");

                if (parts[0].equals(showTime.getHallName())) {
                    hall = new Hall(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Double.parseDouble(parts[4]));
                }
            }
        }
        catch (IOException e){
            System.out.println("Error reading hall file");
        }
    }

    private void loadMovie() {
        try(BufferedReader readLine = new BufferedReader(new FileReader("MovieDetails.txt"))){
            String line;

            while((line = readLine.readLine()) != null){
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");

                if (parts[0].equals(showTime.getMovieName())) {
                    ImageIcon poster = new ImageIcon(parts[10]);
                    Date date = new Date(parts[4]);
                    movieDetail = new Movie(parts[0],parts[1],parts[2],parts[3],date,Integer.parseInt(parts[5]),parts[6],parts[7],parts[8],parts[9],poster);
                }
            }
        }
        catch (IOException e){
            System.out.println("Error reading movies file");
        }
    }

//    public void saveBooking(){
//        bookingID++;
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter("BookingDetail.txt",true))){
//
//            for (Seat s : selectedSeat){
//                writer.write(String.format("%04d",bookingID) + "|" + username + "|" + String.valueOf(showTime.getStartTime()) + "|" + hall.getName() + "|" + s.getRow() + "|" + s.getColumn() + "|" + movieDetail.getTitle());
//                writer.newLine();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            if (!selectedSeat.isEmpty()){
                int choice = JOptionPane.showConfirmDialog(null,"Are you confirm you want to exit? Your selection will be discard.","Confirm Exit",JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION){
                    frame.dispose();
                    homeFrame.setVisible(true);
                }else{
                    return;
                }
            }else{
                frame.dispose();
                homeFrame.setVisible(true);
            }

        } else if (e.getSource() == expandPanelBtn) {
            pullOutPanel.setVisible(true);
            expandPanelBtn.setVisible(false);
            shield = new JPanel();
            shield.setBounds(0,0,500,500);
            shield.addMouseListener(new MouseAdapter() {});
            layeredPane.add(shield,Integer.valueOf(1));
            shield.setOpaque(false);

        } else if (e.getSource() == closeExpandBtn) {
            pullOutPanel.setVisible(false);
            expandPanelBtn.setVisible(true);
            layeredPane.remove(shield);

        } else if (e.getSource() == confirmButton) {
            int totalTicketsSelected = 0;
            for (int count : countType) {
                totalTicketsSelected += count;
            }
            if (selectedSeat.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select at least one seat first.", "No Seats Selected", JOptionPane.WARNING_MESSAGE);
            } else if (totalTicketsSelected != selectedSeat.size()) {
                JOptionPane.showMessageDialog(null, "You have selected "+ selectedSeat.size() + " seats, but only " + totalTicketsSelected + " tickets was selected. Please select a valid ticket quantity.", "Invalid quantity of ticket", JOptionPane.WARNING_MESSAGE);

            }else {
                double totalPrice = 0;
//                StringBuilder msg = new StringBuilder("Please confirm your booking.\n\n");
//                for (Seat s : selectedSeat){
//                    msg.append(s.getSeatId()).append(" ");
//                }
                for (int i = 0; i < countType.length; i++){
                    totalPrice +=countType[i] * typedSeat[i].calculatePrice();
                }
//                msg.append("\nTotal : RM ").append(String.format("%.2f",totalPrice));

//                int choice = JOptionPane.showConfirmDialog(null, msg.toString(), "Confirm Booking", JOptionPane.YES_NO_OPTION);
//                if (choice == JOptionPane.YES_OPTION){
                    //saveBooking();
//                for (Seat s : selectedSeat) {
//                    s.book();
//                }
                UserOrder order = new UserOrder(username,movieDetail,showTime,selectedSeat,countType,new HashMap<>(),totalPrice,0.0);
//                    selectedSeat.clear();
//                    JOptionPane.showMessageDialog(null, "Booking Successful!");
                frame.setVisible(false);
                new FnBwTicket(frame,order);
            }
        }
    }

}

