package edu.cs319.connectionmanager;

import edu.cs319.server.Server;

public class ServerSideConnectionServer {
	private Server actualServer;

	public ServerSideConnectionServer(Server actualServer) {
		this.actualServer = actualServer;
	}

	public void decodeMessage(String message){
		
	}
	
	public void start() {
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
