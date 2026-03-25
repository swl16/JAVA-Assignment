package cinema.payment.model;

import java.util.List;

public class Order {

    // Basic
    private String movieTitle;
    private String cinemaLocation;
    private String time;
    private String hall;

    // Seats
    private List<String> seats;

    // Ticket
    private int ticketQuantity;
    private double ticketPrice;

    // F&B
    private double foodTotal;

    // ================= CALCULATION =================
    public double getSubtotal() {
        return (ticketQuantity * ticketPrice) + foodTotal;
    }

    // ================= GETTERS & SETTERS =================

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public String getCinemaLocation() { return cinemaLocation; }
    public void setCinemaLocation(String cinemaLocation) { this.cinemaLocation = cinemaLocation; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getHall() { return hall; }
    public void setHall(String hall) { this.hall = hall; }

    public List<String> getSeats() { return seats; }
    public void setSeats(List<String> seats) { this.seats = seats; }

    public int getTicketQuantity() { return ticketQuantity; }
    public void setTicketQuantity(int ticketQuantity) { this.ticketQuantity = ticketQuantity; }

    public double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }

    public double getFoodTotal() { return foodTotal; }
    public void setFoodTotal(double foodTotal) { this.foodTotal = foodTotal; }
}