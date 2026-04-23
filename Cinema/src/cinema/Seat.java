package cinema;

public abstract class Seat {
    enum SeatStatus {AVAILABLE, BOOKED, SELECTING}

    private char row;
    private int column;
    private SeatStatus status;
    private double basePrice;

    public Seat(char row, int column, double price) {
        this.row = row;
        this.column = column;
        this.status = SeatStatus.AVAILABLE;
        this.basePrice = price;
    }

    public double getPrice() {
        return basePrice;
    }

    public double calculatePrice() {
        return basePrice;
    }

    // Abstract method
    public abstract String getType();

    public String getSeatId() {
        return String.valueOf(row) + column;
    }

    public char getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public boolean isAvailable() {
        return status == SeatStatus.AVAILABLE || status == SeatStatus.SELECTING;
    }

    public void book() {
        status = SeatStatus.BOOKED;
    }

    public void select() {
        status = SeatStatus.SELECTING;
    }

    public void cancel() {
        status = SeatStatus.AVAILABLE;
    }
    
    @Override
    public String toString(){
        return getType() + " Seat [" + "ID=" + getSeatId() + ", Price=RM " +
                String.format("%.2f",calculatePrice()) + ", Status=" + status + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || !(obj instanceof Seat)) return false;

        Seat other = (Seat) obj;

        return this.row == other.row && this.column == other.column;
    }
    
    @Override
    public int hashCode() {
        return 31 * row + column;
    }
}