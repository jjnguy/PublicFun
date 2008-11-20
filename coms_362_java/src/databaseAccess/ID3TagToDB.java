package databaseAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import id3TagStuff.ID3v2_XTag;
import id3TagStuff.frames.ID3v2_XFrame;
import id3TagStuff.id3Data.ID3_Picture;

public class ID3TagToDB {

	private static final String DB_AND_TABLE_NAME = "songdata";

	private Connection connection;
	private ID3v2_XTag tag;

	public ID3TagToDB(String dbURL, String dbPW, String usr, ID3v2_XTag tag)
			throws SQLException {
		connection = DriverManager.getConnection(dbURL, usr, dbPW);
		this.tag = tag;
	}

	/**
	 * @return error?
	 */
	public boolean storeSongData() {
		Statement storeStatement = null;
		try {
			storeStatement = connection.createStatement();
		} catch (SQLException e) {
			Database.handleSQLException(e);
			return false;
		}
		int rowCount;
		String command = formulateUpdateString();
		try {
			rowCount = storeStatement.executeUpdate(command);
		} catch (SQLException e) {
			Database.handleSQLException(e);
			return false;
		}
		return true;
	}

	private String formulateUpdateString() {
		// BTW
		final int tableWidth = 13;
		int commentCount = 0;
		String title = null, album = null, performer1 = null;
		String[] comments = new String[3];
		int[] picture = null;
		String track = null, performer2 = null, performer3 = null;
		int year = -1;
		String encodedBy = null, composer = null;

		for (ID3v2_XFrame frame : tag.getAllFrames()) {
			if (frame.getFrameID().matches("COMM?")) {
				if (commentCount > 2) {
					System.err.println("A comment tag was ignored");
					continue;
				}
				comments[commentCount] = frame.getData().toString();
			} else if (frame.getFrameID().matches("A?PIC")) {
				if (picture != null) {
					System.err.println("A picture tag was ignored");
					continue;
				}
				picture = ((ID3_Picture) frame.getData()).getPicData();
			} else if (frame.getFrameID().matches("TALB?")) {
				if (album != null) {
					System.err.println("An album tag was ignored");
					continue;
				}
				album = frame.getData().toString();
			} else if (frame.getFrameID().matches("TCO?M")) {
				if (composer != null) {
					System.err.println("A composer tag was ingnored");
					continue;
				}
				composer = frame.getData().toString();
			} else if (frame.getFrameID().matches("TI?T2")) {
				if (title != null) {
					System.err.println("A title tag was ingnored");
					continue;
				}
				title = frame.getData().toString();
			} else if (frame.getFrameID().matches("TRC?K")) {
				if (track != null) {
					System.err.println("A track tag was ingnored");
					continue;
				}
				track = frame.getData().toString();
			} else if (frame.getFrameID().matches("TYER?")) {
				if (year != -1) {
					System.err.println("A year tag was ingnored");
					continue;
				}
				try {
					year = Integer.parseInt(frame.getData().toString());
				} catch (NumberFormatException e) {
					year = -1;
				}
			} else if (frame.getFrameID().matches("TENC?")) {
				if (encodedBy != null) {
					System.err.println("An encoded-by tag was ingnored");
					continue;
				}
				encodedBy = frame.getData().toString();
			} else if (frame.getFrameID().matches("TPE?1")) {
				if (performer1 != null) {
					System.err.println("An artist tag was ingnored");
					continue;
				}
				performer1 = frame.getData().toString();
			} else {
				// laze, leave p2 and 3 blank
				System.err.println("Skipped a tag with ID: " + frame.getFrameID());
			}
		}
		
		StringBuilder ret = new StringBuilder();
		ret.append("INSERT INTO " + DB_AND_TABLE_NAME + " ");
		ret.append(title + ", ");
		ret.append(album + ", ");
		ret.append(performer1 + ", ");
		ret.append(comments[0] + ", ");
		ret.append(comments[1] + ", ");
		ret.append(comments[2] + ", ");
		ret.append("pic" + ", ");
		ret.append(track + ", ");
		ret.append(performer2 + ", ");
		ret.append(performer3 + ", ");
		ret.append(year + ", ");
		ret.append(encodedBy + ", ");
		ret.append(composer);
		
		return ret.toString();
	}
	public static void main(String[] args) {
		// TODO To be tested later!!!
	}
}
