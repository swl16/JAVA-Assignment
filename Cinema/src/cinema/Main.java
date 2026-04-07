package cinema;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        // We provide all 11 arguments in the exact order the error message requires:
        // String, String, String, Date, String, String, String, String, String, String, ImageIcon

        Movie dummyMovie = new Movie(
                "Hoppers",           // 1. title (String)
                "Comedy",            // 2. genre (String)
                "English",           // 3. language (String)
                "P13",               // 4. rating (String) - Previously you put Date here
                new Date(),          // 5. date (java.util.Date) - This belongs here
                "120m",              // 6. duration (String)
                "Unknown",           // 7. director (String)
                "Unknown",           // 8. cast (String)
                "English",           // 9. subtitles (String)
                "A comedy movie.",   // 10. description (String)
                null                 // 11. poster (ImageIcon)
        );

        List<Seat> selectedSeats = new ArrayList<>();
        selectedSeats.add(new Seat('A', 7, 20.0));
        selectedSeats.add(new Seat('A', 8, 20.0));

        Order order = new Order(dummyMovie, "11:30 PM", "KL - Melawati", "Hall 2", selectedSeats);

        // This launches your Payment Module
        new PaymentFrame(order);
    }
}