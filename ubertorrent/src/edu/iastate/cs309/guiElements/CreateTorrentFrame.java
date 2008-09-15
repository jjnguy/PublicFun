package edu.iastate.cs309.guiElements;

import java.awt.AWTKeyStroke;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.GroupLayout.Alignment;

import edu.iastate.cs309.client.ClientLog;
import edu.iastate.cs309.guiElements.guiUTil.GUIUtil;
import edu.iastate.cs309.guiElements.guiUTil.PieceSize;
import edu.iastate.cs309.guiElements.lowerlevel.Box;
import edu.iastate.cs309.guiElements.lowerlevel.HBox;
import edu.iastate.cs309.guiElements.lowerlevel.HStretcher;
import edu.iastate.cs309.guiElements.lowerlevel.VBox;
import edu.iastate.cs309.util.TorrentFileCreator;
import edu.iastate.cs309.util.Util;

/**
 * A CreateTorrentFrame for creating new torrent files.
 * 
 * Provides fields for inputing all of the necessary information for creating a
 * torrent file
 * 
 * @author Michael Seibert, Justin Nelson
 */
@SuppressWarnings("serial")
public class CreateTorrentFrame extends JDialog
{

	private static final String DEFAULT_TRACKER_URL = "http://open.tracker.thepiratebay.org:80/announce";
	private JTextField fileLocationField;
	private JButton fileLocationBrowseButton;
	private JTextArea trackerList;
	private JScrollPane trackerScroller;
	private JButton restoreDefaultsButton;
	private JLabel commentLabel;
	private JTextField commentField;
	private JLabel pieceSizeLabel;
	private JComboBox pieceSizeChooser;
	private JButton createButton;
	private JButton cancelButton;
	private JPanel bigPanel;

	private int seed;

	private File selectedFile;

	private Logger log = ClientLog.log;

	/**
	 * Specifies that the torrent should be immediately be seeded after creation
	 */
	public static final int SEED_OPTION = 1;
	/**
	 * Specifies that the torrent should be not immediately be seeded after
	 * creation
	 */
	public static final int NO_SEED_OPTION = 0;
	/**
	 * Specifies nothing
	 */
	public static final int SEED_OPTION_NOT_SET = -1;

	/**
	 * Create and display a dialog used to create a torrent.
	 * 
	 * @param owner
	 *            THe owner of this JDialog
	 */
	public CreateTorrentFrame(JFrame owner)
	{
		super(owner, "Create Torrent - " + Util.getUber());

		createComponents();

		addEventHandlers();

		bigPanel = layoutUI();

		tidyUpComponents();

		showWindow(bigPanel);
	}

	/**
	 * Initialize all components and add some default values.
	 */
	private void createComponents()
	{
		fileLocationField = new JTextField(30);
		fileLocationBrowseButton = new JButton("Browse...");

		trackerList = new JTextArea();

		trackerList.setFont(fileLocationField.getFont());
		trackerList.setText(DEFAULT_TRACKER_URL + System.getProperty("line.separator"));
		trackerScroller = new JScrollPane(trackerList);

		restoreDefaultsButton = new JButton("Restore Defaults");
		commentLabel = new JLabel("Comment:");
		commentField = new JTextField(30);
		pieceSizeLabel = new JLabel("Piece Size:");

		pieceSizeChooser = new JComboBox(new Object[] { PieceSize.AUTO, PieceSize.THIRTY2, PieceSize.SIXTY4, PieceSize.ONE28, PieceSize.TWO56, PieceSize.FIVE12, PieceSize.TEN24, PieceSize.TWENTY48, PieceSize.FORTY96 });

		createButton = new JButton("Create and Save");
		cancelButton = new JButton("Cancel");
	}

	/**
	 * Add event handlers for components that need them.
	 */
	private void addEventHandlers()
	{
		addKeyListners();
		fileLocationBrowseButton.addActionListener(fileBrowseClicked);
		restoreDefaultsButton.addActionListener(restoreDefaultsClicked);
		createButton.addActionListener(createButtonClicked);
		cancelButton.addActionListener(cancelButtonClicked);
	}

