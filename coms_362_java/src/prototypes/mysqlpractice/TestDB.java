package prototypes.mysqlpractice;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import databaseAccess.Database;

public class TestDB {
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		try {
			/**
			 * Load jdbc driver for MySQL
			 */
			Class c = Class.forName("com.mysql.jdbc.Driver");
			// Loads database name of a mysql database into url
			// localhost says that it is on the current machine and test says that the
			// name of the database is test.
			String url = "jdbc:mysql://localhost";

			String pword = "CH0C0LATEM1LK";
			String user = "root";
			// We have to establish a jdbc connection to the database
			Connection connection = DriverManager.getConnection(url, user, pword);

			String qs2 = "create database madeByJustinNow;";
			Statement st = connection.createStatement();
			st.execute(qs2);
			st.close();
			connection.close();

		} catch (SQLException sqle) {
			Database.handleSQLException(sqle);
		}
	}
}

/*Shaun - Testing Subclipse*/