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
        this.type = SeatType.ADULT;
        this.status = SeatStatus.AVAILABLE;
        this.price = price;
    }

    public SeatType getType() {return type;}

    public String getSeatId(){return String.valueOf(row) + column;}

    public char getRow(){return row;}

    public int getColumn() {return column;}

    public SeatStatus getStatus() {return status;}

    public double getPrice(SeatType type){
        switch (type){
            case STUDENT: {return price*0.7;}
            case OKU,SENIOR: {return price*0.5;}
            default: {return price;}
        }
    }

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

    public void typeAdult(){ type = SeatType.ADULT; }
    public void typeStudent(){ type = SeatType.STUDENT; }
    public void typeSenior(){ type = SeatType.SENIOR; }
    public void typeOku(){ type = SeatType.OKU; }
}
