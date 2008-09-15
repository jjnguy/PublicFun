/**
 * 
 */
package edu.iastate.cs309.torrentManager.socketMeter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;



/**
 * 
 * 
 * @author Michael Seibert
 */
public class ThrottledSocket extends Socket
{
	private final Socket throttled;
	private MeteredOutputStream outputStream;
	private MeteredInputStream inputStream;
	private int targetUpload = Integer.MAX_VALUE;
	private int targetDownload = Integer.MAX_VALUE;

	/**
	 * @param other
	 *            The socket to throttle
	 */
	public ThrottledSocket(Socket other)
	{
		throttled = other;
	}

	/**
	 * @param bindpoint
	 * @throws IOException
	 * @see java.net.Socket#bind(java.net.SocketAddress)
	 */
	@Override
	public void bind(SocketAddress bindpoint) throws IOException
	{
		throttled.bind(bindpoint);
	}

	/**
	 * @throws IOException
	 * @see java.net.Socket#close()
	 */
	@Override
	public void close() throws IOException
	{
		throttled.close();
	}

	/**
	 * @param endpoint
	 * @param timeout
	 * @throws IOException
	 * @see java.net.Socket#connect(java.net.SocketAddress, int)
	 */
	@Override
	public void connect(SocketAddress endpoint, int timeout) throws IOException
	{
		throttled.connect(endpoint, timeout);
	}

	/**
	 * @param endpoint
	 * @throws IOException
	 * @see java.net.Socket#connect(java.net.SocketAddress)
	 */
	@Override
	public void connect(SocketAddress endpoint) throws IOException
	{
		throttled.connect(endpoint);
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		return throttled.equals(obj);
	}

	/**
	 * @return
	 * @see java.net.Socket#getChannel()
	 */
	@Override
	public SocketChannel getChannel()
	{
		return throttled.getChannel();
	}

	/**
	 * @return
	 * @see java.net.Socket#getInetAddress()
	 */
	@Override
	public InetAddress getInetAddress()
	{
		return throttled.getInetAddress();
	}

	/**
	 * @return
	 * @throws IOException
	 * @see java.net.Socket#getInputStream()
	 */
	@Override
	public MeteredInputStream getInputStream() throws IOException
	{
		if (inputStream == null)
			inputStream = new MeteredInputStream(new NotifierInputStream(throttled.getInputStream(), this));
		return inputStream;
	}

	/**
	 * @return
	 * @throws SocketException
	 * @see java.net.Socket#getKeepAlive()
	 */
	@Override
	public boolean getKeepAlive() throws SocketException
	{
		return throttled.getKeepAlive();
	}

	/**
	 * @return
	 * @see java.net.Socket#getLocalAddress()
	 */
	@Override
	public InetAddress getLocalAddress()
	{
		return throttled.getLocalAddress();
	}

	/**
	 * @return
	 * @see java.net.Socket#getLocalPort()
	 */
	@Override
	public int getLocalPort()
	{
		return throttled.getLocalPort();
	}

	/**
	 * @return
	 * @see java.net.Socket#getLocalSocketAddress()
	 */
	@Override
	public SocketAddress getLocalSocketAddress()
	{
		return throttled.getLocalSocketAddress();
	}

	/**
	 * @return
	 * @throws SocketException
	 * @see java.net.Socket#getOOBInline()
	 */
	@Override
	public boolean getOOBInline() throws SocketException
	{
		return throttled.getOOBInline();
	}

	/**
	 * @return
	 * @throws IOException
	 * @see java.net.Socket#getOutputStream()
	 */
	@Override
	public MeteredOutputStream getOutputStream() throws IOException
	{
		if (outputStream == null)
			outputStream = new MeteredOutputStream(new NotifierOutputStream(throttled.getOutputStream(), this));
		return outputStream;
	}

	/**
	 * @return
	 * @see java.net.Socket#getPort()
	 */
	@Override
	public int getPort()
	{
		return throttled.getPort();
	}

