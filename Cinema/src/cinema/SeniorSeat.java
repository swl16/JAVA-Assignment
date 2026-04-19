package cinema;

public class SeniorSeat extends Seat{
    public SeniorSeat(char row, int column, double price){
        super(row, column, price);
    }

    @Override
    public double calculatePrice(){
        return super.getPrice() * 0.5;
    }

    @Override
    public String getType(){
        return "Senior";
    }
}
