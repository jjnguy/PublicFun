package edu.cs319.connectionmanager.clientside;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.cs319.client.IClient;
import edu.cs319.connectionmanager.clientside.impl.ProxyImpl;


public class ConnectionFactory {

	public static Proxy connect(String host, int port, IClient actualClient) throws UnknownHostException, IOException {
		return new ProxyImpl(new Socket(host,port), actualClient);
	}
}

