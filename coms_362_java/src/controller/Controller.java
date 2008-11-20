package controller;
import infoExpert.SongData;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import databaseAccess.Database;


public class Controller implements UploadSong, DatabaseSearch {
	public static final String mp3path = "C:/uploads/mp3/";
	public static final String picpath = "C:/uploads/pic/";
	public static final String dburl = "jdbc:mysql://localhost";
	public static final String dbusr = "root";
	public static final String dbpw = "root";
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
			db.startDatabase(dburl, dbusr, dbpw);
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
