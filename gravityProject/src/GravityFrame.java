import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GravityFrame extends JFrame {

	private GravityPane gravPane;
	private JButton advanceButton;
	private boolean running;
	private Timer t;

	public GravityFrame() throws FileNotFoundException {
		super();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		gravPane = new GravityPane();
		advanceButton = new JButton("Start");
		advanceButton.addActionListener(advanceAction);
		JPanel mainPane = new JPanel();
		mainPane.add(gravPane);
		mainPane.add(advanceButton);
		add(mainPane);
		setVisible(true);
		pack();
		int hiehgt = gravPane.getHeight();
		gravPane.addObject(new GravitySphere(200, hiehgt - 30));
		gravPane.addObject(new GravitySphere(80, 0, 120, 0, .9, 100,
				Color.GREEN));
		gravPane
				.addObject(new GravitySphere(90, 0, 120, 0, .5, 60, Color.BLUE));
		gravPane.addObject(new GravitySphere(100, 0, 120, 0));
		gravPane.addObject(new GravitySphere(300, hiehgt + 1000));
		running = false;
		t = new Timer();
	}

	private ActionListener advanceAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!running) {
				advanceButton.setText("Stop");
				long period = 1;
				t.schedule(new TimerTask() {
					@Override
					public void run() {
						gravPane.advanceFrame();
					}
				}, 0, period);
				running = true;
			} else {
				advanceButton.setText("Start");
				t.cancel();
				t.purge();
				t = new Timer();
				running = false;
			}
		}
	};

	public static void main(String[] args) throws FileNotFoundException {
		new GravityFrame();
	}
}
