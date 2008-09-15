package edu.iastate.cs309.plugins.exceptions;

/**
 * An exception for when the plugin cannot be installed.
 * 
 * @author kc0dhb
 * 
 */
public class PluginConflictsException extends Exception
{

	/**
	 * To eliminate all warnings
	 */
	private static final long serialVersionUID = -2941355091636081210L;

	/**
	 * 
	 * @param s
	 *            the message that lists the conflicts
	 */
	public PluginConflictsException(String s)
	{
		super(s);
	}
}
