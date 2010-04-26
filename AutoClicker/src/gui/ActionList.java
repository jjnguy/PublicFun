package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

import actions.AutoAction;

@SuppressWarnings("serial")
public class ActionList extends JPanel {

    private JList listOfActions;
    private JButton addAction;
    private JButton removeAction;
    private JButton editAction;
    private ActionListModel model;

    public ActionList() {
        super(new BorderLayout());
        createComponents();
        layoutComponents();
    }

    private void layoutComponents() {
        JPanel bottom = new JPanel();
        bottom.add(addAction);
        bottom.add(editAction);
        bottom.add(removeAction);
        add(listOfActions);
        add(bottom, BorderLayout.SOUTH);
    }

    private void createComponents() {
        model = new ActionListModel();
        listOfActions = new JList(model);
        listOfActions.setMinimumSize(new Dimension(100, 100));
        listOfActions.setPreferredSize(new Dimension(100, 100));
        addAction = new JButton("Add");
        removeAction = new JButton("Remove");
        editAction = new JButton("Edit");
    }

    public void addAction(AutoAction a) {
        model.addAction(a);
    }

    public void removeAction(int idx) {
        model.removeAction(idx);
    }

    public void setAction(int idx, AutoAction newAction) {
        model.setNthAction(idx, newAction);
    }

    private ActionListener addActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            CreateActionFrame f = new CreateActionFrame();
            f.setVisible(true);
        }
    };
    private ActionListener editActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };
    private ActionListener removeActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };

    public static class CreateActionFrame extends JDialog {
        private JList listOfActionTypes;
        private JButton createButton;
        private JButton cancelButton;

        private Map<String, CreateActionPanel> possibleViews;

        public CreateActionFrame() {
            super();
            createPossibleViews();
            pack();
            setVisible(true);
        }

        private void createPossibleViews() {
            possibleViews = new HashMap<String, CreateActionPanel>();
            possibleViews.put("Pause", new CreatePauseActionPanel());
            listOfActionTypes.setListData(new String[] { "Pause" });
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new ActionList());
        f.pack();
        f.setVisible(true);
    }
}
