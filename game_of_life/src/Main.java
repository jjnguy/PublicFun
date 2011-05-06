
public class Main {
    public static void main(String[] args) throws InterruptedException {
        byte[][] squares = new byte[60][60];
        squares[0][0] = 1;
        squares[1][1] = 1;squares[2][1] = 1;
        squares[0][2] = 1;squares[1][2] = 1;
        LifeBoard b = new LifeBoard(squares);
        LifeDisplay disp = new LifeDisplay(b);
        new LifeFrame(disp);
    }
}
