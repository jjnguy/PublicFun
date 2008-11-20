package controller;

import infoExpert.SongData;

import java.io.InputStream;
import java.util.List;


public class Controller implements UploadSong, DatabaseSearch {

	@Override
	public String uploadSong(InputStream fileStream) {
		// TODO Needs to call the appropriate classes to store
		// the song into a DB and save the file in the correct location
		return "OOps, this isn't implemented yet";
	}

	@Override
	public List<SongData> simpleSearch(String term) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static Controller getController(){
		return new Controller();
	}
	private Controller(){}

	@Override
	public List<SongData> advancedSearch(String artist, String title, String album,
			String composer, String year, boolean AND) {
		// TODO Auto-generated method stub
		return null;
	}
}
