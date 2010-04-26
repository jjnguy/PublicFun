package actions;
public class KeyTypeAction extends CompoundAction {

    public KeyTypeAction(int key) {
        addAction(new KeyPressAction(key));
        addAction(new PauseAction(100));
        addAction(new KeyReleaseAction(key));
    }
}
