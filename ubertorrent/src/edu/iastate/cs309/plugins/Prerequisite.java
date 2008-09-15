package edu.iastate.cs309.plugins;

/**
 * Prerequisite class to ease the representation of prerequisites
 * 
 * @author kc0dhb
 */
public class Prerequisite
{
	/**
	 * The name of the plugin.
	 */
	String plugin = "";

	/**
	 * The compatibility of the prereq
	 */
	Version compat = new Version();

	/**
	 * Generic constructor
	 * 
	 * @param plugin
	 *            the plugin name
	 * @param compat
	 *            the plugin compatability versions
	 */
	public Prerequisite(String plugin, Version compat)
	{
		this.plugin = plugin;
		this.compat = compat;
	}

	/**
	 * @return the plugin
	 */
	public String getPlugin()
	{
		return plugin;
	}

	/**
	 * @param plugin
	 *            the plugin to set
	 */
	public void setPlugin(String plugin)
	{
		this.plugin = plugin;
	}

	/**
	 * @return the compat
	 */
	public Version getCompat()
	{
		return compat;
	}

	/**
	 * @param compat
	 *            the compat to set
	 */
	public void setCompat(Version compat)
	{
		this.compat = compat;
	}

	/**
	 * Override
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((compat == null) ? 0 : compat.hashCode());
		result = prime * result + ((plugin == null) ? 0 : plugin.hashCode());
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
		final Prerequisite other = (Prerequisite) obj;
		if (compat == null)
		{
			if (other.compat != null)
				return false;
		}
		else if (!compat.equals(other.compat))
			return false;
		if (plugin == null)
		{
			if (other.plugin != null)
				return false;
		}
		else if (!plugin.equals(other.plugin))
			return false;
		return true;
	}

	/**
	 * Override
	 */
	@Override
	public String toString()
	{
		return plugin + " " + compat;
	}
}
