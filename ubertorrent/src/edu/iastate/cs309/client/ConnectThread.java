package edu.iastate.cs309.client;

import java.awt.event.WindowEvent;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import edu.iastate.cs309.clientserverprotocol.exceptions.ServerClientIOException;
import edu.iastate.cs309.communication.PasswordHash;
import edu.iastate.cs309.guiElements.LoggingInFrame;
import edu.iastate.cs309.util.Util;

public class ConnectThread extends Thread
{
	private String host;
	private int port;
	private PasswordHash pword;
	private TheActualClient client;
	private boolean success;
	private LoggingInFrame parent;
	private Logger log;

	public ConnectThread(TheActualClient clientP, String hostP, int portP, PasswordHash pwordP, LoggingInFrame parentP)
	{
		log = ClientLog.log;
		success = false;
		client = clientP;
		host = hostP;
		port = portP;
		pword = pwordP;
		parent = parentP;
	}

	@Override
	public void run()
	{
		if (Util.DEBUG)
			System.out.println(success);
		try
		{
			client.connect(host, port, pword);
			success = true;
		}
		catch (UnknownHostException e)
		{
			log.log(Level.SEVERE, "Failed to connect to the Server.", e);
			JOptionPane.showMessageDialog(parent, "Connecting to the server failed.\nAn error was created with the following message:\n" + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
			if (Util.DEBUG)
				e.printStackTrace();
			success = false;
		}
		catch (ServerClientIOException e)
		{
			log.log(Level.SEVERE, "Failed to connect to the Server.", e);
			JOptionPane.showMessageDialog(parent, "Connecting to the server failed.\nAn error was created with the following message:\n" + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
			if (Util.DEBUG)
				e.printStackTrace();
			success = false;
		}

		if (Util.DEBUG)
			System.out.println(success);
		parent.setSuccess(success);
		parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
	}

	public boolean success()
	{
		return success;
	}

	public void kill()
	{
		stop();
	}
}
