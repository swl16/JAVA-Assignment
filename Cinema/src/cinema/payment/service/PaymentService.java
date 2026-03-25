package cinema.payment.service;

import cinema.payment.model.Order;

public class PaymentService {

    public double calculateTotal(Order order) {
        return order.getSubtotal();
    }

    public double applyDiscount(double total, String memberCode) {

        if (memberCode == null || memberCode.isEmpty())
            return total;

        switch (memberCode) {
            case "MEM10":
                return total * 0.9;

            case "VIP20":
                return total * 0.8;

            default:
                return total;
        }
    }
}