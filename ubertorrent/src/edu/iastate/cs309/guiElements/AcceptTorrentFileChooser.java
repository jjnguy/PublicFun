package edu.iastate.cs309.guiElements;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * This FileChooser only accepts folders or files that end in .torrent
 * 
 * @author Justin
 * 
 */
@SuppressWarnings("serial")
public class AcceptTorrentFileChooser extends JFileChooser
{
	/**
	 * Creates a default AcceptTorrentFileChooser
	 */
	public AcceptTorrentFileChooser()
	{
		setFileSelectionMode(JFileChooser.FILES_ONLY);
		setMultiSelectionEnabled(true);
		setFileFilter(new FileFilter()
		{
			@Override
			public boolean accept(File f)
			{
				if (f.isDirectory())
					return true;
				String path = f.getAbsolutePath();
				return path.endsWith(".torrent");
			}

			@Override
			public String getDescription()
			{
				return ".torrent files";
			}
		});
	}
}
