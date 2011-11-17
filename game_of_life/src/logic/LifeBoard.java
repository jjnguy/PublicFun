package logic;
import java.awt.Point;
import java.util.Collection;

public interface LifeBoard extends Iterable<Point> {
    public void toggle(Point p);

    public void set(Point p, boolean alive);

    public boolean get(Point p);

    public void clear();
    
    public void step();

    public Collection<Point> getLivePoints();
    
}