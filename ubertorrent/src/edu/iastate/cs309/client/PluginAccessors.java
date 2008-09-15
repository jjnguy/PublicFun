package edu.iastate.cs309.client;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;

import edu.iastate.cs309.comminterfaces.IServer;
import edu.iastate.cs309.guiElements.MainGui;
import edu.iastate.cs309.guiElements.mainGuiTabs.UberTab;
import edu.iastate.cs309.guiElements.mainGuiTabs.UberTabbedPane;

//TODO kyle : make the Tab accessors like the Menu accessors

/**
 * This class helps plugins get parts of a MainGui or Server. The plugins can
 * then modify parts of the GUI interface and Server settings
 * 
 */
public class PluginAccessors
{
	/** The user interface to communicate with */
	private static MainGui mainGui;

	/** The server that this Client is to communicate with */
	private static IServer server;

	/**
	 * Disallows instantiation
	 */
	private PluginAccessors()
	{
	}

	/**
	 * Returns whether or not this static class has an instance of a Server
	 * 
	 * @return true if connected, false otherwise
	 */
	public static boolean serverIsProvided()
	{
		return server != null;
	}

	/**
	 * @return the Floatable JToolBar in Ubertorrent
	 */
	public static JToolBar getToolBar()
	{
		return mainGui.getToolbar();
	}

	/**
	 * This is useful for adding and removing elements to the pane
	 * 
	 * @return the UberTabbedPane that Ubertorrent has on the bottom
	 */
	public static UberTabbedPane getUberTabbedPane()
	{
		return mainGui.getTabPane();
	}

	/**
	 * By default contains two menus, more can be added or removed
	 * 
	 * @return the MenuBar on the top of Ubertorrent
	 */
	public static JMenuBar getJMenuBar()
	{
		return mainGui.getMainMenu();
	}

	/**
	 * Method to add a JMenu to the JMenuBar
	 * 
	 * @param jMenu
	 * @return the JMenu Component
	 */
	public static JMenu addJMenu(JMenu jMenu)
	{
		JMenuBar menu = getJMenuBar();
		JMenu retVal = menu.add(jMenu);
		mainGui.setJMenuBar(menu);

		return retVal;
	}

	/**
	 * Removes JMenu's from a JMenuBar
	 * 
	 * @param jm
	 *            the JMenu to remove from the JMenuBar
	 * @return true if one or more JMenu's were removed
	 */
	public static boolean removeJMenu(JMenu jm)
	{
		JMenuBar menu = getJMenuBar();
		boolean removed = false;
		for (int i = 0; i < menu.getMenuCount(); i++)
		{
			if (jm.getText().equals(menu.getMenu(i).getText()))
			{
				menu.remove(i);
				removed = true;
				i--;
			}
		}
		mainGui.setJMenuBar(menu);//is this strictly necessary?
		return removed;
	}

	/**
	 * Method to add a UberTab
	 * 
	 * @param tab
	 *            the tab to be added
	 * @return the JMenu Component
	 */
	public static UberTab addUberTab(UberTab tab)
	{
		PluginAccessors.getUberTabbedPane().addTab(tab);
		return tab;
	}

	/**
	 * Removes UberTabs
	 * 
	 * @param tab
	 *            the tab to be removed
	 * @return true if one or more UberTabs's were removed
	 */
	public static boolean removeUberTab(UberTab tab)
	{
		return getUberTabbedPane().removeTab(tab.getTabName());
	}

	/**
	 * @return the instance of the server that was stored in here
	 */
	public static IServer getServer()
	{
		return server;
	}

	/**
	 * @return the mainGui
	 */
	public static MainGui getMainGui()
	{
		return mainGui;
	}

	/**
	 * @param mainGui
	 *            the mainGui to set
	 */
	public static void setMainGui(MainGui mainGui)
	{
		PluginAccessors.mainGui = mainGui;
	}

	/**
	 * @param server
	 *            the server to set
	 */
	public static void setServer(IServer server)
	{
		PluginAccessors.server = server;
	}
}
