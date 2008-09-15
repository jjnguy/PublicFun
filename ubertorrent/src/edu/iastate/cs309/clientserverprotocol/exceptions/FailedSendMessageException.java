package edu.iastate.cs309.clientserverprotocol.exceptions;

public class FailedSendMessageException extends FailedMessageException
{

	public FailedSendMessageException(String string)
	{
		super(string);
	}

	public FailedSendMessageException()
	{
		// blank
	}

}
