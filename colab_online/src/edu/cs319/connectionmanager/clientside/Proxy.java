package edu.cs319.connectionmanager.clientside;

import java.io.Closeable;

import edu.cs319.server.IServer;

public interface Proxy extends Closeable {

	public IServer getServer();

}
