package edu.iastate.cs309.guiElements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.iastate.cs309.client.TheActualClient;
import edu.iastate.cs309.client.TorrentInformationContainer;

public class TorrentRightClickPopupMenu extends JPopupMenu
{
	private TorrentInformationContainer tInfo;
	private TheActualClient clientP;
	private MainGui par;

	public TorrentRightClickPopupMenu(MainGui parP, TorrentInformationContainer info, TheActualClient client)
	{
		par = parP;
		clientP = client;
		tInfo = info;
		JMenuItem torrentPropMenuItem = new JMenuItem("Properties...");
		torrentPropMenuItem.addActionListener(torentPropAction);
		add(torrentPropMenuItem);
		JMenuItem removeTorrentMenuItem = new JMenuItem("Remove");
		removeTorrentMenuItem.addActionListener(removeTorrent);
		JMenuItem transferMenuItem = new JMenuItem("Transfer Files");
		add(transferMenuItem);
		//transferMenuItem.setEnabled(info.info.getBytesDownloaded() == info.info.getSize());
		transferMenuItem.addActionListener(transferFiles);
		add(removeTorrentMenuItem);
	}

	private ActionListener torentPropAction = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{//TODO need to pop up a menu to change props also, need to add the option to transfer file from server justin
			TorrentPropertyChangerFrame propChange = new TorrentPropertyChangerFrame(tInfo, null);
		}
	};
	private Action removeTorrent = new AbstractAction()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			clientP.removeTorrent(tInfo.refID);
		}
	};
	private ActionListener transferFiles = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			List<Integer> indexes = new ArrayList<Integer>(tInfo.info.getIndividualFileNames().size());
			par.getClient().getFile(tInfo.refID, indexes);
		}
	};
}
