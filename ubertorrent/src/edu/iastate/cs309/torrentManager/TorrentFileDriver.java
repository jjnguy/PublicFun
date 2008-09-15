package edu.iastate.cs309.torrentManager;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import edu.iastate.cs309.torrentparser.ParseException;
import edu.iastate.cs309.torrentparser.TorrentParser;

/**
 * run all of TorrentFile's methods
 * @author sralmai
 *
 */
public class TorrentFileDriver {
	
	public static void main( String[] args) throws IOException, NoSuchAlgorithmException, ParseException
	{
		String[] files = args;
		if (files.length == 0)
		{
			System.out.println("Please enter the location of a .torrent file to parse: ");
			files = new String[] { new Scanner(System.in).nextLine() };
		}
		
		for (String file : files){
			TorrentParser.getInfoHash(file);
		
			runTorrentFile(file);
		}
	}
	
	private static void runTorrentFile( String file)
	{
		try{
		TorrentFile tm = new TorrentFile( file);
		
		int numOfPieces = tm.getNumOfPieces();
		
		System.out.println("Announce: " + tm.getAnnounce()+"\n"
				+ "Piece length: " + tm.pieceLength() + "\n"
				+ "Base name: " + tm.getBaseName() + "\n"
				+ "Number of pieces: " + numOfPieces);
		
		if(tm.isSingleFile())
		{
			System.out.println("Length: " + tm.fileLength());
		}
		else
		{
			int numFiles = tm.getNumOfFiles();
			for(int i = 0; i<numFiles; ++i)
			{
				System.out.println("Path: " + tm.filePath(i));
				System.out.println("Length: " + tm.fileLength(i));
			}
		}
		
//		/** print out all SHA1 hashes */
//		for( int i=0; i< numOfPieces; ++i)
//		{
//			System.out.println("\tPiece " +i+ " hash: " + 
//					new String( tm.getHash(i)));
//		}
		}
		catch(Exception e)
		{
			System.err.println("Something broke: " + e.getMessage());
		}
		
	}

}
