import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ClockPanel extends JPanel {

   private Clock clock;
   private JLabel time;

   public ClockPanel() {
      this(new Clock());
      setOpaque(false);
   }

   public ClockPanel(Clock clock) {
      setPreferredSize(new Dimension(300, 300));
      this.clock = clock;
      clock.run();
      time = new JLabel(clock.getTime());
      add(time);
      new Timer(200, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            time.setText(ClockPanel.this.clock.getTime());
            repaint();
         }
      }).start();
   }

   protected void paintComponent(Graphics gg) {
      super.paintComponent(gg);
      Graphics2D g = (Graphics2D) gg;
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
            RenderingHints.VALUE_ANTIALIAS_ON);
      g.setColor(Color.BLACK);
      g.drawOval(0, 0, getWidth(), getHeight());
      paintTicks(g);
      drawHourHand(g);
      drawMinuteHand(g);
      drawSecondHand(g);
   }

   private void drawHourHand(Graphics2D g) {
      double hours = (clock.getHours() * 5) % 60 + (clock.getMinutes() / 12.0);
      drawGenericHand(secToDeg(hours), .6, 35, .1, true, g);
   }

   private void drawMinuteHand(Graphics2D g) {
      double mins = clock.getMinutes() + clock.getSeconds() / 60.0;
      drawGenericHand(secToDeg(mins), .85, 10, .1, true, g);
   }

   private void drawSecondHand(Graphics2D g) {
      double seconds = clock.getSeconds(); // + clock.getMilis() / 1000.0;
      drawGenericHand(secToDeg(seconds), 1, 6, .1, true, g);
   }

   private static double secToDeg(double seconds) {
      return (seconds - 15) * 6;
   }

   private void drawGenericHand(double angle, double length, double angleDif, double wideLength, boolean border,
         Graphics2D g) {
      Point o = getOrigin();
      Point endpoint = getEndpoint(length, angle);
      Point wideEnd1 = getEndpoint(wideLength, angle + angleDif);
      Point wideEnd2 = getEndpoint(wideLength, angle - angleDif);
      if (angleDif == 0) {
         g.drawLine(o.x, o.y, endpoint.x, endpoint.y);
         return;
      } else {

         int[] x = new int[] { o.x, wideEnd1.x, endpoint.x, wideEnd2.x, o.x };
         int[] y = new int[] { o.y, wideEnd1.y, endpoint.y, wideEnd2.y, o.y };
         g.fillPolygon(new Polygon(x, y, x.length));
      }
   }

   private Point getEndpoint(double length, double degrees) {
      double hyp = getHypLen(degrees); // this hyp is the problem
      int height = (int) (Math.sin(dToR(degrees)) * hyp * length);
      int len = (int) (Math.cos(dToR(degrees)) * hyp * length);
      Point o = getOrigin();
      return new Point(o.x + len, o.y + height);
   }

   private Point getOrigin() {
      return new Point(getWidth() / 2, getHeight() / 2);
   }

   private void paintTicks(Graphics2D g) {
      for (int i = 0; i < 60; i++) {
         Point outerPoint = getEndpoint(.95, secToDeg(i));
         Point innerPoint = getEndpoint(1, secToDeg(i));
         g.drawLine(innerPoint.x, innerPoint.y, outerPoint.x, outerPoint.y);
         if (i % 15 == 0) {
            Point moreInner = getEndpoint(.85, secToDeg(i));
            g.setStroke(new BasicStroke(3));
            g.drawLine(moreInner.x, moreInner.y, innerPoint.x, innerPoint.y);
            g.setStroke(new BasicStroke(1));
         }
         if (i % 5 == 0) {
            g.setStroke(new BasicStroke(2));
            g.drawLine(outerPoint.x, outerPoint.y, innerPoint.x, innerPoint.y);
            g.setStroke(new BasicStroke(1));
         }
      }
   }

   private static double dToR(double deg) {
      return deg * (Math.PI / 180.0);
   }

   /**
    * Very complex method. Would allow the clock to be properly resized.
    * 
    * @param angle
    * @return
    */
   private double getHypLen(double angle) {
      // Write your ellipse as x2/a2+y2/b2=1 . Find where the line y=xtan(0)
      // meets the ellipse. Compute the distance from that to the center
      // (0,0).

      return getHeight() / 2.0;
   }

}
