package gui;

import javax.swing.JPanel;

import logic.LifeBoard;

public abstract class LifeDisplay extends JPanel {
    protected LifeBoard board;
    private int squareWidth = 12;

    public abstract void update();

    public abstract void setGrids(boolean on);

    public LifeBoard getLogic() {
        return board;
    }

    public int sqWidth() {
        return squareWidth;
    }
    
    public void sqWidth(int width) {
        squareWidth = width;
    }
}
