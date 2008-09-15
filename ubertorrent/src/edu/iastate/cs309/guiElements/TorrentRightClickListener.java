/**
 * 
 */
package edu.iastate.cs309.guiElements;

import java.awt.event.MouseEvent;

import edu.iastate.cs309.client.TorrentInformationContainer;

/**
 * An interface for getting right click info from the TorrentTable
 * 
 * @author Michael Seibert
 */
public interface TorrentRightClickListener
{
	/**
	 * @param e
	 *            the mouse event that spawned the event
	 * @param torrent
	 * 
	 */
	public void actionPerformed(MouseEvent e, TorrentInformationContainer torrent);
}
