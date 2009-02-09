package client;
import java.io.IOException;
import java.io.InputStream;

/**
 * Provides chat programs with a very simple interface for sending and recieving mesages
 * 
 * @author Justin Nelson
 * 
 */
public interface LiveEditInterface {

	public boolean sendMessage();

	public boolean hasMesageToSend();

	public void newMessage(String text, String username);
	
	public void saveConversation();
	
	public InputStream getIStream();
	
	public void hostConversation() throws IOException;
	
	public boolean connectToChatServer(String host, int port);
	
	public boolean connected();
}
