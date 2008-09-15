/**
 * 
 */
package edu.iastate.cs309.guiElements.lowerlevel;

import java.awt.Dimension;

import javax.swing.JComponent;

/**
 * @author Michael Seibert
 */
@SuppressWarnings("serial")
public class VStretcher extends JComponent
{
	/**
	 * @see javax.swing.JComponent#getMinimumSize()
	 */
	@Override
	public Dimension getMinimumSize()
	{
		return new Dimension(0, 0);
	}

	/**
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(0, 0);
	}

	/**
	 * @see javax.swing.JComponent#getMaximumSize()
	 */
	@Override
	public Dimension getMaximumSize()
	{
		return new Dimension(0, Integer.MAX_VALUE);
	}
}