	/**
	 * @return
	 * @throws SocketException
	 * @see java.net.Socket#getReceiveBufferSize()
	 */
	@Override
	public int getReceiveBufferSize() throws SocketException
	{
		return throttled.getReceiveBufferSize();
	}

	/**
	 * @return
	 * @see java.net.Socket#getRemoteSocketAddress()
	 */
	@Override
	public SocketAddress getRemoteSocketAddress()
	{
		return throttled.getRemoteSocketAddress();
	}

	/**
	 * @return
	 * @throws SocketException
	 * @see java.net.Socket#getReuseAddress()
	 */
	@Override
	public boolean getReuseAddress() throws SocketException
	{
		return throttled.getReuseAddress();
	}

	/**
	 * @return
	 * @throws SocketException
	 * @see java.net.Socket#getSendBufferSize()
	 */
	@Override
	public int getSendBufferSize() throws SocketException
	{
		return throttled.getSendBufferSize();
	}

	/**
	 * @return
	 * @throws SocketException
	 * @see java.net.Socket#getSoLinger()
	 */
	@Override
	public int getSoLinger() throws SocketException
	{
		return throttled.getSoLinger();
	}

	/**
	 * @return
	 * @throws SocketException
	 * @see java.net.Socket#getSoTimeout()
	 */
	@Override
	public int getSoTimeout() throws SocketException
	{
		return throttled.getSoTimeout();
	}

	/**
	 * @return
	 * @throws SocketException
	 * @see java.net.Socket#getTcpNoDelay()
	 */
	@Override
	public boolean getTcpNoDelay() throws SocketException
	{
		return throttled.getTcpNoDelay();
	}

	/**
	 * @return
	 * @throws SocketException
	 * @see java.net.Socket#getTrafficClass()
	 */
	@Override
	public int getTrafficClass() throws SocketException
	{
		return throttled.getTrafficClass();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return throttled.hashCode();
	}

	/**
	 * @return
	 * @see java.net.Socket#isBound()
	 */
	@Override
	public boolean isBound()
	{
		return throttled.isBound();
	}

	/**
	 * @return
	 * @see java.net.Socket#isClosed()
	 */
	@Override
	public boolean isClosed()
	{
		return throttled.isClosed();
	}

	/**
	 * @return
	 * @see java.net.Socket#isConnected()
	 */
	@Override
	public boolean isConnected()
	{
		return throttled.isConnected();
	}

	/**
	 * @return
	 * @see java.net.Socket#isInputShutdown()
	 */
	@Override
	public boolean isInputShutdown()
	{
		return throttled.isInputShutdown();
	}

	/**
	 * @return
	 * @see java.net.Socket#isOutputShutdown()
	 */
	@Override
	public boolean isOutputShutdown()
	{
		return throttled.isOutputShutdown();
	}

	/**
	 * @param data
	 * @throws IOException
	 * @see java.net.Socket#sendUrgentData(int)
	 */
	@Override
	public void sendUrgentData(int data) throws IOException
	{
		throttled.sendUrgentData(data);
	}

	/**
	 * @param on
	 * @throws SocketException
	 * @see java.net.Socket#setKeepAlive(boolean)
	 */
	@Override
	public void setKeepAlive(boolean on) throws SocketException
	{
		throttled.setKeepAlive(on);
	}

	/**
	 * @param on
	 * @throws SocketException
	 * @see java.net.Socket#setOOBInline(boolean)
	 */
	@Override
	public void setOOBInline(boolean on) throws SocketException
	{
		throttled.setOOBInline(on);
	}

	/**
	 * @param connectionTime
	 * @param latency
	 * @param bandwidth
	 * @see java.net.Socket#setPerformancePreferences(int, int, int)
	 */
	@Override
	public void setPerformancePreferences(int connectionTime, int latency, int bandwidth)
	{
		throttled.setPerformancePreferences(connectionTime, latency, bandwidth);
	}

