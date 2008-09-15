package edu.iastate.cs309.guiElements.mainGuiTabs;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTabbedPane;

/**
 * A JTabbedPane with extra getters and setters for MainGui to work with.
 * 
 * @author Justin
 */
@SuppressWarnings("serial")
public class UberTabbedPane extends JTabbedPane
{
	private List<UberTab> tabs;

	public UberTabbedPane()
	{
		tabs = new ArrayList<UberTab>();
	}

	/**
	 * @see javax.swing.JTabbedPane#addTab(java.lang.String, java.awt.Component)
	 * 
	 * @deprecated please use addTab(UberTab comp)
	 */
	@Override
	@Deprecated
	public void addTab(String title, Component component)
	{
		super.addTab(title, component);
	}

	/**
	 * @see javax.swing.JTabbedPane#addTab(java.lang.String, javax.swing.Icon,
	 *      java.awt.Component)
	 * 
	 * @deprecated please use addTab(UberTab comp)
	 */
	@Override
	@Deprecated
	public void addTab(String title, Icon icon, Component component)
	{
		super.addTab(title, icon, component);
	}

	/**
	 * @see javax.swing.JTabbedPane#addTab(java.lang.String, javax.swing.Icon,
	 *      java.awt.Component, java.lang.String)
	 * 
	 * @deprecated please use addTab(UberTab comp)
	 */
	@Override
	@Deprecated
	public void addTab(String title, Icon icon, Component component, String tip)
	{
		super.addTab(title, icon, component, tip);
	}

	/**
	 * Adds a new UberTab to this tab pane
	 * 
	 * @param comp
	 *            the tab to add
	 */
	public void addTab(UberTab comp)
	{
		super.addTab(comp.getTabName(), comp.getTabIcon(), comp, comp.getTabToolTipText());
		tabs.add(comp);
	}

	/**
	 * @param string
	 *            the tab's displayed text/name to remove
	 * 
	 * Tab collision...fail hard (kinda)
	 * @return whether or not a tab was removed
	 */
	public boolean removeTab(String string)
	{
		boolean removed = false;
		for (int i = 0; i < tabs.size(); i++)
		{
			//Not needed surely
			//			if (Util.DEBUG)
			//			{
			//				System.out.println(tabs.get(i));
			//			}
			if ((tabs.get(i)).getTabName().equals(string))
			{
				tabs.remove(i);
				removeTabAt(i);
				i--;
				removed = true;
			}
		}
		return removed;
	}
}
