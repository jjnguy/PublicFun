package actions;
public class PauseAction extends AutoAction {
    private long pause;

    public PauseAction(long waitTime) {
        pause = waitTime;
    }

    @Override
    public void doAction() {
        try {
            Thread.sleep(pause);
        } catch (InterruptedException e) {
        }
    }
}
