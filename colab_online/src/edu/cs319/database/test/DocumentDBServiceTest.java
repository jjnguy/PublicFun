package edu.cs319.database.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Test;

import edu.cs319.database.DocumentDBService;
import edu.cs319.dataobjects.database.DBCoLabRoom;
import edu.cs319.dataobjects.database.DBDocumentSubSection;
import edu.cs319.dataobjects.database.SectionizedDBDocument;

public class DocumentDBServiceTest extends TestCase {

	@Test
	public void testAddDocumentSubSection(){
		DocumentDBService dbServ = new DocumentDBService();
		DBDocumentSubSection doc = new DBDocumentSubSection();
		doc.setName("subSection2");
		doc.setText("hd;eoulvdkhc;laksoyhfdp9-oiue;jkv badfpigkdfjdjkffffffffffffffffffffffffffffffffffffffffffffffffkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkowfieeiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiijkcdckkkkkkkkkkkkkkkkkkkkkkkkkkkkkkklddddddddddddddddddd;aaaaaaaaaaaaaaaaaaaaaakdddddddddddddddddddddddddddddddddpukwejg,hdpcfvoi0uqlkwehgvndpwuio");
		dbServ.saveDBDocumentSubSection(doc);
		
		List<DBDocumentSubSection> docs = dbServ.getAllDocumentSubSections();
		
		assertNotNull("The retrived sub-sections should not be null", docs);
	}
	
	public void testSaveSectionDocument(){
		DBDocumentSubSection sec1 = new DBDocumentSubSection();
		sec1.setName("sec1");
		sec1.setText("lkdjcaejflksdjhao;wehgJKhgdaweghaeifvalglkajhwlregvai ae   laejkhflkawhefauoiewn");
		
		DBDocumentSubSection sec2 = new DBDocumentSubSection();
		sec2.setName("sec2");
		sec2.setText("ULTRA LONG RANDOM TEXT: aslkfdja;lkvnqoierhga;shrvf;lhawelkrvnbaklwehgrlkabv jknbarek;lgbha;hfvd;alkhgk;lahs;lgdhalkhfvdioa;hreg;vahvdlkhaelkrgalhvdajkhrelvkhbzkdvbajkghadbhvkaljbdvklashijkvdhakdjvbakj6bvlaejkwhrfvlujhagdlkahfkweulfvhakgefaldkcbweluvcahbs8ghajkdhfaklwebfasdvjkawejrvkgawkjevbauebvjkagsjfaejkvbaklwebvajk6dehvfkjawehfkabs8l6ufcablkwevblajkhvklaewhvfkjahsevklahbskvlhakehvawebvjkabewlkvjbablvdajkblkjabvd");
		
		DBDocumentSubSection sec3 = new DBDocumentSubSection();
		sec3.setName("sec3");
		sec3.setText("HEllo and cwellcome to the best application in the entire world...way better that anything you could ever create!");
		
		SectionizedDBDocument doc3 = new SectionizedDBDocument();
		doc3.setName("hibernateIsAwesomerAndCooler");
		doc3.addSubSection(sec1, 0);
		doc3.addSubSection(sec2, 1);
		doc3.addSubSection(sec3, 2);
		
		DocumentDBService dbService = new DocumentDBService();
		dbService.saveDBSectionizedDocument(doc3);
		
		List<SectionizedDBDocument> ret = dbService.getAllSectionizedDocuments();
		
		assertNotNull("", ret);
	}
	
	@Test
	public void testSaveCoLabRoom(){
		DBCoLabRoom room = new DBCoLabRoom();
		room.setAdmin("admin4");
		room.setRoomname("room36");
		
		DBDocumentSubSection sec4 = new DBDocumentSubSection();
		sec4.setName("sec4");
		sec4.setText("lkdjcaejflksdjhao;wehgJKhgdaweghaeifvalglkajhwlregvai ae   laejkhflkawhefauoiewn");
		
		DBDocumentSubSection sec5 = new DBDocumentSubSection();
		sec5.setName("sec5");
		sec5.setText("ULTRA LONG RANDOM TEXT: aslkfdja;lkvnqoierhga;shrvf;lhawelkrvnbaklwehgrlkabv jknbarek;lgbha;hfvd;alkhgk;lahs;lgdhalkhfvdioa;hreg;vahvdlkhaelkrgalhvdajkhrelvkhbzkdvbajkghadbhvkaljbdvklashijkvdhakdjvbakj6bvlaejkwhrfvlujhagdlkahfkweulfvhakgefaldkcbweluvcahbs8ghajkdhfaklwebfasdvjkawejrvkgawkjevbauebvjkagsjfaejkvbaklwebvajk6dehvfkjawehfkabs8l6ufcablkwevblajkhvklaewhvfkjahsevklahbskvlhakehvawebvjkabewlkvjbablvdajkblkjabvd");
		
		DBDocumentSubSection sec6 = new DBDocumentSubSection();
		sec6.setName("sec6");
		sec6.setText("HEllo and cwellcome to the best application in the entire world...way better that anything you could ever create!");
		
		SectionizedDBDocument doc3 = new SectionizedDBDocument();
		doc3.setName("hibernateIsAwesomerAndCooler1234");
		doc3.addSubSection(sec4, 0);
		doc3.addSubSection(sec5, 1);
		doc3.addSubSection(sec6, 2);
		
		Set<SectionizedDBDocument> docs = new HashSet<SectionizedDBDocument>();
		
		SectionizedDBDocument doc1 = new SectionizedDBDocument("javaClass199999");
		doc1.addSubSection(sec4, 0);
		
		SectionizedDBDocument doc2 = new SectionizedDBDocument("javaClass2324");
		doc2.addSubSection(sec4, 0);
		doc2.addSubSection(sec5, 1);
		
		docs.add(doc1);
		docs.add(doc2);
		docs.add(doc3);

		room.setDocuments(docs);
		
		DocumentDBService serv = new DocumentDBService();
		
		serv.saveCoLabRoom(room);
		
		List<DBCoLabRoom> rets = serv.getAllRoomsForUser("admin4");
		
		assertNotNull("", rets);
	}
}
