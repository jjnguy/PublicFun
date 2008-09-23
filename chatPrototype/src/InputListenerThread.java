import java.io.IOException;
import java.io.InputStream;

public class InputListenerThread extends Thread {

	private ChatInterface interFace;
	private InputStream stream;

	public InputListenerThread(ChatInterface inter) {
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
					// input += "\n";
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
			// stream.
		}
	}
}
