import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class SimpleLifeBoard implements LifeBoard {

    private byte[][] board;

    public SimpleLifeBoard(int size) {
        board = new byte[size][size];
    }

    public SimpleLifeBoard(byte[][] initial) {
        board = initial;
    }

    @Override
    public void toggle(Point p) {
        board[p.x][p.y] = (byte) ((int) (board[p.x][p.y] + 1) % 2);
    }

    @Override
    public void set(Point p, boolean alive) {
        board[p.x][p.y] = (byte) (alive ? 1 : 0);
    }

    @Override
    public boolean get(Point p) {
        return board[p.x][p.y] == 1;
    }

    @Override
    public List<Point> step() {
        List<Point> changed = new ArrayList<Point>();
        byte[][] newBoard = new byte[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                newBoard[i][j] = (byte) (lives(new Point(i, j), board) ? 1 : 0);
                if (newBoard[i][j] != board[i][j]) {
                    changed.add(new Point(i,j));
                }
            }
        }
        board = newBoard;
        return changed;
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

    @Override
    public void print() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    @Override
    public int width() {
        return board.length;
    }

    @Override
    public int height() {
        return board[0].length;
    }
}
