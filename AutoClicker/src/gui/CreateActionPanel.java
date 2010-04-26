package gui;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import actions.AutoAction;

public abstract class CreateActionPanel extends JPanel {

    public abstract AutoAction getAction();

    public abstract boolean isValidAction();

    public boolean validateAction() {
        return validateAction("Oops, this form was not valid");
    }

    public boolean validateAction(String mesage) {
        if (isValidAction())
            return true;
        JOptionPane.showMessageDialog(this, mesage, "Invalid Action", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
