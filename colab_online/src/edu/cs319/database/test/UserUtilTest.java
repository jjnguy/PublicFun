package edu.cs319.database.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import edu.cs319.database.UsersUtil;
import edu.cs319.util.Util;

public class UserUtilTest {

	@Ignore
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
	
	@Ignore
	@Test
	public void testDeleteUser(){
		String password = "pass";
		String username = "deletedSoon";
		
		byte[] passbytes = password.getBytes();
		byte[] hashedPass = Util.getHashedBytes(passbytes);
		
		UsersUtil.createUser(username, hashedPass);
		
		UsersUtil.deleteUser("deletedSoon");
		
		assertFalse("User should not be authenticate...does not exist" , UsersUtil.authenticateUser("deletedSoon", hashedPass));
	}

}
