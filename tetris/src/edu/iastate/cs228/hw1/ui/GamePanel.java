package edu.iastate.cs228.hw1.ui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import edu.iastate.cs228.hw1.GameStatus;
import edu.iastate.cs228.hw1.IGame;
import edu.iastate.cs228.hw1.IPlayLevel;
import edu.iastate.cs228.hw1.IPolyomino;
import edu.iastate.cs228.hw1.impl.BasicPlayLevel;

/**
 * User interface for the main grid of a Tetris game.
 */
public class GamePanel extends JPanel
{
  /**
   * The javax.swing.Timer instance used to animate the UI.
   */
  private Timer timer;
  
  /**
   * The IGame instance for which this is the UI.
   */
  private IGame game;
  
  /**
   * State variable indicating when the timer frequency is increased
   * for dropping a polyomino to the bottom of the grid.
   */
  private boolean fastDrop;
  
  /**
   * The difficulty level of the game, which determines the
   * speed of the animation.
   */
  private IPlayLevel level;
  
  /**
   * When the IGame object is in the COLLAPSING state, this 
   * field stores the index of the next row that will be 
   * collapsed.  This value can be used to apply special rendering
   * or animation to the collapsing row.  An invariant is maintained
   * that this variable is >= 0 if and only if the IGame state
   * is COLLAPSING.
   */
  private int rowToCollapse = -1;
  
  /**
   * Constructs a GamePanel with the given game and play level.
   * @param pGame the IGame instance for which this is the UI
   * @param pLevel the IPlayLevel that will determine the 
   *   animation speed for this UI
   */
  public GamePanel(IGame pGame, IPlayLevel pLevel)
  {
    game = pGame;
    level = pLevel;
    addKeyListener(new MyKeyListener());
    timer = new Timer(level.speed(0) , new TimerCallback());
    timer.start();
  }
  
  // The paintComponent is invoked by the Swing framework whenever
  // the panel needs to be rendered on the screen.  In this application,
  // repainting is normally triggered by the calls to the repaint() 
  // method in the timer callback and the keyboard event handler (see below).
  
  @Override
  public void paintComponent(Graphics g)
  {
    // clear background
    g.clearRect(0, 0, getWidth(), getHeight());
    
    // paint occupied cells of the grid.  Each occupied
    // cell contains a reference to the polyomino that
    // was originally frozen in that cell.  The polyomino may have
    // been part of a collapsed row, so its position is no longer
    // meaningful, but it is still used to decide what 
    // color to paint the cell
    for (int row = 0; row < game.getHeight(); ++row)
    {
      for (int col = 0; col < game.getWidth(); ++col)
      {
        IPolyomino t = game.getCell(row, col);
        if (t != null)
        { 
          paintOneCell(g, row, col, t);
        }
      }
    }
    
    if (rowToCollapse >= 0)
    {
      // if a row is collapsing, paint it black for one frame
      g.setColor(Color.DARK_GRAY);
      g.fillRect(0, rowToCollapse * GameMain.SIZE, game.getWidth() * GameMain.SIZE, GameMain.SIZE);
    }
    else if (!game.gameOver())
    {
      // otherwise, paint the current polyomino
      IPolyomino t = game.getCurrent();
      g.setColor(t.getColorHint());
      for (Point p : t.getCells())
      {
        paintOneCell(g, p.y, p.x, t);
      }
    }
  }
  
  /**
   * Renders a single cell of the grid.
   * 
   * @param g the Swing graphics context
   * @param row y-coordinate of the cell to render
   * @param col x-coordinate of the cell to render
   * @param t the IPolyomino associated with the cell, normally used
   *   to determine the color with which to render the cell
   */
  private void paintOneCell(Graphics g, int row, int col, IPolyomino t)
  {
    // scale everything up by the SIZE
    int x = GameMain.SIZE * col;
    int y = GameMain.SIZE * row;
    g.setColor(t.getColorHint());
    g.fillRect(x, y, GameMain.SIZE, GameMain.SIZE);
    g.setColor(Color.GRAY);;
    g.drawRect(x, y, GameMain.SIZE - 1, GameMain.SIZE - 1);
  }
  
  /**
   * Method invoked each time the timer fires.  
   */
  private void doOneStep()
  {
    // transition the game through one step, and interpret the status
    GameStatus state = game.step();
    
    if (state == GameStatus.GAME_OVER)
    {
      timer.stop();
    }
    else
    {
      if (state == GameStatus.NEW_POLYOMINO)
      {
        // if we were in the collapsing state, we're done
        rowToCollapse = -1;
      }
      if (state == GameStatus.COLLAPSING || state == GameStatus.STOPPED)
      {
        // current polygon has reached the bottom, so if we were doing
        // a fast drop, reset the timer to normal
        if (fastDrop)
        {
          fastDrop = false;
          int speed = level.speed(game.getTotalCompletedRows());
          timer.setDelay(speed);
          timer.setInitialDelay(speed);
          timer.restart();
        }
        if (state == GameStatus.COLLAPSING)
        {
          // indicates that next call to step() will collapse a row,
          // so we want to paint it differently this time
          rowToCollapse = game.getNextRowToCollapse();
        }
      }
      // else state is FALLING, nothing to do but repaint
    }   
  }
  
  /**
   * Listener for timer events.  The actionPerformed method
   * is invoked each time the timer fires.
   */
  private class TimerCallback implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent arg0)
    {
      doOneStep();
      repaint();
    }
  }
  
  /**
   * Listener for keyboard events.
   */
  private class MyKeyListener implements KeyListener
  {
    @Override
    public void keyPressed(KeyEvent e)
    {
      int code = e.getKeyCode();
      if (code == KeyEvent.VK_RIGHT)
      {
        if (game.shiftRight())
        {
          repaint();
        }
      }
      else if (code == KeyEvent.VK_LEFT)
      {
        if (game.shiftLeft())
        {
          repaint();
        }
      }
      else if (code == KeyEvent.VK_UP)
      {
        if (game.rotate())
        {
          repaint();
        }
      }
      else if (code == KeyEvent.VK_DOWN)
      {
        // reset timer for the fast drop frame rate
        int speed = level.fastDropSpeed(game.getTotalCompletedRows());
        timer.setDelay(speed);
        timer.setInitialDelay(0);
        timer.restart();
        fastDrop = true;
      }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
      // not used
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
      // not used
    }
    
  }
}
