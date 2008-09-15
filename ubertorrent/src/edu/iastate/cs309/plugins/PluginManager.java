package edu.iastate.cs309.plugins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import edu.iastate.cs309.plugins.exceptions.PluginConflictsException;
import edu.iastate.cs309.plugins.exceptions.PluginNotLoadableException;
import edu.iastate.cs309.plugins.exceptions.PluginVersionCompatException;
import edu.iastate.cs309.plugins.interfaces.Plugin;
import edu.iastate.cs309.util.Util;

/**
 * The class that tests to see if the plugins are valid and can be loaded
 * 
 * @author kc0dhb
 */
public class PluginManager
{

	/**
	 * Magic
	 */
	ClassLoader loader = new FileClassLoader(pluginInstallBaseClient.getAbsolutePath());

	/**
	 * 
	 */
	ArrayList<InstalledPlugin> installed = new ArrayList<InstalledPlugin>();

	/**
	 * The base file location for client plugins
	 * 
	 * TODO [kyle] change for final installation directory, when known
	 */
	public final static File pluginInstallBaseClient = new File("plugins");

	/**
	 * The base file location for server plugins
	 * 
	 * TODO [kyle] change for final installation directory, when known
	 */
	public final static File pluginInstallBaseServer = new File("plugins");

	/**
	 * for Utility
	 */
	private final static String s = File.separator;

	/**
	 * Instantiate the Manager
	 * 
	 * @throws PluginNotLoadableException
	 *             if the already installed plugins cannot be instantiated
	 */
	public PluginManager() throws PluginNotLoadableException
	{
		startInstalledPlugins();
	}

	/**
	 * This adds a Plugin to the system.
	 * 
	 * All exceptions should be caught and displayed to the user.
	 * 
	 * @param xml
	 *            the File pointing to the xml representation of the plugin
	 * @throws PluginConflictsException
	 *             if the plugin cannot be installed because of file conflicts,
	 *             the message should have a list of the files that conflict
	 * @throws PluginVersionCompatException
	 *             if the plugin has incompatible versions, has a nice message
	 * @throws IOException
	 *             if files cannot be installed
	 */
	public void addPlugin(File xml) throws PluginConflictsException, PluginVersionCompatException, IOException
	{
		//parse the new plugin
		PluginRepresentation pr = PluginParser.parse(xml);

		if (!pr.getClientCompat().isCompatible(Util.getVersionInfo()))
			throw new PluginVersionCompatException("Client is compatible with: " + pr.getClientCompat() + " but this is " + Util.getUber() + " version " + Util.getVersionInfo() + ".");

		//TODO [kyle] add support for plugin prereqs
		//TODO [kyle] add support for prereq-others

		String conflicts = pluginConflicts(pr);
		//Throw an exception for conflict
		if (conflicts != null)
			throw new PluginConflictsException(conflicts);

		//TODO [kyle] add Server support for plugins

		InstalledPlugin install = new InstalledPlugin(new File(pluginInstallBaseClient.getAbsoluteFile() + s + pr.getPluginName() + " v" + pr.getVersion()));

		for (File f : installLocalPlugin(pr))
		{
			Class<?> c;
			try
			{

				loader = new FileClassLoader(f.getParentFile().getAbsolutePath());
				c = loader.loadClass(f.getName().replaceAll("\\.class", ""));

				runPlugin(c, install);
				install.addClass(c);
			}
			catch (ClassNotFoundException e)
			{
				if (Util.DEBUG)
					e.printStackTrace();
			}
		}
		installed.add(install);
	}

