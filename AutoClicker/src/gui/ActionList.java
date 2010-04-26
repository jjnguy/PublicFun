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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
        setUpActions();
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

    private void setUpActions() {
        addAction.addActionListener(addActionListener);
    }

    public void addAction(AutoAction a) {
        model.addAction(a);
    }

    public void removeAction(int idx) {
        model.removeAction(idx);
    }

    public void setAction(int idx, AutoAction newAction) {
        model.setAction(idx, newAction);
    }

    private ActionListener addActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            AutoAction newAction = CreateActionFrame.showNewActionDialog();
            if (newAction == null)
                return;
            model.addAction(newAction);
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
        private JPanel mainPane;
        private JList listOfActionTypes;
        private JButton createButton;
        private JButton cancelButton;

        private CreateActionPanel currentFrame;

        private Map<String, CreateActionPanel> possibleViews;

        private CreateActionFrame() {
            super();
            setModal(true);
            createComponents();
            createPossibleViews();
            addActions();
            layoutComponents();
            pack();
        }

        public static AutoAction showNewActionDialog() {
            CreateActionFrame f = new CreateActionFrame();
            f.setVisible(true);
            return f.getAction();
        }

        public AutoAction getAction() {
            return possibleViews.get(listOfActionTypes.getSelectedValue()).getAction();
        }

        private void createComponents() {
            listOfActionTypes = new JList();
            createButton = new JButton("Create");
            cancelButton = new JButton("Cancel");
        }

        private void addActions() {
            listOfActionTypes.addListSelectionListener(selectionChange);
            createButton.addActionListener(createAction);
        }

        private void layoutComponents() {
            mainPane = new JPanel(new BorderLayout());
            JPanel south = new JPanel();
            south.add(createButton);
            south.add(cancelButton);
            mainPane.add(south, BorderLayout.SOUTH);
            mainPane.add(listOfActionTypes, BorderLayout.WEST);
            currentFrame = possibleViews.get(listOfActionTypes.getSelectedValue());
            mainPane.add(currentFrame);
            add(mainPane);
        }

        private void createPossibleViews() {
            possibleViews = new HashMap<String, CreateActionPanel>();
            possibleViews.put("Pause", new CreatePauseActionPanel());
            possibleViews.put("Click", new CreateClickActionPanel());
            listOfActionTypes.setListData(possibleViews.keySet().toArray(
                    new String[possibleViews.keySet().size()]));
            listOfActionTypes.setSelectedIndex(0);
        }

        private ActionListener createAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        };

        private ListSelectionListener selectionChange = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                mainPane.remove(currentFrame);
                currentFrame = possibleViews.get(listOfActionTypes.getSelectedValue());
                mainPane.add(currentFrame);
                invalidate();
                repaint();
            }
        };
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new ActionList());
        f.pack();
        f.setVisible(true);
    }
}
