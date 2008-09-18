package network.connections;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class CheckersGameClient {
	
	private Socket socket;
	private String host;
	private int port;
	
	public CheckersGameClient(String host) {
		this.host = host;
		this.port = CheckerGameHost.DEFAULT_PORT;
		try {
			socket = new Socket(host, CheckerGameHost.DEFAULT_PORT);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean connect() {
		try {
			socket.connect(new InetSocketAddress(host, port));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
