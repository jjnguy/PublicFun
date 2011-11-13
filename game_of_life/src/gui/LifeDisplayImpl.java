package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import logic.LifeBoard;

public class LifeDisplayImpl extends LifeDisplay {
    private static final long serialVersionUID = -3639174827231940297L;
    private Set<Point> points;
    private boolean grids;

    private Point origin;

    public LifeDisplayImpl(LifeBoard board) {
        setBackground(Color.LIGHT_GRAY);
        this.origin = new Point(0, 0);
        this.board = board;
        points = new HashSet<Point>();
        for (int i = 0; i < board.width(); i++) {
            for (int j = 0; j < board.height(); j++) {
                if (board.get(new Point(i, j))) {
                    points.add(new Point(i, j));
                }
            }
        }
        setPreferredSize(new Dimension(board.width() * sqWidth(), board.height() * sqWidth()));
        addMouseListener(ml);
        addMouseMotionListener(mml);
        grids = true;
    }

    @Override
    public void update() {
        List<Point> changed = board.step();
        for (Point p : changed) {
            if (board.get(p)) {
                points.add(p);
            } else {
                points.remove(p);
            }
        }
        repaint();
    }

    /**
     * Translates a game point into a bounding box to draw
     * 
     * @param p
     * @return
     */
    private int[] getCoordsFromPoint(Point p) {
        p = new Point(p.x - origin.x, p.y - origin.y);
        int[] ret = new int[4];
        int x = p.x * sqWidth();
        int y = p.y * sqWidth();
        int width = sqWidth();
        int height = sqWidth();
        ret[0] = x;
        ret[1] = y;
        ret[2] = width;
        ret[3] = height;
        return ret;
    }

    @Override
    public void setGrids(boolean on) {
        grids = on;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Point p : points) {
            drawPoint(p, g);
        }
        if (grids)
            drawGridLines(g);
    }

    private void drawGridLines(Graphics g) {
        g.setColor(Color.BLACK);
        for (int i = 0; i < getWidth(); i += sqWidth()) {
            g.drawLine(i, 0, i, getHeight());
        }
        for (int i = 0; i < getHeight(); i += sqWidth()) {
            g.drawLine(0, i, getWidth(), i);
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

    private MouseMotionListener mml = new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
            if (!e.isControlDown()) {
                Point sqPoint = translateClickedLocationToSquarePoint(e.getPoint());
                points.add(sqPoint);
                board.set(sqPoint, true);
            } else if (dragInitiate != null) {
                dragProgress = e.getPoint();
                int xdiff = dragInitiate.x - dragProgress.x;
                int ydiff = dragInitiate.y - dragProgress.y;
                origin = new Point(origin.x + xdiff, origin.y + ydiff);
                dragInitiate = e.getPoint();
            }
            repaint();
        }
    };

    private Point dragInitiate;
    private Point dragProgress;
    private MouseListener ml = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            Point sqPoint = translateClickedLocationToSquarePoint(e.getPoint());
            points.add(sqPoint);
            board.toggle(sqPoint);
            repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.isControlDown()) {
                dragInitiate = e.getPoint();
            }
        };
        
        @Override
        public void mouseReleased(MouseEvent e) {
            dragInitiate = null;
            dragProgress = null;
        };
    };

    private Point translateClickedLocationToSquarePoint(Point clicked) {
        int x = clicked.x / sqWidth();
        int y = clicked.y / sqWidth();
        Point ret = new Point(x + origin.x, y + origin.y);
        return ret;
    }

    @Override
    public void applyBoard(LifeBoard newB) {
        board.clear();
        
    }
}
