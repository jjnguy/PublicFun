package logic;
import java.awt.Point;
import java.util.Set;

public interface LifeBoard extends Iterable<Point> {
    public void toggle(Point p);

    public void set(Point p, boolean alive);

    public boolean get(Point p);

    public void clear();
    
    /**
     * 
     * @return the points that have changed
     */
    public Set<Point> step();

}