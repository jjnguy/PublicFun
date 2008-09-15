/**
 * 
 */
package edu.iastate.cs309.communication;

import edu.iastate.cs309.torrentManager.containers.ByteID;
import edu.iastate.cs309.torrentparser.ParseException;

/**
 * 
 * 
 * @author Michael Seibert
 */
public class PasswordHash extends ByteID
{
	/**
	 * @param b
	 * @throws ParseException
	 */
	public PasswordHash(byte[] b)
	{
		super(b);
	}
	
	public PasswordHash()
	{
		super();
	}
}
