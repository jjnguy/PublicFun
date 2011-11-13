package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LifeFrame extends JFrame {
    private static final long serialVersionUID = -83611558260904001L;
    private LifeDisplay board;
    private boolean go;
    private Thread runner;
    private long msPerFrame = 75;

    public LifeFrame(LifeDisplay board) {
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.board = board;
        add(board, BorderLayout.CENTER);
        JPanel controls = buildControlPanel();
        add(controls, BorderLayout.SOUTH);
        pack();
        setVisible(true);
        runner = new Thread() {
            @Override
            public void run() {
                while (true) {
                    long startTime = System.currentTimeMillis();
                    if (go) {
                        try {
                            SwingUtilities.invokeAndWait(new Runnable() {
                                @Override
                                public void run() {
                                    LifeFrame.this.board.update();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    long endTime = System.currentTimeMillis();
                    long elapsed = endTime - startTime;
                    long remain = msPerFrame - elapsed;
                    // long timeTaken = Math.max(msPerFrame, elapsed);
                    if (remain > 0) {
                        try {
                            Thread.sleep(remain);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        runner.start();
    }

    private final JPanel buildControlPanel() {
        final JToggleButton tgl = new JToggleButton("Start/Stop");
        tgl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                go = !go;
                LifeFrame.this.board.setGrids(!go);
                repaint();
            }
        });
        final JSlider squareSize = new JSlider(3, 20, 10);
        squareSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                board.sqWidth(squareSize.getValue());
                board.repaint();
            }
        });
        JPanel control = new JPanel();
        control.add(tgl);
        control.add(squareSize);
        return control;
    }

    public void pause() {
        go = false;
    }

    public void go() {
        go = true;
    }
}
