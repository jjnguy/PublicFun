/**
 * 
 */
package edu.iastate.cs309.comminterfaces;

import java.io.InputStream;
import java.util.List;

import edu.iastate.cs309.clientserverprotocol.exceptions.FailedMessageException;
import edu.iastate.cs309.clientserverprotocol.exceptions.FailedSendMessageException;
import edu.iastate.cs309.communication.ServerProperties;
import edu.iastate.cs309.communication.TorrentInfo;
import edu.iastate.cs309.communication.TorrentProp;

/**
 * The unified Client interface for the client/server protocol. This is
 * implemented in two places: (1) the server's ClientConnection, which
 * implements these calls over the network, and (2) the Client callback object,
 * which the ServerConnectionCallback thread calls these messages on after
 * reading from the network.
 * 
 * The idea is that the network layer is transparent.
 * 
 * @author Michael Seibert
 * @author sralmai
 */
public interface Client
{
	/**
	 * Send the current TorrentProp information from the server to the client.
	 * 
	 * @param refID
	 *            the ID of the torrent of intrest
	 * @param prop
	 *            the property that is associated with the refID
	 * @throws FailedMessageException
	 */
	void updateTorrentProperties(int refID, TorrentProp prop) throws FailedMessageException;

	/**
	 * Send the current TorrentInfo information from the server to the client.
	 * refID)
	 * 
	 * @param refID
	 *            the ID of the torrent of intrest
	 * @param info
	 *            the info that is associated with the refID
	 * @throws FailedMessageException
	 */
	void updateTorrentInformation(int refID, TorrentInfo info) throws FailedMessageException;

	/**
	 * Send an updated ServerProperties object containing the current
	 * configuration from the torrent to the server
	 * 
	 * @param settings
	 *            the current settings of the Server
	 * @throws FailedMessageException
	 */
	void updateServerStatus(ServerProperties settings) throws FailedMessageException;

	/**
	 * 
	 * 
	 * @param refID
	 *            the ID of the torrent of intrest
	 * @param fileIndexes
	 *            Indexes of the files within the torrent specified by refID
	 * @param data
	 *            the raw data of the file to be transfered to the Client
	 * @throws FailedMessageException
	 */
	void transferFiles(int refID, List<Integer> fileIndexes, List<InputStream> data) throws FailedMessageException;

	/**
	 * @param refID
	 *            the ID of the torrent of intrest
	 * @throws FailedMessageException
	 */
	void torrentRemoved(int refID) throws FailedMessageException;

	/**
	 * 
	 * @param allTorrents
	 *            a list of refIDs of all current torrents
	 * @throws FailedSendMessageException
	 */
	void torrentList(List<Integer> allTorrents) throws FailedSendMessageException;
}
