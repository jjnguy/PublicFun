package controller;

import java.util.List;

import infoExpert.SongData;

public interface DatabaseSearch {
	public List<SongData> simpleSearch(String term);
	public List<SongData> advancedSearch(String artist, String title, String album, boolean AND);
}
