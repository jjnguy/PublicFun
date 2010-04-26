package gui;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class ControlFrame extends JFrame {
    private JSlider clickCountSelector;
    private JTextField clickCountArea;
    private JButton startButton;
    private JSlider delayAmntSelector;
    private JTextField delayAmntArea;
    private JButton chooseCoordsButton;

    private Point selection;

    public ControlFrame() {
        super("Auto Clicker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createComponents();
        addListeners();
        layoutComponents();
        pack();
        setVisible(true);
    }

    private final void createComponents() {
        clickCountSelector = new JSlider(0, 1000);
        clickCountSelector.setSnapToTicks(true);
        clickCountSelector.setMajorTickSpacing(100);
        clickCountSelector.setMinorTickSpacing(50);
        clickCountSelector.setPaintTicks(true);
        clickCountArea = new JTextField("" + clickCountSelector.getValue());

        delayAmntSelector = new JSlider(0, 1000);
        delayAmntSelector.setSnapToTicks(true);
        delayAmntSelector.setMajorTickSpacing(100);
        delayAmntSelector.setMinorTickSpacing(50);
        delayAmntSelector.setPaintTicks(true);
        delayAmntArea = new JTextField("" + delayAmntSelector.getValue());

        chooseCoordsButton = new JButton("Select");

        startButton = new JButton("Start");
    }

    private final void addListeners() {
        clickCountSelector.addChangeListener(clickCountChangeListener);
        delayAmntSelector.addChangeListener(delayCountChangeListener);

        chooseCoordsButton.addActionListener(chooseCoordListener);

        startButton.addActionListener(startAction);
    }

    private final void layoutComponents() {
        JPanel amntPanel = new JPanel();
        amntPanel.add(clickCountSelector);
        amntPanel.add(clickCountArea);

        JPanel delayPanel = new JPanel();
        delayPanel.add(delayAmntSelector);
        delayPanel.add(delayAmntArea);

        JPanel startAndCoordPanel = new JPanel();
        startAndCoordPanel.add(chooseCoordsButton);
        startAndCoordPanel.add(startButton);

        JPanel mainPane = new JPanel(new GridLayout(3, 1));
        mainPane.add(amntPanel);
        mainPane.add(delayPanel);
        mainPane.add(startAndCoordPanel);

        this.add(mainPane);
    }

    private ActionListener chooseCoordListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            CaptureFrame cap = new CaptureFrame();
        }
    };

    private ActionListener startAction = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            setVisible(false);
            Robot rob = null;
            try {
                rob = new Robot();
            } catch (AWTException e1) {
                e1.printStackTrace();
                return;
            }
            rob.mouseMove(selection.x, selection.y);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
            rob.mousePress(InputEvent.BUTTON1_MASK);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
            rob.keyPress(KeyEvent.VK_K);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }

            rob.mouseMove(selection.x, selection.y);
            for (int i = 0; i < clickCountSelector.getValue()*10; i++) {
                rob.mousePress(InputEvent.BUTTON1_MASK);
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                rob.mouseRelease(InputEvent.BUTTON1_MASK);
                //rob.mouseMove(selection.x, selection.y);
            }
        }
    };

    private ChangeListener clickCountChangeListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            clickCountArea.setText("" + ((JSlider) e.getSource()).getValue());
        }

    };
    private ChangeListener delayCountChangeListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            delayAmntArea.setText("" + ((JSlider) e.getSource()).getValue());
        }
    };

    class CaptureFrame extends JDialog {
        public CaptureFrame() {
            super();
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            setSize(new Dimension(50, 50));
            addMouseMotionListener(mml);
            addMouseListener(ml);
            setAlwaysOnTop(true);
            setMinimumSize(new Dimension(0, 0));
            setUndecorated(true);
            setVisible(true);
        }

        @Override
        public void paintComponents(Graphics g) {
            // Blank on purpose
        }

        private MouseListener ml = new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
                setBounds(e.getXOnScreen() - 10, e.getYOnScreen() - 10, 20, 20);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
                setBounds(e.getXOnScreen() - 10, e.getYOnScreen() - 10, 20, 20);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
                selection = new Point(e.getXOnScreen(), e.getYOnScreen());
            }
        };
        private MouseMotionListener mml = new MouseMotionListener() {

            @Override
            public void mouseMoved(MouseEvent e) {
                // TODO Auto-generated method stub
                setBounds(e.getXOnScreen() - 10, e.getYOnScreen() - 10, 20, 20);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO Auto-generated method stub

            }
        };

    }

    public static void main(String[] args) {
        new ControlFrame();
    }
}
