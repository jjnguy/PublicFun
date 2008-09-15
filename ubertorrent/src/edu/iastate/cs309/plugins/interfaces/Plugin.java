/**
 * 
 */
package edu.iastate.cs309.plugins.interfaces;


/*
 * Both
 * 
 * initializePlugin (where it can run a thread....!)
 * 
 */
/*
 * Client
 * 
 * Context menus
 * 
 * top menu bar
 * 
 * all menu items
 * 
 * too bar
 * 
 * new tabs
 */

/**
 * 
 */

/**
 * @author kc0dhb
 */
public interface Plugin
{
	/**
	 * This starts the plugin and initializes all basic plugin functions
	 * 
	 * @return true if the plugin was initialized correctly
	 * @throws Exception
	 *             that the plugin Manager must handle
	 */
	public boolean loadPlugin() throws Exception;

	/**
	 * This is called when the plugin is unloaded, this should unload all
	 * Menubars. toolbars, and UberTabs,
	 * 
	 * @return true if the plugin was initialized correctly
	 * @throws Exception
	 *             that the plugin Manager must handle
	 */
	public boolean unloadPlugin() throws Exception;

	/**
	 * 
	 * @return the name of the plugin
	 */
	public String getName();

	/**
	 * 
	 * @return the version of the plugin
	 */
	public double getVersion();

}
