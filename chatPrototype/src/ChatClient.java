import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient implements Runnable {
	
	private ChatInterface interFace;
	private Socket connection;
	
	public ChatClient(){
		interFace = new FullChatPanel();
		
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket server = new Socket("localhost", ChatServer.DEFAULT_PORT);
		Scanner stdin = new Scanner(System.in);
		OutputStream out = server.getOutputStream();
		while (true) {
			while (stdin.hasNext()) {
				out.write(stdin.nextLine().getBytes());
			}
			out.write("\n".getBytes());
		}
	}

	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
