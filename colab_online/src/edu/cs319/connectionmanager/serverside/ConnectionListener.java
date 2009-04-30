package edu.cs319.connectionmanager.serverside;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import edu.cs319.server.IServer;
import edu.cs319.util.Util;

/**
 * Listens for incoming network connections to an IServer.
 **/
public class ConnectionListener implements Closeable, Runnable {

	private ServerSocket ss;
	private IServer server;
	private boolean running;

	/**
	 * Creates a new ConnectionListener which will listen for connections to the given IServer on the given port
	 * 
	 * @param server The IServer this ConnectionListener will connect incoming connections to.
	 * @param port The port this ConnectionListener will listen for connections on.
	 **/
	public ConnectionListener(IServer server, int port) throws IOException {
		this.server = server;
		this.running = true;
		ss = new ServerSocket(port);
	}

	/**
	 * Closes this ConnectionListener
	 * 
	 * @throws IOException If something goes wrong while closing.
	 **/
	public void close() throws IOException {
		ss.close();
		running = false;
	}

	/**
	 * A synchronized method to tell whether this ConnectionListener is still running
	 * 
	 * @return Whether this ConnectionListener is still running.
	 **/
	private synchronized boolean isRunning() {
		return running;
	}

	/**
	 * Continuous loop listening for new connections and handling them
	 * Breaks after close() is called on this ConnectionListener
	 **/
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

	/**
	 * A Handler class for establishing new connections to the IServer
	 **/
	private class ConnectionHandler implements Runnable {

		private Socket socket;

		/**
		 * Creates a ConnectionHandler for the given Socket
		 * 
		 * @param socket The Socket for a new IServer connection
		 **/
		private ConnectionHandler(Socket socket) {
			this.socket = socket;
		}
	
		/**
		 * Establishes the connection between the IClient and IServer
		 **/
		public void run() {
			new Thread(new ServerDecoder(server, socket)).start();
		}
	}
}
