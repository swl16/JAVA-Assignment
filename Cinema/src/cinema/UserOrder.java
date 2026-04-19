package cinema;

import java.time.LocalDateTime;
import java.util.*;

public class UserOrder {
    //private int orderId;
    private String username;
    private Movie movie;
    private ShowTime showTime;
    private List<Seat> selectedSeats;
    private int[] seatTypeCount;
//    private List<String> selectedFood;
    Map<fnbitem, Integer> selectedFood;
    private double ticketTotalPrice;
    private double foodTotalPrice;

    private final double processingFee = 0.5;

    public UserOrder( String username, Movie movie, ShowTime showTime, List<Seat> selectedSeats,int[] seatTypeCount,Map<fnbitem, Integer> selectedFood,double ticketTotalPrice,double foodTotalPrice){
       // this.orderId = orderId;
        this.username = username;
        this.movie = movie;
        this.showTime = showTime;
        this.selectedSeats = selectedSeats;
        this.seatTypeCount = seatTypeCount;
        this.selectedFood = selectedFood;
        this.ticketTotalPrice = ticketTotalPrice;
        this.foodTotalPrice = foodTotalPrice;

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double calculateProcessingFee(){
        return processingFee * selectedSeats.size();
    }

    public double calculateTotalPrice(){
        return ticketTotalPrice + foodTotalPrice + calculateProcessingFee();
    }

    public String getUsername() { return username; }

    public Movie getMovie() {return movie;}
    public ShowTime getShowTime() { return showTime; }
    public List<Seat> getSelectedSeats() { return selectedSeats; }
    public int[] getSeatTypeCount() { return seatTypeCount; }

    public Map<fnbitem, Integer> getSelectedFood() {
        return selectedFood;
    }

    //    public List<String> getSelectedFood() { return selectedFood; }
    public double getTicketTotalPrice() { return ticketTotalPrice; }
    public double getFoodTotalPrice() { return foodTotalPrice; }

     public void addFoodItem(fnbitem item, int quantity){
        selectedFood.put(item,quantity);
     }
     public void removeFood(fnbitem item){
        selectedFood.remove(item);
     }

    public void setFoodTotalPrice(double foodTotalPrice) {
        this.foodTotalPrice = foodTotalPrice;
    }
}
