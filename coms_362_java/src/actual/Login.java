package actual;
import java.util.Arrays;
import util.Util;

import databaseAccess.QueryDB;
public class Login 


{
	public boolean login (String username, String password, QueryDB db)
	{
		if ((username == null) || (password == null))
			return false;
		
		if (Arrays.equals(Util.getHashedBytes(password.getBytes()), db.getHashedPassword(username)))
			return true;
				
		else
			return false;
	}
	
	
	
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


