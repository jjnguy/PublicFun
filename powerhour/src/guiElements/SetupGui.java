package guiElements;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import util.Util;

@SuppressWarnings( { "serial" })
public class SetupGui extends JFrame {

	private static final String ITUNES_LIB_DEFAULT_LOC = "C:/Documents and Settings/usrname/My Documents/My Music/iTunes/iTunes Music Library.xml";
	private JLabel iconLable;
	private JLabel vlcLocationLable;
	private JTextField vlcLocationText;
	private JButton browseButton;
	private JLabel usernameLabel;
	private JButton selectSongFolderButton;
	private JButton startButton;
	private JLabel numberOfSongsLable;
	private JComboBox numberOfSongs;
	private File musicLocation;
	private List<File> songs;
	private JTextArea userNameText;

	public SetupGui() {
		super("Power Hour Helper");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);

		Util.setLookAndFeel();

		ImageIcon i = null;
		InputStream imgStream = this.getClass().getClassLoader()
				.getResourceAsStream("powerHour.png");

		try {
			i = new ImageIcon(ImageIO.read(imgStream));
		} catch (IOException e) {
			e.printStackTrace();
		}
		iconLable = new JLabel(i);

		vlcLocationLable = new JLabel("Vlc location");
		vlcLocationLable.setVisible(false);
		vlcLocationText = new JTextField();
		vlcLocationText.setText("C:/Program Files/VideoLAN/VLC/vlc.exe");
		vlcLocationText.setVisible(false);

		browseButton = new JButton("Browse");
		browseButton.setVisible(false);
		browseButton.addActionListener(browseAction);

		numberOfSongsLable = new JLabel("Select Lenth of Play");
		numberOfSongs = new JComboBox(new Object[] { 15, 30, "60 (Power Hour)",
				"100 (Century Club)" });

		usernameLabel = new JLabel("Your Windows Username");
		usernameLabel.setVisible(false);
		userNameText = new JTextArea();
		userNameText.setVisible(false);
		
		selectSongFolderButton = new JButton("Choose iTunes Playlist");
		selectSongFolderButton.addActionListener(selectionSongActionWindows);

		startButton = new JButton("Start Power Hour!");
		startButton.addActionListener(startAction);
		startButton.setEnabled(false);
		startButton.setToolTipText("Please find some songs first");

		JPanel mainPane = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.insets = new Insets(4, 4, 4, 4);
		gc.ipadx = 3;

		gc.gridx = 0;
		mainPane.add(iconLable, gc);
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridy = 1;
		gc.gridx = GridBagConstraints.RELATIVE;
		mainPane.add(vlcLocationLable, gc);
		mainPane.add(vlcLocationText, gc);
		mainPane.add(browseButton, gc);
		gc.gridy = 2;
		mainPane.add(numberOfSongsLable, gc);
		mainPane.add(numberOfSongs, gc);
		gc.gridy = 3;
		gc.fill = GridBagConstraints.HORIZONTAL;
		mainPane.add(usernameLabel, gc);
		mainPane.add(userNameText, gc);
		mainPane.add(selectSongFolderButton, gc);
		gc.gridx = 1;
		mainPane.add(startButton, gc);

		add(mainPane);
		pack();

		Util.moveToMiddle(this);

		setVisible(true);
	}

	private int resolveSongCount() {
		if (numberOfSongs.getSelectedIndex() <= 1)
			return (Integer) numberOfSongs.getSelectedItem();
		return Integer.parseInt(((String) numberOfSongs.getSelectedItem())
				.split(" ")[0]);
	}

	private ActionListener browseAction = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			JFileChooser ch = new JFileChooser();
			ch.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int result = ch.showOpenDialog(SetupGui.this);
			if (result == JFileChooser.CANCEL_OPTION)
				return;
			if (songs != null)
				vlcLocationText.setText(ch.getSelectedFile().getAbsolutePath());
		}
	};

	private ActionListener selectionSongActionWindows = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (songs == null) {
				songs = new ArrayList<File>();
			}
			musicLocation = new File(ITUNES_LIB_DEFAULT_LOC.replace("usrname",
					System.getProperty("user.name")));

			if (!musicLocation.exists()) {
				JOptionPane.showMessageDialog(SetupGui.this,
						"The username doesn't exist on this computer");
				musicLocation = null;
				return;
			}

			songs.addAll(loadPlaylist());

			if (songs != null && songs.size() > resolveSongCount()) {
				startButton.setEnabled(true);
				startButton.setToolTipText("Start Power Hour");
			} else {
				JOptionPane.showMessageDialog(SetupGui.this,
						"You still need to add some songs.");
			}
		}

		private List<File> loadPlaylist() {
			PlaylistSelecterGui pls = new PlaylistSelecterGui(musicLocation,
					SetupGui.this);

			if (pls.showPlistDialog()) {
				List<File> files = pls.getListOfFiles();
				return files;
			} else
				return null;
		}
	};

	private ActionListener startAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!new File(vlcLocationText.getText()).exists()) {
				JOptionPane.showMessageDialog(SetupGui.this,
						"Please find VLC on your computer");
				vlcLocationLable.setVisible(true);
				vlcLocationText.setVisible(true);
				browseButton.setVisible(true);
				pack();
				return;
			}
			if (songs == null) {
				throw new NullPointerException("Need to locate songs first");
			}
			setVisible(false);
			new PlayGui(songs, SetupGui.this.resolveSongCount())
					.setVlcLocation(vlcLocationText.getText());
		}
	};

	public static void main(String[] args) throws IOException {
		new SetupGui();
	}
}
