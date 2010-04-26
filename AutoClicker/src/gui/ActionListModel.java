package gui;

import javax.swing.DefaultListModel;

import actions.AutoAction;
import actions.CompoundAction;

public class ActionListModel extends DefaultListModel {
    private CompoundAction actions;

    public ActionListModel() {
        actions = new CompoundAction();
    }

    public void addAction(AutoAction a) {
        actions.addAction(a);
        fireContentsChanged(this, 0, actions.getActionCount());
    }

    public void removeAction(int idx) {
        actions.removeNthAction(idx);
        fireContentsChanged(this, 0, actions.getActionCount());
    }

    public void setAction(int idx, AutoAction action) {
        actions.setNthAction(idx, action);
        fireContentsChanged(this, 0, actions.getActionCount());
    }

    public Object getElementAt(int arg0) {
        return actions.getNthAction(arg0);
    }

    public int getSize() {
        return actions.getActionCount();
    }

}
