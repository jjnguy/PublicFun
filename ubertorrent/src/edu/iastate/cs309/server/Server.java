package edu.iastate.cs309.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import edu.iastate.cs309.clientserverprotocol.ClientConnection;
import edu.iastate.cs309.clientserverprotocol.exceptions.FailedMessageException;
import edu.iastate.cs309.clientserverprotocol.exceptions.FailedSendMessageException;
import edu.iastate.cs309.comminterfaces.Client;
import edu.iastate.cs309.comminterfaces.IServer;
import edu.iastate.cs309.communication.PasswordHash;
import edu.iastate.cs309.communication.ServerProperties;
import edu.iastate.cs309.communication.TorrentInfo;
import edu.iastate.cs309.communication.TorrentProp;
import edu.iastate.cs309.torrentManager.TorrentManager;
import edu.iastate.cs309.torrentManager.containers.PeerID;
import edu.iastate.cs309.torrentManager.exceptions.IncompleteFileException;
import edu.iastate.cs309.torrentManager.exceptions.TorrentNotFoundException;
import edu.iastate.cs309.torrentparser.BEncodedObject;
import edu.iastate.cs309.torrentparser.BList;
import edu.iastate.cs309.torrentparser.ByteString;
import edu.iastate.cs309.torrentparser.ParseException;
import edu.iastate.cs309.util.ConfigFile;
import edu.iastate.cs309.util.Util;

/**
 * 
 * 
 * @author Michael Seibert
 */
