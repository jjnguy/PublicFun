package edu.iastate.cs309.test.davidsTests.fakeServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import edu.iastate.cs309.torrentManager.Peer;
import edu.iastate.cs309.torrentManager.Torrent;
import edu.iastate.cs309.torrentManager.containers.HandshakeInfo;
import edu.iastate.cs309.torrentManager.exceptions.PeerDeadException;

/**
 * Set up a server socket to pass inbound connections to TorrentDriver
 * 
 * @author sralmai
 * 
 */
public class FakeServer implements Runnable
{
	private int port = 0;
	private Torrent callback = null;

	/**
	 * 
	 * @param port
	 *            to listen on
	 * @param callback
	 *            Torrent to call
	 */
	public FakeServer(int port, Torrent callback)
	{
		this.port = port;
		this.callback = callback;
	}

	/** start this ship going */
	public void run()
	{
		ServerSocket ss = null;
		Socket s = null;
		HandshakeInfo h = null;

		try
		{
			ss = new ServerSocket(port);
		}
		catch (IOException e)
		{
			System.err.println("FakeServer died! : " + e.getMessage());
			System.exit(1);
		}

		while (true)
		{
			try
			{
				s = ss.accept();
				h = Peer.getHandshake(s);
				/**
				 * normally would find out which torrent to roll with by
				 * HandshakeInfo
				 */
				callback.addPeer(s, h.getPeerID());
			}
			catch (PeerDeadException e)
			{
				System.err.println("Peer died trying to connect: " + e.getMessage());
				e.printStackTrace();
			}
			catch (IOException e)
			{
				System.err.println("FakeServer died! : " + e.getMessage());
				e.printStackTrace();
				System.exit(1);
			}

		}

	}

}