	/**
	 * installs the local plugin where specified
	 * 
	 * @param pr
	 * @return a list of clients to run
	 * @throws PluginConflictsException
	 *             if the plugin cannot be installed because of file conflicts
	 *             which has a message list of the files that conflict
	 * @throws IOException
	 *             if files cannot be installed
	 */
	private ArrayList<File> installLocalPlugin(PluginRepresentation pr) throws PluginConflictsException, IOException
	{
		ArrayList<File> retVal = new ArrayList<File>();
		for (int i = 0; i < pr.getFileLocal().size(); i++)
		{
			//sets up the destination file
			File dest = new File(pluginInstallBaseClient.getAbsoluteFile() + s + pr.getPluginName() + " v" + pr.getVersion() + s + pr.getFileInstall().get(i));
			if (!dest.exists())
			{
				if (!dest.getParentFile().exists())
					dest.getParentFile().mkdirs();
				dest.createNewFile();
			}
			else
				throw new PluginConflictsException("The file " + dest.getAbsolutePath() + " already exists.");

			File src = pr.getFileLocal().get(i);
			if (!Util.fileCopy(src, dest))
			{
				dest.delete();
				if (Util.DEBUG)
					System.out.println("Failed to copy from " + src.getAbsolutePath() + " to " + dest.getAbsolutePath());
			}
			else
			{
				if (Pattern.matches(".*\\.class", dest.getName()))
					retVal.add(dest);
			}
		}
		return retVal;
	}

	/**
	 * 
	 * @param pr
	 * @return null if it is possible to install the plugin without conflicting
	 *         with already installed plugins, else returns the list of file
	 *         conflicts
	 */
	private String pluginConflicts(PluginRepresentation pr)
	{
		String retVal = null;
		for (String install : pr.getFileInstall())
		{
			File f = new File(pluginInstallBaseClient.getAbsolutePath() + s + install);
			if (f.exists())
				retVal += "\"" + f.getAbsolutePath() + "\" ";

		}
		return retVal;
	}

	/**
	 * Uninstalls the plugin referenced by the directory.
	 * 
	 * @param dir
	 *            The directory to remove
	 * @return false if the plugin is not removed
	 */
	public boolean uninstallPlugin(File dir)
	{
		//find the executables and run the unload.
		//loop and uninstall
		for (int i = 0; i < installed.size(); i++)
		{
			if (installed.get(i).getInstallDir().getAbsolutePath().equals(dir.getAbsolutePath()))
			{
				for (Object o : installed.get(i).getObjects())
				{
					try
					{
						Plugin p = (Plugin) o;
						p.unloadPlugin();
					}
					catch (Exception e)
					{
						if (Util.DEBUG)
							e.printStackTrace();
					}
				}
				installed.remove(i);
				i--;
			}
		}

		//delete everything
		if (Util.DEBUG)
		{
			System.out.println("InstallBase " + pluginInstallBaseClient.getAbsolutePath());
			System.out.println("Selected dir " + dir.getAbsolutePath());

		}

		for (File f : pluginInstallBaseClient.listFiles())
		{

			if (f.getAbsolutePath().equals(dir.getAbsolutePath()))
			{
				if (Util.DEBUG)
				{
					System.out.println("Started a recursive Delete");
				}
				recursiveDelete(f);
			}
		}
		return true;
	}

	/**
	 * Recursively deletes everything
	 * 
	 * @param f
	 */
	private static void recursiveDelete(File f)
	{
		for (File fs : f.listFiles())
		{
			if (fs.isDirectory())
			{
				recursiveDelete(fs);
			}
			else
			{

				if (Util.DEBUG)
					System.out.println(fs.getAbsolutePath() + " was deleted: " + fs.delete());
				else
					fs.delete();
			}
		}
		if (Util.DEBUG)
			System.out.println(f.getAbsolutePath() + " was deleted: " + f.delete());
		else
			f.delete();
	}

