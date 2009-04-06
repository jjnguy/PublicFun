package edu.cs319.connectionmanager.serverside;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import edu.cs319.server.IServer;
import edu.cs319.util.Util;

public class ConnectionListener implements Closeable, Runnable {

	private ServerSocket ss;
	private IServer server;
	private boolean running;

	public ConnectionListener(IServer server, int port) throws IOException {
		this.server = server;
		this.running = true;
		ss = new ServerSocket(port);
	}

	public void close() throws IOException {
		ss.close();
		running = false;
	}

	private synchronized boolean isRunning() {
		return running;
	}

	public void run() {
		while (isRunning()) {
			try {
				new Thread(new ConnectionHandler(ss.accept())).start();
			} catch (IOException e) {
				if (Util.DEBUG) {
					e.printStackTrace();
				}
			}
		}
	}

	private class ConnectionHandler implements Runnable {

		private Socket socket;

		private ConnectionHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			new Thread(new ServerDecoder(server, socket)).start();
		}
	}
}
