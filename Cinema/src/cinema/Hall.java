package cinema;

public class Hall {
    //name, seat(how many), type, colum&row
    private String hallName;
    private String hallType;
    public Seat[][] seats;
    private int rows;
    private int column;

    Hall(String hallName,String hallType,int rows, int column, double price) {
        this.hallName = hallName;
        this.hallType = hallType;
        this.column = column;
        this.rows = rows;
        this.seats = new Seat[rows][column];

        char[] rowLetter = {'A' , 'B' , 'C' , 'D' , 'E' , 'F' , 'G' , 'H' , 'I' , 'J'};

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < column; j++) {
                seats[i][j] = new Seat(rowLetter[i], j + 1, price);
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

    public int getTotalSeat() {return rows*column;}


}
