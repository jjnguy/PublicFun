package edu.iastate.cs309.guiElements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import edu.iastate.cs309.torrentManager.containers.Bitfield;
import edu.iastate.cs309.torrentManager.exceptions.MalformedBitfieldException;
import edu.iastate.cs309.util.Util;

public class PieceProgressPane extends JPanel
{
	private static final Color HAVE = Color.BLUE;
	private static final Color DONT_HAVE = Color.WHITE;

	private Bitfield progress;
	private boolean paint;

	public PieceProgressPane(byte[] in, int numPieces)
	{
		updatePieces(in, numPieces);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		setBackground(Color.RED);
		setMaximumSize(new Dimension(10000, 50));
		setMinimumSize(new Dimension(10, 50));
		setPreferredSize(new Dimension(400, 100));
	}

	public final void updatePieces(byte[] in, int numPieces)
	{
		if (in == null || numPieces <= 0)
		{
			if (Util.DEBUG)
				System.out.println("A byte array was null or numPieces was less than zero");
			paint = false;
			progress = null;
		}
		else
		{
			try
			{
				progress = new Bitfield(Arrays.copyOf(in, in.length), numPieces);
			}
			catch (MalformedBitfieldException e)
			{
				if (Util.DEBUG)
					e.printStackTrace();
			}
			paint = true;
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (!paint)
			return;
		Graphics2D g2 = (Graphics2D) g;

		g2.drawImage(makeImage(), 0, 0, getWidth(), getHeight(), Color.RED, changeObserver);
	}

	/**
	 * The image.
	 * 
	 * @return the image (pieces x 1)
	 */
	protected BufferedImage makeImage()
	{
		BufferedImage bi = new BufferedImage(progress.getSize(), 1, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < progress.getSize(); i++)
			bi.setRGB(i, 0, (progress.isSet(i) ? HAVE : DONT_HAVE).getRGB());

		return bi;
	}

	private ImageObserver changeObserver = new ImageObserver()
	{
		@Override
		public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height)
		{
			repaint();
			return true;
		}
	};
}
