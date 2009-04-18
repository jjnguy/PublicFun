package edu.cs319.tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import edu.cs319.dataobjects.DocumentSubSection;
import edu.cs319.dataobjects.impl.DocumentSubSectionImpl;

public class DocumentSubSectionTest {

	private DocumentSubSection doc;
	private DocumentSubSection doc2;

	@Before
	public void setUo() {
		doc = new DocumentSubSectionImpl("Test");
		doc2 = new DocumentSubSectionImpl("Texted");
		doc2.setLocked(true,"bob");
		doc2.setText("bob", "hello");
	}

	//public abstract String getName();

	@Test
	public void getName() {
		assertEquals(doc.getName(),"Test");
	}

	//public abstract String getText();

	@Test
	public void getTextEmpty() {
		assertEquals(doc.getText(),"");
	}
	
	@Test
	public void getTextNotEmpty() {
		assertEquals(doc2.getText(),"hello");
	}

	//public abstract boolean setText(String username, String text);

	@Test
	public void setTextUnlocked() {
		assertFalse(doc.setText("joe","hi"));
		assertEquals(doc.getText(),"");
	}
	
	@Test
	public void setTextLockedRightUser() {
		assertTrue(doc2.setText("bob","yay"));
		assertEquals(doc2.getText(),"yay");
	}
	
	@Test
	public void setTextLockedWrongUser() {
		assertFalse(doc2.setText("joe","boo"));
		assertEquals(doc2.getText(),"hello");
	}

	//public abstract boolean isLocked();

	@Test
	public void unlocked() {
		assertFalse(doc.isLocked());
	}
	
	@Test
	public void locked() {
		assertTrue(doc2.isLocked());
	}

	//public abstract void setLocked(boolean lock, String username);

	@Test
	public void lockUnlocked() {
		assertFalse(doc.isLocked());
		doc.setLocked(true,"joe");
		assertTrue(doc.isLocked());
	}
	
	@Test
	public void unlockLockedRightUser() {
		assertTrue(doc2.isLocked());
		doc2.setLocked(false,"bob");
		assertFalse(doc2.isLocked());
	}

	@Test
	public void unlockLockedWrongUser() {
		assertTrue(doc2.isLocked());
		doc2.setLocked(false,"joe");
		assertTrue(doc2.isLocked());
	}

	//public abstract String lockedByUser();
	
	@Test
	public void lockedByUserUnlocked() {
		assertNull(doc.lockedByUser());
	}
	
	@Test
	public void lockedByUserLocked() {
		assertEquals(doc2.lockedByUser(),"bob");
	}
	
	@Test
	public void lockedByUserNewlyUnlocked() {
		doc2.setLocked(false,"bob");
		assertNull(doc2.lockedByUser());
	}
}
