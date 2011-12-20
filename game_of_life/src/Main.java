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
      String loc = "C:\\Users\\U0117691\\Documents\\gliderGun.life";
      LifeBoard fastBoard = LifeBoardSaveAndLoad.load(Main.class.getClassLoader().getResourceAsStream(
            "files/gliderGun.life"));
      LifeDisplay disp = new LifeDisplayImpl(fastBoard);
      LifeFrame frame = new LifeFrame(disp);
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
