package cinema;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ShowTime {
    private String movieName;
    private String hallName;
    private LocalDateTime startTime;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("EEE dd MM yyyy, HH:mm");

    ShowTime(String movieName, String hallName, LocalDateTime startTime){
        this.movieName = movieName;
        this.hallName = hallName;
        this.startTime = startTime;
    }

    public String getMovieName() { return movieName; }
    public String getHallName() { return hallName; }
    public LocalDateTime getStartTime() { return startTime; }

}
