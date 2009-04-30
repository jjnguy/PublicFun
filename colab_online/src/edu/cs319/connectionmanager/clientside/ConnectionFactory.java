package edu.cs319.connectionmanager.clientside;

import edu.cs319.client.IClient;
import edu.cs319.connectionmanager.clientside.impl.ConnectionFactoryLocal;
import edu.cs319.connectionmanager.clientside.impl.ConnectionFactoryNetworked;
import edu.cs319.server.Server;

/**
 * Creates Proxies for connecting to an IServer
 **/
public abstract class ConnectionFactory {

	/**
	 * Retrieves a ConnectionFactory which can interface with a locally running IServer, if present
	 * 
	 * @param A ConnectionFactory for creating Proxy's to a local instance of IServer.
	 **/
	public static ConnectionFactory getLocalInstance() {
		return new ConnectionFactoryLocal(Server.getInstance());
	}
	
	/**
	 * Retrieves a ConnectionFactory which creates Proxies for contacting an IServer over a network.
	 * 
	 * @param A ConnectionFactory for creating network Proxies.
	 **/
	public static ConnectionFactory getNetworkedInstance() {
		return new ConnectionFactoryNetworked();
	}

	/**
	 * Connect to an IServer
	 * 
	 * @param host The IP address of the IServer, can be null for the LocalInstance factory
	 * @param port The port the IServer is listening for connections on, can be 0 for the LocalInstance factory
	 * @param actualClient A reference to the IClient creating the connection.
	 * @param clientName The connecting client's username
	 * @param password The connecting client's password as a SHA1 hashed byte array.
	 * 
	 * @return A Proxy between the calling client and the requested IServer
	 **/
	public abstract Proxy connect(String host, int port, IClient actualClient, String clientName, byte[] password);
	
	/**
	 * Create a user on an IServer
	 * 
	 * @param host The IP address of the IServer, can be null for the LocalInstance factory
	 * @param port The port the IServer is listening for connections on, can be 0 for the LocalInstance factory
	 * @param actualClient A reference to the IClient creating the connection.
	 * @param clientName The connecting client's proposed username
	 * @param password The connecting client's proposed password as a SHA1 hashed byte array.
	 *
	 * @return A Proxy between the calling client and the requested IServer. This can be closed as soon as a response from the Server is received.
	 **/
	public abstract Proxy createUser(String host, int port, IClient actualClient, String clientName, byte[] password);

}
