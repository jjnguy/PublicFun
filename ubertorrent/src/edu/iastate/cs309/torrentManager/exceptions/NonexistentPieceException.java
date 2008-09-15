package edu.iastate.cs309.torrentManager.exceptions;

public class NonexistentPieceException extends Exception
{

	public NonexistentPieceException(final String string)
	{
		super(string);
	}
	
	public NonexistentPieceException()
	{
		// blank
	}

}
