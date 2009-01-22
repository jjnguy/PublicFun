import java.sql.SQLException;

import util.Util;

import databaseAccess.QueryDB;


public class AddUser {

	public static void main(String[] args) throws SQLException {
		QueryDB db = new QueryDB();
		db.startDatabase("jdbc:mysql://localhost", "root", "CH0C0LATEM1LK");
		db.addUser("admin", Util.getHashedBytes("admin".getBytes()));
	}
	
}
