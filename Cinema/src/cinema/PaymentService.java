package cinema;

public class PaymentService {

    public double calculateTotal(Order order) {
        // This now automatically gets (Seats + F&B)
        return order.getSubtotal();
    }

    public double applyDiscount(double total, String memberCode) {
        if (memberCode == null || memberCode.isEmpty()) return total;

        switch (memberCode.toUpperCase()) {
            case "MEM10": return total * 0.9; // 10% discount [cite: 2]
            case "VIP20": return total * 0.8; // 20% discount [cite: 2]
            default: return total;
        }
    }
}