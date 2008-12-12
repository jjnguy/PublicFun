package controller;

//import id3TagStuff.ID3v2_XTag;
import infoExpert.SongData;
import java.io.File;
import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import actual.DatabaseSearch;
import actual.DownloadSong;
import actual.RemoveSong;
import actual.UploadSong;
//import util.SongDataFactory;
import databaseAccess.Database;

//import file.SaveSong;

public class Controller implements DatabaseSearch
{
	public static final String MP3_PATH = "C:/Program Files/apache-tomcat-5.5.17/webapps/sharedmp3s/";
	public static final String PIC_PATH = "C:/uploads/pic/";
	public static final String USERNAME_COOKIENAME = "username";
	public static final String DB_URL = "jdbc:mysql://65.110.247.189";
	public static final String DB_USR = "root";
	public static final String DB_PW = "hotdog";
	public static final String MP3_URL = "http://65.110.247.189/sharedmp3s/";
	public static final int SORT_BY_TITLE = 0;
	public static final int SORT_BY_ARTIST = 1;
	public static final int SORT_BY_ALBUM = 2;
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

	
	public String uploadSong(InputStream fileStream, String owner) 
	{
		return (new UploadSong()).uploadSong(fileStream, owner, db);
		
	}



	@Override
	public List<SongData> simpleSearch(String term, int sortType, String owner) {
		List<SongData> ret = db.simpleSearch(term, sortType, owner);
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
	public List<SongData> advancedSearch(String artist, String title, String album, boolean AND, int sortType, String owner) {
		List<SongData> ret = db.advancedSearch(artist, title, album, AND, sortType, owner);
		db.closeDatabase();
		return ret;
	}

	
	public FileInputStream downloadSong(String fileName) 
	{
		
		return (new DownloadSong()).downloadSong(fileName, MP3_PATH);
	}

	
	public String removeSong(String fileName) 
	{
		return (new RemoveSong()).removeSong(fileName, db);
	}
	
	public String createUser(String user, byte[] pass){
		if(!db.addUser(user, pass)){
			return "User could not be created.";
		}
		
		return "User was successfully created!";
	}
	
	public byte[] getHashedPassword(String user){
		return db.getHashedPassword(user);
	}
}
