package edu.cs319.database.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.cs319.database.UsersUtil;
import edu.cs319.util.Util;

public class UserUtilTest {

	@Test
	public void testAuthenticateUser() {
		String password = "pass";
		String username = "user";
		
		byte[] passbytes = password.getBytes();
		byte[] hashedPass = Util.getHashedBytes(passbytes);
		
		UsersUtil.createUser(username, hashedPass);
		
		assertTrue("SHould pass authentication", UsersUtil.authenticateUser(username, hashedPass));
	}

	@Test
	public void testCreateUser() {
		
		String password = "pass";
		String username = "admin";
		
		byte[] passbytes = password.getBytes();
		byte[] hashedPass = Util.getHashedBytes(passbytes);
		
		UsersUtil.createUser(username, hashedPass);
	}

}
