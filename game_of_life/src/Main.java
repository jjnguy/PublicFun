import gui.LifeDisplay;
import gui.LifeDisplayImpl;
import gui.LifeFrame;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import logic.FastLifeBoard;
import logic.LifeBoard;
import util.LifeBoardSaveAndLoad;

public class Main {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
       //LifeBoard b = new FastLifeBoard(new ArrayList<Point>());
       //LifeDisplay disp = new LifeDisplayImpl(b);
       //LifeFrame frame = new LifeFrame(disp);
       for (int i = 0; i < 100; i++) {
            LifeBoard fastBoard = LifeBoardSaveAndLoad
                    .load("J:\\Documents\\gliderGun.life");
            System.out.println(performanceTest(fastBoard, 4000));
        }
    }

    public static byte[][] buildArray() {
        byte[][] squares = new byte[50][50];
        squares[0][0] = 1;
        squares[1][1] = 1;
        squares[2][1] = 1;
        squares[0][2] = 1;
        squares[1][2] = 1;
        return squares;
    }

    public static long performanceTest(LifeBoard b, long stepCount) {
        long startTime = System.currentTimeMillis();
        for (long i = 0; i < stepCount; i++) {
            b.step();
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
