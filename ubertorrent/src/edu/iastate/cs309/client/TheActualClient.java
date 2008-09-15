package edu.iastate.cs309.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import edu.iastate.cs309.clientserverprotocol.ServerConnection;
import edu.iastate.cs309.clientserverprotocol.exceptions.FailedMessageException;
import edu.iastate.cs309.clientserverprotocol.exceptions.ServerClientIOException;
import edu.iastate.cs309.comminterfaces.Client;
import edu.iastate.cs309.communication.ServerProperties;
import edu.iastate.cs309.communication.TorrentInfo;
import edu.iastate.cs309.communication.TorrentProp;
import edu.iastate.cs309.guiElements.MainGui;
import edu.iastate.cs309.guiElements.guiUTil.GUIUtil;
import edu.iastate.cs309.util.ConfigFile;
import edu.iastate.cs309.util.Util;

/**
 * TheActualClient is an implementation of a Client. It gets and displays
 * information in an instance of MainGUi.
 * 
 * It must always be linked to a MainGui
 * 
 * @author Justin Nelson
 * 
 */
public class TheActualClient implements Client
{
	private Logger log = ClientLog.log;

	public static String fileDirectory;

	public final static short myId = 69;

	private MainGui theGui;

	private ServerConnection theServer;

	//information about the server
	private ServerProperties serverSettings;

	private List<TransferFilesThread> listOfTransfers = new ArrayList<TransferFilesThread>();
	/*
	 * Parallel arrays storing info about Torrents
	 */
	private List<TorrentInformationContainer> activeTorrents;

	/**
	 * Because Justin removed the mainGui main method, I added one here
	 * 
	 * @param args
	 *            because maybe we'll use em?
	 * @throws Exception
	 *             for the fun of it
	 */
	public static void main(String[] args) throws Exception
	{
		try
		{
			GUIUtil.setLookAndFeel();
			new TheActualClient(new MainGui());
		}
		catch (Throwable t)
		{
			JOptionPane.showMessageDialog(null, "Huston we have a problem.  I just crashed...shit.", Util.getUber() + "Fail", JOptionPane.ERROR_MESSAGE);
			if (Util.DEBUG)
				System.out.println("Oops, I just crashed hardcore...I really dunno what happened.");
			System.out.println(t.getMessage());
			t.printStackTrace();
		}
	}

	/**
	 * Creates a default Client that gets and displays its info in a MainGui
	 * 
	 * @param aGui
	 * @throws FileNotFoundException
	 */
	public TheActualClient(MainGui aGui) throws FileNotFoundException
	{
		if (aGui == null)
		{
			log.log(Level.SEVERE, "The client was given a null MainGui to use.");
			throw new IllegalArgumentException("The parameter to this constructor must be non null.");
		}
		theGui = aGui;
		theGui.setClientInstance(this);
		activeTorrents = new ArrayList<TorrentInformationContainer>();
		fileDirectory = new ConfigFile(new File(MainGui.getConfigFileLocation())).getValueByNameString("saveFilesLocation");
	}

	/**
	 * @param host
	 *            the ip or DNS of the server that you are trying to connect to
	 * @param port
	 *            the port to connect to on the server
	 * @param pword
	 *            a user's password
	 * @throws ServerClientIOException
	 * @throws UnknownHostException
	 */
	public void connect(String host, int port, edu.iastate.cs309.communication.PasswordHash pword) throws UnknownHostException, ServerClientIOException
	{
		theServer = new ServerConnection(host, port, pword, this);
		theServer.getAllTorrents(myId);
		theServer.getServerProp(myId);
	}

	public boolean connected()
	{
		return theServer != null;
	}

	public boolean shutDownServer()
	{
		if (!connected())
		{
			log.log(Level.WARNING, "The Client tried to shut down a Server while not connected.");
		}
		boolean ret = false;
		try
		{
			theServer.shutdown();
			ret = true;
		}
		catch (FailedMessageException e)
		{
			JOptionPane.showMessageDialog(theGui, "Could not establish connection with the Server.\nMake sure that the Server is running.", "Failed to Communicate With Server", JOptionPane.ERROR_MESSAGE);
			theGui.logout();
			if (Util.DEBUG)
				e.printStackTrace();
		}
		if (ret)
			disconnect();
		return ret;
	}

