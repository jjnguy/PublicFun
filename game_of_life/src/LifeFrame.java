import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.Timer;

public class LifeFrame extends JFrame {
    private LifeDisplay board;
    private boolean go;

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
            }
        });
        add(tgl, BorderLayout.SOUTH);
        pack();
        setVisible(true);
        new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (go)
                    LifeFrame.this.board.update();
            }
        }).start();
    }

    public void pause() {
        go = false;
    }

    public void go() {
        go = true;
    }

}
