package edu.iastate.cs309.clientserverprotocol.exceptions;

public class CommunicationException extends Exception
{

	public CommunicationException(String string)
	{
		super(string);
	}
	
	public CommunicationException()
	{
		super();
	}

}
