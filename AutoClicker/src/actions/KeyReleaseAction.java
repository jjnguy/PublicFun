package actions;

public class KeyReleaseAction extends KeyAction {

    public KeyReleaseAction(int key) {
        super(key);
    }

    @Override
    public void doAction() {
        rob.keyPress(key);
    }

}
