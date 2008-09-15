/**
 * 
 */
package edu.iastate.cs309.guiElements;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import edu.iastate.cs309.client.TorrentInformationContainer;
import edu.iastate.cs309.communication.TorrentInfo;
import edu.iastate.cs309.torrentManager.containers.Bitfield;
import edu.iastate.cs309.torrentManager.exceptions.MalformedBitfieldException;
import edu.iastate.cs309.util.Util;

/**
 * This TableModel is used in conjunction with TorrentTable. It allows for
 * adding and removing of columns and torrents. It can have columns added to it
 * from outside sources
 * 
 * @author Michael Seibert, Justin Nelson
 */
@SuppressWarnings("serial")
public class TorrentTableModel extends AbstractTableModel
{
	private List<TorrentInformationContainer> torrents;

	/**
	 * a list of all the possible columns that could be shown
	 */
	List<String> possibleColNames;
	/**
	 * a list of the currently visible columns
	 */
	List<String> shownColNames;

	// TODO should have a list of values that the colNames correspond to

	/**
	 * Constructs a default TorrentTableModel. Adds 5 columns by default (Name,
	 * Speed, Size, Bytes Downloaded, and Percent Done). Also makes all of them
	 * visible by default.
	 */
	public TorrentTableModel()
	{
		super();
		torrents = new ArrayList<TorrentInformationContainer>();
		possibleColNames = new ArrayList<String>();
		possibleColNames.add("Name");
		possibleColNames.add("Speed");
		possibleColNames.add("Size");
		possibleColNames.add("Bytes Downloaded");
		possibleColNames.add("Percent Done");
		possibleColNames.add("ETA");
		shownColNames = new ArrayList<String>(possibleColNames);
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount()
	{
		return shownColNames.size();
	}

	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount()
	{
		return torrents.size();
	}

	/**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		if (columnIndex > getColumnCount())
			return null;
		if (rowIndex > torrents.size())
			return null;
		// TODO can't be hard coded
		TorrentInfo torrent = torrents.get(rowIndex).info;
		switch (columnIndex)
		{
		case 0:
			return torrent != null ? torrent.getName() : "No name Info Found";
		case 1:
			return torrent != null ? torrent.getDownloadSpeed() + " B/s" : "No Info Found";
		case 2:
			return torrent != null ? String.format("%.2f", torrent.getSize() / 1024.0) + " Kib" : "No Info Found";
		case 3:
			return torrent != null ? torrent.getBytesDownloaded() : "No Info Found";
		case 4:
			if (torrent == null)
				return "VERY FAST!!!";
			Bitfield prog = null;
			if (torrent.getNumPieces() <= 0)
				return "Progress cannot be calculated yet";
			try
			{
				prog = new Bitfield(torrent.getProgress(), torrent.getNumPieces());
			}
			catch (MalformedBitfieldException e)
			{
				if (Util.DEBUG)
					e.printStackTrace();
			}
			double percent = prog.totalSet() / (double) torrent.getNumPieces() * 100;
			return String.format("%.2f", percent);
		case 5:
			if (torrent == null)
				return '\u221E';
			if (torrent.getComplete())
				return "Done";
			int speed = torrent.getDownloadSpeed();
			long bytesLeft = torrent.getSize() - torrent.getBytesDownloaded();
			if (speed <= 0.1)
				return '\u221E';
			long timeInMili = (bytesLeft / speed) * 1000;
			return Util.getTimeStringFromMiliseconds(timeInMili);
		default:
			return null;
		}
	}

	/**
	 * Adds a new torrent to the list of active torrents
	 * 
	 * @param info
	 *            the Torrent to be added to the list
	 */
	public void add(TorrentInformationContainer info)
	{
		torrents.add(info);
		fireTableRowsInserted(torrents.size() - 1, torrents.size() - 1);
	}

	/**
	 * @param index
	 *            The index to remove the torrent from.
	 * @return the TorrentInfo that was removed
	 */
	public TorrentInformationContainer remove(int index)
	{
		fireTableRowsDeleted(index, index);
		return torrents.remove(index);
	}

	/**
	 * Adds a new column to the table
	 * 
	 * @param name
	 *            the name of the new column
	 */
	public void addColumn(String name)
	{
		possibleColNames.add(name);
		shownColNames.add(name);
	}

	/**
	 * @param name
	 */
	public void showColumn(String name)
	{
		shownColNames.add(name);
		fireTableDataChanged();
		fireTableStructureChanged();
	}

	/**
	 * @param name
	 */
	public void hideColumn(String name)
	{
		if (shownColNames.remove(name))
		{
			fireTableDataChanged();
			fireTableStructureChanged();
		}
	}

	/**
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}

	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column)
	{
		if (column > shownColNames.size())
			return null;
		return shownColNames.get(column);
	}

	/**
	 * @param other
	 * @return whether or not the model contains the supplied TorrentInfo
	 */
	public boolean contains(TorrentInformationContainer other)
	{
		return torrents.contains(other);
	}

	/**
	 * @param index
	 * @return the TorrentInfo at the given index
	 */
	public TorrentInformationContainer getTorrent(int index)
	{
		return torrents.get(index);
	}

	public void updateTorrentList(List<TorrentInformationContainer> allTorrents)
	{
		List<TorrentInformationContainer> toRemove = new LinkedList<TorrentInformationContainer>(torrents);
		// If it's in allTorrents, then we DON'T want to remove it.
		toRemove.removeAll(allTorrents);

		// Remove them.
		for (TorrentInformationContainer t : toRemove)
			remove(torrents.indexOf(t));

		List<TorrentInformationContainer> toAdd = new LinkedList<TorrentInformationContainer>(allTorrents);
		// If it's already in the table, then we don't need to add it again.
		toAdd.removeAll(torrents);

		// Add them.
		for (TorrentInformationContainer t : toAdd)
			add(t);
	}
}
