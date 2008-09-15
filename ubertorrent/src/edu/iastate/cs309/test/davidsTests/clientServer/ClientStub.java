package edu.iastate.cs309.test.davidsTests.clientServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import edu.iastate.cs309.clientserverprotocol.exceptions.FailedMessageException;
import edu.iastate.cs309.clientserverprotocol.exceptions.FailedSendMessageException;
import edu.iastate.cs309.comminterfaces.Client;
import edu.iastate.cs309.communication.ServerProperties;
import edu.iastate.cs309.communication.TorrentInfo;
import edu.iastate.cs309.communication.TorrentProp;

/**
 * simply dump information out
 * 
 * @author sralmai
 * 
 */
public class ClientStub implements Client
{

	public void torrentList(List<Integer> allTorrents) throws FailedSendMessageException
	{
		System.out.println("ClientStub recieved torrentList: " + allTorrents.toString());

	}

	public void torrentRemoved(int refID) throws FailedMessageException
	{
		System.out.println("ClientStub recieved torrentRemoved on refID: " + refID);

	}

	public void transferFiles(int refID, List<Integer> fileIndexes, List<InputStream> data) throws FailedMessageException
	{
		System.out.println("ClientStub recieved transferFiles. refID: " + refID + " fileIndices: " + fileIndexes);

		/** random size for transfers */
		byte[] buff = new byte[1024];

		for (int i = 0; i < fileIndexes.size(); ++i)
		{
			OutputStream out = null;
			try
			{
				System.out.println("Reading file index: " + i);
				out = new FileOutputStream(new File("TODO.txt.copy" + fileIndexes.get(i)));

				int read = data.get(i).read(buff);
				while (read > -1)
				{
					System.out.println("read " + read + " bytes");
					out.write(buff, 0, read);
					read = data.get(i).read(buff);
				}

				System.out.println("finished writing file index " + i);

				out.close();
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void updateServerStatus(ServerProperties settings) throws FailedMessageException
	{
		System.out.println("ClientStub recieved updateServerStatus");

	}

	public void updateTorrentInformation(int refID, TorrentInfo info) throws FailedMessageException
	{
		System.out.println("ClientStub recieved updateTorrentInformation for refID: " + refID + " torrentinfo: " + info.toString());

	}

	public void updateTorrentProperties(int refID, TorrentProp info) throws FailedMessageException
	{
		System.out.println("ClientStub recieved updateTorrentProperties for refID: " + refID);

	}

}
