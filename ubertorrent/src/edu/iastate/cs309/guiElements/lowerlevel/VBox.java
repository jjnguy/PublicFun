/**
 * 
 */
package edu.iastate.cs309.guiElements.lowerlevel;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.Group;

/**
 * @author Michael Seibert
 */
@SuppressWarnings("serial")
public class VBox extends Box
{
	/**
	 * 
	 */
	public VBox(Alignment align)
	{
		super(null, align, false);
	}

	/**
	 * 
	 */
	public VBox(Alignment align, boolean containerGaps)
	{
		super(null, align, containerGaps);
	}

	public VBox(String title, Alignment align, boolean containerGaps)
	{
		super(title, align, containerGaps);
	}

	public VBox(String title, Alignment align)
	{
		super(title, align, false);
	}

	/**
	 * @see edu.iastate.cs309.guiElements.lowerlevel.Box#getHGroup(javax.swing.GroupLayout)
	 */
	@Override
	protected Group getHGroup(GroupLayout layout, Alignment align)
	{
		return layout.createParallelGroup(align);
	}

	/**
	 * @see edu.iastate.cs309.guiElements.lowerlevel.Box#getVGroup(javax.swing.GroupLayout)
	 */
	@Override
	protected Group getVGroup(GroupLayout layout, Alignment align)
	{
		return layout.createSequentialGroup();
	}
}