	/**
	 * Removes the connection to the server
	 */
	public void disconnect()
	{
		theServer.disconnect();
		theServer = null;
	}

	/**
	 * Tells the server to start downloading the supplied torrent file
	 * 
	 * @param theTorrentFile
	 *            a torrent file for the program to start downloading
	 */
	public void addTorrent(File theTorrentFile)
	{
		log.log(Level.FINER, "The client requested the server to add a torrent file.");
		if (!connected())
		{
			log.log(Level.SEVERE, "Tried to change properties on the server or client while not connected.");
			JOptionPane.showMessageDialog(theGui, "Tried to send messages to a non connected Server.", "Error Communicating", JOptionPane.ERROR_MESSAGE);
			return;
		}
		FileInputStream fRead = null;
		try
		{
			fRead = new FileInputStream(theTorrentFile);
			theServer.addTorrent(theTorrentFile.getName(), fRead);
		}
		catch (FailedMessageException e)
		{
			log.log(Level.SEVERE, "A FailedMessageException was thrown while trying to add a torrent file to the Server with the following message:\n" + e.getMessage());
			JOptionPane.showMessageDialog(theGui, "Could not establish connection with the Server.\nMake sure that the Server is running.", "Failed to Communicate With Server", JOptionPane.ERROR_MESSAGE);
			theGui.logout();
			if (Util.DEBUG)
				e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			log.log(Level.SEVERE, Util.getUber() + " was unable to find the file supplied for transfer.");
			if (Util.DEBUG)
				e.printStackTrace();
		}
	}

