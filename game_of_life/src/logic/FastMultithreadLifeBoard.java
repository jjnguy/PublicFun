package logic;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class FastMultithreadLifeBoard extends FastLifeBoard {

    private final int maxThreads = Runtime.getRuntime().availableProcessors();

    public FastMultithreadLifeBoard(byte[][] initial) {
        super(initial);
    }

    public FastMultithreadLifeBoard(List<Point> livePoints) {
        super(livePoints);
    }

    @Override
    public void step() {
        // newBoard will get filled with our new live points
        Set<Point> newBoard = new HashSet<Point>(points.size(), .3f);
        int numberPointsPerSet = points.size() / maxThreads;
        List<Set<Point>> splitPoints = new ArrayList<Set<Point>>();
        for (int j = 0; j < maxThreads; j++) {
            Set<Point> splitSet = new HashSet<Point>();
        }

        points = newBoard;
    }

    private class PointProcessor implements Runnable {

        private final BlockingDeque<Set<Point>> workQueue;
        private final BlockingDeque<Set<Point>> resultQueue;

        private PointProcessor() {
            workQueue = new LinkedBlockingDeque<Set<Point>>();
            resultQueue = new LinkedBlockingDeque<Set<Point>>();
        }

        public Set<Point> process(Set<Point> points)
                throws InterruptedException {
            workQueue.offer(points);
            return resultQueue.take();
        }

        private void processNext() throws InterruptedException {
            Set<Point> pointsToProcess = workQueue.take();
            Set<Point> myPointsToProcess = new HashSet<Point>(pointsToProcess);

            // newBoard will get filled with our new live points
            Set<Point> newBoard = new HashSet<Point>(points.size(), .3f);
            // alreadyProcessed stores any point that we may have already
            // checked if it lives the next generation
            Set<Point> alreadyProcessed = new HashSet<Point>();
            // while there continues to be points to process
            while (myPointsToProcess.size() > 0) {
                // we may add new points to process as we go
                Set<Point> newToProcess = new HashSet<Point>(
                        myPointsToProcess.size(), .3f);
                // process each point that remains
                for (Point p : myPointsToProcess) {
                    if (lives(p)) {
                        // if the point lives, it will be in our new generation
                        newBoard.add(p);
                        // now we check that point's surroundings to see if any
                        // new
                        // points are spawned in this generation
                        // we only check points that live for surroundings
                        // otherwise
                        // we end up infinitely checking all points in
                        // existence.
                        for (Point toAdd : getSurrounding(p)) {
                            if (!alreadyProcessed.contains(toAdd)) {
                                newToProcess.add(toAdd);
                            }
                        }
                    }
                    // we will not process this point again
                    alreadyProcessed.add(p);
                }
                // we will now process any new points that were added to our
                // list
                myPointsToProcess = newToProcess;
            }
            resultQueue.offer(newBoard);
        }

        @Override
        public void run() {
            try {
                processNext();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
