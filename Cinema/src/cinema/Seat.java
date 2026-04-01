package cinema;

public class Seat {
    enum SeatStatus { AVAILABLE, BOOKED, SELECTING}
    enum SeatType {ADULT, STUDENT, SENIOR, OKU}

    private char row;
    private int column;
    private SeatType type;
    private SeatStatus status;
    private double price;

    Seat(char row, int column, double price){
        this.row = row;
        this.column = column;
        //this.type = type;
        this.status = SeatStatus.AVAILABLE;
        this.price = price;
    }

    public String getSeatId(){return String.valueOf(row) + column;}

    public char getRow(){return row;}

    public int getColumn() {return column;}

    public SeatStatus getStatus() {return status;}

    public double getPrice(){
        switch (type){
            case STUDENT: {return price*0.7;}
            case OKU: {return price*0.5;}
            case SENIOR: {return price*0.5;}
            default: {return price;}
        }
    }

    public boolean isAvailable() { return status == SeatStatus.AVAILABLE; }

    public void book(){
        status = SeatStatus.BOOKED;
    }
    public void select(){
        status = SeatStatus.SELECTING;
    }
    public void cancel(){
        status = SeatStatus.AVAILABLE;
    }


}
