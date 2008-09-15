package edu.iastate.cs309.plugins;

import java.util.ArrayList;

/**
 * A class to ease the representation of versions for PLuginRepresentation
 * 
 * @author kc0dhb
 */
public class Version
{
	/**
	 * List of versions, may be length 0.
	 */
	ArrayList<Double> version = new ArrayList<Double>();

	/**
	 * The lowest version that is compatible, inclusive.
	 */
	Double startVersion = null;

	/**
	 * The highest version that is compatible, inclusive.
	 */
	Double endVersion = null;

	/**
	 * Default constructor has nothing mandatory.
	 */
	public Version()
	{
	}

	/**
	 * Constructor with common use information
	 * 
	 * @param startVersion
	 * @param endVersion
	 */
	public Version(Double startVersion, Double endVersion)
	{
		this.startVersion = startVersion;
		this.endVersion = endVersion;
	}

	/**
	 * @return the version
	 */
	public ArrayList<Double> getVersion()
	{
		return version;
	}

	/**
	 * @param v
	 *            the version to add
	 */
	public void addVersion(Double v)
	{
		version.add(v);
	}

	/**
	 * @return the startVersion
	 */
	public Double getStartVersion()
	{
		return startVersion;
	}

	/**
	 * @param startVersion
	 *            the startVersion to set
	 */
	public void setStartVersion(Double startVersion)
	{
		this.startVersion = startVersion;
	}

	/**
	 * @return the endVersion
	 */
	public Double getEndVersion()
	{
		return endVersion;
	}

	/**
	 * @param endVersion
	 *            the endVersion to set
	 */
	public void setEndVersion(Double endVersion)
	{
		this.endVersion = endVersion;
	}

	/**
	 * Override
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endVersion == null) ? 0 : endVersion.hashCode());
		result = prime * result + ((startVersion == null) ? 0 : startVersion.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		final Version other = (Version) obj;
		if (endVersion == null)
		{
			if (other.endVersion != null)
				return false;
		}
		else if (!endVersion.equals(other.endVersion))
			return false;
		if (startVersion == null)
		{
			if (other.startVersion != null)
				return false;
		}
		else if (!startVersion.equals(other.startVersion))
			return false;
		if (version == null)
		{
			if (other.version != null)
				return false;
		}
		else if (!version.equals(other.version))
			return false;
		return true;
	}

	/**
	 * Override
	 */
	@Override
	public String toString()
	{
		String retVal = "versions ";
		if ((startVersion != null) && (endVersion != null))
		{
			retVal += startVersion + "-" + endVersion;
			//			if (version.size() > 0)
			//			{
			//				retVal += ", ";
			//			}
		}
		int i;
		for (i = 0; i < version.size(); i++)
		{
			retVal += version.get(i);
			if (i + 1 != version.size())
			{
				retVal += ", ";
			}
		}
		return retVal;
	}

	/**
	 * A convenient method to check for compatibility
	 * 
	 * @param v
	 * @return true if the argument is compatible with the version
	 */
	public boolean isCompatible(double v)
	{
		if ((startVersion != null) && (endVersion != null))
		{
			if ((v >= startVersion) && (v <= endVersion))
				return true;
		}

		for (Double d : version)
		{
			if (d.equals(v))
				return true;
		}
		return false;
	}
}
