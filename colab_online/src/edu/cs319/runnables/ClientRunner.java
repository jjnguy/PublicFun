package edu.cs319.runnables;

import edu.cs319.connectionmanager.clientside.ClientSideConnectionClient;

public class ClientRunner {
	public static void main(String[] args) {
		// TODO client instance
		ClientSideConnectionClient cli = new ClientSideConnectionClient(null);
		cli.run();
	}
}
