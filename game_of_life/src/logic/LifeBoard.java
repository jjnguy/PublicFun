package logic;
import java.awt.Point;
import java.util.List;

public interface LifeBoard extends Iterable<Point> {
    public void toggle(Point p);

    public void set(Point p, boolean alive);

    public boolean get(Point p);

    public void clear();
    
    public int width();

    public int height();

    /**
     * 
     * @return the points that have changed
     */
    public List<Point> step();

    public void print();
}
