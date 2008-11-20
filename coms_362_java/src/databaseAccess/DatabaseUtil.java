package databaseAccess;

import infoExpert.SongData;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Main database interface for all database tasks including connecting to the database,
 * inserting song data, and retrieving songs based on search strings
 * 
 * @author Justin Nelson, Shaun
 * 
 */
public class DatabaseUtil {
	private static final String TABLE_NAME = "songdata";
	private Connection conn = null;
	
	/*Create a new database connection and return true if successful, false otherwise.*/
	public boolean startDatabase(String url, String user, String pass) throws SQLException{
		try {
			Class.forName("com.mysql.jdbc.Driver");
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
		try {
			insert.close();
		} catch (SQLException e) {
			handleSQLException(e);
		}
		return true;
	}
	
	/*Take in a search string and query all database fields for the string. Used also to simply return everything
	 * as in "View Music Collection" link for instance.*/
	public List<SongData> simpleSearch(String searchString){
		Statement q = null;
		ResultSet rs = null;
		List<SongData> songList = new ArrayList<SongData>();
		try {
			q = conn.createStatement();
		} catch (SQLException e) {
			handleSQLException(e);
			return null;
		}
		
		/*Form Query*/
		String query = "SELECT * FROM " + TABLE_NAME + "s ";
		query += "WHERE s.title LIKE %" + searchString + "% OR "; 
		query += "s.album LIKE %" + searchString + "% OR ";
		query += "s.year LIKE %" + searchString + "% OR ";
		query += "s.composer LIKE %" + searchString + "% OR ";
		query += "s.artist LIKE %" + searchString + "%;";
		
		/*Execute Query*/
		try {
			rs = q.executeQuery(query);
		} catch (SQLException e) {
			handleSQLException(e);
			return null;
		}
		
		/*Process Results and return List<SongData>*/
		try {
			while(rs.next()){
				SongData s = new SongData();
				s.setTitle(rs.getString("title"));
				s.setAlbum(rs.getString("album"));
				s.setPerformer(0, rs.getString("performers0"));
				s.setPerformer(1, rs.getString("performers1"));
				s.setPerformer(2, rs.getString("performers2"));
				s.setComment(0, rs.getString("comments0"));
				s.setComment(1, rs.getString("comments1"));
				s.setComment(2, rs.getString("comments2"));
				s.setTrackNum(rs.getString("trackNum"));
				s.setYear(rs.getString("year"));
				s.setEncodedBy(rs.getString("encodedBy"));
				s.setComposer(rs.getString("composer"));
				s.setFileName(rs.getString("fileName"));
				s.setPictureName(rs.getString("pictureName"));
				songList.add(s);
			}
		} catch (SQLException e) {
			handleSQLException(e);
			return null;
		}
		
		return songList;	
	}
	
	/*advancedSearch takes in a search string like simple search but then only searches specific fields, which are
	 * passed in as true if the user wants to check them.  Boolean and specifies if the searches are combined using
	 * AND or OR*/
	public List<SongData> advancedSearch(String searchString, boolean artist, 
			boolean title,boolean album, boolean composer, boolean year, boolean AND){
		Statement q = null;
		ResultSet rs = null;
		List<SongData> songList = new ArrayList<SongData>();
		String andOr;
		try {
			q = conn.createStatement();
		} catch (SQLException e) {
			handleSQLException(e);
			return null;
		}
		
		/*Set and/or boolean value*/
		if(AND) andOr = "AND";
		else andOr = "OR";
		
		/*simple search*/
		if(!artist && !title && !album && !composer && !year){
			return simpleSearch(searchString);
		}
		
		/*Form Query*/
		String query = "SELECT * FROM " + TABLE_NAME + "s WHERE ";
		if(artist){
			query += "s.artist LIKE %" + searchString + "% " + andOr + " ";
		}
		return null;
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
