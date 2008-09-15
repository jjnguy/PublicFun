package edu.iastate.cs309.clientserverprotocol.transferThreads;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import edu.iastate.cs309.clientserverprotocol.NetUtils;
import edu.iastate.cs309.clientserverprotocol.UserToken;
import edu.iastate.cs309.util.Util;

/**
 * This spawns a thread to send a file over a port. It will accept a file
 * from the first connection, which must authenticate with the 20 byte password
 * hash.
 * 
 * The transfer system has marginal security, but a fast attacker could at least
 * block file transfers from occurring by connecting on new ports before the
 * authorized client does. The thread would close the connection after 20 bytes
 * of garbage were sent.
 * 
 * @author sralmai
 */
public class SendTorrentThread implements Runnable
{
	/** password and server address */
	UserToken ut = null;
	
	/** name of torrent file */
	String name = null;
	
	/** input stream of file to send */
	InputStream in = null;
	
	/** port RecieveTorrentThread is waiting on */
	int port = 0;

	
	/**
	 * Initialize a ftt
	 * 
	 * @param ut
	 *            connection info
	 * @param name file name of torrent
	 * @param file InputStream to send 
	 * @param port to connect on
	 */
	public SendTorrentThread(String name, InputStream file, UserToken ut, int port)
	{
		this.in = file;
		this.name = name;
		this.ut = ut;
		this.port = port;
	}

	/**
	 * start listening and do the actual transfer
	 */
	public void run()
	{
		try
		{
			SSLSocket s = connect();
			OutputStream out = s.getOutputStream();

			/** authenticate */
			out.write(ut.getPwHash().getBytes());

			/** send header */
			sendName( out);
			
			/** send the file */
			NetUtils.transfer(in, out);
			
			/** clean up */
			in.close();
			out.close();
			s.close();
		}
		catch (IOException e)
		{
			if(Util.DEBUG)
			{
				e.printStackTrace();
			}
			System.err.println("transferring torrent failed");
		}
	}

	/**
	 * send the torrent name
	 * @throws IOException
	 */
	private void sendName( OutputStream out) throws IOException
	{
		byte[] rawString = name.getBytes("UTF8");
		byte[] msg = new byte[ 4 + rawString.length];
		
		/** this is ugly... shouldn't need to arraycopy */
		System.arraycopy(rawString, 0, msg, 4, rawString.length);
		
		NetUtils.intToBytes(rawString.length, msg, 0);
		
		out.write(msg);
	}
	
	/**
	 * Connect and return the SSLSocket
	 * @return connected socket
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	private SSLSocket connect() throws UnknownHostException, IOException
	{
        SocketFactory socketFactory = SSLSocketFactory.getDefault();
        SSLSocket s = (SSLSocket)socketFactory.createSocket(ut.getHost(), port);
        String[] ciphers = {"SSL_DH_anon_WITH_RC4_128_MD5" };
        s.setEnabledCipherSuites( ciphers);
        
        return s;
	}
}
