package edu.iastate.cs309.plugins;

import java.io.File;

/**
 * Class to ease construction
 * 
 * @author kc0dhb
 */
public class PrerequisiteOther
{
	/**
	 * The name of the prerequisite
	 */
	String Name = "";

	/**
	 * The path on the installation system
	 */
	File path = null;

	/**
	 * The constructor with all mandatory data
	 * 
	 * @param name
	 *            the name of the plugin
	 * @param path
	 *            the path to the installation file required
	 */
	public PrerequisiteOther(String name, File path)
	{
		Name = name;
		this.path = path;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return Name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		Name = name;
	}

	/**
	 * @return the path
	 */
	public File getPath()
	{
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(File path)
	{
		this.path = path;
	}

	/**
	 * Override
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Name == null) ? 0 : Name.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	/**
	 * Override
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PrerequisiteOther other = (PrerequisiteOther) obj;
		if (Name == null)
		{
			if (other.Name != null)
				return false;
		}
		else if (!Name.equals(other.Name))
			return false;
		if (path == null)
		{
			if (other.path != null)
				return false;
		}
		else if (!path.equals(other.path))
			return false;
		return true;
	}

	/**
	 * Override
	 */
	@Override
	public String toString()
	{
		return Name + " located at " + path.getAbsolutePath();
	}

}
