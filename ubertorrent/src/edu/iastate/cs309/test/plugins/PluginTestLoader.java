/**
 * 
 */
package edu.iastate.cs309.test.plugins;

import java.io.File;

import edu.iastate.cs309.client.PluginAccessors;
import edu.iastate.cs309.client.TheActualClient;
import edu.iastate.cs309.plugins.PluginManager;
import edu.iastate.cs309.util.Util;

/**
 * Tests loading of basic plugins
 * 
 * @author kc0dhb
 */
public class PluginTestLoader
{
	/**
	 * For shorter lines
	 */
	final static String s = File.separator;

	/*
	 * Occasionally crashes with the following output, weird
	 * 
	 * java.lang.ArrayIndexOutOfBoundsException: 5 >= 5
	 */
	//	at java.util.Vector.elementAt(Vector.java:427)
	//	at javax.swing.JTabbedPane.getTabComponentAt(JTabbedPane.java:2363)
	//	at javax.swing.plaf.basic.BasicTabbedPaneUI.calculateTabWidth(BasicTabbedPaneUI.java:1724)
	//	at javax.swing.plaf.basic.BasicTabbedPaneUI$TabbedPaneLayout.calculateTabRects(BasicTabbedPaneUI.java:2593)
	//	at javax.swing.plaf.basic.BasicTabbedPaneUI$TabbedPaneLayout.calculateLayoutInfo(BasicTabbedPaneUI.java:2488)
	//	at javax.swing.plaf.basic.BasicTabbedPaneUI$TabbedPaneLayout.layoutContainer(BasicTabbedPaneUI.java:2383)
	//	at java.awt.Container.layout(Container.java:1432)
	//	at java.awt.Container.doLayout(Container.java:1421)
	//	at java.awt.Container.validateTree(Container.java:1519)
	//	at java.awt.Container.validate(Container.java:1491)
	//	at javax.swing.plaf.basic.BasicTabbedPaneUI.ensureCurrentLayout(BasicTabbedPaneUI.java:1421)
	//	at javax.swing.plaf.basic.BasicTabbedPaneUI.paint(BasicTabbedPaneUI.java:762)
	//	at javax.swing.plaf.metal.MetalTabbedPaneUI.paint(MetalTabbedPaneUI.java:826)
	//	at javax.swing.plaf.metal.MetalTabbedPaneUI.update(MetalTabbedPaneUI.java:707)
	//	at javax.swing.JComponent.paintComponent(JComponent.java:758)
	//	at javax.swing.JComponent.paint(JComponent.java:1022)
	//	at javax.swing.JComponent.paintChildren(JComponent.java:859)
	//	at javax.swing.JSplitPane.paintChildren(JSplitPane.java:1026)
	//	at javax.swing.JComponent.paint(JComponent.java:1031)
	//	at javax.swing.JComponent.paintToOffscreen(JComponent.java:5104)
	//	at javax.swing.BufferStrategyPaintManager.paint(BufferStrategyPaintManager.java:285)
	//	at javax.swing.RepaintManager.paint(RepaintManager.java:1132)
	//	at javax.swing.JComponent._paintImmediately(JComponent.java:5052)
	//	at javax.swing.JComponent.paintImmediately(JComponent.java:4862)
	//	at javax.swing.RepaintManager.paintDirtyRegions(RepaintManager.java:727)
	//	at javax.swing.RepaintManager.paintDirtyRegions(RepaintManager.java:683)
	//	at javax.swing.RepaintManager.seqPaintDirtyRegions(RepaintManager.java:663)
	//	at javax.swing.SystemEventQueueUtilities$ComponentWorkRequest.run(SystemEventQueueUtilities.java:128)
	//	at java.awt.event.InvocationEvent.dispatch(InvocationEvent.java:209)
	//	at java.awt.EventQueue.dispatchEvent(EventQueue.java:597)
	//	at java.awt.EventDispatchThread.pumpOneEventForFilters(EventDispatchThread.java:273)
	//	at java.awt.EventDispatchThread.pumpEventsForFilter(EventDispatchThread.java:183)
	//	at java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:173)
	//	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:168)
	//	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:160)
	//	at java.awt.EventDispatchThread.run(EventDispatchThread.java:121)
	/**
	 * Simple main to test uninstalling all plugins, and then installing all of
	 * them again.
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			TheActualClient.main(null);
			PluginManager pm = PluginAccessors.getMainGui().getPluginManager();

			System.out.println("\nUninstalling all installed plugins.");

			Thread.sleep(1000);

			uninstallAllPlugins(pm);

			Thread.sleep(1000);

			System.out.println("\nInstalling all plugins.");
			runAllPlugins(pm);

			Thread.sleep(1000);
		}
		catch (Exception e)
		{
			System.err.println("The Message is" + e.getMessage());
			e.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * Uninstalls all the plugins
	 * 
	 * @param pm
	 *            to unload the plugins
	 */
	public static void uninstallAllPlugins(PluginManager pm)
	{
		for (File f : pm.getInstalledPlugins())
		{
			if (Util.DEBUG)
				System.out.println("Uninstalling: " + f.getName());
			pm.uninstallPlugin(f);
		}
	}

	/**
	 * runsAll the plugins that are created in the bin directory
	 * 
	 * @param pm
	 *            a PluginManager to load the plugins
	 * @throws Exception
	 *             if something untoward happens
	 */
	public static void runAllPlugins(PluginManager pm) throws Exception
	{
		System.out.println("running");
		for (File f : new File("/home/kc0dhb/workspace/309 Project Plugins/bin").listFiles())
		{
			if (Util.DEBUG)
				System.out.println(f.getAbsolutePath());

			if (f.getName().endsWith("uberplug"))
			{
				pm.addPlugin(f);
			}
		}
	}
}