public class Server implements IServer, Runnable
{
	private static final File TORRENT_DATA_FILE = new File("src/config" + File.separator + "torrents.dat");
	private static final File CONFIG_FILE = new File("src/config" + File.separator + "server.conf");
	private static final long STATUS_UPDATE_PERIOD = 1000;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Server server = new Server();
		new Thread(server).start();
	}

	private ServerProperties props = new ServerProperties();
	private TorrentManager torrentManager;
	private boolean exiting;
	private Map<Integer, ClientConnection> clients = Collections.synchronizedMap(new HashMap<Integer, ClientConnection>());
	private Logger log = ServerLog.log;
	private SSLServerSocket serverSocket;
	public boolean existsStatusUpdater;
	private boolean serverPropChanged;

	/**
	 * 
	 */
	public void run()
	{
		loadConfig();
		makeTorrentManager();
		log.log(Level.INFO, "Server started");
		listenForClients();
		exitThreads();
		saveTorrentInfo(getFileList());
		log.log(Level.INFO, "Server exiting");
	}

	/**
	 * 
	 */
	private void makeStatusUpdater()
	{
		if (needStatusUpdates() && !existsStatusUpdater)
			new Timer().schedule(new StatusUpdater(this), STATUS_UPDATE_PERIOD, STATUS_UPDATE_PERIOD);
	}

	/**
	 * 
	 */
	private void exitThreads()
	{
		torrentManager.stop();
		for (ClientConnection c : clients.values())
		{
			c.disconnect();
		}
		clients.clear();
	}

	/**
	 * 
	 */
	private void loadConfig()
	{
		log.log(Level.FINER, "Loading configuration file.");
		ConfigFile config = null;
		try
		{
			config = new ConfigFile(CONFIG_FILE);
		}
		catch (FileNotFoundException e)
		{
			log.log(Level.SEVERE, "Configuration file not found or could not be opened, server exiting\n\tCreate the file " + CONFIG_FILE.getPath(), e);
			System.exit(1);
		}

		boolean rewriteConfig = false;

		if (loadPeerID(config) == true)
		{
			log.log(Level.FINEST, "Peer ID changed");
			rewriteConfig = true;
		}

		if (loadListenPort(config) == true)
		{
			log.log(Level.FINEST, "Listening port changed");
			rewriteConfig = true;
		}

		if (loadPeerPort(config) == true)
		{
			log.log(Level.FINEST, "Peer listening port changed");
			rewriteConfig = true;
		}

		if (loadRootDir(config) == true)
		{
			log.log(Level.FINEST, "Root directory changed");
			rewriteConfig = true;
		}

		if (loadPeerTimeout(config) == true)
		{
			log.log(Level.FINEST, "Peer timeout changed");
			rewriteConfig = true;
		}

		if (loadPasswordHash(config) == true)
		{
			log.log(Level.FINEST, "Password changed");
			rewriteConfig = true;
		}

		if (rewriteConfig)
			writeConfig();
	}

	/**
	 * @param config
	 */
	private boolean loadPasswordHash(ConfigFile config)
	{
		log.log(Level.FINER, "Loading password hash");
		boolean result = false;

		String passwordHashStr = config.getValueByNameString("passwordHash");

		byte[] passwordHash = new byte[20];
		boolean regenPass = false;

		if (passwordHashStr != null && passwordHashStr.length() == 40)
		{
			try
			{
				for (int i = 0; i < 20; i++)
				{
					passwordHash[i] = (byte) Integer.parseInt(passwordHashStr.substring(2 * i, 2 * (i + 1)), 16);
				}
			}
			catch (NumberFormatException e)
			{
				log.log(Level.FINE, "Password hash didn't parse correctly", e);
				regenPass = true;
			}
		}
		else
		{
			if (passwordHashStr == null)
				log.log(Level.FINEST, "Password not found in configuration file");
			else
				log.log(Level.FINE, "Password string was not length 40, found ", passwordHashStr);

			regenPass = true;
		}

		if (regenPass)
		{
			System.out.println("Missing or corrupted password, please enter a new one: ");
			passwordHash = Util.getHashedBytes(new Scanner(System.in).nextLine().getBytes());
			result = true;
		}

		props.setPass(new PasswordHash(passwordHash));

		return result;
	}

	/**
	 * @param config
	 */
	private boolean loadPeerTimeout(ConfigFile config)
	{
		log.log(Level.FINER, "Loading peer timeout");
		boolean result = false;
		boolean regenTimeout = false;
		int peerTimeout = 0;
		try
		{
			String peerTimeoutString = config.getValueByNameString("peerTimeout");
			peerTimeoutString.length();
			peerTimeout = Integer.parseInt(peerTimeoutString);
		}
		catch (NullPointerException e)
		{
			log.log(Level.FINE, "Peer timeout not in config file: ");
			regenTimeout = true;
		}
		catch (NumberFormatException e)
		{
			log.log(Level.FINE, "Peer timeout was not an integer: ", e);
			regenTimeout = true;
		}

		if (regenTimeout)
		{
			peerTimeout = 120000;
			result = true;
		}
		props.setTimeout(peerTimeout);
		return result;
	}

	/**
	 * @param config
	 */
	private boolean loadRootDir(ConfigFile config)
	{
		log.log(Level.FINER, "Loading torrent root directory");
		String rootDir = config.getValueByNameString("rootDir");
		if (rootDir == null)
		{
			log.log(Level.SEVERE, "Torrent root directory not found in config file, exiting");
			System.exit(1);
		}
		props.setRootDir(rootDir);
		return false;
	}

	/**
	 * @param config
	 */
	private boolean loadListenPort(ConfigFile config)
	{
		log.log(Level.FINER, "Loading peer listening port");
		boolean result = false;

		int listenPort;
		try
		{
			listenPort = Short.parseShort(config.getValueByNameString("listenPort"));
		}
		catch (NumberFormatException e)
		{
			log.log(Level.FINE, "Client listening port was not an integer", e);
			listenPort = 30908;
			result = true;
		}
		props.setPort(listenPort);
		return result;
	}

	/**
	 * @param config
	 */
	private boolean loadPeerPort(ConfigFile config)
	{
		log.log(Level.FINER, "Loading peer listening port");
		boolean result = false;

		int listenPort;
		try
		{
			listenPort = Short.parseShort(config.getValueByNameString("peerPort"));
		}
		catch (NumberFormatException e)
		{
			log.log(Level.FINE, "Peer listening port was not an integer", e);
			listenPort = 30909;
			result = true;
		}
		props.setPeerPort(listenPort);
		return result;
	}

	/**
	 * @param config
	 */
	private boolean loadPeerID(ConfigFile config)
	{
		log.log(Level.FINER, "Loading peer ID");
		boolean result = false;
		String peerIDString = config.getValueByNameString("peerID");
		if (peerIDString == null || peerIDString.length() != 20)
		{
			if (peerIDString == null)
				log.log(Level.FINEST, "Peer ID string not found in config file");
			else
				log.log(Level.FINE, "Peer ID has wrong length", peerIDString);
			peerIDString = "-UB1000-";
			Random rand = new Random();
			for (int i = 0; i < 12; i++)
			{
				peerIDString += Integer.toString(rand.nextInt(10));
			}
			result = true;
		}
		try
		{
			props.setPeerID(new PeerID(peerIDString.getBytes("US-ASCII")));
		}
		catch (UnsupportedEncodingException e)
		{
			log.log(Level.SEVERE, "US ASCII encoding not supported by java, exiting", e);
			System.exit(1);
		}

		return result;
	}

	/**
	 * 
	 */
	private void makeTorrentManager()
	{
		log.log(Level.FINER, "Creating torrent manager");
		torrentManager = new TorrentManager(this);
		torrentManager.newServerProp(props);
		loadTorrentInfo();
		new Thread(torrentManager).start();
	}

	/**
	 * @throws IOException
	 * 
	 */
	private void listenForClients()
	{
		log.log(Level.FINER, "Listening for clients");
		while (!exiting)
		{
			try
			{
				ServerSocketFactory factory = SSLServerSocketFactory.getDefault();
				serverSocket = (SSLServerSocket) factory.createServerSocket(props.getPort());
				String[] ciphers = { "SSL_DH_anon_WITH_RC4_128_MD5" };
				serverSocket.setEnabledCipherSuites(ciphers);
			}
			catch (IOException e)
			{
				log.log(Level.SEVERE, "Unable to open port for client listening.", e);
				return;
			}

			try
			{
				SSLSocket client = (SSLSocket) serverSocket.accept();
				log.log(Level.FINER, "Client connected successfully via SSL, checking password...");
				ClientConnection clientConn = new ClientConnection(client, this, props.getPass());
				log.log(Level.INFO, "Client connected with correct password");
				clients.put(clientConn.getID(), clientConn);
				makeStatusUpdater();
				serverSocket.close();
			}
			catch (SSLException e)
			{
				log.log(Level.INFO, "Client failed to connect via SSL", e);
			}
			catch (SecurityException e)
			{
				log.log(Level.INFO, "Client failed to provide correct password", e);
			}
			catch (IOException e)
			{
				if (exiting)
					return;
				if (serverPropChanged)
					continue;
				log.log(Level.SEVERE, "IO error while listening on client port", e);
				return;
			}
		}
	}

	/**
	 * @see edu.iastate.cs309.comminterfaces.IServer#addTorrent(String,
	 *      java.io.InputStream)
	 */
	public void addTorrent(String name, InputStream dotTorrentFile) throws FailedMessageException
	{
		log.log(Level.FINE, "Adding torrent: " + name);
		File torrentDir = new File(props.getRootDir());
		if (!torrentDir.isDirectory())
		{
			if (!torrentDir.mkdirs())
			{
				log.log(Level.SEVERE, "Couldn't create torrent download directory, server exiting", torrentDir);
				System.exit(1);
			}
		}
		File file = new File(torrentDir, name);

		FileOutputStream out = null;

		try
		{

			out = new FileOutputStream(file);
		}
		catch (FileNotFoundException e)
		{
			log.log(Level.WARNING, "Couldn't open server-side torrent file", e);
			return;
		}

		try
		{
			byte[] buffer = new byte[1 << 15];
			int bytesRead = dotTorrentFile.read(buffer);
			while (bytesRead != -1)
			{
				out.write(buffer, 0, bytesRead);
				bytesRead = dotTorrentFile.read(buffer);
			}
		}
		catch (IOException e)
		{
			log.log(Level.WARNING, "IO error transferring torrent file", e);
			return;
		}
		finally
		{
			try
			{
				out.close();
			}
			catch (IOException e)
			{
			}
		}

		int refID = torrentManager.addTorrent(file);

		if (refID == -1)
		{
			log.log(Level.WARNING, "Couldn't add torrent");
			return;
		}

		/* if the add is successful, update all clients */
		List<Integer> list = torrentManager.getTorrents();

		for (Client c : clients.values())
			c.torrentList(list);

		makeStatusUpdater();

		List<String> files = getFileList();

		if (!files.contains(file.toString()))
			files.add(file.toString());

		saveTorrentInfo(files);

		log.log(Level.FINER, "Successfully added torrent: " + name);
	}

	private List<String> getFileList()
	{
		List<String> files = new LinkedList<String>();

		for (Integer id : torrentManager.getTorrents())
			try
			{
				files.add(torrentManager.getInfo(id).getDotTorrentFile());
			}
			catch (TorrentNotFoundException e)
			{
			}
		return files;
	}

	/**
	 * @see edu.iastate.cs309.comminterfaces.IServer#changeServerProp(edu.iastate.cs309.communication.ServerProperties)
	 */
	@Override
	public void changeServerProp(edu.iastate.cs309.communication.ServerProperties setting) throws FailedMessageException
	{
		log.log(Level.FINE, "Changing server props");

		props = setting;
		torrentManager.newServerProp(props);
		writeConfig();
		for (Client c : clients.values())
			c.updateServerStatus(props);

		serverPropChanged = true;
		try
		{
			serverSocket.close();
		}
		catch (IOException e)
		{
			ServerLog.log.log(Level.WARNING, "Weird stuff happened while changing server properties");
		}
	}

	/**
	 * 
	 */
	private void writeConfig()
	{
		log.log(Level.FINER, "Writing configuration file");
		ConfigFile config = new ConfigFile();

		byte[] peerID = props.getPeerID().getBytes();

		config.setProperty("peerID", new String(peerID));

		config.setProperty("rootDir", props.getRootDir());

		config.setProperty("listenPort", Integer.toString(props.getPort()));

		config.setProperty("peerPort", Integer.toString(props.getPeerPort()));

		config.setProperty("peerTimeout", Long.toString(props.getTimeout()));

		byte[] passwordHash = props.getPass().getBytes();
		String passwordHashEncoded = "";
		for (byte b : passwordHash)
			passwordHashEncoded += String.format("%h", (0xff & b) + 0x100).substring(1);
		config.setProperty("passwordHash", passwordHashEncoded);

		try
		{
			config.writeFile(CONFIG_FILE);
		}
		catch (FileNotFoundException e)
		{
			log.log(Level.WARNING, "Couldn't save config file", e);
		}
	}

	/**
	 * @see edu.iastate.cs309.comminterfaces.IServer#changeTorrentProp(int,
	 *      edu.iastate.cs309.communication.TorrentProp)
	 */
	@Override
	public void changeTorrentProp(int refID, TorrentProp prop) throws FailedMessageException
	{
		log.log(Level.FINE, "Changing torrent props for torrent refID " + refID);

		torrentManager.changeProperties(refID, prop);
		for (Client client : clients.values())
			client.updateTorrentProperties(refID, prop);
	}

	/**
	 * @see edu.iastate.cs309.comminterfaces.IServer#getAllTorrents(int)
	 */
	@Override
	public void getAllTorrents(int clientID) throws FailedMessageException
	{
		log.log(Level.FINE, "Client " + clientID + " requested full torrent list");

		Client client = clients.get(clientID);

		sendTorrentList(client);
	}

	private void sendTorrentList(Client client) throws FailedSendMessageException
	{
		List<Integer> torrents = torrentManager.getTorrents();
		client.torrentList(torrents);
	}

	/**
	 * @see edu.iastate.cs309.comminterfaces.IServer#getServerProp(int)
	 */
	@Override
	public void getServerProp(int clientID) throws FailedMessageException
	{
		log.log(Level.FINE, "Client " + clientID + " requested server props");

		clients.get(clientID).updateServerStatus(props);
	}

	/**
	 * @see edu.iastate.cs309.comminterfaces.IServer#getTorrentInfo(int, int)
	 */
	@Override
	public void getTorrentInfo(int clientID, int refID) throws FailedMessageException
	{
		log.log(Level.FINE, "Client " + clientID + " requested info for torrent refID " + refID);

		Client client = clients.get(clientID);

		sendTorrentInfo(refID, client);
	}

	private void sendTorrentInfo(int refID, Client client) throws FailedMessageException
	{
		try
		{
			TorrentInfo info = torrentManager.getInfo(refID);
			client.updateTorrentInformation(refID, info);
		}
		catch (TorrentNotFoundException e)
		{
			if (Util.DEBUG)
				e.printStackTrace();
			log.log(Level.WARNING, "Client tried to getInfo on a nonexistent torrent", e);
		}
	}

	/**
	 * @see edu.iastate.cs309.comminterfaces.IServer#getTorrentProp(int, int)
	 */
	@Override
	public void getTorrentProp(int clientID, int refID) throws FailedMessageException
	{
		log.log(Level.FINE, "Client " + clientID + " requested props for torrent refID " + refID);

		try
		{
			TorrentProp torrProps = torrentManager.getProp(refID);
			clients.get(clientID).updateTorrentProperties(refID, torrProps);
		}
		catch (TorrentNotFoundException e)
		{
			if (Util.DEBUG)
				e.printStackTrace();
			log.log(Level.WARNING, "Client tried to getProp on a nonexistent torrent", e);
		}

	}

	/**
	 * @see edu.iastate.cs309.comminterfaces.IServer#transferTorrent(int, int,
	 *      java.util.List)
	 */
	@Override
	public void transferTorrent(int clientID, int refID, List<Integer> fileIndex) throws FailedMessageException
	{
		log.log(Level.FINE, "Client " + clientID + " wants to transfer file for torrent refID " + refID);

		if (Util.TDEBUG)
			System.out.println("Server got request:\n\tclientID: " + clientID + "\n\trefID: " + refID + "\n\tfileIndices: " + fileIndex);

		List<InputStream> streams = new LinkedList<InputStream>();
		for (int index : fileIndex)
		{
			try
			{
				streams.add(torrentManager.getFile(refID, index));
			}
			catch (FileNotFoundException e)
			{
				if (Util.DEBUG)
					e.printStackTrace();
				System.err.println("couldn't find the file to transfer... skipping");
				return;
			}
			catch (TorrentNotFoundException e)
			{
				if (Util.DEBUG)
					e.printStackTrace();
				System.err.println("the requested torrent does not exist");
				return;
			}
			catch (IncompleteFileException e)
			{
				if (Util.DEBUG)
					e.printStackTrace();
				System.err.println("the requested file is not completely downloaded yet!");
			}
		}
		clients.get(clientID).transferFiles(refID, fileIndex, streams);
	}

	/**
	 * @see edu.iastate.cs309.comminterfaces.IServer#shutdown()
	 */
	@Override
	public void shutdown()
	{
		log.log(Level.FINE, "Shutting down due to client request");

		exiting = true;
		if (serverSocket != null)
		{
			try
			{
				serverSocket.close();
			}
			catch (IOException e)
			{
				log.log(Level.WARNING, "IO error while trying to close socket", e);
			}
		}
	}

	/**
	 * Load torrent info into torrentManager from a file.
	 */
	private void loadTorrentInfo()
	{
		BEncodedObject data = null;
		try
		{
			FileInputStream dataFile = new FileInputStream(TORRENT_DATA_FILE);
			data = BEncodedObject.readObject(dataFile);
		}
		catch (IOException e)
		{
			log.log(Level.WARNING, "Couldn't read from torrent status file", e);
			return;
		}
		catch (ParseException e)
		{
			log.log(Level.WARNING, "Couldn't parse torrent status file");
			return;
		}

		try
		{
			for (BEncodedObject torrent : ((BList) data).get())
			{
				BList torrentData = (BList) torrent;
				BEncodedObject[] arr = torrentData.get();
				//String name = ((ByteString) arr[0]).toString();
				File torrentFile = new File(((ByteString) arr[0]).toString());
				torrentManager.addTorrent(torrentFile);
				//BInteger active = (BInteger) arr[1];
				// We don't even use this
				//torrentManager.getProp(refID).setActive(active.get() == 1);
			}
		}
		catch (ClassCastException e)
		{
			log.log(Level.SEVERE, "Torrent status file is corrupt, exiting");
			//TODO (FindBugs) This is not a terminal error, should be returned to the user.
			System.exit(1);
		}
	}

	/**
	 * Save torrent info from torrentManager into a file.
	 * 
	 * @param files
	 */
	private void saveTorrentInfo(List<String> files)
	{
		log.log(Level.FINER, "Saving torrent info to file");
		BList torrents = new BList();
		for (String s : files)
		{
			BList torrentData = new BList();

			//			try
			//			{
			//TorrentInfo info = torrentManager.getInfo(i);
			//TorrentProp props = torrentManager.getProp(i);

			//torrentData.add(new ByteString(info.getName()));
			//torrentData.add(new BInteger(props.isActive() ? 1 : 0));
			torrentData.add(new ByteString(s));
			//						}
			//						catch (TorrentNotFoundException e)
			//						{
			//							if (Util.DEBUG)
			//								e.printStackTrace();
			//							log.log(Level.WARNING, "Client tried to getInfo and getProp on a nonexistent torrent", e);
			//						}

			torrents.add(torrentData);
		}
		try
		{
			FileOutputStream dataFile = new FileOutputStream(TORRENT_DATA_FILE);
			torrents.bEncode(dataFile);
			dataFile.close();
		}
		catch (IOException e)
		{
			log.log(Level.WARNING, "Couldn't write torrent status file", e);
		}
	}

	/**
	 * @see edu.iastate.cs309.comminterfaces.IServer#removeTorrent(int)
	 */
	@Override
	public void removeTorrent(int refID) throws FailedSendMessageException
	{
		log.log(Level.FINE, "Removing torrent with refID " + refID);

		try
		{
			torrentManager.removeTorrent(refID);
			for (Client c : clients.values())
				sendTorrentList(c);
		}
		catch (TorrentNotFoundException e)
		{
			if (Util.DEBUG)
				e.printStackTrace();
			log.log(Level.WARNING, "Client tried to remove nonexistent torrent", e);
		}
	}

	/**
	 * 
	 */
	public void updateStatus()
	{
		for (int refID : torrentManager.getTorrents())
		{
			for (Client c : clients.values())
			{
				try
				{
					sendTorrentInfo(refID, c);
				}
				catch (FailedMessageException e)
				{
					log.log(Level.WARNING, "Failed to send a status update to a client", e);
				}
			}
		}
	}

	/**
	 * @return Whether status updates need to be sent to clients.
	 */
	public boolean needStatusUpdates()
	{
		return torrentManager.getTorrents().size() != 0 && clients.size() != 0;
	}

	/**
	 * Called whenever a new torrent needs to be reported to the user.
	 */
	public void torrentListUpdated()
	{
		for (Client c : clients.values())
			try
			{
				sendTorrentList(c);
			}
			catch (FailedSendMessageException e)
			{
			}
		makeStatusUpdater();
	}
}
