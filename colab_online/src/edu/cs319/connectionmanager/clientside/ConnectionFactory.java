package edu.cs319.connectionmanager.clientside;

import edu.cs319.connectionmanager.clientside.impl.ProxyImpl;
import edu.cs319.client.IClient;

import java.net.Socket;


public class ConnectionFactory {

	public static Proxy connect(String host, int port, IClient actualClient) {

		return new ProxyImpl(new Socket(host,port), actualClient);
	}
}

