package edu.cs319.tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

import edu.cs319.dataobjects.DocumentSubSection;
import edu.cs319.dataobjects.SectionizedDocument;
import edu.cs319.dataobjects.impl.DocumentSubSectionImpl;
import edu.cs319.dataobjects.impl.SectionizedDocumentImpl;

public class SectionizedDocumentTest {

	private SectionizedDocument doc;
	private SectionizedDocument doc2;
	
	@Before
	public void setUp() {
		doc = new SectionizedDocumentImpl("Empty");
		doc2 = new SectionizedDocumentImpl("Full");
		DocumentSubSection ss = new DocumentSubSectionImpl("one");
		ss.setLocked(true, "joe");
		ss.setText("joe","One");
		doc2.addSubSection(ss,0);
		ss = new DocumentSubSectionImpl("two");
		ss.setLocked(true, "joe");
		ss.setText("joe","Two");
		doc2.addSubSection(ss,1);
		ss = new DocumentSubSectionImpl("three");
		ss.setLocked(true, "joe");
		ss.setText("joe","Three");
		doc2.addSubSection(ss,2);
	}

	//public String getName();

	@Test
	public void getNameEmpty() {
		assertEquals("Empty",doc.getName());
	}
	
	@Test
	public void getNameFull() {
		assertEquals("Full",doc2.getName());
	}

	//public List<DocumentSubSection> getAllSubSections();
	
	@Test
	public void getAllEmpty() {
		assertEquals(doc.getAllSubSections().size(),0);
	}
	
	@Test
	public void getAllFull() {
		assertEquals(doc2.getAllSubSections().size(),3);
		assertEquals(doc2.getAllSubSections().get(0).getName(),"one");
		assertEquals(doc2.getAllSubSections().get(1).getName(),"two");
		assertEquals(doc2.getAllSubSections().get(2).getName(),"three");
	}
	
	//public void removeAllSubSections();

	@Test
	public void removeAllEmpty() {
		doc.removeAllSubSections();
		assertEquals(doc.getAllSubSections().size(),0);
	}
	
	@Test
	public void removeAllFull() {
		doc2.removeAllSubSections();
		assertEquals(doc2.getAllSubSections().size(),0);
	}

	//public void addAllSubSections(List<DocumentSubSection> ss);
	
	@Test
	public void addAllEmpty() {
		DocumentSubSection s1 = new DocumentSubSectionImpl("A");
		DocumentSubSection s2 = new DocumentSubSectionImpl("B");
		DocumentSubSection s3 = new DocumentSubSectionImpl("C");
		List<DocumentSubSection> ls = new ArrayList<DocumentSubSection>();
		ls.add(s1);
		ls.add(s2);
		ls.add(s3);
		doc.addAllSubSections(ls);
		assertEquals(doc.getAllSubSections(),ls);
	}
	
	@Test
	public void addAllFull() {
		DocumentSubSection s1 = new DocumentSubSectionImpl("A");
		DocumentSubSection s2 = new DocumentSubSectionImpl("B");
		DocumentSubSection s3 = new DocumentSubSectionImpl("C");
		List<DocumentSubSection> ls = new ArrayList<DocumentSubSection>();
		ls.add(s1);
		ls.add(s2);
		ls.add(s3);
		doc2.addAllSubSections(ls);
		String[] names = new String[doc2.getSubSectionCount()];
		for(int i = 0; i < names.length; i++) {
			names[i] = doc2.getSectionAt(i).getName();
		}
		assertArrayEquals(names,new String[] {"one","two","three","A","B","C"});
	}

	//public int getSubsectionCount();
	
	@Test
	public void getSubSectionCountEmpty() {
		assertEquals(doc.getSubSectionCount(),0);
	}
	
	@Test
	public void getSubSectionCountFull() {
		assertEquals(doc2.getSubSectionCount(),3);
	}
	
	//public DocumentSubSection getSectionAt(int idx);
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void getSectionAtIndexEmpty() {
		doc.getSectionAt(1);
	}
	
