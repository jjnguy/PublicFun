package connectionmanager;

public interface IServerConnection {

	public boolean newText(int position, String text);
	public boolean highlightedText(int startPos, int endPos);
	public boolean textDeleted(int startPos, int endPos);
	public boolean movedMouse(int newX, int newY);
	
	public boolean connect(String host, int port );
	public boolean disconnect();
}
