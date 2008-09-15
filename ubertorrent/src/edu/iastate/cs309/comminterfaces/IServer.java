/**
 * 
 */
package edu.iastate.cs309.comminterfaces;

import java.io.InputStream;
import java.util.List;

import edu.iastate.cs309.clientserverprotocol.exceptions.FailedMessageException;
import edu.iastate.cs309.communication.ServerProperties;
import edu.iastate.cs309.communication.TorrentProp;

/**
 * A Server object must implement these methods (and hopefully send something
 * back). A Client object can call these methods.
 * 
 * @author Michael Seibert
 */
public interface IServer
{
	/**
	 * Instructs the server to add a new torrent.
	 * 
	 * The new torrent will be added, and the client will be informed of the new
	 * torrent via the {@link Client#torrentList} method.
	 * 
	 * @param dotTorrentFile
	 *            An InputStream containing the data of the torrent to be added.
	 * 
	 * @param name
	 *            of torrent file
	 * @throws FailedMessageException
	 *             If the message could not be successfully sent.
	 */
	void addTorrent(String name, InputStream dotTorrentFile) throws FailedMessageException;

	/**
	 * Changes a setting for a torrent.
	 * 
	 * The setting will be changed, and the client will be informed of the
	 * changed setting via the {@link Client#updateTorrentProperties} method.
	 * 
	 * @param refID
	 *            The ID of the torrent whose properties are being changed
	 * @param prop
	 *            The new torrent properties
	 * @throws FailedMessageException
	 *             If the message could not be successfully sent.
	 */
	void changeTorrentProp(int refID, TorrentProp prop) throws FailedMessageException;

	/**
	 * Changes a setting on the server.
	 * 
	 * The setting will be changed, and the client will be informed of the
	 * changed setting via the {@link Client#updateServerStatus} method.
	 * 
	 * @param setting
	 *            The new server setting
	 * @throws FailedMessageException
	 *             If the message could not be successfully sent.
	 */
	void changeServerProp(ServerProperties setting) throws FailedMessageException;

	/**
	 * Instructs the server that the client would like to transfer the data of
	 * one or more files from the server to the client.
	 * 
	 * The data will be sent asynchronously via the {@link Client#transferFiles}
	 * method, so that the client remains responsive while the list is sent.
	 * 
	 * @param clientID
	 *            The ID of the client requesting the information.
	 * @param refID
	 *            The ID of the torrent to be transferred
	 * @param fileIndex
	 *            The indexes of the files within the torrent to download
	 * @throws FailedMessageException
	 *             If the message could not be successfully sent.
	 */
	void transferTorrent(int clientID, int refID, List<Integer> fileIndex) throws FailedMessageException;

	//void pluginMessage(byte[] message) throws FailedMessageException;

	/**
	 * Instructs the server that the client would like a list of all information
	 * about a torrent.
	 * 
	 * The list will be sent asynchronously via the
	 * {@link Client#updateTorrentInformation} method, so that the client
	 * remains responsive while the list is sent.
	 * 
	 * @throws FailedMessageException
	 *             If the message could not be successfully sent.
	 */
	//void getServerStatus() throws FailedMessageException;
	/**
	 * Instructs the server that the client would like a list of all information
	 * about a torrent.
	 * 
	 * The list will be sent asynchronously via the
	 * {@link Client#updateTorrentInformation} method, so that the client
	 * remains responsive while the list is sent.
	 * 
	 * @param clientID
	 *            The ID of the client requesting the information.
	 * @param refID
	 *            The ID of the torrent whose properties are requested.
	 * @throws FailedMessageException
	 *             If the message could not be successfully sent.
	 */
	void getTorrentInfo(int clientID, int refID) throws FailedMessageException;

	/**
	 * Instructs the server that the client would like a list of all properties
	 * of a torrent.
	 * 
	 * The list will be sent asynchronously via the
	 * {@link Client#updateTorrentProperties} method, so that the client remains
	 * responsive while the list is sent.
	 * 
	 * @param clientID
	 *            The ID of the client requesting the information.
	 * @param refID
	 *            The ID of the torrent whose properties are requested.
	 * @throws FailedMessageException
	 *             If the message could not be successfully sent.
	 */
	void getTorrentProp(int clientID, int refID) throws FailedMessageException;

	/**
	 * Instructs the server that the client would like a list of all server
	 * properties.
	 * 
	 * The list will be sent asynchronously via the
	 * {@link Client#updateServerStatus} method, so that the client remains
	 * responsive while the list is sent.
	 * 
	 * @param clientID
	 *            The ID of the client requesting the information.
	 * @throws FailedMessageException
	 *             If the message could not be successfully sent.
	 */
	void getServerProp(int clientID) throws FailedMessageException;

	/**
	 * Instructs the server that the client would like a list of all torrents.
	 * 
	 * The list will be sent asynchronously via the {@link Client#torrentList}
	 * method, so that the client remains responsive while the list is sent.
	 * 
	 * @param clientID
	 *            The ID of the client requesting the information.
	 * @throws FailedMessageException
	 *             If the message could not be successfully sent.
	 */
	void getAllTorrents(int clientID) throws FailedMessageException;

	/**
	 * Shut down the server
	 * 
	 * @throws FailedMessageException
	 *             if message could not be sent
	 */
	void shutdown() throws FailedMessageException;

	/**
	 * Removes the torrent with the specified refID
	 * 
	 * @param refID
	 * @throws FailedMessageException 
	 */
	void removeTorrent(int refID) throws FailedMessageException;
}
