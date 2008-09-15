package edu.iastate.cs309.guiElements.mainGuiTabs;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import edu.iastate.cs309.guiElements.guiUTil.GUIUtil;

/**
 * A PeerInfoTab contains information about a Torrent's peers
 * 
 * @author Justin
 * 
 */
@SuppressWarnings("serial")
public class PeerInfoTab extends UberTab
{
	/**
	 * Super simple
	 */
	public PeerInfoTab()
	{

	}

	/**
	 * @see edu.iastate.cs309.guiElements.mainGuiTabs.UberTab#getTabName()
	 */
	@Override
	public String getTabName()
	{
		// TODO Auto-generated method stub
		return "Peer Info";
	}

	/**
	 * @see edu.iastate.cs309.guiElements.mainGuiTabs.UberTab#getTabToolTipText()
	 */
	@Override
	public String getTabToolTipText()
	{
		// TODO Auto-generated method stub
		return "Info about peers";
	}

	/**
	 * @see edu.iastate.cs309.guiElements.mainGuiTabs.UberTab#getTabIcon()
	 */
	@Override
	public Icon getTabIcon()
	{
		// TODO Auto-generated method stub
		return new ImageIcon(GUIUtil.tabImageFOlder + "/" + "PeerLogo.png");
	}

}
