package iTunesDataStructures;

import genericDataStructures.Array;
import genericDataStructures.Dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ITunesLibFile {
	
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
			Dictionary pl = (Dictionary)pListArray.get(i);
			// skip smart playlists
			if (pl.containsKey("Playlist Items")){
				playlists.add(new PlayList(pl));
			}
		}
	}
	
	public Dictionary getSongs(){
		return songsDict;
	}
	public List<PlayList> getPlaylists() {
		return playlists;
	}
}
