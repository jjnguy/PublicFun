package actions;
import java.awt.event.InputEvent;

public class MouseReleaseAction extends AutoAction {
    private int buttonType;

    private MouseReleaseAction(int buttonType) {
        this.buttonType = buttonType;
    }

    public static final AutoAction LEFT_CLICK = new MouseReleaseAction(InputEvent.BUTTON1_MASK);
    public static final AutoAction RIGHT_CLICK = new MouseReleaseAction(InputEvent.BUTTON2_MASK);
    public static final AutoAction MIDDLE_CLICK = new MouseReleaseAction(InputEvent.BUTTON3_MASK);

    @Override
    public void doAction() {
        rob.mouseRelease(buttonType);
    }
}
