import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JToggleButton;

public class LifeFrame extends JFrame {
    private LifeDisplay board;
    private boolean go;
    private Thread runner;
    private long msPerFrame = 75;

    public LifeFrame(LifeDisplay board) {
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.board = board;
        add(board, BorderLayout.CENTER);
        final JToggleButton tgl = new JToggleButton("Start/Stop");
        tgl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                go = !go;
                LifeFrame.this.board.setGrids(!go);
                repaint();
            }
        });
        add(tgl, BorderLayout.SOUTH);
        pack();
        setVisible(true);
        runner = new Thread() {
            @Override
            public void run() {
                while (true) {
                    long startTime = System.currentTimeMillis();
                    if (go)
                        LifeFrame.this.board.update();
                    long endTime = System.currentTimeMillis();
                    long elapsed = endTime - startTime;
                    long remain = msPerFrame - elapsed;
                    long timeTaken = Math.max(msPerFrame, elapsed);
                    if (remain > 0)
                        try {
                            Thread.sleep(remain);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }
        };
        runner.start();
    }

    public void pause() {
        go = false;
    }

    public void go() {
        go = true;
    }
}
