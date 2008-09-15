package edu.iastate.cs309.guiElements;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import edu.iastate.cs309.plugins.PluginManager;
import edu.iastate.cs309.plugins.exceptions.PluginConflictsException;
import edu.iastate.cs309.plugins.exceptions.PluginVersionCompatException;
import edu.iastate.cs309.util.Util;

/**
 * A simple frame for letting users add new plugins to Uebertorrent
 * 
 * @author Justin
 * 
 */
@SuppressWarnings("serial")
public class AddPluginFrame extends JFileChooser
{
	private PluginManager mgr;

	/**
	 * Create and display a new AddPluginFrame
	 * 
	 * @param mgrP
	 *            the PluginManager that this frame will use to interface with
	 *            the MainGui
	 */
	public AddPluginFrame(PluginManager mgrP)
	{
		mgr = mgrP;
		setMultiSelectionEnabled(true);
		// Open a file chooser
		setCurrentDirectory(new File("bin"));
		setFileFilter(new FileFilter()
		{

			@Override
			public boolean accept(File f)
			{
				if (f.isDirectory())
					return true;
				return f.getName().toLowerCase().endsWith(".uberplug");
			}

			@Override
			public String getDescription()
			{
				return "*.uberplug (Ubertorrent plugin files)";
			}
		});

		setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		setVisible(true);
	}

	/**
	 * @return true if the add was successful, false otherwise
	 */
	public boolean addPlugin()
	{
		try
		{
			for (File file : getSelectedFiles())
			{
				mgr.addPlugin(file);
			}
		}
		catch (PluginConflictsException e1)
		{
			JOptionPane.showMessageDialog(AddPluginFrame.this, "The plugin could not be installed.  It failed with the following message:\n" + e1.getMessage(), "Add Plugin Failed", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		catch (PluginVersionCompatException e1)
		{
			JOptionPane.showMessageDialog(AddPluginFrame.this, "The plugin could not be installed.\nThe version of " + Util.getUber() + " is not compatible with the plugin.", "Add Plugin Failed", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		catch (IOException e1)
		{
			JOptionPane.showMessageDialog(AddPluginFrame.this, "The plugin could not be installed.\nThere was a file access error", "Add Plugin Failed", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
}
