import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GravityFrame extends JFrame {

	private GravityPane gravPane;

	public GravityFrame() {
		super();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		gravPane = new GravityPane();
		JButton advanceButton = new JButton("Start");
		advanceButton.addActionListener(advanceAction);
		JPanel mainPane = new JPanel();
		mainPane.add(gravPane);
		mainPane.add(advanceButton);
		add(mainPane);
		setVisible(true);
		pack();
		int hiehgt = gravPane.getHeight();
		gravPane.addObject(new GravitySphere(200, hiehgt - 30));
		gravPane.addObject(new GravitySphere(100, 0, 120, 10));
		gravPane.addObject(new GravitySphere(300, hiehgt + 1000));
		// gravPane.run();
	}

	private ActionListener advanceAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			long period = 20;
			Timer t = new Timer();
			t.schedule(new TimerTask() {
				@Override
				public void run() {
					gravPane.advanceFrame();
				}
			}, 0, period);
		}
	};

	public static void main(String[] args) {
		new GravityFrame();
	}

}
