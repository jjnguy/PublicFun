package controller;

import id3TagStuff.ID3v2_XTag;
import infoExpert.SongData;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import controller.DatabaseSearch;
import controller.SaveSong;
import controller.UploadSong;
import controller.SongDataFactory;
import databaseAccess.Database;

public class Controller implements UploadSong, DatabaseSearch {
	public static final String MP3_PATH = "C:/Program Files/apache-tomcat-5.5.17/webapps/sharedmp3s/";	//hard coded...we can change later
	public static final String PIC_PATH = "C:/uploads/pic/";
	public static final String DB_URL = "jdbc:mysql://65.110.247.189";
	public static final String DB_USR = "root";
	public static final String DB_PW = "hotdog";
	private Database db;

	static {
		File existTest = new File(MP3_PATH);
		if (!existTest.exists()) {
			boolean created = existTest.mkdirs();
		}
		existTest = new File(PIC_PATH);
		if (!existTest.exists()) {
			boolean created = existTest.mkdirs();
		}
	}

	@Override
	public String uploadSong(InputStream fileStream) {
		String fileLocation;
		
		try {
			SaveSong SS = new SaveSong(fileStream);
			fileLocation = SS.getPathToMP3();
			
		
			/* Create SongData class from the ID3 tag and insert it into the DB */
			ID3v2_XTag newTag = new ID3v2_XTag(new File(fileLocation));
			SongData sd = SongDataFactory.tagToSongData(newTag);
			sd.setFileName(fileLocation);
			if (!insertSongIntoDatabase(sd)) {
				File f = new File(fileLocation);
				f.delete();
				return "Database access failed";
			}

			return "Saved to: " + fileLocation;
		} catch (IOException e) {
			e.printStackTrace();
			return "Error: Song not saved";
		}
	}

	public boolean insertSongIntoDatabase(SongData song) {
		boolean ret = db.insertSongIntoDatabase(song);
		db.closeDatabase();
		return ret;
	}

	@Override
	public List<SongData> simpleSearch(String term) {
		List<SongData> ret = db.simpleSearch(term);
		db.closeDatabase();
		return ret;
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
	public List<SongData> advancedSearch(String artist, String title, String album, boolean AND) {
		List<SongData> ret = db.advancedSearch(artist, title, album, AND);
		db.closeDatabase();
		return ret;
	}

}