	private void addKeyListners()
	{
		fileLocationField.addKeyListener(enterPressed);
		fileLocationBrowseButton.addKeyListener(enterPressed);
		trackerList.addKeyListener(enterPressed);
		trackerScroller.addKeyListener(enterPressed);
		restoreDefaultsButton.addKeyListener(enterPressed);
		commentField.addKeyListener(enterPressed);
		pieceSizeChooser.addKeyListener(enterPressed);
		createButton.addKeyListener(enterPressed);
	}

	/**
	 * Lay out all the components.
	 * 
	 * @return A panel containing all components.
	 */
	private JPanel layoutUI()
	{
		Box bigPanel = new VBox(Alignment.LEADING, true).addComp(new HBox("Files", Alignment.TRAILING).addComp(fileLocationField).addComp(fileLocationBrowseButton)).addComp(new VBox("Trackers", Alignment.CENTER).addComp(trackerScroller)).addComp(new VBox("Other", Alignment.TRAILING).addComp(new HBox(Alignment.BASELINE).addComp(commentLabel).addComp(commentField).addComp(new HBox(Alignment.BASELINE).addComp(pieceSizeLabel).addComp(pieceSizeChooser)))).addComp(new HBox(Alignment.BASELINE).addComp(restoreDefaultsButton).addComp(new HStretcher()).addComp(createButton).addComp(cancelButton));

		return bigPanel;
	}

	/**
	 * Fix some little things with various components to make the UI work
	 * better.
	 */
	private void tidyUpComponents()
	{
		GUIUtil.setUberImageIcon(this);

		// Tab on tracker list focuses next control...
		Set<AWTKeyStroke> forwardTraversalKeys = trackerList.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
		LinkedHashSet<AWTKeyStroke> newKeys = new LinkedHashSet<AWTKeyStroke>(forwardTraversalKeys);
		newKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
		trackerList.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newKeys);

