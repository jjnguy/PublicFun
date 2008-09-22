import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Provides chat programs with a very simple interface for sending and recieving mesages
 * 
 * @author Justin Nelson
 * 
 */
public interface ChatInterface {

	public void sendMessage();

	public boolean hasMesageToSend();

	public void newMessage(String text, String username);
	
	public void saveConversation(String location);
	
	public InputStream getIStream();
	
	public void hostConversation() throws IOException;
	
	public void connectToChatServer(String host, int port);
	
	public boolean connected();
}
