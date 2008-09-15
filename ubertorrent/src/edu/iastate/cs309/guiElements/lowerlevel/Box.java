/**
 * 
 */
package edu.iastate.cs309.guiElements.lowerlevel;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.Group;

/**
 * @author Michael Seibert
 */
public abstract class Box extends JPanel
{
	private GroupLayout layout;
	private Group hGroup;
	private Group vGroup;

	/**
	 * @param title
	 *            the title of the Box
	 * @param align
	 *            the alignment of the box
	 * @see Alignment
	 * @param containerGaps
	 *            weather or not to have gaps separating the components
	 */
	public Box(String title, Alignment align, boolean containerGaps)
	{
		layout = new GroupLayout(this);
		setLayout(layout);
		hGroup = getHGroup(layout, align);
		layout.setHorizontalGroup(hGroup);
		vGroup = getVGroup(layout, align);
		layout.setVerticalGroup(vGroup);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(containerGaps);
		if (title != null)
			setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(title), BorderFactory.createEmptyBorder(0, 5, 5, 5)));
	}

	/**
	 * @param layout2
	 * @param align
	 * @return the Group given the input parameters
	 */
	protected abstract Group getHGroup(GroupLayout layout2, Alignment align);

	/**
	 * @param layout2
	 * @param align
	 * @return the Group given the input parameters
	 */
	protected abstract Group getVGroup(GroupLayout layout2, Alignment align);

	/**
	 * @param comp
	 * @return the Box after a component has been added
	 */
	public Box addComp(Component comp)
	{
		hGroup.addComponent(comp);
		vGroup.addComponent(comp);
		return this;
	}

	/**
	 * @param comp1
	 * @param comp2
	 * @return returns the result of linking the two components together
	 */
	public Box link(Component comp1, Component comp2)
	{
		layout.linkSize(comp1, comp2);
		return this;
	}
}
