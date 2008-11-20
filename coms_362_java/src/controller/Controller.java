package controller;

import infoExpert.SongData;

import java.io.File;
import java.io.IOException;
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

	static {
		File existTest = new File(MP3_PATH);
		if (!existTest.exists()) {
			boolean created = existTest.mkdirs();
			if (!created) {
				// TODO throw new IOException("Could not access the uploads directory!");
			}
		}
		existTest = new File(PIC_PATH);
		if (!existTest.exists()) {
			boolean created = existTest.mkdirs();
			if (!created) {
				// TODO throw new IOException("Could not access the uploads directory!");
			}
		}
	}

	@Override
	public String uploadSong(InputStream fileStream) 
	{
		String fileLocation;
		try {
			
			// TODO Needs to call the appropriate classes to store
			// the song into a DB
			fileLocation = SaveSong.SaveASong(fileStream);
			return "Saved to: " + fileLocation) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error: Song not saved";
		}
	}

	public boolean insertSongIntoDatabase(SongData song) {
		return db.insertSongIntoDatabase(song);
	}

	@Override
	public List<SongData> simpleSearch(String term) {
		return db.simpleSearch(term);
	}

	public static Controller getController() {
		return new Controller();
	}

	private Controller() {
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
