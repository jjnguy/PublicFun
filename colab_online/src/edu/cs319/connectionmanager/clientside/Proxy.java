package edu.cs319.connectionmanager.clientside;

import java.io.Closeable;

public interface Proxy extends Closeable {

	public IServer getServer();

}
