package edu.cs319.connectionmanager.clientside;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.cs319.client.IClient;
import edu.cs319.server.IServer;

public class ConnectionFactory {

	public static Proxy connect(String host, int port, IClient actualClient, String clientName)
			throws UnknownHostException, IOException {
		return new ProxyImpl(new Socket(host, port), actualClient, clientName);
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
