package edu.iastate.cs309.plugins.exceptions;

/**
 * An exception for when the plugin is of an incompatible version.
 * 
 * @author kc0dhb
 * 
 */
public class PluginVersionCompatException extends Exception
{

	/**
	 * To eliminate all warnings
	 */
	private static final long serialVersionUID = 4727974142185167275L;

	/**
	 * 
	 * @param s
	 *            the message that is the version and the compatible versions
	 */
	public PluginVersionCompatException(String s)
	{
		super(s);
	}
}
