import gui.FileLocationPanel;

import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class CalculatorRunner {

   public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
      UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
      JFrame f = new JFrame("Coding Competition Results");
      f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      f.add(new FileLocationPanel());
      f.pack();
      f.setResizable(false);
      f.setVisible(true);
   }
}
