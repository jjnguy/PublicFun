package databaseAccess;

import java.io.PrintStream;
import java.sql.SQLException;

public class DatabaseUtil {

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

	public static void handleSQLException(SQLException sqle) {
		handleSQLException(sqle, System.out);
	}
}
