package edu.iastate.cs309.guiElements.guiUTil;

/**
 * A simple enum for helping get a piece size from the CreateTorrentFrame
 * 
 * @author justin
 * 
 */
public enum PieceSize
{

	/**
	 * 
	 */
	THIRTY2(32),
	/**
	 * 
	 */
	SIXTY4(64),
	/**
	 * 
	 */
	ONE28(128),
	/**
	 * 
	 */
	TWO56(256),
	/**
	 * 
	 */
	FIVE12(512),
	/**
	 * 
	 */
	TEN24(1024),
	/**
	 * 
	 */
	TWENTY48(2048),
	/**
	 * 
	 */
	FORTY96(4096), AUTO();

	private int numBytes;
	private String pretyString;

	private PieceSize(int numBytesP)
	{
		numBytes = numBytesP;
		pretyString = (numBytesP > 1023 ? numBytes / 1024 : numBytesP) + (numBytesP > 1024 ? "MB" : "KB");
	}

	private PieceSize()
	{
		numBytes = 0;
		pretyString = "auto";
	}

	/**
	 * @return an int representing the number of bytes in each peice
	 */
	public int getNumBytes()
	{
		return numBytes * 1024;
	}

	public static PieceSize autoCalcPieceSize(long numBytes)
	{
		int idealNumPieces = 500;
		long autoCalc = numBytes / idealNumPieces;
		if (autoCalc < 64 * 1024)
			return THIRTY2;
		if (autoCalc < 128 * 1024)
			return SIXTY4;
		if (autoCalc < 256 * 1024)
			return ONE28;
		if (autoCalc < 512 * 1024)
			return TWO56;
		if (autoCalc < 1024 * 1024)
			return FIVE12;
		if (autoCalc < 2048 * 1024)
			return TEN24;
		if (autoCalc < 4096 * 1024)
			return TWENTY48;
		return FORTY96;
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		return pretyString;
	}
}
