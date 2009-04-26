package edu.cs319.database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import edu.cs319.dataobjects.CoLabRoom;
import edu.cs319.dataobjects.CoLabRoomMember;
import edu.cs319.dataobjects.SectionizedDocument;
import edu.cs319.dataobjects.database.DBCoLabRoom;
import edu.cs319.dataobjects.database.DBDocumentSubSection;
import edu.cs319.dataobjects.database.SectionizedDBDocument;
import edu.cs319.dataobjects.impl.DocumentSubSectionImpl;
import edu.cs319.dataobjects.impl.SectionizedDocumentImpl;

public class DocumentDatabaseUtil {

	public static void saveCoLab(String admin, CoLabRoom room) {
		List<SectionizedDocument> docs = room.getAllDocuments();
		DBCoLabRoom dbRoom = new DBCoLabRoom();
		dbRoom.setAdmin(admin);
		Set<SectionizedDBDocument> setDocs = new HashSet<SectionizedDBDocument>();
		SectionizedDBDocument nDoc;

		for (SectionizedDocument d : docs) {
			nDoc = new SectionizedDBDocument();
			nDoc.setName(d.getName());
			nDoc.addAllSubSections(d.getAllSubSections());

			setDocs.add(nDoc);
		}

		dbRoom.setDocuments(setDocs);
		dbRoom.setRoomname(room.roomName());

		Session session = HibernateUtil.getSession();

		try{
			session.save(dbRoom);
			session.flush();
			session.close();
		}
		catch(ConstraintViolationException e){
			session.close();
			System.err.println("Roomname is already persisted in the Database");
		}
	}

	public static List<String> getRoomNames(String user) {
		Session session = HibernateUtil.getSession();
		Criteria criteria = session.createCriteria(DBCoLabRoom.class);

		criteria.add(Restrictions.eq("admin", user));
		
		List<DBCoLabRoom> shittyRooms = criteria.list();
		
		Set<DBCoLabRoom> rooms = new HashSet<DBCoLabRoom>(shittyRooms);
		
		List<String> roomNames = new ArrayList<String>();
		
		for (DBCoLabRoom room : rooms) {
			roomNames.add(room.getRoomname());
		}
		
		session.close();

		return roomNames;
	}

	public static CoLabRoom getCoLabRoom(String roomName) {
		Session session = HibernateUtil.getSession();
		Criteria criteria = session.createCriteria(DBCoLabRoom.class);

		criteria.add(Restrictions.eq("roomname", roomName));
		
		CoLabRoom newRoom = null;
		List<CoLabRoom> retRooms;

		DBCoLabRoom dbRoom = (DBCoLabRoom) criteria.uniqueResult();

		if (dbRoom != null) {
			Set<SectionizedDBDocument> dbDocs;
			SectionizedDocument doc;
			List<DocumentSubSectionImpl> subDocs;
			DocumentSubSectionImpl subDoc;
			newRoom = new CoLabRoom(dbRoom.getRoomname(), new CoLabRoomMember(dbRoom.getAdmin(),
					null));
			dbDocs = dbRoom.getDocuments();

			// For each document in the room, rebuild it
			for (SectionizedDBDocument d : dbDocs) {
				doc = new SectionizedDocumentImpl(d.getName());

				// Add all the sub documents to the sectionized document
				subDocs = new ArrayList<DocumentSubSectionImpl>();
				for (DBDocumentSubSection dss : d.getSubSections()) {
					subDoc = new DocumentSubSectionImpl();
					subDoc.setName(dss.getName());
					subDoc.setText(dss.getText());
					subDocs.add(subDoc);
				}

				// Add the list of document sub sections to the new Sectionized document
				doc.addAllSubSections(subDocs);

				newRoom.addDocument(doc);
			}
			// Add the list of document sub sections to the new Sectionized document

			session.delete(dbRoom);
			session.flush();
			session.close();
		}

		return newRoom;

	}
}
