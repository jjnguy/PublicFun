package edu.iastate.cs309.test.davidsTests.fakeTracker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Single threaded tracker for fake.torrent torrent file in this directory.
 * Follows the tracker spec, but sends out phony data.
 * 
 * No
 * 
 * @author sralmai
 * 
 */
public class FakeTracker implements Runnable

{
	/** die flag */
	boolean timeToDie = false;

	/** for the single connection */
	private InputStream in = null;
	private OutputStream out = null;

	/** response file */
	File resp = null;

	/**
	 * Start a new tracker as a daemon
	 * 
	 * @return a reference to the new tracker
	 * @throws IOException
	 */
	public static FakeTracker startDaemon() throws IOException
	{
		FakeTracker t = new FakeTracker();

		new Thread(t).start();

		// Horrible code... hope that daemon starts before returning
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			// don't care
		}

		return t;
	}

	/**
	 * Stop a running tracker
	 */
	public void stop()
	{
		timeToDie = true;
	}

	/**
	 * create a tracker
	 * 
	 * @throws IOException
	 */
	private FakeTracker() throws IOException
	{
		/** set up default file location */
		resp = new File(System.getProperty("user.dir"), "edu/iastate/cs309/test/fakeTracker/server.response");
	}

	/**
	 * read request input
	 * 
	 * @throws IOException
	 */
	private byte[] readInput() throws IOException
	{
		byte[] input = new byte[100];

		int maxSize = in.available();

		if (maxSize > input.length)
			maxSize = input.length;

		in.read(input, 0, maxSize);

		return input;
	}

	/**
	 * return the bencoded response to the client (from file server.response)
	 * 
	 * @throws IOException
	 */
	private void sendResponse(byte[] warning) throws IOException
	{
		byte[] msg = new byte[1000];

		FileInputStream fRead = new FileInputStream(resp);
		int length = fRead.read(msg);
		out.write(msg, 0, length);

		fRead.close();
	}

	/**
	 * start the server on a thread
	 */
	public void run()
	{
		try
		{
			/** the port is specified in fake.torrent */
			ServerSocket ss = new ServerSocket(30908);

			Socket s = null;

			while (!timeToDie)
			{
				s = ss.accept();
				System.err.println("FakeTracker accepted connection");
				in = s.getInputStream();
				out = s.getOutputStream();

				sendResponse(readInput());
			}
		}
		catch (IOException e)
		{
			System.err.println("Caught exception: " + e.getMessage());
		}
	}

}
