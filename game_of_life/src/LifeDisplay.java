import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class LifeDisplay extends JPanel {
    private LifeBoard board;
    private LifeSquare[][] squares;
    boolean mouseDown;

    public LifeDisplay(LifeBoard board) {
        super(new GridLayout(board.width(), board.height()));
        mouseDown = false;
        this.board = board;
        squares = new LifeSquare[board.width()][board.height()];
        for (int i = 0; i < board.width(); i++) {
            for (int j = 0; j < board.height(); j++) {
                squares[i][j] = new LifeSquare(new Point(i, j));
                add(squares[i][j]);
            }
        }
    }

    public void update() {
        List<Point> changed = board.step();
        for(Point p: changed) {
            if (p.x < 0 || p.x >= squares.length)
                continue;
            if (p.y < 0 || p.y >= squares[0].length)
                continue;
            squares[p.x][p.y].repaint();
        }
    }

    class LifeSquare extends JPanel {
        Point coord;
        public LifeSquare(Point p) {
            coord = p;
            setPreferredSize(new Dimension(6, 6));
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    board.toggle(coord);
                    repaint();
                }
                
                @Override
                public void mousePressed(MouseEvent e) {
                    mouseDown = true;
                }
                
                @Override
                public void mouseReleased(MouseEvent e) {
                    mouseDown = false;
                }
                
                @Override
                public void mouseEntered(MouseEvent arg0) {
                    if (mouseDown) {
                        board.toggle(coord);
                        repaint();
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (board.get(coord))
                setBackground(Color.BLACK);
            else
                setBackground(Color.LIGHT_GRAY);
        }
    }
}
