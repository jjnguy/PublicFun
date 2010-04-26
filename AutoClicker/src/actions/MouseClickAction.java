package actions;
import java.awt.Point;

public class MouseClickAction extends CompoundAction {

    /**
     * Clicks the mouse at a specified location
     * @param loc
     */
    public MouseClickAction(Point loc) {
        addMoveAction(loc);
        addClickActions();
    }

    /**
     * Clicks the mouse at its current location
     */
    public MouseClickAction() {
        addClickActions();
    }

    private final void addMoveAction(Point coord) {
        addAction(new MouseMoveAction(coord));
    }

    private final void addClickActions() {
        addAction(MousePressAction.LEFT_CLICK);
        addAction(new PauseAction(100));
        addAction(MouseReleaseAction.LEFT_CLICK);
    }
}
