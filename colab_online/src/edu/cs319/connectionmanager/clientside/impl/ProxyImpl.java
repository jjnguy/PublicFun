package edu.cs319.connectionmanager.clientside.impl;

import edu.cs319.connectionmanager.clientside.Proxy;

import edu.cs319.client.IClient;
import edu.cs319.server.IServer;

import 


public class ProxyImpl implements Proxy {

	private IServer server;
	private IClient client;
	private Socket socket;

	public ProxyImpl(Socket socket, IClient actualClient) {
		this.socket = socket;
		this.server = new ServerEncoder(socket.getOutputStream());
		this.client = new ClientDecoder(socket.getInputStream(),actualClient);
	}

	public IServer getServer() {
		return server;
	}

	public void close() throws IOException {
		try{
			socket.close();
		}
	}

}
