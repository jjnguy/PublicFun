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
	
	/*Create a new database connection and return true if successful*/
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
	
	public boolean insertSongIntoDatabase(SongData song) throws SQLException{
		Statement insert = conn.createStatement();
		String query = "INSERT INTO " + TABLE_NAME;
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
