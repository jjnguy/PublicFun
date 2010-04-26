package gui;

import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JTextField;

import actions.AutoAction;
import actions.MouseClickAction;

@SuppressWarnings("serial")
public class CreateClickActionPanel extends CreateActionPanel {

    private JLabel coordLabel;
    private JTextField xField, yField;

    public CreateClickActionPanel() {
        createComponents();
        layoutComponents();
    }

    private void layoutComponents() {
        add(coordLabel);
        add(xField);
        add(yField);
    }

    private void createComponents() {
        coordLabel = new JLabel("Coordinates to click: Leave blank for current position:");
        xField = new JTextField(4);
        yField = new JTextField(4);
    }

    @Override
    public AutoAction getAction() {
        if (!validateAction())
            return null;
        if (xField.getText().trim().length() == 0 || yField.getText().trim().length() == 0) {
            return new MouseClickAction();
        }
        return new MouseClickAction(new Point(Integer.parseInt(xField.getText()), Integer
                .parseInt(yField.getText())));
    }

    @Override
    public boolean isValidAction() {
        if (xField.getText().trim().length() == 0 || yField.getText().trim().length() == 0) {
            return true;
        }
        try {
            Integer.parseInt(xField.getText());
            Integer.parseInt(yField.getText());
        }catch (Exception e){
            return false;
        }
        return true;
    }

}
