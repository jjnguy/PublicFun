package edu.iastate.cs309.clientserverprotocol.exceptions;

/**
 * Base class for all exceptions generated from an open server/client
 * connection.
 * 
 * @author singularity
 */
public class ServerClientIOException extends Exception
{

	public ServerClientIOException()
	{

	}

	public ServerClientIOException(String string)
	{
		super(string);
	}
}
