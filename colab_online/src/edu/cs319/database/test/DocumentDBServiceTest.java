package edu.cs319.database.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import edu.cs319.database.CoLabSave;
import edu.cs319.database.DocumentDBService;
import edu.cs319.dataobjects.impl.DocumentSubSectionImpl;
import edu.cs319.dataobjects.impl.SectionizedDocumentImpl;

public class DocumentDBServiceTest extends TestCase {

	@Test
	public void testAddDocumentSubSection(){
		DocumentDBService dbServ = new DocumentDBService();
		DocumentSubSectionImpl doc = new DocumentSubSectionImpl("sub4");
		doc.setLocked(true, "john");
		doc.setText("john", "hd;eoulvdkhc;laksoyhfdp9-oiue;jkv badfpigkdfjdjkffffffffffffffffffffffffffffffffffffffffffffffffkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkowfieeiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiijkcdckkkkkkkkkkkkkkkkkkkkkkkkkkkkkkklddddddddddddddddddd;aaaaaaaaaaaaaaaaaaaaaakdddddddddddddddddddddddddddddddddpukwejg,hdpcfvoi0uqlkwehgvndpwuio");
		dbServ.addDocumentSubSection(doc);
		
		List<DocumentSubSectionImpl> docs = dbServ.getAllDocumentSubSections();
		
		assertNotNull("The retrived sub-sections should not be null", docs);
	}
	
	@Test
	public void testSaveCoLabRoom(){
		CoLabSave room = new CoLabSave();
		room.setAdmin("admin");
		room.setRoomName("room1");
		
		DocumentSubSectionImpl sec1 = new DocumentSubSectionImpl();
		sec1.setName("sec1");
		sec1.setText("lkdjcaejflksdjhao;wehgJKhgdaweghaeifvalglkajhwlregvai ae   laejkhflkawhefauoiewn");
		
		DocumentSubSectionImpl sec2 = new DocumentSubSectionImpl();
		sec2.setName("sec2");
		sec2.setText("ULTRA LONG RANDOM TEXT: aslkfdja;lkvnqoierhga;shrvf;lhawelkrvnbaklwehgrlkabv jknbarek;lgbha;hfvd;alkhgk;lahs;lgdhalkhfvdioa;hreg;vahvdlkhaelkrgalhvdajkhrelvkhbzkdvbajkghadbhvkaljbdvklashijkvdhakdjvbakj6bvlaejkwhrfvlujhagdlkahfkweulfvhakgefaldkcbweluvcahbs8ghajkdhfaklwebfasdvjkawejrvkgawkjevbauebvjkagsjfaejkvbaklwebvajk6dehvfkjawehfkabs8l6ufcablkwevblajkhvklaewhvfkjahsevklahbskvlhakehvawebvjkabewlkvjbablvdajkblkjabvd");
		
		DocumentSubSectionImpl sec3 = new DocumentSubSectionImpl();
		sec3.setName("sec3");
		sec3.setText("HEllo and cwellcome to the best application in the entire world...way better that anything you could ever create!");
		
		List<SectionizedDocumentImpl> docs = new ArrayList<SectionizedDocumentImpl>();
		
		SectionizedDocumentImpl doc1 = new SectionizedDocumentImpl("javaClass1");
		doc1.addSubSection(sec1, 0);
		
		SectionizedDocumentImpl doc2 = new SectionizedDocumentImpl("javaClass2");
		doc2.addSubSection(sec1, 0);
		doc2.addSubSection(sec2, 1);
		
		SectionizedDocumentImpl doc3 = new SectionizedDocumentImpl("javaClass3");
		doc3.addSubSection(sec1, 0);
		doc3.addSubSection(sec2, 1);
		doc3.addSubSection(sec3, 2);
		
		docs.add(doc1);
		docs.add(doc2);
		docs.add(doc3);

		room.setDocuments(docs);
		
		DocumentDBService serv = new DocumentDBService();
		
		serv.saveCoLabRoom(room);
		
		List<CoLabSave> rets = serv.getAllRoomsForUser("admin");
		
		assertNotNull("", rets);
	}
}
