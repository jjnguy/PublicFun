package client;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JOptionPane;

import connectionmanager.IServerConnection;

public class ClientChangeThread extends Thread {

	private LiveEditInterface interFace;
	private IServerConnection connection;

	public ClientChangeThread(LiveEditInterface inter) {
		interFace = inter;
		connection = inter.getServerConnection();
	}

	@Override
	public void run() {
		while (true) {
			String input = "";
			
			if (!input.trim().equals("")) {
				interFace.newText(0, "");
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
