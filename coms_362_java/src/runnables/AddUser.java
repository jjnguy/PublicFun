package runnables;

import java.sql.SQLException;

import controller.Controller;
import util.Util;
import databaseAccess.Database;

public class AddUser {
	public static void main(String[] args) throws SQLException {
		Database d = new Database();
		d.startDatabase(Controller.DB_URL, Controller.DB_USR, Controller.DB_PW);
		d.addUser("jjnguy", Util.getHashedBytes("".getBytes()));
	}
}
