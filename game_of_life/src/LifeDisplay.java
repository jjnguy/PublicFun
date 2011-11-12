import javax.swing.JPanel;


public abstract class LifeDisplay extends JPanel {
    public abstract void update();
    public abstract void setGrids(boolean on);
}
