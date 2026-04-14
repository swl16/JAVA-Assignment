package cinema;

public abstract class Seat {
    enum SeatStatus { AVAILABLE, BOOKED, SELECTING}
//    enum SeatType {ADULT, STUDENT, SENIOR, OKU}

    private char row;
    private int column;
//    private SeatType type;
    private SeatStatus status;
    private double basePrice;

    Seat(char row, int column, double price){
        this.row = row;
        this.column = column;
//        this.type = SeatType.ADULT;
        this.status = SeatStatus.AVAILABLE;
        this.basePrice = price;
    }

//    public SeatType getType() {return type;}

    public double getPrice(){
        return basePrice;
    }

    public double calculatePrice(){
        return basePrice;
    }

    // Abstract method — each subclass defines its own type label
    public abstract String getType();

    public String getSeatId(){return String.valueOf(row) + column;}

    public char getRow(){return row;}

    public int getColumn() {return column;}

    public SeatStatus getStatus() {return status;}

//    public double getPrice(SeatType type){
//        switch (type){
//            case STUDENT: {return price*0.7; }
//            case OKU: {return price*0.5;}
//            case SENIOR: {return price*0.5;}
//            default: {return price;}
//        }
//    }

    public boolean isAvailable() { return status == SeatStatus.AVAILABLE || status == SeatStatus.SELECTING; }

    public void book(){
        status = SeatStatus.BOOKED;
    }
    public void select(){
        status = SeatStatus.SELECTING;
    }
    public void cancel(){
        status = SeatStatus.AVAILABLE;
    }

    @Override
    public String toString() {
        return String.format("Seat %s | Type: %-8s | Price: RM%.2f | Status: %s",
                getSeatId(), getType(), calculatePrice(), status);
    }

    //    public void typeAdult(){ type = SeatType.ADULT; }
//    public void typeStudent(){ type = SeatType.STUDENT; }
//    public void typeSenior(){ type = SeatType.SENIOR; }
//    public void typeOku(){ type = SeatType.OKU; }
}
