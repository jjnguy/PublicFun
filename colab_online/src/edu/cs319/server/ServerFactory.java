package edu.cs319.server;

public class ServerFactory {
	private static IServer serv = null;

	public static IServer getServerInstance() {
		if (serv == null) {
			serv = new Server();
		}
		return serv;
	}
}
