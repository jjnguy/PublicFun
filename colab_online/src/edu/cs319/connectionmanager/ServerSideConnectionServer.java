package edu.cs319.connectionmanager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import edu.cs319.server.Server;
import edu.cs319.util.Util;

public class ServerSideConnectionServer implements Runnable {
	private Server actualServer;
	public static final int DEFAULT_PORT = 112886;

	public ServerSideConnectionServer(Server actualServer) {
		this.actualServer = actualServer;
	}

	@Override
	public void run() {
		while (true) {
			ServerSocket serverSOck = null;
			try {
				serverSOck = new ServerSocket(DEFAULT_PORT);
				Socket s = serverSOck.accept();
				// TODO implement a queue of these things, need to decode them in order
				(new DecodeMessage(s)).start();
			} catch (IOException e1) {
				if (Util.DEBUG) {
					e1.printStackTrace();
				}
			}
		}
	}

	private class DecodeMessage extends Thread {
		private Socket s;

		public DecodeMessage(Socket s) {
			this.s = s;
		}

		public void decodeMessage() {
			// needs to interpret message format and decide what method to call on the server
		}

		@Override
		public void run() {
			decodeMessage();
		}
	}
}
