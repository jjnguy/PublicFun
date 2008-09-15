/**
 * 
 */
package edu.iastate.cs309.guiElements;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import edu.iastate.cs309.client.TorrentInformationContainer;

/**
 * A table for displaying information about torrents.
 * 
 * @author Michael Seibert
 */
@SuppressWarnings("serial")
public class TorrentTable extends JTable
{
	private int cellMargin;

	private TorrentTableHeadderRightClickPopupMenu pop;

	/**
	 * Default torrent table
	 */
	public TorrentTable()
	{
		super(new TorrentTableModel());
		setAutoCreateColumnsFromModel(true);
		setAutoCreateRowSorter(true);
		setGridColor(Color.WHITE);
		cellMargin = getIntercellSpacing().width;
		setIntercellSpacing(new Dimension(0, getIntercellSpacing().height));
		pop = new TorrentTableHeadderRightClickPopupMenu(getModel());
	}

	/**
	 * @see javax.swing.JTable#prepareRenderer(javax.swing.table.TableCellRenderer,
	 *      int, int)
	 */
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
	{
		Component comp = super.prepareRenderer(renderer, row, column);
		if (!(comp instanceof JComponent))
			return comp;
		JComponent jcomp = (JComponent) comp;
		jcomp.setBorder(BorderFactory.createEmptyBorder(0, cellMargin, 0, cellMargin));
		return jcomp;
	}

	/**
	 * @see javax.swing.JTable#getModel()
	 */
	@Override
	public TorrentTableModel getModel()
	{
		return (TorrentTableModel) super.getModel();
	}

	public TorrentInformationContainer getTorrent(int index)
	{
		return getModel().getTorrent(index);
	}

	public TorrentInformationContainer getSelectedTorrent()
	{
		if (getSelectedRow() == -1)
			return null;
		return getModel().getTorrent(getSelectedRow());
	}

	/**
	 * @param torrent
	 */
	public void add(TorrentInformationContainer torrent)
	{
		getModel().add(torrent);
	}

	/**
	 * @param listener
	 *            the listner to be associated with this TorrentTable
	 */
	public void addRightClickListener(TorrentRightClickListener listener)
	{
		addMouseListener(new TorrentListenerWrapper(listener));
		//getTableHeader().addMouseListener(headerRightClickListener); TODO should prolly maybe make this work
	}

	private MouseListener headerRightClickListener = new MouseAdapter()
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			if (e.getButton() == 1)
				return;
			pop.show(e.getComponent(), e.getX(), e.getY());
		}
	};

	private class TorrentListenerWrapper extends MouseAdapter
	{
		private final TorrentRightClickListener listener;

		/**
		 * @param listener
		 */
		public TorrentListenerWrapper(TorrentRightClickListener listener)
		{
			this.listener = listener;
		}

		/**
		 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent e)
		{
			if (e.isPopupTrigger())
				clicked(e);
		}

		/**
		 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent e)
		{
			Point clickLoc = e.getPoint();
			int displayRow = rowAtPoint(clickLoc);
			getSelectionModel().setSelectionInterval(displayRow, displayRow);
			if (e.isPopupTrigger())
				clicked(e);
		}

		/**
		 * @param e
		 */
		private void clicked(MouseEvent e)
		{
			Point clickLoc = e.getPoint();
			int displayRow = rowAtPoint(clickLoc);
			int logicalRow = getRowSorter().convertRowIndexToModel(displayRow);
			listener.actionPerformed(e, getModel().getTorrent(logicalRow));
		}
	}

	/**
	 * @param refID
	 * @return weather or not the remove was successful
	 */
	public boolean removeTorrent(int refID)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public void updateList(List<TorrentInformationContainer> allTorrents)
	{
		getModel().updateTorrentList(allTorrents);
	}
}
