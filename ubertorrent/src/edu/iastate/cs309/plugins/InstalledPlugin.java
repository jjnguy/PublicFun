package edu.iastate.cs309.plugins;

import java.io.File;
import java.util.ArrayList;

/**
 * A class to hold the references to an installed plugin for unloading when
 * uninstalling
 * 
 * @author kc0dhb
 * 
 */
public class InstalledPlugin
{
	/**
	 * The directory that the plugin is installed in.
	 */
	File installDir;

	/**
	 * List of all classes installed
	 */
	ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

	/**
	 * List of all classes installed
	 */
	ArrayList<Object> objects = new ArrayList<Object>();

	/**
	 * Add the install directory.
	 * 
	 * @param installDir
	 */
	public InstalledPlugin(File installDir)
	{
		this.installDir = installDir;
	}

	/**
	 * @return the installDir
	 */
	public File getInstallDir()
	{
		return installDir;
	}

	/**
	 * @param installDir
	 *            the installDir to set
	 */
	public void setInstallDir(File installDir)
	{
		this.installDir = installDir;
	}

	/**
	 * @return the classes
	 */
	public ArrayList<Class<?>> getClasses()
	{
		return classes;
	}

	/**
	 * @param c
	 *            the class to add
	 */
	public void addClass(Class<?> c)
	{
		classes.add(c);
	}

	/**
	 * 
	 * @return the objects
	 */
	public ArrayList<Object> getObjects()
	{
		return objects;
	}

	/**
	 * 
	 * 
	 * @param object
	 */
	public void addObject(Object object)
	{
		objects.add(object);
	}

}
