package edu.cs319.connectionmanager.clientside;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.cs319.client.IClient;
import edu.cs319.server.IServer;
import edu.cs319.server.Server;

import edu.cs319.connectionmanager.clientside.impl.ConnectionFactoryLocal;
import edu.cs319.connectionmanager.clientside.impl.ConnectionFactoryNetworked;


public abstract class ConnectionFactory {

	public static ConnectionFactory getLocalInstance() {
		return new ConnectionFactoryLocal(Server.getInstance());
	}
	
	public static ConnectionFactory getNetworkedInstance() {
		return new ConnectionFactoryNetworked();
	}

	public abstract Proxy connect(String host, int port, IClient actualClient, String clientName);


}
