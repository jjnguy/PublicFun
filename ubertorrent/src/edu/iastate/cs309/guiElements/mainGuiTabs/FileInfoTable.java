package edu.iastate.cs309.guiElements.mainGuiTabs;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import edu.iastate.cs309.client.TorrentInformationContainer;

public class FileInfoTable extends JTable
{
	private int cellMargin;

	public FileInfoTable()
	{
		super(new FileTableModel());
		cellMargin = getIntercellSpacing().width;
		setAutoCreateColumnsFromModel(true);
		setAutoCreateRowSorter(true);
		setIntercellSpacing(new Dimension(0, getIntercellSpacing().height));
	}

	/**
	 * @see javax.swing.JTable#getModel()
	 */
	@Override
	public FileTableModel getModel()
	{
		return (FileTableModel) super.getModel();
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

	public void setInfo(TorrentInformationContainer infoP)
	{
		getModel().setInfo(infoP);

	}
}
