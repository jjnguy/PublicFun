package edu.iastate.cs309.guiElements.mainGuiTabs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import edu.iastate.cs309.guiElements.guiUTil.GUIUtil;

/**
 * Contains a graph of how fast downloads and uploads are going
 * 
 * @author Justin
 * 
 */
@SuppressWarnings("serial")
public class SpeedTab extends UberTab
{

	@SuppressWarnings("unused")
	private List<Double> listOfSpeeds;

	private Color bGroundColor;

	/**
	 * 
	 */
	public SpeedTab()
	{
		super();
		bGroundColor = Color.BLACK;

	}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(bGroundColor);
		g2.fillRect(0, 0, getWidth(), getHeight());

		g2.setColor(Color.WHITE);
		@SuppressWarnings("unused")
		Point point1, point2;
		// System.out.print(curMid + "\n");
		for (int i = 0; i < 256 / 2; i++)
		{
			g2.setStroke(new BasicStroke(2));
			g2.setColor(new Color(100, 0, 0));

			g2.setColor(Color.YELLOW);
			// g2.setStroke(new BasicStroke(2));

		}

	}

	@SuppressWarnings("unused")
	private void paintRule(Graphics2D g2, int max)
	{
		Color originalColor = g2.getColor();
		Stroke originalStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(1));

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setColor(Color.DARK_GRAY);

		for (int i = 1; i < 8; i++)
		{
			g2.drawLine(0, i * 32, getWidth(), i * 32);
		}

		g2.setColor(Color.WHITE);
		g2.drawLine(10, 0, 10, getHeight());
		g2.drawLine(10, 4, 20, 4);
		g2.drawString(max + "", 22, 8);

		g2.setColor(originalColor);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(originalStroke);
	}

	/**
	 * @param color
	 */
	public void setBground(Color color)
	{
		bGroundColor = color;
	}

	/**
	 * @see edu.iastate.cs309.guiElements.mainGuiTabs.UberTab#getTabName()
	 */
	@Override
	public String getTabName()
	{
		return "Speed";
	}

	/**
	 * @see edu.iastate.cs309.guiElements.mainGuiTabs.UberTab#getTabToolTipText()
	 */
	@Override
	public String getTabToolTipText()
	{
		return "Shows network speed";
	}

	/**
	 * @see edu.iastate.cs309.guiElements.mainGuiTabs.UberTab#getTabIcon()
	 */
	@Override
	public Icon getTabIcon()
	{
		return new ImageIcon(GUIUtil.tabImageFOlder + "/" + "SpeedLogo.png");
	}
}
