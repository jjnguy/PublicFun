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
public class HBox extends Box
{

	public HBox(Alignment align)
	{
		super(null, align, false);
	}

	public HBox(Alignment align, boolean containerGaps)
	{
		super(null, align, containerGaps);
	}

	public HBox(String title, Alignment align)
	{
		super(title, align, false);
	}

	@Override
	protected Group getHGroup(GroupLayout layout, Alignment align)
	{
		return layout.createSequentialGroup();
	}

	@Override
	protected Group getVGroup(GroupLayout layout, Alignment align)
	{
		return layout.createParallelGroup(align);
	}
}
