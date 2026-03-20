package cinema.payment;

import cinema.payment.model.Order;
import cinema.payment.ui.PaymentFrame;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        Order order = new Order();
        order.setMovieTitle("Hoppers");
        order.setCinemaLocation("KL - Melawati");
        order.setTime("Thu 19 Mar, 11:30 PM");
        order.setHall("Hall 2");

        order.setSeats(Arrays.asList("B07"));

        order.setTicketQuantity(1);
        order.setTicketPrice(19.0);
        order.setFoodTotal(0);

        new PaymentFrame(order); // MUST exist
    }
}