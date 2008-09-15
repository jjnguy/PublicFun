package edu.iastate.cs309.clientserverprotocol.exceptions;

public class FailedMessageException extends ServerClientIOException
{

	public FailedMessageException(String string)
	{
		super(string);
	}

	public FailedMessageException()
	{
		// empty
	}

}
