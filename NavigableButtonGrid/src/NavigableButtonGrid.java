import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class NavigableButtonGrid extends JPanel {

   private JButton[][] buttons;

   public NavigableButtonGrid(int row, int col) {
      super(new GridLayout(row, col));
      buttons = new JButton[row][col];
      for (int i = 0; i < buttons.length; i++) {
         for (int j = 0; j < buttons[i].length; j++) {
            final int curRow = i;
            final int curCol = j;
            buttons[i][j] = new JButton(i + ", " + j);
            buttons[i][j].addKeyListener(enter);
            buttons[i][j].addKeyListener(new KeyAdapter() {
               @Override
               public void keyPressed(KeyEvent e) {
                  switch (e.getKeyCode()) {
                  case KeyEvent.VK_UP:
                     if (curRow > 0) {
                        buttons[curRow][curCol].setText("^");
                        buttons[curRow - 1][curCol].requestFocus();
                     }
                     break;
                  case KeyEvent.VK_DOWN:
                     if (curRow < buttons.length - 1) {
                        buttons[curRow][curCol].setText("v");
                        buttons[curRow + 1][curCol].requestFocus();
                     }
                     break;
                  case KeyEvent.VK_LEFT:
                     if (curCol > 0) {
                        buttons[curRow][curCol].setText("<");
                        buttons[curRow][curCol - 1].requestFocus();
                     }
                     break;
                  case KeyEvent.VK_RIGHT:
                     if (curCol < buttons[curRow].length - 1) {
                        buttons[curRow][curCol].setText(">");
                        buttons[curRow][curCol + 1].requestFocus();
                     }
                     break;
                  default:
                     break;
                  }
               }
            });
            add(buttons[i][j]);
         }
      }
   }

   public static Font getStrikethroughFont2(String name, int properties, int size) {
      Font font = new Font(name, properties, size);
      Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
      attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
      Font newFont = font.deriveFont(attributes);
      return newFont;
   }

   private KeyListener enter = new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
         if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            ((JButton) e.getComponent()).doClick();
         }
      }
   };

   public static void main(String[] args) {
      JFrame f = new JFrame();
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.add(new NavigableButtonGrid(30, 24));
      f.pack();
      f.setVisible(true);
   }
}
