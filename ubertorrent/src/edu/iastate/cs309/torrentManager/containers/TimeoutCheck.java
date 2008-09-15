package edu.iastate.cs309.torrentManager.containers;

import java.util.Date;

/**
 * helper class for Peer
 * 
 * handles all keep-alive timing
 * 
 * @author sralmai
 * 
 */
public class TimeoutCheck
{
	/** message counts */
	private int numSent = 0;
	private int numRecv = 0;
	
	/** time of last messages */
	private long sentMsg = 0;
	private long recvMsg = 0;

	private long timeOut = 0;
	private long minSend = 0;

	/**
	 * 
	 * @param timeOut
	 *            how long after last gotMsg() util timeOut() returns true (in
	 *            milliseconds)
	 * @param minSend
	 *            how long after last sentMsg until sendKeepAlive() returns true
	 *            (in milliseconds)
	 */
	public TimeoutCheck(long timeOut, long minSend)
	{
		this.timeOut = timeOut;
		this.minSend = minSend;
		
		/* can't see into the past, so initialize to sane values */
		sentMsg = recvMsg = new Date().getTime();
	}

	/** call whenever a message is sent */
	public void sentMsg()
	{
		sentMsg = new Date().getTime();
		++numSent;
	}

	/** call whever a message is recieved */
	public void gotMsg()
	{
		recvMsg = new Date().getTime();
		++numRecv;
	}

	/**
	 * Check if it has been too long since recieving a message.
	 * 
	 * @return true if the Peer object should drop the connection
	 */
	public boolean timedOut()
	{
		return (new Date().getTime()) > (recvMsg + timeOut);
	}

	/**
	 * Check if a keep-alive message should be sent.
	 * @return true if a keep-alive should be sent
	 */
	public boolean sendKeepAlive()
	{
		return (new Date().getTime()) > (sentMsg + minSend);
	}
	
	/**
	 * @return the total number of messages sent
	 */
	public int getNumSent()
	{
		return numSent;
	}
	
	
	/**
	 * @return the total number of messages recieved
	 */
	public int getNumRecv()
	{
		return numRecv;
	}
}
