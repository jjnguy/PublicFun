package edu.iastate.cs309.clientserverprotocol;

/**
 * All message codes for client/server interaction
 * <p>
 * This should only be used internally by network implementations of Client and
 * Server
 */
public enum MessageCode
{
	/**
	 * fake value assigned when ordinal not in MessageCode.getValues()[]
	 */
	BAD_MID,
	
	/** ****************** SERVER CODES *********************** */
	/**
	 * List of updated torrent properties
	 */
	SRV_TORPROP,

	/**
	 * A bad torrent was sent by the client
	 */
	SRV_BADTOR,

	/**
	 * Reference ID for a just added torrent
	 */
	SRV_RID,

	/**
	 * List of updated torrent information
	 */
	SRV_TORINFO,

	/**
	 * List of torrents on server
	 */
	SRV_TORLIST,

	/**
	 * List of server properties
	 */
	SRV_PROP,

	/**
	 * File available
	 */
	SRV_FILE,

	/**
	 * torrent removed
	 */
	SRV_TORREMOVED,

	/**
	 * Torrent started successfully
	 */
	SRV_STARTOK,

	/**
	 * torrent failed to start
	 */
	SRV_STARTFAILED,

	/**
	 * torrent stopped successfully
	 */
	SRV_STOPOK,

	/**
	 * torrent failed to stop
	 */
	SRV_STOPFAILED,
	
	/**
	 * server's current config
	 */
	SRV_CONFIG,
	
	/**
	 * ready to recieve a torrent
	 */
	SRV_READY_FOR_TORRENT,

	/** ******************* CLIENT CODES ******************** */

	/**
	 * Send a torrent
	 */
	MSG_ADDTOR,

	/**
	 * Get current torrents
	 */
	MSG_TORLIST,
	
	/**
	 * set torrent information
	 */
	MSG_SETTORPROP,
	
	/**
	 * get torrent properties
	 */
	MSG_GETTORPROP,

	/**
	 * Get torrent information
	 */
	MSG_GETTORINFO,

	/**
	 * Get server config
	 */
	MSG_GETSERVERCONFIG,

	/**
	 * Send new server config
	 */
	MSG_SETSERVERCONFIG,

	/**
	 * Stop torrent by refId
	 */
	MSG_STOPTOR,

	/**
	 * Start downloading torrent by refId
	 */
	MSG_STARTTOR,

	/**
	 * Request files from torrent by refId and file index
	 */
	MSG_GETTOR,
	
	/**
	 * remove a torrent
	 */
	MSG_REMOVETOR,
	
	/**
	 * shut down the server
	 */
	MSG_SHUTDOWN
}
