package controller.interfaces;

import infoExpert.SongData;

import java.util.List;

public interface DatabaseSearch {
	public List<SongData> simpleSearch(String term, int sortBy, String owner);
	public List<SongData> advancedSearch(String artist, String title, String album, boolean AND, int sortBy, String owner);
}
