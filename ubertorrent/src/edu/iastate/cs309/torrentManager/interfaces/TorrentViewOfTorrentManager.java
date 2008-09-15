package edu.iastate.cs309.torrentManager.interfaces;

import edu.iastate.cs309.torrentManager.containers.PeerID;

/**
 * Mask TorrentManager to see only what Torrent needs to call
 * @author sralmai
 *
 */
public interface TorrentViewOfTorrentManager
{
	public abstract PeerID getPeerID();
	
	public abstract long getTimeout();
	
	public abstract int getPort();
	
	public abstract String getRootDir();

}
