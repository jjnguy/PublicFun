package edu.cs319.database.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import edu.cs319.database.DocumentDatabaseUtil;
import edu.cs319.dataobjects.CoLabRoom;

public class DocumentDatabaseUtilTest {

	@Test
	public void testSaveCoLab() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRoomNames() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCoLabRoom() {
		
		DocumentDatabaseUtil dbUtil = new DocumentDatabaseUtil();
		
		String roomName = "room36";
		
		CoLabRoom newRoom = dbUtil.getCoLabRoom(roomName);
		
		assertNotNull("New room should not be null", newRoom);
	}

}
