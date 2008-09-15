package edu.iastate.cs309.clientserverprotocol.transferThreads;

import java.io.IOException;
import java.io.InputStream;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import edu.iastate.cs309.clientserverprotocol.NetUtils;
import edu.iastate.cs309.clientserverprotocol.exceptions.FailedMessageException;
import edu.iastate.cs309.comminterfaces.IServer;
import edu.iastate.cs309.communication.PasswordHash;
import edu.iastate.cs309.util.Util;

/**
 * This spawns a thread to recieve a torrent file over a port. It will accept a file
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
public class RecieveTorrentThread implements Runnable
{
	/** hash for authentication */
	private PasswordHash pw = null;

	/** connection to listen on */
	SSLServerSocket sss = null;

	/** for handing the input stream to the server */
	IServer callback = null;

	/**
	 * Initialize a ftt
	 * 
	 * @param pw
	 *            password to authenticate with
	 * @param callback
	 *            callback to server
	 */
	public RecieveTorrentThread(IServer callback, PasswordHash pw)
	{
		this.callback = callback;
		this.pw = pw;
	}

	/**
	 * find a random unused port to bind on
	 * 
	 * @return the port the FileTransferThread connected on (or -1 on failure to
	 *         bind)
	 */
	public int bind()
	{
		/** get a server socket on some random port */
		try
		{
			sss = (SSLServerSocket) SSLServerSocketFactory.getDefault().createServerSocket(0);
	        String[] ciphers = {"SSL_DH_anon_WITH_RC4_128_MD5" };
	        sss.setEnabledCipherSuites( ciphers);
		}
		catch (IOException e)
		{
			return -1;
		}
		return sss.getLocalPort();
	}

	/**
	 * start listening and do the actual transfer
	 */
	public void run()
	{
		try
		{
			/** get authenticated peer */
			InputStream in = connect();
			
			/** read torrent filename */
			String name = getName(in);
			
			/** pass on to the overlord */
			callback.addTorrent(name, in);

		}
		catch (IOException e)
		{
			if(Util.DEBUG)
			{
				e.printStackTrace();
			}
		}
		catch (FailedMessageException e)
		{
			if(Util.DEBUG)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * get the torrent name
	 * 
	 * @return the name of the torrent file
	 * @throws IOException
	 */
	private String getName(InputStream in) throws IOException
	{
		int len = NetUtils.readInt(in);

		byte[] rawName = new byte[len];
		NetUtils.readFully(in, rawName, 0, rawName.length);

		return new String(rawName, "UTF8");
	}
	
	/**
	 * validate and return input stream of connected socket
	 * @return inputstream connected to client (or null on authentication error) 
	 * @throws IOException 
	 */
	private InputStream connect() throws IOException
	{
		SSLSocket s = (SSLSocket) sss.accept();
		sss.close();
        String[] ciphers = {"SSL_DH_anon_WITH_RC4_128_MD5" };
        s.setEnabledCipherSuites( ciphers);
        
        InputStream in = s.getInputStream();
        byte[] rawPw = new byte[20];
        
        NetUtils.readFully(in, rawPw, 0, 20);
        
        if( pw.equals(new PasswordHash(rawPw)))
        		return in;
        else
        {
        	in.close();
        	s.close();
        	return null;
        }
	}
}
