import java.awt.Point;

public class LifeBoard {

    private byte[][] board;

    public LifeBoard(int size) {
        board = new byte[size][size];
    }

    public LifeBoard(byte[][] initial) {
        board = initial;
    }

    public void toggle(Point p) {
        board[p.x][p.y] = (byte) ((int) (board[p.x][p.y] + 1) % 2);
    }

    public void set(Point p, byte val) {
        board[p.x][p.y] = val;
    }

    public byte get(Point p) {
        return board[p.x][p.y];
    }

    public int size() {
        return board.length;
    }

    public void step() {
        byte[][] newBoard = new byte[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                newBoard[i][j] = (byte) (lives(new Point(i, j), board) ? 1 : 0);
            }
        }
        board = newBoard;
    }

    private static boolean lives(Point p, byte[][] squares) {
        int liveNeighboors = 0;
        for (int i = p.x - 1; i < p.x + 2; i++) {
            for (int j = p.y - 1; j < p.y + 2; j++) {
                if (new Point(i, j).equals(p))
                    continue;
                try {
                    liveNeighboors += squares[i][j];
                } catch (Exception e) {
                }
            }
        }
        if (squares[p.x][p.y] == 0) {
            return liveNeighboors == 3;
        } else {
            return liveNeighboors == 2 || liveNeighboors == 3;
        }
    }

    public byte[][] expose() {
        return board;
    }

    public void print() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
