package logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class FastLifeBoard implements LifeBoard {

    // This holds all of the live points in the system
    private Set<Point> points;

    public FastLifeBoard(byte[][] initial) {
        points = new HashSet<Point>();
        for (int i = 0; i < initial.length; i++) {
            for (int j = 0; j < initial[0].length; j++) {
                if (initial[i][j] == 1)
                    points.add(new Point(i, j));
            }
        }
    }

    public FastLifeBoard(List<Point> livePoints) {
        points = new HashSet<Point>(livePoints);
    }

    @Override
    public void toggle(Point p) {
        if (points.contains(p))
            points.remove(p);
        else
            points.add(p);
    }

    @Override
    public void set(Point p, boolean alive) {
        if (alive) {
            points.add(p);
        } else {
            points.remove(p);
        }
    }

    @Override
    public boolean get(Point p) {
        return points.contains(p);
    }

    @Override
    public Set<Point> step() {
        Set<Point> changed = new HashSet<Point>(1000);
        Set<Point> newBoard = new HashSet<Point>();
        Set<Point> alreadyProcessed = new HashSet<Point>();
        Deque<Point> toProcess = new LinkedList<Point>();
        toProcess.addAll(points);
        while (toProcess.size() > 0) {
            Point p = toProcess.removeFirst();
            if (lives(p)) {
                newBoard.add(p);
                for (Point toAdd : getSurrounding(p)) {
                    if (!alreadyProcessed.contains(toAdd)) {
                        toProcess.add(toAdd);
                        changed.add(toAdd);
                    }
                }
            } else {
                changed.add(p);
            }
            alreadyProcessed.add(p);
        }
        points = newBoard;
        return changed;
    }

    private List<Point> getSurrounding(Point p) {
        List<Point> adj = new ArrayList<Point>();
        for (int i = p.x - 2; i < p.x + 3; i++) {
            for (int j = p.y - 2; j < p.y + 3; j++) {
                if (new Point(i, j).equals(p))
                    continue;
                adj.add(new Point(i, j));
            }
        }
        return adj;
    }

    private boolean lives(Point p) {
        int liveNeighboors = 0;
        for (int i = p.x - 1; i < p.x + 2; i++) {
            for (int j = p.y - 1; j < p.y + 2; j++) {
                if (new Point(i, j).equals(p))
                    continue;
                if (get(new Point(i, j))) {
                    liveNeighboors++;
                }
            }
        }
        if (!get(p)) {
            return liveNeighboors == 3;
        } else {
            return liveNeighboors == 2 || liveNeighboors == 3;
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return points.iterator();
    }

    @Override
    public void clear() {
        points.clear();
    }
}
