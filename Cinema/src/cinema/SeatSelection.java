package cinema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SeatSelection implements ActionListener {
    JFrame frame = new JFrame("TGC Cinema - Seat Selection");
    JFrame homeFrame;
    JButton backButton;
    Hall hall;
    ShowTime showTime;
    Movie movieDetail;
    List<Seat> selectedSeat = new ArrayList<>();

    JButton[][] seatButtons;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("EEE dd MMM , HH:mm");

    SeatSelection(JFrame homeFrame,ShowTime showTime){
        this.showTime = showTime;
        this.homeFrame = homeFrame;

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

        loadHall();
        loadMovie();

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

        JLabel detailsLabel = new JLabel(movieDetail.getDuration() + "   " + hall.getHallType());
        detailsLabel.setForeground(new Color(0xF7F7F7));
        detailsLabel.setFont(new Font("Courier New",Font.BOLD,13));
        detailsLabel.setBounds(10,25,400,20);
        redPanel.add(detailsLabel);

        JLabel showtimeLabel = new JLabel("Show time: " + showTime.getStartTime().format(FMT) + " at " + hall.getName());
        showtimeLabel.setForeground(new Color(0xF7F7F7));
        showtimeLabel.setFont(new Font("Courier New",Font.BOLD,15));
        showtimeLabel.setBounds(10,105,400,30);
        upperPanel.add(showtimeLabel);

        JLabel screenLabel = new JLabel("Screen", SwingConstants.CENTER);
        screenLabel.setForeground(new Color(0xF7F7F7));
        screenLabel.setFont(new Font("Courier New",Font.BOLD,20));
        screenLabel.setBounds(0,140,500,40);
        upperPanel.add(screenLabel);

        JPanel seatPanel = new JPanel(new GridLayout(hall.getRows() ,hall.getColumn()+2,5,5));
        seatPanel.setBackground(new Color(0x242424));
        frame.add(seatPanel,BorderLayout.CENTER);
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
                seatPanel.add(seatButton);
            }

            seatPanel.add(new JLabel(""));
        }



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

    void clickSeat(Seat seat,JButton btn){
        if (!seat.isAvailable()){
            JOptionPane.showMessageDialog(frame,
                    "Seat " + seat.getSeatId() + " is already booked.",
                    "Unavailable", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedSeat.contains(seat)){
            selectedSeat.remove(seat);
            updateBtnColor(btn,seat);

        }else {
            selectedSeat.add(seat);
            btn.setBackground(new Color(0x6e7075));
            btn.setForeground(new Color(0xF7F7F7));
        }

    }

    void updateBtnColor(JButton btn, Seat seat){
        if (!seat.isAvailable()){
            btn.setBackground(new Color(0xD44444));
            btn.setForeground(new Color(0xF7F7F7));
            btn.setEnabled(false);

            return;
        }else {
            btn.setBackground(new Color(0xcccccc));
            btn.setForeground(new Color(0x242424));
        }
    }

    public void loadHall(){
        try(BufferedReader readLine = new BufferedReader(new FileReader("Hall.txt"))){
            String line;

            while((line = readLine.readLine()) != null){
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(" , ");

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
        try(BufferedReader readLine = new BufferedReader(new FileReader("MovieDetail.txt"))){
            String line;

            while((line = readLine.readLine()) != null){
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(" , ");

                if (parts[0].equals(showTime.getMovieName())) {
                    ImageIcon poster = new ImageIcon(parts[3]);
                    movieDetail = new Movie(parts[0], parts[1], parts[2], poster);
                }
            }
        }
        catch (IOException e){
            System.out.println("Error reading movies file");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            frame.dispose();
            homeFrame.setVisible(true);
        }
    }

}

