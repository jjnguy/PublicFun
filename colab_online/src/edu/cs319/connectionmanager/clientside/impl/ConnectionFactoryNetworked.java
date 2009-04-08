package edu.cs319.connectionmanager.clientside.impl;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.cs319.client.IClient;
import edu.cs319.server.IServer;

import edu.cs319.connectionmanager.clientside.ConnectionFactory;
import edu.cs319.connectionmanager.clientside.Proxy;
import edu.cs319.connectionmanager.clientside.ClientDecoder;
import edu.cs319.connectionmanager.clientside.ServerEncoder;

public class ConnectionFactoryNetworked extends ConnectionFactory {

	public Proxy connect(String host, int port, IClient actualClient, String clientName) {
		Proxy p = null;
		try {
			p = new ProxyImpl(new Socket(host, port), actualClient, clientName);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return p;
	}

	static private class ProxyImpl implements Proxy {

		private IServer server;
		private ClientDecoder client;
		private Socket socket;

		public ProxyImpl(Socket socket, IClient actualClient, String clientName) throws IOException {
			this.socket = socket;
			this.server = new ServerEncoder(socket.getOutputStream());
			this.server.addNewClient(null, clientName);
			this.client = new ClientDecoder(actualClient, socket.getInputStream());
			// TODO make sure we don't need reference
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
