package connectionmanager;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerConnection implements IServerConnection {

	private Socket connection;
	private InputStream sockIn;
	private OutputStream socOut;
	
	@Override
	public boolean connect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean disconnect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean highlightedText(int startPos, int endPos) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean movedMouse(int newX, int newY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean newText(int position, String text) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean textDeleted(int startPos, int endPos) {
		// TODO Auto-generated method stub
		return false;
	}

}
