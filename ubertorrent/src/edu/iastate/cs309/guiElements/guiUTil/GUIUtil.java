package edu.iastate.cs309.guiElements.guiUTil;

import java.awt.Image;
import java.awt.Window;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * GUIUtil contains helpful info and methods regarding GUI components
 * 
 * @author Justin
 * 
 */
public class GUIUtil
{

	/**
	 * The folder in the project where the images are located
	 */
	public static final String imagesFolder = "src/images";
	/**
	 * A more specific path specifying where the default tab icons are located
	 */
	public static final String tabImageFOlder = "src/images/tabIcons";
	/**
	 * A more specific path specifying where the default toolbar icons are
	 * located
	 */
	public static final String toolBarIconFolder = "src/images/toolbarIcons";

	/**
	 * A convenience method for setting the look and feel of the GUI.
	 */
	public static void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Setting the look and feel did not go smoothly.  Epic fail...I dunno why.");
		}
	}

	/**
	 * A convenience method for setting a window to have the Uebertorrent logo
	 * 
	 * @param toSet
	 *            the window that will be fitted with a shiny logo
	 */
	public static void setUberImageIcon(Window toSet)
	{
		Image cornerImage = null;
		try
		{
			cornerImage = ImageIO.read(new File(GUIUtil.imagesFolder + "/" + "Umlaut.png"));
		}
		catch (IOException e)
		{
			// The image will remain as the default
		}
		toSet.setIconImage(cornerImage);
	}

}
