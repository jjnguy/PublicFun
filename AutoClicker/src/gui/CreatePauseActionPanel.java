package gui;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import actions.AutoAction;
import actions.PauseAction;

public class CreatePauseActionPanel extends CreateActionPanel {
    private JLabel pauseLabel;
    private JTextArea pauseAmnt;

    public CreatePauseActionPanel() {
        super();
    }

    @Override
    public AutoAction getAction() {
        if (!validateAction())
            return null;
        setVisible(false);
        return new PauseAction(Long.parseLong(pauseAmnt.getText()));
    }

    @Override
    public boolean isValidAction() {
        long pauseTime;
        try {
            pauseTime = Long.parseLong(pauseAmnt.getText());
        } catch (Exception e) {
            return false;
        }
        if (pauseTime < 0)
            return false;
        // one hour
        if (pauseTime > 1000 * 60 * 60)
            return false;
        return true;
    }

}
