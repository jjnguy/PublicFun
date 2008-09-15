package edu.iastate.cs309.torrentManager.containers;

/**
 * Container for information taken from a handshake (used by TorrentManager to
 * place a new peer connection)
 * 
 * @author singularity
 */
public class HandshakeInfo
{
	private InfoHash iHash;
	private PeerID peerID;

	/**
	 * create a HandshakeInfo object immutable properties
	 * 
	 * @param i infohash
	 * @param p peerID
	 */
	public HandshakeInfo(final InfoHash i, final PeerID p)
	{
		iHash = i;
		peerID = p;
	}

	/**
	 * 
	 * @return InfoHash in this HandshakeInfo
	 */
	public InfoHash getInfoHash()
	{
		return iHash;
	}

	/**
	 * 
	 * @return PeerID in this HandshakeInfo
	 */
	public PeerID getPeerID()
	{
		return peerID;
	}

}
