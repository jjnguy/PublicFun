package edu.iastate.cs309.test.davidsTests.clientServer;

import java.io.InputStream;
import java.util.List;

import edu.iastate.cs309.clientserverprotocol.exceptions.FailedMessageException;
import edu.iastate.cs309.comminterfaces.IServer;
import edu.iastate.cs309.communication.ServerProperties;
import edu.iastate.cs309.communication.TorrentProp;

/**
 * simply dump recieved messages to stdout
 * @author sralmai
 *
 */
public class ServerStub implements IServer
{
	public void changeServerProp(ServerProperties setting) throws FailedMessageException
	{
		System.out.println("ServerStub recieved changeServerProp message");

	}

	public void changeTorrentProp(int refID, TorrentProp prop) throws FailedMessageException
	{
		System.out.println("ServerStub recieved changeTorrentProp for refID: "+refID);

	}

	public void getAllTorrents(int clientID) throws FailedMessageException
	{
		System.out.println("ServerStub recieved getAllTorrents from clientID: "+clientID);

	}

	public void getServerProp(int clientID) throws FailedMessageException
	{
		System.out.println("ServerStub recieved getServerProp for clientID: "+clientID);

	}

	public void getTorrentInfo(int clientID, int refID) throws FailedMessageException
	{
		System.out.println("ServerStub recieved getTorrentInfo from clientID: "+clientID+" for refID: "+refID);

	}

	public void getTorrentProp(int clientID, int refID) throws FailedMessageException
	{
		System.out.println("ServerStub recieved getTorrentProp from clientID: "+clientID+" for refID: "+refID);

	}

	public void transferTorrent(int clientID, int refID, List<Integer> fileIndex) throws FailedMessageException
	{
		System.out.println("ServerStub recieved transferTorrent from clientID: "+clientID+" refID: "+refID+" file indices: " +fileIndex.toString());

	}

	public void addTorrent(String name, InputStream dotTorrentFile) throws FailedMessageException
	{
		System.out.println("ServerStub recieved addTorrent name: "+name);
		
	}

	public void shutdown() throws FailedMessageException
	{
		System.out.println("ServerStub got shutdown message!");
		
	}

	public void removeTorrent(int refID) throws FailedMessageException
	{
		System.out.println("ServerStub got removeTorrent() message. refID: " + refID);
		
	}

}
