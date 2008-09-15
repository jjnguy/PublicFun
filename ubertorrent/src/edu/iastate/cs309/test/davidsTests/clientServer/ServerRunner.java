package edu.iastate.cs309.test.davidsTests.clientServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import edu.iastate.cs309.clientserverprotocol.ClientConnection;
import edu.iastate.cs309.clientserverprotocol.exceptions.FailedMessageException;
import edu.iastate.cs309.communication.PasswordHash;
import edu.iastate.cs309.communication.ServerProperties;
import edu.iastate.cs309.communication.TorrentInfo;
import edu.iastate.cs309.communication.TorrentProp;

/**
 * test the client/server communication
 * 
 * @author sralmai
 * 
 */
public class ServerRunner
{
	/** fake server */
	private static ServerStub serverStub = new ServerStub();

	/** the password */
	private static PasswordHash pw = null;

	private static ClientConnection c = null;

	/**
	 * open a ClientConnection and send all possible messages
	 * 
	 * @param args
	 *            ignored list of arguments
	 */
	public static void main(String[] args)
	{

		try
		{
			pw = new PasswordHash(new byte[20]);

			ServerSocketFactory ssocketFactory = SSLServerSocketFactory.getDefault();

			System.out.println();
			SSLServerSocket sc = (SSLServerSocket) ssocketFactory.createServerSocket(1432);
			String[] ciphers = { "SSL_DH_anon_WITH_RC4_128_MD5" };
			sc.setEnabledCipherSuites(ciphers);

			/** get the connection we just started */
			SSLSocket s = (SSLSocket) sc.accept();

			c = new ClientConnection(s, serverStub, pw);

			sendNonTransferMsgs();

			sendTransferMsgs();

		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("couldn't bind to server socket" + e.getMessage());
			return;
		}
		catch (FailedMessageException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * send a "transfer files" message to the client
	 * 
	 */
	private static void sendTransferMsgs()
	{
		/** open up TODO.txt for sending over the pipe */
		try
		{
			InputStream in = new FileInputStream(new File("testTorrentFiles/dieselsweeties/dieselsweeties_volume01_webcomic_ebook.torrent"));
			List<Integer> indices = new ArrayList<Integer>();
			List<InputStream> files = new ArrayList<InputStream>();

			/** the meaning of life */
			indices.add(42);
			files.add(in);

			c.transferFiles(0, indices, files);
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (FailedMessageException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * send one of each kind of message except those which spawn file transfer
	 * threads
	 * 
	 * @throws FailedMessageException
	 */
	private static void sendNonTransferMsgs() throws FailedMessageException
	{
		c.torrentList((new ArrayList<Integer>()));
		c.torrentRemoved(14);
		c.updateServerStatus(new ServerProperties());
		TorrentInfo t = new TorrentInfo();
		t.setName("he");
		c.updateTorrentInformation(4, t);
		c.updateTorrentProperties(111, new TorrentProp());
	}
}
