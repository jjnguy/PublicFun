package controller;
import infoExpert.SongData;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import databaseAccess.Database;


public class Controller implements UploadSong, DatabaseSearch {
	public static final String MP3_PATH = "C:/uploads/mp3/";
	public static final String PIC_PATH = "C:/uploads/pic/";
	public static final String DB_URL = "jdbc:mysql://localhost";
	public static final String DB_USR = "root";
	public static final String DB_PW = "root";
	private Database db;

	@Override
	public String uploadSong(InputStream fileStream) {
		// TODO Needs to call the appropriate classes to store
		// the song into a DB and save the file in the correct location
		return "OOps, this isn't implemented yet";
	}
	
	public boolean insertSongIntoDatabase(SongData song){
		return insertSongIntoDatabase(song);
	}

	@Override
	public List<SongData> simpleSearch(String term) {
		return db.simpleSearch(term);
	}
	
	public static Controller getController(){
		return new Controller();
	}
	private Controller(){
		db = new Database();
		try {
			db.startDatabase(DB_URL, DB_USR, DB_PW);
		} catch (SQLException e) {
			Database.handleSQLException(e);
		}
	}

	@Override
	public List<SongData> advancedSearch(String artist, String title, String album,
			String composer, String year, boolean AND) {
		return db.advancedSearch(artist, title, album, composer, year, AND);
	}
}
