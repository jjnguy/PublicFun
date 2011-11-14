import java.io.FileNotFoundException;

import util.LifeBoardSaveAndLoad;
import gui.LifeDisplay;
import gui.LifeDisplayImpl;
import gui.LifeFrame;
import logic.FastLifeBoard;
import logic.LifeBoard;

public class Main {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        for (int i = 0; i < 100; i++) {
            LifeBoard fastBoard = LifeBoardSaveAndLoad
                    .load("C:\\Users\\Justin\\Documents\\gliderGun.life");
            System.out.println(performanceTest(fastBoard, 1000));
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
