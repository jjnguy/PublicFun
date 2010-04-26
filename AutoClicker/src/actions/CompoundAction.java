package actions;
import java.util.ArrayList;
import java.util.List;

public class CompoundAction extends AutoAction {

    private List<AutoAction> actions;

    public CompoundAction() {
        actions = new ArrayList<AutoAction>();
    }

    public void addAction(AutoAction a) {
        actions.add(a);
    }
    
    public AutoAction removeNthAction(int n){
        return actions.remove(n);
    }
    
    public AutoAction getNthAction(int n){
        return actions.get(n);
    }
    
    public AutoAction setNthAction(int n, AutoAction newAction){
        actions.add(n, newAction);
        return actions.remove(n+1);
    }

    public int getActionCount(){
        return actions.size();
    }
    
    @Override
    public void doAction() {
        for (AutoAction a : actions) {
            a.doAction();
        }
    }

}
