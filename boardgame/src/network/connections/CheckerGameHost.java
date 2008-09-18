package network.connections;

import game.CheckersGame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CheckerGameHost {
	public static final int DEFAULT_PORT = 2020;
	
	protected ServerSocket socket;
	private CheckersGame game;
	
	public CheckerGameHost() {
		
		try {
			socket = new ServerSocket(DEFAULT_PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		game = new CheckersGame();
		
	}
	
	public Socket waitForConnection() throws IOException {
		return socket.accept();
	}
	
	public static void main(String[] args) {
		CheckerGameHost host = new CheckerGameHost();
		try {
			host.waitForConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
