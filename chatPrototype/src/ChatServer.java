import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
	public static final int DEFAULT_PORT = 5001;

	
	
	public static void main(String[] args) throws IOException {
		ServerSocket sock = new ServerSocket(DEFAULT_PORT);
		Socket client = sock.accept();
		InputStream in = client.getInputStream();

		while (true) {
			while (in.available() != 0) {
				System.out.print((char) in.read());
			}
		}
	}
}
