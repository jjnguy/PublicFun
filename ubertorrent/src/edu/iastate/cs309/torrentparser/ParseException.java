/**
 * 
 */
package edu.iastate.cs309.torrentparser;

/**
 * @author Michael Seibert
 */
public class ParseException extends Exception
{
	/**  */
	private static final long serialVersionUID = 792485183200725619L;

	/**
	 * Create an exception with the given message
	 * 
	 * @param message
	 */
	public ParseException(String message)
	{
		super(message);
	}

	/**
	 * Create an exception with a blank message
	 */
	public ParseException()
	{
		// left blank
	}
}
