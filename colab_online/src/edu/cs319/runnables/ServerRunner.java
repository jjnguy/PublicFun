package edu.cs319.runnables;

import edu.cs319.connectionmanager.ServerSideConnectionServer;
import edu.cs319.server.Server;

public class ServerRunner {

	public static void main(String[] args) {
		ServerSideConnectionServer serv = new ServerSideConnectionServer(Server.getInstance());
		serv.run();
	}
}
