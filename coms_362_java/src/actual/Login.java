package actual;
import java.util.Arrays;
import util.Util;

import databaseAccess.Database;
public class Login 
{

	public boolean login (String username, String password, Database db)
	{
		if ((username == null) || (password == null))
			return false;
		
		if (Arrays.equals(Util.getHashedBytes(password.getBytes()), db.getHashedPassword(username)))
				return true;
				
		else
				return false;
	}
}


