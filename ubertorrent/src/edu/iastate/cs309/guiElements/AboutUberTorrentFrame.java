// TODO needs to not suck
package edu.iastate.cs309.guiElements;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.GroupLayout.Alignment;

import edu.iastate.cs309.guiElements.guiUTil.GUIUtil;
import edu.iastate.cs309.guiElements.lowerlevel.Box;
import edu.iastate.cs309.guiElements.lowerlevel.HBox;
import edu.iastate.cs309.guiElements.lowerlevel.VBox;
import edu.iastate.cs309.util.Util;

/**
 * This Window displays information about UeberTorrnent
 * 
 * Allows users to see version information and about the authors.
 * 
 * @author Justin
 * 
 */
@SuppressWarnings("serial")
public class AboutUberTorrentFrame extends JDialog
{
	private Box mainPane;

	private Image uberImage;
	private JLabel uberLabel;

	private JLabel versionTitle, versionInfo;

	private JButton credits;

	/**
	 * Creates and displays a new AboutUberTorrentFrame.
	 * 
	 * @param owner
	 *            the owner of this JDialog
	 */
	public AboutUberTorrentFrame(JFrame owner)
	{
		super(owner, Util.getUber() + " - About");

		createComponents();

		layoutComponents();

		showFrame();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void showFrame()
	{
		GUIUtil.setUberImageIcon(this);
		pack();
		setResizable(false);
		setVisible(true);
	}

	private void layoutComponents()
	{
		mainPane.addComp(uberLabel).addComp(new HBox(Alignment.BASELINE).addComp(versionTitle).addComp(versionInfo)).addComp(credits);
		add(mainPane);
	}

	private void createComponents()
	{
		mainPane = new VBox(Alignment.CENTER, true);
		try
		{
			uberImage = ImageIO.read(new File("images" + File.separator + "Umlaut.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		uberLabel = new JLabel(new ImageIcon(uberImage));
		versionTitle = new JLabel("Version: ");
		versionInfo = new JLabel(Util.getVersionInfo() + "");
		credits = new JButton("Credits");
		credits.addActionListener(creditListener);
		credits.addKeyListener(enterPressed);
	}

	private KeyListener enterPressed = new KeyListener()
	{

		@Override
		public void keyPressed(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
				credits.doClick();

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

	private ActionListener creditListener = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			new CreditFrame(AboutUberTorrentFrame.this);
		}
	};
}

/**
 * This frame specificaly has information about the four authors of UeberTorrent
 * 
 * @author Justin
 * 
 */
@SuppressWarnings("serial")
class CreditFrame extends JDialog
{
	private static final String DAVID_INFO = "David Samuelson: I'm a boy!";
	private static final String JUSTIN_INFO = "Justin Nelson: " + Util.getUber() + " is only for use in situations where its legality cannot be called into question.";
	private static final String KYLE_INFO = "Kyle Byerly: Hi, I'm at an interview!!";
	private static final String MIKE_INFO = "Mike Seibert: Hi, I'm Mike!";

	private JPanel mainPane;
	private JLabel authorTitle;
	private JTextArea authorInfo;

	/**
	 * Creates a new frame specifically for displaying info about the authors
	 * 
	 * @param owner
	 *            the owner of the JDialog
	 */
	public CreditFrame(JDialog owner)
	{
		super(owner, Util.getUber() + " - credits");

		authorTitle = new JLabel("About the Authors:");

		authorInfo = new JTextArea(DAVID_INFO + '\n' + JUSTIN_INFO + '\n' + KYLE_INFO + '\n' + MIKE_INFO + '\n');
		authorInfo.setEditable(false);
		authorInfo.setLineWrap(true);
		authorInfo.setWrapStyleWord(true);

		JScrollPane scroll = new JScrollPane(authorInfo);

		mainPane = new VBox(Alignment.CENTER).addComp(authorTitle).addComp(scroll);

		add(mainPane);

		pack();
		setPreferredSize(new Dimension(200, 100));
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}
