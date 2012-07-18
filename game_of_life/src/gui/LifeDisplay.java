package gui;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;

import logic.LifeBoard;

/**
 * This class is in charge of dsiplaying a LifeBoard on a Swing component.
 * 
 * @author Justin
 * 
 */
public abstract class LifeDisplay extends JPanel {
    private static final long serialVersionUID = -3750659407276582573L;
    protected LifeBoard board;
    private int squareWidth = 12;

    public abstract void update();
    public abstract void update(boolean repaint);

    public abstract void setGrids(boolean on);

    public abstract void applyBoard(LifeBoard newB);

    public LifeBoard getLogic() {
        return board;
    }

    public int sqWidth() {
        return squareWidth;
    }

    public void sqWidth(int width) {
        squareWidth = width;
    }
    
    public abstract int liveCells();
    
    public abstract Rectangle maxDimension();
}
