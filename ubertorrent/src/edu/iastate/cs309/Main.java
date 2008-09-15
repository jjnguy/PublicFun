package edu.iastate.cs309;

import java.io.FileNotFoundException;
import java.net.UnknownHostException;

import edu.iastate.cs309.client.TheActualClient;
import edu.iastate.cs309.clientserverprotocol.exceptions.ServerClientIOException;
import edu.iastate.cs309.guiElements.MainGui;
import edu.iastate.cs309.guiElements.guiUTil.GUIUtil;
import edu.iastate.cs309.plugins.exceptions.PluginNotLoadableException;
import edu.iastate.cs309.server.Server;

public class Main
{
	public static void main(String[] args) throws PluginNotLoadableException, UnknownHostException, ServerClientIOException, FileNotFoundException
	{
		GUIUtil.setLookAndFeel();
		MainGui g = new MainGui();
		TheActualClient c = new TheActualClient(g);
		g.setClientInstance(c);
		Server s = new Server();
		Thread th = new Thread(s);
		th.start();

		g.login();

	}
}
