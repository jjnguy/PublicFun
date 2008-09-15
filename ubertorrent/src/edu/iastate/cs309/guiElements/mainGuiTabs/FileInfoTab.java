package edu.iastate.cs309.guiElements.mainGuiTabs;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import edu.iastate.cs309.client.TorrentInformationContainer;
import edu.iastate.cs309.guiElements.guiUTil.GUIUtil;

public class FileInfoTab extends UberTab
{
	private FileInfoTable ftable;

	public FileInfoTab()
	{
		ftable = new FileInfoTable();
		setLayout(new BorderLayout());
		add(ftable, BorderLayout.CENTER);
	}

	public void setInfo(TorrentInformationContainer infoP)
	{
		ftable.setInfo(infoP);
	}

	@Override
	public Icon getTabIcon()
	{
		return new ImageIcon(GUIUtil.tabImageFOlder + File.separator + "filesIcon.png");
	}

	@Override
	public String getTabName()
	{
		return "Files";
	}

	@Override
	public String getTabToolTipText()
	{
		return "Information regarding the files in the torrent file";
	}

}
