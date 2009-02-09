package server;

import client.LiveEditInterface;

public interface IServer {

	public boolean connect(LiveEditInterface client);
	public boolean disconnect();
	
	
}
