package edu.iastate.cs228.hw1.ui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.iastate.cs228.hw1.IGame;
import edu.iastate.cs228.hw1.IPlayLevel;
import edu.iastate.cs228.hw1.impl.BasicPlayLevel;
import edu.iastate.cs228.hw1.impl.TetrisGame;

/**
 * Main class for a GUI for a Tetris game sets up a 
 * GamePanel instance in a frame.
 */
public class GameMain
{
  /**
   * Cell size in pixels.
   */
  public static final int SIZE = 25; 
  
  /**
   * Helper method for instantiating the components.  This
   * method should be executed in the context of the Swing
   * event thread only.
   */
  private static void create()
  {
    IPlayLevel level = new BasicPlayLevel();
    IGame game = new TetrisGame();
    GamePanel panel = new GamePanel(game, level);
    JFrame frame = new JFrame();
    frame.getContentPane().add(panel);
    
    // give it a nonzero size
    panel.setPreferredSize(new Dimension(game.getWidth() * GameMain.SIZE, game.getHeight() * GameMain.SIZE));
    frame.pack();
    
    // we want to shut down the application if the 
    // "close" button is pressed on the frame
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // be sure key events get to the panel
    panel.requestFocus();
    
    // rock and roll...
    frame.setVisible(true);
  }
  
  /**
   * Entry point.  Main thread passed control immediately
   * to the Swing event thread.
   * @param args not used
   */
  public static void main(String[] args)
  {
    Runnable r = new Runnable()
    {
      public void run()
      {
        create();
      }
    };
    SwingUtilities.invokeLater(r);
  }
}
