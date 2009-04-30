package edu.cs319.connectionmanager.clientside;

import java.io.Closeable;

import edu.cs319.server.IServer;

/**
 * A Proxy between an IClient and an IServer
 **/
public interface Proxy extends Closeable {

	/**
	 * Retrieves the IServer on the other end of this Proxy
	 *
	 * @return The IServer this Proxy is connected to.
	 **/
	public IServer getServer();

}