		// Shift-tab on tracker list focuses previous control...
		Set<AWTKeyStroke> backwardTraversalKeys = trackerList.getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
		newKeys = new LinkedHashSet<AWTKeyStroke>(backwardTraversalKeys);
		newKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK));
		trackerList.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, newKeys);

		// Make this field not expand vertically
		fileLocationField.setMaximumSize(new Dimension(fileLocationField.getMaximumSize().width, fileLocationField.getPreferredSize().height));

		// Make this combo box not expand horizontally
		pieceSizeChooser.setMaximumSize(pieceSizeChooser.getPreferredSize());
	}

	/**
	 * Fix up the JFrame with size info and show it.
	 * 
	 * @param bigPanel
	 *            The panel to put in the JFrame before displaying.
	 */
	private void showWindow(JPanel bigPanel)
	{
		seed = SEED_OPTION_NOT_SET;
		setModal(true);
		add(bigPanel);
		pack();
		// Add 30 pixels to the height to make the list of trackers longer.
		setSize(getWidth(), getHeight() + 30);
		setMinimumSize(new Dimension(390, 270));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * @return returns an array of all of the trackers to be used
	 */
	public String[] getTrackerUrls()
	{
		return trackerList.getText().trim().split("\n");
	}

	/**
	 * @return the file where the torrent was saved
	 */
	public File getFile()
	{
		return selectedFile;
	}

	/**
	 * @return the seed option of the torrent
	 */
	public boolean seed()
	{
		return seed == SEED_OPTION;
	}

	private ActionListener fileBrowseClicked = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			// Open a file chooser
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int result = chooser.showOpenDialog(CreateTorrentFrame.this);

			if (result != JFileChooser.APPROVE_OPTION)
				return;

			File file = chooser.getSelectedFile();
			String path;
			try
			{
				// Try this first, since it gives us a nice clean path.
				path = file.getCanonicalPath();
			}
			catch (IOException e)
			{
				// Use this if that failed.
				path = file.getAbsolutePath();
			}
			fileLocationField.setText(path);

		}
	};

	private ActionListener restoreDefaultsClicked = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			fileLocationField.setText("");
			trackerList.setText(DEFAULT_TRACKER_URL + System.getProperty("line.separator"));
			commentField.setText("");
			pieceSizeChooser.setSelectedIndex(0);
		}
	};

	private ActionListener createButtonClicked = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			if (formHasErrors())
				return;

			AcceptTorrentFileChooser choose = new AcceptTorrentFileChooser();
			int actionChosen = choose.showSaveDialog(CreateTorrentFrame.this);

			// if the user chooses cancel then we just exit
			if (actionChosen != JFileChooser.APPROVE_OPTION)
				return;

			selectedFile = ensureCorrectExtention(choose.getSelectedFile());
			if (selectedFile.exists())
			{
				int choice = JOptionPane.showConfirmDialog(CreateTorrentFrame.this, "There was already a file that has the same name, would you like to overwrite the file?", "File Overwritten", JOptionPane.WARNING_MESSAGE);
				if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.NO_OPTION)
					return;
			}

			int peiceSize = resolvePeiceSize();
			TorrentFileCreator tfc = new TorrentFileCreator(trackerList.getText().split("\n"), peiceSize, new File(fileLocationField.getText()), commentField.getText().equals("") ? null : commentField.getText());
			try
			{
				tfc.writeFile(selectedFile);
			}
			catch (IOException e)
			{
				log.log(Level.WARNING, "Writing to a .torrent file was unsucessful.");
				JOptionPane.showMessageDialog(CreateTorrentFrame.this, "Writing to the .torrent file was unsucessful.", "Failed Torrent File Write", JOptionPane.ERROR_MESSAGE);
				if (Util.DEBUG)
					e.printStackTrace();
			}

			//int choice = JOptionPane.showConfirmDialog(CreateTorrentFrame.this, "File created successfully in " + selectedFile.getAbsolutePath() + "\n" + "Would you like to immediately begin seeding the file?", "Create Torrent", JOptionPane.YES_NO_OPTION);
			//seed = JOptionPane.OK_OPTION == choice ? SEED_OPTION : NO_SEED_OPTION;

			dispatchEvent(new WindowEvent(CreateTorrentFrame.this, WindowEvent.WINDOW_CLOSING));
		}

		private int resolvePeiceSize()
		{
			Object o = pieceSizeChooser.getSelectedItem();
			PieceSize p = null;
			if (o instanceof PieceSize)
			{
				p = (PieceSize) o;
			}
			else
			{
				log.log(Level.WARNING, "The PieceSize chooser had a non piece size satroed into it.  Used auto.");
				return PieceSize.autoCalcPieceSize(Util.getTotalLength(selectedFile)).getNumBytes();
			}
			if (p.getNumBytes() != 0)
				return p.getNumBytes();

			return PieceSize.autoCalcPieceSize(Util.getTotalLength(selectedFile)).getNumBytes();
		}

		private File ensureCorrectExtention(File in)
		{
			String fName = in.getAbsolutePath();

			if (!fName.endsWith(".torrent"))
			{
				fName += ".torrent";
				return new File(fName);
			}
			return in;
		}

		@SuppressWarnings("deprecation")
		private boolean formHasErrors()
		{
			String errorMesage = "";
			int errorCount = 0;

			// Validate urls
			int numValidUrls = 0;
			String[] trackers = getTrackerUrls();
			for (String string : trackers)
			{
				if (Util.validateUrl(string))
					numValidUrls++;
			}

			if (numValidUrls != trackers.length)
			{
				log.log(Level.FINE, "A url in the create torrent frame was invalid.");
				errorCount++;
				errorMesage += errorCount + ". One or more of your url's is invalid.\n";
			}
			if (fileLocationField.getText().equals(""))
			{
				log.log(Level.FINE, "No file was selected to save to.");
				errorCount++;
				errorMesage += errorCount + ". You must select a file or folder to create a .torrent file of.\n";
			}
			if (trackerList.getText().equals(""))
			{
				log.log(Level.FINE, "No trackers were defined.");
				errorCount++;
				errorMesage += errorCount + ". You must specify atleast one tracker.\n";
			}
			if (errorCount != 0)
			{
				JOptionPane.showMessageDialog(CreateTorrentFrame.this, errorMesage, "Error Creating Torrent", JOptionPane.ERROR_MESSAGE);
				return true;
			}
			return false;
		}
	};
	private KeyListener enterPressed = new KeyAdapter()
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				createButton.doClick();
			}

		}
	};
	private ActionListener cancelButtonClicked = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			dispatchEvent(new WindowEvent(CreateTorrentFrame.this, WindowEvent.WINDOW_CLOSING));
		}
	};
}
