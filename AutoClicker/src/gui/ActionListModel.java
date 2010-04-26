package gui;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import actions.AutoAction;
import actions.CompoundAction;

public class ActionListModel extends CompoundAction implements ListModel {

    public ActionListModel(){listeners = new ArrayList<ListDataListener>();}
    
    public void addAction(AutoAction a) {
        addAction(a);
    }

    public void removeAction(int idx){
        removeAction(idx);
    }
    
    @Override
    public Object getElementAt(int arg0) {
        return getNthAction(arg0);
    }

    @Override
    public int getSize() {
        return getActionCount();
    }

    private List<ListDataListener> listeners;

    @Override
    public void removeListDataListener(ListDataListener arg0) {
        listeners.remove(arg0);
    }

    @Override
    public void addListDataListener(ListDataListener arg0) {
        listeners.add(arg0);
    }
}
