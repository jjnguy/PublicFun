package edu.iastate.cs309.clientserverprotocol;

import edu.iastate.cs309.communication.PasswordHash;

/**
 * Encapsulates user, hashed password, host, and port.
 * 
 * @author sralmai
 */
public class UserToken
{

	private PasswordHash pwHash;
	private String host;
	private short port;

	/**
	 * @param pHash
	 * @param h
	 * @param p
	 */
	public UserToken( PasswordHash pHash, String h, short p)
	{
		pwHash = pHash;
		host = h;
		port = p;	
	}

	/**
	 * @return the host 
	 */
	public String getHost()
	{
		return host;
	}

	/**
	 * 
	 * @return the port 
	 */
	protected short getPort()
	{
		return port;
	}

	/**
	 * @return the password
	 */
	public PasswordHash getPwHash()
	{
		return pwHash;
	}
}
