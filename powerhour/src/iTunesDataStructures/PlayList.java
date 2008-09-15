package iTunesDataStructures;

import java.util.ArrayList;
import java.util.List;

import genericDataStructures.Array;
import genericDataStructures.Dictionary;

public class PlayList {
	
	private Dictionary playlist;
	private List<Long> songs;
	private String name;
	
	public PlayList(Dictionary pList) {
		playlist = pList;
		name = (String) playlist.get("Name");
		songs = new ArrayList<Long>();
		Array tracksA = (Array) playlist.get("Playlist Items");
		for (int i = 0; i < tracksA.length(); i++) {
			Long id = (Long) ((Dictionary) tracksA.get(i)).get("Track ID");
			songs.add(id);
		}
	}
	
	public List<Long> getSongs() {
		return songs;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
}
