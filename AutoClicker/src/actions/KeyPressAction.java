package actions;
public class KeyPressAction extends KeyAction {

    public KeyPressAction(int key) {
        super(key);
    }

    @Override
    public void doAction() {
        rob.keyPress(key);
    }

}
