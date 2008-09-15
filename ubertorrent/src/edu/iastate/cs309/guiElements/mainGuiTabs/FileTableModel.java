package edu.iastate.cs309.guiElements.mainGuiTabs;

import java.util.Arrays;

import javax.swing.table.AbstractTableModel;

import edu.iastate.cs309.client.TorrentInformationContainer;

public class FileTableModel extends AbstractTableModel
{
	private final int numCols = 4;
	private TorrentInformationContainer info;

	public FileTableModel()
	{
		super();
	}

	public void setInfo(TorrentInformationContainer infoP)
	{
		info = infoP;
	}

	@Override
	public int getColumnCount()
	{
		return numCols;
	}

	@Override
	public int getRowCount()
	{
		if (info == null)
			return 0;
		return info.info.getIndividualFileNames().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		if (info == null)
			return "";
		if (columnIndex == 0)
			return info.info.getIndividualFileNames().get(rowIndex);
		if (columnIndex == 1)
			return info.info.getFilesizes().get(rowIndex);
		if (columnIndex == 2)
			return Arrays.toString(info.info.getProgress());// TODO justin make this look hot!
		return null;
	}

	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column)
	{
		if (column == 0)
			return "File Name";
		if (column == 0)
			return "Size";
		if (column == 0)
			return "Progress";
		return null;
	}
}
