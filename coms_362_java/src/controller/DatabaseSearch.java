package controller;

import java.util.List;

import infoExpert.SongData;

public interface DatabaseSearch {
	public List<SongData> simpleSearch(String term);
	// TODO public List<SongData> advancedSearch(Some Parameter List);
}
