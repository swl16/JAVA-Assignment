package cinema;

import java.util.List;
import java.util.stream.Collectors;

public class Order {
    private Movie movie;
    private String showTime;
    private String cinemaLocation; // Added field
    private String hallName;
    private List<Seat> selectedSeats;
    private double foodTotal;

    public Order(Movie movie, String showTime, String cinemaLocation, String hallName, List<Seat> selectedSeats) {
        this.movie = movie;
        this.showTime = showTime;
        this.cinemaLocation = cinemaLocation;
        this.hallName = hallName;
        this.selectedSeats = selectedSeats;
    }

    public String getCinemaLocation() {
        return (cinemaLocation != null) ? cinemaLocation : "TGC Cinema";
    }

    public String getMovieTitle() {
        return (movie != null) ? movie.getTitle() : "N/A";
    }

    public double getSubtotal() {
        double ticketTotal = 0;
        if (selectedSeats != null) {
            for (Seat s : selectedSeats) {
                ticketTotal += s.calculatePrice();
            }
        }
        return ticketTotal + foodTotal;
    }

    public int getTicketQuantity() {
        return (selectedSeats != null) ? selectedSeats.size() : 0;
    }

    public double getFoodTotal() { return foodTotal; }
    public void setFoodTotal(double amount) { this.foodTotal = amount; }

    public List<String> getSeats() {
        if (selectedSeats == null) return java.util.Collections.emptyList();
        return selectedSeats.stream()
                .map(Seat::getSeatId)
                .collect(Collectors.toList());
    }
}