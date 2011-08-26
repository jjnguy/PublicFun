import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GravityFrame extends JFrame {

   private GravityPane gravPane;
   private JButton advanceButton;
   private boolean running;
   private Timer t;

   public GravityFrame() throws FileNotFoundException {
      super("Gravity Fun!");
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      gravPane = new GravityPane();
      advanceButton = new JButton("Start");
      advanceButton.addActionListener(advanceAction);
      gravPane.addHierarchyBoundsListener(windowMovedListener);
      JPanel mainPane = new JPanel();
      mainPane.add(gravPane);
      mainPane.add(advanceButton);
      add(mainPane);
      setVisible(true);
      pack();
      int hiehgt = gravPane.getHeight();
      gravPane.addObject(new GravitySphere(200, hiehgt - 30));
      gravPane.addObject(new GravitySphere(90, 0, 120, 0, .99, 60, Color.BLUE));
      gravPane.addObject(new GravitySphere(100, 0, 120, 0));
      gravPane.addObject(new GravitySphere(300, hiehgt + 1000));
      gravPane.addObject(new GravitySphere(80, 0, 120, 0, .9, 20, Color.GREEN));
      running = false;
      t = new Timer();
   }

   private long lastMovedTime;
   private Point last_Pos;
   private HierarchyBoundsListener windowMovedListener = new HierarchyBoundsAdapter() {
      @Override
      public void ancestorMoved(HierarchyEvent arg0) {
         final int FACTOR = 10000;
         if (!isVisible())
            return;
         long prevMoveTime = lastMovedTime;
         lastMovedTime = System.nanoTime();
         long time_diff = (lastMovedTime - prevMoveTime)/100;
         Point prev_Pos = last_Pos;
         last_Pos = getLocationOnScreen();
         long xdiff = last_Pos.x - prev_Pos.x;
         long ydiff = last_Pos.y - prev_Pos.y;
         System.out.println("Time diff: " + time_diff);
         System.out.println("Location diff X: " + xdiff);
         System.out.println("Location diff Y: " + ydiff);
         double xAcel = (xdiff / (double) time_diff)*FACTOR;
         double yAcel = (ydiff / (double) time_diff)*FACTOR;
         System.out.println("Percent diff X: " + xAcel);
         System.out.println("Percent diff Y: " + yAcel);
         gravPane.energize(xAcel, yAcel);
      }
   };

   private ActionListener advanceAction = new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
         if (!running) {
            advanceButton.setText("Stop");
            long period = 1;
            t.schedule(new TimerTask() {
               @Override
               public void run() {
                  gravPane.advanceFrame();
               }
            }, 0, period);
            running = true;
         } else {
            advanceButton.setText("Start");
            t.cancel();
            t.purge();
            t = new Timer();
            running = false;
         }
      }
   };

   public static void main(String[] args) throws FileNotFoundException {
      new GravityFrame();
   }
}
