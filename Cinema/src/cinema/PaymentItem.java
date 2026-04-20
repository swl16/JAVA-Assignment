package cinema;

public class PaymentItem {
    private String name;
    private int quantity;
    private double price;

    public PaymentItem(String name, int qty, double price){
        this.name = name;
        this.quantity = qty;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}

