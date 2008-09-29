import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class GraphPanel extends JPanel {

	private Color bGroundColor;
	private MainGraphFrame parentComponent;

	public GraphPanel(MainGraphFrame parent) {
		super();
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		parentComponent = parent;
		setPreferredSize(new Dimension(1024, 250));
		bGroundColor = Color.BLACK;
		setToolTipText("No mouse movement yet");
		repaint();
	}

	@Override
	public String getToolTipText(MouseEvent e) {
		/*
		 * if (!parentComponent.noFile) { int sampleNumToGet =
		 * parentComponent.buttons.slide.getValue() - 125 + (e.getPoint().x /
		 * 4); String ret = "Sample Number: " + sampleNumToGet + " "; ret +=
		 * "Value: " + parentComponent.listOfSampleValues.get(sampleNumToGet);
		 * return ret; } else {
		 * 
		 * return "No File Selected"; }
		 */return "";

	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (!parentComponent.noFile) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(bGroundColor);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());
			int curMid = parentComponent.buttons.slide.getValue();
			if (curMid < 125) {
				curMid = 125;
			} else if (curMid > parentComponent.listOfSampleValues.size() - 125) {
				curMid = parentComponent.listOfSampleValues.size() - 125;
			}
			int start = curMid - 125;
			g2.setColor(Color.BLACK);
			Point point1, point2;
			//point1 = new Point(0, parentComponent.listOfSampleValues.get(start));
			point2 = new Point(4, parentComponent.listOfSampleValues.get(start));
			
			paintRule(g2);
			for (int i = 0; i < 250; i++) {
				point1 = point2;
				point2 = new Point(i * 4, parentComponent.listOfSampleValues.get(i + start));
				g2.setStroke(new BasicStroke(2));
				g2.setColor(Color.RED);
				g2.drawLine(point1.x, this.getHeight() - point1.y, point2.x, this.getHeight() - point2.y);
			}

		} else {

			g.drawString("No File Selected", 200, 300);
		}
	}

	private void paintRule(Graphics2D g2) {
		Color originalColor = g2.getColor();
		Stroke originalStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(1));

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setColor(Color.WHITE);

		g2.drawLine(10, 0, 10, this.getHeight());
		g2.drawLine(10, this.getHeight() / 2, this.getWidth(), this.getHeight() / 2);

		for (int i = 1; i < 10; i++) {
			g2.drawLine(10, i * this.getHeight() / 10, 20, i * this.getHeight() / 10);
		}

		for (int i = 0; i <= 10; i++) {
			if (i != 5) g2.drawString((int) (i * 25.6) + "", 22, this.getHeight() - i * this.getHeight() / 10 + 4);
		}

		g2.setColor(originalColor);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(originalStroke);
	}

	public void setBground(Color color) {
		bGroundColor = color;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
