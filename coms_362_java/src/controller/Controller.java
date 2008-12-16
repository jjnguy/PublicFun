package controller;

import infoExpert.SongData;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import actual.DatabaseSearch;
import actual.DownloadSong;
import actual.RemoveSong;
import actual.UploadSong;
import actual.Login;
import databaseAccess.Database;


public class Controller implements DatabaseSearch {
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

	public String uploadSong(InputStream fileStream, String owner) {
		return (new UploadSong()).uploadSong(fileStream, owner, db);

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
	public List<SongData> search(String broadTerm, String title, String artist,
			String album, int sortBy, String usename) {
		List<SongData> ret = db.search(broadTerm, title, artist, album, sortBy,
				usename);
		db.closeDatabase();
		return ret;
	}

	public FileInputStream downloadSong(String fileName) {
		return (new DownloadSong()).downloadSong(fileName, MP3_PATH);
	}

	public String removeSong(String fileName, String owner) {
		return (new RemoveSong()).removeSong(fileName, db, owner);
	}
	
	public String createUser(String username, String password1, String password2)
	{
		if ((new Login()).createUser(username, password1, password2, db))
			return "User was successfully created!";
		
		else
			return "User was successfully created!";
	}

	public String deleteUser(String userToDelete, String currentUser) {
		if (userToDelete.equals("admin")) {
			return "Username admin cannot be deleted.";
		}
		if (userToDelete.equals(currentUser)) {
			return "You cannot delete yourself.";
		}
		if (!db.deleteUser(userToDelete, currentUser)) {
			return "User could not be deleted or insufficient rights.";
		}
		return "User was successfully deleted!";
	}


	public boolean login(String username, String password)
	{
		return (new Login()).login(username, password, db);
	}

	@Override
	public List<String> getAllUsers() {
		return db.getAllUsers();
	}
}
