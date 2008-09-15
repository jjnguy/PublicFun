package edu.iastate.cs309.test.davidsTests.clientServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import edu.iastate.cs309.clientserverprotocol.ServerConnection;
import edu.iastate.cs309.clientserverprotocol.exceptions.FailedMessageException;
import edu.iastate.cs309.clientserverprotocol.exceptions.ServerClientIOException;
import edu.iastate.cs309.communication.PasswordHash;
import edu.iastate.cs309.communication.ServerProperties;
import edu.iastate.cs309.communication.TorrentProp;

/**
 * do some basic tests over a connection (from the client side)
 * @author sralmai
 *
 */
public class ClientRunner
{
	private static ServerConnection s = null;

	/**
	 * open a ServerConnection and send all possible messages 
	 * @param args
	 */
	public static void main(String[] args)
	{

		ClientStub clientStub = new ClientStub();

		try
		{
			PasswordHash pw = new PasswordHash(new byte[20]);
			s = new ServerConnection("127.0.0.1", 1432, pw, clientStub);
			
			sendNonTransferMsgs();
			
		}
		catch (ServerClientIOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void sendNonTransferMsgs() throws FailedMessageException
	{
		try
		{
			s.addTorrent("new torrent!", new FileInputStream( new File("TODO.txt")));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		s.changeServerProp(new ServerProperties());
		s.changeTorrentProp(23, new TorrentProp());
		s.getAllTorrents(3);
		s.getServerProp(3);
		s.getTorrentInfo(3, 03);
		s.getTorrentProp(234, 02);
		s.shutdown();
		s.transferTorrent(0, 212, new ArrayList<Integer>());
	}
}
