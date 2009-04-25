package edu.cs319.database;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import edu.cs319.dataobjects.DocumentSubSection;
import edu.cs319.dataobjects.SectionizedDocument;
import edu.cs319.dataobjects.database.DBDocumentSubSection;
import edu.cs319.dataobjects.database.SectionizedDBDocument;

public class DocumentDBService {
	
	/**
	 * Will return all of the CoLabSave objects for the specified username or
	 * an empty list if none exist.
	 * 
	 * @param username
	 * @return CoLabSave objects for a username
	 */
	@SuppressWarnings("unchecked")
	public List<CoLabSave> getAllRoomsForUser(String username){
		Session session = HibernateUtil.getSession();
		Criteria criteria = session.createCriteria(CoLabSave.class);
		
		criteria.add(Restrictions.eq("admin", username));
		
		List ret = criteria.list();
		
		if(ret == null){
			ret = new ArrayList<CoLabSave>();
		}
		
		return ret;
	}
	
	public void updateCoLabRoom(CoLabSave room){
		Session session = HibernateUtil.getSession();
		
		session.update(room);
		
		session.flush();
		session.close();
	}

	public void saveDBSectionizedDocument(SectionizedDBDocument sd){
		Session session = HibernateUtil.getSession();
		
		session.save(sd);
		
		session.flush();
		session.close();
	}
	
	public void saveSectionizedDocument(SectionizedDocument sd){
		SectionizedDBDocument newSd = new SectionizedDBDocument();
		
		List<DocumentSubSection> sections = sd.getAllSubSections();
		List<DBDocumentSubSection> dbSections = new ArrayList<DBDocumentSubSection>();
		
		DBDocumentSubSection dbsec;
		for(DocumentSubSection sec : sections){
			dbsec = new DBDocumentSubSection();
			dbsec.setName(sec.getName());
			dbsec.setText(sec.getText());
			
			dbSections.add(dbsec);
		}
		
		newSd.setSubSections(dbSections);
		
		newSd.setName(sd.getName());
		
		Session session = HibernateUtil.getSession();
		
		session.save(newSd);
		
		session.flush();
		session.close();
	}
	
	@SuppressWarnings("unchecked")
	public List<SectionizedDBDocument> getAllSectionizedDocuments(){
		Session session = HibernateUtil.getSession();
		Criteria criteria = session.createCriteria(SectionizedDBDocument.class);
		
		List<SectionizedDBDocument> ret = criteria.list();
		
		return ret;
	}
	
	public void saveCoLabRoom(CoLabSave room){
		Session session = HibernateUtil.getSession();
		
		session.save(room);
		
		session.flush();
		session.close();
	}
	
	public void deleteAllRoomsForUser(String username){
		Session session = HibernateUtil.getSession();
		Criteria criteria = session.createCriteria(CoLabSave.class);
		
		criteria.add(Restrictions.eq("admin", username));
		
		List<CoLabSave> rooms = criteria.list();
		
		for(CoLabSave room : rooms){
			session.delete(room);
		}
		
		session.flush();
		session.close();
	}
	
	public void deleteCoLabRoom(CoLabSave room){
		Session session = HibernateUtil.getSession();
		session.delete(room);
		
		session.flush();
		session.close();
	}
	
	public void saveDBDocumentSubSection(DBDocumentSubSection sec){
		Session session = HibernateUtil.getSession();
		
		session.save(sec);
		
		session.flush();
		session.close();
	}

	public void saveDocumentSubSection(DocumentSubSection doc){
		Session session = HibernateUtil.getSession();
		
		DBDocumentSubSection dbsec = new DBDocumentSubSection();
		dbsec.setName(doc.getName());
		dbsec.setText(doc.getText());
		
		session.save(dbsec);
		
		session.flush();
		session.close();
	}
	
	@SuppressWarnings("unchecked")
	public List<DBDocumentSubSection> getAllDocumentSubSections(){
		Session session = HibernateUtil.getSession();
		Criteria criteria = session.createCriteria(DBDocumentSubSection.class);
		
		return criteria.list();
		
	}
	
}
