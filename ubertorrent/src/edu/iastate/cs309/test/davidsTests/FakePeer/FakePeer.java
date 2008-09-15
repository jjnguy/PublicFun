package edu.iastate.cs309.test.davidsTests.FakePeer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Simulates a peer with all pieces of the fake.torrent download.
 * 
 * Listens on port 30909 as defined in fake.torrent
 * @author sralmai
 *
 */
public class FakePeer implements Runnable
{
	private boolean timeToDie = false;
	private InputStream in = null;
	private OutputStream out = null;
	
	public FakePeer()
	{
		// empty
	}
	
	public void run()
	{
		try{
		/** the port is specified in fake.torrent */
		ServerSocket ss = new ServerSocket( 30909);
		
		Socket s = null;
		
		while( ! timeToDie)
		{
			s = ss.accept();
			System.err.println("FakePeer accepted connection");
			in = s.getInputStream();
			out = s.getOutputStream();
			
			sendResponse( readInput());
		}

		}
		catch(IOException e)
		{
			// do something
		}
	}

	private void sendResponse(Object object)
	{
		// TODO Auto-generated method stub
		
	}

	private Object readInput()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
