package logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
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
   public void step() {
      // newBoard will get filled with our new live points
      Set<Point> newBoard = new HashSet<Point>(points.size(), .3f);
      // alreadyProcessed stores any point that we may have already checked if it lives the next generation
      Set<Point> alreadyProcessed = new HashSet<Point>();
      // toProcess will contain a set of items that still needs to be processed
      Set<Point> toProcess = points;
      // while there continues to be points to process
      while (toProcess.size() > 0) {
         // we may add new points to process as we go
         Set<Point> newToProcess = new HashSet<Point>(toProcess.size(), .3f);
         // process each point that remains
         for (Point p : toProcess) {
            if (lives(p)) {
               // if the point lives, it will be in our new generation
               newBoard.add(p);
               // now we check that point's surroundings to see if any new points are spawned in this generation
               // we only check points that live for surroundings otherwise we end up infinitely checking all points in existence.
               for (Point toAdd : getSurrounding(p)) {
                  if (!alreadyProcessed.contains(toAdd)) {
                     newToProcess.add(toAdd);
                  }
               }
            }
            // we will not process this point again
            alreadyProcessed.add(p);
         }
         // we will now process any new points that were added to our list
         toProcess = newToProcess;
      }
      points = newBoard;
   }

   private List<Point> getSurrounding(Point p) {
      List<Point> adj = new ArrayList<Point>();
      for (int i = p.x - 2; i < p.x + 3; i++) {
         for (int j = p.y - 2; j < p.y + 3; j++) {
            Point cur = new Point(i, j);
            if (cur.equals(p))
               continue;
            adj.add(cur);
         }
      }
      return adj;
   }

   private boolean lives(Point p) {
      int liveNeighboors = 0;
      for (int i = p.x - 1; i < p.x + 2; i++) {
         for (int j = p.y - 1; j < p.y + 2; j++) {
            Point cur = new Point(i, j);
            if (cur.equals(p))
               continue;
            if (get(cur)) {
               liveNeighboors++;
            }
         }
         if (liveNeighboors > 3)
            return false;
      }
      if (get(p)) {
         return liveNeighboors == 2 || liveNeighboors == 3;
      } else {
         return liveNeighboors == 3;
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

   @Override
   public Collection<Point> getLivePoints() {
      return points;
   }
}
