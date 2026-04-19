package cinema;

public class StudentSeat extends Seat{
    public StudentSeat(char row, int column, double price){
        super(row,column,price);
    }

    @Override
    public double calculatePrice(){
        return super.getPrice() * 0.7;
    }

    @Override
    public String getType(){
        return "Student";
    }
}
