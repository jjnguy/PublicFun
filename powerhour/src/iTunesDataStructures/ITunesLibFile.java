package iTunesDataStructures;

import genericDataStructures.Array;
import genericDataStructures.Dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import util.Util;

public class ITunesLibFile {
	public static final String ITUNES_LIB_DEFAULT_LOC = System.getProperty("user.home")
			+ "/My Documents/My Music/iTunes/iTunes Music Library.xml";

	private Dictionary fullDict;
	private Dictionary songsDict;
	private List<PlayList> playlists;

	public ITunesLibFile(File f) {
		Scanner s = null;
		try {
			s = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// throw away the doc headders in the file
		s.nextLine();
		s.nextLine();
		s.nextLine();
		s.nextLine();
		// make the full dictionary from the file
		fullDict = new Dictionary(s);
		songsDict = (Dictionary) fullDict.get("Tracks");
		playlists = new ArrayList<PlayList>();
		Array pListArray = (Array) fullDict.get("Playlists");
		for (int i = 0; i < pListArray.length(); i++) {
			Dictionary pl = (Dictionary) pListArray.get(i);
			// skip smart playlists
			if (pl.containsKey("Playlist Items")) {
				playlists.add(new PlayList(pl));
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public List<File> getListOfFiles(String playlistName) {
		List<Long> songs = null; // Somehow pick a certain playlist
		for (PlayList plist : playlists) {
			if (plist.getName().equals(playlistName)) {
				songs = plist.getSongs();
				break;
			}
		}
		if (songs == null)
			return null;
		List<File> ret = new ArrayList<File>();
		for (Long song : songs) {
			Dictionary songD = (Dictionary) songsDict.get(song.toString());
			String iTunesFormatLoc = (String) songD.get("Location");
			ret.add(new File(Util.parseITunesLocationFormat(iTunesFormatLoc)));
		}
		return ret;
	}

	public Dictionary getSongs() {
		return songsDict;
	}

	public List<PlayList> getPlaylists() {
		return playlists;
	}
}
