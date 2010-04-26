package actions;

public abstract class KeyAction extends AutoAction {
    protected int key;

    public KeyAction(int key) {
        this.key = key;
    }
}
