package edu.iastate.cs309.torrentManager.exceptions;

public class IncompleteFileException extends Exception
{

	public IncompleteFileException(String string)
	{
		super(string);
	}

	public IncompleteFileException()
	{
		super();
	}

}
