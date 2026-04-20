package cinema;

public class FnbItem {
    private String itemName, category, description;
    private double price;

    public FnbItem(String itemName, String category, double price, String description) {
        this.itemName = itemName;
        this.category = category;
        this.price = price;
        this.description = description;
    }

    public String getItemName() {
        return itemName;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }
}
