package edu.cs319.runnables;


import java.io.IOException;

import edu.cs319.connectionmanager.serverside.ConnectionListener;
import edu.cs319.server.Server;

public class ServerRunner {

	public static void main(String[] args) throws IOException {
		ConnectionListener l = new ConnectionListener(Server.getInstance(), 4444);
		l.run();
	}
}