	/**
	 * This starts all installed plugins and shoculd be called at startup
	 * 
	 * @throws PluginNotLoadableException
	 *             if the already installed plugins cannot be instantiated
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private void startInstalledPlugins() throws PluginNotLoadableException
	{
		/*
		 * This basically iterates through all installed files and if they
		 * implement plugin, start them, and if they implement various other
		 * extensions to instantiate them.
		 */
		try
		{
			if (!pluginInstallBaseClient.exists())
			{
				pluginInstallBaseClient.mkdirs();
			}
			for (File f : pluginInstallBaseClient.listFiles())
			{
				//currently ignores files as they are "improperly" installed

				if (f.isDirectory())
				{
					InstalledPlugin inst = new InstalledPlugin(f);
					recursiveLoader(f, inst);
					if (inst.getClasses().size() > 0)
					{
						installed.add(inst);
					}
				}
			}
		}
		catch (ClassNotFoundException e)
		{
			if (Util.DEBUG)
			{
				e.printStackTrace();
			}
			//TODO [kyle] actually handle this gracefully and actually unload...
			throw new PluginNotLoadableException("Could not load already installed plugins, loading none.");
		}
		try
		{
			runPlugins();
		}
		catch (Exception e)
		{
			if (Util.DEBUG)
				e.printStackTrace();
			//TODO kyle do something here?
			throw new PluginNotLoadableException("Could not run already installed plugins, we are in an undefined state, please erase your plugins and restart.");
		}
	}

	/**
	 * Recursively loads all class files
	 * 
	 * @param root
	 *            the current root that we are iterating through
	 * @param inst
	 *            the representation of the individual files to add the loaded
	 *            classes to
	 * @throws ClassNotFoundException
	 */
	private void recursiveLoader(File root, InstalledPlugin inst) throws ClassNotFoundException
	{

		for (File f : root.listFiles())
		{
			if (f.isFile())
			{
				if (Pattern.matches(".*\\.class", f.getName()))
				{
					loader = new FileClassLoader(f.getParentFile().getAbsolutePath());
					Class<?> c = loader.loadClass(f.getName().replaceAll("\\.class", ""));
					inst.addClass(c);
				}
				else if (Pattern.matches(".*\\.jar", f.getName()))
				{
					System.err.println(".jar loading not implemented.");
					//TODO [kyle] Load .jars
				}
			}
			else if (f.isDirectory())
			{
				recursiveLoader(f, inst);
			}
			else
			{
				System.err.println("Neither a file or a directory, why are you trying to load " + f.getAbsolutePath() + "?");
			}
		}
	}

	/**
	 * Iterates over the plugins and runs them if they are of the correct types.
	 * 
	 */
	private void runPlugins()
	{
		for (InstalledPlugin inst : installed)
		{
			for (Class<?> c : inst.getClasses())
			{
				runPlugin(c, inst);
			}
		}
	}

	/**
	 * Run a singal class.
	 * 
	 * @param c
	 */
	private void runPlugin(Class<?> c, InstalledPlugin install)
	{
		try
		{
			Plugin p = (Plugin) c.newInstance();

			try
			{
				if (!p.loadPlugin())
				{
					//TODO [kyle] throw an exception like a good little boy.
				}
				install.addObject(p);
			}
			catch (Exception e)
			{
				//TODO [kyle] handle later
				if (Util.DEBUG)
				{
					System.err.println("the plugin " + p.getName() + " version " + p.getVersion() + " cannot be initialized.");
					e.printStackTrace();
				}
			}
		}
		catch (Exception e)
		{
			//TODO [kyle] handle correctly and throw up correctly.
			if (Util.DEBUG)
			{
				System.out.println("This doesn't implement a Plugin, but is likely necessary for the plugin");
				StackTraceElement[] ste = e.getStackTrace(); //e.printStackTrace();
				for (int i = 0; i < ste.length; i++)
				{
					System.out.println("\t" + ste[i].toString());
				}
				System.out.println("End debug \"harmless\" stack trace.");
			}
		}
	}

	/**
	 * Gets a list of possible plugins to uninstall. The File that is wanted to
	 * uninstall should be passed to uninstallPlugin
	 * 
	 * @return a list of plugins installed.
	 */
	public ArrayList<File> getInstalledPlugins()
	{
		ArrayList<File> retVal = new ArrayList<File>();
		for (File f : pluginInstallBaseClient.listFiles())
		{
			if (f.isDirectory())
			{
				retVal.add(f);
			}
		}
		return retVal;
	}

	/**
	 * This method will erase all plugins. This method should be an option for
	 * the "Safe Mode" of the program. It should be run before instantiation of
	 * the PluginManager
	 */
	public static void removeAllPlugins()
	{
		recursiveDelete(pluginInstallBaseClient);
	}

	/**
	 * Removes all plugins nicely
	 * 
	 * @return true if all plugins are removed, false if there are no plugins or
	 *         one of the plugins is failed to be removed
	 */
	public boolean removeAllPluginsLocal()
	{
		boolean retVal;
		if (getInstalledPlugins().size() > 0)
			retVal = true;
		else
			return false;

		for (File f : getInstalledPlugins())
			retVal = retVal & uninstallPlugin(f);

		return retVal;
	}
}
