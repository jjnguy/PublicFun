package edu.iastate.cs309.plugins;

import java.io.File;
import java.util.ArrayList;

/**
 * The direct representation of the plugin xml file parsed by PluginParser.parse
 * 
 * @author kc0dhb
 */
public class PluginRepresentation
{

	/**
	 * The orginal XML file that the representation is based upon.
	 */
	File orginalXML = null;

	/**
	 * The name of the plugin
	 */
	String pluginName = "";

	/**
	 * The version of this plugin
	 */
	double version;

	/**
	 * The list of files that are included.
	 */
	ArrayList<File> fileLocal = new ArrayList<File>();

	/**
	 * The install location of the list of files.
	 */
	ArrayList<String> fileInstall = new ArrayList<String>();

	/**
	 * Server compatibility.
	 */
	Version serverCompat = new Version();

	/**
	 * Client compatibility.
	 */
	Version clientCompat = new Version();

	/**
	 * List of prerequisite plugins, may have length 0.
	 */
	ArrayList<Prerequisite> prereqs = new ArrayList<Prerequisite>();

	/**
	 * List of prerequisite applications on the machine, may have length 0
	 */
	ArrayList<PrerequisiteOther> prereqOthers = new ArrayList<PrerequisiteOther>();

	/**
	 * @param pluginName
	 * @param version
	 */
	public PluginRepresentation(String pluginName, double version)
	{
		this.pluginName = pluginName;
		this.version = version;
	}

	/**
	 * @return the pluginName
	 */
	public String getPluginName()
	{
		return pluginName;
	}

	/**
	 * @param pluginName
	 *            the pluginName to set
	 */
	public void setPluginName(String pluginName)
	{
		this.pluginName = pluginName;
	}

	/**
	 * @return the fileLocal
	 */
	public ArrayList<File> getFileLocal()
	{
		return fileLocal;
	}

	/**
	 * @param f
	 *            the fileLocal to add
	 */
	public void addFileLocal(File f)
	{
		fileLocal.add(f);
	}

	/**
	 * @return the fileInstall
	 */
	public ArrayList<String> getFileInstall()
	{
		return fileInstall;
	}

	/**
	 * @param f
	 *            the fileInstall to add to the list
	 */
	public void addFileInstall(String f)
	{
		fileInstall.add(f);
	}

	/**
	 * @return the serverCompat
	 */
	public Version getServerCompat()
	{
		return serverCompat;
	}

	/**
	 * @param serverCompat
	 *            the serverCompat to set
	 */
	public void setServerCompat(Version serverCompat)
	{
		this.serverCompat = serverCompat;
	}

	/**
	 * @return the clientCompat
	 */
	public Version getClientCompat()
	{
		return clientCompat;
	}

	/**
	 * @param clientCompat
	 *            the clientCompat to set
	 */
	public void setClientCompat(Version clientCompat)
	{
		this.clientCompat = clientCompat;
	}

	/**
	 * @return the prereqs
	 */
	public ArrayList<Prerequisite> getPrereqs()
	{
		return prereqs;
	}

	/**
	 * @param p
	 *            the prereq to add
	 */
	public void addPrereq(Prerequisite p)
	{
		prereqs.add(p);
	}

	/**
	 * @return the prereqOthers
	 */
	public ArrayList<PrerequisiteOther> getPrereqOthers()
	{
		return prereqOthers;
	}

	/**
	 * @param p
	 *            the prereqOthersto add
	 */
	public void addPrereqOther(PrerequisiteOther p)
	{
		prereqOthers.add(p);
	}

	/**
	 * @return the version
	 */
	public double getVersion()
	{
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(double version)
	{
		this.version = version;
	}

	/**
	 * @return the orginalXML
	 */
	public File getOrginalXML()
	{
		return orginalXML;
	}

	/**
	 * @param orginalXML
	 *            the orginalXML to set
	 */
	public void setOrginalXML(File orginalXML)
	{
		this.orginalXML = orginalXML;
	}

	/**
	 * Override
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientCompat == null) ? 0 : clientCompat.hashCode());
		result = prime * result + ((fileInstall == null) ? 0 : fileInstall.hashCode());
		result = prime * result + ((fileLocal == null) ? 0 : fileLocal.hashCode());
		result = prime * result + ((orginalXML == null) ? 0 : orginalXML.hashCode());
		result = prime * result + ((pluginName == null) ? 0 : pluginName.hashCode());
		result = prime * result + ((prereqOthers == null) ? 0 : prereqOthers.hashCode());
		result = prime * result + ((prereqs == null) ? 0 : prereqs.hashCode());
		result = prime * result + ((serverCompat == null) ? 0 : serverCompat.hashCode());
		long temp;
		temp = Double.doubleToLongBits(version);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		final PluginRepresentation other = (PluginRepresentation) obj;
		if (clientCompat == null)
		{
			if (other.clientCompat != null)
				return false;
		}
		else if (!clientCompat.equals(other.clientCompat))
			return false;
		if (fileInstall == null)
		{
			if (other.fileInstall != null)
				return false;
		}
		else if (!fileInstall.equals(other.fileInstall))
			return false;
		if (fileLocal == null)
		{
			if (other.fileLocal != null)
				return false;
		}
		else if (!fileLocal.equals(other.fileLocal))
			return false;
		if (orginalXML == null)
		{
			if (other.orginalXML != null)
				return false;
		}
		else if (!orginalXML.equals(other.orginalXML))
			return false;
		if (pluginName == null)
		{
			if (other.pluginName != null)
				return false;
		}
		else if (!pluginName.equals(other.pluginName))
			return false;
		if (prereqOthers == null)
		{
			if (other.prereqOthers != null)
				return false;
		}
		else if (!prereqOthers.equals(other.prereqOthers))
			return false;
		if (prereqs == null)
		{
			if (other.prereqs != null)
				return false;
		}
		else if (!prereqs.equals(other.prereqs))
			return false;
		if (serverCompat == null)
		{
			if (other.serverCompat != null)
				return false;
		}
		else if (!serverCompat.equals(other.serverCompat))
			return false;
		if (Double.doubleToLongBits(version) != Double.doubleToLongBits(other.version))
			return false;
		return true;
	}

	/**
	 * Override
	 */
	@Override
	public String toString()
	{
		String retVal = pluginName + " " + version;
		retVal += " referenced from " + orginalXML.getAbsolutePath();
		int i;
		retVal += "\nUsing Files";
		for (i = 0; i < fileLocal.size(); i++)
			retVal += "\n\t Installing from" + fileLocal.get(i).getAbsolutePath() + " to " + PluginManager.pluginInstallBaseClient.getAbsolutePath() + File.separator + fileInstall.get(i);

		retVal += "\nWith server compatibilty of " + serverCompat;
		retVal += "\nWith client compatibilty of " + clientCompat;

		retVal += "\nWith prerequisite plugins of:";
		for (Prerequisite p : prereqs)
			retVal += "\n\t" + p;

		retVal += "\nWith other prerequisites of:";
		for (PrerequisiteOther p : prereqOthers)
			retVal += "\n\t" + p;

		return retVal;
	}
}
