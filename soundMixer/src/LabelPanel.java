import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class LabelPanel extends JPanel {
	public static final int MAX = 4000;
	private MainGraphFrame parent;
	private int sampleRate;

	public LabelPanel(MainGraphFrame par) {
		parent = par;

		setPreferredSize(new Dimension(1024, 20));
		sampleRate = parent.getSampleRate();

	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		g2.setColor(Color.WHITE);
		for (int i = 0; i <= 10; i++) {
			g2.drawString(i * sampleRate / 10 / 2 + "", (getWidth() / 10) * i, 10);
		}
	}

	public void resetLabels(int newSampleRate) {
		sampleRate = newSampleRate;
	}

	private static final long serialVersionUID = 1L;

}
