import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainFrame extends JFrame {

	private JSlider delaySelector;
	private JButton startButton;
	private JLabel timeLable;

	public MainFrame() {
		super("Delayed Shutdown");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		startButton = new JButton("Start Timer");
		startButton.addActionListener(start);

		delaySelector = new JSlider(0, 240);
		delaySelector.setMajorTickSpacing(60);
		delaySelector.setMinorTickSpacing(10);
		// delaySelector.setSnapToTicks(true);
		delaySelector.setPaintLabels(true);
		delaySelector.setPaintTicks(true);
		delaySelector.addChangeListener(valueChanged);

		timeLable = new JLabel(delaySelector.getValue() + "");

		JPanel mainPane = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		Insets i = new Insets(3, 3, 3, 3);
		mainPane.add(new JLabel("Shutdown in:"));
		gc.gridy = 1;
		mainPane.add(timeLable, gc);
		gc.insets = i;
		gc.gridy = 2;
		gc.weightx = gc.weighty = 1;
		mainPane.add(startButton, gc);
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = gc.weighty = 0;
		gc.gridy = 3;
		mainPane.add(delaySelector, gc);

		addComponentListener(compListener);
		add(mainPane);

		pack();
		setVisible(true);
	}

	private ChangeListener valueChanged = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			timeLable.setText(delaySelector.getValue() + "");
		}
	};
	private ComponentListener compListener = new ComponentAdapter() {

		@Override
		public void componentResized(ComponentEvent e) {
			int area = getWidth() * getHeight();
			timeLable.setFont(new BiggerFont(2 * (int) Math.pow((double) area,
					1 / 3.0)));
			pack();
		}
	};
	private ActionListener start = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			timeLable.setForeground(Color.RED);
			WaitAndGoThread th = new WaitAndGoThread(delaySelector.getValue(),
					timeLable);
			th.start();
			startButton.setEnabled(false);
			delaySelector.setEnabled(false);
		}
	};

	public static void main(String[] args) {
		new MainFrame();
	}
}

class WaitAndGoThread extends Thread {
	int min;
	JLabel timeLeftDisp;

	public WaitAndGoThread(int mins_p, JLabel timeLeftDisplay) {
		min = mins_p;
		timeLeftDisp = timeLeftDisplay;
	}

	@Override
	public void run() {
		super.run();

		int i = 0;
		while (i < min) {
			try {
				Thread.sleep(1000 * 60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			timeLeftDisp.setText((min - i - 1) + "");
			i++;
		}

		Process p = null;
		try {
			p = Runtime.getRuntime().exec("shutdown -s -t 0");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.exit(0);
	}
}

class BiggerFont extends Font {

	public BiggerFont(int size) {
		super("Ariel", Font.BOLD, size);
	}
}