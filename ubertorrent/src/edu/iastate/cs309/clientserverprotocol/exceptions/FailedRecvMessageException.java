package edu.iastate.cs309.clientserverprotocol.exceptions;

public class FailedRecvMessageException extends FailedMessageException
{

	public FailedRecvMessageException(String string)
	{
		super(string);
	}

	public FailedRecvMessageException()
	{
		// blank
	}

}
