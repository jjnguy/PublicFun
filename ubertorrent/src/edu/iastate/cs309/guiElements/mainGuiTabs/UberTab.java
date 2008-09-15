package edu.iastate.cs309.guiElements.mainGuiTabs;

import javax.swing.Icon;
import javax.swing.JPanel;

/**
 * This class makes adding new tabs simpler for a plugin.
 * 
 * @author Justin
 */
public abstract class UberTab extends JPanel
{
	/**
	 * @return the name to be shown by the tab
	 */
	public abstract String getTabName();

	/**
	 * @return the tooltip text of the tab
	 */
	public abstract String getTabToolTipText();

	/**
	 * @return the Icon that represents the tab if icon is null, no icon will be
	 *         shown
	 */
	public abstract Icon getTabIcon();

	/**
	 * @see java.awt.Component#toString()
	 */
	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		return getTabName();
	}

	/**
	 * The name of UberTabs is the text that is displayed
	 * 
	 * @see java.awt.Component#getName()
	 */
	@Override
	public String getName()
	{
		return getTabName();
	}
}