	/**
	 * @param size
	 * @throws SocketException
	 * @see java.net.Socket#setReceiveBufferSize(int)
	 */
	@Override
	public void setReceiveBufferSize(int size) throws SocketException
	{
		throttled.setReceiveBufferSize(size);
	}

	/**
	 * @param on
	 * @throws SocketException
	 * @see java.net.Socket#setReuseAddress(boolean)
	 */
	@Override
	public void setReuseAddress(boolean on) throws SocketException
	{
		throttled.setReuseAddress(on);
	}

	/**
	 * @param size
	 * @throws SocketException
	 * @see java.net.Socket#setSendBufferSize(int)
	 */
	@Override
	public void setSendBufferSize(int size) throws SocketException
	{
		throttled.setSendBufferSize(size);
	}

	/**
	 * @param on
	 * @param linger
	 * @throws SocketException
	 * @see java.net.Socket#setSoLinger(boolean, int)
	 */
	@Override
	public void setSoLinger(boolean on, int linger) throws SocketException
	{
		throttled.setSoLinger(on, linger);
	}

	/**
	 * @param timeout
	 * @throws SocketException
	 * @see java.net.Socket#setSoTimeout(int)
	 */
	@Override
	public void setSoTimeout(int timeout) throws SocketException
	{
		throttled.setSoTimeout(timeout);
	}

	/**
	 * @param on
	 * @throws SocketException
	 * @see java.net.Socket#setTcpNoDelay(boolean)
	 */
	@Override
	public void setTcpNoDelay(boolean on) throws SocketException
	{
		throttled.setTcpNoDelay(on);
	}

	/**
	 * @param tc
	 * @throws SocketException
	 * @see java.net.Socket#setTrafficClass(int)
	 */
	@Override
	public void setTrafficClass(int tc) throws SocketException
	{
		throttled.setTrafficClass(tc);
	}

	/**
	 * @throws IOException
	 * @see java.net.Socket#shutdownInput()
	 */
	@Override
	public void shutdownInput() throws IOException
	{
		throttled.shutdownInput();
	}

	/**
	 * @throws IOException
	 * @see java.net.Socket#shutdownOutput()
	 */
	@Override
	public void shutdownOutput() throws IOException
	{
		throttled.shutdownOutput();
	}

	/**
	 * @return
	 * @see java.net.Socket#toString()
	 */
	@Override
	public String toString()
	{
		return throttled.toString();
	}

	/**
	 * 
	 */
	public void updateDownloadSpeed()
	{
		if (inputStream.timeSinceLastUpdate() / MeteredInputStream.NANOS_PER_SECOND > 10)
		{
			try
			{
				int speed = inputStream.speed();
				if (speed < targetDownload)
					setReceiveBufferSize(Math.min(getReceiveBufferSize() << 2, 1 << 15));
				else
					setReceiveBufferSize(Math.max(getReceiveBufferSize() >> 2, 128));
			}
			catch (SocketException e)
			{
			}
		}
	}

	/**
	 * 
	 */
	public void updateUploadSpeed()
	{
		if (outputStream.timeSinceLastUpdate() / MeteredOutputStream.NANOS_PER_SECOND > 10)
		{
			try
			{
				int speed = outputStream.speed();
				if (speed < targetUpload)
					setSendBufferSize(Math.min(getReceiveBufferSize() << 2, 1 << 15));
				else
					setSendBufferSize(Math.max(getReceiveBufferSize() >> 2, 128));
			}
			catch (SocketException e)
			{
			}
		}
	}

	/**
	 * @param targetUpload
	 *            The new targetUpload.
	 */
	public void setTargetUpload(int targetUpload)
	{
		this.targetUpload = targetUpload;
	}

	/**
	 * @param targetDownload
	 *            The new targetDownload.
	 */
	public void setTargetDownload(int targetDownload)
	{
		this.targetDownload = targetDownload;
	}
}
