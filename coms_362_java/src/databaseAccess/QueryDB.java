package databaseAccess;

import infoExpert.SongData;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import controller.Controller;

/**
 * Main database interface for all database tasks.  Tasks include connecting to the database, adding new songs,
 * searching for existing songs, adding new users, and deleting users and songs.
 * 
 * @author Shaun Brockhoff
 * 
 */
public class QueryDB {
	private static final String MP3_TABLE = "mp3table";
	private static final String USER_TABLE = "users";
	private Connection conn = null;


	/**
	 * Creates a new connection to the database and initializes the driver.
	 * @param url - database URL
	 * @param user - database username
	 * @param pass - database password
	 * @return true if the database connected successfully
	 * @throws SQLException if the connection fails. 
	 */
	public boolean startDatabase(String url, String user, String pass)
			throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
			return false;
		}
		conn = DriverManager.getConnection(url, user, pass);

		Statement use = conn.createStatement();
		use.execute("USE mp3db;");
		return true;
	}

	/**
	 * Closes the connection to the database for this instance.
	 */
	public void closeDatabase() {
		try {
			conn.close();
		} catch (SQLException e) {
			handleSQLException(e);
		}
	}

	/**
	 * Attempts to insert a new song into the database.  This method will return false if there
	 * is a SQLException of any kind, including if the user attempts to insert a duplicate song.
	 * @param song - Object that contains all the information pertaining to the song
	 * @param owner - Owner of the song. Used to determine permissions for viewing, deleting, etc.
	 * @return true on successful insertion, false otherwise
	 */
	public boolean insertSongIntoDatabase(SongData song, String owner) {
		/* Prepare statement with wildcards */
		PreparedStatement insert;
		try {
			insert = conn
					.prepareStatement("INSERT INTO "
							+ MP3_TABLE
							+ " (title, album, performers0, performers1, performers2, comments0, comments1, comments2,"
							+ " trackNum, pubYear, encodedBy, composer, fileName, pictureName, owner)"
							+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
		} catch (SQLException e) {
			handleSQLException(e);
			return false;
		}
		/* Fill in statement data. */
		try {
			insert.setString(1, song.getTitle());
			insert.setString(2, song.getAlbum());
			insert.setString(3, song.getPerformer(0));
			insert.setString(4, song.getPerformer(1));
			insert.setString(5, song.getPerformer(2));
			insert.setString(6, song.getComment(0));
			insert.setString(7, song.getComment(1));
			insert.setString(8, song.getComment(2));
			insert.setString(9, song.getTrackNum());
			insert.setString(10, song.getYear());
			insert.setString(11, song.getEncodedBy());
			insert.setString(12, song.getComposer());
			insert.setString(13, song.getFileName());
			insert.setString(14, song.getPictureName());
			insert.setString(15, owner);

			insert.executeUpdate();
		} catch (SQLException e) {
			handleSQLException(e);
			return false;
		}

		return true;
	}

	/**
	 * Takes in a broad search term and queries all the pertinent fields that the user
	 * is allowed to search for including performers0,1,2, title, and artist. It is also
	 * used to simply return all songs (visible to the user) such as from the "View Music
	 * Collection" link.
	 * @param searchString - Broad search term
	 * @param sortType - What field to sort by (defined in Controller)
	 * @param owner - Currently logged in user. Compared with owner field in database to
	 * 				  determine which songs this user can view.
	 * @return a List of type SongData of the search results
	 */
	public List<SongData> simpleSearch(String searchString, int sortType,
			String owner) {
		PreparedStatement q = null;
		ResultSet rs = null;
		String sort;
		String query;

		if (sortType == Controller.SORT_BY_ALBUM) {
			sort = "album";
		} else if (sortType == Controller.SORT_BY_ARTIST) {
			sort = "performers0";
		} else {
			sort = "title";
		}

		/* Prepare query statement with wildcards */

		query = "SELECT * FROM " + MP3_TABLE + " s "
				+ "WHERE (s.title LIKE ? OR " + "s.album LIKE ? OR "
				+ "s.performers0 LIKE ? OR " + "s.performers1 LIKE ? OR "
				+ "s.performers2 LIKE ?) ";

		if (!owner.equals("admin")) {
			query += "AND s.owner = ?";
		}
		query += "ORDER BY " + sort + ";";
		try {
			q = conn.prepareStatement(query);

		} catch (SQLException e) {
			handleSQLException(e);
			return null;
		}

		/* Fill in wildcards with search string and the sort type */
		try {
			q.setString(1, "%" + searchString + "%");
			q.setString(2, "%" + searchString + "%");
			q.setString(3, "%" + searchString + "%");
			q.setString(4, "%" + searchString + "%");
			q.setString(5, "%" + searchString + "%");

			if (!owner.equals("admin")) {
				q.setString(6, owner);
			}

			rs = q.executeQuery();
		} catch (SQLException e) {
			handleSQLException(e);
			return null;
		}

		/* Process Results and return List<SongData> */
		return processResults(rs);
	}


	private List<SongData> advancedSearch(String artist, String title,
			String album, boolean AND, int sortType, String owner) {
		String sort;
		PreparedStatement q = null;
		ResultSet rs = null;
		String query;
		String andOr; // default to AND search
		boolean useAndOr = false;
		int wcNum = 0;
		String wcArr[] = new String[6]; // Keeps track of which wildcards are
		// used where since not all parts of
		// the prepared statement are used.

		if (sortType == Controller.SORT_BY_ALBUM) {
			sort = "album";
		} else if (sortType == Controller.SORT_BY_ARTIST) {
			sort = "performers0";
		} else {
			sort = "title";
		}

		/* Set and/or boolean value */
		if (AND)
			andOr = "AND";
		else
			andOr = "OR";

		/* user entered no data, return null */
		if (artist == null && title == null && album == null) {
			return (simpleSearch("", sortType, owner));
		}

		/* Form query statement with wildcards */
		query = "SELECT * FROM " + MP3_TABLE + " s WHERE";
		if (artist != null) {
			query += " ((s.performers0 LIKE ?" + " OR ";
			query += "s.performers1 LIKE ?" + " OR ";
			query += "s.performers2 LIKE ?)";

			useAndOr = true;
			wcArr[0] = artist;
			wcArr[1] = artist;
			wcArr[2] = artist;
			wcNum = 3;
		}
		if (title != null) {
			if (useAndOr)
				query += " " + andOr;
			else
				query += " (";
			query += " s.title LIKE ?";
			useAndOr = true;
			wcArr[wcNum] = title;
			wcNum++;
		}
		if (album != null) {
			if (useAndOr)
				query += " " + andOr;
			else
				query += " (";
			query += " s.album LIKE ?";
			useAndOr = true;
			wcArr[wcNum] = album;
			wcNum++;
		}

		if (useAndOr)
			query += ")";
		if (!owner.equals("admin")) { // skip this part if user is admin - can
			// see all songs
			query += " AND s.owner = ?";
		}

		query += " ORDER BY " + sort + ";";

		try {
			q = conn.prepareStatement(query);
		} catch (SQLException e) {
			handleSQLException(e);
			return null;
		}

		/* Fill in wildcards in statement */
		try {
			int i;
			for (i = 0; i < wcNum; i++) {
				q.setString(i + 1, "%" + wcArr[i] + "%");
			}
			if (!owner.equals("admin")) {
				q.setString(i + 1, owner);
			}

			rs = q.executeQuery();
		} catch (SQLException e) {
			handleSQLException(e);
			return null;
		}

		/* Process Results and return List<SongData> */
		return processResults(rs);
	}

	/*
	 * processResults takes in a ResultSet from a query and extracts each field
	 * into a new SongData object which are added to a List and then returned.
	 */
	private List<SongData> processResults(ResultSet rs) {
		List<SongData> songList = new ArrayList<SongData>();
		try {
			while (rs.next()) {
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
				s.setOwner(rs.getString("owner"));
				songList.add(s);
			}
		} catch (SQLException e) {
			handleSQLException(e);
			return null;
		}
		return songList;
	}

	/**
	 * Delete a song from the database by comparing the fileName parameter with all file
	 * names in the database. No two songs can have the same file name.
	 * @param fileName - the file name to search for in the database
	 * @param owner - the currently logged in user. Used to make sure user has permissions
	 * 				  delete the file
	 * @return true if the song was successfully deleted, false on any SQLException
	 */
	public boolean deleteSong(String fileName, String owner) {
		PreparedStatement q1; // query statement
		ResultSet rs1;
		PreparedStatement q2; // delete statement

		/*
		 * First check to see if user has rights to delete the file. Skip check
		 * if admin
		 */
		if (!owner.equals("admin")) {
			try {
				q1 = conn.prepareStatement("SELECT owner FROM " + MP3_TABLE
						+ " WHERE fileName = ?;");
				q1.setString(1, fileName);

				rs1 = q1.executeQuery();
				rs1.next();
				if (!rs1.getString("owner").equals(owner)) { // return false if
					// ownership
					// mismatch
					return false;
				}
			} catch (SQLException e) {
				handleSQLException(e);
				return false;
			}
		}

		/*
		 * If ownership matches or if logged in user is admin, execute the
		 * delete statement
		 */
		try {
			q2 = conn.prepareStatement("DELETE FROM " + MP3_TABLE
					+ " WHERE fileName = ?;");
			q2.setString(1, fileName);

			q2.execute();
		} catch (SQLException e) {
			handleSQLException(e);
			return false;
		}

		return true;
	}

	/**
	 * Delete a user from the database. Method verifies that the user is an admin before allowing
	 * the deletion.
	 * @param nameToDelete - user to delete from the database
	 * @param currentUser - currently logged in user.
	 * @return true if successfully deleted, false if not admin or on any SQLException
	 */
	public boolean deleteUser(String nameToDelete, String currentUser) {
		PreparedStatement q; // query statement

		if (currentUser.equals("admin")) {
			try {
				q = conn.prepareStatement("DELETE FROM " + USER_TABLE
						+ " WHERE username = ?;");
				q.setString(1, nameToDelete);

				q.execute();
				return true;
			} catch (SQLException e) {
				handleSQLException(e);
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Create a new user in the database. Provide a username and a hashed
	 * password. Query will fail if the user attempts to create a user that
	 * already exists.
	 * @param user - new user to add
	 * @param password - hashed password to associate with user
	 * @return true if user was successfully added, false on SQLException (will
	 * 		   be thrown if user already exists).
	 */
	public boolean addUser(String user, byte password[]) {
		PreparedStatement q;

		try {
			q = conn.prepareStatement("INSERT INTO " + USER_TABLE
					+ " (username, pass)" + " VALUES (?, ?);");

			q.setString(1, user);
			q.setBytes(2, password);

			q.execute();
		} catch (SQLException e) {
			handleSQLException(e);
			return false;
		}
		return true;
	}

	/*
	 * Return the hashed password from a user in the database. Can be used to
	 * verify login credentials of a user that already exists. If the user does
	 * not exist, returns a null to indicate that the username attempted to
	 * login with was invalid.
	 */
	public byte[] getHashedPassword(String user) {
		PreparedStatement q;
		ResultSet rs;

		try {
			q = conn.prepareStatement("SELECT * FROM " + USER_TABLE
					+ " WHERE username = ?;");
			q.setString(1, user);
			rs = q.executeQuery();
			rs.next();
			return rs.getBytes("pass");
		} catch (SQLException e) {
			handleSQLException(e);
			return null;
		}
	}

	/**
	 * @return a list of all the users for administration purposes. Returns null
	 * if any error.
	 */
	public List<String> getAllUsers() {
		Statement s;
		ResultSet rs;
		List<String> userList = new ArrayList<String>();

		/* Get all users from database */
		try {
			s = conn.createStatement();
			rs = s.executeQuery("SELECT username FROM " + USER_TABLE
					+ " ORDER BY username ASC;");

			/* Add user results to List and return */
			while (rs.next()) {
				userList.add(rs.getString(1));
			}

			return userList;
		} catch (SQLException e) {
			handleSQLException(e);
			return null;
		}
	}

	/**
	 * 	/**
	 * Takes in a broad search term, multiple search terms (artist, title, album), 
	 * the join type (AND/OR), the field to sort by (enum defined in Controller), 
	 * and the currently logged in user.
	 * @param broadTerm - broad search term
	 * @param title - title to search for
	 * @param artist - artist to search for
	 * @param album - album to search for
	 * @param sortBy - what field to sort by (defined in Controller)
	 * @param username - currently logged in user - used to determine what results
	 * 					 are returned
	 * @return a List of type SongData containing all the songs in the search result
	 */
	public List<SongData> search(String broadTerm, String title, String artist,
			String album, int sortBy, String username) {
		if (broadTerm != null) {
			return simpleSearch(broadTerm, sortBy, username);
		}
		if (artist.trim().equals(""))
			artist = null;
		if (title.trim().equals(""))
			title = null;
		if (album.trim().equals(""))
			album = null;
		return advancedSearch(artist, title, album, false, sortBy, username);
	}

	/*
	 * Simple method to handle {@link SQLException}s. Loops through generated
	 * SQLExceptions and prints them the the specified {@link PrintStream}.
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
					+ "Error code: " + sqle.getErrorCode() + "\n\t\t"
					+ "SQLState: " + sqle.getSQLState() + "\n";
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
