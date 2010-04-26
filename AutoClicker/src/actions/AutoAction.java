package actions;
import java.awt.AWTException;
import java.awt.Robot;

public abstract class AutoAction {
    protected Robot rob;

    public AutoAction() {

        try {
            rob = new Robot();
        } catch (AWTException e) {
            try {
                rob = new Robot();
            } catch (AWTException e1) {
                try {
                    rob = new Robot();
                } catch (AWTException e2) {
                    // if it hasn't worked after 3 tries...oops
                }
            }
        }
    }

    public abstract void doAction();
}
