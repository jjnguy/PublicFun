package network.connections;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class CheckersGameClient {
	
	private Socket socket;
	
	public CheckersGameClient(String host) {
		
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
}
