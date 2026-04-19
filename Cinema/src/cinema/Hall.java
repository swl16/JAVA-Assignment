package cinema;

public class Hall {
    //name, seat(how many), type, colum&row
    private String hallName;
    private String hallType;
    private Seat[][] seats;
    private int rows;
    private int column;
    private double price;

    public Hall(String hallName, String hallType, int rows, int column, double price) {
        this.hallName = hallName;
        this.hallType = hallType;
        this.column = column;
        this.rows = rows;
        this.seats = new Seat[rows][column];
        this.price = price;

        for (int i = 0; i < rows; i++) {
            char currentRowChar = (char) ('A' + i);

            for (int j = 0; j < column; j++) {
                seats[i][j] = new AdultSeat(currentRowChar, j + 1, price);
            }
        }
    }

    public String getName(){return hallName;}

    public String getHallType() {return hallType;}

    public int getRows() {
        return rows;
    }

    public int getColumn() {
        return column;
    }

    public Seat getSeat(int r, int c){
        return seats[r][c];
    }

    public double getPrice(){ return price; }

    public int getTotalSeat() {return rows*column;}


}
