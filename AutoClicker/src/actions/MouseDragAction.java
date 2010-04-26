package actions;
import java.awt.Point;

public class MouseDragAction extends CompoundAction {
    /**
     * Drags the mouse from start to end
     * @param start
     * @param end
     */
    public MouseDragAction(Point start, Point end) {
        addReLocateAction(start);
        addDragActions(end);
    }

    /**
     * Drags the mouse from its current location to end
     * @param end
     */
    public MouseDragAction(Point end) {
        addDragActions(end);
    }
    
    private final void addReLocateAction(Point start){
        addAction(new MouseMoveAction(start));
    }
    
    private final void addDragActions(Point end){
        addAction(MousePressAction.LEFT_CLICK);
        addAction(new MouseMoveAction(end));
        addAction(MouseReleaseAction.LEFT_CLICK);
    }
}