	@Test
	public void getSectionAtIndexFull() {
		DocumentSubSection s1 = doc2.getSectionAt(0);
		DocumentSubSection s2 = doc2.getSectionAt(1);
		DocumentSubSection s3 = doc2.getSectionAt(2);
		assertArrayEquals(new String[] {s1.getName(),s2.getName(),s3.getName()}, new String[] {"one","two","three"});
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void getSectionAtIndexFullOOB() {
		doc.getSectionAt(3);
	}
	
	//public DocumentSubSection getSection(String id);
	
	@Test
	public void getSectionEmpty() {
		assertNull(doc.getSection("Null"));
	}
	
	@Test
	public void getSectionFull() {
		DocumentSubSection s1 = doc2.getSection("one");
		DocumentSubSection s2 = doc2.getSection("two");
		DocumentSubSection s3 = doc2.getSection("three");
		assertArrayEquals(new String[] {s1.getText(),s2.getText(),s3.getText()}, new String[] {"One","Two","Three"});
	}
	
	//public int getSubSectionIndex(String sectionID);
	
	@Test
	public void getSubSectionIndexNonExistant() {
		assertEquals(-1, doc.getSubSectionIndex("hi"));
	}
	
	@Test
	public void getSubSectionIndexExistant() {
		int one = doc2.getSubSectionIndex("one");
		int two = doc2.getSubSectionIndex("two");
		int three = doc2.getSubSectionIndex("three");
		assertArrayEquals(new int[] {one,two,three}, new int[] {0,1,2});
	}
	
	//public String getFullText();
	
	@Test
	public void getFullTextEmpty() {
		assertEquals(doc.getFullText(),"");
	}
	
	@Test
	public void getFullTextFull() {
		assertEquals(doc2.getFullText(),"One\nTwo\nThree\n");
	}
	
	//public boolean addSubSection(DocumentSubSection ds, int index);

	@Test
	public void addToEmpty() {
		assertTrue(doc.addSubSection(new DocumentSubSectionImpl("hi"),0));
		assertEquals(doc.getSectionAt(0).getName(),"hi");
	}
	
	@Test
	public void addToEmptyNonStandard() {
		assertFalse(doc.addSubSection(new DocumentSubSectionImpl("wrong"),1));
		assertFalse(doc.addSubSection(new DocumentSubSectionImpl("stillWrong"),-1));
	}
	
	@Test
	public void addSubSectionAtIndex() {
		assertTrue(doc2.addSubSection(new DocumentSubSectionImpl("B"),3));
		assertTrue(doc2.addSubSection(new DocumentSubSectionImpl("A"),0));
		assertEquals(doc2.getSubSectionIndex("A"),0);
		assertEquals(doc2.getSubSectionIndex("B"),4);
	}
	
	@Test
	public void addSubSectionExisting() {
		assertFalse(doc2.addSubSection(new DocumentSubSectionImpl("one"),1));
	}
		

	//public boolean removeSubSection(String name);

	@Test
	public void removeSubSectionEmpty() {
		assertFalse(doc.removeSubSection("A"));
	}
	
	@Test
	public void removeSubSectionFull() {
		assertTrue(doc2.removeSubSection("one"));
		assertEquals(doc2.getSubSectionCount(),2);
		assertNull(doc2.getSection("one"));
	}

	//public boolean splitSubSection(String name, String partA, String partB, int splitIndex);

	@Test
	public void splitNonExistant() {
		assertFalse(doc.splitSubSection("a","b","c",2,"bob"));
	}
	
	@Test
	public void splitOutOfBoundsNegative() {
		assertFalse(doc2.splitSubSection("one","oneA","oneB",-1,"joe"));
		String[] names = new String[doc2.getSubSectionCount()];
		for(int i = 0; i < names.length; i++) {
			names[i] = doc2.getSectionAt(i).getName();
		}
		assertArrayEquals(names,new String[] {"one","two","three"});
	}

	@Test
	public void splitOutOfBoundsPositive() {
		assertFalse(doc2.splitSubSection("one","oneA","oneB",3,"joe"));
		String[] names = new String[doc2.getSubSectionCount()];
		for(int i = 0; i < names.length; i++) {
			names[i] = doc2.getSectionAt(i).getName();
		}
		assertArrayEquals(names,new String[] {"one","two","three"});
	}
	
	@Test
	public void splitExistantLocked() {
		assertTrue(doc2.splitSubSection("one","oneA","oneB",1,"joe"));
		assertEquals(doc2.getSubSectionCount(),4);
		assertEquals(doc2.getSubSectionIndex("oneA"),0);
		assertEquals(doc2.getSubSectionIndex("oneB"),1);
		assertEquals(doc2.getSectionAt(0).getText(),"O");
		assertEquals(doc2.getSectionAt(1).getText(),"ne");
		assertEquals(doc2.getSection("oneA").lockedByUser(),"joe");
		assertEquals(doc2.getSection("oneB").lockedByUser(),"joe");
	}
	
	@Test
	public void splitExistantUnlocked() {
		doc2.getSection("one").setLocked(false,"joe");
		assertTrue(doc2.splitSubSection("one","oneA","oneB",1,"joe"));
		assertEquals(doc2.getSubSectionCount(),4);
		assertEquals(doc2.getSubSectionIndex("oneA"),0);
		assertEquals(doc2.getSubSectionIndex("oneB"),1);
		assertEquals(doc2.getSectionAt(0).getText(),"O");
		assertEquals(doc2.getSectionAt(1).getText(),"ne");
		assertFalse(doc2.getSection("oneA").isLocked());
		assertFalse(doc2.getSection("oneB").isLocked());
	}
	
	//public boolean combineSubSections(String partA, String partB, String combinedName);

	@Test
	public void combineNonExistant() {
		assertFalse(doc.combineSubSections("one","b","oneb"));
		assertFalse(doc.combineSubSections("a","two","atwo"));
	}
		
	@Test
	public void combineExistantLocked() {
		assertFalse(doc2.combineSubSections("one","two","oneB"));
		assertEquals(doc2.getSubSectionCount(),3);
		assertEquals(doc2.getSubSectionIndex("one"),0);
		assertEquals(doc2.getSubSectionIndex("two"),1);
		assertEquals(doc2.getSubSectionIndex("oneB"),-1);
		assertEquals(doc2.getSection("one").lockedByUser(),"joe");
		assertEquals(doc2.getSection("two").lockedByUser(),"joe");
	}
	
	@Test
	public void combineExistantUnlocked() {
		doc2.getSection("one").setLocked(false,"joe");
		doc2.getSection("two").setLocked(false,"joe");
		assertTrue(doc2.combineSubSections("one","two","oneB"));
		assertEquals(doc2.getSubSectionCount(),2);
		assertEquals(doc2.getSubSectionIndex("oneB"),0);
		assertEquals(doc2.getSectionAt(0).getText(),"One\nTwo");
		assertFalse(doc2.getSection("oneB").isLocked());
	}
	
	//public boolean flopSubSections(int idx1, int idx2);
	
	@Test
	public void flopOutOfBounds() {
		assertFalse(doc.flopSubSections(1,-1));
	}
	
	@Test
	public void flopExisting() {
		assertTrue(doc2.flopSubSections(0,1));
		assertEquals(doc2.getSubSectionCount(),3);
		assertEquals(doc2.getSubSectionIndex("one"),1);
		assertEquals(doc2.getSubSectionIndex("two"),0);
	}
}
