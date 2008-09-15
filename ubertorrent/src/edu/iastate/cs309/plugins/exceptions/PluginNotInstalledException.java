package edu.iastate.cs309.plugins.exceptions;

/**
 * An exception for when the plugin that is not installed.
 * 
 * @author kc0dhb
 * 
 */
public class PluginNotInstalledException extends Exception
{

	/**
	 * To eliminate all warnings
	 */
	private static final long serialVersionUID = -8878276479816384421L;

	/**
	 * 
	 * @param s
	 *            the message that says what plugin is not installed
	 */
	public PluginNotInstalledException(String s)
	{
		super(s);
	}
}
