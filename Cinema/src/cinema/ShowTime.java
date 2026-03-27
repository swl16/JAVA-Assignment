package cinema;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ShowTime {
    private Movie movie;
    private Hall hall;
    private LocalDateTime startTime;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("EEE dd MM yyyy, HH:mm");

    ShowTime(){

    }
}
