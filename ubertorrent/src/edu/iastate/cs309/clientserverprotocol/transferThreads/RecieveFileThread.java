package edu.iastate.cs309.clientserverprotocol.transferThreads;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocket;

import edu.iastate.cs309.clientserverprotocol.NetUtils;
import edu.iastate.cs309.clientserverprotocol.UserToken;
import edu.iastate.cs309.clientserverprotocol.exceptions.FailedMessageException;
import edu.iastate.cs309.comminterfaces.Client;
import edu.iastate.cs309.util.Util;

/**
 * This thread connects to a SendFileThread at the specified port and recieves a
 * file. (This should be called to download a file from the server).
 * 
 * The transfer system has marginal security, but a fast attacker could at least
 * block file transfers from occurring by connecting on new ports before the
 * authorized client does. The thread would close the connection after 20 bytes
 * of garbage were sent.
 * 
 * @author sralmai
 */
public class RecieveFileThread implements Runnable
{
	/** hash for authentication */
	private UserToken ut = null;

	/** used to pass input stream on */
	Client callback = null;

	/** port to connect on */
	int port = 0;

	/**
	 * Initialize a ftt
	 * 
	 * @param ut
	 *            connection info
	 * @param port
	 *            port to connect on
	 * @param callback
	 *            client to send the file to
	 */
	public RecieveFileThread(Client callback, UserToken ut, int port)
	{
		this.ut = ut;
		this.port = port;
		this.callback = callback;
	}

	/**
	 * connect and grab the file
	 */
	public void run()
	{
		try
		{
			SSLSocket s = NetUtils.connectOnPort(ut, port);

			InputStream in = s.getInputStream();

			/** read refID and index (first two integers sent) */
			int refID = NetUtils.readInt(in);
			int index = NetUtils.readInt(in);

			/**
			 * passing in arrays is a nasty artifact of this (poor)
			 * client/server model
			 */

			if (Util.DEBUG)
			{
				System.out.println("recvFileThread shows " + in.available() + "bytes to read");
			}

			List<Integer> indices = new ArrayList<Integer>();
			indices.add(index);

			List<InputStream> files = new ArrayList<InputStream>();
			files.add(in);

			callback.transferFiles(refID, indices, files);
		}
		catch (IOException e)
		{
			if (Util.DEBUG)
				e.printStackTrace();
		}
		catch (FailedMessageException e)
		{
			if (Util.DEBUG)
				e.printStackTrace();
		}
	}
}