	public void removeTorrent(int refID)
	{
		log.log(Level.FINE, "The Client requested to remove torrent with refID " + refID);
		if (!connected())
		{
			log.log(Level.SEVERE, "Tried to change properties on the server or client while not connected.");
			JOptionPane.showMessageDialog(theGui, "Tried to send messages to a non connected Server.", "Error Communicating", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try
		{
			theServer.removeTorrent(refID);
		}
		catch (FailedMessageException e)
		{
			log.log(Level.SEVERE, "A FailedMessageException was thrown while trying to remove a torrent with the following message:\n" + e.getMessage());
			JOptionPane.showMessageDialog(theGui, "Could not establish connection with the Server.\nMake sure that the Server is running.", "Failed to Communicate With Server", JOptionPane.ERROR_MESSAGE);
			theGui.logout();
			if (Util.DEBUG)
				e.printStackTrace();
		}
	}

	private void tellTheGUIAboutSomeNewTorrentInfo()
	{
		theGui.updateTorrentInformation(activeTorrents);
	}

	public void changeServerSettings(ServerProperties theNewSettings)
	{
		if (!connected())
		{
			log.log(Level.SEVERE, "Tried to change properties on the server or client while not connected.");
			JOptionPane.showMessageDialog(theGui, "Tried to send messages to a non connected Server.", "Error Communicating", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try
		{
			theServer.changeServerProp(theNewSettings);
		}
		catch (FailedMessageException e)
		{
			JOptionPane.showMessageDialog(theGui, "Could not establish connection with the Server.\nMake sure that the Server is running.", "Failed to Communicate With Server", JOptionPane.ERROR_MESSAGE);
			theGui.logout();
			log.log(Level.SEVERE, "A FailedMessageException was thrown while trying to change the Server settings with the following message:\n" + e.getMessage());
			if (Util.DEBUG)
				e.printStackTrace();
		}
	}

	public void getListOfTorrents()
	{
		if (!connected())
		{
			log.log(Level.SEVERE, "Tried to change properties on the server or client while not connected.");
			JOptionPane.showMessageDialog(theGui, "Tried to send messages to a non connected Server.", "Error Communicating", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try
		{
			theServer.getAllTorrents(myId);
		}
		catch (FailedMessageException e)
		{
			JOptionPane.showMessageDialog(theGui, "Could not establish connection with the Server.\nMake sure that the Server is running.", "Failed to Communicate With Server", JOptionPane.ERROR_MESSAGE);
			theGui.logout();
			log.log(Level.SEVERE, "A FailedMessageException was thrown while trying to get the list of torrents from the Server with the following message:\n" + e.getMessage());
			if (Util.DEBUG)
				e.printStackTrace();
		}
	}

	public void getFile(int refID, List<Integer> fileIndex)
	{
		if (!connected())
		{
			log.log(Level.SEVERE, "Tried to change properties on the server or client while not connected.");
			JOptionPane.showMessageDialog(theGui, "Tried to send messages to a non connected Server.", "Error Communicating", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try
		{
			if (Util.DEBUG)
				System.out.println("The Client says: We are asking the server to transfer a torrent file.");
			theServer.transferTorrent(myId, refID, fileIndex);
		}
		catch (FailedMessageException e)
		{
			theGui.logout();
			JOptionPane.showMessageDialog(theGui, "Could not establish connection with the Server.\nMake sure that the Server is running.", "Failed to Communicate With Server", JOptionPane.ERROR_MESSAGE);
			log.log(Level.SEVERE, "A FailedMessageException was thrown while trying to give a file to the Server with the following message: " + e.getMessage());
			if (Util.DEBUG)
				e.printStackTrace();
		}
	}

	/**
	 * @param allTorrents
	 *            the list of torrents that the server has
	 * @see edu.iastate.cs309.comminterfaces.Client#torrentList(java.util.List)
	 */
	@Override
	public void torrentList(List<Integer> allTorrents)
	{
		log.log(Level.INFO, "The Server sent the client a list of all active torrents and their refID's");
		if (!connected())
		{
			log.log(Level.SEVERE, "Tried to change properties on the server or client while not connected.");
			JOptionPane.showMessageDialog(theGui, "Tried to send messages to a non connected Server.", "Error Communicating", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Remove them.
		for (Iterator<TorrentInformationContainer> iter = activeTorrents.iterator(); iter.hasNext();)
		{
			if (!allTorrents.contains(iter.next().refID))
				iter.remove();
		}

		// Add new ones
		for (Integer refID : allTorrents)
		{
			TorrentInformationContainer toAdd = new TorrentInformationContainer();
			toAdd.refID = refID;

			try
			{
				if (!activeTorrents.contains(toAdd))
				{
					activeTorrents.add(toAdd);
					theServer.getTorrentProp(myId, refID);
					theServer.getTorrentInfo(myId, refID);
				}
			}
			catch (FailedMessageException e)
			{
				JOptionPane.showMessageDialog(theGui, "Could not establish connection with the Server.\nMake sure that the Server is running.", "Failed to Communicate With Server", JOptionPane.ERROR_MESSAGE);
				theGui.logout();
				log.log(Level.SEVERE, "A FailedMessageException was thrown while trying to get the list of torrents from the server with the following message:\n" + e.getMessage());
				if (Util.DEBUG)
					e.printStackTrace();
			}
		}
		tellTheGUIAboutSomeNewTorrentInfo();
	}

	/**
	 * @see edu.iastate.cs309.comminterfaces.Client#torrentRemoved(int)
	 */
	@Override
	public void torrentRemoved(int refID) throws FailedMessageException
	{
		for (Iterator<TorrentInformationContainer> iterator = activeTorrents.iterator(); iterator.hasNext();)
		{
			TorrentInformationContainer tInfo = iterator.next();
			if (tInfo.refID == refID)
			{
				iterator.remove();
				return;
			}
		}
		tellTheGUIAboutSomeNewTorrentInfo();
		log.log(Level.FINE, "A torrent was removed from the Server/Client.");
	}

	/**
	 * @see edu.iastate.cs309.comminterfaces.Client#transferFiles(int,
	 *      java.util.List, java.util.List)
	 */
	@Override
	public void transferFiles(int refID, List<Integer> fileIndexes, List<InputStream> data) throws FailedMessageException
	{
		if (Util.DEBUG)
			System.out.println("WE GOT HEREAKJFDSHLKJSADFHLKJSDHF \n\n\n\n\n\n\n\nn\n\n\n\n\nKJHDSF OIUDSAFJ I!!!!!\n\tThis is good");
		log.log(Level.INFO, "The Server requested to send the Client some files.");
		listOfTransfers.add(new TransferFilesThread(refID, fileIndexes, data, getEntireInfo(refID)));
		listOfTransfers.get(listOfTransfers.size() - 1).start();
	}

	/**
	 * @see edu.iastate.cs309.comminterfaces.Client#updateServerStatus(edu.iastate.cs309.communication.ServerProperties)
	 */
	@Override
	public void updateServerStatus(ServerProperties settings) throws FailedMessageException
	{
		serverSettings = settings;
		log.log(Level.INFO, "Server settings were changed.");
	}

	/**
	 * @see edu.iastate.cs309.comminterfaces.Client#updateTorrentInformation(int,
	 *      edu.iastate.cs309.communication.TorrentInfo)
	 */
	@Override
	public void updateTorrentInformation(int refID, TorrentInfo info) throws FailedMessageException
	{
		// log.log(Level.INFO, "The Server sent the client an updated torrent info for the torrent with refID " + refID);
		TorrentInformationContainer tInfoTemp = new TorrentInformationContainer();
		tInfoTemp.refID = refID;
		tInfoTemp.info = info;
		if (!activeTorrents.contains(tInfoTemp))
		{
			activeTorrents.add(tInfoTemp);
		}
		else
		{
			for (TorrentInformationContainer tInfos : activeTorrents)
			{
				if (tInfos.refID == tInfoTemp.refID)
				{
					tInfos.info = info;
					break;
				}
			}
		}
		tellTheGUIAboutSomeNewTorrentInfo();
	}

	/**
	 * @see edu.iastate.cs309.comminterfaces.Client#updateTorrentProperties(int,
	 *      edu.iastate.cs309.communication.TorrentProp)
	 */
	@Override
	public void updateTorrentProperties(int refID, TorrentProp prop) throws FailedMessageException
	{
		TorrentInformationContainer tInfoTemp = new TorrentInformationContainer();
		tInfoTemp.refID = refID;
		tInfoTemp.prop = prop;
		if (!activeTorrents.contains(tInfoTemp))
		{
			activeTorrents.add(tInfoTemp);
		}
		else
		{
			for (TorrentInformationContainer tInfos : activeTorrents)
			{
				if (tInfos.refID == refID)
				{
					tInfos.prop = prop;
					return;
				}
			}
		}
	}

	public TorrentInfo getTorrentInfo(int refID)
	{
		for (TorrentInformationContainer tInfos : activeTorrents)
		{
			if (tInfos.refID == refID)
				return tInfos.info;
		}
		log.log(Level.WARNING, "Something tried to get information regarding a non-existant torrent.");
		throw new NoSuchElementException("There was no torrent with the given ref ID in this list of active Torrents. ID: " + refID);
	}

	public TorrentProp getTorrentProp(int refID)
	{
		for (TorrentInformationContainer tInfos : activeTorrents)
		{
			if (tInfos.refID == refID)
				return tInfos.prop;
		}
		log.log(Level.WARNING, "Something tried to get information regarding a non-existant torrent.");
		throw new NoSuchElementException("There was no torrent with the given ref ID in this list of active Torrents. ID: " + refID);
	}

	private TorrentInformationContainer getEntireInfo(int refID)
	{
		for (TorrentInformationContainer info : activeTorrents)
		{
			if (info.refID == refID)
				return info;
		}
		log.log(Level.WARNING, "Something tried to get information regarding a non-existant torrent.");
		throw new NoSuchElementException("The torrent with refID " + refID + " doesn't exist for this client.");
	}

	public ServerProperties getServerProps()
	{
		return serverSettings;
	}
}
