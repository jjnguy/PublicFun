package actions;
import java.awt.event.InputEvent;

public class MousePressAction extends AutoAction {
    private int buttonType;

    private MousePressAction(int buttonType) {
        this.buttonType = buttonType;
    }

    public static AutoAction LEFT_CLICK = new MousePressAction(InputEvent.BUTTON1_MASK);
    public static AutoAction RIGHT_CLICK = new MousePressAction(InputEvent.BUTTON2_MASK);
    public static AutoAction MIDDLE_CLICK = new MousePressAction(InputEvent.BUTTON3_MASK);

    @Override
    public void doAction() {
        rob.mousePress(buttonType);
    }
}
