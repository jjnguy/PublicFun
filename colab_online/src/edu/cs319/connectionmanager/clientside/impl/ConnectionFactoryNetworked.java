package edu.cs319.connectionmanager.clientside.impl;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.cs319.client.IClient;
import edu.cs319.connectionmanager.clientside.ClientDecoder;
import edu.cs319.connectionmanager.clientside.ConnectionFactory;
import edu.cs319.connectionmanager.clientside.Proxy;
import edu.cs319.connectionmanager.clientside.ServerEncoder;
import edu.cs319.server.IServer;
import edu.cs319.util.Util;

public class ConnectionFactoryNetworked extends ConnectionFactory {

	@Override
	public Proxy connect(String host, int port, IClient actualClient, String clientName,
			byte[] password) {
		try {
			return new ProxyImpl(new Socket(host, port), actualClient, clientName, password);
		} catch (IOException e) {
			if (Util.DEBUG) {
				System.out.println("Error connecting to server");
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void createUser(String host, int port, IClient actualClient, String clientName,
			byte[] password) {
		try {
			Proxy p = new ProxyImpl(new Socket(host, port), actualClient, clientName, password);
			p.getServer().createUser(null, clientName, password);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static private class ProxyImpl implements Proxy {

		private IServer server;
		private ClientDecoder client;
		private Socket socket;

		public ProxyImpl(Socket socket, IClient actualClient, String clientName, byte[] password)
				throws IOException {
			this.socket = socket;
			this.server = new ServerEncoder(socket.getOutputStream());
			this.server.authenticateUser(null,clientName, password);
			this.client = new ClientDecoder(actualClient, socket.getInputStream());
			new Thread(client).start();
		}

		public IServer getServer() {
			return server;
		}

		public void close() throws IOException {
			socket.close();
		}
	}
}
