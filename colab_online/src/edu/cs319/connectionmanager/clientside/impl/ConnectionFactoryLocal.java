package edu.cs319.connectionmanager.clientside.impl;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.cs319.client.IClient;
import edu.cs319.server.IServer;
import edu.cs319.connectionmanager.clientside.ConnectionFactory;
import edu.cs319.connectionmanager.clientside.Proxy;

public class ConnectionFactoryLocal extends ConnectionFactory {

	private IServer actualServer;

	public ConnectionFactoryLocal(IServer actualServer) {
		this.actualServer = actualServer;
	}

	public Proxy connect(String host, int port, IClient actualClient, String clientName) {
		return new ProxyImpl(actualServer, actualClient, clientName);
	}

	static private class ProxyImpl implements Proxy {

		private IServer server;

		public ProxyImpl(IServer actualServer, IClient actualClient, String clientName)  {
			
			this.server = actualServer;
			this.server.addNewClient(actualClient, clientName);
			
		}

		public IServer getServer() {
			return server;
		}

		public void close() throws IOException {
			// Nothing to close
		}

	}

}
