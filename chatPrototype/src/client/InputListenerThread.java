package client;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JOptionPane;

public class InputListenerThread extends Thread {

	private LiveEditInterface interFace;
	private InputStream stream;

	public InputListenerThread(LiveEditInterface inter) {
		interFace = inter;
		stream = inter.getIStream();
	}

	@Override
	public void run() {
		while (true) {
			String input = "";
			try {
				while (stream.available() != 0) {
					input += (char) stream.read();
				}
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Failed to send message.",
						"Message Send Fail", JOptionPane.ERROR_MESSAGE);
			}
			if (!input.trim().equals("")) {
				interFace.newMessage(input, "Them");
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
