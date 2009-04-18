package edu.cs319.tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

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
	
	//public int getSubsectionCount();
	
	//public DocumentSubSection getSectionAt(int idx);
	
	//public DocumentSubSection getSection(String id);
	
	//public int getSubSectionIndex(String sectionID);
	
	//public String getFullText();
	
	//public boolean addSubSection(DocumentSubSection ds, int index);

	//public void removeSubSection(String name);

	//public void splitSubSection(String name, String partA, String partB, int splitIndex);

	//public void combineSubSections(String partA, String partB, String combinedName);
	
	//public void flopSubSections(int idx1, int idx2);

	
}
