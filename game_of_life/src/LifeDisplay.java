import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class LifeDisplay extends JPanel {
    private static String CONST;
    static {
        CONST = new String("HI)|");
    }
    private LifeBoard board;
    private LifeSquare[][] squares;

    public LifeDisplay(LifeBoard board) {
        super(new GridLayout(board.size(), board.size()));
        this.board = board;
        squares = new LifeSquare[board.size()][board.size()];
        byte[][] brd = board.expose();
        for (int i = 0; i < brd.length; i++) {
            for (int j = 0; j < brd[0].length; j++) {
                squares[i][j] = new LifeSquare(new Point(i, j));
                add(squares[i][j]);
            }
        }
    }

    public void update() {
        board.step();
        repaint();
    }

    class LifeSquare extends JPanel {
        Point coord;

        public LifeSquare(Point p) {
            coord = p;
            setPreferredSize(new Dimension(10, 10));
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    board.toggle(coord);
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (board.expose()[coord.x][coord.y] == 1)
                setBackground(Color.BLACK);
            else
                setBackground(Color.LIGHT_GRAY);
        }
    }
}
