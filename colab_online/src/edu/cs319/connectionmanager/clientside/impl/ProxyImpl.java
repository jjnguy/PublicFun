package edu.cs319.connectionmanager.clientside.impl;

import java.io.IOException;
import java.net.Socket;

import edu.cs319.client.IClient;
import edu.cs319.connectionmanager.clientside.ClientDecoder;
import edu.cs319.connectionmanager.clientside.Proxy;
import edu.cs319.connectionmanager.clientside.ServerEncoder;
import edu.cs319.server.IServer;

public class ProxyImpl implements Proxy {

	private IServer server;
	private ClientDecoder client;
	private Socket socket;

	public ProxyImpl(Socket socket, IClient actualClient) throws IOException {
		this.socket = socket;
		this.server = new ServerEncoder(socket.getOutputStream());
		this.client = new ClientDecoder(actualClient, socket.getInputStream());
	}

	public IServer getServer() {
		return server;
	}

	public void close() throws IOException {
			socket.close();
	}

}
