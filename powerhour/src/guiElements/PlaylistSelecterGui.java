package guiElements;

import genericDataStructures.Dictionary;
import iTunesDataStructures.ITunesLibFile;
import iTunesDataStructures.PlayList;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import util.Util;

@SuppressWarnings("serial")
public class PlaylistSelecterGui extends JDialog {

	private boolean okPressed;

	private JComboBox playlistsCombo;
	private JButton ok;
	private ITunesLibFile lib;

	public PlaylistSelecterGui(File musicLocation, JFrame par) {
		super(par);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		lib = new ITunesLibFile(musicLocation);
		List<PlayList> playlists = lib.getPlaylists();
		playlistsCombo = new JComboBox(playlists.toArray());
		ok = new JButton("Ok");
		ok.addActionListener(okAction);

		okPressed = false;

		JPanel mainPane = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		mainPane.add(playlistsCombo, gc);
		mainPane.add(ok, gc);
		add(mainPane);
		setModal(true);

		Util.moveToMiddle(this);

		pack();
	}

	/**
	 * 
	 * @return true if the user pressed ok, false if the user exited some other
	 *         way
	 */
	public boolean showPlistDialog() {
		setVisible(true);
		return okPressed;
	}

	public List<File> getListOfFiles() {
		List<Long> songs = ((PlayList) playlistsCombo.getSelectedItem())
				.getSongs();

		Dictionary allSongs = lib.getSongs();
		List<File> ret = new ArrayList<File>();
		for (Long song : songs) {
			Dictionary songD = (Dictionary) allSongs.get(song.toString());
			String iTunesFormatLoc = (String) songD.get("Location");
			ret.add(new File(Util.parseITunesLocationFormat(iTunesFormatLoc)));
		}
		return ret;
	}

	private ActionListener okAction = new ActionListener() {
		// @Override
		public void actionPerformed(ActionEvent e) {
			okPressed = true;
			setVisible(false);
		}
	};
}
