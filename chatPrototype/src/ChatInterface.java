import java.io.IOException;
import java.net.Socket;

/**
 * Provides chat programs with a very simple interface for sending and recieving mesages
 * 
 * @author Justin Nelson
 * 
 */
public interface ChatInterface {

	public String getMessage();

	public boolean hasMesageToSend();

	public void newMessage(String text, String username);
	
	public void saveConversation(String location);
	
	public Socket hostConversation() throws IOException;
	
	public Socket connectToChatServer(String host, int port);
	
	public boolean connected();
	
}
