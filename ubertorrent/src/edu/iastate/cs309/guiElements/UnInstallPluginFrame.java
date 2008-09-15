package edu.iastate.cs309.guiElements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.iastate.cs309.plugins.PluginManager;
import edu.iastate.cs309.util.Util;

/**
 * 
 * A simple frame for uninstalling plugins
 * 
 * @author Justin
 * 
 */
@SuppressWarnings("serial")
public class UnInstallPluginFrame extends JDialog
{
	private PluginManager pManager;
	private JButton remove;

	private JPanel mainPane;
	private JComboBox listOfPossiblePlugins;
	private JLabel noPlugins;
	private MainGui owner;

	/**
	 * Creates and displays an UnInstallPluginFrame
	 * 
	 * @param pmgr
	 *            the PluginManager to communicate with
	 * @param owner
	 *            the owner of this frame
	 */
	public UnInstallPluginFrame(PluginManager pmgr, MainGui ownerP)
	{
		super(ownerP);
		owner = ownerP;
		pManager = pmgr;

		listOfPossiblePlugins = new JComboBox(pManager.getInstalledPlugins().toArray());
		listOfPossiblePlugins.addKeyListener(enterPressed);
		noPlugins = new JLabel("There are no plugins to remove");

		remove = new JButton("remove");
		remove.addActionListener(removeAction);
		remove.addKeyListener(enterPressed);

		mainPane = new JPanel();
		if (listOfPossiblePlugins.getItemCount() == 0)
		{
			mainPane.add(noPlugins);
			remove.setEnabled(false);
		}
		else
		{
			mainPane.add(listOfPossiblePlugins);
		}
		mainPane.add(remove);

		add(mainPane);
		setModal(true);
		pack();
		setVisible(true);
	}

	private KeyListener enterPressed = new KeyListener()
	{

		@Override
		public void keyPressed(KeyEvent e)
		{
			remove.doClick();
		}

		@Override
		public void keyReleased(KeyEvent e)
		{
		}

		@Override
		public void keyTyped(KeyEvent e)
		{

		}
	};
	private ActionListener removeAction = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (Util.DEBUG)
				System.out.println(listOfPossiblePlugins);
			pManager.uninstallPlugin((File) listOfPossiblePlugins.getSelectedItem());
			listOfPossiblePlugins.removeItemAt(listOfPossiblePlugins.getSelectedIndex());
			if (listOfPossiblePlugins.getItemCount() == 0)
			{
				dispatchEvent(new WindowEvent(UnInstallPluginFrame.this, WindowEvent.WINDOW_CLOSING));
			}
		}
	};
}
