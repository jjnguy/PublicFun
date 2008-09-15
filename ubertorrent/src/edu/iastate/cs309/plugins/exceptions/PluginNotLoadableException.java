package edu.iastate.cs309.plugins.exceptions;

/**
 * An exception for when the supposedly already installed plugins cannot be
 * instantiated.
 * 
 * @author kc0dhb
 * 
 */
public class PluginNotLoadableException extends Exception
{
	/**
	 * To eliminate all warnings
	 */
	private static final long serialVersionUID = -8212749538186033376L;

	/**
	 * 
	 * @param s
	 *            the message for the GUI to display.
	 */
	public PluginNotLoadableException(String s)
	{
		super(s);
	}
}
