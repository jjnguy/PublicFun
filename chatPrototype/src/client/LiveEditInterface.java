package client;
import java.io.IOException;
import java.io.InputStream;

import connectionmanager.IServerConnection;

/**
 * Provides chat programs with a very simple interface for sending and recieving mesages
 * 
 * @author Justin Nelson
 * 
 */
public interface LiveEditInterface {

	public boolean newText(String text, String username);
	public boolean newText(int position, String text);
	public boolean highlightedText(int startPos, int endPos);
	public boolean textDeleted(int startPos, int endPos);
	public boolean movedMouse(int newX, int newY);
	
	public IServerConnection getServerConnection();
	
	public void hostCoLabRoom() throws IOException;
	
	public boolean connectToCoLabServer(String host, int port);
	
	public boolean connected();
}
