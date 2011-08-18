import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class EJSlider extends JSlider {

   public EJSlider() {
      super();
      addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            Point p = e.getPoint();
            double percent = isHorizontal() ? p.x / ((double) getWidth()) : p.y / ((double) getHeight());
            double newVal = range() * percent;
            int result = (int) (isHorizontal() ? getMinimum() + newVal : getMaximum() - newVal);
            setValue(result);
         }
      });
      addKeyListener(new KeyAdapter() {
         @Override
         public void keyTyped(KeyEvent e) {
            setLayout(null);
            final JTextField field = new JTextField((getMaximum() + "").length());
            field.setText(e.getKeyChar()+"");
            field.addKeyListener(new KeyAdapter() {
               @Override
               public void keyTyped(KeyEvent e) {
                  if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                     EJSlider.this.setValue(Integer.parseInt(field.getText()));
                     EJSlider.this.remove(field);
                  }
               }
            });
            add(field);
            field.requestFocus();
            field.setBounds(new Rectangle(0, 0, field.getPreferredSize().width, field.getPreferredSize().height));
            repaint();
         }
      });
   }

   public int range() {
      return getMaximum() - getMinimum();
   }

   public boolean isHorizontal() {
      return getOrientation() == SwingConstants.HORIZONTAL;
   }

   public static void main(String[] args) {
      JFrame f = new JFrame();
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.add(new EJSlider());
      f.pack();
      f.setVisible(true);
   }
}
