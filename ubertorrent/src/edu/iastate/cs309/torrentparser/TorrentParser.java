/**
 * 
 */
package edu.iastate.cs309.torrentparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Scanner;

import edu.iastate.cs309.torrentManager.containers.InfoHash;

/**
 * Note: not used in the main UberTorrent applications.
 * 
 ***** Note [david]: this is used to parse .torrent files
 * 
 * This class is meant for debugging purposes only, and may be removed at a
 * later date.
 * 
 * @author Michael Seibert
 */
public class TorrentParser
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		String[] files = args;
		if (files.length == 0)
		{
			System.out.println("Please enter the location of a .torrent file to parse: ");
			files = new String[] { new Scanner(System.in).nextLine() };
		}
		for (String file : files)
			parseTorrent(file);
	}

	private static void parseTorrent(String file)
	{
		try
		{
			printTorrentInfo(file, getDictionary(file));
		}
		catch (FileNotFoundException e)
		{
			if (e.getMessage() != null)
				System.out.println(e.getMessage());
			System.out.println("Couldn't find the file.");
		}
		catch (ParseException e)
		{
			if (e.getMessage() != null)
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			System.out.println("The file is probably corrupted.");
		}
		catch (IOException e)
		{
			if (e.getMessage() != null)
				System.out.println(e.getMessage());
			System.out.println("Could read the file.");
		}
	}

	/**
	 * public so TorrentFile can use it
	 * 
	 * @param file
	 * @return A {@link Dictionary}
	 * @throws FileNotFoundException
	 * @throws ParseException
	 * @throws IOException
	 */
	public static Dictionary getDictionary(String file) throws FileNotFoundException, ParseException, IOException
	{
		FileInputStream in;

		in = new FileInputStream(new File(file));

		BEncodedObject obj = BEncodedObject.readObject(in);

		if (!(obj instanceof Dictionary))
			throw new ParseException("Created dictionary has bad semantics!");

		in.close();

		return (Dictionary) obj;
	}

	/**
	 * 
	 */
	private static void printTorrentInfo(String file, Dictionary torrent)
	{
		try
		{
			StringBuilder displayInfo = new StringBuilder();
			displayInfo.append("Information for " + file + ":\n");

			addInfo(torrent, displayInfo, "comment", "Comment", ByteString.class, false);
			addInfo(torrent, displayInfo, "created by", "Created by", ByteString.class, false);
			addCreationTime(torrent, displayInfo);
			addInfo(torrent, displayInfo, "announce", "Announce", ByteString.class, true);

			/** grab info dictionary */
			BEncodedObject infoObj = torrent.get("info");
			if (infoObj == null || !(infoObj instanceof Dictionary))
			{
				if (infoObj == null)
					throw new ParseException("info was nonexistant.");
				else
					throw new ParseException("info was not a Dictionary by .torrent standards.");
			}

			Dictionary info = (Dictionary) infoObj;

			addInfo(info, displayInfo, "piece length", "Piece length (bytes)", BInteger.class, true);

			addInfo(info, displayInfo, "name", "Name", ByteString.class, true);

			/** check for "files" list */
			BEncodedObject filesObj = info.get("files");
			if (filesObj != null)
			{
				if (!(filesObj instanceof BList))
					throw new ParseException("The files object is not a list.");

				BList files = (BList) filesObj;
				BEncodedObject[] fileD = files.get();

				for (int i = 0; i < fileD.length; ++i)
				{
					if (!(fileD[i] instanceof Dictionary))
						throw new ParseException("The " + i + "th item in the files obj was not a dictionary.");
					Dictionary d = (Dictionary) fileD[i];

					addInfo(d, displayInfo, "length", "Length", BInteger.class, true);
					addInfo(d, displayInfo, "md5sum", "md5sum", ByteString.class, false);
					/** skipping "path" (is a list) */
				}

			}

			System.out.println(displayInfo);
		}

		catch (ParseException e)
		{
			System.out.println("The torrent file has incorrect semantics");
			e.printStackTrace();
		}
	}

	/**
	 * @param torrent
	 * @param info
	 * @throws ParseException
	 */
	private static void addCreationTime(Dictionary torrent, StringBuilder info) throws ParseException
	{
		BEncodedObject infoObj = torrent.get("creation date");
		if (infoObj == null)
			return;
		if (!(infoObj instanceof BInteger))
			throw new ParseException("Creation date was not a BInteger.");
		long date = ((BInteger) infoObj).get();
		info.append("Created on: " + new Date(date * 1000).toString() + "\n");
	}

	/**
	 * @param torrent
	 * @param info
	 * @param required
	 * @throws ParseException
	 */
	private static void addInfo(Dictionary torrent, StringBuilder info, String name, String description, Class<? extends BEncodedObject> type, boolean required) throws ParseException
	{
		BEncodedObject infoObj = torrent.get(name);
		if (infoObj == null)
		{
			if (required)
				throw new ParseException("The required object" + description + " was left out.");
			return;
		}
		if (!type.isInstance(infoObj))
			throw new ParseException("I dunno what caused this exception to be thrown.");
		String infoVal = infoObj.toString();
		info.append(description + ": " + infoVal + "\n");
	}

	/**
	 * generate the info_hash from a torrent file
	 * 
	 * this is needed for communication with the tracker ** making the (usually
	 * true, but nasty) assumption that the files dictionary (the part that is
	 * hashed), is the last part of the torrent file
	 * 
	 * @param file
	 *            torrent to generate infohash from
	 * @return SHA1 hash of the value of the info key
	 * @throws IOException
	 * @throws ParseException
	 * @throws NoSuchAlgorithmException
	 */
	public static InfoHash getInfoHash(String file) throws IOException, ParseException, NoSuchAlgorithmException
	{
		FileInputStream in;

		in = new FileInputStream(new File(file));

		int input = in.read();
		byte cur;
		boolean done = false;
		while (input > -1 && !done)
		{
			cur = (byte) input;

			if (cur == '4')
			{
				if (in.read() == ':' && in.read() == 'i' && in.read() == 'n' && in.read() == 'f' && in.read() == 'o')
					return makeHash(in);
			}

			input = in.read();
		}

		throw new ParseException("Couldn't find info dictionary!");

	}

	private static InfoHash makeHash(FileInputStream in) throws ParseException, IOException, NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.reset();

		FileChannel f = in.getChannel();

		/** start on the 'd' for this dictionary */
		long start = f.position();

		/** leave the last 'e' on (the entire file is a dictionary) */
		long stop = f.size() - 1;

		if ((stop - start) != (int) (stop - start))
			throw new ParseException("Torrent file is huge.. need a better parser");

		byte[] bits = new byte[(int) (stop - start)];
		f.read(ByteBuffer.wrap(bits), start);

		return new InfoHash(md.digest(bits));
	}
}
