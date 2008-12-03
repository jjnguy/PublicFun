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
public class Database {
	private static final String TABLE_NAME = "mp3table";
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
		
		Statement use = conn.createStatement();
		use.execute("USE mp3db;");
		return true;
	}
	
	public void closeDatabase(){
		try {
			conn.close();
		} catch (SQLException e) {
			handleSQLException(e);
		}
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
		
		query += " (title, album, performers0, performers1, performers2, comments0, comments1, comments2,";
		query += " trackNum, pubYear, encodedBy, composer, fileName, pictureName)";
		query += " VALUES ("+ "\'"+song.getTitle()+"\'" +", "+"\'"+song.getAlbum()+"\'" +", "+
		"\'"+song.getPerformer(0)+"\'"+", "+"\'"+song.getPerformer(1)+"\'";
		query += ", "+"\'"+song.getPerformer(2)+"\'" +", "+"\'"+song.getComment(0)+"\'"+", "+
		"\'"+song.getComment(1)+"\'"+", "+"\'"+song.getComment(2)+"\'";
		query += ", "+"\'"+song.getTrackNum()+"\'"+", "+"\'"+song.getYear()+"\'"+", "+"\'"+
		song.getEncodedBy()+"\'"+", "+"\'"+song.getComposer()+"\'";
		query += ", "+"\'"+song.getFileName()+"\'"+", "+"\'"+song.getPictureName()+"\'"+");";
		
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
			return false;
		}
		return true;
	}
	
	/*Take in a search string and query all database fields for the string. Used also to simply return everything
	 * as in "View Music Collection" link for instance*/
	public List<SongData> simpleSearch(String searchString){
		Statement q = null;
		ResultSet rs = null;
		
		try {
			q = conn.createStatement();
		} catch (SQLException e) {
			handleSQLException(e);
			return null;
		}
		
		/*Form Query*/
		String query = "SELECT * FROM " + TABLE_NAME + " s ";
		query += "WHERE s.title LIKE \'%" + searchString + "%\' OR "; 
		query += "s.album LIKE \'%" + searchString + "%\' OR ";
		query += "s.performers0 LIKE \'%" + searchString + "%\' OR ";
		query += "s.performers1 LIKE \'%" + searchString + "%\' OR ";
		query += "s.performers2 LIKE \'%" + searchString + "%\';";
		
		/*Execute Query*/
		try {
			rs = q.executeQuery(query);
		} catch (SQLException e) {
			handleSQLException(e);
			return null;
		}
		
		/*Process Results and return List<SongData>*/
		return processResults(rs);	
	}
	
	/*advancedSearch takes in a search string like simple search but then only searches specific fields, which are
	 * passed in as true if the user wants to check them.  Boolean and specifies if the searches are combined using
	 * AND or OR*/
	public List<SongData> advancedSearch(String artist, String title,
			String album, boolean AND){
		Statement q = null;
		ResultSet rs = null;
		String andOr;	//default to AND search
		boolean useAndOr = false;
		try {
			q = conn.createStatement();
		} catch (SQLException e) {
			handleSQLException(e);
			return null;
		}
		
		/*Set and/or boolean value*/
		if(AND) andOr = "AND";
		else andOr = "OR";
		
		/*user entered no data, return null*/
		if(artist == null && title == null && album == null){
			return null;
		}
		
		/*Form Query*/
		String query = "SELECT * FROM " + TABLE_NAME + " s WHERE";
		if(artist != null){
			query += " s.performers0 LIKE \'%" + artist + "%\' " + "OR" + " ";
			query += "s.performers1 LIKE \'%" + artist + "%\' " + "OR" + " ";
			query += "s.performers2 LIKE \'%" + artist + "%\'";
			useAndOr = true;
		}
		if(title != null){
			if(useAndOr) query += " " + andOr;
			query += " s.title LIKE \'%" + title + "%\'";
			useAndOr = true;
		}
		if(album != null){
			if(useAndOr) query += " " + andOr;
			query += " s.album LIKE \'%" + album + "%\'";
		}
		query += ";";
		
		/*Execute Query*/
		try {
			rs = q.executeQuery(query);
		} catch (SQLException e) {
			handleSQLException(e);
			return null;
		}
		
		/*Process Results and return List<SongData>*/
		return processResults(rs);	
		
	}
	
	/*processResults takes in a ResultSet from a query and extracts each field into a new
	 * SongData object which are added to a List and then returned.*/
	private List<SongData> processResults(ResultSet rs){
		List<SongData> songList = new ArrayList<SongData>();
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
				s.setYear(rs.getString("pubYear"));
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
	
	
	/**
	 * Simple method to handle {@link SQLException}s. Loops through generated SQLExceptions and
	 * prints them the the specified {@link PrintStream}.
	 * 
	 * @param sqle
	 *            The exception to handle.
	 * @param out
	 *            The PrintStream to print the error messages to.
	 */
	private static void handleSQLException(SQLException sqle, PrintStream out) {
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
