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
    public String toString() {
        return String.format("Seat %s | Type: %-8s | Price: RM%.2f | Status: %s",
                getSeatId(), getType(), calculatePrice(), status);
    }
}