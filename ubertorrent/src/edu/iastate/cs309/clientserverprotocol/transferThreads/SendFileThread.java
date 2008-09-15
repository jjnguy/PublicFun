package edu.iastate.cs309.clientserverprotocol.transferThreads;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ClosedByInterruptException;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import edu.iastate.cs309.clientserverprotocol.NetUtils;
import edu.iastate.cs309.communication.PasswordHash;
import edu.iastate.cs309.torrentparser.ParseException;
import edu.iastate.cs309.util.Util;

/**
 * This thread sends a file from from the server to the first connection.
 * 
 * The transfer system has marginal security, but a fast attacker could at least
 * block file transfers from occurring by connecting on new ports before the
 * authorized client does. The thread would close the connection after 20 bytes
 * of garbage were sent.
 * 
 * @author sralmai
 */
public class SendFileThread implements Runnable
{
	/** hash for authentication */
	private PasswordHash pw = null;

	/** the file to send on connection */
	InputStream in = null;

	/** listening socket */
	SSLServerSocket sss = null;

	/** refID of of torrent we are passing */
	int refID = 0;

	/** file index of torrent we are passing */
	int index = 0;

	/**
	 * Initialize a ftt
	 * 
	 * @param refID
	 * @param index
	 * @param pw
	 *            password to authenticate with
	 * @param in
	 *            input stream to send down the pipe
	 */
	public SendFileThread(int refID, int index, InputStream in, PasswordHash pw)
	{
		if (in == null || pw == null)
			throw new NullPointerException("in: "+in+" pw: "+pw);
		this.pw = pw;
		this.in = in;
		this.refID = refID;
		this.index = index;
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
			String[] ciphers = { "SSL_DH_anon_WITH_RC4_128_MD5" };
			sss.setEnabledCipherSuites(ciphers);
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
			SSLSocket s = connect();

			/** bad password */
			if (s == null)
				return;

			OutputStream out = s.getOutputStream();

			/** send refID and file index */
			sendHeader(out);

			/** send the file */
			NetUtils.transfer(in, out);

			/** clean up */
			out.close();
			in.close();
			s.close();
		}
		catch (ClosedByInterruptException e)
		{
			System.err.println("caught interrupt... dying");
			return;
		}
		catch (IOException e)
		{
			System.err.println("error listening for client: " + e.getMessage());
			return;
		}
	}

	/**
	 * Send the refID and file index down the pipe
	 * 
	 * @param out
	 *            output stream to write to
	 * @throws IOException
	 */
	private void sendHeader(OutputStream out) throws IOException
	{
		byte[] header = new byte[8];

		NetUtils.intToBytes(refID, header, 0);
		NetUtils.intToBytes(index, header, 4);

		out.write(header);
	}

	/**
	 * initialize a connection
	 * 
	 * @param ss
	 * @return validated SSLSocket or null on failure
	 * @throws IOException
	 * @throws ParseException
	 */
	private SSLSocket connect() throws IOException
	{
		SSLSocket s = (SSLSocket) sss.accept();
		sss.close();

		InputStream sockIn = s.getInputStream();

		/** validate */
		byte[] rawPw = new byte[20];

		NetUtils.readFully(sockIn, rawPw, 0, 20);

		// test ignore
		//sockIn.close();

		if (pw.equals(new PasswordHash(rawPw)))
		{
			if (Util.DEBUG)
				System.out.println("recievefileThread authenticated!");
			return s;
		}
		else
		{
			if (Util.DEBUG)
				System.out.println("SendFileThread rejected a connection: bad password");
			s.close();
			in.close();
			return null;
		}
	}
}
