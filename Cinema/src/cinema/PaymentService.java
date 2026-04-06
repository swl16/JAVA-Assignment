package cinema;

import java.util.HashMap;
import java.util.Map;

public class PaymentService {
    // Simulate a database of members and their specific discount rates
    private final Map<String, Double> memberDatabase;

    public PaymentService() {
        memberDatabase = new HashMap<>();
        memberDatabase.put("MEM10", 0.10); // 10% off
        memberDatabase.put("VIP20", 0.20); // 20% off
    }

    public double calculateTotal(Order order) {
        return order.getSubtotal(); //
    }

    public double applyDiscount(double total, String memberCode) {
        if (memberCode == null) return total;

        String code = memberCode.toUpperCase().trim();
        if (memberDatabase.containsKey(code)) {
            double rate = memberDatabase.get(code);
            return total * (1 - rate); //
        }
        return total;
    }
}