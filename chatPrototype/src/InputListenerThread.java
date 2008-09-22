import java.io.InputStream;
import java.util.Scanner;

public class InputListenerThread extends Thread {

	private ChatInterface interFace;
	private Scanner stream;

	public InputListenerThread(ChatInterface inter) {
		interFace = inter;
		stream = new Scanner(inter.getIStream());
	}

	@Override
	public void run() {
		while (true) {
			String input = "";
			while (stream.hasNextLine()) {
				input += stream.nextLine();
				input += "\n";
			}
			interFace.newMessage(input, "Them");
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
