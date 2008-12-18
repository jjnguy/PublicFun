package controller;

import infoExpert.SongData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import actual.DatabaseSearch;
import actual.RemoveSong;
import actual.UploadSong;
import actual.Login;
import databaseAccess.QueryDB;

/**
 * Main interface for the jTunes System.  All methods called from the JSP's reside here
 * 
 * @author Benjamin Petersen, Shaun Brockhoff, Justin Nelson
 *
 */
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
	private QueryDB db;

	
	  
	 //Ensures that the needed directories are on the system
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
	/**
	 * Utilizes the actual.UploadSong class to upload a song to the
	 * server and add the song's information to the database
	 * 
	 * @param fileStream
	 * 				InputStream of the mp3 to upload
	 * @param owner
	 * 				The username of the owner of the song
	 * @return
	 */
	public String uploadSong(InputStream fileStream, String owner) {
		return (new UploadSong()).uploadSong(fileStream, owner, db);

	}

	public static Controller getController() {
		return new Controller();
	}

	private Controller() {
		db = new QueryDB();
		try {
			db.startDatabase(DB_URL, DB_USR, DB_PW);
		} catch (SQLException e) {
			QueryDB.handleSQLException(e);
		}
	}


	@Override
	public List<SongData> search(String broadTerm, String title, String artist, String album,
			int sortBy, String usename) {
		List<SongData> ret = db.search(broadTerm, title, artist, album, sortBy, usename);
		db.closeDatabase();
		return ret;
	}

	/**
	 * Returns the FileInputStream of a mp3 file a user wishes to download
	 * 
	 * @param fileName
	 * 				The filename of the mp3 file
	 * @return
	 * 			a FileInputStream of the selected mp3 file
	 * @throws FileNotFoundException
	 */
	public FileInputStream downloadSong(String fileName) throws FileNotFoundException {
		return new FileInputStream(MP3_PATH + fileName);
	}

	/**
	 * Utilizes the actual.RemoveSong class to remove a mp3 file from
	 * the system including the database
	 * 
	 * @param fileName
	 * 			The name of the mp3 file to remove
	 * @param owner
	 * 			The username of the owner of the mp3
	 * @return
	 * 		A String indicating the success or failure of the removal
	 */
	public String removeSong(String fileName, String owner) {
		return (new RemoveSong()).removeSong(fileName, db, owner);
	}

	/**
	 * Utilizes the actual.Login class to create a new user
	 * 
	 * @param username
	 * 			The desired username
	 * @param password1
	 * 			The desired password
	 * @param password2
	 * 			The desired password again for verification
	 * @return
	 * 			String indicating the success or failure in creating the new user
	 */
	public String createUser(String username, String password1, String password2) {
		if ((new Login()).createUser(username, password1, password2, db))
			return "User was successfully created!";

		else
			return "Error: User was not created!";
	}

	/**
	 * Utilizes the databaseAccess.QuerySB class to delete a user from the system.
	 * 
	 * Only an admin can delete a user and a user can not delete his/her own account
	 * 
	 * @param userToDelete
	 * 			The user to delete
	 * @param currentUser
	 * 			The current user of the system
	 * @return
	 * 			A String indicating the success or failure of the deletion
	 */
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

	/**
	 * Utilizes the actual.Login class to log a user into the system
	 * 
	 * @param username
	 * 			The username of the user
	 * @param password
	 * 			The password of the user
	 * @return
	 * 			A String indicating the success or failure of the login
	 */
	public boolean login(String username, String password) {
		return (new Login()).login(username, password, db);
	}

	@Override
	public List<String> getAllUsers() {
		return db.getAllUsers();
	}
}
