package cinema;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import java.util.Date; // Added this import for the 4th argument

public class Main {
    public static void main(String[] args) {
        // We provide all 11 arguments in the exact order the error message requires:
        // String, String, String, Date, String, String, String, String, String, String, ImageIcon

        Movie dummyMovie = new Movie(
                "Hoppers",           // 1. Title (String)
                "Comedy",            // 2. Genre (String)
                "English",           // 3. Language (String)
                new Date(),          // 4. Date (java.util.Date) - This was missing!
                "P13",               // 5. Rating (String)
                "120m",              // 6. Duration (String)
                "Unknown",           // 7. Director (String)
                "Unknown",           // 8. Cast (String)
                "English",           // 9. Subtitles (String)
                "A comedy movie.",   // 10. Description (String)
                null                 // 11. Poster (ImageIcon)
        );

        List<Seat> selectedSeats = new ArrayList<>();
        selectedSeats.add(new Seat('A', 7, 20.0));
        selectedSeats.add(new Seat('A', 8, 20.0));

        Order order = new Order(dummyMovie, "11:30 PM", "KL - Melawati", "Hall 2", selectedSeats);

        // This launches your Payment Module
        new PaymentFrame(order);
    }
}