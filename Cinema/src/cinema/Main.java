package cinema;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Now you can call Movie directly because it's in the same folder
        MovieDetailPage.Movie dummyMovie = new MovieDetailPage.Movie("Hoppers", "Comedy", "120m", null);

        List<Seat> selectedSeats = new ArrayList<>();
        // Seat works now because you are in the same package!
        selectedSeats.add(new Seat('A', 7, 20.0));
        selectedSeats.add(new Seat('A', 8, 20.0));

        Order order = new Order(dummyMovie, "11:30 PM", "KL - Melawati", "Hall 2", selectedSeats);

        new PaymentFrame(order);
    }
}