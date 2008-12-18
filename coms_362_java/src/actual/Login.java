package actual;
import java.util.Arrays;
import util.Util;
import databaseAccess.QueryDB;

/**
 * For creating a new user or logging an existing user into the system
 * 
 * @author Benjamin Petersen
 *
 */
public class Login 
{
	/**
	 * Logs the user into the system
	 * 
	 * @param username
	 * 			username of the user who wished to log in
	 * @param password
	 * 			password of the user
	 * @param db
	 * 			QueryDB class that is being used
	 * @return
	 * 		true if the login is successful, false if not
	 */
	public boolean login (String username, String password, QueryDB db)
	{
		if ((username == null) || (password == null))
			return false;
		
		if (Arrays.equals(Util.getHashedBytes(password.getBytes()), db.getHashedPassword(username)))
			return true;
				
		else
			return false;
	}
	
	
	/**
	 * Creates a new user and adds them to the system
	 * 
	 * @param username
	 * 			desired username
	 * @param password1
	 * 			desired password
	 * @param password2
	 * 			desired password again for verification
	 * @param db
	 * 		QueryDB class that is being used
	 * @return
	 * 		true if the user was successfully created, false if not
	 */
	public boolean createUser(String username, String password1, String password2, QueryDB db)
	{
		if ((password1 == null)||(password2 == null)||(!password1.equals(password2)))
				return false;
		
		if (db.addUser(username, Util.getHashedBytes(password1.getBytes())))  
			return true;
		
		else 
			return false;
		
	}
}


