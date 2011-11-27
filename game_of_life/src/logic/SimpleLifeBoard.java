package logic;

import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SimpleLifeBoard implements LifeBoard {

    private Point origin;
    private byte[][] board;

    public SimpleLifeBoard(int size) {
        origin = new Point();
        board = new byte[size][size];
    }

    public SimpleLifeBoard(byte[][] initial) {
        origin = new Point();
        board = initial;
    }

    public SimpleLifeBoard(Collection<Point> livePoints) {
        int max_X = Integer.MIN_VALUE;
        int max_Y = Integer.MIN_VALUE;
        int min_X = 0;
        int min_Y = 0;
        for (Point p : livePoints) {
            max_X = Math.max(max_X, p.x);
            max_Y = Math.max(max_Y, p.y);
            min_X = Math.min(min_X, p.x);
            min_Y = Math.min(min_Y, p.y);
        }
        byte[][] board = new byte[max_X * 4][max_Y * 4];
        for (Point p : livePoints) {
            board[p.x - min_X][p.y - min_Y] = 1;
        }
        origin = new Point(min_X, min_Y);
        this.board = board;
    }

    @Override
    public void toggle(Point p) {
        board[p.x - origin.x][p.y - origin.y] = (byte) ((int) (board[p.x - origin.x][p.y - origin.y] + 1) % 2);
    }

    @Override
    public void set(Point p, boolean alive) {
        board[p.x - origin.x][p.y - origin.y] = (byte) (alive ? 1 : 0);
    }

    @Override
    public boolean get(Point p) {
        return board[p.x - origin.x][p.y - origin.y] == 1;
    }

    @Override
    public void step() {
        rangeCheck();
        byte[][] newBoard = new byte[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                newBoard[i][j] = (byte) (lives(new Point(i, j)) ? 1 : 0);
            }
        }
        board = newBoard;
    }

    private boolean lives(Point p) {
        int liveNeighboors = countLiveNeighboors(p);
        if (board[p.x][p.y] == 0) {
            return liveNeighboors == 3;
        } else {
            return liveNeighboors == 2 || liveNeighboors == 3;
        }
    }

    private int countLiveNeighboors(Point p) {
        int liveNeighboors = 0;
        for (int i = p.x - 1; i < p.x + 2; i++) {
            for (int j = p.y - 1; j < p.y + 2; j++) {
                if (new Point(i, j).equals(p))
                    continue;
                if (i < 0 || i >= board.length || j < 0 || j >= board[0].length)
                    liveNeighboors += 0;
                else
                    liveNeighboors += board[i][j];
            }
        }
        return liveNeighboors;
    }

    /**
     * If any of the current points are near the edge of the board, we will grow the size of the
     * board.
     */
    private void rangeCheck() {
        int top = 0, bottom = 0, left = 0, right = 0;
        // Checks the top and bottom 2 rows
        for (int i = 0; i < board.length; i++) {
            if (board[i][0] == 1 || board[i][1] == 1 || board[i][2] == 1) {
                top = 20;
            }
            if (board[i][board[i].length - 1] == 1 || board[i][board[i].length - 2] == 1
                    || board[i][board[i].length - 3] == 1) {
                bottom = 20;
            }
            if (top > 0 && bottom > 0) {
                break;
            }
        }
        // Checks the far left and right columns
        for (int j = 0; j < board[0].length; j++) {
            if (board[0][j] == 1 || board[1][j] == 1 || board[2][j] == 1) {
                left = 20;
            }
            if (board[board.length - 1][j] == 1 || board[board.length - 2][j] == 1
                    || board[board.length - 3][j] == 1) {
                right = 20;
            }
            if (right > 0 && left > 0) {
                break;
            }
        }
        if (top + bottom + left + right != 0) {
            origin = new Point(origin.x - left, origin.y - top);
            System.out.println("New Origin: " + origin);
            board = grow(board, top, bottom, left, right);
        }
    }

    public static byte[][] grow(byte[][] original, int up, int down, int left, int right) {
        System.out.println(String.format("Growing: %d %d %d %d", up, down, left, right));
        byte[][] ret = new byte[original.length + left + right][original[0].length + up + down];
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[0].length; j++) {
                ret[i + left][j + up] = original[i][j];
            }
        }
        return ret;
    }

    @Override
    public Iterator<Point> iterator() {
        return getLivePoints().iterator();
    }

    @Override
    public void clear() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = 0;
            }
        }
    }

    @Override
    public Collection<Point> getLivePoints() {
        Set<Point> points = new HashSet<Point>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 1) {
                    points.add(new Point(i, j));
                }
            }
        }
        return points;
    }
}
