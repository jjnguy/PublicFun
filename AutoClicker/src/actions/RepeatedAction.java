package actions;
public class RepeatedAction extends AutoAction {

    private AutoAction action;
    private PauseAction pause;
    private int repeatCount;
    
    public RepeatedAction(AutoAction action, int repeatCount, long delay) {
        this.action = action;
        this.repeatCount = repeatCount;
        this.pause = new PauseAction(delay);
    }
    
    public RepeatedAction(AutoAction action, int repeatCount){
        this(action, repeatCount, 100);
    }

    @Override
    public void doAction() {
        for(int i = 0; i < repeatCount; i++){
            action.doAction();
            pause.doAction();
        }
    }

}
