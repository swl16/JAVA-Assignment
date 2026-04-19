package cinema;

public class AdultSeat extends Seat {
    public AdultSeat(char row, int column, double price){
        super(row,column,price);
    }

    @Override
    public double calculatePrice(){
        return super.getPrice();
    }

    @Override
    public String getType(){
        return "Adult";
    }
}
