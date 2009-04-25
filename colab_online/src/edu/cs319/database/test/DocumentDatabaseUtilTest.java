package edu.cs319.database.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import edu.cs319.database.DocumentDatabaseUtil;
import edu.cs319.dataobjects.CoLabRoom;
import edu.cs319.dataobjects.CoLabRoomMember;
import edu.cs319.dataobjects.impl.DocumentSubSectionImpl;
import edu.cs319.dataobjects.impl.SectionizedDocumentImpl;

public class DocumentDatabaseUtilTest {

	@Test
	public void testSaveCoLab() {
		CoLabRoom nr = new CoLabRoom("testCoLabRoom", new CoLabRoomMember("ian", null));
		
		//Set up sub sections
		DocumentSubSectionImpl s1 = new DocumentSubSectionImpl(),
			s2=new DocumentSubSectionImpl(), 
			s3=new DocumentSubSectionImpl(),
			s4=new DocumentSubSectionImpl();
		
		s1.setName("sub section 1");
		s2.setName("sub section 2");
		s3.setName("sub section 3");
		s4.setName("sub section 4");
		
		s1.setText("This is the super cool text of the first awesome series of test to come from my computer, and they will all pass or I will destroy everyone who has ever had any part in the development of Hibernate or its related technologies");
		s2.setText("1111111111111111111111111111111111112222222222222222222222233333333333333333333333333334444444444444455555555555555555666666666666666666666667777777777777777777777778888888888888888889999999999999999999999999991012381823082903lkdnafljshfvdanbe;kfwuhnbvdak;luv b");
		s3.setText("THis is very small but it is mightier that the meaningless ramble of numbers in the sub section above it");
		s4.setText("This is even smalller but it knows it sucks");
		
		SectionizedDocumentImpl d1 = new SectionizedDocumentImpl("Just One"), 
				d2= new SectionizedDocumentImpl("Two!!"), 
				d3= new SectionizedDocumentImpl("GETTING BIGGER with THREE!!!"),
				d4= new SectionizedDocumentImpl("THE MIGHTIEST OF ALL>>>MAXCAPACITY");
		
		d1.addSubSection(s1, 0);
		
		d2.addSubSection(s1, 0);
		d2.addSubSection(s2, 1);
		
		d3.addSubSection(s1, 0);
		d3.addSubSection(s2, 1);
		d3.addSubSection(s3, 2);
		
		d4.addSubSection(s1, 0);
		d4.addSubSection(s2, 1);
		d4.addSubSection(s3, 2);
		d4.addSubSection(s4, 3);
		
		nr.addDocument(d1);
		nr.addDocument(d2);
		nr.addDocument(d3);
		nr.addDocument(d4);
		
		DocumentDatabaseUtil.saveCoLab("ian", nr);
		
		CoLabRoom ret = DocumentDatabaseUtil.getCoLabRoom("testCoLabRoom");
		
		assertNotNull("Co Lab room should not be null", ret);
	}

	@Test
	public void testGetRoomNames() {
		
	}

	@Test
	public void testGetCoLabRoom() {
		
		DocumentDatabaseUtil dbUtil = new DocumentDatabaseUtil();
		
		String roomName = "room36";
		
		CoLabRoom newRoom = dbUtil.getCoLabRoom(roomName);
		
		assertNotNull("New room should not be null", newRoom);
	}

}
