package databaseAccess;

import infoExpert.SongData;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Main database interface for all database tasks including connecting to the database,
 * inserting song data, and retrieving songs based on search strings
 * 
 * @author Justin Nelson
 * 
 */
public class DatabaseUtil {
	private static final String TABLE_NAME = "songdata";
	private Connection conn = null;
	
	/*Create a new database connection and return true if successful, false otherwise.*/
	public boolean startDatabase(String url, String user, String pass) throws SQLException{
		try {
			Class driver = Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
		conn = DriverManager.getConnection(url, user, pass);
		return true;
	}
	
	/*Attempts to insert a new song into the database and returns true if successful, false otherwise.*/
	public boolean insertSongIntoDatabase(SongData song){
		Statement insert;
		try {
			insert = conn.createStatement();
		} catch (SQLException e) {
			handleSQLException(e);
			return false;
		}
		String query = "INSERT INTO " + TABLE_NAME;
		
		query += " (title, album, performers0, performers1, performers2, comments0, comments1, comments2";
		query += " trackNum, year, encodedBy, composer, fileName, pictureName)";
		query += "values ("+song.getTitle()+", "+song.getAlbum()+", "+song.getPerformer(0)+", "+song.getPerformer(1);
		query += ", "+song.getPerformer(2)+", "+song.getComment(0)+", "+song.getComment(1)+", "+song.getComment(2);
		query += ", "+song.getTrackNum()+", "+song.getYear()+", "+song.getEncodedBy()+", "+song.getComposer();
		query += ", "+song.getFileName()+", "+song.getPictureName()+");";
		
		try {
			insert.executeUpdate(query);
		} catch (SQLException e) {
			handleSQLException(e);
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * Simple method to handle {@link SQLException}s. Loops through generated SQLExceptions and
	 * prints them the the specified {@link PrintStream}.
	 * 
	 * @param sqle
	 *            The exception to handle.
	 * @param out
	 *            The PrintStream to print the error messages to.
	 */
	public static void handleSQLException(SQLException sqle, PrintStream out) {
		sqle.printStackTrace(out);
		while (sqle != null) {
			String logMessage = "\n SQL Error: " + sqle.getMessage() + "\n\t\t"
					+ "Error code: " + sqle.getErrorCode() + "\n\t\t" + "SQLState: "
					+ sqle.getSQLState() + "\n";
			out.println(logMessage);
			sqle = sqle.getNextException();
		}
	}

	/**
	 * Convenience method for DatabaseUtil.handleSQLException(sqle, System.err);
	 * 
	 * @param sqle
	 *            The exception to handle.
	 */
	public static void handleSQLException(SQLException sqle) {
		handleSQLException(sqle, System.err);
	}
}
