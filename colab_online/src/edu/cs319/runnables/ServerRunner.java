package edu.cs319.runnables;

import edu.cs319.connectionmanager.serverside.ServerDecoder;
import edu.cs319.server.Server;

public class ServerRunner {

	public static void main(String[] args) {
		ServerDecoder serv = new ServerDecoder(Server.getInstance());
		serv.run();
	}
}
