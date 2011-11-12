import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


public class LifeDisplayNew extends LifeDisplay {
    private final int SQUARE_WIDTH = 8;
    private LifeBoard board;
    private Set<Point> points;
    
    
    public LifeDisplayNew(LifeBoard board) {
        setBackground(Color.LIGHT_GRAY);
        this.board = board;
        points = new HashSet<Point>();
        for (int i = 0; i < board.width(); i++) {
            for (int j = 0; j < board.height(); j++) {
                if (board.get(new Point(i, j))){
                    points.add(new Point(i, j));
                }
            }
        }
        addMouseListener(ml);
    }

    @Override
    public void update() {
        List<Point> changed = board.step();
        for(Point p: changed) {
            if (board.get(p)) {
                points.add(p);
            } else {
                points.remove(p);
            }
        }
        repaint();
    }

    
    private int[] getCoordsFromPoint(Point p) {
        int[] ret = new int[4];
        int x = p.x * SQUARE_WIDTH;
        int y = p.y * SQUARE_WIDTH;
        int width = SQUARE_WIDTH;
        int height = SQUARE_WIDTH;
        ret[0] = x;
        ret[1] = y;
        ret[2] = width;
        ret[3] = height;
        return ret;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Point p: points) {
            drawPoint(p, g);
        }
    }
    
    private void drawPoint(Point p, Graphics g) {
        int[] coords = getCoordsFromPoint(p);
        if (board.get(p)) {
            g.setColor(Color.BLACK);
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }
        fillRect(coords, g);
    }
    
    private static void fillRect(int[] coords, Graphics g) {
        g.fillRect(coords[0], coords[1], coords[2], coords[3]);
    }
    
    private MouseListener ml = new MouseListener() {
        
        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
            Point sqPoint = translateClickedLocationToSquarePoint(e.getPoint());
            points.add(sqPoint);
            board.set(sqPoint, true);
            repaint();
        }
    };
    
    private Point translateClickedLocationToSquarePoint(Point clicked) {
        int x = clicked.x / SQUARE_WIDTH;
        int y = clicked.y / SQUARE_WIDTH;
        Point ret = new Point(x, y);
        return ret;
    }
}
