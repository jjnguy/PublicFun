package actions;
import java.awt.Point;

public class MouseMoveAction extends AutoAction {
    private Point coord;

    public MouseMoveAction(Point coord) {
        this.coord = coord;
    }

    @Override
    public void doAction() {
        rob.mouseMove(coord.x, coord.y);
    }
}
