package edu.cs319.connectionmanager.clientside;

import edu.cs319.client.IClient;
import edu.cs319.connectionmanager.clientside.impl.ConnectionFactoryLocal;
import edu.cs319.connectionmanager.clientside.impl.ConnectionFactoryNetworked;
import edu.cs319.server.Server;


public abstract class ConnectionFactory {

	public static ConnectionFactory getLocalInstance() {
		return new ConnectionFactoryLocal(Server.getInstance());
	}
	
	public static ConnectionFactory getNetworkedInstance() {
		return new ConnectionFactoryNetworked();
	}

	public abstract Proxy connect(String host, int port, IClient actualClient, String clientName);


}
